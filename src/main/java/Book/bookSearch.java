package Book;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/bookSearch")
public class bookSearch extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user input
    	//response.getWriter().append("Test");
        String userInput = request.getParameter("search");

        // Establish database connection
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            // Prepare SQL query
            String sql = "SELECT * FROM book WHERE title LIKE ? OR authorName LIKE ? OR category LIKE ?";
            pstmt = conn.prepareStatement(sql);
            
            // Set the input for the SQL query
            pstmt.setString(1, "%" + userInput + "%");
            pstmt.setString(2, "%" + userInput + "%");
            pstmt.setString(3, "%" + userInput + "%");

            // Execute query
            rs = pstmt.executeQuery();

            // Forward the result to a JSP page for displaying the results
            request.setAttribute("resultSet", rs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/displayBooks.jsp");
            dispatcher.forward(request, response);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
