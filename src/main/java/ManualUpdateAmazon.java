import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ManualUpdateAmazon {
    void update(){
        ArrayList<String> data=new ArrayList<String>();
        CrawlAmazon amazon=new CrawlAmazon();
        Jdbc jdbc=new Jdbc();
        AddDbFlipkart flip=new AddDbFlipkart();

        try {
            //File file = new File("PriceSpec.csv");

            flip.makeJDBCConnection();
            CSVReader reader = new CSVReader(new FileReader("FinalSpec.csv"));
            String[] d=new String[3];
            int j=0;
            int k=0;
            while (j<4321){
                String[] name=reader.readNext();
                j++;
                String[] value=amazon.add(name[0]);
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

                java.util.Date date=new java.util.Date();
                Timestamp Time =new Timestamp(date.getTime());
                flip.addDataToDB(d[0],d[1],d[2],Time,false);


            }
            flip.close();
            reader.close();
        } catch (Exception e) {
            flip.close();
            System.out.println(e);
        }
    }
}
