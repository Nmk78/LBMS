package auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.DB_connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Servlet implementation class authServlet
 */
public class authServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public authServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String item = request.getParameter("item");
        HttpSession session = request.getSession();
        
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String mode = request.getParameter("mode");
		String id = request.getParameter("id");
		String password = request.getParameter("password");

        String sql = "INSERT INTO users (name, email, id) VALUES (?, ?, ?)";

        

        try (Connection conn = DB_connection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, id);
            pstmt.setString(4, password);
            pstmt.setString(5, mode);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                response.getWriter().println("Data inserted successfully!");
            } else {
                response.getWriter().println("Failed to insert data.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
        
//        ArrayList<String> user = (ArrayList<String>) session.getAttribute("name");
//        if (user == null) {
//            user = new ArrayList<>();
//            session.setAttribute("name", name);
//            session.setAttribute("email", email);
//            session.setAttribute("id", id);
//
//        }
//
//        
//        
//            session.setAttribute("user", user);
//
//        out.println("<html>");
//        out.println("<head><title>Cart</title></head>");
//        out.println("<body bgcolor='white'>");
//        out.println("<center><h2>Items Purchased</h2>");
//
//        out.println("<ul>");
//            out.println("<li>" + session.getAttribute(name) + "</li>");
//        out.println("</ul>");
//
//        out.println("</center></body>");
//        out.println("</html>");
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
