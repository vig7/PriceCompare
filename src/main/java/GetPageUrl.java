public class GetPageUrl {
    public static void main(String args[]) {
        PriceBaba get=new PriceBaba();
    try {
//        for(int i=104;i<=108;i++) {
//            Document document = Jsoup.connect("https://pricebaba.com/mobile/pricelist/all-mobiles-sold-in-india?page="+i).get();
//            Elements links = document.select("a[href]");
//            for (Element l : links) {
//                try {
//                    if (l.attr("href").substring(0, 28).equals("https://pricebaba.com/mobile"))
//                        get.get(l.attr("href").toString());
//                } catch (Exception e) {
//                    continue;
//                }
//            }
//        }
        AmazonProductDetails cs=new AmazonProductDetails();
        cs.getAmazonPrice();
    }
    catch (Exception e){
        System.out.println(e);
    }
    }
}