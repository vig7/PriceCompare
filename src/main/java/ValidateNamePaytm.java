import java.util.Arrays;
import java.util.List;

public class ValidateNamePaytm {

    boolean check(String websiteName,String databseName){

        databseName=databseName.toLowerCase();

        databseName=databseName.replaceAll("ram","");

        databseName=databseName.replaceAll("gb"," gb");
        websiteName=websiteName.toLowerCase();
        String[] temp=websiteName.split(" ");
        List<String> webbroken = Arrays.asList(temp);
        String nickname = databseName.substring(databseName.indexOf(' ')+1);
        String[] brokenD=databseName.split(" " );

        String[] brokenN=nickname.split(" " );
        databseName=databseName.replaceAll("\\s","");
        nickname =nickname.replaceAll("\\s","").toLowerCase();

        int val=0;
        for (int i=0;i<brokenD.length;i++){
            if(webbroken.indexOf(brokenD[i])==-1) {
                val++;
                break;
            }
        }
        if(!brokenD[0].equals("oneplus")){
        for (int i=0;i<brokenN.length;i++){
            if(webbroken.indexOf(brokenN[i])==-1)
                val++;
        }
        }
        else val++;
        if (val<2)
        return true;
        else
            return false;
    }

    public static void main(String[] args) {
        ValidateNamePaytm paytm=new ValidateNamePaytm();
        System.out.println( paytm.check("Redmi Note 7 Pro 4 GB 64 GB Space Black","OnePlus 7"));
    }
}
