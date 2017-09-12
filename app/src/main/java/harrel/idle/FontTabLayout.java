package harrel.idle;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Harrel on 9/1/2017.
 */

public class FontTabLayout extends TabLayout {

    public FontTabLayout(Context context) {
        super(context);
    }

    public FontTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setupWithViewPager(ViewPager viewPager) {
        super.setupWithViewPager(viewPager);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arcadeclassic.ttf");
        this.removeAllTabs();

        ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

        PagerAdapter adapter = viewPager.getAdapter();

        for (int i = 0, count = adapter.getCount(); i < count; i++) {
            Tab tab = this.newTab();
            this.addTab(tab.setText(adapter.getPageTitle(i)));
            AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
            view.setTypeface(tf);
        }
    }
}
