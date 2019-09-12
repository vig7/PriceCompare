public class AutoUpdateFlipkart extends  Thread {
    String name;
        public  AutoUpdateFlipkart(String name){
            this.name=name;
        }
     public void run(){
        try {
            System.out.println(10);
            AddDbFlipkart flipk = new AddDbFlipkart();
            flipk.makeJDBCConnection();
            flipkartinsert insert = new flipkartinsert();
            insert.hit(name);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
