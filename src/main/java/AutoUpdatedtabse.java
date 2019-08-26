
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static org.apache.commons.validator.GenericValidator.isInt;


public class AutoUpdatedtabse {
    void hit() {
            //CrawlSnap snap=new CrawlSnap();
            //snap.test();
            //CrawlFlipk flipk=new CrawlFlipk();

/*            CrawlPaytm paytm=new CrawlPaytm();
           String[] temp= paytm.test("IPHONE 64 GB");
           System.out.println(temp[0]+" "+temp[2]);

      AutoUpdatePaytm paytm =new AutoUpdatePaytm();
      paytm.updatePaytm();*/
        //AddStockFlipk flipk=new AddStockFlipk();
        //flipk.test();

            //CrawlPaytm paytm=new CrawlPaytm();
        //paytm.test();
       /* AutoUpdateFlipkart flipkart=new AutoUpdateFlipkart();
        flipkart.check("Itel it2182");*/
        try {

            Document page = Jsoup.connect("https://pricebaba.com/mobile/pricelist/all-mobiles-sold-in-india?page=").get();
            Elements lastpg=page.select("ul.pgntn-lnk");
            int last=getLastPage(lastpg);
            int middle=last/2;
            if(middle<54)
                middle=54;
            PriceBaba priceBaba=new PriceBaba();
            AddDbFlipkart db=new AddDbFlipkart();
            db.makeJDBCConnection();
            for(int i=1;i<=middle;i++) {
                Document document = Jsoup.connect("https://pricebaba.com/mobile/pricelist/all-mobiles-sold-in-india?page="+i).get();
              //  Elements links = document.select("img.img__s");

                Elements nameList=document.select("a.productSKULink.ellips-line");

                System.out.println(i);
                for (Element name:nameList) {
                    try {/*
                        String img = l.attr("alt").substring(0, l.attr("alt").length() - 15).trim();
                        FileOutputStream out = (new FileOutputStream(new java.io.File("ImageStore/" + img + ".png")));
                        Connection.Response b = Jsoup.connect(l.attr("data-src"))
                                .ignoreContentType(true).execute();
                        out.write(b.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
                        out.close();*/
                        if(db.checkIfExist(name.text())){
                            priceBaba.get(name.attr("href"));
                        }


                    } catch (Exception e) {
                        System.out.println("error" + i + " " + e);
                        continue;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
   }
   int getLastPage(Elements allpages){
        int max=0;
        try{
            for(Element last:allpages){
                if(isInt(last.text())){
                    if(Integer.parseInt(last.text())>max)
                        max=Integer.parseInt(last.text());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return max;
   }
}
