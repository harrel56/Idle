package harrel.idle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Harrel on 9/2/2017.
 */

public class AnimatedImageButton extends ImageButton {

    final private float SCALE_DOWN = 0.9f;
    final private float SCALE_UP = 1f;

    public enum State {STATIC, SCALING_DOWN, SCALING_UP;}

    private State mState = State.STATIC;
    private boolean isNextAnimationPending = false;

    public AnimatedImageButton(Context context) {
        super(context);
    }

    public AnimatedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void scaleDown(final int duration){
        mState = State.SCALING_DOWN;
        animate().scaleX(SCALE_DOWN).scaleY(SCALE_DOWN).setDuration(duration).withEndAction(new Runnable() {
            @Override
            public void run() {
                if(!isPressed())
                    scaleUp(duration);
                else {
                    isNextAnimationPending = false;
                    mState = State.STATIC;
                }
            }
        });
    }

    public void scaleUp(final int duration){
        mState = State.SCALING_UP;
        animate().scaleX(SCALE_UP).scaleY(SCALE_UP).setDuration(duration).withEndAction(new Runnable() {
            @Override
            public void run() {
                if(isNextAnimationPending) {
                    isNextAnimationPending = false;
                    scaleDown(duration);
                }
                else
                    mState = State.STATIC;
            }
        });
    }

    public void queueNextAnimation(){
        isNextAnimationPending = true;
    }

    public State getState() {
        return mState;
    }
}
