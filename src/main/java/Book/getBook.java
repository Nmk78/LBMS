package Book;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet("/getBook")
public class getBook extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
    private static final String USER = "root";
    private static final String PASS = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookId = Integer.parseInt(request.getParameter("id")); // Get book ID from request parameter
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "SELECT Bid, Title, bookShelf, category, Image, AuthorName, CopiesAvailable FROM book WHERE Bid = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Book book = new Book();
                book.setBid(rs.getInt("Bid"));
                book.setTitle(rs.getString("Title"));
                book.setAuthorName(rs.getString("AuthorName"));
                
                Blob imageBlob = rs.getBlob("Image");
                if (imageBlob == null) {
                    // Handle default image scenario; store default image in the Blob if needed
                    imageBlob = getDefaultImageBlob();
                }
                book.setImage(imageBlob);

                int copiesAvailable = rs.getInt("CopiesAvailable");
                book.setCopiesAvailable(copiesAvailable);

                if (copiesAvailable > 0) {
                    book.setAvailability("Available");
                } else {
                    // Assuming you need to get the nearest return date if no copies are available
                    Date nearestReturnDate = getNearestReturnDate(conn, book.getBid()); // Adjusted to use getBid()
                    book.setAvailability("Not Available, nearest return date: " + nearestReturnDate);
                }


                request.setAttribute("book", book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("book.jsp");
        dispatcher.forward(request, response);
    }

    private String getNearestReturnDate(Connection conn, int bookId) throws Exception {
        String nearestReturnDate = "N/A";
        String query = "SELECT MIN(returnDate) AS nearestReturnDate FROM loan WHERE bookid = ? AND returnDate IS NOT NULL AND returnDate > CURDATE()";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                nearestReturnDate = rs.getString("nearestReturnDate");
                if (nearestReturnDate == null) {
                    nearestReturnDate = "No upcoming return dates";
                }
            }
            rs.close();
        }
        return nearestReturnDate;
        }
    }