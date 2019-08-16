
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.ws.Response;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GetPageUrl {
    public static void main(String args[]) {
            //CrawlSnap snap=new CrawlSnap();
            //snap.test();
            //CrawlFlipk flipk=new CrawlFlipk();
            //CrawlPaytm paytm=new CrawlPaytm();
            //paytm.test();
       UpdateRam ram=new UpdateRam();
       ram.updateRam();
        try {
            for(int i=1;i<=0;i++) {
                Document document = Jsoup.connect("https://pricebaba.com/mobile/pricelist/all-mobiles-sold-in-india?page="+i).get();
                Elements links = document.select("img.img__s");

                System.out.println(i);
                for (Element l : links) {
                    try {/*
                        String img = l.attr("alt").substring(0, l.attr("alt").length() - 15).trim();
                        FileOutputStream out = (new FileOutputStream(new java.io.File("ImageStore/" + img + ".png")));
                        Connection.Response b = Jsoup.connect(l.attr("data-src"))
                                .ignoreContentType(true).execute();
                        out.write(b.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
                        out.close();*/
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
}