<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
    <title>Profile | LBMS</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="global.css" />

<script>
        // Function to save data to LocalStorage
        function saveToLocalStorage(key, value) {
            localStorage.setItem(key, value);
        }

        // Function to get data from LocalStorage
        function getFromLocalStorage(key) {
            return localStorage.getItem(key);
        }

        // Example usage
function handlePageLoad() {
    var name = "<%= session.getAttribute("name") != null ? session.getAttribute("name").toString() : "" %>";
    var email = "<%= session.getAttribute("email") != null ? session.getAttribute("email").toString() : "" %>";
    var phone = "<%= session.getAttribute("phone") != null ? session.getAttribute("phone").toString() : "" %>";
    var idOrDept = "<%= session.getAttribute("idOrDept") != null ? session.getAttribute("idOrDept").toString() : "" %>";
        console.log("Name:", name || "No Name");
        console.log("Email:", email || "No Email");
        console.log("Phone:", phone || "No Phone");
        console.log("ID or Dept:", idOrDept || "No ID or Dept");

        saveToLocalStorage('name', name);
        saveToLocalStorage('email', email);
        saveToLocalStorage('phone', phone);
        saveToLocalStorage('idOrDept', idOrDept);
        saveToLocalStorage('isLoggedIn', true);

    }


        // Ensure the function runs when the page loads
        window.onload = handlePageLoad;
    </script>
</head>
<body>
<% 
    String name = (String) session.getAttribute("name");
    String email = (String) session.getAttribute("email");
    String phone = (String) session.getAttribute("phone");
    String idOrDept = (String) session.getAttribute("idOrDept");
%>
    <nav
      id="nav"
      class="flex sticky top-0 w-full h-18 py-1 justify-between items-center shadow-0 px-20 shadow-slate-300 bg-[--bg]"
    ></nav>
    <!-- Profile Container -->
    <div class="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-md">
      <!-- Profile Header -->
      <div class="flex h-full items-center border-b border-gray-200 pb-4">
        <img
          src="./assets/img/sample4.jpg"
          alt="Profile Picture"
          class="h-36 aspect-square rounded-0 object-cover mr-6"
        />
        <div class="border-r border-gray-200 pr-4">
          <div id="nameAndAchievement" class="flex gap-2">
            <h1 class="text-3xl font-semibold text-gray-800">${name}</h1>
          </div>
		  <p class="text-gray-500"><%= email %></p>
          <p class="text-gray-500"><%= phone %></p>
          <p class="text-gray-500">ID: <%= idOrDept %></p>
          
          
        </div>
        <div class="space-x-10 pl-4 flex justify-around">
          <div class="text-center space-y-3">
            <p class="text-[--secondary] font-semibold text-xl">Read</p>
            <p class="text-[--secondary] font-medium text-md">34</p>
          </div>
          <div class="text-center space-y-3">
            <p class="text-[--secondary] font-semibold text-xl">Saved</p>
            <p class="text-[--secondary] font-medium text-md">14</p>
          </div>
          <div class="text-center space-y-3">
            <p class="text-[--secondary] font-semibold text-xl">Status</p>
            <p class="text-[--danger] font-medium text-md">Over Date</p>
          </div>
        </div>
      </div>

      <!-- Profile Details -->
      <div class="mt-6">
        <h2 class="text-xl font-semibold text-[--secondary] mb-4">Saved</h2>
        <section
        id="cardContainer"
        class="w-full flex flex-wrap justify-between mx-auto h-full mb-5 gap-5"
      ></section>
      </div>

      <!-- Edit Profile Button -->
      <!-- <div class="mt-6 text-center">
        <a
          href="/edit-profile"
          class="bg-blue-500 text-white px-4 py-2 hover:bg-blue-600 transition duration-300"
          >Edit Profile</a
        >
      </div> -->
    </div>

    <script src="./scripts/innerHtmlInserter.js"></script>
    <script src="./scripts/book/card.js"></script>

</body>
</html>