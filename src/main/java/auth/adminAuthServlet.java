package auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/adminAuthServlet")
public class adminAuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = request.getParameter("mode");
        
        if ("SignUp".equals(mode)) {
            registerAdmin(request, response);
        } else if ("SignIn".equals(mode)) {
            signInAdmin(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid mode");
        }
    }

    private void registerAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String adminName = request.getParameter("adminName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        String checkEmailSQL = "SELECT email FROM admin WHERE email = ?";
        String insertSQL = "INSERT INTO admin (adminName, email, phone, password, referralCode) VALUES (?, ?, ?, ?, ?)";

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 60 * 24 * 30); // Max 30-day session age

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");
            PreparedStatement checkStatement = conn.prepareStatement(checkEmailSQL);

            checkStatement.setString(1, email);
            ResultSet emailResult = checkStatement.executeQuery();
            if (emailResult.next()) {
                response.sendRedirect("/LBMS/auth/admin/signup.html?mode=SignUp&err=email_exists&email=" + email + "&name=" + adminName + "&phone=" + phone);
                return;
            }

            // Validate phone number length (e.g., must be 11 characters)
            if (phone.length() != 11) {
                response.sendRedirect("/LBMS/auth/admin/signup.html?mode=SignUp&err=phone_seem_wrong&email=" + email + "&name=" + adminName + "&phone=" + phone);
                return;
            }

            // Validate password strength (e.g., must be at least 8 characters)
            if (password.length() < 8) {
                response.sendRedirect("/LBMS/auth/admin/signup.html?mode=SignUp&err=weak_password&email=" + email + "&name=" + adminName + "&phone=" + phone);
                return;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Generate a unique referral code
            String generatedId = generateUniqueID();

            try (PreparedStatement insertStatement = conn.prepareStatement(insertSQL)) {
                insertStatement.setString(1, adminName);
                insertStatement.setString(2, email);
                insertStatement.setString(3, phone);
                insertStatement.setString(4, hashedPassword);
                insertStatement.setString(5, generatedId); // Correctly set the referral code

                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    session.setAttribute("name", adminName);
                    session.setAttribute("email", email);
                    session.setAttribute("phone", phone);
                    session.setAttribute("referralCode", generatedId);
                    session.setAttribute("role", "admin");
                    session.setAttribute("message", "Admin registered successfully.");
                    response.sendRedirect("admin.jsp");
                } else {
                    session.setAttribute("message", "Admin registration failed.");
                    response.sendRedirect("/LBMS/auth/admin/signup.html");
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException("Database error", ex);
        }
    }

    protected void signInAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String checkEmailSQL = "SELECT email FROM admin WHERE email = ?";
        String getPwsQuery = "SELECT password FROM admin WHERE email = ?";
        String getAdminQuery = "SELECT * FROM admin WHERE email = ?";

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 60 * 24 * 30); // Max 30 day session age
        
        Connection conn = null;
        PreparedStatement checkEmailStatement = null;
        PreparedStatement getPwsStatement = null;
        PreparedStatement getAdminStatement = null;
        ResultSet checkEmailRs = null;
        ResultSet getPwsRs = null;
        ResultSet adminRs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");

            checkEmailStatement = conn.prepareStatement(checkEmailSQL);
            checkEmailStatement.setString(1, email);
            checkEmailRs = checkEmailStatement.executeQuery();

            if (checkEmailRs.next()) {
                getPwsStatement = conn.prepareStatement(getPwsQuery);
                getPwsStatement.setString(1, email);
                getPwsRs = getPwsStatement.executeQuery();

                if (getPwsRs.next()) {
                    String storedHashedPassword = getPwsRs.getString("password");

                    boolean passwordMatch = BCrypt.checkpw(password, storedHashedPassword);

                    if (passwordMatch) {
                        getAdminStatement = conn.prepareStatement(getAdminQuery);
                        getAdminStatement.setString(1, email);
                        adminRs = getAdminStatement.executeQuery();

                        if (adminRs.next()) {
                        	session.setAttribute("name", adminRs.getString("adminName"));
                        	session.setAttribute("email", adminRs.getString("email"));
                        	session.setAttribute("phone", adminRs.getString("phone"));
                        	session.setAttribute("referralCode", adminRs.getString("referralCode"));
                        	session.setAttribute("role", "admin");

                            response.sendRedirect("admin.jsp");
                        }
                    } else {
                        response.sendRedirect("/LBMS/auth/admin/signin.html?mode=SignIn&err=wrong_credentials&email=" + email);
                    }
                }
            } else {
                response.sendRedirect("/LBMS/auth/admin/signin.html?mode=SignIn&err=wrong_credentials");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException("Database error", e);
        } finally {
            try {
                if (adminRs != null) adminRs.close();
                if (getAdminStatement != null) getAdminStatement.close();
                if (getPwsRs != null) getPwsRs.close();
                if (getPwsStatement != null) getPwsStatement.close();
                if (checkEmailRs != null) checkEmailRs.close();
                if (checkEmailStatement != null) checkEmailStatement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
private String generateUniqueID() {
    // Generate a random number between 100000 and 999999
    int randomNum = 100000 + (int)(Math.random() * 900000);
    return String.format("%06d", randomNum); // Ensure the ID is always 6 digits
}
}

