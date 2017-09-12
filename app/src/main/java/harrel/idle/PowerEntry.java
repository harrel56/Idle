package harrel.idle;

import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by Harrel on 9/2/2017.
 */

public class PowerEntry {
    private Drawable icon;
    private String name;
    private BigDecimal powerPerSec;
    private BigDecimal powerReq;
    private double time;
    private double timeDone = -1;

    public static int lastAvailablePosition = -1;

    private boolean affordable;

    public PowerEntry(JSONObject json) throws JSONException {
        icon = null;
        name = json.getString("name");
        powerPerSec = new BigDecimal(json.getString("powerPerSec"));
        powerReq = new BigDecimal(json.getString("powerReq"));
        time = json.getDouble("time");
        affordable = false;
    }

    public String toString(){
        return new String("name: " + name + ", powerReq: " + powerReq + ", powerPerSec: " + powerPerSec + ", time: " + time);
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPowerPerSec() {
        return powerPerSec;
    }

    public BigDecimal getPowerReq() {
        return powerReq;
    }

    public double getTime() {
        return time;
    }

    public boolean isAffordable() {
        return affordable;
    }

    public void setAffordable(boolean affordable) {
        this.affordable = affordable;
    }

    public double getTimeDone() {
        return timeDone;
    }

    public void setTimeDone(double timeDone) {
        this.timeDone = timeDone;
    }
}
