import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class PriceBaba {
    void get(String pageurl){
        String html="";
        String webPage = "";
        try {
            //File file = new File("PriceSpec.csv");
            String csv = "PriceSpec.csv";
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true));

            // adding header to csv

            // add data to csv
            //String[] data1 = { "Aman", "10", "620" };
            //writer.writeNext(data1);
            //String[] data2 = { "Suraj", "10", "630" };
            //writer.writeNext(data2);

            // closing writer connection
            ArrayList<String> checker = new ArrayList<String>(
                    Arrays.asList("Dimensions","Weight","Build","SIM","Type","Size","Resolution","Protection","OS","Chipset","CPU","GPU","Card slot","Internal","Features","Video","Single","Features","Video","Loudspeaker","3.5mm jack","WLAN","Bluetooth","GPS","NFC","Radio","USB","Sensors","Charging","Colors","Models","Price","Performance","Camera","Loudspeaker","Audio quality","Battery life"));
            String[] header ={"PhoneName","Dimensions","Weight","Build","SIM","Type","Size","Resolution","Protection","OS","Chipset","CPU","GPU","Card slot","Internal","Features","Video","Single","Features","Video","Loudspeaker","3.5mm jack","WLAN","Bluetooth","GPS","NFC","Radio","USB","Sensors","Charging","Colors","Models","Price","Performance","Camera","Loudspeaker","Audio quality","Battery life"};

            //String[] header;
            ArrayList<String> b=new ArrayList<String>();
           // writer.writeNext(header);
            Document document = Jsoup.connect(pageurl).get();

            Elements title = document.select("h1.specs-phone-name-title");
            b.add(title.text());
            Elements lin1 = document.select("tr");
            int temp=0;
            for(Element link:lin1) {
                Elements ttl = link.select("td.ttl");
                Elements nfo = link.select("td.nfo");
                while (true) {
                    if (ttl.text().equals(checker.get(temp))) {
                        b.add(nfo.text());
                        break;
                    }
                    else if(checker.indexOf(ttl.text())==-1)
                        break;
                    else
                        b.add(null);
                    temp++;
                }
            }
            System.out.println(b);
            String[] data =GetStringArray(b);
            writer.writeNext(data);
            writer.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public static String[] GetStringArray(ArrayList<String> arr)
    {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // Convert ArrayList to object array
        Object[] objArr = arr.toArray();

        // Iterating and converting to String
        int i = 0;
        for (Object obj : objArr) {
            str[i++] = (String)obj;
        }

        return str;
    }

}
