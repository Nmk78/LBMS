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
import java.net.URLEncoder;
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
                case "pinReview":
                	togglePinReview(request, conn, response);
                    break;
                case "addOrUpdateRating":
                	addOrUpdateRating(request, conn, response);
                	break;
                case "borrowBook":
                    borrowBook(request, conn, response);
                    break;
//                case "removeRating":
//                    removeRating(request, conn, response);
//                    break;
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
    String author = request.getParameter("author");
    String addedDateStr = request.getParameter("addedDate");
    String bookShelf = request.getParameter("bookshelf");
    String copiesAvailableStr = request.getParameter("copy");
    String acquireBy = request.getParameter("acquireBy");


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

    int copiesAvailable = 0;

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

    String insertBookQuery = "INSERT INTO book (Title, AuthorName, addedDate, BookShelf, copy, CopiesAvailable, AcquireBy, Image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement insertBookStmt = conn.prepareStatement(insertBookQuery)) {
        insertBookStmt.setString(1, title);
        insertBookStmt.setString(2, author);
        insertBookStmt.setDate(3, addedDate);
        insertBookStmt.setString(4, bookShelf);
        insertBookStmt.setInt(5, copiesAvailable);
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
    String author = request.getParameter("author");
    String addedDateStr = request.getParameter("addedDate");
    String bookShelf = request.getParameter("bookShelf");
    String copiesAvailableStr = request.getParameter("copy");
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

    String updateBookQuery = "UPDATE book SET Title = ?,  AuthorName = ?, addedDate = ?, BookShelf = ?, CopiesAvailable = ?, AcquireBy = ?, Image = ? WHERE BookId = ?";
    try (PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuery)) {
        updateBookStmt.setString(1, title);
        updateBookStmt.setString(2, author);
        updateBookStmt.setInt(3, addedDate);
        updateBookStmt.setString(4, bookShelf);
        updateBookStmt.setInt(5, copiesAvailable);
        updateBookStmt.setString(6, acquireBy);

        if (inputStream != null) {
            updateBookStmt.setBlob(7, inputStream);
        } else {
            updateBookStmt.setNull(7, java.sql.Types.BLOB);
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

    String MemberId = request.getParameter("MemberId");
    String reviewContent = request.getParameter("reviewContent");

    String insertReviewQuery = "INSERT INTO review (BookId, MemberId, ReviewContent, CreatedAt) VALUES (?, ?, ?, NOW())";
    try (PreparedStatement insertReviewStmt = conn.prepareStatement(insertReviewQuery)) {
        insertReviewStmt.setInt(1, bookId);
        insertReviewStmt.setString(2, MemberId);
        insertReviewStmt.setString(3, reviewContent);

        int rowsAffected = insertReviewStmt.executeUpdate();
        String message = rowsAffected > 0 ? "Review added successfully" : "Failed to add review";
        request.setAttribute("message", message);
        response.sendRedirect(request.getContextPath() + "/book?id=" + bookId + "&message=" + URLEncoder.encode(message, "UTF-8"));
    }
}

private void togglePinReview(HttpServletRequest request, Connection conn, HttpServletResponse response) 
        throws ServletException, IOException, SQLException {
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

    // Check the current pin status
    String checkPinStatusQuery = "SELECT IsPinned FROM review WHERE ReviewId = ?";
    try (PreparedStatement checkStmt = conn.prepareStatement(checkPinStatusQuery)) {
        checkStmt.setInt(1, reviewId);
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next()) {
                boolean isPinned = rs.getBoolean("IsPinned");
                
                // Toggle the pin status
                String togglePinQuery = "UPDATE review SET IsPinned = ? WHERE ReviewId = ?";
                try (PreparedStatement toggleStmt = conn.prepareStatement(togglePinQuery)) {
                    toggleStmt.setBoolean(1, !isPinned);
                    toggleStmt.setInt(2, reviewId);
                    int rowsUpdated = toggleStmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        String message = isPinned ? "Review unpinned successfully" : "Review pinned successfully";
                        response.getWriter().write(message);
                    } else {
                        response.getWriter().write("Failed to toggle pin status");
                    }
                }
            } else {
                response.getWriter().write("Review not found");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        response.getWriter().write("An error occurred while toggling the pin status");
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
        request.getRequestDispatcher("/book.jsp?id=" + bookId).forward(request, response);
    }
}


private void addOrUpdateRating(HttpServletRequest request, Connection conn, HttpServletResponse response) 
        throws ServletException, IOException, SQLException {
    String bookIdStr = request.getParameter("bookId");
    String memberIdStr = request.getParameter("memberId"); // memberId is a String, no parsing to int needed
    String ratingStr = request.getParameter("rating");
    
    int bookId = 0;
    int rating = 0;

    // Validate and parse input parameters
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

    if (memberIdStr == null || memberIdStr.isEmpty()) {
        response.getWriter().write("Member ID is required");
        return;
    }

    if (ratingStr != null && !ratingStr.isEmpty()) {
        try {
            rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) { // Assuming ratings are between 1 and 5
                response.getWriter().write("Rating must be between 1 and 5");
                return;
            }
        } catch (NumberFormatException e) {
            response.getWriter().write("Invalid rating format");
            return;
        }
    } else {
        response.getWriter().write("Rating is required");
        return;
    }

    // Check if the rating already exists
    String checkRatingQuery = "SELECT RatingValue FROM rating WHERE BookId = ? AND MemberId = ?";
    try (PreparedStatement checkStmt = conn.prepareStatement(checkRatingQuery)) {
        checkStmt.setInt(1, bookId);
        checkStmt.setString(2, memberIdStr);
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next()) {
                // Rating exists, so update it
                String updateRatingQuery = "UPDATE rating SET RatingValue = ? WHERE BookId = ? AND MemberId = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateRatingQuery)) {
                    updateStmt.setInt(1, rating);
                    updateStmt.setInt(2, bookId);
                    updateStmt.setString(3, memberIdStr);
                    int rowsUpdated = updateStmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        String message = "Rated This Book";
                        request.setAttribute("message", message);
                        response.sendRedirect(request.getContextPath() + "/book?id=" + bookId + "&message=" + URLEncoder.encode(message, "UTF-8"));
                    } else {
                        String message = "Failed to update this Book";
                        request.setAttribute("message", message);
                        response.sendRedirect(request.getContextPath() + "/book?id=" + bookId + "&message=" + URLEncoder.encode(message, "UTF-8"));
                        }
                }
            } else {
                // Rating does not exist, so insert a new one
                String insertRatingQuery = "INSERT INTO rating (BookId, MemberId, RatingValue, CreatedAt) VALUES (?, ?, ?, NOW())";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertRatingQuery)) {
                    insertStmt.setInt(1, bookId);
                    insertStmt.setString(2, memberIdStr);
                    insertStmt.setInt(3, rating);
                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        String message = "Rated This Book";
                        request.setAttribute("message", message);
                        response.sendRedirect(request.getContextPath() + "/book?id=" + bookId + "&message=" + URLEncoder.encode(message, "UTF-8"));
                    } else {
                        String message = "Failed to rate";
                        request.setAttribute("message", message);
                        response.sendRedirect(request.getContextPath() + "/book?id=" + bookId + "&message=" + URLEncoder.encode(message, "UTF-8"));
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        response.getWriter().write("An error occurred while processing the rating");
    }
}

private void borrowBook(HttpServletRequest request, Connection conn, HttpServletResponse response) throws ServletException, IOException {
    // Retrieve form parameters
	int bookId = Integer.parseInt(request.getParameter("bookid"));
	String memberId = request.getParameter("memberid"); // Change to String
	String loanDate = request.getParameter("loanDate");
	String returnDate = request.getParameter("returnDate");
	String dueDate = request.getParameter("dueDate");

	// SQL query to insert data into the loan table
	String sql = "INSERT INTO loan (bookid, memberid, loanDate, returnDate, dueDate) VALUES (?, ?, ?, ?, ?)";
	try (PreparedStatement statement = conn.prepareStatement(sql)) {
	    statement.setInt(1, bookId);
	    statement.setString(2, memberId); // Set as String
	    statement.setString(3, loanDate);
	    if (returnDate != null && !returnDate.isEmpty()) {
	        statement.setString(4, returnDate);
	    } else {
	        statement.setNull(4, java.sql.Types.DATE);
	    }
	    statement.setString(5, dueDate);

	    int row = statement.executeUpdate();
	    if (row > 0) {
	        // Redirect to success page or display success message
	        String message = "Book borrowed successfully";
	        request.setAttribute("message", message);
	        request.getRequestDispatcher("/admin.jsp").forward(request, response);  
	    } else {
	        // Redirect to error page or display error message
	        String message = "Failed to borrow book";
	        request.setAttribute("message", message);
	        request.getRequestDispatcher("/admin.jsp").forward(request, response);              
	    }
	} catch (Exception ex) {
	    throw new ServletException("Error: " + ex.getMessage(), ex);
	}
}

}



