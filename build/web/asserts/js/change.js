function change(id){
    if(id==1){
      document.getElementById("div1").style.display="block";
      document.getElementById("div1").style.marginTop="50px";
      document.getElementById("div2").style.display="none";
      document.getElementById("div3").style.display="none";
      document.getElementById("div4").style.display="none";
      
    }else if(id==2){
      document.getElementById("div1").style.display="none";
      document.getElementById("div2").style.display="block";
      document.getElementById("div3").style.display="none";
      document.getElementById("div4").style.display="none";
      
    }
    else if(id==3){
      document.getElementById("div1").style.display="none";
      document.getElementById("div2").style.display="none";
      document.getElementById("div3").style.display="block";
      document.getElementById("div4").style.display="none";
      loadUserProduct();
      
    }
    else if(id==4){
      document.getElementById("div1").style.display="none";
      document.getElementById("div2").style.display="none";
      document.getElementById("div3").style.display="none";
      document.getElementById("div4").style.display="block";
      
      loadorders();
    }

}