/**
 *
 */

document.addEventListener("DOMContentLoaded", function () {
  console.log("Table loaded");
  const books = [
    {
      title: "Book One",
      image: "./assets/img/sample1.jpg",
      author: "Author One",
      category: "Category One",
      available: true,
      id: "1",
      acquireBy: "buy",
    },
    {
      title: "Book Two",
      image: "./assets/img/sample1.jpg",
      author: "Author Two",
      category: "Category Two",
      available: false,
      id: "2",
      acquireBy: "rent",
    },
    {
      title: "Book Three",
      image: "./assets/img/sample4.jpg",
      author: "Author Three",
      category: "Category Three",
      available: true,
      id: "3",
      acquireBy: "buy",
    },
    {
      title: "AAA",
      image: "./assets/img/sample4.jpg",
      author: "Author Three",
      category: "Category Three",
      available: true,
      id: "4",
      acquireBy: "buy",
    },
  ];

  let sortedBooks = [...books];
  let sortOrder = {};

  const booksTableBody = document.getElementById("books-table-body");
  const loanTableBody = document.getElementById("loan-table-body");

  const bookSearchInput = document.getElementById("books-search-input");
  const loanSearchInput = document.getElementById("loan-search-input");

  function renderBooksTable(data) {
    // Ensure booksTableBody is correctly referenced
    booksTableBody.innerHTML = ""; // Clear the existing table rows
    console.log("Books Table Rendered");
    data.forEach((book) => {
      // Create a new table row element
      const row = document.createElement("tr");
      row.classList.add("border-b", "hover:bg-gray-100");
      // console.log(book);
      // Create table cells and append them to the row
      row.innerHTML = `
                    <td class="py-2 px-4">${book.title}</td>
                    <td class="py-2 px-4"><img src="${book.image}" alt="${
        book.title
      }" class="w-16 h-16 object-cover"></td>
                    <td class="py-2 px-4">${book.author}</td>
                    <td class="py-2 px-4">${book.category}</td>
                    <td class="py-2 px-4">${book.available ? "Yes" : "No"}</td>
                    <td class="py-2 px-4">${book.id}</td>
                    <td class="py-2 px-4">${book.acquireBy}</td>
                    <td class="py-2 px-4">
                        <button class="bg-blue-500 text-white px-3 py-1 hover:bg-blue-700" onclick="editBook('${
                          book.id
                        }')">Edit</button>
                    </td>
                `;

      // Append the row to the table body
      booksTableBody.appendChild(row);
    });
  }

  function renderLoanTable(data) {
    console.log('renderLoanTable');
    loanTableBody.innerHTML = ""; // Clear existing content
    data.forEach((book) => {
      const row = `
        	        <tr class="border-b hover:bg-gray-100">
        	            <td class="py-2 px-4">${book.title}</td>
        	            <td class="py-2 px-4">${book.author}</td>
        	            <td class="py-2 px-4">${book.category}</td>
        	            <td class="py-2 px-4">${book.available ? "Yes" : "No"}</td>
        	            <td class="py-2 px-4">
        	                <button class="bg-blue-500 text-white px-3 py-1 hover:bg-blue-700" onclick="editBook('${
                            book.id
                          }')">Edit</button>
        	            </td>
        	        </tr>
        	    `;
      loanTableBody.insertAdjacentHTML("beforeend", row);
    });
  }

  /// Book Table Manupulation Functions
  function sortBookTable(column) {
    const sortDirection = sortOrder[column] === "asc" ? "desc" : "asc";
    sortOrder[column] = sortDirection;

    sortedBooks.sort((a, b) => {
      if (a[column] < b[column]) return sortDirection === "asc" ? -1 : 1;
      if (a[column] > b[column]) return sortDirection === "asc" ? 1 : -1;
      return 0;
    });

    renderBooksTable(sortedBooks);
  }

  function filterBooksTable(query) {
    const filteredBooks = books.filter((book) => {
      return (
        book.title.toLowerCase().includes(query) ||
        book.author.toLowerCase().includes(query) ||
        book.category.toLowerCase().includes(query)
      );
    });

    renderBooksTable(filteredBooks);
  }

  bookSearchInput.addEventListener("input", function () {
    filterBooksTable(this.value.toLowerCase());
  });

  document.querySelectorAll("th[data-sort-books]").forEach((header) => {
    header.addEventListener("click", function () {
      sortBookTable(this.getAttribute("data-sort-books"));  
    });
  });

  /// Loan Table Manipulation Functions
  function sortLoanTable(column) {
    const sortDirection = sortOrder[column] === "asc" ? "desc" : "asc";
    sortOrder[column] = sortDirection;

    sortedBooks.sort((a, b) => {
      if (a[column] < b[column]) return sortDirection === "asc" ? -1 : 1;
      if (a[column] > b[column]) return sortDirection === "asc" ? 1 : -1;
      return 0;
    });

    renderLoanTable(sortedBooks);
  }

  function filterLoanTable(query) {
    const filteredBooks = books.filter((book) => {
      return (
        book.title.toLowerCase().includes(query) ||
        book.author.toLowerCase().includes(query) ||
        book.category.toLowerCase().includes(query)
      );
    });

    renderLoanTable(filteredBooks);
  }

  loanSearchInput.addEventListener("input", function () {
    filterLoanTable(this.value.toLowerCase());
  });

  document.querySelectorAll("th[data-sort-loan]").forEach((header) => {
    header.addEventListener("click", function () {
      sortLoanTable(this.getAttribute("data-sort-loan"));
    });
  });

  // Initial render
  console.log("Initial Books:", books); // Debugging log
  renderBooksTable(books);
  renderLoanTable(books);
});
