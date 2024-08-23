const popular = document.querySelector("#popular");
const peopleChoices = document.querySelector("#peopleChoices");
const explore = document.querySelector("#explore");

// Function to generate HTML for a book card
const generateCard = (title, image, author, availability, id) => {
  return `
    <a href="/book?id=${id}" target="_self" title="${title}" id="card" class="min-w-[250px] w-[--cardWidth] max-w-96 md:w-[300px] h-[--cardHeight] bg-[--bg] border-[3px]">
      <div id="image" class="w-full h-[300px] overflow-hidden bg-cover">
        <img class="w-full h-full object-cover" src="${image}" alt="Book">
      </div>
      <div id="details" class="p-3 min-h-[150px] h-[160px] flex flex-col justify-between">
        <p id="title" class="font-semibold text-[--text] text-xl line-clamp-2">${title}</p>
        <div id="AuthorAndAvailability" class="w-full flex justify-between items-center">
          <p id="author" class="font-medium text-lg text-[--text]">${author}</p>
		<p id="status" class="px-2 py-1 border-2 border-dashed ${availability > 0 ? 'border-[--accent] text-[--accent]' : 'border-[--warning] text-[--warning]'}">
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

let books;

// Fetch book data and populate sections
fetch('/LBMS/getBooks')
  .then(response => response.json())
  .then(data => {
    console.log(data);
    books = data;

    // Immediately Invoked Function Expression to handle books
    (function(books) {
      // Clear existing content
      popular.innerHTML = '';
      peopleChoices.innerHTML = '';
      explore.innerHTML = '';

      // Populate each section
      books.slice(0, 20).forEach(book => {
        const cardHTML = generateCard(book.name, book.image, book.author, book.availability, book.id);
        popular.innerHTML += cardHTML;
      });

      books.slice(20, 30).forEach(book => {
        const cardHTML = generateCard(book.name, book.image, book.author, book.availability, book.id);
        peopleChoices.innerHTML += cardHTML;
      });

      books.forEach(book => {
        const cardHTML = generateCard(book.name, book.image, book.author, book.availability, book.id);
        explore.innerHTML += cardHTML;
      });
    })(books);  // Pass the books data to the IIFE
  })
  .catch(error => console.error('Error fetching books:', error));
