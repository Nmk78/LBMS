//package Book;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Date;
//
//@WebServlet("/reservation")
//public class reservation extends HttpServlet {
//
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
//    private static final String DB_USER = "root";
//    private static final String DB_PASSWORD = "root";
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        
//        if ("create".equalsIgnoreCase(action)) {
//            createReservation(request, response);
//        } else if ("cancel".equalsIgnoreCase(action)) {
//            cancelReservation(request, response);
//        }
//    }
//    
//
//    
//    private void createReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String memberId = request.getParameter("memberId");
//        String bookId = request.getParameter("bookId");
//        Date reservationDate = new Date();
//        String redirectUrl = "/LBMS/book?id=" + bookId;
//
//        // Validate memberId
//        if (memberId == null || memberId.trim().isEmpty()) {
//            String errorMessage = "Member ID cannot be null or empty.";
//            response.sendRedirect(redirectUrl + "&status=" + URLEncoder.encode(errorMessage, "UTF-8"));
//            return;
//        }
//
//        String message;
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            // Check if the book is already reserved by this member
//            String checkSql = "SELECT COUNT(*) FROM reservation WHERE bookId = ? AND memberId = ? AND status = 'Pending'";
//            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
//                checkStmt.setInt(1, Integer.parseInt(bookId));
//                checkStmt.setString(2, memberId);
//
//                ResultSet rs = checkStmt.executeQuery();
//                if (rs.next() && rs.getInt(1) > 0) {
//                    // Reservation already exists
//                    message = "You have already reserved this book.";
//                } else {
//                    // Insert new reservation
//                    String insertSql = "INSERT INTO reservation (memberId, bookId, reservationDate, status) VALUES (?, ?, ?, 'Pending')";
//                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
//                        insertStmt.setString(1, memberId);
//                        insertStmt.setInt(2, Integer.parseInt(bookId));
//                        insertStmt.setDate(3, new java.sql.Date(reservationDate.getTime()));
//
//                        int rowsInserted = insertStmt.executeUpdate();
//                        if (rowsInserted > 0) {
//                            message = "Reservation created successfully.";
//                        } else {
//                            message = "Failed to create reservation.";
//                        }
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            message = "Database access error: " + e.getMessage();
//        }
//
//        // Redirect with a message parameter
//        response.sendRedirect(redirectUrl + "&status=" + URLEncoder.encode(message, "UTF-8"));
//    }
//
//
//
//    private void cancelReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String reservationId = request.getParameter("reservationId");
//        String bookId = request.getParameter("bookId");
//
//        String redirectUrl = "/LBMS/book?id=" + bookId;
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            String sql = "UPDATE reservation SET status = 'Cancelled' WHERE reservationId = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setInt(1, Integer.parseInt(reservationId));
//                String message;
//
//                int rowsUpdated = stmt.executeUpdate();
//                if (rowsUpdated > 0) {
//                    message = "Reservation created successfully.";
//                } else {
//                    message = "Failed to create reservation.";
//                }
//            }
//        } catch (SQLException e) {
//            throw new ServletException("Database access error: " + e.getMessage(), e);
//        }
//        response.sendRedirect(redirectUrl + "&status=" + URLEncoder.encode(message, "UTF-8"));
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String memberId = request.getParameter("memberId");
//
//        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
//            String sql = "SELECT * FROM reservation WHERE memberId = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, memberId);
//                ResultSet rs = stmt.executeQuery();
//
//                StringBuilder result = new StringBuilder();
//                while (rs.next()) {
//                    result.append("Reservation ID: ").append(rs.getInt("reservationId")).append(", ");
//                    result.append("Book ID: ").append(rs.getInt("bookId")).append(", ");
//                    result.append("Reservation Date: ").append(rs.getDate("reservationDate")).append(", ");
//                    result.append("Status: ").append(rs.getString("status")).append("<br>");
//                }
//
//                response.getWriter().write(result.toString());
//            }
//        } catch (SQLException e) {
//            throw new ServletException("Database access error: " + e.getMessage(), e);
//        }
//    }
//}


package Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@WebServlet("/reservation")
public class reservation extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("create".equalsIgnoreCase(action)) {
            createReservation(request, response);
        } else if ("cancel".equalsIgnoreCase(action)) {
            cancelReservation(request, response);
        } else if ("check".equalsIgnoreCase(action)) {
            checkReservationStatus(request, response);
        }
    }

    private void createReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String memberId = request.getParameter("memberId");
        String bookId = request.getParameter("bookId");
        Date reservationDate = new Date();
        String redirectUrl = "/LBMS/book?id=" + bookId;

        // Validate memberId
        if (memberId == null || memberId.trim().isEmpty()) {
            String errorMessage = "Member ID cannot be null or empty.";
            response.sendRedirect(redirectUrl + "&status=" + URLEncoder.encode(errorMessage, "UTF-8"));
            return;
        }

        String message;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the book is already reserved by this member
            String checkSql = "SELECT COUNT(*) FROM reservation WHERE bookId = ? AND memberId = ? AND status = 'Pending'";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, Integer.parseInt(bookId));
                checkStmt.setString(2, memberId);

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Reservation already exists
                    message = "You have already reserved this book.";
                } else {
                    // Insert new reservation
                    String insertSql = "INSERT INTO reservation (memberId, bookId, reservationDate, status) VALUES (?, ?, ?, 'Pending')";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, memberId);
                        insertStmt.setInt(2, Integer.parseInt(bookId));
                        insertStmt.setDate(3, new java.sql.Date(reservationDate.getTime()));

                        int rowsInserted = insertStmt.executeUpdate();
                        if (rowsInserted > 0) {
                            message = "Reservation created successfully.";
                        } else {
                            message = "Failed to create reservation.";
                        }
                    }
                }
            }
        } catch (SQLException e) {
            message = "Database access error: " + e.getMessage();
        }

        // Redirect with a message parameter
        response.sendRedirect(redirectUrl + "&status=" + URLEncoder.encode(message, "UTF-8"));
    }

    private void cancelReservation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String memberId = request.getParameter("memberId");
        String bookId = request.getParameter("bookId");

        String redirectUrl = "/LBMS/book?id=" + bookId;
        String message;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE reservation SET status = 'Cancelled' WHERE memberId = ? AND bookId= ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            	stmt.setString(1, memberId);
            	stmt.setInt(2, Integer.parseInt(bookId));


                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    message = "Reservation cancelled successfully.";
                } else {
                    message = "Failed to cancel reservation.";
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database access error: " + e.getMessage(), e);
        }

        // Redirect with a message parameter
        response.sendRedirect(redirectUrl + "&status=" + URLEncoder.encode(message, "UTF-8"));
    }

    private void checkReservationStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String memberId = request.getParameter("memberId");
        String bookId = request.getParameter("bookId");
        String message;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT COUNT(*) FROM reservation WHERE bookId = ? AND memberId = ? AND status = 'Pending'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(bookId));
                stmt.setString(2, memberId);

                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    message = "The book is currently reserved by you.";
                } else {
                    message = "The book is not reserved by you.";
                }
            }
        } catch (SQLException e) {
            message = "Database access error: " + e.getMessage();
        }

        // Return the result as a plain text response
        response.setContentType("text/plain");
        response.getWriter().write(message);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String memberId = request.getParameter("memberId");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM reservation WHERE memberId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, memberId);
                ResultSet rs = stmt.executeQuery();

                StringBuilder result = new StringBuilder();
                while (rs.next()) {
                    result.append("Reservation ID: ").append(rs.getInt("reservationId")).append(", ");
                    result.append("Book ID: ").append(rs.getInt("bookId")).append(", ");
                    result.append("Reservation Date: ").append(rs.getDate("reservationDate")).append(", ");
                    result.append("Status: ").append(rs.getString("status")).append("<br>");
                }

                response.getWriter().write(result.toString());
            }
        } catch (SQLException e) {
            throw new ServletException("Database access error: " + e.getMessage(), e);
        }
    }
}

