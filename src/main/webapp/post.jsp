<%@ page import="java.util.List" %>
<%@ page import="post.Post" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Posts</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <h1>Posts</h1>
    <c:if test="${not empty postList}">
        <ul>
            <c:forEach var="post" items="${postList}">
                <li>
                    <h2>${post.title}</h2>
                    <p>${post.content}</p>
                    <c:if test="${not empty post.imageBase64}">
                        <img src="data:image/jpeg;base64,${post.imageBase64}" alt="${post.title}" width="300" height="200">
                    </c:if>
                    <p>Created at: ${post.createdAt}</p>
                    <p>Updated at: ${post.updatedAt}</p>
                </li>
            </c:forEach>
        </ul>
    </c:if>
    <c:if test="${empty postList}">
        <p>No posts available.</p>
    </c:if>
</body>
</html>
