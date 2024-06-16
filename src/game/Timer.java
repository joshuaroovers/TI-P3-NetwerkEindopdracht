package game;

import java.io.Serializable;

public class Timer implements Serializable {
    private int interval;
    private long lasttick;

    public Timer(int var1) {
        this.interval = var1;
        this.lasttick = System.currentTimeMillis();
    }

    public boolean timeout() {
        long var1 = System.currentTimeMillis();
        if (var1 > this.lasttick + (long)this.interval) {
            this.lasttick += (long)this.interval;
            return true;
        } else {
            return false;
        }
    }

    public void mark() {
        this.lasttick = System.currentTimeMillis();
    }

    public void setInterval(int var1) {
        this.interval = var1;
        this.lasttick = System.currentTimeMillis();
    }
}
