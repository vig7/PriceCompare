import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlFlipk {
    String[] getPrice(String product){
        String[] value= new String[4];
        ValidateName validate=new ValidateName();
        try {
            String url="https://www.flipkart.com/mobiles/pr?sid=tyy%2C4io&q="+product;
            Document document = Jsoup.connect(url).get();
            Elements look=document.select("div._1UoZlX");
            //Elements stock=document.select("img._1Nyybr");
            int i=0;
            for (Element l:look) {
                Elements links=l.select("div._1vC4OE._2rQ-NK");
                Elements n=l.select("div._3wU53n");
                String a="flipkart.com"+l.select("a._31qSD5").attr("href");
                value[1]=links.text();
                value[0]=n.text();
                value[2]=a;
                i++;
                if(validate.check(n.text(),product)){
                    return value;
                }
                if(i>3){

                    value[0]="-1";
                    return value;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(product);
        }
        value[0]="-1";
        return value;
    }

}
