package auth;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class authServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public authServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 60 * 24 * 30); // Max 30 day session age
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String mode = request.getParameter("mode");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String idOrDept = request.getParameter("idOrDept");
        String role = request.getParameter("userType");

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String signUpQuery = "INSERT INTO Users (name, email, password, department, role) VALUES (?, ?, ?, ?, ?)";
        String signInQuery = "SELECT password FROM Users WHERE email = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            if ("SignUp".equals(mode)) {
                PreparedStatement pstmt = conn.prepareStatement(signUpQuery);
                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setString(3, hashedPassword);
                pstmt.setString(4, idOrDept);
                pstmt.setString(5, role);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    session.setAttribute("name", name);
                    session.setAttribute("email", email);
                    session.setAttribute("role", role);
                    out.println("Sign up successful!");
                } else {
                    out.println("Failed to sign up.");
                }
                pstmt.close();
            } else if ("SignIn".equals(mode)) {
                PreparedStatement pstmt = conn.prepareStatement(signInQuery);
                pstmt.setString(1, email);
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String storedHashedPassword = resultSet.getString("password");
                    boolean passwordMatch = BCrypt.checkpw(password, storedHashedPassword);

                    if (passwordMatch) {
                        session.setAttribute("name", name);
                        session.setAttribute("email", email);
                        session.setAttribute("role", role);
                        out.println("Login successful!");
                    } else {
                        out.println("Invalid email or password.");
                    }
                } else {
                    out.println("Invalid email or password.");
                }
                resultSet.close();
                pstmt.close();
            } else {
                out.println("ERROR: Unspecified Mode.");
            }

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }

        out.println("<html>");
        out.println("<head><title>User Info</title></head>");
        out.println("<body bgcolor='white'>");
        out.println("<center><h2>User Information</h2>");
        out.println("<ul>");
        out.println("<li>Name: " + session.getAttribute("name") + "</li>");
        out.println("<li>Email: " + session.getAttribute("email") + "</li>");
        out.println("<li>Role: " + session.getAttribute("role") + "</li>");
        out.println("</ul>");
        out.println("</center></body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
