
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class GetPrice {
    public static  void main(String args[]){
        String html="";
        String webPage = "";
    try {
        File file = new File("PriceSpec.csv");
        FileWriter outputfile = new FileWriter("PriceSpec.csv");
        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputfile);

        // adding header to csv

        // add data to csv
        //String[] data1 = { "Aman", "10", "620" };
        //writer.writeNext(data1);
        //String[] data2 = { "Suraj", "10", "630" };
        //writer.writeNext(data2);

        // closing writer connection



        String url = "https://www.gsmarena.com/samsung_galaxy_note10+_5g-9787.php";

        Document document = Jsoup.connect(url).get();
        Elements links = document.select("td.nfo");

        Elements linkq = document.select("td.ttl");
        ArrayList<String> b=new ArrayList<String>();
        for (Element link : linkq) {
            if(link.text()!="")
                b.add(link.text());
        }

        String[] header=GetStringArray(b);

        writer.writeNext(header);
        b.clear();
        for (Element link : links) {
            if(link.text()!="")
            b.add(link.text());
        }
        header=GetStringArray(b);

        writer.writeNext(header);
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
