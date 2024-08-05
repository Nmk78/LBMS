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
import java.sql.*;

/**
 * Servlet implementation class authServlet
 */
//@WebServlet("/authServlet")
public class authServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public authServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html");
//
//        PrintWriter out = response.getWriter();
//        String name = request.getParameter("name");
//        String email = request.getParameter("email");
//        String password = request.getParameter("password");
//
//        String sql = "INSERT INTO Users (name, email, password) VALUES (?, ?, ?)";
//        out.println("Data inserting....!1");
//
////        try (Connection conn = DB_connection.getConnection();
////             PreparedStatement pstmt = conn.prepareStatement(sql)) {
////            out.println("Data inserting....!2");
////
////            pstmt.setString(1, name);
////            pstmt.setString(2, email);
////            pstmt.setString(3, password);
////
////            int rowsAffected = pstmt.executeUpdate();
////
////            if (rowsAffected > 0) {
////                out.println("Data inserted successfully!");
////            } else {
////                out.println("Failed to insert data.");
////            }
////
////        } catch (SQLException e) {
////            e.printStackTrace();
////            out.println("Error: " + e.getMessage());
////        }
//
//        HttpSession session = request.getSession();
//        session.setAttribute("name", name);
//        session.setAttribute("email", email);
//        session.setAttribute("password", password);
//
//        out.println("<html>");
//        out.println("<head><title>User Info</title></head>");
//        out.println("<body bgcolor='white'>");
//        out.println("<center><h2>User Information</h2>");
//        out.println("<ul>");
//        out.println("<li>Name: " + name + "</li>");
//        out.println("<li>Email: " + email + "</li>");
//        out.println("<li>Password: " + password + "</li>");
//        out.println("</ul>");
//        out.println("</center></body>");
//        out.println("</html>");
//    }

    	response.setContentType("text/html"); 
        PrintWriter pw = response.getWriter(); 
        pw.println("<h1>Hello</h1>");

        try {

        Class.forName("com.mysql.cj.jdbc.Driver");

        pw.println("<h1> LoadDriver***</h1>");

        java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3 306/userdb", "root", "password");

        pw.println("<h1>Connection is established</h1>");

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("select * from users"); //Step 4

        while (rs.next()) {

        int id = rs.getInt("id");

        String email = rs.getString("email");

        String password = rs.getString("password");

        String fullname = rs.getString("fullname");

        pw.println(id + "\t" + email +"\t"+ password + "\t" + fullname);

        pw.println("<br>");
        
        }

        stmt.close(); //Step 5 conn.close(); //Step 6
        conn.close();
        }catch(SQLException e) {

        e.printStackTrace();

        }

        catch (ClassNotFoundException e) { 
        	e.printStackTrace();
        }
        }
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
