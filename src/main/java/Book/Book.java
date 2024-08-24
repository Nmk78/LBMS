package Book;

import java.sql.Blob;
import java.util.Date;

public class Book {
    private int bid;
    private String title;
    private String category;
    private String authorName;
    private Date addedDate;
    private int bookShelf;
    private int copiesAvailable;
    private String acquireBy;
    private Blob image;
    private String availability;

    // Getters and setters for each field

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public int getBookShelf() {
        return bookShelf;
    }

    public void setBookShelf(int i) {
        this.bookShelf = i;
    }

    public int getCopiesAvailable() {
        return copiesAvailable;
    }

    public void setCopiesAvailable(int copiesAvailable) {
        this.copiesAvailable = copiesAvailable;
    }

    public String getAcquireBy() {
        return acquireBy;
    }

    public void setAcquireBy(String acquireBy) {
        this.acquireBy = acquireBy;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
    // Additional methods (if necessary)
    
    /**
     * Convert Blob to String (e.g., Base64 encoded image)
     * @return Base64 encoded image string
     */
    public String getImageAsBase64() {
        if (image == null) {
            return null;
        }
        try {
            byte[] imageBytes = image.getBytes(1, (int) image.length());
            return java.util.Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
