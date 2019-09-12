public class AutoUpdatePaytm extends  Thread {
    String name;
    public  AutoUpdatePaytm(String name){
        this.name=name;
    }

    public  void run(){
        try {

            System.out.println(10);
            AddDbPaytm paytm = new AddDbPaytm();
            String data = paytm.getTimestamp(name);
            java.util.Date date = new java.util.Date();
            PaytmInsert update = new PaytmInsert();
            update.hit(name);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
