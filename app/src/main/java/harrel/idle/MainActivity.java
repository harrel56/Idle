package harrel.idle;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private MyPagerAdapter mMyPagerAdapter;
    private ViewPager mViewPager;
    private AnimatedImageButton mAnimatedImageButton;
    private FontTextView mTextViewGold;
    private FontTextView mTextViewGps;
    private FontTextView mTextViewPower;
    private FrameLayout mUpperLayout;
    private Handler mHandler = new Handler();

    private Timer mTimer = new Timer();

    //Variables for gold/lvl count
    private BigDecimal goldCount = BigDecimal.ZERO;
    private BigDecimal goldOnTap = BigDecimal.valueOf(10000l);
    private BigDecimal goldPerSec = BigDecimal.ZERO;
    private BigDecimal goldPerSecScaled = BigDecimal.ZERO;
    private BigDecimal powerCount = BigDecimal.TEN;
    private int currentTrainingIndex = -1;

    private GoldEntry[] goldEntries;
    private PowerEntry[] powerEntries;

    private OnGoldDataChangeListener mGoldListener;
    private OnPowerDataChangeListener mPowerListener;

    private final int REFRESH_TIME = 100; //ms
    private final BigDecimal REFRESH_FACTOR = BigDecimal.valueOf(REFRESH_TIME / 1000.0); //to save computations


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        fetchGrowRates();
        fetchPowerData();
        fetchGoldData();

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        addGoldCount(goldPerSecScaled);
                    }
                });
            }
        }, 0, REFRESH_TIME);

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(currentTrainingIndex != -1){
                            PowerEntry tmp = powerEntries[currentTrainingIndex];
                            addPowerCount(tmp.getPowerPerSec());
                            tmp.setTimeDone(tmp.getTimeDone() + 1);
                            mPowerListener.onPowerDataChange(currentTrainingIndex, (int)(tmp.getTimeDone() / tmp.getTime() * 100));
                        }
                    }
                });
            }
        }, 0, 1000);

        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mMyPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        FontTabLayout tabLayout = (FontTabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mUpperLayout = (FrameLayout) findViewById(R.id.upperLayout);
        mTextViewGold = (FontTextView) findViewById(R.id.textViewGold);
        mTextViewGps = (FontTextView) findViewById(R.id.textViewGps);
        mTextViewPower = (FontTextView) findViewById(R.id.textViewPower);

        ImageButton button = (ImageButton) findViewById(R.id.optionsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

                dialog.setContentView(R.layout.dialog_options);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                dialog.getWindow().getDecorView().setSystemUiVisibility(MainActivity.this.getWindow().getDecorView().getSystemUiVisibility());
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            }
        });

        mAnimatedImageButton = (AnimatedImageButton) findViewById(R.id.imgButton);
        mAnimatedImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 addGoldCount(goldOnTap);
            }
        });
        //Proper clickable image animation
        mAnimatedImageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        mAnimatedImageButton.setPressed(true);

                        if(mAnimatedImageButton.getState() == AnimatedImageButton.State.STATIC)
                            mAnimatedImageButton.scaleDown(40);
                        else
                            mAnimatedImageButton.queueNextAnimation();
                        break;

                    case MotionEvent.ACTION_UP:

                        mAnimatedImageButton.setPressed(false);
                        mAnimatedImageButton.callOnClick();

                        if(mAnimatedImageButton.getState() == AnimatedImageButton.State.STATIC)
                            mAnimatedImageButton.scaleUp(40);

                        SmokeTextView text = new SmokeTextView(MainActivity.this);

                        text.setPos((int)event.getX(), (int)event.getY());
                        text.setText("+" + FontTextView.valueToString(goldOnTap) + " zeni");
                        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        text.setLayoutParams(params);
                        mUpperLayout.addView(text);

                        //Log.d("CNT", Integer.toString(mUpperLayout.getChildCount()));
                        //Log.d("POSCLICK", Integer.toString(text.getMeasuredWidth()) + ", " + Integer.toString(text.getMeasuredHeight()));
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void setOnGoldDataChangeListener(OnGoldDataChangeListener l) {mGoldListener = l;}

    public void setOnPowerDataChangeListener(OnPowerDataChangeListener l) {mPowerListener = l;}

    public synchronized void addGoldCount(BigDecimal val){
        goldCount = goldCount.add(val);
        if(mTextViewGold != null)
            mTextViewGold.setText(FontTextView.valueToString(goldCount));

        for(int i = 0; i < goldEntries.length; i++){
            boolean affordable = goldEntries[i].getCost().compareTo(goldCount) <= 0;
            if(goldEntries[i].isAffordable() ^ affordable){
                goldEntries[i].setAffordable(affordable);
                if(mGoldListener != null)
                    mGoldListener.onGoldDataChange(i);
            }

        }
    }

    public synchronized void addPowerCount(BigDecimal val){

        powerCount = powerCount.add(val);
        mTextViewPower.setText(FontTextView.valueToString(powerCount));

        SmokeTextView text = new SmokeTextView(MainActivity.this);

        text.setPos(mUpperLayout.getWidth() / 2, mUpperLayout.getHeight() / 2);
        text.setText("+" + FontTextView.valueToString(val) + " power");
        text.setTextColor(Color.BLUE);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        text.setLayoutParams(params);
        mUpperLayout.addView(text);
    }

    private void fetchGoldData(){
        InputStream jsonStream = getResources().openRawResource(R.raw.gold_entries);
        try {
            JSONObject jsonObject = new JSONObject(convertStreamToString(jsonStream));
            JSONArray jsonEntries = jsonObject.getJSONArray("goldEntries");
            goldEntries = new GoldEntry[jsonEntries.length()];

            for(int i = 0; i < jsonEntries.length(); i++){
                goldEntries[i] = new GoldEntry(jsonEntries.getJSONObject(i));
            }
        }catch(Exception e){
            Log.d("JSON ERROR", e.toString());
        }
    }

    private void fetchPowerData(){
        InputStream jsonStream = getResources().openRawResource(R.raw.power_entries);
        try {
            JSONObject jsonObject = new JSONObject(convertStreamToString(jsonStream));
            JSONArray jsonEntries = jsonObject.getJSONArray("powerEntries");
            powerEntries = new PowerEntry[jsonEntries.length()];

            for(int i = 0; i < jsonEntries.length(); i++){
                powerEntries[i] = new PowerEntry(jsonEntries.getJSONObject(i));
            }
        }catch(Exception e){
            Log.d("JSON ERROR", e.toString());
        }
    }

    private void fetchGrowRates(){
        InputStream jsonStream = getResources().openRawResource(R.raw.grow_rate);
        try {
            JSONObject jsonObject = new JSONObject(convertStreamToString(jsonStream));
            JSONArray jsonEntries = jsonObject.getJSONArray("growRate");
            BigDecimal[] vals = new BigDecimal[jsonEntries.length()];

            for(int i = 0; i < jsonEntries.length(); i++){
                vals[i] = new BigDecimal(jsonEntries.getJSONObject(i).getDouble("val"));
            }

            GoldEntry.GROW_RATES = vals;
        }catch(Exception e){
            Log.d("JSON ERROR", e.toString());
        }
    }

    public GoldEntry[] getGoldData(){
        return goldEntries;
    }

    public PowerEntry[] getPowerData(){
        return powerEntries;
    }

    public void recalculateGoldPerSec(){
        BigDecimal temp = BigDecimal.ZERO;
        for(int i = 0; i < goldEntries.length; i++){
            if(!goldEntries[i].getCount().equals(0))
                temp = temp.add(goldEntries[i].getGoldPerSecondCount());
        }
        goldPerSec = temp;
        goldPerSecScaled = goldPerSec.multiply(REFRESH_FACTOR);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mTextViewGps.setText(FontTextView.valueToString(goldPerSec));
            }
        });
    }

    public void setCurrentTrainingIndex(int index){
        currentTrainingIndex = index;
    }

    public int getCurrentTrainingIndex(){
        return currentTrainingIndex;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
