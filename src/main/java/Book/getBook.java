//package Book;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//
//@WebServlet("/getBook")
//public class getBook extends HttpServlet {
//
//    private static final long serialVersionUID = 1L;
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
//    private static final String USER = "root";
//    private static final String PASS = "root";
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String bidParam = request.getParameter("id");
//        
//        if (bidParam == null || bidParam.trim().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid book ID.");
//            return; // Ensure that no further processing occurs
//        }
//        
//        int bookId;
//        try {
//            bookId = Integer.parseInt(bidParam);
//        } catch (NumberFormatException e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID format.");
//            return; // Ensure that no further processing occurs
//        }
//
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            conn = DriverManager.getConnection(DB_URL, USER, PASS);
//            String sql = "SELECT Bid, Title, bookShelf, category, Image, AuthorName, CopiesAvailable FROM book WHERE Bid = ?";
//            pstmt = conn.prepareStatement(sql);
//            pstmt.setInt(1, bookId);
//            rs = pstmt.executeQuery();
//
//
//            if (rs.next()) {
//            	//Get Reviews if book was exits
//                String query = "SELECT ReviewerName, ReviewContent, CreatedAt FROM Review WHERE BookId = ?";
//                PreparedStatement stmt = conn.prepareStatement(query);
//                stmt.setInt(1, bookId);
//                ResultSet RWrs = stmt.executeQuery();
//
//                List<Map<String, String>> reviews = new ArrayList();
//                while (RWrs.next()) {
//                    Map<String, String> review = new HashMap();
//                    review.put("ReviewerName", rs.getString("ReviewerName"));
//                    review.put("ReviewContent", rs.getString("ReviewContent"));
//                    review.put("CreatedAt", rs.getString("CreatedAt").toString());
//                    reviews.add(review);
//                }
//                
//            	
//                Book book = new Book();
//                book.setBid(rs.getInt("Bid"));
//                book.setTitle(rs.getString("Title"));
//                book.setAuthorName(rs.getString("AuthorName"));
//                book.setCategory(rs.getString("category"));
//                book.setBookShelf(rs.getInt("BookShelf"));
//
////                Blob imageBlob = rs.getBlob("Image");
////                if (imageBlob == null) {
////                    imageBlob = getDefaultImageBlob(conn);
////                    book.setImage(imageBlob);
////
////                }
//
//                // Check if image is null or empty and set default if necessary
//                String image = rs.getString("Image");
//                if (image == null || image.isEmpty()) {
//                    image = "./assets/img/defaultCover.png";
//                }
//                
//                int copiesAvailable = rs.getInt("CopiesAvailable");
//                book.setCopiesAvailable(copiesAvailable);
//
//                if (copiesAvailable > 0) {
//                    book.setAvailability("Available");
//                } else {
//                    Date nearestReturnDate = getNearestReturnDate(conn, book.getBid());
//                    book.setAvailability("Not Available, nearest return date: " + nearestReturnDate);
//                }
//
//                System.out.print(book);
//                request.setAttribute("reviews", reviews);
//                request.setAttribute("book", book);
//                request.getRequestDispatcher("/book.jsp").forward(request, response);
//            } else {
//                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found.");
//                return; // Ensure that no further processing occurs
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
//            return; // Ensure that no further processing occurs
//        } finally {
//            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
//            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
//            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
//        }
//
//        request.getRequestDispatcher("book.jsp").forward(request, response);
//    }
//
//    public static Blob getDefaultImageBlob(Connection conn) {
//        Blob defaultImageBlob = null;
//        try {
//            // Path to the default image file
//            String defaultImagePath = "./assets/img/defaultCover.png";
//            System.out.println("Loading default image from path: " + defaultImagePath);
//            
//            // Read the image file into an InputStream
//            InputStream inputStream = new FileInputStream(defaultImagePath);
//            
//            // Convert InputStream to Blob
//            defaultImageBlob = conn.createBlob();
//            byte[] bytes = inputStream.readAllBytes();
//            defaultImageBlob.setBytes(1, bytes);
//            
//            System.out.println("Default image loaded successfully.");
//        } catch (Exception e) {
//            e.printStackTrace(); // Handle exception appropriately
//        }
//        return defaultImageBlob;
//    }
//
//
//    private Date getNearestReturnDate(Connection conn, int bookId) throws Exception {
//        Date nearestReturnDate = null;
//        String query = "SELECT MIN(returnDate) AS nearestReturnDate FROM loan WHERE bookid = ? AND returnDate IS NOT NULL AND returnDate > CURDATE()";
//        
//        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setInt(1, bookId);
//            ResultSet rs = pstmt.executeQuery();
//            
//            if (rs.next()) {
//                Date date = rs.getDate("nearestReturnDate");
//                if (date != null) {
//                	nearestReturnDate = date;
//                }
//            }
//            rs.close();
//        }
//        return nearestReturnDate;
//        }
//    }

package Book;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String bidParam = request.getParameter("id");

        if (bidParam == null || bidParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid book ID.");
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(bidParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID format.");
            return;
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        JsonObject bookJson = new JsonObject();
        JsonArray reviewsArray = new JsonArray();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement("SELECT Bid, Title, bookShelf, category, Image, AuthorName, CopiesAvailable FROM book WHERE Bid = ?")) {

            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Get Book Details
                    bookJson.addProperty("id", rs.getInt("Bid"));
                    bookJson.addProperty("title", rs.getString("Title"));
                    bookJson.addProperty("author", rs.getString("AuthorName"));
                    bookJson.addProperty("category", rs.getString("category"));
                    bookJson.addProperty("bookShelf", rs.getInt("bookShelf"));

                    // Handle image encoding
                    byte[] imageBytes = rs.getBytes("Image");
                    String image = "";
                    if (imageBytes != null && imageBytes.length > 0) {
                        image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
                    } else {
                        image = "./assets/img/defaultCover.png"; // Default image URL
                    }
                    bookJson.addProperty("image", image);
                    

                    int copiesAvailable = rs.getInt("CopiesAvailable");
                    bookJson.addProperty("copiesAvailable", copiesAvailable);

                    if (copiesAvailable > 0) {
                        bookJson.addProperty("availability", "Available");
                    } else {
                        Date nearestReturnDate = getNearestReturnDate(conn, bookId);
                        bookJson.addProperty("availability", "Not Available, nearest return date: " + (nearestReturnDate != null ? nearestReturnDate.toString() : "N/A"));
                    }

                    // Get Reviews
                    try (PreparedStatement reviewStmt = conn.prepareStatement("SELECT ReviewerName, ReviewContent, CreatedAt FROM Review WHERE BookId = ?")) {
                        reviewStmt.setInt(1, bookId);
                        try (ResultSet rwRs = reviewStmt.executeQuery()) {
                            while (rwRs.next()) {
                                JsonObject reviewJson = new JsonObject();
                                reviewJson.addProperty("reviewerName", rwRs.getString("ReviewerName"));
                                reviewJson.addProperty("reviewContent", rwRs.getString("ReviewContent"));
                                reviewJson.addProperty("createdAt", rwRs.getString("CreatedAt"));
                                reviewsArray.add(reviewJson);
                            }
                        }
                    }

                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found.");
                    return;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
            return;
        }

        // Add reviews to book JSON
        bookJson.add("reviews", reviewsArray);

        // Convert to JSON string and send response
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(bookJson);
        out.print(jsonResponse);
        out.flush();
    }

    private Date getNearestReturnDate(Connection conn, int bookId) throws Exception {
        Date nearestReturnDate = null;
        String query = "SELECT MIN(returnDate) AS nearestReturnDate FROM loan WHERE bookid = ? AND returnDate IS NOT NULL AND returnDate > CURDATE()";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    nearestReturnDate = rs.getDate("nearestReturnDate");
                }
            }
        }
        return nearestReturnDate;
    }
}
