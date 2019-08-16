import java.util.Date;

public class ValidateName {
    boolean check(String websiteName,String databseName){
        String nickname = databseName.substring(databseName.indexOf(' ')+1);
        databseName=databseName.replaceAll("\\s","");
        websiteName=websiteName.replaceAll("\\s","");
        nickname =nickname.replaceAll("\\s","").toLowerCase();
        websiteName=websiteName.toLowerCase();
        databseName=databseName.toLowerCase();
        String[] value=websiteName.split("\\(");
        String restOfval="";
        for(int i=1;i<value.length;i++){
            restOfval+=value[i];
        }

        if(databseName.length()<=websiteName.length()&&databseName.contains(value[0].trim())) {
            String restofData=databseName.substring(value[0].length(),databseName.length());
            if(restofData.length()==0){
                if(value[0].trim().equals(databseName.trim())||value[0].trim().equals(nickname.trim()))
                    return true;
            }
            else if (value[0].equals(databseName.substring(0, value[0].length()))) {
                if(restOfval.replaceAll("\\s","").contains(databseName))
                    return true;
            }
        }
        return false;
    }
}
