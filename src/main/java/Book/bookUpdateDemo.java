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
 * Servlet implementation class bookUpdateDemo
 */
public class bookUpdateDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public bookUpdateDemo() {
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
        String author=request.getParameter("author");


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            String checkAuthorQuery = "SELECT COUNT(*) FROM author WHERE Aname = ?";
            PreparedStatement checkAuthorStmt = conn.prepareStatement(checkAuthorQuery);
            checkAuthorStmt.setString(1, author);
            ResultSet rs = checkAuthorStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                out.println("<center><h2>Author Information Didn't have in DB</h2>");
            }else {
            	PreparedStatement updateAuthor = conn.prepareStatement("update author set Aname = ? where Aname = ?");
            	updateAuthor.setString(1, title);
            	updateAuthor.setString(2, author);
            	
            	updateAuthor.executeUpdate();
            	
                request.setAttribute("message","SuccessFul");
//              response.sendRedirect("/LBMS/admin.jsp");
               request.getRequestDispatcher("/admin.jsp").forward(request,response);
            }

        }
            catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly
            
        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	}

	/**
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
