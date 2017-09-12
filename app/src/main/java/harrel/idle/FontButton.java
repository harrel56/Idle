package harrel.idle;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Harrel on 9/1/2017.
 */

public class FontButton extends Button {

    public FontButton(Context context) {
        super(context);
        Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/arcadeclassic.ttf");
        this.setTypeface(type);
        setBackgroundResource(R.drawable.button_selector);
    }
    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/arcadeclassic.ttf");
        this.setTypeface(type);
        setBackgroundResource(R.drawable.button_selector);
    }
    public FontButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/arcadeclassic.ttf");
        this.setTypeface(type);
        setBackgroundResource(R.drawable.button_selector);
    }
}
