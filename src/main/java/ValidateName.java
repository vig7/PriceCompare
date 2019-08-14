import java.util.Date;

public class ValidateName {
    boolean check(String productName,String databseName){
        productName=productName.toLowerCase();
        databseName=databseName.toLowerCase();
        String[] value=productName.split("\\(");
        String temp="";
        for(int i=1;i<value.length;i++){

            temp+=value[i];
        }
        if(databseName.length()<=productName.length())
        if(value[0].equals(databseName.substring(0,value[0].length()))){
            if(temp.replaceAll("\\s+","").contains(databseName.substring(value[0].length()).replaceAll("\\s+","")))
                return true;
        }

        return false;
    }
}
