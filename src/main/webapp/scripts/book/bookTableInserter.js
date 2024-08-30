function viewLoan(loanId) {
  const loanModal = document.getElementById("viewLoanModal");
  const loadingText = document.getElementById("loadingText");
  const loanDetails = document.getElementById("loanDetails");
  const closeModal = document.getElementById("closeModal");

  // Show the modal and loading text
  loanModal.classList.remove("hidden");
  loadingText.classList.remove("hidden");
  loanDetails.classList.add("hidden");

  fetch(`/LBMS/loan?action=view&loanId=${loanId}`)
    .then(response => response.json())
    .then(data => {
      console.log("View Loans", data);

	let img = `<img class="w-full h-full object-cover transition-transform group-hover:scale-110" src="${data.image}" alt="Book">`

      // Populate the modal with loan details
      document.getElementById("imageContainer").innerHTML = img;
      
      document.getElementById("loanId").textContent = data.id;
      document.getElementById("bookName").textContent = data.name;
      document.getElementById("memberName").textContent = data.memberName;
      document.getElementById("dueingDate").textContent = formatDate(data.dueDate);

      // Hide loading text and show loan details
      loadingText.classList.add("hidden");
      loanDetails.classList.remove("hidden");
    })
    .catch(error => {
      console.error('Error fetching loan details:', error);
      // Optionally, you could show an error message in the modal here
      loadingText.textContent = "Error loading data.";
    });

  // Close modal on button click
  closeModal.addEventListener("click", () => {
    loanModal.classList.add("hidden");
  });
}

function formatDate(dateString) {
    const options = { month: 'short', day: 'numeric', year: 'numeric' };
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', options);
}

document.getElementById("extendLoanBtn").addEventListener("click", function() {
    const loanId = document.getElementById("loanId").textContent;

    fetch(`/LBMS/loan?action=extend&loanId=${loanId}`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Loan extended successfully!");
                // Optionally, update the due date in the modal
                document.getElementById("dueDate").textContent = formatDate(new Date(new Date().setDate(new Date().getDate() + 5)));
            } else {
                alert(data.message);
            }
        })
        .catch(error => console.error('Error extending loan:', error));
});

document.getElementById("returnLoanBtn").addEventListener("click", function() {
    const loanId = document.getElementById("loanId").textContent;

    fetch(`/LBMS/loan?action=return&loanId=${loanId}`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Loan marked as returned!");
                // Optionally, update the UI to reflect the return
                loanModal.classList.add("hidden");
                listLoans(); // Refresh the loan list to remove the returned loan
            } else {
                alert(data.message);
            }
        })
        .catch(error => console.error('Error marking loan as returned:', error));
});


document.addEventListener("DOMContentLoaded", function () {
	  console.log("Loading Status");

	    fetch('/LBMS/SystemStatus')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("totalBooks").textContent = data.totalBooks;
            document.getElementById("overdueBooks").textContent = data.overdueBooks;
            document.getElementById("borrowedBooks").textContent = data.borrowedBooks;
            document.getElementById("totalMembers").textContent = data.totalMembers;
        })
        .catch(error => {
            console.error('Error fetching stats:', error);
        });
  console.log("Table loaded");

  let books = [];
  let loans = [];

  let sortedBooks = [];
  let sortedLoans = [];
  let sortOrder = {};

  const booksTableBody = document.getElementById("books-table-body");
  const loanTableBody = document.getElementById("loan-table-body");

  const bookSearchInput = document.getElementById("books-search-input");
  const loanSearchInput = document.getElementById("loan-search-input");
  // Example function to view a specific loan




  function renderBooksTable(data) {
    booksTableBody.innerHTML = ""; // Clear the existing table rows
    console.log("Books Table Rendered");
    data.forEach((book) => {
      const row = document.createElement("tr");
      row.classList.add("border-b", "hover:bg-gray-100");
      row.innerHTML = `
        <td class="py-2 px-4">${book.name}</td>
        <td class="py-2 px-4"><img src="${book.image}" alt="${book.title}" class="w-16 h-16 object-cover"></td>
        <td class="py-2 px-4">${book.author}</td>
        <td class="py-2 px-4">${book.category}</td>
        <td class="py-2 px-4">${book.availability}</td>
        <td class="py-2 px-4">${book.id}</td>
        <td class="py-2 px-4">${book.AcquireBy}</td>
        <td class="py-2 px-4">
			<button class="bg-blue-500 text-white px-3 py-1 hover:bg-blue-700" onclick='editBook(${JSON.stringify(book)})'>Edit</button>
        </td>
      `;
      booksTableBody.appendChild(row);
    });
  }

function renderLoanTable(data) {
  loanTableBody.innerHTML = ""; // Clear existing content
  console.log("Loans Table Rendered");

  const today = new Date();

  data.forEach((loan) => {
    // Parse the dueDate to a Date object
    const dueDate = new Date(loan.dueDate);
    // Determine if the due date is past
    const isOverdue = dueDate < today;

    const row = `
      <tr class="border-b hover:bg-gray-100">
        <td class="py-2 px-2 line-clamp-4">${loan.name}</td>
        <td class="py-2 px-2 w-[100px] truncate">${loan.memberName}</td>
        <td class="py-2 w-[100px] px-2 ${isOverdue ? 'bg-red-500 text-white' : ''}">
          ${formatDate(loan.dueDate)}
        </td>
        <td class="py-2 px-2">
          <button class="bg-blue-500 text-white px-3 py-1 hover:bg-blue-700" onclick="viewLoan(${loan.id})">View</button>
        </td>
      </tr>
    `;
    loanTableBody.insertAdjacentHTML("beforeend", row);
  });
}


  function sortTable(column, data) {
    const sortDirection = sortOrder[column] === "asc" ? "desc" : "asc";
    sortOrder[column] = sortDirection;

    data.sort((a, b) => {
      if (a[column] < b[column]) return sortDirection === "asc" ? -1 : 1;
      if (a[column] > b[column]) return sortDirection === "asc" ? 1 : -1;
      return 0;
    });

    return data;
  }

  function filterTable(query, data) {
	console.log(data)
    return data.filter((item) => {
      return (
        item.name.toLowerCase().includes(query) ||
        (item.author && item.author.toLowerCase().includes(query)) ||
        item.category.toLowerCase().includes(query) 
        //||(item.memberName && item.memberName.toLowerCase().includes(query))
      );
    });
  }

  bookSearchInput.addEventListener("input", function () {
    const query = this.value.toLowerCase();
    const filteredBooks = filterTable(query, books);
    renderBooksTable(filteredBooks);
  });

  document.querySelectorAll("th[data-sort-books]").forEach((header) => {
    header.addEventListener("click", function () {
      const column = this.getAttribute("data-sort-books");
      sortedBooks = sortTable(column, [...books]);
      renderBooksTable(sortedBooks);
    });
  });

  loanSearchInput.addEventListener("input", function () {
    const query = this.value.toLowerCase();
    const filteredLoans = filterTable(query, loans);
    renderLoanTable(filteredLoans);
  });

  document.querySelectorAll("th[data-sort-loan]").forEach((header) => {
    header.addEventListener("click", function () {
      const column = this.getAttribute("data-sort-loan");
      sortedLoans = sortTable(column, [...loans]);
      renderLoanTable(sortedLoans);
    });
  });

  // Fetch book data and initialize the table
  fetch('/LBMS/books')
    .then(response => response.json())
    .then(data => {
      books = data;
      sortedBooks = [...books]; // Initialize sortedBooks with the fetched data
      renderBooksTable(books);
      console.log(books)
    })
    .catch(error => console.error('Error fetching books:', error));

  // Fetch loan data and initialize the table
  function listLoans() {
    fetch('/LBMS/loan?action=list')
      .then(response => response.json())
      .then(data => {
        loans = data;
        sortedLoans = [...loans]; // Initialize sortedLoans with the fetched data
        renderLoanTable(loans);
        console.log("Loans", loans)
      })
      .catch(error => console.error('Error fetching loan list:', error));
  }


function formatDate(dateString) {
    const options = { month: 'short', day: 'numeric' };
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', options);
}

  // Initial call to list loans when the page loads
  listLoans();
});
