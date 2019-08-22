var phone_id;
var res;
console.log(localStorage);

//live search
function dynamicSearch(str){
    console.log(str);
    if (str.length==0) { 
        document.getElementById("livesearch").innerHTML="";
        document.getElementById("livesearch").style.border="0px";
        return;
      }
      if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
      } else {  // code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
      xmlhttp.onreadystatechange=function() {
        if (this.readyState==4 && this.status==200) {
            console.log(this);
            searchResults(this);
        }
      }
      xmlhttp.open("GET","http://localhost:5678/SearchResults?searchKey="+str,true);
      xmlhttp.send();
}

function searchResults(obj){
   var responseSearch= obj.response;
    responseSearch=JSON.parse( responseSearch);
    console.log( responseSearch);
    var text="";
    if( responseSearch.length!=0){
        for(i=0;i<10;i++){
            let name= responseSearch[i].Name
            console.log(name);
            text+='<li><ul><h5 id="clickRes" style="display:block;border:1px #A5ACB2" onclick="setSearchVal('+"'"+name+"',"+"'"+ responseSearch[i].id+"'"+')">'+name+'</h5></ul></li>';
            document.getElementById("livesearch").innerHTML=text;
        }
    }
    else text="Sorry!!!! No suggestions";
    document.getElementById("livesearch").innerHTML=text;
    document.getElementById("livesearch").style.border="1px solid #A5ACB2";
}

function setSearchVal(v,id){
    localStorage.setItem("prod_id", id);
    document.getElementById("mobile-brands").value=v;
    var x = document.getElementById("livesearch");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        document.getElementById("livesearch").innerHTML="";
        document.getElementById("livesearch").style.border="0px";
        return;
    }
}

//modal data loading
  function showDeals(){
    var deals=["Amazon","Flipkart","SnapDeal","Paytm"];
    var links=[];
    var prices=[];
    var text=" ";
    var price=" ";
    var button=" ";
    let flipPrice=res[0].flipkartPrice;
    let snapPrice=res[0].SnapPrice;
    prices[0]="Not Available";
    if(flipPrice!="null"){
        prices[1]=flipPrice;
        links[1]=res[0].FlipkartLink;
    }
    else{
        prices[1]="Not Available";
    }
    if(snapPrice!="" ){
        prices[2]=snapPrice;
        links[2]=res[0].SnapLink;
    }
    else{
        prices[2]="Not Available";
    }
    prices[3]="Not Available";
      for(i=0;i<deals.length;i++){
         text+="<p>"+deals[i]+"</p><br>";
         price+="<p>"+prices[i]+"</p><br>";
         if(i==1)
            button+='<a href="https://'+links[i]+'" class="btn btn-primary " style="margin-bottom:10px;">'+'Go to site'+'</a><br>';
         else
            button+='<a href="'+links[i]+'" class="btn btn-primary " style="margin-bottom:10px;">'+'Go to site'+'</a><br>';
      }
      document.getElementById("show-deals").innerHTML=text;
      document.getElementById("show-price").innerHTML=price;
      document.getElementById("show-button").innerHTML=button;
  }

//feedback data
 function sendFeedback(){
     var email=document.getElementById("usr").value;
     var comment=document.getElementById("comment").value;
     if(email==null)
         email="Anonymous";
     alert(email+" "+comment);
 }

 //ratings data
 function giveRatings(){
    var ratings=document.getElementById("ratings").value;
    if(ratings!="-")
        alert(ratings); 
 }

 //Adding featured mobiles
 function show() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            addFeatures(this); 
        }
    };
    xmlhttp.open("GET", "http://localhost:5678/FeaturedPhones", true);
    xmlhttp.send();
 }

function addFeatures(obj){
    var text=" ";
     res= obj.response;
    res=JSON.parse(res);
    // console.log(res)
    for(i=0;i<8;i++){
       let flipPrice=res[i].flipkartPrice;
       let snapPrice=res[i].SnapPrice;
       let amazonPrice=res[i].amazonPrice;
       if(flipPrice >= snapPrice && flipPrice!="null" && snapPrice !=""){
           price=snapPrice;
       }
       else if(flipPrice < snapPrice && flipPrice!="null" && snapPrice !=""){
           price=flipPrice;
       }
       else if(flipPrice=="null" && snapPrice!=""){
           price=snapPrice;
       }
        else if(snapPrice=="" && flipPrice!="null"){
           price=flipPrice;
        }
        else
           price="Not Available";
       text+='<div class="col-sm-3 fix-sides"><div class="product-image-wrapper"><div class="single-products"><div class="card text-center"><img class="pop-up" style="height:200px" src="assets/images/lg.jpg" alt="" /><h5 class="card-title">'+price+'</h5><p class="card-text">'+res[i].Name+'</p><button class="btn btn-default add-to-cart" value ="'+res[i].id+'"  onclick="loadDetails(this.value)"><i class="fa fa-shopping"></i>See details</button></div>  </div></div></div>';
    }
    document.getElementById("fix-features").innerHTML=text;
 }
 
 //loading details page
 function loadDetails(val){
    localStorage.setItem("prod_id", val);
    window.location.href="productDescription.html"
}

 //fetching and showing specific mobile phone specs
 function showMobilePage() {
    var url="";
    phone_id=localStorage.getItem("prod_id");
    url= "http://localhost:5678/MobileSpecs?id="+phone_id;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this);
            showSpecifications(this); 
        }
    };
    xmlhttp.open("GET",url, true);
    xmlhttp.send();
 }

 function showSearchResultPage(val){
    var url="";
    url="http://localhost:5678/SearchSpecificResults?searchKey="+val;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // console.log();
            // if(JSON.parse(this.response).length==0){
                
            //     return -1;
            // }
            // else{
                console.log(this)
                showSpecifications(this,val); 
               
            //     return 0;
            // }
            
        }
    };
    xmlhttp.open("GET",url, true);
    xmlhttp.send();

 }

 function showSpecifications(obj,val){
    var specsObj= obj.response;
     res=JSON.parse(specsObj);
    console.log("searchspecs"+res)
    var text=" ";
    if(!val)
        var name=res[0].Name;
    else
        var name=val;
    var price="Best Price: ";
    let flipPrice=res[0].flipkartPrice;
    let snapPrice=res[0].SnapPrice;
    if(flipPrice >= snapPrice && flipPrice!="null" && snapPrice !=""){
        price=price+napPrice;
    }
    else if(flipPrice < snapPrice && flipPrice!="null" && snapPrice !=""){
        price=price+"Rs."+flipPrice;
    }
    else if(flipPrice=="null" && snapPrice!=""){
        price=price+snapPrice;
    }
    else if(snapPrice=="" && flipPrice!="null"){
        price=price+"Rs."+flipPrice;
    }
    else
        price="Currently out of stock";    
    text=' <table class="table table-bordered"> <tbody> <tr style="border: none"><td >Model Name</td><td>'+res[0].Name+'</td></tr><tr><td>Operating System</td><td>'+res[0].operatingSystem+'</td></tr><tr><td>Camera</td><td>'+res[0].Camera+'</td></tr><tr><td>Display</td><td>'+res[0].Display+'</td></tr><tr><td>Battery</td><td>'+res[0].Battery+'</td></tr><tr><td>Special Features</td><td>'+res[0].specialFeat+'</td></tr><tr><td>RAM</td><td>'+res[0].RAM+'</td></tr></tbody></table>';
    document.getElementById("prodName").innerHTML=name;
    document.getElementById("prodPrice").innerHTML=price;
    document.getElementById("showSpecifications").innerHTML=text;
    var butvalue=localStorage.getItem("prod_id");
    console.log(butvalue);
    document.getElementById("show-compare").innerHTML='<div class="row" style="padding:20px"><div class="col-sm-3"><button class="btn btn-primary" onclick="obtainSpecs('+"'"+butvalue+"'"+')">Compare</button></div></div>';
 }

 function obtainSpecs(id) {
    phone_id=localStorage.setItem("compare_id",id);
    window.location.href="comparePrices.html" 
 }

 function sendRequest(){
    var url="";
    phone_id=localStorage.getItem("compare_id");
    url= "http://localhost:5678/MobileSpecs?id="+phone_id;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            setCompareVal(this,phone_id);
        }
    };
    xmlhttp.open("GET",url, true);
    xmlhttp.send();
 }

 function setCompareVal(obj,id){
     var res=obj.response;
     res=JSON.parse(res);
     var compareProd="";
     console.log(id);
     compareProd={id:res[0].id,Name:res[0].Name,flipkartPrice:(res[0].flipkartPrice?res[0].flipkartPrice:"Not available"),
        SnapPrice:res[0].SnapPrice,os:res[0].operatingSystem,
        camera:res[0].Camera,Display:res[0].Display,RAM:res[0].RAM,specialFeat:res[0].specialFeat,Battery:res[0].Battery,
        FlipkartLink:res[0].FlipkartLink,SnapLink:res[0].SnapLink
     };
    var result="";
    localStorage.setItem("prod"+id, JSON.stringify(compareProd));
    console.log(localStorage);
    Object.keys(localStorage).forEach(function(key){
      if(key!="prod_id" && key!="compare_id"){
            val=JSON.parse(localStorage.getItem(key));
            console.log(key);
            result+= '<div class="ComapreTable table-dark col-sm-3 compare-section"><button class= " col-sm-6 btn btn-primary" type="submit" value="'+val.id+'" onclick="clearCompare(this.value)">Clear</button><table class="table table table-striped table-dark"><thead><tr><th class="PName" scope="col">'+val.Name+'</th> </tr></thead><tbody><tr><td>'+val.os+'</td></tr><tr><td>'+val.Display+'</td></tr><tr><td>'+val.camera+'</td></tr><tr><td>'+val.Battery+'</td></tr><tr><td>'+val.specialFeat+'</td></tr><tr><td>'+val.RAM+'</td></tr><tr><td><img src="https://www.91-img.com/sourceimg/1433150439.jpg" alt=""  class="ecom-logo"> <br><h5 class="font-italic">Rs.'+val.flipkartPrice+'</h5><button class="btn btn-primary"> Buy now </button></td></tr><tr><td><img src="https://www.91-img.com/sourceimg/1520940259.png" alt=""  class="ecom-logo"><br><h5 class="font-italic">Rs. 30000</h5><button class="btn btn-primary">Buy now </button> </td> </tr><tr><td><img src="https://images.yourstory.com/cs/wordpress/2016/09/snapdeal-new-logo.png?fm=png&auto=format" style="width:100px;height:50px" alt="" class="ecom-logo"><br><h5 class="font-italic">'+val.SnapPrice+'</h5><button class="btn btn-primary"> Buy now</button></td></tr></tbody></table></div>';
       }
     });
    document.getElementById("compareBrands").innerHTML=result;
 }
 
 function clearCompare(val){
     console.log("compare"+val);
    localStorage.removeItem("prod"+val);
    console.log(localStorage);
    var result="";
    Object.keys(localStorage).forEach(function(key){
        if(key!="prod_id" && key!="compare_id"){
              val=JSON.parse(localStorage.getItem(key));
              console.log(key);
              result+= '<div class="ComapreTable table-dark col-sm-3 compare-section"><button class= " col-sm-6 btn btn-primary" type="submit" value="'+val.id+'" onclick="clearCompare(this.value)">Clear</button><table class="table table table-striped table-dark"><thead><tr><th class="PName" scope="col">'+val.Name+'</th> </tr></thead><tbody><tr><td>'+val.os+'</td></tr><tr><td>'+val.Display+'</td></tr><tr><td>'+val.camera+'</td></tr><tr><td>'+val.Battery+'</td></tr><tr><td>'+val.specialFeat+'</td></tr><tr><td>'+val.RAM+'</td></tr><tr><td><img src="https://www.91-img.com/sourceimg/1433150439.jpg" alt="" style="width:100%; height:50%"> <br><h4 class="font-italic">Rs.'+val.flipkartPrice+'</h4><button class="btn btn-primary"> Buy now </button></td></tr><tr><td><img src="https://www.91-img.com/sourceimg/1520940259.png" alt="" style="width:100%; height:50%"><br><h4 class="font-italic">Rs. 30000</h4><button class="btn btn-primary">Buy now </button> </td> </tr><tr><td><img src="https://images.yourstory.com/cs/wordpress/2016/09/snapdeal-new-logo.png?fm=png&auto=format" style="width:100%; height:50%" alt=""><br><h4 class="font-italic">'+val.SnapPrice+'</h4><button class="btn btn-primary"> Buy now</button></td></tr></tbody></table></div>';
         }
  
       });
    document.getElementById("compareBrands").innerHTML=result;
    

     
 }
 