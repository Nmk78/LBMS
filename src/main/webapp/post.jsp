<%@page import="post.PostClass"%>
<%@page import="java.util.List"%>
<html>
<head>
    <title>Posts</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/global.css">
</head>
<body class="bg-gray-100 text-gray-900 font-sans">
    <nav id="nav" class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow px-20 bg-gray-200"></nav>
    
    <div class="container flex max-w-6xl mx-auto mt-6 space-x-5 px-6">
        <!-- Main Content Area -->
        <div class="w-3/4">
            <%
                List<PostClass> postList = (List<PostClass>) request.getAttribute("postList");
                if (postList != null && !postList.isEmpty()) {
            %>
                <div class="space-y-8">
                    <%
                        for (PostClass post : postList) {
                    %>
                        <a href="post?id=<%= post.getPostId() %>" id="<%= post.getPostId() %>" class="block bg-white shadow-md p-4 hover:shadow-lg transition-shadow duration-300">
                            <%
                                if (post.getImageBase64() != null && !post.getImageBase64().isEmpty()) {
                            %>
                                <img class="w-full object-cover mb-4" src="data:image/jpeg;base64,<%= post.getImageBase64() %>" alt="<%=post.getTitle() %>"/>
                            <%
                                }
                            %>
                            <h2 class="text-2xl font-semibold mb-2"><%= post.getTitle() %></h2>
                            <p class="text-gray-700 mb-4 line-clamp-3"><%= post.getContent() %></p>
                            <p class="text-sm text-gray-500">Posted: <%= post.getCreatedAt() %></p>
                        </a>
                    <%
                        }
                    %>
                </div>
            <%
                } else {
            %>
                <p class="text-center text-gray-600">No posts available.</p>
            <%
                }
            %>
        </div>

        <!-- Sidebar Area -->
        <div class="w-80 z-10 fixed top-20 right-32 bg-white p-5 shadow-md">
            <p class="text-xl font-bold text-gray-800 mb-5">Recent Posts</p>
            <%
                if (postList != null && !postList.isEmpty()) {
            %>
                <div class="space-y-5">
                    <%
                        for (PostClass post : postList) {
                    %>
                        <a href="#<%= post.getPostId() %>" class="block text-gray-700 truncate hover:underline"><%= post.getTitle() %></a>
                    <%
                        }
                    %>
                </div>
            <%
                } else {
            %>
                <p class="text-center text-gray-600">No recent posts available.</p>
            <%
                }
            %>
        </div>
    </div>

    <footer id="footer" class="mt-10 py-10 border-t-2 border-gray-300"></footer>
    
    <script src="./scripts/innerHtmlInserter.js"></script>
</body>
</html>
