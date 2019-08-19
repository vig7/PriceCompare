
var searchBrand="";
//get phone brand name
function getSearchValue() {
    var x = document.getElementById("mobile-brands").value;
    searchBrand=x;
    alert(searchBrand);
    var arr=["nokia 7.1","nokia Pro"]
    var select=document.getElementById("mobile-models");
    for(i=0;i<arr.length;i++){
        var opt1 = document.createElement("option");
        opt1.value = arr[i];
        opt1.text = arr[i];
        select.add(opt1, null);

    }
}

//enabling searching
function search() {
    var selectModel=document.getElementById("mobile-models").value;
    var selectBrand=document.getElementById("mobile-brands").value;
    alert(selectBrand+" "+selectModel);
}

//loading details page
  function loadDetails(val){
    window.location.href="productDescription.html";
}
//modal data loading
  function showDeals(){
      
      var deals=["Amazon","Flipkart","SnapDeal"];
      var prices=["Rs.12,000","Rs.15000","Rs.15000"];
      var text=" ";
      var price=" ";
      var button=" ";
      for(i=0;i<deals.length;i++){
         text+="<p>"+deals[i]+"</p><br>";
         price+="<p>"+prices[i]+"</p><br>";
         button+='<a href="" class="btn btn-primary " style="margin-bottom:10px;">'+'Go to site'+'</a><br>';
      }
      document.getElementById("show-deals").innerHTML=text;
      document.getElementById("show-price").innerHTML=price;
      document.getElementById("show-button").innerHTML=button;
  }

  function getDetails(){
      
  }

 function sendFeedback(){
     var email=document.getElementById("usr").value;
     var comment=document.getElementById("comment").value;
     if(email==null)
         email="Anonymous";
     alert(email+" "+comment);
 }

 function giveRatings(){
    var ratings=document.getElementById("ratings").value;
    if(ratings!="-")
        alert(ratings); 
 }

 function addFeatures(){
     var text=" ";
     var detailValue=1;
     for(i=0;i<8;i++){
         text+='<div class="col-sm-3 fix-sides"><div class="product-image-wrapper"><div class="single-products"><div class="card text-center"><img class="pop-up" style="height:200px" src="assets/images/lg.jpg" alt="" /><h5 class="card-title">RS. 15,000</h5><p class="card-text">Google Pixel</p><button class="btn btn-default add-to-cart" value ="'+detailValue+'"  onclick="loadDetails(this.value)"><i class="fa fa-shopping"></i>See details</button></div>  </div></div></div>';
       
     }
     document.getElementById("fix-features").innerHTML=text;
 }
 
 //fetching and showing specific mobile phone specs
 function showSpecifications(){
     var text=" ";
     var name="LG Pro";
     var price="Best Price: Rs :12,000";
     text=' <table class="table table-bordered"> <tbody> <tr style="border: none"><td >John</td><td>john@example.com</td></tr><tr><td>Mary</td><td>mary@example.com</td></tr><tr><td>July</td><td>july@example.com</td></tr></tbody></table>';
     document.getElementById("prodName").innerHTML=name;
     document.getElementById("prodPrice").innerHTML=price;
     document.getElementById("showSpecifications").innerHTML=text;
 }

 function loadReviews(){

 }

 