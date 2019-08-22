import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ManualUpdateflip {
    void update(){
        ArrayList<String> data=new ArrayList<String>();
        CrawlFlipk flipk=new CrawlFlipk();
        Jdbc jdbc=new Jdbc();
        AddDbFlipkart flip=new AddDbFlipkart();

        try {
            //File file = new File("PriceSpec.csv");
            CSVReader reader = new CSVReader(new FileReader("FinalSpec.csv"));
            String[] d=new String[3];
                int j=0;
                int k=0;
                while (j<4321){

                    String[] name=reader.readNext();
                    if (k<1){
                        if(name[0].equals("Jivi X606 Plus"))
                            k=2;
                        else
                            continue;
                    }
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