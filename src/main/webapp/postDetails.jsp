<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Post Details</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/global.css">
</head>
<body class="bg-gray-100 font-sans">
    <nav id="nav" class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow px-20 bg-gray-200"></nav>
    <div class="container max-w-4xl mx-auto p-6 bg-white shadow-lg mt-6 relative">
        <button onclick="goBack()" class="absolute bg-white top-4 left-4 text-blue-500 hover:text-blue-700 focus:outline-none">
            <svg width="30" height="30" xmlns="http://www.w3.org/2000/svg" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-6 h-6">
                <path d="M2.117 12l7.527 6.235-.644.765-9-7.521 9-7.479.645.764-7.529 6.236h21.884v1h-21.883z"/>
            </svg>
        </button>
        <%
            ResultSet post = (ResultSet) request.getAttribute("post");
            if (post != null) {
                String title = post.getString("Title");
                String content = post.getString("Content");
                String createdAt = post.getString("CreatedAt");
                byte[] imageBytes = post.getBytes("Image");
                String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
        %>
            <img class="w-full h-auto rounded-lg mb-4" src="data:image/jpeg;base64,<%= base64Image %>" alt="Post Image" />
            <h2 class="text-3xl font-bold text-gray-900 mb-2"><%= title %></h2>
            <p class="text-gray-700 text-lg mb-4"><%= createdAt %></p>
            <p class="text-gray-700 text-lg mb-4"><%= content %></p>
        <%
            } else {
        %>
            <div class="text-center text-xl text-gray-500">No post details available.</div>
        <%
            }
        %>
    </div>
        <script src="./scripts/innerHtmlInserter.js"></script>
    
    <script>
        function goBack() {
            window.history.back();
        }
    </script>
</body>
</html>
