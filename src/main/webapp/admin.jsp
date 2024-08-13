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
  </head>
  <body>
    <!-- Navbar -->
    <nav
      id="nav"
      class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow-0 px-20 shadow-slate-300 bg-[--bg]"
    ></nav>
       <div id="drawer" class="fixed top-0 right-0 w-80 h-full bg-white shadow-lg transform translate-x-full transition-transform duration-300 z-50">

        <div class="p-6">
            <div class="w-full flex justify-between">
            	<h2 class="text-2xl text-[--secondary] font-bold mb-4">Add New Book</h2>
            	<button id="drawer-close">Close</span>
            </div>
            <form action="/insert-book" method="POST">

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
                    	<input type="number" id="copy" name="copy" class="w-full p-2 border rounded" value="1">
                	</div>
                </div>                

                <!-- Book Shelf -->
                <div class="mb-4">
                	<label for="bookshelf" class="block text-gray-700">Book Shelf</label>
                    <input type="text" id="bookshelf" name="bookshelf" class="w-full p-2 border rounded">
                    	
                </div>

                <!-- Acquire By -->
                <div class="mb-4">
                    <label for="acquireby" class="block text-gray-700">Acquire By</label>
                    <select id="acquireby" name="acquireby" class="w-full p-2 border rounded">
                        <option value="Buy">Buy</option>
                        <option value="Donate">Donate</option>
                    </select>
                </div>

                <!-- Submit Button -->
                <button type="submit" class="w-full bg-[--secondary] hover:shadow-md text-white p-2 rounded">Add Book</button>

            </form>
        </div>
    </div>
       
	<div class="p-6 px-20 space-y-6">
       <!-- Top Boxes -->
	  <div class="w-full flex justify-around space-x-6">
	  	<div class="flex w-full justify-between space-x-6">
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
          class="flex items-center max-w-72 space-x-7 justify-between bg-white p-6 shadow-md "
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
        <!-- Left Content -->
        <div class="w-2/3 space-y-4">
          <div class="h-24 bg-red-300"></div>
          <div class="h-24 bg-red-300"></div>
          <div class="h-24 bg-red-300"></div>
        </div>

        <!-- Right Content -->
        <div class="w-1/3 space-y-4">
          <div class="h-20 bg-red-300"></div>
          <div class="h-20 bg-red-300"></div>
          <div class="h-20 bg-red-300"></div>
          <div class="h-20 bg-red-300"></div>
        </div>
      </div>
    </div>

    <!-- JavaScript for Drawer Toggle -->
    <script>
        const drawerButton = document.getElementById('drawer-button');
        const drawerClose = document.getElementById('drawer-close');

        const drawer = document.getElementById('drawer');

        drawerButton.addEventListener('click', () => {
            drawer.classList.toggle('translate-x-full');
            drawer.classList.toggle('translate-x-0');
        });
        
        drawerClose.addEventListener('click', () => {
            drawer.classList.toggle('translate-x-full');
            drawer.classList.toggle('translate-x-0');
        });
    </script>
    <script src="./scripts/innerHtmlInserter.js"></script>
  </body>
</html>
