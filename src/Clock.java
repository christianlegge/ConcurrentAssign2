import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

public class Clock {

    private Timer timer;
    private int currentTime = 0;
    private ReentrantLock lock = new ReentrantLock();

    public Clock(int ms) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentTime++;
                if (currentTime >= Main.endTime) {
                    stopClock();
                }
            }
        }, ms, ms);
    }

    public int getTime() {
        lock.lock();
        int t = currentTime;
        lock.unlock();
        return t;
    }

    public void stopClock() {
        timer.cancel();
        Main.printAllResults();
    }

}
