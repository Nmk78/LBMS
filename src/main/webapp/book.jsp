<%@page import="Book.Book"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.util.Base64" %> 

<%
    Book book = (Book) request.getAttribute("book");
    if (book == null) {
        out.println("Book details not available.");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Details</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="global.css" />
    

</head>
<body class="bg-gray-100 mx-auto text-gray-800">
    <nav id="nav" class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow-0 px-20 shadow-slate-300 bg-[--bg]"></nav>
    <div class="container mx-auto px-4 pb-8">
        <!-- Book Details Section -->
        <section class="bg-white max-w-7xl h-2/5 mx-auto p-6 shadow-md pb-8 pt-4 border-b border-[--secondary]">
            <div class="flex flex-col md:flex-row ">
                <div class="md:w-1/4 md:h-2/3 mb-4 md:mb-0">
					<!-- //Cover Will come Here  -->
					    <% if (book.getImage() != null) { %>
					        <img id="bookImage" class="w-full h-auto object-cover rounded-lg" 
					            src="<%= "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(book.getImage().getBytes(1L, (int) book.getImage().length())) %>" 
					            alt="Book Cover">
					    <% } else { %>
					        <img id="bookImage" class="w-full h-auto object-cover rounded-lg" 
					            src="./assets/img/defaultCover.png" 
					            alt="No Cover Available">
					    <% } %>
                </div>
                <div class="md:w-2/3 md:pl-6">
                    <header class="mb-8 space-y-4">
                        <h1 class="text-3xl font-bold mb-2" id="bookTitle"><%= book.getTitle() %></h1>
                        <p id="bookAuthor" class="font-xl text-lg">Author : <%= book.getAuthorName() %></p>
                    </header>
                    <p id="bookAvailability" class="text-lg font-semibold">Availability: <span id="availabilityStatus"><%= book.getAvailability() %></span></p>
                </div>
            </div>
        </section>

        <!-- Reviews Section -->
		<section class="bg-white max-w-7xl mx-auto p-6 relative">
		    <h2 class="text-3xl font-semibold mb-4">Reviews</h2>
		    <div id="reviewsList" class="mb-4">
		        <%
		            List<Map<String, String>> reviews = (List<Map<String, String>>) request.getAttribute("reviews");
		            if (reviews != null) {
		                for (Map<String, String> review : reviews) {
		        %>
		                    <div class="border-b mb-4 pb-4">
		                        <p class="font-semibold"><%= review.get("ReviewerName") %></p>
		                        <p><%= review.get("ReviewContent") %></p>
		                        <p class="text-sm text-gray-500"><%= review.get("CreatedAt") %></p>
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
		<section class="bg-white max-w-7xl border-t border-[--secondary] mx-auto p-6 mb-8 relative">
            <div class="flex justify-center items-center w-full relative space-y-4 divider-x">
                <form id="reviewForm" class=" w-1/2 space-y-4">
                    <h3 class="text-2xl font-semibold mb-4">Submit a Review</h3>
                    <div class="mb-4">
                        <label for="reviewerName" class="block text-lg font-medium mb-2">Your Name</label>
                        <input type="text" id="reviewerName" name="reviewerName" class="w-full p-2 border border-gray-300 rounded" required>
                    </div>
                    <div class="mb-4">
                        <label for="reviewContent" class="block text-lg font-medium mb-2">Your Review</label>
                        <textarea id="reviewContent" name="reviewContent" rows="4" class="w-full p-2 border border-gray-300 rounded" required></textarea>
                    </div>
                    <button type="submit" class="w-full bg-[--secondary] hover:bg-blue-600 text-white p-2 rounded">Submit Review</button>
                </form>
                
                <!-- Rating Form -->
                <form action="rateBook" id="ratingForm" class="w-1/2 space-y-7 h-full flex flex-col justify-center items-center">
                    <div class="w-2/3 flex flex-col items-center ">
                        <label for="rating" class="text-xl font-bold text-center mb-7">Rate this book</label>
                        <div id="star-rating" class="flex items-center">
                            <!-- Radio Buttons and Stars -->
                            <input type="radio" id="star1" name="rating" value="1" class="hidden" />
                            <label for="star1">
                                <svg id="star-1" class="w-8 h-8 ms-3 text-gray-300" fill="currentColor" viewBox="0 0 22 20">
                                    <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
                                </svg>
                            </label>
                            <input type="radio" id="star2" name="rating" value="2" class="hidden" />
                            <label for="star2">
                                <svg id="star-2" class="w-8 h-8 ms-3 text-gray-300" fill="currentColor" viewBox="0 0 22 20">
                                    <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
                                </svg>
                            </label>
                            <input type="radio" id="star3" name="rating" value="3" class="hidden" />
                            <label for="star3">
                                <svg id="star-3" class="w-8 h-8 ms-3 text-gray-300" fill="currentColor" viewBox="0 0 22 20">
                                    <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
                                </svg>
                            </label>
                            <input type="radio" id="star4" name="rating" value="4" class="hidden" />
                            <label for="star4">
                                <svg id="star-4" class="w-8 h-8 ms-3 text-gray-300" fill="currentColor" viewBox="0 0 22 20">
                                    <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
                                </svg>
                            </label>
                            <input type="radio" id="star5" name="rating" value="5" class="hidden" />
                            <label for="star5">
                                <svg id="star-5" class="w-8 h-8 ms-3 text-gray-300" fill="currentColor" viewBox="0 0 22 20">
                                    <path d="M20.924 7.625a1.523 1.523 0 0 0-1.238-1.044l-5.051-.734-2.259-4.577a1.534 1.534 0 0 0-2.752 0L7.365 5.847l-5.051.734A1.535 1.535 0 0 0 1.463 9.2l3.656 3.563-.863 5.031a1.532 1.532 0 0 0 2.226 1.616L11 17.033l4.518 2.375a1.534 1.534 0 0 0 2.226-1.617l-.863-5.03L20.537 9.2a1.523 1.523 0 0 0 .387-1.575Z"/>
                                </svg>
                            </label>
                        </div>
                    </div>
					<button type="submit" class=" bg-yellow-300 hover:bg-yellow-400 text-white p-2 rounded">Rate this book</button>
                </form>
            </div>
			<div id="overlay" class="absolute inset-0 bg-gray-100 bg-opacity-50 hidden flex justify-center text-red-500 font-bold text-4xl items-center" title="You must be logged in to give reviews and ratings.">
				Login to give reviews and ratings.
			</div>

        </section>

    </div>
    <script src="./scripts/innerHtmlInserter.js"></script>

	<script>
    document.addEventListener("DOMContentLoaded", function() {
        console.log("DOM fully loaded");

        // Check user login status from localStorage
        const userLogin = localStorage.getItem("userLogin");
        console.log("User login status:", isLoggedIn);
        
        if (!userLogin) {
            console.log("User not logged in");
            // Show overlay if user is not logged in
            document.getElementById("overlay").classList.remove("hidden");
            //document.getElementById("reviewForm").classList.add("pointer-events-none", "opacity-50");
            //document.getElementById("ratingForm").classList.add("pointer-events-none", "opacity-50");
        } else {
            console.log("User logged in");
        }


    });
</script>
	


</body>
</html>
