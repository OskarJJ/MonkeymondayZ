package nu.fml.monkeymondayz;

import java.util.*;

/**
 * Created by Oskar on 2015-04-21.
 */
public class Data {
    ArrayList<String> monkeymonList = new ArrayList<String>();


    public Data() {
        this.monkeymonList.add(0, "Asfaltsapa");
        this.monkeymonList.add(1, "Grönapa");
        this.monkeymonList.add(2, "Fiskapa");
    }
    public String getApa(int i){
       String apa;
        if(i>2||0>i){
        apa = "Retardapa";
        }
        apa =this.monkeymonList.get(i);
        return apa;
    }

}
