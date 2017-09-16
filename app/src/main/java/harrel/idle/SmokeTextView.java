package harrel.idle;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Harrel on 9/2/2017.
 */

public class SmokeTextView extends FontTextView {

    final private int DURATION = 500;
    private float DISTANCE;

    private int x = 0;
    private int y = 0;

    public SmokeTextView(Context context) {
        super(context);
        setFocusable(false);
        setFocusableInTouchMode(false);
        setClickable(false);
        setLongClickable(false);
        setTextIsSelectable(false);
        setEnabled(false);
        setMaxLines(1);
        //setGravity(Gravity.CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        DISTANCE = getTextSize() * -6;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

//        Log.d("SMOKEPAR", "w: " + ((View)getParent()).getWidth());

        setX(x - w / 2);
        setY(y - h * 3);
        super.onSizeChanged(w, h, oldw, oldh);
        startSmoke();
    }

    private void startSmoke(){
        animate().alpha(0f).translationYBy(DISTANCE).setDuration(DURATION).withEndAction(new Runnable() {
            @Override
            public void run() {
                ((FrameLayout)getParent()).removeView(SmokeTextView.this);
            }
        });
    }

    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }
}
