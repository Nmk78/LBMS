package auth;

import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/authServlet")
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
        String checkEmailQuery = "SELECT email FROM member WHERE email = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            if ("SignUp".equals(mode)) {
                // Check if email is already registered
                PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailQuery);
                checkEmailStmt.setString(1, email);
                ResultSet emailResult = checkEmailStmt.executeQuery();
                
                if (emailResult.next()) {
                    response.sendRedirect("/LBMS/?mode=SignUp&err=email_exists&email="+email+"&name="+name+"&phone="+phone+"&idOrDept="+idOrDept);
                    return;
                }


                // Validate phone strength (example: at least 8 characters)
                if (phone.length() != 11 ) {
                    response.sendRedirect("/LBMS/?mode=SignUp&err=phone_seem_wrong&email="+email+"&name="+name+"&phone="+phone+"&idOrDept="+idOrDept);
                    return;
                }

                // Validate password strength (example: at least 8 characters)
                if (password.length() < 8) {
                    response.sendRedirect("/LBMS/?mode=SignUp&err=weak_password&email="+email+"&name="+name+"&phone="+phone+"&idOrDept="+idOrDept);
                    return;
                }

                // Proceed with sign-up if checks pass
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
                    response.sendRedirect("user.jsp");
                } else {
                    response.sendRedirect("/LBMS/?mode=SignUp&err=signup_failed");
                }

                pstmt.close();
                checkEmailStmt.close();
            } else if ("SignIn".equals(mode)) {
                PreparedStatement pstmt = conn.prepareStatement(signInQuery);
                pstmt.setString(1, email);
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    String storedHashedPassword = resultSet.getString("password");
                    if(storedHashedPassword == null) {
                        response.sendRedirect("/LBMS/?mode=SignIn&err=email_does_not_eixt!");
                    }
                    boolean passwordMatch = BCrypt.checkpw(password, storedHashedPassword);

                    if (passwordMatch) {
                        PreparedStatement getUser = conn.prepareStatement(getUserQuery);
                        getUser.setString(1, email);
                        ResultSet userResultSet = getUser.executeQuery();

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

                        userResultSet.close();
                        getUser.close();
                    } else {
                        response.sendRedirect("/LBMS/?mode=SignIn&err=wrong_credentials&email="+email);
                    }
                } else {
                    response.sendRedirect("/LBMS/?mode=SignIn&err=wrong_credentials");
                }

                resultSet.close();
                pstmt.close();
            } else {
                out.println("ERROR: Unspecified Mode.");
            }

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.sendRedirect("/LBMS/?mode=" + mode + "&err=internal_error");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
