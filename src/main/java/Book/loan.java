package Book;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/loan")
public class loan extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/lbms";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            listLoans(request, response);
        } else if ("view".equals(action)) {
            viewLoan(request, response);
        } else {
            sendError(response, "Unspecified action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addLoan(request, response);
        } else if ("update".equals(action)) {
            updateLoan(request, response);
        } else if ("delete".equals(action)) {
            deleteLoan(request, response);
        } else {
            sendError(response, "Unspecified action");
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    private void listLoans(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String query = "SELECT l.*, b.title as bookTitle, m.name as memberName " +
                "FROM loan l " +
                "JOIN book b ON l.bookid = b.Bid " +
                "JOIN member m ON l.memberid = m.idOrDept " +
                "WHERE l.status <> 'clear'";


        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            List<LoanClass> loans = new ArrayList<>();
            while (rs.next()) {
                LoanClass loan = new LoanClass(
                    rs.getInt("id"),
                    rs.getInt("bookid"),
                    rs.getString("memberid"),
                    rs.getDate("loanDate"),
                    rs.getDate("returnDate"),
                    rs.getDate("dueDate"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getString("bookTitle"), // Add book title
                    rs.getString("memberName") // Add member name
                );
                loans.add(loan);
            }

            String loansJson = gson.toJson(loans);
            sendJsonResponse(response, loansJson);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void viewLoan(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int loanId = Integer.parseInt(request.getParameter("loanId"));
        String query = "SELECT l.*, b.title as bookTitle, m.name as memberName " +
                       "FROM loan l " +
                       "JOIN book b ON l.bookid = b.Bid " +
                       "JOIN member m ON l.memberid = m.idOrDept " +
                       "WHERE l.id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, loanId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LoanClass loan = new LoanClass(
                        rs.getInt("id"),
                        rs.getInt("bookid"),
                        rs.getString("memberid"),
                        rs.getDate("loanDate"),
                        rs.getDate("returnDate"),
                        rs.getDate("dueDate"),
                        rs.getString("status"),
                        rs.getString("notes"),
                        rs.getString("bookTitle"), // Add book title
                        rs.getString("memberName") // Add member name
                    );

                    String loanJson = gson.toJson(loan);
                    sendJsonResponse(response, loanJson);
                } else {
                    sendError(response, "Loan not found");
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void addLoan(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("bookid"));
        String memberId = request.getParameter("memberid");
        java.sql.Date loanDate = java.sql.Date.valueOf(request.getParameter("loanDate"));
        java.sql.Date dueDate = java.sql.Date.valueOf(request.getParameter("dueDate"));

        String checkBlacklistQuery = "SELECT blacklist FROM member WHERE idOrDept = ?";
        String checkCopiesQuery = "SELECT copyAvailable FROM book WHERE Bid = ?";
        String checkBorrowedBooksQuery = "SELECT COUNT(*) AS borrowedCount FROM loan WHERE memberid = ? AND returnDate IS NULL";
        String insertLoanQuery = "INSERT INTO loan (bookid, memberid, loanDate, dueDate) VALUES (?, ?, ?, ?)";
        String updateCopiesQuery = "UPDATE book SET copyAvailable = copyAvailable - 1 WHERE Bid = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // Check if the member is blacklisted
            try (PreparedStatement checkBlacklistStmt = conn.prepareStatement(checkBlacklistQuery)) {
                checkBlacklistStmt.setString(1, memberId);
                try (ResultSet rs = checkBlacklistStmt.executeQuery()) {
                    if (rs.next()) {
                        String blacklistStatus = rs.getString("blacklist");
                        if ("permanent".equals(blacklistStatus)) {
                        	sendError(response, "Member is blacklisted as permanent and cannot borrow books.");
                            return;
                        }else if ("temporary".equals(blacklistStatus)) {
                        	sendError(response, "Member is blacklisted as temporarily.");
                            return;
                        }
                    }
                }
            }

            // Check if the book has available copies
            try (PreparedStatement checkCopiesStmt = conn.prepareStatement(checkCopiesQuery)) {
                checkCopiesStmt.setInt(1, bookId);
                try (ResultSet rs = checkCopiesStmt.executeQuery()) {
                    if (rs.next()) {
                        int copyAvailable = rs.getInt("copyAvailable");
                        if (copyAvailable <= 0) {
                            sendError(response, "No copies available for this book.");
                            return;
                        }
                    }
                }
            }

            // Check if the member has already borrowed 2 books
            try (PreparedStatement checkBorrowedBooksStmt = conn.prepareStatement(checkBorrowedBooksQuery)) {
                checkBorrowedBooksStmt.setString(1, memberId);
                try (ResultSet rs = checkBorrowedBooksStmt.executeQuery()) {
                    if (rs.next()) {
                        int borrowedCount = rs.getInt("borrowedCount");
                        if (borrowedCount >= 2) {
                        	sendError(response, "Member has already borrowed 2 books. Return a book to borrow another.");
                            return;
                        }
                    }
                }
            }

            // Insert the loan record
            try (PreparedStatement insertLoanStmt = conn.prepareStatement(insertLoanQuery)) {
                insertLoanStmt.setInt(1, bookId);
                insertLoanStmt.setString(2, memberId);
                insertLoanStmt.setDate(3, loanDate);
                insertLoanStmt.setDate(4, dueDate);
                insertLoanStmt.executeUpdate();
            }

            // Update the book's available copies
            try (PreparedStatement updateCopiesStmt = conn.prepareStatement(updateCopiesQuery)) {
                updateCopiesStmt.setInt(1, bookId);
                updateCopiesStmt.executeUpdate();
            }

            conn.commit(); // Commit the transaction
            sendSuccessResponse(response, "Loan added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error", e);
        }
    }



    private void updateLoan(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int loanId = Integer.parseInt(request.getParameter("loanId"));
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        String memberId = request.getParameter("memberId");
        java.sql.Date loanDate = java.sql.Date.valueOf(request.getParameter("loanDate"));
        java.sql.Date returnDate = request.getParameter("returnDate") != null ?
            java.sql.Date.valueOf(request.getParameter("returnDate")) : null;
        java.sql.Date dueDate = java.sql.Date.valueOf(request.getParameter("dueDate"));
        String status = request.getParameter("status");
        String notes = request.getParameter("notes");

        String query = "UPDATE loan SET bookid = ?, memberid = ?, loanDate = ?, returnDate = ?, dueDate = ?, status = ?, notes = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            pstmt.setString(2, memberId);
            pstmt.setDate(3, loanDate);
            pstmt.setDate(4, returnDate);
            pstmt.setDate(5, dueDate);
            pstmt.setString(6, status);
            pstmt.setString(7, notes);
            pstmt.setInt(8, loanId);
            pstmt.executeUpdate();

            sendSuccessResponse(response, "Loan updated successfully");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void deleteLoan(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int loanId = Integer.parseInt(request.getParameter("loanId"));
        String query = "DELETE FROM loan WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, loanId);
            pstmt.executeUpdate();

            sendSuccessResponse(response, "Loan deleted successfully");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json);
            out.flush();
        }
    }

    private void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print("{\"success\": true, \"message\": \"" + message + "\"}");
            out.flush();
        }
    }

    private void sendError(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print("{\"success\": false, \"error\": \"" + errorMessage + "\"}");
            out.flush();
        }
    }
}
