package Book;

import java.sql.Date;

public class LoanClass {
    private int id;
    private int bookId;
    private String memberId;
    private Date loanDate;
    private Date returnDate;
    private Date dueDate;
    private String status;
    private String notes;
    private String name; // Add this field
    private String memberName; // Add this field
	private String image;
    // Constructor
 // Modify your LoanClass constructor to accept the image field
    public LoanClass(int id, int bookId, String memberId, Date loanDate, Date returnDate, Date dueDate, String status, String notes, String bookTitle, String memberName, String image) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.status = status;
        this.notes = notes;
        this.name = bookTitle;
        this.memberName = memberName;
        this.image = image; // Assign the image
    }


    // Default constructor
    public LoanClass() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", memberId='" + memberId + '\'' +
                ", loanDate=" + loanDate +
                ", returnDate=" + returnDate +
                ", dueDate=" + dueDate +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
