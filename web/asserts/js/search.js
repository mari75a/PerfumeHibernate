async function loadData() {
    const response = await fetch("LoadSearchData");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
            loadCategoryList(json.categoryList);
//            loadConditions(json.conditionList);
//            loadStorageList(json.storageList);
//            loadColorList(json.colorList);
//            loadProductList(json.productList);
//            loadPagination(json.totalProductsCount);
        } else {
            error("Data not available");
        }
    } else {
        error("Something went wrong");
    }
}

//function loadCategoryList(categoryList) {
//    const ul = document.getElementById("category-options");
//    ul.innerHTML = "";
//    for (item of categoryList) {
//        let listItemTag = document.createElement("li");
//        let anchurTag = document.createElement("a");
//        anchurTag.innerHTML = item.name;
//        listItemTag.appendChild(anchurTag);
//        ul.appendChild(listItemTag);
//    }
//
//    const categoryOptions = document.querySelectorAll('#category-options li');
//    categoryOptions.forEach(option => {
//        option.addEventListener('click', function () {
//            categoryOptions.forEach(opt => opt.classList.remove('current-cat'));
//            this.classList.add('current-cat');
//        });
//    });
//
//}
//
//function loadConditions(conditionList) {
//    const ul = document.getElementById("condition-options");
//    ul.innerHTML = "";
//    for (item of conditionList) {
//        let listItemTag = document.createElement("li");
//        let anchurTag = document.createElement("a");
//        anchurTag.innerHTML = item.name;
//        listItemTag.appendChild(anchurTag);
//        ul.appendChild(listItemTag);
//    }
//    const conditionOptions = document.querySelectorAll('#condition-options li');
//    conditionOptions.forEach(option => {
//        option.addEventListener('click', function () {
//            conditionOptions.forEach(opt => opt.classList.remove('chosen'));
//            this.classList.add('chosen');
//        });
//    });
//}
//function loadStorageList(storageList) {
//    const ul = document.getElementById("storage-options");
//    ul.innerHTML = "";
//    for (item of storageList) {
//        let listItemTag = document.createElement("li");
//        let anchurTag = document.createElement("a");
//        anchurTag.innerHTML = item.value;
//        listItemTag.appendChild(anchurTag);
//        ul.appendChild(listItemTag);
//    }
//    const storageOptions = document.querySelectorAll('#storage-options li');
//    storageOptions.forEach(option => {
//        option.addEventListener('click', function () {
//            storageOptions.forEach(opt => opt.classList.remove('chosen'));
//            this.classList.add('chosen');
//        });
//    });
//}
//function loadColorList(colorList) {
//    const container = document.getElementById("color-options");
//    const template = document.getElementById("color-tag");
//    container.innerHTML = "";
//    for (item of colorList) {
//        const clone = template.cloneNode(true);
//        clone.querySelector(".color-extra-02").style.background = item.name;
//        clone.style.borderColor = item.name;
//        container.appendChild(clone);
//    }
//    const colorOptions = document.querySelectorAll('#color-options li');
//    colorOptions.forEach(option => {
//        option.addEventListener('click', function () {
//            colorOptions.forEach(opt => opt.classList.remove('chosen'));
//            this.classList.add('chosen');
//        });
//    });
//}
//
//function loadProductList(productList) {
//   const container=document.getElementById("product-container");
//         const  cloneitem=document.getElementById("product-item");
//    container.innerHTML = "";
//    for (item of productList) {
//        const clone = cloneitem.cloneNode(true);
//        const clone=cloneitem.cloneNode(true);
//            
//            clone.querySelector("#title").innerHTML=item.title;
//            clone.querySelector("#cart-item-image").src = "product/" + item.id + "/image1.png";
//             clone.querySelector("#similar-product-addto-cart").addEventListener("click",(e)=>{
//            addToCart(item.id);
//            e.preventDefault();
//        });
//            container.appendChild(clone);
//    }
//}
//
//function loadPagination(totalProductsCount) {
//    let products_per_page = 6;
//    let page_count = Math.ceil(totalProductsCount / products_per_page);
//
//    const container = document.getElementById("pagination-button-container");
//    const template = document.getElementById("pagination-button-template");
//    container.innerHTML = "";
//
//    let prevButton = template.cloneNode(true);
//    prevButton.innerHTML = "Prev";
//    container.appendChild(prevButton);
//
//    for (let i = 1; i <= page_count; i++) {
//        let pageButton = template.cloneNode(true);
//        pageButton.innerHTML = i;
//        container.appendChild(pageButton);
//    }
//    
//    let next = template.cloneNode(true);
//    next.innerHTML = "Next";
//    container.appendChild(next);
//}
async function searchProducts(){
    
    const categoryList = document.getElementById("categoryList").value;
    const brandList = document.getElementById("brandList").value;
    const sizeList = document.getElementById("sizeList").value;
    
    if(categoryList.equals("Choose a category")){
        alert("category is empty");
        categoryList=null;
    }
    if(brandList=="Choose a Brand"){
        categoryList=null;
    }
    if(sizeList=="Choose a size"){
        sizeList=null;
    }
   
    const data = {
        category:categoryList,
        brand:brandList,
        size:sizeList,

//        min_price:$('#slider-range').slider('values', 0),
//        max_price:$('#slider-range').slider('values', 1),
//        sort_text:document.querySelector('#sort-filter-options').value
    };
    console.log(data);
    const response = await fetch("SearchProducts",{
        method:"POST",
        body:JSON.stringify(data),
        headers:{
            "Content-Type":"application/json"
        }
     }); 
     
     if(response.ok){
         const json = await response.json();
         if(json.success){
             console.log(json.productList);
//             success("Search success");
             loadProductList(json.productList);
//             loadPagination(json.totalProductsCount);
         }else{
              error("Data not available");
         }
     }else{
         error("Something went wrong");error("Something went wrong");
     }
}
