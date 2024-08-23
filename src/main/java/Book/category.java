package Book;

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

@WebServlet("/category")
public class category extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder categoryButtons = new StringBuilder();

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            // Query to get distinct categories
            String sql = "SELECT DISTINCT category FROM book";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String category = rs.getString("category");
                // Build the button HTML for each category
                categoryButtons.append("<button class='category-btn bg-blue-500 text-white px-3 py-1 rounded-full hover:bg-blue-700 whitespace-nowrap' data-category='")
                .append(category)
                .append("'>")
                .append(category)
                .append("</button>");


            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Database connection or query failed: " + e.getMessage());
        }

        // Pass the generated HTML string to the JSP page
        request.setAttribute("categoryButtons", categoryButtons.toString());
        request.getRequestDispatcher("category.jsp").forward(request, response);
    }
}
