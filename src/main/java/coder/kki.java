package coder;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class FooterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("text/html");

        response.getWriter().write("<footer class='footer'>"
            + "<div class='container'>"
            + "<div class='footer-section'>"
            + "<div class='footer-logo'>"
            + "<img src='images/logo.png' alt='Kalasa Art Space Logo'>"
            + "<p>A Space of sharing ART for all generations.</p>"
            + "<div class='social-icons'>"
            + "<a href='https://facebook.com' target='_blank'>"
            + "<img src='images/facebook-icon.png' alt='Facebook'>"
            + "</a>"
            + "<a href='https://instagram.com' target='_blank'>"
            + "<img src='images/instagram-icon.png' alt='Instagram'>"
            + "</a>"
            + "<a href='https://maps.google.com' target='_blank'>"
            + "<img src='images/maps-icon.png' alt='Maps'>"
            + "</a>"
            + "</div>"
            + "</div>"
            + "<div class='footer-links'>"
            + "<h3>QUICK LINKS</h3>"
            + "<ul>"
            + "<li><a href='home.jsp'>Home</a></li>"
            + "<li><a href='gallery.jsp'>Gallery</a></li>"
            + "<li><a href='exhibitions.jsp'>Exhibitions</a></li>"
            + "<li><a href='collections.jsp'>Collections</a></li>"
            + "<li><a href='artists.jsp'>Artists</a></li>"
            + "<li><a href='blogs.jsp'>Blogs</a></li>"
            + "</ul>"
            + "</div>"
            + "<div class='footer-contact'>"
            + "<h3>GET IN TOUCH</h3>"
            + "<p>Contact us:</p>"
            + "<p>+95 976 345 678</p>"
            + "<p><a href='mailto:admin.kalasa@gmail.com'>admin.kalasa@gmail.com</a></p>"
            + "</div>"
            + "<div class='footer-location'>"
            + "<h3>LOCATION</h3>"
            + "<p>No. 91-93, 1st floor (left), Seikkantha Street</p>"
            + "<p>(Middle Block), Kyauktada Township, Yangon.</p>"
            + "</div>"
            + "</div>"
            + "</div>"
            + "<div class='footer-bottom'>"
            + "<p>© 2019-2023. All Rights Reserved by Kalasa Art Space.</p>"
            + "</div>"
            + "</footer>");
    }

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
}
}
