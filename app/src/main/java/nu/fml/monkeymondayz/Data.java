package nu.fml.monkeymondayz;

import java.util.*;

/**
 * Created by Oskar on 2015-04-21.
 */
public class Data {
    HashMap<String,String> monkeymonMap = new HashMap<String,String>();
    int myLevel = 0;
    String apaName;

    public Data() {
        this.monkeymonMap.put("asphalt", Monkey.MONKEY_ASPHALT);
        this.monkeymonMap.put("forest", Monkey.MONKEY_FOREST);
        this.monkeymonMap.put("water", Monkey.MONKEY_WATER);
    }
    public String getApa(String s) {
        String apa = monkeymonMap.get(s);
        if (apa==null) {
            apa = Monkey.MONKEY_DEFAULT;
        }
        String apaName= apa;
        return apa;
    }
    public String getApaName(){return apaName; }
    public void updateMyLevel(int i) {
        myLevel = myLevel+i;
    }
    public int getMyLevel(){
        return myLevel;
    }
}
