const navContainer =  document.getElementById("nav")

const navInnerHtml = `	<nav class="flex w-full h-20 justify-between shadow-md px-5 shadow-slate-300 bg-[--bg]">
		<a href="/LBMS/" id="logo" class="w-16 h-20 flex items-center">
			<img src="assets/logo/logo.png" alt="Logo">
		</a>
		
		<div class="flex items-center w-auto h-full gap-x-3">
			<a href="/:id" class="text-[--secondary] font-semibold text-xl">Account</a>
			<form class="example" action="action_page.php">
				<input class=" border-none bg-primary h-1/2" type="text" placeholder="Search.." name="search">
				<button type="submit">Search</button>
			  </form>
		</div>
		
	</nav>`;


    navContainer.innerHTML = navInnerHtml;