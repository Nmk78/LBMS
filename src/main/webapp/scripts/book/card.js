// Select multiple elements by querySelectorAll
const popularElements = document.querySelectorAll("#popular");
const peopleChoicesElements = document.querySelectorAll("#peopleChoices");
const exploreElements = document.querySelectorAll("#explore");

const loadingAnimation = document.getElementById("loadingAnimation");

// Function to show or hide the loading animation
const setLoadingVisibility = (isVisible) => {
  if (isVisible) {
    loadingAnimation.classList.remove("hidden");
  } else {
    loadingAnimation.classList.add("hidden");
  }
};


// Function to generate HTML for a book card
const generateCard = (title, image, author, availability, id) => {
  // Retrieve the idOrDept from localStorage
  const idOrDept = localStorage.getItem("idOrDept");

  // Conditionally construct the URL based on the presence of idOrDept
  const bookUrl = idOrDept
    ? `/LBMS/book?id=${id}&user=${encodeURIComponent(idOrDept)}`
    : `/LBMS/book?id=${id}`;

  return `
    <a href="${bookUrl}" target="_self" title="${title}" class="group relative min-w-[250px] transition-all duration-300 w-[300px] h-[460px] bg-white border-2 border-gray-300 rounded-lg shadow-md overflow-hidden">
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

// Function to populate a section with books
const populateSection = (elements, books) => {
  elements.forEach(element => {
    element.innerHTML = ''; // Clear existing content
    books.forEach(book => {
      const cardHTML = generateCard(book.name, book.image, book.author, book.availability, book.id);
      element.innerHTML += cardHTML;
    });
  });
};

// Function to fetch and populate books for a specific section
const fetchBooksForSection = (url, elements) => {
  setLoadingVisibility(true); // Show loading animation

  fetch(url)
    .then(response => response.json())
    .then(data => {
      console.log(`Books from ${url}:`, data);
      populateSection(elements, data);
    })
    .catch(error => console.error(`Error fetching books from ${url}:`, error))
    .finally(() => setLoadingVisibility(false)); // Hide loading animation after fetch is complete
};

// Fetch and populate books for each section
fetchBooksForSection('/LBMS/books', popularElements);
fetchBooksForSection('/LBMS/mostRatedBooks', peopleChoicesElements);
fetchBooksForSection('/LBMS/popularBooks', exploreElements);