package harrel.idle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;

public class FontTextView extends TextView {
    public static Typeface FONT_NAME;

    public static String[] POSTFIXES_SHORT = {"", "K", "KK", "B", "T", "QD", "QT", "SX", "SP", "OC", "NO", "DC", "UD", "DD", "TD"};
    public static String[] POSTFIXES_LONG = {"", "Thousand", "Million", "Billion", "Trillion", "Quadrillion", "Quintiliion", "Sextillion", "Septillion", "Octillion", "Nonillion",
            "Decillion", "Undecillion", "Duodecillion   ", "Tredecillion"};


    public FontTextView(Context context) {
        super(context);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "fonts/arcadeclassic.ttf");
        this.setTypeface(FONT_NAME);
        setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        setTextColor(Color.WHITE);
    }
    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "fonts/arcadeclassic.ttf");
        this.setTypeface(FONT_NAME);
        this.setTypeface(FONT_NAME);
        setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        setTextColor(Color.WHITE);
    }
    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(FONT_NAME == null) FONT_NAME = Typeface.createFromAsset(context.getAssets(), "fonts/arcadeclassic.ttf");
        this.setTypeface(FONT_NAME);
        this.setTypeface(FONT_NAME);
        setShadowLayer(1.6f, 1.5f, 1.3f, Color.BLACK);
        setTextColor(Color.WHITE);
    }

    public static String valueToString(BigDecimal n){

        int zeros = (n.signum() == 0) ? 0 : ( n.precision() - n.scale() - 1);
        int power = zeros / 3;
        return n.divide(BigDecimal.valueOf(Math.pow(1000, power))).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString() + POSTFIXES_SHORT[power];
    }

    public static String timeToString(double n){

        int days = (int)n / 86400;
        n = n % 86400;
        int hours = (int)n / 3600;
        n = n % 3600;
        int minutes = (int)n / 60;
        n = n % 60;

        StringBuilder str = new StringBuilder();
        if(days != 0) {
            str.append(days);
            str.append("d ");
            str.append(hours);
            str.append("h ");
            str.append(minutes);
            str.append("m ");
        }else if(hours != 0){
            str.append(hours);
            str.append("h ");
            str.append(minutes);
            str.append("m ");
        }else if(minutes != 0){
            str.append(minutes);
            str.append("m ");
        }
        str.append((int)n);
        str.append("s ");
        return str.toString();
    }
}