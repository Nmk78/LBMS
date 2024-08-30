<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Profile | LBMS</title>
<script src="https://cdn.tailwindcss.com"></script>
<link rel="stylesheet" href="global.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/logout.js"></script>

<script>
    // Function to save data to LocalStorage
    function saveToLocalStorage(key, value) {
        localStorage.setItem(key, value);
    }

    // Function to get data from LocalStorage
    function getFromLocalStorage(key) {
        return localStorage.getItem(key);
    }

    // Function to populate profile data
    function populateProfileData() {
        let name = getFromLocalStorage("name");
        let email = getFromLocalStorage("email");
        let phone = getFromLocalStorage("phone");
        let idOrDept = getFromLocalStorage("idOrDept");

        if (/^\d+$/.test(idOrDept)) {
            // If idOrDept is all digits
            idOrDept = "ID : " + idOrDept;
        } else {
            // If idOrDept contains alphabetic characters
            idOrDept = "Faculty : " + idOrDept;
        }


        document.getElementById("name").innerText = name || "No Name";
        document.getElementById("email").innerText = email || "No Email";
        document.getElementById("phone").innerText = phone || "No Phone";
        document.getElementById("idOrDept").innerText = idOrDept || "No ID or Dept";
    }

    // Function to handle page load and save data
    function handlePageLoad() {
        var name = "<%= session.getAttribute("name") != null ? session.getAttribute("name").toString() : "" %>";
        var email = "<%= session.getAttribute("email") != null ? session.getAttribute("email").toString() : "" %>";
        var phone = "<%= session.getAttribute("phone") != null ? session.getAttribute("phone").toString() : "" %>";
        var idOrDept = "<%= session.getAttribute("idOrDept") != null ? session.getAttribute("idOrDept").toString() : "" %>";

        if (name && email && phone && idOrDept) {
            saveToLocalStorage('name', name);
            saveToLocalStorage('email', email);
            saveToLocalStorage('phone', phone);
            saveToLocalStorage('idOrDept', idOrDept);
            saveToLocalStorage('isLoggedIn', true);
        } else {
            console.log("Can't set user data, doens't have user data.");
        }

        // Populate profile data after saving to LocalStorage
        populateProfileData();
    }

    // Ensure the function runs when the page loads
    window.onload = handlePageLoad;
</script>
</head>
<body>
<nav
    id="nav"
    class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow-0 px-20 shadow-slate-300 bg-[--bg]"
></nav>
<!-- Profile Container -->
<div class="max-w-6xl mx-auto mt-10 p-6 bg-white shadow-md">
    <!-- Profile Header -->
    <div class="flex h-full items-center border-b border-gray-200 pb-4 space-x-5">
        <img
            src="./assets/img/avatar.svg"
            alt="Profile Picture"
            class="h-44 object-contain mr-6"
        />
        <div class="border-r border-gray-200 pr-4">
            <div id="nameAndAchievement" class="flex gap-2">
                <h1 id="name" class="text-3xl font-semibold text-gray-800"></h1>
            </div>
            <p id="email" class="text-gray-500"></p>
            <p id="phone" class="text-gray-500"></p>
            <p id="idOrDept" class="text-gray-500"></p>
        </div>
        <div class="space-x-10 pl-4 flex justify-around">
            <div class="text-center space-y-3">
                <p class="text-[--secondary] font-semibold text-xl">Read</p>
                <p class="text-[--secondary] font-medium text-md">34</p>
            </div>
            <div class="text-center space-y-3">
                <p class="text-[--secondary] font-semibold text-xl">Saved</p>
                <p id="savedBooks" class="text-[--secondary] font-medium text-md"></p>
            </div>
            <div class="text-center space-y-3">
                <p class="text-[--secondary] font-semibold text-xl">Status</p>
                <p class="text-[--danger] font-medium text-md">Over Date</p>
            </div>
        </div>
        <div class="h-full flex items-start">
            <button id="logoutButton" onclick="logout('user')" class="bg-[--secondary] py-2 px-3 text-white">Logout</button>
        </div>
    </div>

    <!-- Loan Details -->
    <div class="mt-6 pb-6 border-b border-gray-200">
        <h2 class="text-xl font-semibold text-[--secondary] mb-4">Reserved</h2>
        <section
            id="reservedContainer"
            class="w-full flex flex-wrap mx-auto h-full mb-5 gap-5"
        ></section>
    </div>
    
    <!-- Borrowed Books -->
    <div class="mt-6 pb-6 border-b border-gray-200">
        <h2 class="text-xl font-semibold text-[--secondary] mb-4">Borrowed</h2>
        <section
            id="borrowedContainer"
            class="w-full flex flex-wrap mx-auto h-full mb-5 gap-5"
        ></section>
    </div>
    
    <!-- Saved Books -->
    <div class="mt-6">
        <h2 class="text-xl font-semibold text-[--secondary] mb-4">Saved</h2>
        <section
            id="cardContainer"
            class="w-full flex flex-wrap mx-auto h-full mb-5 gap-5"
        ></section>
    </div>
</div>


<script src="./scripts/innerHtmlInserter.js"></script>
<script src="./scripts/book/card.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function () {
    const savedBookIds = JSON.parse(localStorage.getItem('savedBooks')) || [];
    if (savedBookIds.length > 0) {
    	console.log("savedBookIds",savedBookIds)
        fetchSavedBooks(savedBookIds);
    }

    function fetchSavedBooks(bookIds) {
        console.log('Sending bookIds:', bookIds); // Debugging line

        const params = new URLSearchParams();
        bookIds.forEach(id => params.append('bookIds[]', id));

        fetch('/LBMS/getSavedBooks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params
        })
        .then(response => response.json())
        .then(data => {
            displayBooks(data);
            console.log("Saved data:", data); // Debugging line
        })
        .catch(error => console.error('Error:', error));
    }


    function displayBooks(books) {
        const cardContainer = document.getElementById('cardContainer');
        cardContainer.innerHTML = '';

        books.forEach(function(book) {
            const bookCard = document.createElement('a');
            bookCard.href = 'book?id=' + book.bid;  // Using standard string concatenation
            bookCard.id = book.bid;
            bookCard.className = 'block w-[300px] bg-white shadow-md p-4 hover:shadow-lg transition-shadow duration-300';

            // Build the inner HTML of the book card using standard string concatenation
            let cardHTML = '';

            if (book.getImage) {
                cardHTML += '<img class="w-full object-cover mb-4" src="' +book.getImage + '" alt="' + book.title + '"/>';
            }
            
            

            cardHTML += '<h2 class="text-2xl font-semibold mb-2 line-clamp-3">' + book.title + '</h2>';
            cardHTML += '<p class="text-gray-700 mb-4 line-clamp-3">' + book.authorName + '</p>';
            cardHTML += '<p class="text-sm text-gray-500">Rating: ' + book.averageRating.toFixed(1) + '</p>';
            cardHTML += '<button class="mt-4 bg-red-500 text-white px-4 py-2 hover:bg-red-700" onclick="removeBook(' + book.bid + ')">Remove</button>';

            bookCard.innerHTML = cardHTML;

            cardContainer.appendChild(bookCard);
        });
    }
    
 // Fetch borrowed books from the server
    function fetchBorrowedBooks(memberId) {
        const params = new URLSearchParams();
        params.append('memberId', memberId); // Append memberId to the request parameters

        fetch('/LBMS/getBorrowedBooks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString() // Convert params to a query string
        })
        .then(response => response.json())
        .then(data => {
        	displayBorrowedBooks(data);
            console.log("Fetched borrowed books:", data); // Debugging line
        })
        .catch(error => console.error('Error:', error));
    }

// Display borrowed books
function displayBorrowedBooks(books) {
    const cardContainer = document.getElementById('borrowedContainer');
    cardContainer.innerHTML = '';

    console.log(books)
    books.forEach(function(book) {
        const today = new Date();
        const dueDate = new Date(book.dueDate);

        const isOverdue = dueDate < today;

        console.log(isOverdue)
        const statusClass = isOverdue ? 'shadow-sm shadow-red-500' : '';
        
        const bookCard = document.createElement('a');
        bookCard.href = '/LBMS/book?id=' + book.bid;  // Use the provided URL format
        bookCard.target = '_self'; // Ensure the link opens in the same tab
        bookCard.title = book.title;
        bookCard.className = 'group relative min-w-[250px]' + statusClass + ' transition-all duration-300 w-[300px] h-[460px] bg-white border-2 border-gray-300 rounded-lg shadow-md overflow-hidden';

        // Build the inner HTML of the book card
        let cardHTML = '';

        // Check if the book has an image and append it to the card
        if (book.image) {
            cardHTML += '<div class="image w-full h-full absolute border-2 border-dashed top-0 left-0 z-0 overflow-hidden bg-cover transition-transform">';
            cardHTML += '<img class="w-full h-full object-cover transition-transform group-hover:scale-110" src="' + book.image + '" alt="Book">';
            cardHTML += '</div>';
        }

        // Add book details
        cardHTML += '<div class="details p-3 flex flex-col justify-between absolute bottom-0 left-0 right-0 z-10 bg-white transition-transform transform translate-y-full group-hover:translate-y-0">';
        cardHTML += '<p class="title font-semibold text-gray-800 text-xl line-clamp-2">' + book.title + '</p>';
        cardHTML += '<div class="AuthorAndAvailability w-full flex justify-between items-center">';
        cardHTML += '<p class="author font-medium text-lg text-gray-600">' + book.authorName + '</p>';

        // Calculate the due date or availability
        const statusText = book.dueDate ? `Due: ${book.dueDate} days` : 'N/A';


        cardHTML += '<p class="status px-2 py-1 border-2 border-dashed">';
        cardHTML += statusText;
        cardHTML += '</p>';
        cardHTML += '</div>';
        cardHTML += '</div>';

        bookCard.innerHTML = cardHTML;
        cardContainer.appendChild(bookCard);
    });
}


    
    window.removeBook = function (bookId) {
        const savedBooks = JSON.parse(localStorage.getItem('savedBooks')) || [];
        const updatedBooks = savedBooks.filter(id => id !== bookId);
        localStorage.setItem('savedBooks', JSON.stringify(updatedBooks));
        fetchSavedBooks(updatedBooks);
    }
    
    function updateSaveBookCount() {
    	console.log("run");
        const savedBooks = JSON.parse(localStorage.getItem('savedBooks')) || [];
        const bookCount = savedBooks.length;
        document.getElementById('savedBooks').textContent = bookCount;
    }
    updateSaveBookCount()
    fetchBorrowedBooks(localStorage.getItem('idOrDept'))

});

</script>
</body>
</html>
