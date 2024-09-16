/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

async function loadData() {
    const response = await fetch("LoadSearchData");
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
//            loadCategoryList(json.categoryList);
//            loadConditions(json.conditionList);
//            loadStorageList(json.storageList);
//            loadColorList(json.colorList);
//            loadProductList(json.productList);
//            loadPagination(json.totalProductsCount);
loadProductList(json.productList,json);
        } else {
            error("Data not available");
        }
    } else {
        error("Something went wrong");
    }
}


var st_pagination_button = document.getElementById("st-pagination-button");

var currentPage = 0;

function loadProductList(productList,json) {
    
    let st_pagination_container = document.getElementById("st-pagination-container");
    st_pagination_container.innerHTML = "";

    let product_count = json.allProductCount;
    const product_per_page = 4;

    let pages = Math.ceil(product_count / product_per_page);

    //add previous button
    if (currentPage != 0) {
        let st_pagination_button_clone_prev = st_pagination_button.cloneNode(true);
        st_pagination_button_clone_prev.innerHTML = "Prev";

        st_pagination_button_clone_prev.addEventListener("click", e => {
            currentPage--;
            searchProducts(currentPage * 4);
        });

        st_pagination_container.appendChild(st_pagination_button_clone_prev);
    }

    //add page buttons
    for (let i = 0; i < pages; i++) {
        let st_pagination_button_clone = st_pagination_button.cloneNode(true);
        st_pagination_button_clone.innerHTML = i + 1;

        st_pagination_button_clone.addEventListener("click", e => {
            currentPage = i;
            searchProducts(i * 4);
        });

        if (i === currentPage) {
            st_pagination_button_clone.className = "axil-btn btn-bg-secondary me-2";
        } else {
            st_pagination_button_clone.className = "axil-btn btn-bg-primary me-2";
        }

        st_pagination_container.appendChild(st_pagination_button_clone);
    }

    //add Next button
    if (currentPage != (pages - 1)) {
        let st_pagination_button_clone_next = st_pagination_button.cloneNode(true);
        st_pagination_button_clone_next.innerHTML = "Next";

        st_pagination_button_clone_next.addEventListener("click", e => {
            currentPage++;
            searchProducts(currentPage * 4);
        });

        st_pagination_container.appendChild(st_pagination_button_clone_next);
    }
    const container = document.getElementById("product-container");
    const  cloneitem = document.getElementById("product-item");
    container.innerHTML = "";
    for (item of productList) {
        const clone = cloneitem.cloneNode(true);
        clone.querySelector("#category").innerHTML = item.brand.category.name;
        clone.querySelector("#price").innerHTML = item.price;
        clone.querySelector("#title").innerHTML = item.title;
        clone.querySelector("#cart-item-image").src = "product/" + item.id + "/image1.png";
        clone.querySelector("#similar-product-addto-cart").addEventListener("click", (e) => {
            addToCart(id1);
            e.preventDefault();
        });
        const id1=item.id;
        clone.querySelector("#cart-item-image").addEventListener("click", (e) => {
                window.location = "singleview.html?id=" + id1 + "";
                e.preventDefault();
            });
        container.appendChild(clone);
    }
}

function loadPagination(totalProductsCount) {
    let products_per_page = 6;
    let page_count = Math.ceil(totalProductsCount / products_per_page);

    const container = document.getElementById("pagination-button-container");
    const template = document.getElementById("pagination-button-template");
    container.innerHTML = "";

    let prevButton = template.cloneNode(true);
    prevButton.innerHTML = "Prev";
    container.appendChild(prevButton);

    for (let i = 1; i <= page_count; i++) {
        let pageButton = template.cloneNode(true);
        pageButton.innerHTML = i;
        container.appendChild(pageButton);
    }

    let next = template.cloneNode(true);
    next.innerHTML = "Next";
    container.appendChild(next);
}
async function searchProducts() {

    var categoryList = document.getElementById("categoryList").value;
    var brandList = document.getElementById("brandList").value;
    var sizeList = document.getElementById("sizeList").value;
    var search = document.getElementById("search").value;
    var min = document.getElementById("min").value;
    var max = document.getElementById("max").value;
    alert("ok");
    if (categoryList == "Choose a category") {
        if (brandList == "Choose a Brand") {
             var data = {
                
                search:search,

        min_price:min,
        max_price:max,
        sort_text:document.querySelector('#sort-filter-options').value
            };
        }else{
            var data = {
                brand: brandList,
                search:search,

        min_price:min,
        max_price:max,
        sort_text:document.querySelector('#sort-filter-options').value
            };
        }
    } else if (brandList == "Choose a Brand") {
        var data = {
            category: categoryList,
            search:search,

        min_price:min,
        max_price:max,
        sort_text:document.querySelector('#sort-filter-options').value
        };
    } else {
        var data = {
            category: categoryList,
            brand: brandList,
            search:search,

        min_price:min,
        max_price:max,
        sort_text:document.querySelector('#sort-filter-options').value
        };

    }
    
    

    console.log(data);
    const response = await fetch("SearchProducts", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            console.log(json.productList);
//             success("Search success");
            loadProductList(json.productList);
             loadPagination(json.totalProductsCount);
        } else {
            error("Data not available");
        }
    } else {
        error("Something went wrong");
    }
}

async function searchProducts(firstResult) {

    var categoryList = document.getElementById("categoryList").value;
    var brandList = document.getElementById("brandList").value;
    var sizeList = document.getElementById("sizeList").value;
    var search = document.getElementById("search").value;
     var min = document.getElementById("min").value;
    var max = document.getElementById("max").value;
    if (categoryList == "Choose a category") {
        if (brandList == "Choose a Brand") {
             var data = {
                
                search:search,
                firstResult: firstResult,

min_price:min,
        max_price:max,
        sort_text:document.querySelector('#sort-filter-options').value
            };
        }else{
            var data = {
                brand: brandList,
                search:search,
                firstResult: firstResult,

min_price:min,
        max_price:max,
        sort_text:document.querySelector('#sort-filter-options').value
            };
        }
    } else if (brandList == "Choose a Brand") {
        var data = {
            category: categoryList,
            search:search,
            firstResult: firstResult,

min_price:min,
        max_price:max,
        sort_text:document.querySelector('#sort-filter-options').value
        };
    } else {
        var data = {
            category: categoryList,
            brand: brandList,
            search:search,
            firstResult: firstResult,

min_price:min,
        max_price:max,
        sort_text:document.querySelector('#sort-filter-options').value
        };

    }
    
    

    console.log(data);
    const response = await fetch("SearchProducts", {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json"
        }
    });

    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            console.log(json.productList);
             success("Search success");
            loadProductList(json.productList,json);
//             loadPagination(json.totalProductsCount);
        } else {
            error("Data not available");
        }
    } else {
        error("Something went wrong");
    }
}


//function updateProductView(json) {
//
//    let st_product_container = document.getElementById("st-product-container");
//
//    st_product_container.innerHTML = "";
//
//    json.productList.forEach(product => {
//        let st_product_clone = st_product.cloneNode(true);
//
//        //update cards
//        st_product_clone.querySelector("#st-product-a-1").href = "single-product.html?pid=" + product.id;
//        st_product_clone.querySelector("#st-product-img-1").src = "product_images/" + product.id + "/image1.png";
//        st_product_clone.querySelector("#st-product-a-2").href = "single-product.html?pid=" + product.id;
//        st_product_clone.querySelector("#st-product-title-1").innerHTML = product.title;
//        st_product_clone.querySelector("#st-product-price-1").innerHTML = new Intl.NumberFormat(
//                "en-US",
//                {
//                    minimumFractionDigits: 2
//                }
//        ).format(product.price);
//
//        st_product_container.appendChild(st_product_clone);
//
//    });
//
//    //start pagination
//    let st_pagination_container = document.getElementById("st-pagination-container");
//    st_pagination_container.innerHTML = "";
//
//    let product_count = json.allProductCount;
//    const product_per_page = 6;
//
//    let pages = Math.ceil(product_count / product_per_page);
//
//    //add previous button
//    if (currentPage != 0) {
//        let st_pagination_button_clone_prev = st_pagination_button.cloneNode(true);
//        st_pagination_button_clone_prev.innerHTML = "Prev";
//
//        st_pagination_button_clone_prev.addEventListener("click", e => {
//            currentPage--;
//            searchProducts(currentPage * 6);
//        });
//
//        st_pagination_container.appendChild(st_pagination_button_clone_prev);
//    }
//
//    //add page buttons
//    for (let i = 0; i < pages; i++) {
//        let st_pagination_button_clone = st_pagination_button.cloneNode(true);
//        st_pagination_button_clone.innerHTML = i + 1;
//
//        st_pagination_button_clone.addEventListener("click", e => {
//            currentPage = i;
//            searchProducts(i * 6);
//        });
//
//        if (i === currentPage) {
//            st_pagination_button_clone.className = "axil-btn btn-bg-secondary me-2";
//        } else {
//            st_pagination_button_clone.className = "axil-btn btn-bg-primary me-2";
//        }
//
//        st_pagination_container.appendChild(st_pagination_button_clone);
//    }
//
//    //add Next button
//    if (currentPage != (pages - 1)) {
//        let st_pagination_button_clone_next = st_pagination_button.cloneNode(true);
//        st_pagination_button_clone_next.innerHTML = "Next";
//
//        st_pagination_button_clone_next.addEventListener("click", e => {
//            currentPage++;
//            searchProducts(currentPage * 6);
//        });
//
//        st_pagination_container.appendChild(st_pagination_button_clone_next);
//    }
//
//}
