package hanium.oldercare.oldercareservice.utility;

import java.util.ArrayList;
import java.util.List;

public class ParseManager {

    public static List<String> stringToArrayList(String str){

        List<String> array = new ArrayList<String>();

        str = str.substring(1,str.length());
        String[] splitArray = str.split(",");
        for(String objStr : splitArray){
            array.add(objStr.trim());
        }
        return array;

    }

}
