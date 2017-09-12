package harrel.idle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Harrel on 8/28/2017.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public MyPagerAdapter(FragmentManager m){
        super(m);
    }

    @Override
    public int getCount(){
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int pos){
        switch(pos){
            case 0:
                return GoldFragment.newInstance("0", "Money");
            case 1:
                return PowerLevelFragment.newInstance("1", "Training");
            case 2:
                return UpgradesFragment.newInstance("2", "Upgrades");
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Money";
            case 1:
                return "Training";
            case 2:
                return "Upgrades";
            default:
                return "null";
        }

    }

}
