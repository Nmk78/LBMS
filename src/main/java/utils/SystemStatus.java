package utils;

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

@SuppressWarnings("serial")
@WebServlet("/SystemStatus")
public class SystemStatus extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/lbms";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try (Connection conn = getConnection()) {
            // Total books
            int totalBooks = getCount(conn, "SELECT COUNT(*) FROM book");
            // Overdue books
            int overdueBooks = getCount(conn, "SELECT COUNT(*) FROM loan WHERE dueDate < CURDATE() AND status <> 'clear'");
            // Borrowed books
            int borrowedBooks = getCount(conn, "SELECT COUNT(*) FROM loan WHERE status = 'due' OR status = 'overdue'");
            // Total members
            int totalMembers = getCount(conn, "SELECT COUNT(*) FROM member");

            // Create JSON response
            String jsonResponse = String.format(
                "{\"totalBooks\": %d, \"overdueBooks\": %d, \"borrowedBooks\": %d, \"totalMembers\": %d}",
                totalBooks, overdueBooks, borrowedBooks, totalMembers
            );
            
            out.print(jsonResponse);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    private int getCount(Connection conn, String query) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
