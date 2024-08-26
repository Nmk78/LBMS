package post;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/post")
public class post extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String postId = request.getParameter("id");

        if (postId != null) {
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                // Load the database driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish the connection
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Prepare and execute the SQL query
                String sql = "SELECT * FROM Post WHERE PostId = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, postId);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    request.setAttribute("post", rs);
                    request.getRequestDispatcher("postDetails.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post not found");
                }
            } catch (ClassNotFoundException | SQLException e) {
                throw new ServletException("Database error", e);
            } finally {
                // Close resources
                try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
                try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
                try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing post ID");
        }
    }
}
