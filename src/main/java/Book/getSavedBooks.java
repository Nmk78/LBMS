package Book;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@WebServlet("/getSavedBooks")
public class getSavedBooks extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] bookIds = request.getParameterValues("bookIds[]");

        if (bookIds == null || bookIds.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        List<BookClass> savedBooks = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");
            StringBuilder query = new StringBuilder("SELECT * FROM book WHERE bid IN (");
            for (int i = 0; i < bookIds.length; i++) {
                query.append("?");
                if (i < bookIds.length - 1) {
                    query.append(",");
                }
            }
            query.append(")");

            stmt = conn.prepareStatement(query.toString());
            for (int i = 0; i < bookIds.length; i++) {
                stmt.setInt(i + 1, Integer.parseInt(bookIds[i])); // Correctly parse each ID
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                BookClass book = new BookClass();
                book.setBid(rs.getInt("bid"));
                book.setTitle(rs.getString("title"));
                book.setCategory(rs.getString("category"));
                book.setAuthorName(rs.getString("authorName"));
                book.setBookShelf(rs.getInt("bookShelf"));
                book.setCopiesAvailable(rs.getInt("copiesAvailable"));
                book.setAcquireBy(rs.getString("acquireBy"));
                book.setImage(rs.getBlob("image"));
                savedBooks.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (Exception e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        String json = new Gson().toJson(savedBooks);
        response.setContentType("application/json");
        response.getWriter().write(json);
    }

}
