package Book;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@WebServlet("/getBooks")
public class getBooks extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
    private static final String USER = "root";
    private static final String PASS = "root";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        JsonArray jsonArray = new JsonArray();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT Bid, Title, Image, AuthorName, CopiesAvailable FROM book";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                JsonObject jsonObject = new JsonObject();
                int bookId = rs.getInt("Bid");
                jsonObject.addProperty("id", bookId);
                jsonObject.addProperty("name", rs.getString("Title"));

                // Check if image is null or empty and set default if necessary
                String image = rs.getString("Image");
                if (image == null || image.isEmpty()) {
                    image = "./assets/img/defaultCover.png";
                }
                jsonObject.addProperty("image", image);

                jsonObject.addProperty("author", rs.getString("AuthorName"));
                
                int copiesAvailable = rs.getInt("CopiesAvailable");
                if (copiesAvailable > 0) {
                    jsonObject.addProperty("availability", copiesAvailable);
                } else {
                    String nearestReturnDate = getNearestReturnDate(conn, bookId);
                    jsonObject.addProperty("NearestReturnDate", nearestReturnDate);
                }
                jsonArray.add(jsonObject);
            }

            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonArray);
        out.print(jsonResponse);
        out.flush();
    }

    private String getNearestReturnDate(Connection conn, int bookId) throws Exception {
        String nearestReturnDate = "N/A";
        String query = "SELECT MIN(returnDate) AS nearestReturnDate FROM loan WHERE bookid = ? AND returnDate IS NOT NULL AND returnDate > CURDATE()";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                nearestReturnDate = rs.getString("nearestReturnDate");
                if (nearestReturnDate == null) {
                    nearestReturnDate = "No upcoming return dates";
                }
            }
            rs.close();
        }
        return nearestReturnDate;
    }
}
