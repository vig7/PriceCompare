var phone_id,phone_name;
var res;
var domain = "172.16.172.28:4567";
console.log(localStorage);

//finding minimum price
function findMinPrice(flipPrice,snapPrice,amazonPrice,paytmprice){
    console.log(flipPrice+snapPrice+amazonPrice+paytmprice);
    var price="";
    console.log(flipPrice+" "+snapPrice+" "+amazonPrice+" "+paytmprice+" "+price);
    if(flipPrice=="0"||flipPrice==undefined||flipPrice=="null"||flipPrice=="")
    flipPrice="1000000";
    else
    flipPrice=parseFloat(flipPrice.replace(',',''));
    if(snapPrice=="0"||snapPrice==undefined||snapPrice=="null"||snapPrice=="")
    snapPrice="1000000";
    else
    snapPrice=parseFloat(snapPrice.replace(',',''));
    if(paytmprice=="0"||paytmprice==undefined||snapPrice=="null"||snapPrice=="")
    paytmprice="1000000";
    else
    paytmprice=parseFloat(paytmprice.replace(',',''));
    if(amazonPrice=="0"||amazonPrice==undefined||amazonPrice=="null"||amazonPrice=="")
    amazonPrice="1000000";
    else
    amazonPrice=parseFloat(amazonPrice.replace(',',''));
    price+=Math.min(flipPrice,snapPrice,amazonPrice,paytmprice);
    return price;
}

//live search
function dynamicSearch(str){
    console.log(str);
    if (str.length==0) { 
        document.getElementById("livesearch").innerHTML="";
        document.getElementById("livesearch").style.border="0px";
        return;
      }
      if (window.XMLHttpRequest) {
        xmlhttp=new XMLHttpRequest();
      } else {  
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
      xmlhttp.onreadystatechange=function() {
        if (this.readyState==4 && this.status==200) {
            console.log(this);
            searchResults(this);
        }
      }
      xmlhttp.open("GET","http://172.16.172.28:4567/SearchResults?searchKey="+str,true);
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
            text+='<div class="highlight"><li><ul><h5 id="clickRes" style="display:block;border:1px #A5ACB2" onclick="setSearchVal('+"'"+name+"',"+"'"+ responseSearch[i].id+"'"+')">'+name+'</h5></ul></li></div>';
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
    if (x.style.display === "none" || v==null) {
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
    let flipPrice;
    let snapPrice;
    let amazonPrice;
    let paytmprice;
    if(Object.keys(res).length==4){
        flipPrice=res[0];
        snapPrice=res[1];
        amazonPrice=res[2];
        paytmprice=res[3];

    }
    else{
        flipPrice=res[0].flipkartPrice;
        snapPrice=res[0].SnapPrice;
        amazonPrice=res[0].AmazonPrice;
        paytmprice=res[0].PaytmPrice;
    }
     
    if(amazonPrice!="0" ){
        prices[0]=amazonPrice;
        links[0]=localStorage.getItem("AmazonLink");
    }
    else{
        prices[0]="Not Available";
    }
    if(flipPrice!="0"){
        prices[1]=flipPrice;
        links[1]=localStorage.getItem("FlipkartLink");
    }
    else{
        prices[1]="Not Available";
    }
    if(snapPrice!="0" ){
        prices[2]=snapPrice;
        links[2]=localStorage.getItem("SnapLink");
    }
    else{
        prices[2]="Not Available";
    }
    if(paytmprice!="0"){
        prices[3]=paytmprice;
        links[3]=localStorage.getItem("PaytmLink");
    }
    else{
        prices[3]="Not Available";
    }
      for(i=0;i<deals.length;i++){
         text+="<p>"+deals[i]+"</p><br>";
         price+="<p>"+prices[i]+"</p><br>";
         if(prices[i]!="Not Available"){
                button+='<a href="'+links[i]+'" class="btn btn-primary " style="margin-bottom:10px;">'+'Go to site'+'</a><br>';
        }
        else{
            button+='<a href="'+links[i]+'" class="btn btn-primary "  disabled style="margin-bottom:10px;">'+'Go to site'+'</a><br>';
        }
    }
      document.getElementById("show-deals").innerHTML=text;
      document.getElementById("show-price").innerHTML=price;
      document.getElementById("show-button").innerHTML=button;
  }

function callFeedback(proid){
  
        var email=document.getElementById("usr").value;
        alert(email)
        var comment=document.getElementById("comment").value;
        var ratings=document.getElementById("ratings").value;
        var id=proid;
        console.log(email+" "+comment+" "+ratings+" "+id);
        var XHR = new XMLHttpRequest();
        var FD  = new FormData();
        // var data=
        var data={proid:proid,email:email, comment:comment,ratings:ratings};
        console.log(data);
        for(name in data) {
            console.log(name+" "+data[name]);
            FD.append(name, data[name]);
            
        }
        console.log(FD.getAll);

  // Define what happens on successful data submission
        XHR.addEventListener('load', function(event) {
            alert('Yeah! Data sent and response loaded.');
        });

  // Define what happens in case of error
        XHR.addEventListener('error', function(event) {
            alert('Oops! Something went wrong.');
        });

  // Set up our request
   XHR.open('POST', 'http://172.16.172.28:4567/feedback');

  // Send our FormData object; HTTP headers are set automatically
    XHR.send(FD);
}

 //Adding featured mobiles
 function show() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            addFeatures(this); 
        }
    };
    xmlhttp.open("GET", "http://172.16.172.28:4567/FeaturedPhones", true);
    xmlhttp.send();
 }

function addFeatures(obj){
    var text=" ";
    res= obj.response;
    res=JSON.parse(res);
    console.log(res);
    var price;
    console.log(res)
    for(i=0;i<8;i++){
       let flipPrice=res[i].flipkartPrice;
       let snapPrice=res[i].SnapPrice;
       let amazonPrice=res[i].AmazonPrice;
       let paytmprice=res[i].PaytmPrice;
        price="Rs." 
        price+=findMinPrice(flipPrice,snapPrice,amazonPrice,paytmprice);
        if(price=="Rs.1000000")
           price="Not Available";
       text+='<div class="col-sm-3 fix-sides"><div class="product-image-wrapper"><div class="single-products"><div class="card text-center"><img class="pop-up" style="height:200px" src="ImageStore/'+res[i].Name+'.PNG" /><h5 class="card-title set-font">'+price+'</h5><p class="card-text">'+res[i].Name+'</p><button class="btn btn-default add-to-cart" value ="'+res[i].id+'"  onclick="loadDetails(this.value)"><i class="fa fa-shopping"></i>See details</button></div>  </div></div></div>';
    }
    document.getElementById("fix-features").innerHTML=text;
 }
 
 //loading details page
 function loadDetails(val){
    localStorage.setItem("prod_id", val);
    window.location.href="productDescription.html"
}
function showInitialPage(){
    var url="";
    phone_id=localStorage.getItem("prod_id");
    var result;
    url= "http://172.16.172.28:4567/MobileSpecs?id="+phone_id;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            showSpecifications(this); 
            
        }
    };
    xmlhttp.open("GET",url, true);
    xmlhttp.send();
   
}
 //fetching and showing specific mobile phone specs
 function showMobilePage() {
    var url="";
    phone_id=localStorage.getItem("prod_id");
    
    url= "http://172.16.172.28:4567/updatedSpecs?id="+phone_id;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            showupdatedSpecifications(this); 
            
        }
    };
    xmlhttp.open("GET",url, true);
    xmlhttp.send();
    
 }

 function showSearchResultPage(val){

    // var url="";
    // url="http://172.16.172.28:4567/SearchSpecificResults?searchKey="+val;
    document.getElementById("livesearch").innerHTML="";
    document.getElementById("livesearch").style.border="0px";
    // var xmlhttp = new XMLHttpRequest();
    // xmlhttp.onreadystatechange = function() {
    //     if (this.readyState == 4 && this.status == 200) {
    //         console.log(Object.keys(JSON.parse(this.response)).length);
            // if(Object.keys(JSON.parse(this.response)).length==1){
            //     var res1=JSON.parse(this.response);
            //     console.log("in"+res1);
            //     loadDetails(res1[0].id);
            // }
            // else 
                showAllRelevantResults(val);
            //  }
    // };
    // xmlhttp.open("GET",url, true);
    // xmlhttp.send();
 }

 function  showAllRelevantResults(val){
    var url="";
    url="http://172.16.172.28:4567/SimilarPhones?searchKey="+val;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {      
                showSpecsCard(this,val); 
             }
    };
    xmlhttp.open("GET",url, true);
    xmlhttp.send();
 }

 function showSpecsCard(obj,val){
    var text=" ";
    res= obj.response;
    res=JSON.parse(res);
    console.log(res);
    if(Object.keys(res).length==0){
        console.log(res);
        document.getElementById("search-result").innerHTML='<div class="container"><p><b>Nothing relevant could be found!!!!</b></p></div>';
        return;
    }
    var price;
    for(i=0;i<Object.keys(res).length;i++){
       let flipPrice=res[i].flipkartPrice;
       let snapPrice=res[i].SnapPrice;
       let amazonPrice=res[i].AmazonPrice;
       let paytmprice=res[i].PaytmPrice;
        price="Rs." 
        price+=findMinPrice(flipPrice,snapPrice,amazonPrice,paytmprice);
        if(price=="Rs.1000000")
           price="Not Available";
            imglink="ImageStore/google-pixel.jpg";
        text+='<div class="col-sm-4 fix-sides"><div class="product-image-wrapper" style="border:1px red""><div class="single-products"><div class="card text-center"><img class="pop-up" style="height:200px" src="ImageStore/'+res[i].Name+'.PNG" onerror="this.onerror=null; this.src=\'ImageStore/google-pixel.jpg\'" /><h5 class="card-title set-font">'+price+'</h5><p class="card-text">'+res[i].Name+'</p><button class="btn btn-default add-to-cart" value ="'+res[i].id+'"  onclick="loadDetails(this.value)"><i class="fa fa-shopping"></i>See details</button></div>  </div></div></div>';
    }
     document.getElementById("search-result").innerHTML=text;
   //  document.getElementById("listingTable").innerHTML='<a href="javascript:prevPage()" id="btn_prev">Prev</a><a href="javascript:nextPage()" id="btn_next">Next</a>page: <span id="page"></span>';
}

 

function showSpecifications(obj,val){
    var specsObj= obj.response;
    res=JSON.parse(specsObj);
    console.log("in"+res);
    if(Object.keys(res[0]).length==0){
        console.log(res);
        document.getElementById("search-result").innerHTML='<div class="container"><p><b>Nothing relevant could be found!!!!</b></p></div>';
        return;
    }      
    var text=" ";
    if(!val)
        var name=res[0].Name;
    else
        var name=val;
    var price="Best Price:Rs.";
    let flipPrice=res[0].flipkartPrice;
    let snapPrice=res[0].flipkartPrice;
    let amazonPrice=res[0].AmazonPrice;
    let paytmprice=res[0].PaytmPrice;
    price+=findMinPrice(flipPrice,snapPrice,amazonPrice,paytmprice)
    console.log(price);
    if(price=="Best Price:Rs.1000000")
        price="Not Available"; 
    localStorage.setItem("price",price);
    localStorage.setItem("name",name);
    localStorage.setItem("os",res[0].operatingSystem);
    localStorage.setItem("camera",res[0].Camera);
    localStorage.setItem("battery",res[0].Battery);
    localStorage.setItem("display",res[0].Display);
    localStorage.setItem("special_feat",res[0].specialFeat);
    localStorage.setItem("RAM",res[0].RAM);
    localStorage.setItem("AmazonLink",res[0].AmazonLink);
    localStorage.setItem("FlipkartLink",res[0].FlipkartLink);
    localStorage.setItem("SnapLink",res[0].SnapLink);
    localStorage.setItem("PaytmLink",res[0].PaytmLink);
    image='<div class="col-sm-6"><img class="pop-up "  id="description-image-size"  src="ImageStore/'+name+'.PNG" onerror="this.onerror=null; this.src=\'ImageStore/google-pixel.jpg\'"  alt="" /></div>';   
    text=' <table class="table table-bordered"> <tbody> <tr style="border: none"><td >Model Name</td><td>'+res[0].Name+'</td></tr><tr><td>Operating System</td><td>'+localStorage.getItem("os")+'</td></tr><tr><td>Camera</td><td>'+localStorage.getItem("camera")+'</td></tr><tr><td>Display</td><td>'+localStorage.getItem("display")+'</td></tr><tr><td>Battery</td><td>'+localStorage.getItem("battery")+'</td></tr><tr><td>Special Features</td><td>'+localStorage.getItem("special_feat")+'</td></tr><tr><td>RAM</td><td>'+localStorage.getItem("RAM")+'</td></tr></tbody></table>';
    buttonshow='<button type="submit" class="btn btn-primary" onclick="callFeedback('+"'"+localStorage.getItem("prod_id")+"'"+')">Submit</button>';
    document.getElementById("prodName").innerHTML=name;
    document.getElementById("prodPrice").innerHTML=price;
    document.getElementById("showSpecifications").innerHTML=text;
    document.getElementById("image-pro").innerHTML=image;
    document.getElementById("feedback-button").innerHTML=buttonshow;
    var butvalue=localStorage.getItem("prod_id");
    phone_name=name;
    document.getElementById("show-compare").innerHTML='<div class="row" style="padding:20px"><div class="col-sm-3"><button class="btn btn-primary" onclick="obtainSpecs('+"'"+butvalue+"'"+')">Compare</button></div></div>';
    console.log(res);
    if(Object.keys(res).length==1)
        return;
    var text11="";
    document.getElementById("similar-prod").innerHTML="Similar Products";
    for(i=1;i<Object.keys(res).length;i++){
        var minprice="Best Price:Rs.";
        let flipPrice=res[i].flipkartPrice;
        let snapPrice=res[i].flipkartPrice;
        let amazonPrice=res[i].AmazonPrice;
        let paytmprice=res[i].PaytmPrice;
        console.log(flipPrice+" "+snapPrice+" "+amazonPrice+" "+paytmprice);
        minprice+=findMinPrice(flipPrice,snapPrice,amazonPrice,paytmprice)
        if(minprice=="Best Price:Rs.1000000")
           minprice="Not Available"; 
           text11+='<div class="col-sm-3 fix-sides"><div class="product-image-wrapper"><div class="single-products"><div class="card text-center"><img class="pop-up" style="height:200px" src="ImageStore/'+res[i].Name+'.PNG" onerror="this.onerror=null; this.src=\'ImageStore/google-pixel.jpg\'" /><h5 class="card-title set-font">'+minprice+'</h5><p class="card-text">'+res[i].Name+'</p><button class="btn btn-default add-to-cart" value ="'+res[i].id+'"  onclick="loadDetails(this.value)"><i class="fa fa-shopping"></i>See details</button></div>  </div></div></div>';

   }   
  document.getElementById("show-rec").innerHTML=text11;
  console.log(minprice)
// showMobilePage();
}

function showupdatedSpecifications(obj,val){
    var specsObj= obj.response;
    res=JSON.parse(specsObj);
    console.log(res);
    if(Object.keys(res).length==0){
        return;
    }      
    var price="Best Price:Rs.";
    let flipPrice=res[0];
    let snapPrice=res[1];
    let amazonPrice=res[2];
    let paytmprice=res[3];
    console.log(flipPrice+" "+snapPrice+" "+amazonPrice+" "+paytmprice);
    price+=findMinPrice(flipPrice,snapPrice,amazonPrice,paytmprice)
    console.log(price);
    if(price=="Best Price:Rs.1000000")
        price="Not Available"; 
    document.getElementById("prodPrice").innerHTML=price;
    // alert("New price available");
}

 function obtainSpecs(id) {
    phone_id=localStorage.setItem("compare_id",id);
    window.location.href="comparePrices.html" 
 }

 function sendRequest(){
    var url="";
    phone_id=localStorage.getItem("compare_id");
    url= "http://172.16.172.28:4567/MobileSpecs?id="+phone_id;
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
     let flipPrice=res[0].flipkartPrice;
     let snapPrice=res[0].flipkartPrice;
     let amazonPrice=res[0].AmazonPrice;
     let paytmprice=res[0].PaytmPrice;
     console.log(flipPrice+" "+snapPrice+" "+amazonPrice+" "+paytmprice);
     if(flipPrice=="0")
        flipPrice="Not available";
    else
      flipPrice="Rs."+parseFloat(flipPrice.replace(',',''));
    if(snapPrice=="0")
        snapPrice="Not available";
    else
        snapPrice="Rs."+parseFloat(snapPrice.replace(',',''));
    if(paytmprice=="0")
        paytmprice="Not available";
    else
        paytmprice="Rs."+parseFloat(paytmprice.replace(',',''));
    if(amazonPrice=="0")
        amazonPrice="Not available";
    else
        amazonPrice="Rs."+parseFloat(amazonPrice.replace(/\,/g,""));
     compareProd={id:res[0].id,Name:res[0].Name,flipkartPrice:flipPrice,
        SnapPrice:snapPrice,os:res[0].operatingSystem,
        camera:res[0].Camera,Display:res[0].Display,RAM:res[0].RAM,specialFeat:res[0].specialFeat,Battery:res[0].Battery,
        FlipkartLink:res[0].FlipkartLink,SnapLink:res[0].SnapLink,paytmPrice:paytmprice,amazonPrice:amazonPrice
     };
    var result="";
    localStorage.setItem("prod"+id, JSON.stringify(compareProd));
    Object.keys(localStorage).forEach(function(key){
        if(key!="AmazonLink" && key!="FlipkartLink" && key!="SnapLink" && key!="PaytmLink" && key!="prod_id" && key!="compare_id" && key!="os" && key!="display" && key!="RAM" && key!="battery" && key!="special_feat" && key!="name" && key!="price" && key!="camera"){
            val=JSON.parse(localStorage.getItem(key));
            console.log(key);
            result+= '<div class="ComapreTable table-dark col-sm-3 compare-section"><button class= " col-sm-6 btn btn-primary" id ="compare-button" type="submit" value="'+val.id+'" onclick="clearCompare(this.value)">Clear</button><table class="table table table-striped table-dark"><thead><tr><th class="PName" scope="col">'+val.Name+'</th> </tr></thead><tbody><tr><td>'+val.os+'</td></tr><tr><td>'+val.Display+'</td></tr><tr><td>'+val.camera+'</td></tr><tr><td>'+val.Battery+'</td></tr><tr><td>'+val.specialFeat+'</td></tr><tr><td>'+val.RAM+'</td></tr><tr><td> <br><h5 class="font-italic set-font" >Flipkart:'+val.flipkartPrice+'</h5></td></tr><tr><td> <br><h5 class="font-italic set-font" >Amazon :'+val.amazonPrice+'</h5></td></tr><tr><td><h5 class="font-italic set-font">Paytm Mall :'+val.paytmPrice+'</h5> </td> </tr><tr><td><br><h5 class="font-italic set-font">SnapDeal:'+val.SnapPrice+'</h5></td></tr></tbody></table></div>';
       }
     });
    document.getElementById("compareBrands").innerHTML=result;
 }

 function clearCompare(val){
     if(localStorage.length==10){
         document.getElementById("compareBrands").innerHTML="Nothing to compare";
         return;
     }
    localStorage.removeItem("prod"+val);
    console.log(localStorage);
    var result="";
    Object.keys(localStorage).forEach(function(key){
        if(key!="AmazonLink" && key!="FlipkartLink" && key!="SnapLink" && key!="PaytmLink" && key!="prod_id" && key!="compare_id" && key!="os" && key!="display" && key!="RAM" && key!="battery" && key!="special_feat" && key!="name" && key!="price" && key!="camera"){
            val=JSON.parse(localStorage.getItem(key));
              console.log(key);
              result+= '<div class="ComapreTable table-dark col-sm-3 compare-section"><button class= " col-sm-6 btn btn-primary" type="submit" value="'+val.id+'" onclick="clearCompare(this.value)">Clear</button><table class="table table table-striped table-dark"><thead><tr><th class="PName" scope="col">'+val.Name+'</th> </tr></thead><tbody><tr><td>'+val.os+'</td></tr><tr><td>'+val.Display+'</td></tr><tr><td>'+val.camera+'</td></tr><tr><td>'+val.Battery+'</td></tr><tr><td>'+val.specialFeat+'</td></tr><tr><td>'+val.RAM+'</td></tr><tr><td> <br><h5 class="font-italic set-font" >Flipkart:'+val.flipkartPrice+'</h5></td></tr><tr><td> <br><h5 class="font-italic set-font" >Amazon :'+val.amazonPrice+'</h5></td></tr><tr><td><h5 class="font-italic set-font">Paytm Mall :'+val.paytmPrice+'</h5> </td> </tr><tr><td><br><h5 class="font-italic set-font">SnapDeal:'+val.SnapPrice+'</h5></td></tr></tbody></table></div>';
            }
       });
    document.getElementById("compareBrands").innerHTML=result;     

 }
 