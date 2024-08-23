/**
 * This file was for initial functions such as Auth Checking
 * 
 */



// Check URL on page load and initialize modal if needed
window.addEventListener("load", () => {
    const params = new URLSearchParams(window.location.search);
    const mode = params.get("mode");
    const err = params.get("err");
    const email = params.get("email");
	const name = params.get("name");
    const phone = params.get("phone");
    const idOrDept = params.get("idOrDept");


    if (mode) {
        authModalToggler(mode, err, email,name,phone,idOrDept);
    }
});

function authModalToggler(mode, err, email ,name,phone,idOrDept) {
    const modalContent = getModalContent(mode, err = "", email = "",name="",phone="",idOrDept="" );
    const modalContainer = document.createElement('div');
    modalContainer.innerHTML = modalContent;
    document.body.appendChild(modalContainer);

    const authClose = document.getElementById("authClose");
    if (authClose) {
        authClose.addEventListener("click", () => {
            modalContainer.remove();
        });
    }

    const toggleButton = mode === "SignIn" ? document.getElementById("signUp") : document.getElementById("signIn");
    if (toggleButton) {
        toggleButton.addEventListener("click", () => {
            modalContainer.remove();
            authModalToggler(mode === "SignIn" ? "SignUp" : "SignIn");
        });
    }
}
