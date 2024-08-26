package Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.tomcat.jakartaee.commons.compress.utils.IOUtils;

/**
 * Servlet implementation class bookInsert
 */
@WebServlet("/bookInsert")
public class bookInsert extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public bookInsert() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String message = "";
        
        // Process form fields
        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String author = request.getParameter("author");
        String bookShelf = request.getParameter("bookShelf");
        String acquireBy = request.getParameter("acquireBy");
        
        String copyStr = request.getParameter("copy");
        int copy = 1; // Default value if not provided
        if (copyStr != null && !copyStr.isEmpty()) {
            copy = Integer.parseInt(copyStr);
        }
        String addYearStr = request.getParameter("addYear");
        int addYear = 0; // Or any other default value
        if (addYearStr != null && !addYearStr.isEmpty()) {
            addYear = Integer.parseInt(addYearStr);
        }
        
        // Handle file upload
        Part filePart = request.getPart("image");
        String imagePath = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = UUID.randomUUID().toString() + "_" + filePart.getSubmittedFileName();
            String uploadDir = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdir();
            }
            File file = new File(uploadDir, fileName);
            try (InputStream input = filePart.getInputStream();
                 FileOutputStream output = new FileOutputStream(file)) {
                IOUtils.copy(input, output);
                imagePath = "uploads/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
                message = "<p id=\"toast-message\" class=\"text-red-500 p-4\">Failed to upload image.</p>";
            }
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            String checkAuthorQuery = "SELECT COUNT(*) FROM author WHERE Aname = ?";
            PreparedStatement checkAuthorStmt = conn.prepareStatement(checkAuthorQuery);
            checkAuthorStmt.setString(1, author);
            ResultSet rs = checkAuthorStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                String insertAuthorQuery = "INSERT INTO author (Aname) VALUES (?)";
                PreparedStatement insertAuthorStmt = conn.prepareStatement(insertAuthorQuery);
                insertAuthorStmt.setString(1, author);
                insertAuthorStmt.executeUpdate();
            }

            String insertBookQuery = "INSERT INTO book (Title, Category, AuthorName, AddYear, BookShelf, CopiesAvailable, Copy, AcquireBy, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertBookStmt = conn.prepareStatement(insertBookQuery);
            
            insertBookStmt.setString(1, title);
            insertBookStmt.setString(2, category);
            insertBookStmt.setString(3, author);
            insertBookStmt.setInt(4, addYear);
            insertBookStmt.setString(5, bookShelf);
            insertBookStmt.setInt(6, copy);
            insertBookStmt.setInt(7, copy);
            insertBookStmt.setString(8, acquireBy);
            insertBookStmt.setString(9, imagePath);
            
            int rowsAffected = insertBookStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                message = "<p id=\"toast-message\" class=\"text-green-500 p-4\">Book was successfully added!</p>"; 
            } else {
                message = "<p id=\"toast-message\" class=\"text-red-500 p-4\">Failed to add.</p>";
            }
            request.setAttribute("message", message);
            request.getRequestDispatcher("/admin.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            message = "<p id=\"toast-message\" class=\"text-red-500 p-4\">Database error occurred.</p>";
            request.setAttribute("message", message);
            request.getRequestDispatcher("/admin.jsp").forward(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            message = "<p id=\"toast-message\" class=\"text-red-500 p-4\">Driver not found.</p>";
            request.setAttribute("message", message);
            request.getRequestDispatcher("/admin.jsp").forward(request, response);
        }
    }
}
