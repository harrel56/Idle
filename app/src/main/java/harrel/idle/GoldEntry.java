package harrel.idle;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by Harrel on 9/2/2017.
 */

public class GoldEntry {
    private Drawable icon;
    private String name;
    private BigDecimal cost;
    private BigDecimal goldPerSec;
    private BigDecimal powerReq;
    private BigDecimal count;
    private BigDecimal goldPerSecondCount;

    public static BigDecimal[] GROW_RATES;
    public static int lastOwnedPosition = -1;

    private boolean affordable;

    public GoldEntry(JSONObject json) throws JSONException {
        icon = null;
        name = json.getString("name");
        cost = new BigDecimal(json.getString("cost"));
        goldPerSec = new BigDecimal(json.getString("goldPerSec"));
        powerReq = new BigDecimal(json.getString("powerReq"));
        count = BigDecimal.ZERO;
        goldPerSecondCount = BigDecimal.ZERO;
        affordable = false;
    }

    public String toString(){
        return new String("name: " + name + ", cost: " + cost + ", goldPerSec: " + goldPerSec + ", powerReq: " + powerReq);
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() {
        return cost.multiply(GROW_RATES[count.intValue()]);
    }

    public BigDecimal getGoldPerSec() {
        return goldPerSec;
    }

    public BigDecimal getPowerReq() {
        return powerReq;
    }

    public BigDecimal getCount() {
        return count;
    }

    public BigDecimal getGoldPerSecondCount() {
        return goldPerSecondCount;
    }

    public void addCount(BigDecimal count) {
        this.count = this.count.add(count);
        goldPerSecondCount = this.count.multiply(goldPerSec);
    }

    public boolean isAffordable() {
        return affordable;
    }

    public void setAffordable(boolean affordable) {
        this.affordable = affordable;
    }
}
