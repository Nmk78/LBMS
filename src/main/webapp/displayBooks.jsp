<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Results</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="global.css" /></head>
<body class="bg-gray-100">
    <nav
      id="nav"
      class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow-0 px-20 shadow-slate-300 bg-[--bg]"
    ></nav>
    <div class="max-w-4xl mx-auto shadow-md rounded-lg p-6">
        <h1 onclick="back()" class="text-2xl font-bold mb-4 cursor-pointer">⬅️</h1>
        <h1 class="text-2xl font-bold mb-4">Search Results</h1>
        <table class="min-w-full bg-white border border-gray-200">
            <thead>
                <tr>
                    <th class="py-2 px-4 border-b">Title</th>
                    <th class="py-2 px-4 border-b">Author</th>
                    <th class="py-2 px-4 border-b">Category</th>
                    <th class="py-2 px-4 border-b">Available</th>
                </tr>
            </thead>
            <tbody>
                <% 
                ResultSet rs = (ResultSet) request.getAttribute("resultSet");
                while (rs.next()) {
                %>
                    <tr class="border-b hover:bg-gray-100">
                        <td class="py-2 px-4"><%= rs.getString("title") %></td>
                        <td class="py-2 px-4"><%= rs.getString("authorName") %></td>
                        <td class="py-2 px-4"><%= rs.getString("category") %></td>
                        <td class="py-2 px-4"><%= rs.getBoolean("CopiesAvailable") ? "Yes" : "No" %></td>
                    </tr>
                <% 
                }
                %>
            </tbody>
        </table>
    </div>
    <script>
    	let back = ()=>{
            console.log("Bck");
    		window.history.back();
    	}
    </script>
        <script src="./scripts/innerHtmlInserter.js"></script>
</body>
</html>
