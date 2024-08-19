package Book;

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

/**
 * Servlet implementation class bookInsert
 */
public class bookInsert extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public bookInsert() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String message ="";
        
        String title=request.getParameter("title");
        String category=request.getParameter("category");
        String author=request.getParameter("author");
        String bookShelf=request.getParameter("bookShelf");
        String acquireBy=request.getParameter("acquireBy");
        
        // Bcakend Received Data From Front End
        
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
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            String checkAuthorQuery = "SELECT COUNT(*) FROM author WHERE Aname = ?";
            PreparedStatement checkAuthorStmt = conn.prepareStatement(checkAuthorQuery);

            checkAuthorStmt.setString(1, author);
//            "SELECT COUNT(*) FROM author WHERE Aname = author"
            ResultSet rs = checkAuthorStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // Author does not exist, so insert the author first
                String insertAuthorQuery = "INSERT INTO author (Aname) VALUES (?)";
                PreparedStatement insertAuthorStmt = conn.prepareStatement(insertAuthorQuery);
                insertAuthorStmt.setString(1, author);
                insertAuthorStmt.executeUpdate();
            }
            /// Author will Indeed exit in author table
            
            // Now insert the book
            String insertBookQuery = "INSERT INTO book (Title, Category, AuthorName, AddYear, BookShelf, CopiesAvailable, Copy, AcquireBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertBookStmt = conn.prepareStatement(insertBookQuery);
            
            insertBookStmt.setString(1, title);
            insertBookStmt.setString(2, category);
            insertBookStmt.setString(3, author);
            insertBookStmt.setInt(4, addYear);
            insertBookStmt.setString(5, bookShelf);
            insertBookStmt.setInt(6, copy);
            insertBookStmt.setInt(7, copy);
            insertBookStmt.setString(8, acquireBy);
            
            int rowsAffected =  insertBookStmt.executeUpdate();
            
            if (rowsAffected > 0) {
            	message = "<p id=\"toast-message\" class=\"text-green-500 p-4\">Book was scuucsscully added!</p>"; 
            }else {
            	message = "<p id=\"toast-message\" class=\"text-red-500 p-4\">Failed to add.</p>";
            }
            request.setAttribute("message",message);
//           response.sendRedirect("/LBMS/admin.jsp");
            request.getRequestDispatcher("/admin.jsp").forward(request,response);

        }
            catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly
        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
