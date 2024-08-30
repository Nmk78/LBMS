package Book;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/getBorrowedBooks")
public class getBorrowedBooks extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String memberId = request.getParameter("memberId");

        if (memberId == null || memberId.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            // Query to get borrowed books details from the loan table
            String query = "SELECT b.*, l.dueDate, l.loanDate FROM book b " +
                           "JOIN loan l ON b.bid = l.bookid WHERE l.memberId = ?";

            stmt = conn.prepareStatement(query);
            stmt.setString(1, memberId); // Set member ID parameter

            rs = stmt.executeQuery();

            while (rs.next()) {
                BorrowedBook book = new BorrowedBook();
                book.setBid(rs.getInt("bid"));
                book.setTitle(rs.getString("title"));
                book.setCategory(rs.getString("category"));
                book.setAuthorName(rs.getString("authorName"));
                book.setBookShelf(rs.getInt("bookShelf"));
                book.setCopiesAvailable(rs.getInt("copiesAvailable"));
                book.setAcquireBy(rs.getString("acquireBy"));
                byte[] imageBytes = rs.getBytes("image");
                String image = "";
                if (imageBytes != null && imageBytes.length > 0) {
                    image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
                } else {
                    image = "./assets/img/defaultCover.png"; // Default image URL
                }
                book.setImage(image);
                // Add loan details
                book.setDueDate(rs.getDate("dueDate"));
                book.setBorrowedDate(rs.getDate("loanDate"));
                
                borrowedBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (Exception e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        String json = new Gson().toJson(borrowedBooks);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}
