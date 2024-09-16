async function loadCartItems(){

const response = await fetch("loadCartItems");
        if (response.ok){
const json = await response.json();
        if (json.length === 0){
info("You have no items in your cart");
        console.log("You have no items in your cart");
} else{
const data = cloneCartElement(json);
        document.getElementById("cart-item-total").innerHTML = " " + data.total.toLocaleString("en-LK", {minimumFractionDigits: 2});
}
} else{
error("Unable to perform your request");
        console.log("Error");
}
}

function cloneCartElement(productList) {
const container = document.getElementById("cart-item-container");
        const productHtml = document.getElementById("cart-item-row");
        container.replaceChildren();
        let totalQty = 0;
        let total = 0;
        productList.forEach(item => {
        const clone = productHtml.cloneNode(true);
                totalQty += item.qty;
                total += (item.product.price * item.qty);
                clone.querySelector("#cart-item-image").src = "product/" + item.product.id + "/image1.png";
                clone.querySelector("#cart-item-title").innerHTML = item.product.title;
                clone.querySelector("#cart-item-price").innerHTML = " " + item.product.price.toLocaleString("en-LK", {minimumFractionDigits: 2});
                clone.querySelector("#removebtn").addEventListener("click", (e) => {
        removefromCart(item.product.id);
                e.preventDefault();
        });
                clone.querySelector("#cart-item-qty").innerHTML = item.qty;
//        clone.querySelector(".product-subtotal").innerHTML = "Rs. "+(item.product.price*item.qty).toLocaleString("en-LK", {minimumFractionDigits: 2});

                container.appendChild(clone);
        });
        return {
        total:total,
                totalQty:totalQty
        };
}

async function removefromCart(id){
const response = await fetch("RemoveCart?id=" + id);
        if (response.ok) {
const json = await response.json();
        console.log(json);
        if (json.success) {
success(json.content);
            loadCartItems();
} else {
error(json.content);
}
} else {
console.log("Error");
}
}
