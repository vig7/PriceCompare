import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.WriteAbortedException;
import java.lang.ref.WeakReference;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.CheckedInputStream;

public class UpdateRam {
    void updateRam(){
        ArrayList<String> data=new ArrayList<String>();
        CrawlFlipk flipk=new CrawlFlipk();
        Jdbc jdbc=new Jdbc();
        AddDbFlipkart flip=new AddDbFlipkart();

        try {
            //File file = new File("PriceSpec.csv");
            CSVReader reader = new CSVReader(new FileReader("FinalSpec.csv"));
            String[] d=new String[3];
                int j=0;
                while (j<4321){
                    String[] name=reader.readNext();
                            j++;
                          String[] value=flipk.getPrice(name[0]);
                          if(!value[0].equals("-1")){
                              d[0]=name[0];
                              d[1]=value[1];
                              d[2]=value[2];
                          }
                          else {
                              d[0]=name[0];
                              d[1]=null;
                              d[2]=null;
                          }

                          flip.makeJDBCConnection();
                          java.util.Date date=new java.util.Date();
                          Timestamp Time =new Timestamp(date.getTime());
                          flip.addDataToDB(d[0],d[1],d[2],Time,false);
                }

            reader.close();
        } catch (Exception e) {

            System.out.println(e);
        }
    }
}