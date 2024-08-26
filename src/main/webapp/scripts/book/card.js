// Select multiple elements by querySelectorAll
const popularElements = document.querySelectorAll("#popular");
const peopleChoicesElements = document.querySelectorAll("#peopleChoices");
const exploreElements = document.querySelectorAll("#explore");

// Function to generate HTML for a book card
const generateCard = (title, image, author, availability, id) => {
  return `
    <a href="/LBMS/book?id=${id}" target="_self" title="${title}" class="group relative min-w-[250px] transition-all duration-300 w-[300px] h-[460px] bg-white border-2 border-gray-300 rounded-lg shadow-md overflow-hidden">
      <div class="image w-full h-full absolute top-0 left-0 z-0 overflow-hidden bg-cover transition-transform">
        <img class="w-full h-full object-cover transition-transform group-hover:scale-110" src="${image}" alt="Book">
      </div>
      <div class="details p-3 flex flex-col justify-between absolute bottom-0 left-0 right-0 z-10 bg-white transition-transform transform translate-y-full group-hover:translate-y-0">
        <p class="title font-semibold text-gray-800 text-xl line-clamp-2">${title}</p>
        <div class="AuthorAndAvailability w-full flex justify-between items-center">
          <p class="author font-medium text-lg text-gray-600">${author}</p>
          <p class="status px-2 py-1 border-2 border-dashed ${availability > 0 ? 'border-green-500 text-green-500' : 'border-red-500 text-red-500'}">
            ${availability > 0 
              ? `Available : ${availability}` 
              : availability 
                ? `Due: ${availability} days` 
                : `N/A`}
          </p>
        </div>
      </div>
    </a>
  `;
};

// Fetch book data and populate sections
fetch('/LBMS/books')
  .then(response => response.json())
  .then(data => {
    console.log(data);
    const books = data;

    // Immediately Invoked Function Expression to handle books
    (function(books) {
      // Clear existing content
      popularElements.forEach(element => element.innerHTML = '');
      peopleChoicesElements.forEach(element => element.innerHTML = '');
      exploreElements.forEach(element => element.innerHTML = '');

      // Populate each section
      books.slice(0, 20).forEach(book => {
        const cardHTML = generateCard(book.name, book.image, book.author, book.availability, book.id);
        popularElements.forEach(element => element.innerHTML += cardHTML);
      });

      books.slice(20, 30).forEach(book => {
        const cardHTML = generateCard(book.name, book.image, book.author, book.availability, book.id);
        peopleChoicesElements.forEach(element => element.innerHTML += cardHTML);
      });

      books.forEach(book => {
        const cardHTML = generateCard(book.name, book.image, book.author, book.availability, book.id);
        exploreElements.forEach(element => element.innerHTML += cardHTML);
      });
    })(books);  // Pass the books data to the IIFE
  })
  .catch(error => console.error('Error fetching books:', error));
