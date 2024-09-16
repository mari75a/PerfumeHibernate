async function signUp(){
    const user_dto = {
        firstName:document.getElementById("firstName").value,
        lastName:document.getElementById("lastName").value,
        email:document.getElementById("email").value,
        password:document.getElementById("password").value,
    }
    console.log(user_dto);
    const content = {
        method:"POST",
        body:JSON.stringify(user_dto),
        headers:{
            "Content-Type":"application/json"
        }
    }
    
    const response = await fetch("Signup",content);
    
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
            alert("Registered Successfully");
            window.location = "verify-account.html";

        } else {
            alert(json.content);
        }

    } else {
        alert("Please try Again later");
    }
}