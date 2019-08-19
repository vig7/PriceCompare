import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;

public class AutoUpdatePaytm {
    void updatePaytm(){
        ArrayList<String> data=new ArrayList<String>();
        Jdbc jdbc=new Jdbc();
        AddDbPaytm paytm=new AddDbPaytm();
        CrawlPaytm paytmc=new CrawlPaytm();
        try {
            //File file = new File("PriceSpec.csv");
            CSVReader reader = new CSVReader(new FileReader("FinalSpec.csv"));
            String[] d=new String[3];
            int j=0;
            while (j<4321){
                String[] name=reader.readNext();
                j++;
                String[] value=paytmc.test(name[0]);
                if(!value[1].equals("-1")){
                    d[0]=name[0];
                    d[1]=value[1];
                    d[2]=value[2];
                }
                else {
                    d[0]=name[0];
                    d[1]=null;
                    d[2]=null;
                }

                paytm.makeJDBCConnection();
                java.util.Date date=new java.util.Date();
                Timestamp Time =new Timestamp(date.getTime());
                paytm.addDataToDB(d[0],d[1],d[2],Time);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
