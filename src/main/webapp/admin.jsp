<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="ISO-8859-1" />
    <title>Admin | LBMS</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
      integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
      crossorigin="anonymous"
      referrerpolicy="no-referrer"
    />
    <link rel="stylesheet" href="global.css" />
    
    <style>
        .resizable {
            position: relative;
            overflow: auto;
            resize: vertical; /* Allows vertical resizing */
			min-height: 400px; /* Minimum height */
           
        }
        .resizable::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 100%;
            height: 8px; /* Height of the resize handle */
            cursor: ns-resize;
            background-color: rgba(0, 0, 0, 0.1);
        }
    </style>
    
  </head>
  <body class="relative">
    <!-- Navbar -->
    <div id="toast" class="hidden fixed top-4 right-4 p-4 bg-[--bg] border-gray-[--secondary] text-white rounded shadow-lg z-50 opacity-0 transition-opacity duration-500">
        
    </div>
    <nav
      id="nav"
      class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow-0 px-20 shadow-slate-300 bg-[--bg]"
    ></nav>
      
        <div id="book-drawer" class="fixed top-0 right-0 w-[340px] h-full bg-white shadow-lg transform translate-x-full transition-transform duration-300 z-50">
	        <div id="drawer-container" class="p-6">
	            <div class="w-full flex justify-between">
	            	<h2 id="drawer-title" class="text-2xl text-[--secondary] font-bold mb-4">Add New Post</h2>
	            	<button id="drawer-close">Close</span>
	            </div>
            <form action="/LBMS/bookInsert" method="POST">

                <!-- Title -->
                <div class="mb-4">
                    <label for="title" class="block text-gray-700">Title</label>
                    <input type="text" id="title" name="title" class="w-full p-2 border rounded" required>
                </div>

                <!-- Category -->
                <div class="mb-4">
                    <label for="category" class="block text-gray-700">Category</label>
                    <input type="text" id="category" name="category" class="w-full p-2 border rounded">
                </div>

                <!-- Author Name -->
                <div class="mb-4">
                    <label for="author" class="block text-gray-700">Author Name</label>
                    <input type="text" id="author" name="author" class="w-full p-2 border rounded">
                </div>

                <!-- Add Year --> <!-- Copy -->
                <div class="mb-4 flex space-x-3">
                    <div class="w-1/2">
                    	<label for="addyear" class="block text-gray-700">Add Year</label>
                    	<input type="number" id="addyear" name="addyear" class="w-full p-2 border rounded" min="1000" max="9999" required>
                    </div>
					<div class="w-1/2">
                    	<label for="copy" class="block text-gray-700">Copy</label>
                    	<input type="number" id="copy" name="copy" class="w-full p-2 border rounded" value="1" required>
                	</div>
                </div>                

                <!-- Book Shelf -->
                <div class="mb-4">
                	<label for="bookshelf" class="block text-gray-700">Book Shelf</label>
                    <input type="text" id="bookshelf" name="bookshelf" class="w-full p-2 border rounded" required>
                    	
                </div>

                <!-- Acquire By -->
                <div class="mb-4">
                    <label for="acquireby" class="block text-gray-700">Acquire By</label>
                    <select id="acquireby" name="acquireby" class="w-full p-2 border rounded" required>
                        <option value="Buy">Buy</option>
                        <option value="Donate">Donate</option>
                    </select>
                </div>

                <!-- Submit Button -->
                <button type="submit" class="w-full bg-[--secondary] hover:shadow-md text-white p-2 rounded">Add Book</button>

            </form>
	        </div>
    </div> 

       <div id="edit-drawer" class="fixed top-0 right-0 w-80 h-full bg-white shadow-lg transform translate-x-full transition-transform duration-300 z-50">
        <div id="drawer-container" class="p-6">
            <div class="w-full flex justify-between">
            	<h2 id="drawer-title" class="text-2xl text-[--secondary] font-bold mb-4">Add New Book</h2>
            	<button id="edit-drawer-close">Close</span>
            </div>
            <form action="/LBMS/bookUpdateDemo" method="POST">

                <!-- Title -->
                <div class="mb-4">
                    <label for="title" class="block text-gray-700">Book Title</label>
                    <input type="text" id="title" name="title" class="w-full p-2 border rounded" required>
                </div>                

                <!-- Book Shelf -->
                <div class="mb-4">
                	<label for="bookshelf" class="block text-gray-700">Book Shelf</label>
                    <input type="text" id="bookshelf" name="bookshelf" class="w-full p-2 border rounded" required>
                    	
                </div>

                <!-- Acquire By -->
                <div class="mb-4">
                    <label for="acquireby" class="block text-gray-700">Duration</label>
                    <select id="acquireby" name="acquireby" class="w-full p-2 border rounded" required>
                        <option value="5">Student</option>
                        <option value="90">Staff</option>
                    </select>
                </div>

                <!-- Submit Button -->
                <button type="submit" class="w-full bg-[--secondary] hover:shadow-md text-white p-2 rounded">Borrow This Book</button>

            </form>
        </div>
    </div>
      
       <div id="post-drawer" class="fixed top-0 right-0 w-80 h-full bg-white shadow-lg transform translate-x-full transition-transform duration-300 z-50">
        <div id="edit-drawer-container" class="p-6">
            <div class="w-full flex justify-between">
            	<h2 id="drawer-title" class="text-2xl text-[--secondary] font-bold mb-4">Add New Post</h2>
            	<button id="drawer-close">Close</span>
            </div>
            <form action="/insert-book" method="POST">

                <!-- Title -->
                <div class="mb-4">
                    <label for="title" class="block text-gray-700">Title</label>
                    <input type="text" id="title" name="title" class="w-full p-2 border rounded" required>
                </div>

                <div class="mb-4">
    				<label for="description" class="block text-gray-700">Description</label>
    				<textarea id="description" name="description" class="w-full p-2 border rounded" rows="4" required></textarea>
			   </div>
			   <div class="mb-4"> 
					<div class="flex items-center justify-center w-full">
					    <label for="dropzone-file" class="flex flex-col items-center justify-center w-full h-64 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 hover:bg-gray-100 ">
					        <div class="flex flex-col items-center justify-center pt-5 pb-6">
					            <svg class="w-8 h-8 mb-4 text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 20 16">
					                <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"/>
					            </svg>
					            <p class="mb-2 text-sm text-gray-500"><span class="font-semibold">Click to upload</span> or drag and drop</p>
					            <p class="text-xs text-gray-500">SVG, PNG, JPG or GIF (MAX. 800x400px)</p>
					        </div>
					        <input id="dropzone-file" type="file" class="hidden" />
					    </label>
					</div> 
			   </div>
							   

                <!-- Submit Button -->
                <button type="submit" class="w-full bg-[--secondary] hover:shadow-md text-white p-2 rounded">Add Book</button>

            </form>
        </div>
    </div> 
	
	
	<div class="p-6 px-20 space-y-6">
       <!-- Top Boxes -->
	  <div class="w-full flex justify-around space-x-6">
	  	<div class="flex flex-1 justify-between space-x-6">
        <button
          id="drawer-button"
          class="flex items-center max-w-72 space-x-7 border border-[--accent] justify-between bg-white p-6 shadow-md "
        >
          <div class="text-start space-y-4">
            <div class="text-3xl font-bold">465</div>
            <div class="text-gray-600">Total Books</div>
          </div>
          <div class="w-12 h-12 flex items-center justify-center rounded-full">
            <div class="text-white text-lg font-bold">
              <img src="./assets/icons/addBook.svg" alt="Add Book" />
            </div>
          </div>
        </button>
                
        <button
          class="flex items-center max-w-72 space-x-7  border border-[--primary] justify-between bg-white p-6 shadow-md "
        >
          <div class="text-start space-y-4">
            <div class="text-3xl font-bold">65</div>
            <div class="text-gray-600">Books Borrowed</div>
          </div>
          <div class="w-12 h-12 flex items-center justify-center rounded-full">
            <div class="text-white text-lg font-bold">
              <img src="./assets/icons/hourglass.svg" alt="Borrow Book" />
            </div>
          </div>
        </button>
                		
		<button
		id="postDrawerButton"
          class="flex items-center max-w-72 space-x-7  border border-[--secondary] justify-between bg-white p-6 shadow-md "
        >
          <div class="text-start space-y-4">
            <div class="text-3xl font-bold">Post</div>
            <div class="text-gray-600">Announcement</div>
          </div>
          <div class="w-12 h-12 flex items-center justify-center rounded-full">
            <div class="text-white text-lg font-bold">
              <img src="./assets/icons/announce.svg" alt="Total Member" />
            </div>
          </div>
        </button>
        
        <button
          class="flex items-center max-w-72 space-x-7 justify-between bg-white p-6 shadow-md "
        >
          <div class="text-start space-y-4">
            <div class="text-3xl font-bold">465</div>
            <div class="text-gray-600">Overdued Books</div>
          </div>
          <div class="w-12 h-12 flex items-center justify-center rounded-full">
            <div class="text-white text-lg font-bold">
              <img src="./assets/icons/overdue.svg" alt="Overdue Book" />
            </div>
          </div>
        </button>
		
		<button
          class="flex items-center max-w-72 space-x-7 justify-between bg-white p-6 shadow-md "
        >
          <div class="text-start space-y-4">
            <div class="text-3xl font-bold">351</div>
            <div class="text-gray-600">Total Members</div>
          </div>
          <div class="w-12 h-12 flex items-center justify-center rounded-full">
            <div class="text-white text-lg font-bold">
              <img src="./assets/icons/member.svg" alt="Total Member" />
            </div>
          </div>
        </button>

      </div>
	 	<button class="px-4 w-40 mx-auto py-2 text-red-800 hover:text-white hover:bg-red-800">
        	Sign Out
        </button> 
	  </div>


      <!-- Bottom Content -->
	<div class="flex space-x-6">
		<div class="w-2/3 min-h-[400px] h-[450px] relative overflow-y-scroll resizable bg-white shadow-md">
            <div class="flex sticky top-0 bg-gray-50 justify-between items-center mb-4 pb-2">
                <input type="text" id="books-search-input" class="border border-gray-300 py-2 px-4 w-full max-w-xs" placeholder="Search books...">
            </div>
            <table class="min-w-full bg-gray-50 border border-gray-300">
                <thead class="sticky top-12 py-1 bg-gray-200 z-50">
                    <tr>
                        <th class="py-2 px-4 border-b cursor-pointer" data-sort="title">Title</th>
                        <th class="py-2 px-4 border-b">Image</th>
                        <th class="py-2 px-4 border-b cursor-pointer" data-sort="author">Author</th>
                        <th class="py-2 px-4 border-b cursor-pointer" data-sort="category">Category</th>
                        <th class="py-2 px-4 border-b cursor-pointer" data-sort="available">Available</th>
                        <th class="py-2 px-4 border-b cursor-pointer" data-sort="id">ID</th>
                        <th class="py-2 px-4 border-b">Acquired By</th>
                        <th class="py-2 px-4 border-b"></th>
                    </tr>
                </thead>
                <tbody id="books-table-body">
                    <!-- Rows will be injected here by JavaScript -->
                </tbody>
            </table>
        </div>

        <!-- Right Content -->
		<div class="w-1/3 min-h-[400px] h-[450px] relative overflow-y-scroll resizable bg-white shadow-md">
            <div class="flex sticky top-0 bg-gray-50 justify-between items-center mb-4 pb-2">
                <input type="text" id="loan-search-input" class="border border-gray-300 py-2 px-4 w-full max-w-xs" placeholder="Search loans...">
            </div>
            <table class="min-w-full bg-gray-50 border border-gray-300">
                <thead class="sticky top-12 py-1 w-full bg-gray-200 z-50">
                    <tr>
                        <th class="py-2 px-4 border-b cursor-pointer" data-sort="title">Title</th>
                        <th class="py-2 px-4 border-b cursor-pointer" data-sort="author">Author</th>
                        <th class="py-2 px-4 border-b cursor-pointer" data-sort="category">Category</th>
                        <th class="py-2 px-4 border-b"></th>
                    </tr>
                </thead>
                <tbody id="loan-table-body">
                    <!-- Rows will be injected here by JavaScript -->
                </tbody>
            </table>
        </div>
    </div>
	</div>
	

    <!-- JavaScript for Drawer Toggle -->
    
    <script src="./scripts/innerHtmlInserter.js"></script>
    <script>

    document.addEventListener('DOMContentLoaded', () => {
        const drawerButton = document.getElementById('drawer-button');
        const postDrawerButton = document.getElementById('postDrawerButton');
        const editDrawerButton = document.getElementById('book');

        const drawerClose = document.getElementById('drawer-close');
        const postDrawerClose = document.getElementById('post-drawer-close');
        const editDrawerClose = document.getElementById('edit-drawer-close');

        const drawer = document.getElementById('book-drawer');
        const postDrawer = document.getElementById('post-drawer');
        const editDrawer = document.getElementById('edit-drawer');

        // Toggle drawer for "Add Book" button
        drawerButton.addEventListener('click', () => {
            drawer.classList.toggle('translate-x-full');
            drawer.classList.toggle('translate-x-0');
        });

        drawerClose.addEventListener('click', () => {
            drawer.classList.toggle('translate-x-full');
            drawer.classList.toggle('translate-x-0');
        });

        // Toggle post drawer for "Announcement" button
        postDrawerButton.addEventListener('click', () => {
            postDrawer.classList.toggle('translate-x-full');
            postDrawer.classList.toggle('translate-x-0');
        });

        postDrawerClose.addEventListener('click', () => {
            postDrawer.classList.toggle('translate-x-full');
            postDrawer.classList.toggle('translate-x-0');
        });

        // Toggle edit drawer (if necessary)
        editDrawerButton.addEventListener('click', () => {
            editDrawer.classList.toggle('translate-x-full');
            editDrawer.classList.toggle('translate-x-0');
        });

        editDrawerClose.addEventListener('click', () => {
            editDrawer.classList.toggle('translate-x-full');
            editDrawer.classList.toggle('translate-x-0');
        });
    });
    
    const message = '<%= request.getAttribute("message") != null ? request.getAttribute("message").toString() : "" %>';

    console.log("message", message)
    // Function to show the toast
    function showToast(message) {
        const toast = document.getElementById('toast');

        toast.innerHTML = message;
        toast.classList.remove('hidden', 'opacity-0');
        toast.classList.add('opacity-100');

        // Hide after 5 seconds
        setTimeout(() => {
            toast.classList.add('opacity-0');
            toast.classList.remove('opacity-100');

            // Remove the element after the transition (0.5s) is done
            setTimeout(() => {
                toast.classList.add('hidden');
            }, 500);
        }, 5000);
    }

    // Show the toast if there's a message
    if (message) {
        showToast(message);
    }
    
    
    document.addEventListener('DOMContentLoaded', function () {
        const books = [
            { title: 'Book One', image: './assets/img/sample1.jpg', author: 'Author One', category: 'Category One', available: true, id: '1', acquireBy: 'buy' },
            { title: 'Book Two', image: './assets/img/sample2.jpg', author: 'Author Two', category: 'Category Two', available: false, id: '2', acquireBy: 'rent' },
            { title: 'Book Three', image: './assets/img/sample4.jpg', author: 'Author Three', category: 'Category Three', available: true, id: '3', acquireBy: 'buy' },
            { title: 'Book One', image: './assets/img/sample1.jpg', author: 'Author One', category: 'Category One', available: true, id: '1', acquireBy: 'buy' },
            { title: 'Book Two', image: './assets/img/sample2.jpg', author: 'Author Two', category: 'Category Two', available: false, id: '2', acquireBy: 'rent' },
            { title: 'Book Three', image: './assets/img/sample4.jpg', author: 'Author Three', category: 'Category Three', available: true, id: '3', acquireBy: 'buy' },
            { title: 'Book One', image: './assets/img/sample1.jpg', author: 'Author One', category: 'Category One', available: true, id: '1', acquireBy: 'buy' },
            { title: 'Book Two', image: './assets/img/sample2.jpg', author: 'Author Two', category: 'Category Two', available: false, id: '2', acquireBy: 'rent' },
            { title: 'Book Three', image: './assets/img/sample4.jpg', author: 'Author Three', category: 'Category Three', available: true, id: '3', acquireBy: 'buy' },
            { title: 'Book One', image: './assets/img/sample1.jpg', author: 'Author One', category: 'Category One', available: true, id: '1', acquireBy: 'buy' },
            { title: 'Book Two', image: './assets/img/sample2.jpg', author: 'Author Two', category: 'Category Two', available: false, id: '2', acquireBy: 'rent' },
            { title: 'AAA', image: './assets/img/sample4.jpg', author: 'Author Three', category: 'Category Three', available: true, id: '3', acquireBy: 'buy' }
        ];

        let sortedBooks = [...books];
        let sortOrder = {};

        const booksTableBody = document.getElementById('books-table-body');
        const loanTableBody = document.getElementById('loan-table-body');

        const bookSearchInput = document.getElementById('books-search-input');
        const loanSearchInput = document.getElementById('loan-search-input');

        function renderBooksTable(data) {
            booksTableBody.innerHTML = '';
            data.forEach(book => {
                const row = document.createElement('tr');
                row.classList.add('border-b', 'hover:bg-gray-100');

                row.innerHTML = `
                    <td class="py-2 px-4">${book.title}</td>
                    <td class="py-2 px-4"><img src="${book.image}" alt="${book.title}" class="w-16 h-16 object-cover"></td>
                    <td class="py-2 px-4">${book.author}</td>
                    <td class="py-2 px-4">${book.category}</td>
                    <td class="py-2 px-4">${book.available ? 'Yes' : 'No'}</td>
                    <td class="py-2 px-4">${book.id}</td>
                    <td class="py-2 px-4">${book.acquireBy}</td>
                    <td class="py-2 px-4">
                        <button class="bg-blue-500 text-white px-3 py-1 hover:bg-blue-700" onclick="editBook('${book.id}')">Edit</button>
                    </td>
                `;

                booksTableBody.appendChild(row);
            });
        }

        function renderLoanTable(data) {
            loanTableBody.innerHTML = '';
            data.forEach(book => {
                const row = document.createElement('tr');
                row.classList.add('border-b', 'hover:bg-gray-100');

                row.innerHTML = `
                    <td class="py-2 px-4">${book.title}</td>
                    <td class="py-2 px-4">${book.author}</td>
                    <td class="py-2 px-4">${book.category}</td>
                    <td class="py-2 px-4">${book.available ? 'Yes' : 'No'}</td>
                    <td class="py-2 px-4">
                        <button class="bg-blue-500 text-white px-3 py-1 hover:bg-blue-700" onclick="editBook('${book.id}')">Edit</button>
                    </td>
                `;

                loanTableBody.appendChild(row);
            });
        }

        function sortBooksTable(column) {
            const sortDirection = sortOrder[column] === 'asc' ? 'desc' : 'asc';
            sortOrder[column] = sortDirection;

            sortedBooks.sort((a, b) => {
                if (a[column] < b[column]) return sortDirection === 'asc' ? -1 : 1;
                if (a[column] > b[column]) return sortDirection === 'asc' ? 1 : -1;
                return 0;
            });

            renderBooksTable(sortedBooks);
        }

        function sortLoanTable(column) {
            const sortDirection = sortOrder[column] === 'asc' ? 'desc' : 'asc';
            sortOrder[column] = sortDirection;

            sortedBooks.sort((a, b) => {
                if (a[column] < b[column]) return sortDirection === 'asc' ? -1 : 1;
                if (a[column] > b[column]) return sortDirection === 'asc' ? 1 : -1;
                return 0;
            });

            renderLoanTable(sortedBooks);
        }

        function filterBooksTable(query) {
            const filteredBooks = books.filter(book => {
                return (
                    book.title.toLowerCase().includes(query) ||
                    book.author.toLowerCase().includes(query) ||
                    book.category.toLowerCase().includes(query)
                );
            });

            renderBooksTable(filteredBooks);
        }

        function filterLoanTable(query) {
            const filteredLoans = books.filter(book => {
                return (
                    book.title.toLowerCase().includes(query) ||
                    book.author.toLowerCase().includes(query) ||
                    book.category.toLowerCase().includes(query)
                );
            });

            renderLoanTable(filteredLoans);
        }

        bookSearchInput.addEventListener('input', function () {
            filterBooksTable(this.value.toLowerCase());
        });

        loanSearchInput.addEventListener('input', function () {
            filterLoanTable(this.value.toLowerCase());
        });

        document.querySelectorAll('th[data-sort]').forEach(header => {
            header.addEventListener('click', function () {
                const column = this.getAttribute('data-sort');
                if (this.parentElement.parentElement.querySelector('thead').parentElement.classList.contains('w-2/3')) {
                    sortBooksTable(column);
                } else {
                    sortLoanTable(column);
                }
            });
        });

        window.editBook = function (id) {
            const bookToEdit = books.find(book => book.id === id);
            alert(`Editing book: ${bookToEdit.title}`);
            // Implement actual editing logic here
        };

        renderBooksTable(books);
        renderLoanTable(books);
    });
   </script>
</body>
</html>
