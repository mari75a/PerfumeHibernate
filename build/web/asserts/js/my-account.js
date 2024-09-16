


var data;
async function loadUserProduct() {
    
    const response = await fetch("LoadUserProduct");
    if (response.ok) {
        const productList1 = await response.json();
        const container = document.getElementById("product-container1");

        const productHtml = document.getElementById("product-row");
        for (let i = 0; i < productList1.length; i++) {
    const item = productList1[i];
            
    const clone = productHtml.cloneNode(true);
    clone.querySelector("#id").innerHTML = item.id;
    clone.querySelector("#title").innerHTML = item.title;
    clone.querySelector("#price1").innerHTML = " " + item.price.toLocaleString("en-LK", {minimumFractionDigits: 2});
    
//    clone.querySelector("#removebtn").addEventListener("click", function(e) {
//        removefromCart(item.product.id);
//        e.preventDefault();
//    });
container.appendChild(clone);
}
    } else {
        console.log("Error");
   }
}
async function loadUser() {
    const response = await fetch("LoadUser");
    if (response.ok) {
        const user = await response.json();
        console.log(user);
        document.getElementById("email").value = user.email;
        document.getElementById("password").value = user.password;
        document.getElementById("first_name").value = user.firstName;
        document.getElementById("last_name").value = user.lastName;
    } else {
        console.log("Error");
    }
}
async function loadFeatures() {

    const response = await fetch("LoadFeatures");
    if (response.ok) {
        data = await response.json();
        console.log(data);
        for (let key in data) {
            let tag = document.getElementById(key);
            console.log(key);
            data[key].forEach(item => {
                let optionTag = document.createElement("option");
                optionTag.value = item.id;
                optionTag.innerHTML = item.name || item.value;
                tag.appendChild(optionTag);
            });
        }
    } else {
        console.log("Error");
    }
}

function updateModels() {
    let selectedCategoryId = document.getElementById("categoryList").value;
    let modelSelectTag = document.getElementById("brandList");
    modelSelectTag.length = 1;
    data.brandList.forEach(brand => {
        if (brand.category.id == selectedCategoryId) {
            let optionTag = document.createElement("option");
            optionTag.value = brand.id;
            optionTag.innerHTML = brand.name;
            modelSelectTag.appendChild(optionTag);
        }
    });
}

async function productLiisting() {
    const categoryList = document.getElementById("categoryList");
    const brandList = document.getElementById("brandList");
    const sizeList = document.getElementById("sizeList");
    const title = document.getElementById("title");
    const description = document.getElementById("description");
    const price = document.getElementById("price");
    const quantity = document.getElementById("qty");
    const img1 = document.getElementById("file");
    const form = new FormData();
    form.append("categoryId", categoryList.value);
    form.append("brandId", brandList.value);
    form.append("sizeId", sizeList.value);
    form.append("title", title.value);
    form.append("description", description.value);
    form.append("price", price.value);
    form.append("quantity", quantity.value);
    form.append("img1", img1.files[0]);
    const message = document.getElementById("message")
    const response = await fetch("ProductListing", {
        method: "POST",
        body: form,
    });
    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
            success(json.content);
            window.location.reload();
        } else {
            error(json.content);
            console.log(json.content);
        }
    } else {
        document.getElementById("message").innerHTML = "PLease try again later!";
    }

}





