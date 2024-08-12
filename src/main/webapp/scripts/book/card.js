const cardContainer = document.querySelector("#cardContainer");
const peopleChoices = document.querySelector("#peopleChoices");
const explore = document.querySelector("#explore");

const generateCard = (name, image, author, available, id) => {
  return `<a href="${id}" target="_self" id="card" class="min-w-[250px] w-[--cardWidth] max-w-96 md:w-[300px] h-[--cardHeigth] bg-[--bg] border-[3px]">
		<div id="image" class="w-full h-2/3 overflow-hidden bg-cover">
			<img class="" src="./assets/img/sample4.jpg" alt="Book">
		</div>
		<div id="details" class="p-3 space-y-2">
			<p id="title" class=" font-bold text-[--text] text-2xl">${name}</p>
			<div id="AuthorAndAvailablity" class="w-full flex justify-between items-center ">
				<p id="author" class="font-semibold text-lg text-[--text]">${author}</p>
				${available ? `<p id="status" class="px-3 py-1 border-2 mt-0 border-dashed border-[--accent] text-md text-[--accent]">Availabale</p>` : `<p id="status" class="px-3 py-1 border-2 mt-0 border-dashed border-[--danger] text-md text-[--danger]">Borrowed</p>`}
			</div>
			<p id="review" class=" line-clamp-2">Lorem ipsum dolor sit amet consectetur adipisicing elit. Quam recusandae illo sequi provident? Minima explicabo officia cumque animi, velit blanditiis consectetur consequatur molestias cum porro vel exercitationem corporis culpa eius.</p>

		</div>
	  </a>`;
};

const books = [
    {
      name: 'Book One',
      image: './assets/img/sample4.jpg',
      author: 'Author One',
      available: true,
      id: '1'
    },
    {
      name: 'Book Two',
      image: '../assets/img/sample2.jpg',
      author: 'Author Two',
      available: false,
      id: '2'
    },
    {
      name: 'Book Three',
      image: '../assets/img/sample3.jpg',
      author: 'Author Three',
      available: true,
      id: '3'
    },
    {
      name: 'Book Four',
      image: '../assets/img/sample4.jpg',
      author: 'Author Four',
      available: false,
      id: '4'
    },
    {
      name: 'Book One',
      image: './assets/img/sample4.jpg',
      author: 'Author One',
      available: true,
      id: '1'
    },
    {
      name: 'Book Two',
      image: '../assets/img/sample2.jpg',
      author: 'Author Two',
      available: false,
      id: '2'
    },
    {
      name: 'Book Three',
      image: '../assets/img/sample3.jpg',
      author: 'Author Three',
      available: true,
      id: '3'
    },
    {
      name: 'Book Four',
      image: '../assets/img/sample4.jpg',
      author: 'Author Four',
      available: false,
      id: '4'
    },
    {
      name: 'Book One',
      image: './assets/img/sample4.jpg',
      author: 'Author One',
      available: true,
      id: '1'
    },
    {
      name: 'Book Two',
      image: '../assets/img/sample2.jpg',
      author: 'Author Two',
      available: false,
      id: '2'
    },
    {
      name: 'Book Three',
      image: '../assets/img/sample3.jpg',
      author: 'Author Three',
      available: true,
      id: '3'
    },
    {
      name: 'Book Four',
      image: '../assets/img/sample4.jpg',
      author: 'Author Four',
      available: false,
      id: '4'
    }
  ];

  books.forEach(book => {
    const cardHTML = generateCard(book.name, book.image, book.author, book.available, book.id);
    cardContainer.innerHTML += cardHTML;
  });
  
  books.forEach(book => {
    const cardHTML = generateCard(book.name, book.image, book.author, book.available, book.id);
    peopleChoices.innerHTML += cardHTML;
  });

  books.forEach(book => {
    const cardHTML = generateCard(book.name, book.image, book.author, book.available, book.id);
    explore.innerHTML += cardHTML;
  });