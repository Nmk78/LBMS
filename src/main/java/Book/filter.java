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

@WebServlet("/filter")
public class filter extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String category = request.getParameter("category");
        StringBuilder booksHtml = new StringBuilder();

        try {
            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            // Query to get books by category
            String sql = "SELECT * FROM book WHERE category = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("authorName");
                String bookCategory = rs.getString("category");
                int CopiesAvailable = rs.getInt("CopiesAvailable");
                
                Boolean available = CopiesAvailable > 0;
                
                booksHtml.append("<div class='bg-white p-4 shadow-md rounded-md'>")
                          .append("<h3 class='text-xl font-bold'>").append(title).append("</h3>")
                          .append("<p class='text-gray-700'>").append(author).append("</p>")
                          .append("<p class='text-gray-500'>").append(bookCategory).append("</p>")
                          .append("<p class='").append(available ? "text-green-500" : "text-red-500").append("'>")
                          .append(available ? "Available" : "Borrowed")
                          .append("</p>")
                          .append("</div>");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Database connection or query failed: " + e.getMessage());
        }

        // Pass the generated HTML string to the JSP page
        request.setAttribute("booksHtml", booksHtml.toString());
        response.getWriter().append(category);
        request.getRequestDispatcher("filtered.jsp").forward(request, response);
    }
}
