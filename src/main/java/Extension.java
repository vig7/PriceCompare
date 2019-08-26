public class Extension {
    String hit(String title){
        title=title.replaceAll("\"","");
        AddDbFlipkart flip=new AddDbFlipkart();
        flip.makeJDBCConnection();
        title=title.toLowerCase();
        String insideB="";
        String[] temp=title.split(" ");
        try {
            insideB=title.substring(title.indexOf("\\("),title.indexOf("\\)"));
        }
        catch (Exception e){

        }

            if(title.contains("\\(")&&title.contains("\\)")&&(insideB.contains("ram")||insideB.contains("rom"))){
               /* String Namemain =title.substring(0,title.indexOf("\\("));
                ArrayList<String>databaseSample=flip.matchName(Namemain);
                ArrayList<String>resultSample=new ArrayList<String>();

                for(int i=0;i<databaseSample.size();i++){
                    String lowerdatabase=databaseSample.get(i).replaceAll(" ","");

                }*/
            }
            String[] data=title.split(" ");
            String phoneName="";
            for(int i=0;i<data.length;i++){
                if(i==0){
                    if(flip.checkTitleCom(data[i]))
                        phoneName+=data[i];
                }
                else {
                    if(flip.checkTitle(data[i]))
                        phoneName+=" "+data[i];
                    else
                        break;
                }
            }
            phoneName=phoneName.trim();
            if(phoneName.length()==0)
                return "-1";
            title.indexOf("gb");
            int r=-1;
            int restofr=-1;
            if(title.contains("gb")) {
                r = recurgb(title, title.indexOf("gb"));
                if(r!=-1&&r<title.length())
                restofr = recurrest(title, title.indexOf("gb")+3);
            }
            int ram,rom;
            if(r>=restofr){
                ram=restofr;
                rom=r;
            }
            else {
                ram=r;
                rom=restofr;
            }
            String res=flip.checkExten(phoneName,""+ram,""+rom);
            if(res==null)
            res=flip.checkExten(phoneName,""+ram,"");
            System.out.println(res);
            flip.close();
            return res;

    }
    int recurgb(String title,int index){
        String res="";

        boolean b=true;
        while (index!=0){
            if(Character.isDigit(title.charAt(index))){
                int i=index;
                while (Character.isDigit(title.charAt(i))){
                    res+=title.charAt(i);
                    i--;
                    b=false;

                }
                if(!b){
                    String temp="";
                    for(int j=res.length()-1;j>=0;j--){
                        temp+=res.charAt(j);
                    }
                    return Integer.parseInt(temp);
                }
            }
            index --;
        }
        return -1;
    }
    int recurrest(String title,int index){
        if(index>title.length()-1)
            return -1;
        String rest=title.substring(index);
        if(rest.contains("gb")){
            index=rest.indexOf("gb");
            return  recurgb(rest,index);
        }
        return -1;
    }
}
