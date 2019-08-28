public class ValidateName {
    static boolean check(String websiteName,String databseName){

        String nickname = databseName.substring(databseName.indexOf(' ')+1);
//        String nickname2 = nickname.substring(nickname.indexOf(' ')+1);
//        String finalnickname=databseName.substring(0,databseName.indexOf(' '))+" "+nickname2;
        databseName=databseName.replaceAll("%20","");
        databseName=databseName.replaceAll(" ","");
        websiteName=websiteName.replaceAll("\\s","");
        nickname =nickname.replaceAll("%20","").toLowerCase();
        nickname =nickname.replaceAll(" ","").toLowerCase();
//        finalnickname =finalnickname.replaceAll("\\s","").toLowerCase();
        websiteName=websiteName.toLowerCase();
        databseName=databseName.toLowerCase();
        String[] value=websiteName.split("\\(");
        String restOfval="";
        for(int i=1;i<value.length;i++){
            restOfval+=value[i];
        }
        if(databseName.length()<=websiteName.length()&&(databseName.contains(value[0].trim()) || nickname.contains(value[0].trim()))) {
            String restofData=databseName.substring(value[0].length());
            String restofnick;
            try {
                restofnick = nickname.substring(value[0].length());
            }
            catch (Exception e){
                restofnick="-1";
            }
            if(restofData.length()==0){
                if(value[0].trim().equals(databseName.trim())||value[0].trim().equals(nickname.trim()))
                    return true;
            }
            else if (value[0].equals(databseName.substring(0, value[0].length()))) {
                if(restOfval.replaceAll("\\s","").contains(restofData))
                    return true;
            }

            if(restofnick.length()==0&&!restofnick.equals("-1")){

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
    static boolean check1(String websiteName,String databseName){
        String nickname = databseName.substring(databseName.indexOf(' ')+1);
//        String nickname2 = nickname.substring(nickname.indexOf(' ')+1);
//        String finalnickname=databseName.substring(0,databseName.indexOf(' '))+" "+nickname2;
        databseName=databseName.replaceAll("%20","");
        databseName=databseName.replaceAll(" ","");
        websiteName=websiteName.replaceAll("\\s","");
        nickname =nickname.replaceAll("%20","").toLowerCase();
        nickname =nickname.replaceAll(" ","").toLowerCase();
//        finalnickname =finalnickname.replaceAll("\\s","").toLowerCase();
        websiteName=websiteName.toLowerCase();
        databseName=databseName.toLowerCase();
        String[] value=websiteName.split("\\(");
        String restOfval="";
        for(int i=1;i<value.length;i++){
            restOfval+=value[i];
        }
        if(databseName.length()<=websiteName.length()&&(databseName.contains(value[0].trim()) || nickname.contains(value[0].trim()))) {
            String restofData=databseName.substring(value[0].length());
            String restofdata=new StringBuilder(nickname).reverse().toString();
//                    .substring(new StringBuilder(value[0]).reverse().toString().length());
            System.out.println(nickname+"="+value[0]);
            String restofnick;
            try {
                restofnick = nickname.substring(value[0].length());
                System.out.println(restofdata+"="+restofnick);

            }
            catch (Exception e){
                restofnick="-1";
            }
//            if(restofdata.substring(0,value[0].length()))
//            if(restofData.length()==0){
//
            if(value[0].trim().equals(databseName.trim())||value[0].trim().equals(nickname.trim()))
                return true;
//            }
            else if (value[0].equals(databseName.substring(0, value[0].length()))) {
//                if(restOfval.replaceAll("\\s","").contains(restofData))
                return true;
            }

            if(restofnick.length()==0&&!restofnick.equals("-1")){

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

    public static void main(String args[]){
        System.out.println(check("Redmi Note 7 (Onyx Black, 32 GB)  (3 GB RAM)","Xiaomi Note 7"));
    }

}