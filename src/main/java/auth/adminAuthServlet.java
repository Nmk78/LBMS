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
        String insertSQL = "INSERT INTO admin (adminName, email, phone, password, referralCode) VALUES (?, ?, ?, ?)";

        
        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");
            PreparedStatement checkStatement = conn.prepareStatement(checkEmailSQL);
            
            
            checkStatement.setString(1, email);
            ResultSet emailResult = checkStatement.executeQuery();
            if (emailResult.next()) {
                response.sendRedirect("/LBMS/auth/admin/signup.html?mode=SignUp&err=email_exists&email="+email+"&name="+adminName+"&phone="+phone+"&email="+email);
                return;
            }


            // Validate phone strength (example: at least 8 characters)
            if (phone.length() != 11 ) {
                response.sendRedirect("/LBMS/auth/admin/signup.html?mode=SignUp&err=phone_seem_wrong&email="+email+"&name="+adminName+"&phone="+phone+"&email="+email);
                return;
            }

            // Validate password strength (example: at least 8 characters)
            if (password.length() < 8) {
                response.sendRedirect("/LBMS/auth/admin/signup.html?mode=SignUp&err=weak_password&email="+email+"&name="+adminName+"&phone="+phone+"&email="+email);
                return;
            }
            
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());


            try (PreparedStatement insertStatement = conn.prepareStatement(insertSQL)) {
                insertStatement.setString(1, adminName);
                insertStatement.setString(2, email);
                insertStatement.setString(3, phone);
                insertStatement.setString(4, hashedPassword);
                insertStatement.setString(5, generateUniqueID());


                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                	request.setAttribute("name", adminName);
                    request.setAttribute("email", email);
                    request.setAttribute("phone", phone);
                    request.setAttribute("role", "admin");
                    request.setAttribute("message", "Admin registered successfully.");
                    response.sendRedirect("admin.jsp");
                } else {
                    request.setAttribute("message", "Admin registration failed.");
                    request.getRequestDispatcher("/LBMS/auth/admin/signup.html").forward(request, response);
                }
            }

        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException("Database error", ex);
        }
    }

    private void signInAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lbms", "root", "root");
        
        String checkEmailSQL = "SELECT email FROM admin WHERE email = ?";
        String getPwsQuery = "SELECT password FROM admin WHERE email = ?";
        String getAdminQuery = "SELECT * FROM admin WHERE email = ?";


        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        PreparedStatement checkEmailStatement = conn.prepareStatement(checkEmailSQL);
        checkEmailStatement.setString(1, email);
        ResultSet checkEmailStatementRs = checkEmailStatement.executeQuery();
        


        if (checkEmailStatementRs.next()) {
        	
        	PreparedStatement getPws = conn.prepareStatement(getPwsQuery);
        	getPws.setString(1, email);
            ResultSet getPwsRs = getPws.executeQuery();
            
            String storedHashedPassword = getPwsRs.getString("password");
            

            boolean passwordMatch = BCrypt.checkpw(password, storedHashedPassword);

            if (passwordMatch) {
                PreparedStatement getAdmin = conn.prepareStatement(getAdminQuery);
                getAdmin.setString(1, email);
                ResultSet userResultSet = getAdmin.executeQuery();

                if (userResultSet.next()) {
                    String n = userResultSet.getString("name");
                    String mail = userResultSet.getString("email");
                    String ph = userResultSet.getString("phone");
                    String referralCode = userResultSet.getString("referralCode");

                    request.setAttribute("name", n);
                    request.setAttribute("email", mail);
                    request.setAttribute("phone", ph);
                    request.setAttribute("referralCode", referralCode);

                    response.sendRedirect("user.jsp");
                }
                

                userResultSet.close();
                getAdmin.close();
            } else {
                response.sendRedirect("/LBMS/?mode=SignIn&err=wrong_credentials&email="+email);
            }
        } else {
            response.sendRedirect("/LBMS/?mode=SignIn&err=wrong_credentials");
        }

        checkEmailStatement.close();
        checkEmailStatementRs.close();
    }
    }
}

private String generateUniqueID() {
    // Generate a random number between 100000 and 999999
    int randomNum = 100000 + (int)(Math.random() * 900000);
    return String.format("%06d", randomNum); // Ensure the ID is always 6 digits
}