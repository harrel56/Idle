package harrel.idle;

/**
 * Created by Harrel on 9/5/2017.
 */

public interface OnPowerDataChangeListener {
    void onPowerTick(int position, int progress);
    void onPowerDataChange(int position);
}
