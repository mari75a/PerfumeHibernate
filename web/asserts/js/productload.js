async function productLoad() {
    const response = await fetch("LoadProducts");

    if (response.ok) {

        const json = await response.json();
        const container = document.getElementById("product-container");
        const  cloneitem = document.getElementById("product-item");

        json.forEach(item => {
            const clone = cloneitem.cloneNode(true);

            clone.querySelector("#title").innerHTML = item.title;
            clone.querySelector("#category").innerHTML = item.brand.category.name;
            clone.querySelector("#price").innerHTML = item.price;
            clone.querySelector("#cart-item-image").src = "product/" + item.id + "/image1.png";
            clone.querySelector("#similar-product-addto-cart").addEventListener("click", (e) => {
                addToCart(item.id);
                e.preventDefault();
            });
            clone.querySelector("#cart-item-image").addEventListener("click", (e) => {
                window.location = "singleview.html?id=" + item.id + "";
                e.preventDefault();
            });
            container.appendChild(clone);
        });
    } else {

    }



}


async function addToCart(id) {
    alert(id);
    const response = await fetch("AddToCart?id=" + id+"&qty="+1);

    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
            success(json.content);
        } else {
            error(json.content);
        }
    } else {
        console.log("Error");
    }

}



