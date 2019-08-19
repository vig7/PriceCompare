import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class AddStockFlipk {
void test(){
    AddDbFlipkart flip=new AddDbFlipkart();
    flip.makeJDBCConnection();
    int i=0;
    ArrayList<String> links=flip.getDataFromDB();
    ArrayList<String> stock=flip.getStock();
    System.out.println(stock);
    for (int j=0;j<links.size();j++){
        if(!links.get(j).equals("null")){
            try {
                System.out.println(links.get(j));
                Document document = Jsoup.connect("https://www."+links.get(j)).get();
                String look=document.select("div._9-sL7L").text();
                Date date=new Date();
                System.out.println(i++);
                if(look.length()==0){
                }
                else {
                    flip.addDataToDB("","",links.get(j),date,false);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    flip.close();

}

}
