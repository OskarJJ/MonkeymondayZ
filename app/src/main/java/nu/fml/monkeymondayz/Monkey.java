package nu.fml.monkeymondayz;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Hawry on 2015-04-23.
 */
public class Monkey {
    public static final String MONKEY_ASPHALT = "Asfaltsapa";
    public static final String MONKEY_FOREST = "Grönapa";
    public static final String MONKEY_WATER = "Fiskapa";
    public static final String MONKEY_DEFAULT = "Retardapa";

    private String type;
    private Drawable monkeyImage;
    private int maxHealth,currentHealth,level,attackDamage;
    private Context context;
    public Monkey(String type,Context context,int level) {
        this.type = type;
        this.context = context;
        this.level = level;
        this.attackDamage = 5+(2*this.level);
        this.maxHealth = 10+(this.level);
        this.currentHealth = maxHealth;
        initMonkey();
    }
    public Monkey(Context context) {
        this(Monkey.MONKEY_DEFAULT,context,1);
    }
    public Monkey(Context context,int level) {
        this(Monkey.MONKEY_DEFAULT,context,level);
    }
    public Monkey(String type,Context context) {
        this(type,context,1);
    }

    private void initMonkey() {
        Resources res = context.getResources();
        Drawable monkeyImg = res.getDrawable(R.drawable.apa);
        int color = 0;
        boolean changeColor = false;
        PorterDuff.Mode filter = PorterDuff.Mode.SRC_ATOP;
        switch (type) {
            case Monkey.MONKEY_ASPHALT:
                color = Color.parseColor("#44FF0000");
                changeColor = true;
                break;
            case Monkey.MONKEY_FOREST:
                color = Color.parseColor("#4400FF00");
                changeColor = true;
                break;
            case Monkey.MONKEY_WATER:
                color = Color.parseColor("#440000FF");
                changeColor = true;
                break;
            default:
                changeColor = false;
                break;
        }
        if (changeColor) {
            monkeyImg.setColorFilter(color,filter);
        }
        this.monkeyImage = monkeyImg;
    }

    public int doDamage(int dhp) {
        this.currentHealth -= dhp;
        return this.currentHealth;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getHealth() {
        return this.currentHealth;
    }

    public int getLevel() {
        return this.level;
    }

    public int getAttackDamage() {
        return this.getAttackDamage();
    }

    public Drawable getDrawable() {
        return this.monkeyImage;
    }

}
