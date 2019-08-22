import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Date;

public class AddStockFlipk {
boolean test(String link){
    int i=0;
    boolean stock=false;
        if(!link.equals("null")){
            try {
                System.out.println(link);
                Document document = Jsoup.connect("https://www."+link).get();
                String look=document.select("div._9-sL7L").text();
                Date date=new Date();
                System.out.println(i++);
                if(look.length()!=0){
                    stock=true;
                }
                else {
                    stock=false;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

    }
    return stock;
}

}
