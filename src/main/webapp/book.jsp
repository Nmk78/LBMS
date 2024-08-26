<%@page import="com.google.gson.Gson"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.google.gson.JsonObject, com.google.gson.JsonArray, com.google.gson.JsonElement, com.google.gson.JsonParser" %> 

<%
    String bookId = ""; // Assuming the book ID is passed as a request parameter
    String title = "";
    String author = "";
    String category = "";
    String availability = "";
    String image = "";
    double rating = 0.0;

    String bookJson = (String) request.getAttribute("book");
    JsonObject book = new Gson().fromJson(bookJson, JsonObject.class);

    if (book != null) {
        // Display book details and reviews
		bookId = book.get("id").getAsString();
        title = book.get("title").getAsString();
        author = book.get("author").getAsString();
        category = book.get("category").getAsString();
        availability = book.get("availability").getAsString();
        image = book.get("image").getAsString();
        rating = book.get("averageRating").getAsDouble();
    }
%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Details</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/global.css">
    
    <style>
        /* The provided CSS styles */
        .ui-bookmark {
            --icon-size: 24px;
            --icon-secondary-color: rgb(77, 77, 77);
            --icon-hover-color: rgb(97, 97, 97);
            --icon-primary-color: gold;
            --icon-circle-border: 1px solid var(--icon-primary-color);
            --icon-circle-size: 35px;
            --icon-anmt-duration: 0.3s;
        }

        .ui-bookmark input {
            -webkit-appearance: none;
            -moz-appearance: none;
            appearance: none;
            display: none;
        }

        .ui-bookmark .bookmark {
            width: var(--icon-size);
            height: auto;
            fill: var(--icon-secondary-color);
            cursor: pointer;
            transition: 0.2s;
            display: flex;
            justify-content: center;
            align-items: center;
            position: relative;
            transform-origin: top;
        }

        .bookmark::after {
            content: "";
            position: absolute;
            width: 10px;
            height: 10px;
            box-shadow: 0 30px 0 -4px var(--icon-primary-color),
                30px 0 0 -4px var(--icon-primary-color),
                0 -30px 0 -4px var(--icon-primary-color),
                -30px 0 0 -4px var(--icon-primary-color),
                -22px 22px 0 -4px var(--icon-primary-color),
                -22px -22px 0 -4px var(--icon-primary-color),
                22px -22px 0 -4px var(--icon-primary-color),
                22px 22px 0 -4px var(--icon-primary-color);
            border-radius: 50%;
            transform: scale(0);
        }

        .bookmark::before {
            content: "";
            position: absolute;
            border-radius: 50%;
            border: var(--icon-circle-border);
            opacity: 0;
        }

        .ui-bookmark:hover .bookmark {
            fill: var(--icon-hover-color);
        }

        .ui-bookmark input:checked + .bookmark::after {
            animation: circles var(--icon-anmt-duration)
                cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards;
            animation-delay: var(--icon-anmt-duration);
        }

        .ui-bookmark input:checked + .bookmark {
            fill: var(--icon-primary-color);
            animation: bookmark var(--icon-anmt-duration) forwards;
            transition-delay: 0.3s;
        }

        .ui-bookmark input:checked + .bookmark::before {
            animation: circle var(--icon-anmt-duration)
                cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards;
            animation-delay: var(--icon-anmt-duration);
        }

        @keyframes bookmark {
            50% {
                transform: scaleY(0.6);
            }

            100% {
                transform: scaleY(1);
            }
        }

        @keyframes circle {
            from {
                width: 0;
                height: 0;
                opacity: 0;
            }

            90% {
                width: var(--icon-circle-size);
                height: var(--icon-circle-size);
                opacity: 1;
            }

            to {
                opacity: 0;
            }
        }

        @keyframes circles {
            from {
                transform: scale(0);
            }

            40% {
                opacity: 1;
            }

            to {
                transform: scale(0.8);
                opacity: 0;
            }
        }
    </style>
</head>
<body class="bg-gray-100 mx-auto text-gray-800">
    <nav
      id="nav"
      class="flex sticky z-50 top-0 w-full h-18 py-1 justify-between items-center shadow-0 px-20 shadow-slate-300 bg-[--bg]"
    ></nav>
    
    				<div id="successModal" class="fixed z-50 inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center hidden">
					    <div class="bg-white shadow-lg p-6 max-w-sm w-full">
							<div class="w-full flex space-x-3">
								<div class="inline-flex items-center justify-center flex-shrink-0 w-8 h-8 text-green-500 bg-green-200 ">
						        <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
						            <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5Zm3.707 8.207-4 4a1 1 0 0 1-1.414 0l-2-2a1 1 0 0 1 1.414-1.414L9 10.586l3.293-3.293a1 1 0 0 1 1.414 1.414Z"/>
						        </svg>
						        <span class="sr-only">Mark Icon</span>
						    </div>
						        <h2 class="text-xl font-bold mb-4">Reservation Successful</h2>
							</div>
					        <p class="text-gray-700 ml-11 mb-4">Your reservation has been successfully queued. Please remember to take the book within 3 days.</p>
							<div class="w-full flex items-start space-x-3">
								<div class="inline-flex items-center justify-center flex-shrink-0 w-8 h-8 text-orange-500 bg-orange-100 ">
							        <svg class="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
							            <path d="M10 .5a9.5 9.5 0 1 0 9.5 9.5A9.51 9.51 0 0 0 10 .5ZM10 15a1 1 0 1 1 0-2 1 1 0 0 1 0 2Zm1-4a1 1 0 0 1-2 0V6a1 1 0 0 1 2 0v5Z"/>
							        </svg>
							        <span class="sr-only">Warning icon</span>
							    </div>
					        	<p class="text-red-500 mb-4">If you fail to take the book within this period, you may be added to our blacklist.</p>
							</div>
					        <div class="w-full flex justify-start">
					        	<button id="closeModal" class="bg-blue-500 text-white font-semibold py-2 px-4 ml-auto shadow-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300 transition ease-in-out duration-150">
					            Close
					        </button>
					        </div>
					    </div>
					</div>
    <div class="container max-w-5xl mx-auto px-4 pb-8">
        <!-- Book Details Section -->
        <section class="bg-white h-2/5 mx-auto p-6 shadow-md py-8 border-b border-[--secondary]">
            <div class="flex flex-col md:flex-row ">
                <div class="md:w-1/4 md:h-2/3 mb-4 md:mb-0">
                    <!-- Cover Image -->
                    <% if (!image.isEmpty()) { %>
                        <img id="bookImage" class="w-full h-auto object-cover rounded-lg" 
                            src="<%= image %>" 
                            alt="Book Cover">
                    <% } else { %>
                        <img id="bookImage" class="w-full h-auto object-cover rounded-lg" 
                            src="./assets/img/defaultCover.png" 
                            alt="No Cover Available">
                    <% } %>
                </div>
                <div class="w-full mx-auto md:w-2/3 md:pl-6">
                    <header class="mb-8 z-10 space-y-4 w-full relative">
						<div class="saveBtn absolute top-5 right-0">
						
						    <label class="ui-bookmark" for="bookmark-checkbox">
						        <input type="checkbox" id="bookmark-checkbox">
						        <div class="bookmark">
						            <svg viewBox="0 0 32 32">
						                <g>
						                    <path d="M27 4v27a1 1 0 0 1-1.625.781L16 24.281l-9.375 7.5A1 1 0 0 1 5 31V4a4 4 0 0 1 4-4h14a4 4 0 0 1 4 4z"></path>
						                </g>
						            </svg>
						        </div>
						    </label>

							  
							  	
						</div>
                        <h1 class="text-3xl z-10 font-bold mb-2" id="bookTitle"><%= title %></h1>
                        <p id="bookAuthor" class="font-xl z-10 text-lg"><%= category %></p>

                        <div class="flex w-full justify-between">
                        	<p id="bookAuthor" class="font-xl z-10 text-lg font-semibold">Author: <%= author %></p>
							<div class="flex space-x-1 z-50">
									<svg id="star-5" class="w-8 h-8 ms-3 text-gray-300 star" fill="#FFEB3B" viewBox="0 0 22 20">
					            		<path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
					        		</svg>
									<h1 class="text-3xl z-10 font-bold mb-2" id="bookTitle"><%= rating %>/5</h1>
							</div>
                        </div>
                        
                    </header>
					<div class="w-full flex justify-between">
						<p id="bookAvailability" class="text-lg font-semibold">Availability: 
		                    <span id="availabilityStatus" class="<%= "Available".equals(availability) ? "text-green-500" : "text-orange-500" %>">
							    <%= availability %>
							</span>
						</p>
							<button title="You will need to get and borrow this book in library" class="bg-blue-500 text-white font-semibold py-2 px-4 shadow-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-300 transition ease-in-out duration-150">
							    Reserve This Book
							</button>
					</div>
                </div>
            </div>
        </section>

        <!-- Reviews Section -->
<section class="bg-white mx-auto p-6 z-10 relative">
    <h2 class="text-3xl font-semibold mb-4">Reviews</h2>
    <div id="reviewsList" class="mb-4 divide-y divide-dashed hover:divide-solid divide-gray-400">
<%

    // For reviews
    JsonArray reviews = book.getAsJsonArray("reviews");
    if (reviews != null && reviews.size() > 0) {
        for (JsonElement reviewElement : reviews) {
            JsonObject review = reviewElement.getAsJsonObject();
            String reviewerName = review.get("reviewerName").getAsString();
            String reviewContent = review.get("reviewContent").getAsString();
            String createdAt = review.get("createdAt").getAsString();
%>
            <div class="review py-1">
				<div class="w-full flex justify-between mb-2">
					<p><strong><%= reviewerName %></strong></p>
					<p><em><%= createdAt %></em></p>
				</div>
                <p class="pl-10"><%= reviewContent %></p>
            </div>
<%
        }
    } else {
%>
    <p>No reviews yet.</p>
<%
    }
%>

    </div>
</section>

        <section class="bg-white border-t border-[--secondary] mx-auto p-6 mb-8 relative">
            <div class="flex justify-center items-center w-full relative space-x-10 divider-x">
<form id="reviewForm" action="/LBMS/BookServlet" method="post" class="w-1/2 space-y-4">
    <h3 class="text-2xl font-semibold mb-4">Submit a Review</h3>
    <div class="mb-4">
        <input type="hidden" id="MemberId" name="MemberId" value="" />
            <input type="hidden" name="bookId" value="<%= bookId %>" />
            
        <input type="hidden" name="action" value="addReview" />
        <label for="reviewContent" class="block text-lg font-medium mb-2">Your Review</label>
        <textarea id="reviewContent" name="reviewContent" rows="4" class="w-full p-2 border border-gray-300 rounded" required></textarea>
    </div>

    <button type="submit" class="w-full bg-[--secondary] hover:bg-blue-600 text-white p-2 rounded">Submit Review</button>
</form>

                
                <!-- Rating Form -->
                <form action="/LBMS/BookServlet" method="post" id="ratingForm" class="w-1/2 space-y-7 h-full flex flex-col justify-center items-center">
						<input type="hidden" id="MemberId" name="memberId" />
						<input type="hidden" name="action" value="addOrUpdateRating" />
						<input type="hidden" name="bookId" value="<%= bookId %>" />
                    <div class="w-2/3 flex flex-col items-center">
                        <label for="rating" class="text-xl font-bold text-center mb-7">Rate this book</label>
                        <div id="star-rating" class="flex items-center">
					    <!-- Radio Buttons and Stars -->
					    <input type="radio" id="star1" name="rating" required value="1" class="hidden" />
					    <label for="star1">
					        <svg id="star-1" class="w-8 h-8 ms-3 text-gray-300 star" fill="currentColor" viewBox="0 0 22 20">
					            <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
					        </svg>
					    </label>
					    <input type="radio" id="star2" name="rating" value="2" class="hidden" />
					    <label for="star2">
					        <svg id="star-2" class="w-8 h-8 ms-3 text-gray-300 star" fill="currentColor" viewBox="0 0 22 20">
					            <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
					        </svg>
					    </label>
					    <input type="radio" id="star3" name="rating" value="3" class="hidden" />
					    <label for="star3">
					        <svg id="star-3" class="w-8 h-8 ms-3 text-gray-300 star" fill="currentColor" viewBox="0 0 22 20">
					            <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
					        </svg>
					    </label>
					    <input type="radio" id="star4" name="rating" value="4" class="hidden" />
					    <label for="star4">
					        <svg id="star-4" class="w-8 h-8 ms-3 text-gray-300 star" fill="currentColor" viewBox="0 0 22 20">
					            <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
					        </svg>
					    </label>
					    <input type="radio" id="star5" name="rating" value="5" class="hidden" />
					    <label for="star5">
					        <svg id="star-5" class="w-8 h-8 ms-3 text-gray-300 star" fill="currentColor" viewBox="0 0 22 20">
					            <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
					        </svg>
					    </label>
					</div>

                    </div>
                    <button type="submit" class=" bg-yellow-400 hover:bg-yellow-500 text-white p-2 rounded">Submit Rating</button>
                </form>
            </div>
        </section>
    </div>
    
        <script src="./scripts/innerHtmlInserter.js"></script>

    <script>
		// Save and unsave 
        const checkbox = document.getElementById('bookmark-checkbox');
        const bookId = <%=bookId%>; 


        // Check if the book is already saved in localStorage
        if (localStorage.getItem('savedBooks')?.includes(bookId)) {
        	checkbox.checked = true;
        }

        // Toggle save and unsave in localStorage on click
        checkbox.addEventListener('change', () => {
            let savedBooks = JSON.parse(localStorage.getItem('savedBooks')) || [];

            if (checkbox.checked) {
                if (!savedBooks.includes(bookId)) {
                    savedBooks.push(bookId);
                }
            } else {
                savedBooks = savedBooks.filter(id => id !== bookId);
            }

            localStorage.setItem('savedBooks', JSON.stringify(savedBooks));
        });

    
    
        // Example JavaScript for handling the star rating and review submission
        document.addEventListener('DOMContentLoaded', () => {
			///Set id values
			const memberIdInputs = document.querySelectorAll("#MemberId");
			const idOrDept = localStorage.getItem("idOrDept");
			
			memberIdInputs.forEach(input => {
			  input.value = idOrDept;
			});
        	  
           
            /// Rating

            const starInputs = document.querySelectorAll('input[name="rating"]');
            const starSvgs = document.querySelectorAll('.star');

            // Pre-existing rating value, for example, fetched from the backend
            const preExistingRating = parseInt(document.querySelector('input[name="rating"]:checked')?.value) || 0;

            // Function to update stars based on a rating value
            function updateStars(ratingValue) {
            	console.log("ran")
                starSvgs.forEach((svg, svgIndex) => {
                    if (svgIndex <= ratingValue) {
                        svg.classList.remove('text-gray-300');
                        svg.classList.add('text-yellow-500');
                    } else {
                        svg.classList.remove('text-yellow-500');
                        svg.classList.add('text-gray-300');
                    }
                });
            }

            // Update stars on page load based on pre-existing rating
            if (preExistingRating > 0) {
                updateStars(preExistingRating);
            }

            // Add event listener to each radio input
            starInputs.forEach((input) => {
                input.addEventListener('change', () => {
                    const ratingValue = parseInt(input.value);
                    updateStars(ratingValue);
                });
            });
        });
    </script>
</body>
</html>
