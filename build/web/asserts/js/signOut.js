async function  signout(){
    
    const response=await fetch("SignOut");
    
    if(response.ok){
        
        const json = await response.json();
        
        if(json.success){
            window.location.reload();
        }else{
            alert("Error");
        }
    }
    
}


