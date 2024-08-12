package auth;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.RequestDispatcher;
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
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String idOrDept = request.getParameter("idOrDept");
        String role = request.getParameter("userType");

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String signUpQuery = "INSERT INTO member (name, email, password, idOrDept, role, phone) VALUES (?, ?, ?, ?, ?, ?)";
        String signInQuery = "SELECT password FROM member WHERE email = ?";
        String getUserQuery = "SELECT name, email, phone, idOrDept FROM member WHERE email = ?";


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
                pstmt.setString(6, phone);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    session.setAttribute("name", name);
                    session.setAttribute("email", email);
                    session.setAttribute("phone", phone);
                    session.setAttribute("idOrDept", idOrDept);
                    out.println("Sign up successful!");

        			response.sendRedirect("user.jsp");

                } else {
                    out.println("Failed to sign up.");
                }
                pstmt.close();
            } else if ("SignIn".equals(mode)) {
                PreparedStatement pstmt = null;
                ResultSet resultSet = null;
                PreparedStatement getUser = null;
                ResultSet userResultSet = null;

                try {
                    pstmt = conn.prepareStatement(signInQuery);
                    pstmt.setString(1, email);
                    resultSet = pstmt.executeQuery();

                    if (resultSet.next()) {
                        String storedHashedPassword = resultSet.getString("password");
                        boolean passwordMatch = BCrypt.checkpw(password, storedHashedPassword);

                        if (passwordMatch) {
                            getUser = conn.prepareStatement(getUserQuery);
                            getUser.setString(1, email); // Change 'pstmt' to 'getUser'
                            userResultSet = getUser.executeQuery();

                            if (userResultSet.next()) {
                                String n = userResultSet.getString("name");
                                String mail = userResultSet.getString("email");
                                String ph = userResultSet.getString("phone");
                                String idOrDeptFromDB = userResultSet.getString("idOrDept");

                                session.setAttribute("name", n);
                                session.setAttribute("email", mail);
                                session.setAttribute("phone", ph);
                                session.setAttribute("idOrDept", idOrDeptFromDB);

                                 response.sendRedirect("user.jsp");
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace(); // Handle exceptions properly
                } finally {
                    // Close resources in reverse order of their opening
                    if (userResultSet != null) try { userResultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
                    if (getUser != null) try { getUser.close(); } catch (SQLException e) { e.printStackTrace(); }
                    if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
                    if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
                }
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
