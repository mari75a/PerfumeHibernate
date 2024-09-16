async function loadorders(){
  
    const response=await fetch("Orders");
    if(response.ok){
        const json=await response.json();
         
             alert(json.id);
     
        
        
    }else{
       
        alert("something wrong");
    }
    
}


