import java.util.ArrayList;
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
        if(brokenD[0]!="oneplus"){
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
}
