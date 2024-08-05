/**
 * This file was for initial functions such as Auth Checking
 * 
 */



// Check URL on page load and initialize modal if needed
window.addEventListener("load", () => {
    const params = new URLSearchParams(window.location.search);
    const mode = params.get("mode");
    if (mode) {
      authModalToggler(mode);
    }

    // check auth state via sessionStorage

    window.sessionStorage
  });
  
  