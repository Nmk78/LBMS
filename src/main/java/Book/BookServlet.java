package Book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/BookServlet")
@MultipartConfig(maxFileSize = 16177215) // Limit file size to 16MB
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String dbURL = "jdbc:mysql://localhost:3306/lbms";
    private String dbUser = "root";
    private String dbPass = "root";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            switch (action) {
                case "addBook":
                    addBook(request, conn, response);
                    break;
                case "editBook":
                    editBook(request, conn, response);
                    break;
                case "deleteBook":
                    deleteBook(request, conn, response);
                    break;
                case "addReview":
                    addReview(request, conn, response);
                    break;
                case "deleteReview":
                    deleteReview(request, conn, response);
                    break;
                case "addRating":
                    addRating(request, conn, response);
                    break;
                case "removeRating":
                    removeRating(request, conn, response);
                    break;
                default:
                    response.getWriter().write("Invalid action");
                    break;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

private void addBook(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException, SQLException {
    String title = request.getParameter("title");
    String category = request.getParameter("category");
    String author = request.getParameter("author");
    String addedDateStr = request.getParameter("addedDate");
    String bookShelf = request.getParameter("bookShelf");
    String copiesAvailableStr = request.getParameter("copiesAvailable");
    String acquireBy = request.getParameter("acquireBy");

    int copiesAvailable = 0;

    Date addedDate = null;

    if (addedDateStr != null && !addedDateStr.isEmpty()) {
        try {
            // Assuming the date format is YYYY-MM-DD
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(addedDateStr);
            addedDate = new Date(parsedDate.getTime());
        } catch (ParseException e) {
            response.getWriter().write("Invalid date format");
            return;
        }
    }


    if (copiesAvailableStr != null && !copiesAvailableStr.isEmpty()) {
        try {
            copiesAvailable = Integer.parseInt(copiesAvailableStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid copies available format");
            return;
        }
    }

    Part filePart = request.getPart("image");
    InputStream inputStream = null;

    if (filePart != null && filePart.getSize() > 0) {
        inputStream = filePart.getInputStream();
    }

    // Check and insert author if necessary, then insert book
    String checkAuthorQuery = "SELECT COUNT(*) FROM author WHERE Aname = ?";
    try (PreparedStatement checkAuthorStmt = conn.prepareStatement(checkAuthorQuery)) {
        checkAuthorStmt.setString(1, author);
        ResultSet rs = checkAuthorStmt.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            String insertAuthorQuery = "INSERT INTO author (Aname) VALUES (?)";
            try (PreparedStatement insertAuthorStmt = conn.prepareStatement(insertAuthorQuery)) {
                insertAuthorStmt.setString(1, author);
                insertAuthorStmt.executeUpdate();
            }
        }
    }

    String insertBookQuery = "INSERT INTO book (Title, Category, AuthorName, addedDate, BookShelf, CopiesAvailable, AcquireBy, Image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement insertBookStmt = conn.prepareStatement(insertBookQuery)) {
        insertBookStmt.setString(1, title);
        insertBookStmt.setString(2, category);
        insertBookStmt.setString(3, author);
        insertBookStmt.setDate(4, addedDate);
        insertBookStmt.setString(5, bookShelf);
        insertBookStmt.setInt(6, copiesAvailable);
        insertBookStmt.setString(7, acquireBy);

        if (inputStream != null) {
            insertBookStmt.setBlob(8, inputStream);
        } else {
            insertBookStmt.setNull(8, java.sql.Types.BLOB);
        }

        int rowsAffected = insertBookStmt.executeUpdate();
        String message = rowsAffected > 0 ? "Book added successfully" : "Failed to add book";
        request.setAttribute("message", message);
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }
}

private void editBook(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException, SQLException {
    int bookId = 0;
    String bookIdStr = request.getParameter("bookId");
    if (bookIdStr != null && !bookIdStr.isEmpty()) {
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid book ID format");
            return;
        }
    } else {
        response.getWriter().write("Book ID is required");
        return;
    }

    String title = request.getParameter("title");
    String category = request.getParameter("category");
    String author = request.getParameter("author");
    String addedDateStr = request.getParameter("addedDate");
    String bookShelf = request.getParameter("bookShelf");
    String copiesAvailableStr = request.getParameter("copiesAvailable");
    String acquireBy = request.getParameter("acquireBy");

    int addedDate = 0;
    int copiesAvailable = 0;

    if (addedDateStr != null && !addedDateStr.isEmpty()) {
        try {
            addedDate = Integer.parseInt(addedDateStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid year format");
            return;
        }
    }

    if (copiesAvailableStr != null && !copiesAvailableStr.isEmpty()) {
        try {
            copiesAvailable = Integer.parseInt(copiesAvailableStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid copies available format");
            return;
        }
    }

    Part filePart = request.getPart("image");
    InputStream inputStream = null;

    if (filePart != null && filePart.getSize() > 0) {
        inputStream = filePart.getInputStream();
    }

    // Check and insert author if necessary, then update book
    String checkAuthorQuery = "SELECT COUNT(*) FROM author WHERE Aname = ?";
    try (PreparedStatement checkAuthorStmt = conn.prepareStatement(checkAuthorQuery)) {
        checkAuthorStmt.setString(1, author);
        ResultSet rs = checkAuthorStmt.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            String insertAuthorQuery = "INSERT INTO author (Aname) VALUES (?)";
            try (PreparedStatement insertAuthorStmt = conn.prepareStatement(insertAuthorQuery)) {
                insertAuthorStmt.setString(1, author);
                insertAuthorStmt.executeUpdate();
            }
        }
    }

    String updateBookQuery = "UPDATE book SET Title = ?, Category = ?, AuthorName = ?, addedDate = ?, BookShelf = ?, CopiesAvailable = ?, AcquireBy = ?, Image = ? WHERE BookId = ?";
    try (PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery)) {
        updateBookStmt.setString(1, title);
        updateBookStmt.setString(2, category);
        updateBookStmt.setString(3, author);
        updateBookStmt.setInt(4, addedDate);
        updateBookStmt.setString(5, bookShelf);
        updateBookStmt.setInt(6, copiesAvailable);
        updateBookStmt.setString(7, acquireBy);

        if (inputStream != null) {
            updateBookStmt.setBlob(8, inputStream);
        } else {
            updateBookStmt.setNull(8, java.sql.Types.BLOB);
        }

        updateBookStmt.setInt(9, bookId);

        int rowsAffected = updateBookStmt.executeUpdate();
        String message = rowsAffected > 0 ? "Book updated successfully" : "Failed to update book";
        request.setAttribute("message", message);
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }
}

private void deleteBook(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException, SQLException {
    int bookId = Integer.parseInt(request.getParameter("bookId"));

    String deleteBookQuery = "DELETE FROM book WHERE BookId = ?";
    try (PreparedStatement deleteBookStmt = conn.prepareStatement(deleteBookQuery)) {
        deleteBookStmt.setInt(1, bookId);
        int rowsAffected = deleteBookStmt.executeUpdate();
        String message = rowsAffected > 0 ? "Book deleted successfully" : "Failed to delete book";
        request.setAttribute("message", message);
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }
}

private void addReview(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException, SQLException {
    String bookIdStr = request.getParameter("bookId");
    int bookId = 0;
    if (bookIdStr != null && !bookIdStr.isEmpty()) {
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid book ID format");
            return;
        }
    } else {
        response.getWriter().write("Book ID is required");
        return;
    }

    String reviewerName = request.getParameter("reviewerName");
    String reviewContent = request.getParameter("reviewContent");

    String insertReviewQuery = "INSERT INTO review (BookId, ReviewerName, ReviewContent, CreatedAt) VALUES (?, ?, ?, NOW())";
    try (PreparedStatement insertReviewStmt = conn.prepareStatement(insertReviewQuery)) {
        insertReviewStmt.setInt(1, bookId);
        insertReviewStmt.setString(2, reviewerName);
        insertReviewStmt.setString(3, reviewContent);

        int rowsAffected = insertReviewStmt.executeUpdate();
        String message = rowsAffected > 0 ? "Review added successfully" : "Failed to add review";
        request.setAttribute("message", message);
        request.getRequestDispatcher("/bookDetails.jsp?bookId=" + bookId).forward(request, response);
    }
}

private void deleteReview(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException, SQLException {
    String reviewIdStr = request.getParameter("reviewId");
    int reviewId = 0;
    if (reviewIdStr != null && !reviewIdStr.isEmpty()) {
        try {
            reviewId = Integer.parseInt(reviewIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid review ID format");
            return;
        }
    } else {
        response.getWriter().write("Review ID is required");
        return;
    }

    String bookIdStr = request.getParameter("bookId");
    int bookId = 0;
    if (bookIdStr != null && !bookIdStr.isEmpty()) {
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid book ID format");
            return;
        }
    } else {
        response.getWriter().write("Book ID is required");
        return;
    }

    String deleteReviewQuery = "DELETE FROM review WHERE ReviewId = ?";
    try (PreparedStatement deleteReviewStmt = conn.prepareStatement(deleteReviewQuery)) {
        deleteReviewStmt.setInt(1, reviewId);
        int rowsAffected = deleteReviewStmt.executeUpdate();
        String message = rowsAffected > 0 ? "Review deleted successfully" : "Failed to delete review";
        request.setAttribute("message", message);
        request.getRequestDispatcher("/bookDetails.jsp?bookId=" + bookId).forward(request, response);
    }
}

private void addRating(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException, SQLException {
    String bookIdStr = request.getParameter("bookId");
    int bookId = 0;
    if (bookIdStr != null && !bookIdStr.isEmpty()) {
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid book ID format");
            return;
        }
    } else {
        response.getWriter().write("Book ID is required");
        return;
    }

    String reviewerName = request.getParameter("reviewerName");
    String ratingStr = request.getParameter("rating");
    int rating = 0;

    if (ratingStr != null && !ratingStr.isEmpty()) {
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid rating format");
            return;
        }
    } else {
        response.getWriter().write("Rating is required");
        return;
    }

    String insertRatingQuery = "INSERT INTO rating (BookId, ReviewerName, Rating) VALUES (?, ?, ?)";
    try (PreparedStatement insertRatingStmt = conn.prepareStatement(insertRatingQuery)) {
        insertRatingStmt.setInt(1, bookId);
        insertRatingStmt.setString(2, reviewerName);
        insertRatingStmt.setInt(3, rating);

        int rowsAffected = insertRatingStmt.executeUpdate();
        String message = rowsAffected > 0 ? "Rating added successfully" : "Failed to add rating";
        request.setAttribute("message", message);
        request.getRequestDispatcher("/bookDetails.jsp?bookId=" + bookId).forward(request, response);
    }
}

private void removeRating(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException, SQLException {
    String bookIdStr = request.getParameter("bookId");
    int bookId = 0;
    if (bookIdStr != null && !bookIdStr.isEmpty()) {
        try {
            bookId = Integer.parseInt(bookIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid book ID format");
            return;
        }
    } else {
        response.getWriter().write("Book ID is required");
        return;
    }

    String reviewerName = request.getParameter("reviewerName");

    String deleteRatingQuery = "DELETE FROM rating WHERE BookId = ? AND ReviewerName = ?";
    try (PreparedStatement deleteRatingStmt = conn.prepareStatement(deleteRatingQuery)) {
        deleteRatingStmt.setInt(1, bookId);
        deleteRatingStmt.setString(2, reviewerName);
        int rowsAffected = deleteRatingStmt.executeUpdate();
        String message = rowsAffected > 0 ? "Rating removed successfully" : "Failed to remove rating";
        request.setAttribute("message", message);
        request.getRequestDispatcher("/bookDetails.jsp?bookId=" + bookId).forward(request, response);
    }

}
}

