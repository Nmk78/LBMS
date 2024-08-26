// Select multiple elements by querySelectorAll
const popularElements = document.querySelectorAll("#popular");
const peopleChoicesElements = document.querySelectorAll("#peopleChoices");
const exploreElements = document.querySelectorAll("#explore");

// Function to generate HTML for a book card
const generateCard = (title, image, author, availability, id) => {
  return `
    <a href="/LBMS/getBook?id=${id}" target="_self" title="${title}" class="min-w-[250px] w-[--cardWidth] max-w-96 md:w-[300px] h-[--cardHeight] bg-[--bg] border-[3px]">
      <div class="image w-full h-[300px] overflow-hidden bg-cover">
        <img class="w-full h-full object-cover hover:object-contain" src="${image}" alt="Book">
      </div>
      <div class="details p-3 min-h-[150px] h-[160px] flex flex-col justify-between">
        <p class="title font-semibold text-[--text] text-xl line-clamp-2">${title}</p>
        <div class="AuthorAndAvailability w-full flex justify-between items-center">
          <p class="author font-medium text-lg text-[--text]">${author}</p>
		  <p class="status px-2 py-1 border-2 border-dashed ${availability > 0 ? 'border-[--accent] text-[--accent]' : 'border-[--warning] text-[--warning]'}">
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
fetch('/LBMS/getBooks')
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
