package nu.fml.monkeymondayz;

import java.util.*;

/**
 * Created by Oskar on 2015-04-21.
 */
public class Data {
    HashMap<String,String> monkeymonMap = new HashMap<String,String>();
    int myLevel = 0;

    public Data() {
        this.monkeymonMap.put("asphalt", "Asfaltsapa");
        this.monkeymonMap.put("forest", "Grönapa");
        this.monkeymonMap.put("water", "Fiskapa");
    }
    public String getApa(String s) {
        String apa = monkeymonMap.get(s);
        if (apa==null) {
            apa = "Retardapa";
        }
        return apa;
    }
    public void setMyLevel(int i) {
        myLevel = myLevel+i;
    }
    public int getMyLevel(){
        return myLevel;
    }
}
