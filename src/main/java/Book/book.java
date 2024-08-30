package Book;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

//@WebServlet("/book")
//public class book extends HttpServlet {
//
//    private static final long serialVersionUID = 1L;
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
//    private static final String USER = "root";
//    private static final String PASS = "root";
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String bidParam = request.getParameter("id");
//        PrintWriter out = response.getWriter();
//
//
//        if (bidParam == null || bidParam.trim().isEmpty()) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid book ID.");
//            return;
//        }
//
//        int bookId;
//        try {
//            bookId = Integer.parseInt(bidParam);
//        } catch (NumberFormatException e) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID format.");
//            return;
//        }
//
////        // Retrieve the member ID from the session
////        HttpSession session = request.getSession();
////        Integer memberId = (Integer) session.getAttribute("idOrDept");
////        if (memberId == null) {
////            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Member not logged in.");
////            return;
////        }
////        System.out.println(memberId);
//
//
//        response.setContentType("application/json");
//
//        JsonObject bookJson = new JsonObject();
//        JsonArray reviewsArray = new JsonArray();
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//             PreparedStatement pstmt = conn.prepareStatement("SELECT Bid, Title, bookShelf, category, Image, AuthorName, CopiesAvailable FROM book WHERE Bid = ?")) {
//
//            pstmt.setInt(1, bookId);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    // Get Book Details
//                    bookJson.addProperty("id", rs.getInt("Bid"));
//                    bookJson.addProperty("title", rs.getString("Title"));
//                    bookJson.addProperty("author", rs.getString("AuthorName"));
//                    bookJson.addProperty("category", rs.getString("category"));
//                    bookJson.addProperty("bookShelf", rs.getInt("bookShelf"));
//
//                    // Handle image encoding
//                    byte[] imageBytes = rs.getBytes("Image");
//                    String image = "";
//                    if (imageBytes != null && imageBytes.length > 0) {
//                        image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
//                    } else {
//                        image = "./assets/img/defaultCover.png"; // Default image URL
//                    }
//                    bookJson.addProperty("image", image);
//
//                    int copiesAvailable = rs.getInt("CopiesAvailable");
//                    bookJson.addProperty("copiesAvailable", copiesAvailable);
//
//                    if (copiesAvailable > 0) {
//                        bookJson.addProperty("availability", "Available");
//                    } else {
//                        Date nearestReturnDate = getNearestReturnDate(conn, bookId);
//                        bookJson.addProperty("availability", "Nearest return date: " + (nearestReturnDate != null ? nearestReturnDate.toString() : "N/A"));
//                    }
//
//                    // Query to fetch review details
//                    String reviewsQuery = "SELECT r.ReviewContent, r.CreatedAt, m.name AS reviewerName " +
//                                          "FROM review r " +
//                                          "JOIN member m ON r.MemberId = m.idOrDept " +
//                                          "WHERE r.BookId = ? " +
//                                          "ORDER BY r.CreatedAt DESC";
//
//                    try (PreparedStatement reviewStmt = conn.prepareStatement(reviewsQuery)) {
//                        reviewStmt.setInt(1, bookId);
//                        
//                        // Debugging output
//                        System.out.println("Executing query: " + reviewsQuery + " with BookId: " + bookId);
//
//                        try (ResultSet rwRs = reviewStmt.executeQuery()) {
//                            boolean hasResults = false;
//                            while (rwRs.next()) {
//                                hasResults = true;
//                                JsonObject reviewJson = new JsonObject();
//                                reviewJson.addProperty("reviewerName", rwRs.getString("reviewerName"));
//                                reviewJson.addProperty("reviewContent", rwRs.getString("ReviewContent"));
//                                reviewJson.addProperty("createdAt", rwRs.getString("CreatedAt"));
//                                reviewsArray.add(reviewJson);
//                            }
//                            if (!hasResults) {
//                                System.out.println("No reviews found for BookId: " + bookId);
//                            }
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//
//                    // Get the Average Rating for the Book
//                    try (PreparedStatement ratingStmt = conn.prepareStatement("SELECT AVG(RatingValue) AS averageRating FROM Rating WHERE bookId = ?")) {
//                        ratingStmt.setInt(1, bookId);
//                        try (ResultSet rtRs = ratingStmt.executeQuery()) {
//                            if (rtRs.next()) {
//                                double averageRating = rtRs.getDouble("averageRating");
//                                bookJson.addProperty("averageRating", averageRating);
//                            } else {
//                                bookJson.addProperty("averageRating", "No ratings available");
//                            }
//                        }
//                    }
//
//                } else {
//                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found.");
//                    return;
//                }
//                
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
//            return;
//        }
//
//        // Add reviews to book JSON
//        bookJson.add("reviews", reviewsArray);
//
//        // Convert to JSON string and forward to JSP
//        Gson gson = new Gson();
//        String jsonResponse = gson.toJson(bookJson);
//        request.setAttribute("book", jsonResponse);
//        request.getRequestDispatcher("book.jsp?id="+bidParam).forward(request, response);
//    }
//
//
//    private Date getNearestReturnDate(Connection conn, int bookId) throws Exception {
//        Date nearestReturnDate = null;
//        String query = "SELECT MIN(dueDate) AS nearestReturnDate FROM loan WHERE bookid = ? AND returnDate IS NOT NULL AND returnDate > CURDATE()";
//        
//        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setInt(1, bookId);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    nearestReturnDate = rs.getDate("nearestReturnDate");
//                }
//            }
//        }
//        System.out.println("nearestReturnDate "+nearestReturnDate);
//        return nearestReturnDate;
//    }
//}

@WebServlet("/book")
public class book extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
    private static final String USER = "root";
    private static final String PASS = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bidParam = request.getParameter("id");
        String userId = request.getParameter("user");  // Get userId from request
        PrintWriter out = response.getWriter();

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

        JsonObject bookJson = new JsonObject();
        JsonArray reviewsArray = new JsonArray();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {

            // Retrieve book details
            String bookQuery = "SELECT Bid, Title, bookShelf, category, Image, AuthorName, CopiesAvailable FROM book WHERE Bid = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(bookQuery)) {
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
                            bookJson.addProperty("availability", "Nearest return date: " + (nearestReturnDate != null ? nearestReturnDate.toString() : "N/A"));
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found.");
                        return;
                    }
                }
            }

            // Fetch reviews
            String reviewsQuery = "SELECT r.ReviewContent, r.CreatedAt, m.name AS reviewerName " +
                                  "FROM review r " +
                                  "JOIN member m ON r.MemberId = m.idOrDept " +
                                  "WHERE r.BookId = ? " +
                                  "ORDER BY r.CreatedAt DESC";
            try (PreparedStatement reviewStmt = conn.prepareStatement(reviewsQuery)) {
                reviewStmt.setInt(1, bookId);
                try (ResultSet rwRs = reviewStmt.executeQuery()) {
                    while (rwRs.next()) {
                        JsonObject reviewJson = new JsonObject();
                        reviewJson.addProperty("reviewerName", rwRs.getString("reviewerName"));
                        reviewJson.addProperty("reviewContent", rwRs.getString("ReviewContent"));
                        reviewJson.addProperty("createdAt", rwRs.getString("CreatedAt"));
                        reviewsArray.add(reviewJson);
                    }
                }
            }

            // Get the average rating
            try (PreparedStatement ratingStmt = conn.prepareStatement("SELECT AVG(RatingValue) AS averageRating FROM Rating WHERE bookId = ?")) {
                ratingStmt.setInt(1, bookId);
                try (ResultSet rtRs = ratingStmt.executeQuery()) {
                    if (rtRs.next()) {
                        double averageRating = rtRs.getDouble("averageRating");
                        bookJson.addProperty("averageRating", averageRating);
                    } else {
                        bookJson.addProperty("averageRating", "No ratings available");
                    }
                }
            }

            // Get the user's rating
            try (PreparedStatement ratingStmt = conn.prepareStatement("SELECT RatingValue FROM Rating WHERE bookId = ? AND memberId = ?")) {
                ratingStmt.setInt(1, bookId);
                ratingStmt.setString(2, userId);
                try (ResultSet rtRs = ratingStmt.executeQuery()) {
                    if (rtRs.next()) {
                        double averageRating = rtRs.getDouble("RatingValue");
                        bookJson.addProperty("userRating", averageRating);
                    } else {
                        bookJson.addProperty("userRating", 0.0);
                    }
                }
            }

         // Log the IDs being used for the query
            System.out.println("Checking reservation status for bookId: " + bookId + " and memberId: " + userId);

            // Check reservation status
            if (userId != null) {
                String reservationQuery = "SELECT status FROM reservation WHERE bookId = ? AND memberId = ?";
                try (PreparedStatement reservationStmt = conn.prepareStatement(reservationQuery)) {
                    reservationStmt.setInt(1, bookId);
                    reservationStmt.setString(2, userId);
                    try (ResultSet resRs = reservationStmt.executeQuery()) {
                        if (resRs.next()) {
                            String reservationStatus = resRs.getString("status");
                            System.out.println("Status found: " + reservationStatus);
                            bookJson.addProperty("reservationStatus", reservationStatus);
                        } else {
                            System.out.println("No reservation found for this user and book.");
                            bookJson.addProperty("reservationStatus", "Not Reserved");
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
            return;
        }

        // Add reviews to book JSON
        bookJson.add("reviews", reviewsArray);

        // Convert to JSON string and forward to JSP
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(bookJson);
        request.setAttribute("book", jsonResponse);
        request.getRequestDispatcher("book.jsp?id="+bidParam).forward(request, response);
    }

    private Date getNearestReturnDate(Connection conn, int bookId) throws SQLException {
        Date nearestReturnDate = null;
        String query = "SELECT MIN(dueDate) AS nearestReturnDate FROM loan WHERE bookid = ? AND returnDate IS NOT NULL AND returnDate > CURDATE()";
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

