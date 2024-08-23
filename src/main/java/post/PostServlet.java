package post;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/PostServlet")
@MultipartConfig(maxFileSize = 16177215) // Limit file size to 16MB
public class PostServlet extends HttpServlet {

    private String dbURL = "jdbc:mysql://localhost:3306/lbms";
    private String dbUser = "root";
    private String dbPass = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        response.getWriter().append("AA");
        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            switch (action) {
                case "insert":
                    insertPost(request, conn, response);
                    break;
                case "edit":
                    editPost(request, conn, response);
                    break;
                case "delete":
                    deletePost(request, conn, response);
                    break;
                case "addReaction":
                    addReaction(request, conn, response);
                    break;
                case "removeReaction":
                    removeReaction(request, conn, response);
                    break;
                default:
                    response.getWriter().write("Invalid action");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    private void insertPost(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Part filePart = request.getPart("image");
        InputStream inputStream = null;

        if (filePart != null) {
            inputStream = filePart.getInputStream();
        }

        String sql = "INSERT INTO Post (Title, Content, Image) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, content);
            if (inputStream != null) {
                statement.setBlob(3, inputStream);
            }
            int row = statement.executeUpdate();
            if (row > 0) {
                response.getWriter().write("Post created successfully");
            } else {
                response.getWriter().write("Failed to create post");
            }
        } catch (Exception ex) {
            throw new ServletException("Error: " + ex.getMessage(), ex);
        }
    }

    private void editPost(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        Part filePart = request.getPart("image");
        InputStream inputStream = null;

        if (filePart != null) {
            inputStream = filePart.getInputStream();
        }

        String sql = "UPDATE Post SET Title = ?, Content = ?, Image = ? WHERE PostId = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setString(2, content);
            if (inputStream != null) {
                statement.setBlob(3, inputStream);
            }
            statement.setInt(4, postId);
            int row = statement.executeUpdate();
            if (row > 0) {
                response.getWriter().write("Post updated successfully");
            } else {
                response.getWriter().write("Failed to update post");
            }
        } catch (Exception ex) {
            throw new ServletException("Error: " + ex.getMessage(), ex);
        }
    }

    private void deletePost(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException {
        int postId = Integer.parseInt(request.getParameter("postId"));

        String sql = "DELETE FROM Post WHERE PostId = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, postId);
            int row = statement.executeUpdate();
            if (row > 0) {
                response.getWriter().write("Post deleted successfully");
            } else {
                response.getWriter().write("Failed to delete post");
            }
        } catch (Exception ex) {
            throw new ServletException("Error: " + ex.getMessage(), ex);
        }
    }

    private void addReaction(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        int reactionId = Integer.parseInt(request.getParameter("reactionId"));

        String sql = "INSERT INTO PostReaction (PostId, ReactionId) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, postId);
            statement.setInt(2, reactionId);
            int row = statement.executeUpdate();
            if (row > 0) {
                response.getWriter().write("Reaction added successfully");
            } else {
                response.getWriter().write("Failed to add reaction");
            }
        } catch (Exception ex) {
            throw new ServletException("Error: " + ex.getMessage(), ex);
        }
    }

    private void removeReaction(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        int reactionId = Integer.parseInt(request.getParameter("reactionId"));

        String sql = "DELETE FROM PostReaction WHERE PostId = ? AND ReactionId = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, postId);
            statement.setInt(2, reactionId);
            int row = statement.executeUpdate();
            if (row > 0) {
                response.getWriter().write("Reaction removed successfully");
            } else {
                response.getWriter().write("Failed to remove reaction");
            }
        } catch (Exception ex) {
            throw new ServletException("Error: " + ex.getMessage(), ex);
        }
    }
}
