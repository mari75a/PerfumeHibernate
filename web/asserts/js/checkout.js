   // Payment completed. It can be a successful failure.
    payhere.onCompleted = function onCompleted(orderId) {
        console.log("Payment completed. OrderID:" + orderId);
        alert("Payment Success");
    window.location="index.html";
        // Note: validate the payment and show success or failure page to the customer
    };

    // Payment window closed
    payhere.onDismissed = function onDismissed() {
        // Note: Prompt user to pay again or show an error page
        console.log("Payment dismissed");
    };

    // Error occurred
    payhere.onError = function onError(error) {
        // Note: show an error page
        console.log("Error:"  + error);
    };
    
async function checkout() {
    const response = await fetch("Loadchekout");
    if (response.ok) {
        const json = await response.json();
        if (json.success) {
            address = json.address;
            loadCities(json.cityList);
            document.getElementById("checkbox1").addEventListener("change", (e) => {
                setCurrentAddress(address);
                document.getElementById('city').dispatchEvent(new Event("change"));
                
            });
            loadOrder(json.cartList);
        } else {
            window.location = "sign-in.html";
        }

    } else {
        console.log("Error");
    }
}

function loadCities(cityList) {
    const cityTag = document.getElementById("city");
    cityTag.length = 1;
    for (city of cityList) {
        const option = document.createElement("option");
        option.innerHTML = city.name;
        option.value = city.id;
        cityTag.appendChild(option);
    }
}

function setCurrentAddress(address) {
    const fname = document.getElementById("first-name");
    const lname = document.getElementById("last-name");
    const city = document.getElementById("city");
    const line1 = document.getElementById("address1");
    const line2 = document.getElementById("address2");
    const postalcode = document.getElementById("postal-code");
    const mobile = document.getElementById("mobile");
    let isDisabled;
    if (document.getElementById("checkbox1").checked) {
        fname.value = address.firstname;
        lname.value = address.lastname;
        city.value = address.city.id;
        line1.value = address.line1;
        line2.value = address.line2;
        postalcode.value = address.postal_code;
        mobile.value = address.mobile;

        isDisabled = true;
    } else {
        isDisabled = false;
    }
    fname.disabled = isDisabled;
    lname.disabled = isDisabled;
    city.disabled = isDisabled;
    line1.disabled = isDisabled;
    line2.disabled = isDisabled;
    mobile.disabled = isDisabled;
}

function loadOrder(cartList) {
    const container = document.getElementById("order-list-container");
    const template = document.getElementById("order-cart-item");
    const subtotal = document.getElementById("subtotal-amount");
    const netTotal = document.getElementById("order-total-amount");
    const citySelect = document.getElementById("city");
    container.innerHTML = "";

    let total = 0;
    let totalQty = 0;
    for (item of cartList) {
        total += (item.product.price * item.qty);
        totalQty += item.qty;
        const clone = template.cloneNode(true);
        const qty = clone.querySelector("#order-item-quantity");
        qty.innerText = " x" + item.qty;
        const t = clone.querySelector("#order-item-title");
        t.innerText = item.product.title;
        t.appendChild(qty);
        clone.querySelector("#order-item-subtotal").innerHTML = "Rs. " + (item.qty * item.product.price).toLocaleString("en-LK", {minimumFractionDigits: 2});
        container.appendChild(clone);
    }

    let shippingFee;
    if (true) {
        shippingFee = totalQty * 1000;
    } else {
        shippingFee = totalQty * 2500;
    }

    citySelect.addEventListener("change", e => {
        loadOrder(cartList);
    });
    document.getElementById("order-total-amount").innerHTML = "Rs. " + (total + shippingFee).toLocaleString("en-LK", {minimumFractionDigits: 2});
    document.getElementById("subtotal-amount").innerHTML = "Rs. " + total.toLocaleString("en-LK", {minimumFractionDigits: 2});
    document.getElementById("shipping-fee").innerHTML = "Rs. " + shippingFee.toLocaleString("en-LK", {minimumFractionDigits: 2});

}

async function proceedCheckout() {
    let isCurrentAddress = document.getElementById("checkbox1").checked;
    const data = {
        fname: document.getElementById("first-name").value,
        lname: document.getElementById("last-name").value,
        city: document.getElementById("city").value,
        line1: document.getElementById("address1").value,
        line2: document.getElementById("address2").value,
        postalcode: document.getElementById("postal-code").value,
        mobile: document.getElementById("mobile").value,
    }
    const request = {
        isCurrentAddress: isCurrentAddress
    }
     Object.assign(request, data)
//    if (!isCurrentAddress) {
//        Object.assign(request, data)
//    }
    console.log(request);
    
    const response = await fetch("Checkout",{
        method:"POST",
        body:JSON.stringify(request),
        headers:{
            "Content-Type":"application.json",
        }
    });

    if (response.ok) {
        const json = await response.json();
        console.log(json);
        if (json.success) {
            
            startPay(json.payment);
        } else {
            error(json.error)
        }
    } else {
        console.log("Error");
    }
}

function startPay(payment){
    payhere.startPayment(payment);
    
}