package Book;

import java.sql.Blob;
import java.util.Date;

public class BorrowedBook {
    private int bid;
    private String title;
    private String category;
    private String authorName;
    private int bookShelf;
    private int copiesAvailable;
    private String acquireBy;
    private String image;
    private Date dueDate;
    private Date borrowedDate;

    // Getters and setters for all fields

    public int getBid() { return bid; }
    public void setBid(int bid) { this.bid = bid; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public int getBookShelf() { return bookShelf; }
    public void setBookShelf(int bookShelf) { this.bookShelf = bookShelf; }

    public int getCopiesAvailable() { return copiesAvailable; }
    public void setCopiesAvailable(int copiesAvailable) { this.copiesAvailable = copiesAvailable; }

    public String getAcquireBy() { return acquireBy; }
    public void setAcquireBy(String acquireBy) { this.acquireBy = acquireBy; }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public Date getBorrowedDate() { return borrowedDate; }
    public void setBorrowedDate(Date borrowedDate) { this.borrowedDate = borrowedDate; }
}

