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
import java.util.Base64;

@WebServlet("/category")
public class category extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 18; // Number of items per page

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder categoryButtons = new StringBuilder();
        StringBuilder bookCards = new StringBuilder();
        StringBuilder paginationControls = new StringBuilder();

        int currentPage = 1; // Default to the first page
        String selectedCategory = request.getParameter("category");
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            selectedCategory = "all"; // Default category
        }

        try {
            // Get the current page from request parameter
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1; // Ensure the page number is at least 1
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            // Calculate pagination parameters
            int startIndex = (currentPage - 1) * PAGE_SIZE;

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
                categoryButtons.append("<button class='category-btn bg-blue-500 text-white px-3 py-1 hover:bg-blue-700 whitespace-nowrap' ")
                        .append("onclick=\"location.href='?category=")
                        .append(category)
                        .append("&page=1'\">")
                        .append(category)
                        .append("</button>");
            }

            // Build the base SQL query with optional filter
            StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM book");
            StringBuilder bookSql = new StringBuilder("SELECT Bid, title, authorName, image, CopiesAvailable FROM book");

            if (!"all".equals(selectedCategory)) {
                countSql.append(" WHERE category = ?");
                bookSql.append(" WHERE category = ?");
            }

            // Get the total number of books for pagination
            PreparedStatement countStmt = conn.prepareStatement(countSql.toString());
            if (!"all".equals(selectedCategory)) {
                countStmt.setString(1, selectedCategory);
            }
            ResultSet countRs = countStmt.executeQuery();
            int totalItems = 0;
            if (countRs.next()) {
                totalItems = countRs.getInt(1);
            }

            // Calculate total pages
            int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
            if (currentPage > totalPages) currentPage = totalPages; // Adjust page number if it exceeds total pages

            // Get books for the current page
            PreparedStatement bookStmt = conn.prepareStatement(bookSql.append(" LIMIT ?, ?").toString());
            if (!"all".equals(selectedCategory)) {
                bookStmt.setString(1, selectedCategory);
                bookStmt.setInt(2, startIndex);
                bookStmt.setInt(3, PAGE_SIZE);
            } else {
                bookStmt.setInt(1, startIndex);
                bookStmt.setInt(2, PAGE_SIZE);
            }
            ResultSet bookRs = bookStmt.executeQuery();

            while (bookRs.next()) {
                int bid = bookRs.getInt("Bid");
                String title = bookRs.getString("title");
                String authorName = bookRs.getString("authorName");
                int copiesAvailable = bookRs.getInt("CopiesAvailable");

                byte[] imageBytes = bookRs.getBytes("image");
                String image = "";
                if (imageBytes != null && imageBytes.length > 0) {
                    image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
                } else {
                    image = "./assets/img/defaultCover.png"; // Default image URL
                }
                
                // Build the book card HTML
//                bookCards.append("<a href='/LBMS/getBook?id=")
//                        .append(bid)
//                        .append("' target='_self' title='")
//                        .append(title)
//                        .append("' class='min-w-[250px] w-[--cardWidth] max-w-96 md:w-[300px] h-[--cardHeight] bg-[--bg] border-[3px]'>")
//                        .append("<div class='image w-full h-[300px] overflow-hidden bg-cover'>")
//                        .append("<img class='w-full h-full object-cover hover:object-contain' src='")
//                        .append(image)
//                        .append("' alt='Book'>")
//                        .append("</div>")
//                        .append("<div class='details p-3 min-h-[150px] h-[160px] flex flex-col justify-between'>")
//                        .append("<p class='title font-semibold text-[--text] text-xl line-clamp-2'>")
//                        .append(title)
//                        .append("</p>")
//                        .append("<div class='AuthorAndAvailability w-full flex justify-between items-center'>")
//                        .append("<p class='author font-medium text-lg text-[--text]'>")
//                        .append(authorName)
//                        .append("</p>")
//                        .append("<p class='status px-2 py-1 border-2 border-dashed ")
//                        .append(copiesAvailable > 0 ? "border-[--accent] text-[--accent]" : "border-[--warning] text-[--warning]")
//                        .append("'>")
//                        .append(copiesAvailable > 0 ? "Available : " + copiesAvailable : copiesAvailable != 0 ? "Due: " + copiesAvailable + " days" : "N/A")
//                        .append("</p>")
//                        .append("</div>")
//                        .append("</div>")
//                        .append("</a>");
                bookCards.append("<a href='/LBMS/getBook?id=")
                .append(bid)
                .append("' target='_self' title='")
                .append(title)
                .append("' class='group relative min-w-[250px] transition-all duration-300 w-[300px] h-[460px] bg-white border-2 border-gray-300 rounded-lg shadow-md overflow-hidden'>")
                .append("<div class='image w-full h-full absolute top-0 left-0 z-0 overflow-hidden bg-cover transition-transform'>")
                .append("<img class='w-full h-full object-cover transition-transform group-hover:scale-110' src='")
                .append(image)
                .append("' alt='Book'>")
                .append("</div>")
                .append("<div class='details p-3 flex flex-col justify-between absolute bottom-0 left-0 right-0 z-10 bg-white transition-transform transform translate-y-full group-hover:translate-y-0'>")
                .append("<p class='title font-semibold text-gray-800 text-xl line-clamp-2'>")
                .append(title)
                .append("</p>")
                .append("<div class='AuthorAndAvailability w-full flex justify-between items-center'>")
                .append("<p class='author font-medium text-lg text-gray-600'>")
                .append(authorName)
                .append("</p>")
                .append("<p class='status px-2 py-1 border-2 border-dashed ")
                .append(copiesAvailable > 0 ? "border-green-500 text-green-500" : "border-red-500 text-red-500")
                .append("'>")
                .append(copiesAvailable > 0 ? "Available : " + copiesAvailable : copiesAvailable != 0 ? "Due: " + copiesAvailable + " days" : "N/A")
                .append("</p>")
                .append("</div>")
                .append("</div>")
                .append("</a>");

            }

                
            // Generate pagination controls
            if (totalPages > 1) {
                if (currentPage > 1) {
                    paginationControls.append("<a href='?page=")
                            .append(currentPage - 1)
                            .append(selectedCategory != null && !"all".equals(selectedCategory) ? "&category=" + selectedCategory : "")
                            .append("' class='pagination-button'>Previous</a>");
                }
                for (int i = 1; i <= totalPages; i++) {
                    paginationControls.append("<a href='?page=")
                            .append(i)
                            .append(selectedCategory != null && !"all".equals(selectedCategory) ? "&category=" + selectedCategory : "")
                            .append("' class='pagination-button ")
                            .append(i == currentPage ? "bg-blue-500 text-white text-lg px-5 py-2 mx-1" : "bg-gray-300 text-gray-700 px-5 py-2 mx-1")
                            .append("'>")
                            .append(i)
                            .append("</a>");
                }
                if (currentPage < totalPages) {
                    paginationControls.append("<a href='?page=")
                            .append(currentPage + 1)
                            .append(selectedCategory != null && !"all".equals(selectedCategory) ? "&category=" + selectedCategory : "")
                            .append("' class='pagination-button'>Next</a>");
                }
            }

            // Close resources
            bookRs.close();
            countRs.close();
            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Database connection or query failed: " + e.getMessage());
        }

        // Pass the generated HTML strings to the JSP page
        request.setAttribute("categoryButtons", categoryButtons.toString());
        request.setAttribute("bookCards", bookCards.toString());
        request.setAttribute("paginationControls", paginationControls.toString());
        request.getRequestDispatcher("category.jsp").forward(request, response);
    }
}
