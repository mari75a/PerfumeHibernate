async function loadProduct() {
    
    const parameters = new URLSearchParams(window.location.search);

    if (parameters.has("id")) {
        const productId = parameters.get("id");

        const response = await fetch("LoadSingleProduct?id=" + productId);

        if (response.ok) {
            const json = await response.json();

            console.log(json.product);

            const img1 = document.getElementById("image1");
            

            img1.src = "product/" + productId + "/image1.png";
            

            img1.parentElement.href = "product/" + productId + "/image1.png";
            

            
            

            document.querySelector("#product-title").innerHTML = json.product.title;
             document.querySelector("#id").innerHTML = json.product.id;

            document.getElementById("size").innerHTML=json.product.size.name;
            
            const price = json.product.price.toLocaleString("en-LK", {minimumFractionDigits: 2});
            document.getElementById("description").innerHTML = json.product.description;
            document.getElementById("price").innerHTML = "Rs. " + price;
//            const qty =document.getElementById("add-to-cart-quantity").value;
            document.getElementById("instock").innerHTML=json.product.qty ;
            
            
          

            

            cloneElement(json.productList);
//            loadSimilarProducts(json.productList);    

        } else {
            window.location = "index.html";
        }
    } else {
        window.location = "index.html";
    }
}

function loadSimilarProducts(list) {
    const template = document.getElementById("product-template");
    const container = document.getElementById("product-container");

    container.innerHTML = "";

    list.forEach(product => {
        const clone = document.importNode(template.content, true);
        const title = clone.querySelector('.title').children[0];

        title.innerHTML = product.title;
        title.href = "?id=" + product.id;

        document.getElementById("product-container").appendChild(clone);
    });

    $('.recent-product-activation').slick({
        infinite: true,
        slidesToShow: 4,
        slidesToScroll: 4,
        arrows: true,
        dots: false,
        prevArrow: '<button class="slide-arrow prev-arrow"><i class="fal fa-long-arrow-left"></i></button>',
        nextArrow: '<button class="slide-arrow next-arrow"><i class="fal fa-long-arrow-right"></i></button>',
        responsive: [{
                breakpoint: 1199,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 3
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 2
                }
            },
            {
                breakpoint: 479,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1
                }
            }
        ]
    });

}

function cloneElement(productList) {
    const container = document.getElementById("product-container");
    const productHtml = document.getElementById("similar-product");
    container.replaceChildren();
    productList.forEach(item => {

        const clone = productHtml.cloneNode(true);

        clone.querySelector("#similar-product-image").src = "product/" + item.id + "/image1.png";
        clone.querySelector(".title").innerHTML = item.title;
        clone.querySelector("#similar-product-storage").innerHTML = item.storage.value;
        clone.querySelector("#similar-product-price").innerHTML = "Rs. " + item.price.toLocaleString("en-LK", {minimumFractionDigits: 2});
        ;
        clone.querySelector("#similar-product-a1").href = "single-product.html?id=" + item.id;
        clone.querySelector("#similar-product-a2").href = "single-product.html?id=" + item.id;
        clone.querySelector("#similar-product-color-border").style.borderColor = item.color.name;
        clone.querySelector("#similar-product-color-background").style.backgroundColor = item.color.name;
        clone.querySelector("#similar-product-addto-cart").addEventListener("click",(e)=>{
            addToCart(item.id);
            e.preventDefault();
        });
        
        
        container.appendChild(clone);
    });

    $('.recent-product-activation').slick({
        infinite: true,
        slidesToShow: 4,
        slidesToScroll: 4,
        arrows: true,
        dots: false,
        prevArrow: '<button class="slide-arrow prev-arrow"><i class="fal fa-long-arrow-left"></i></button>',
        nextArrow: '<button class="slide-arrow next-arrow"><i class="fal fa-long-arrow-right"></i></button>',
        responsive: [{
                breakpoint: 1199,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 3
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 2
                }
            },
            {
                breakpoint: 479,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1
                }
            }
        ]
    });
}

async function addToCart(){
    const id=document.getElementById("id").innerHTML;
    const qty=document.getElementById("quantity-input").value;
    alert(qty);
    const response = await fetch("AddToCart?id="+id+"&qty="+qty);
    
    if(response.ok){
        const json = await response.json();
        console.log(json);
        if(json.success){
            success(json.content);
        }else{
            error(json.content);
        }
    }else{
        console.log("Error");
    }
    
}




