<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Details</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="global.css" />
</head>
<body class="bg-gray-100 max-w-7xl mx-auto text-gray-800">
    <nav id="nav" class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow-0 px-20 shadow-slate-300 bg-[--bg]"></nav>
    <div class="container mx-auto px-4 py-8">
        <section class="bg-white p-6 shadow-md pb-8 border-b border-[--secondary]">
            <div class="flex flex-col md:flex-row">
                <div class="md:w-1/3 mb-4 md:mb-0">
                    <img id="bookImage" class="w-full h-auto object-cover rounded-lg" src="<%= book.getImage() %>" alt="Book Cover">
                </div>
                <div class="md:w-2/3 md:pl-6">
                    <header class="mb-8">
                        <h1 class="text-4xl font-bold mb-2" id="bookTitle"><%= book.getTitle() %></h1>
                        <p id="bookAuthor" class="text-lg">by <%= book.getAuthorName() %></p>
                    </header>
                    <p id="bookDescription" class="mb-4 text-gray-700"><%= book.getDescription() %></p>
                    <p id="bookAvailability" class="text-lg font-semibold">Availability: <span id="availabilityStatus"><%= book.getAvailability() %></span></p>
                </div>
            </div>
        </section>
        
        <section class="bg-white p-6 relative">
            <h2 class="text-3xl font-semibold mb-4">Reviews</h2>
            <div id="reviewsList" class="mb-4">
                <% 
                    List<Review> reviews = book.getReviews(); 
                    for (Review review : reviews) { 
                %>
                    <div class="border-b mb-4 pb-4">
                        <p class="font-semibold"><%= review.getName() %></p>
                        <p><%= review.getContent() %></p>
                    </div>
                <% 
                    } 
                %>
            </div>
        </section>

        <section class="bg-white p-6 mb-8 relative">
            <div class="flex justify-center items-center w-full relative">
                <form id="reviewForm" class="bg-gray-50 w-1/2 p-6 rounded-lg shadow-md" method="post" action="submitReview.jsp">
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
                <form class="w-1/2 h-full flex justify-center items-center">
                    <div class="w-2/3 flex flex-col items-center space-y-7">
                        <label for="rating" class="text-xl font-bold text-center">Rate this book</label>
                        <div id="star-rating" class="flex items-center mt-2">
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
                </form>
            </div>
        </section>
    </div>
    <script src="scripts.js"></script>
</body>
</html>
