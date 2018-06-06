import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class TimQueue implements Queue {

    private int numServers;
    private int lineCapacity;
    private int customersInLine = 0;
    private CustomerHandler customerAdder = new CustomerHandler(this);
    private ArrayList<TimServiceStation> stations = new ArrayList<>();
    private ArrayList<Thread> stationThreads = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    private int totalCustomersArrived = 0;
    private int totalCustomersServed = 0;
    private int totalCustomersLeft = 0;

    public TimQueue(int servers, int cap) {

        for (int i = 0; i < servers; i++) {
            TimServiceStation s = new TimServiceStation(this);
            stations.add(s);
            Thread t = new Thread(s);
            stationThreads.add(t);
            t.start();
        }

        Thread t = new Thread(customerAdder);
        t.start();

        numServers = servers;
        lineCapacity = cap;
    }

    public boolean getCustomer() {
        lock.lock();
        if (customersInLine > 0) {
            customersInLine--;
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;
    }

    public void reportArrived() {
        lock.lock();
        totalCustomersArrived++;
        lock.unlock();
    }

    public boolean addCustomer() {
        lock.lock();
        if (customersInLine < lineCapacity) {
            customersInLine++;
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;
    }

    public void reportServed() {
        lock.lock();
        totalCustomersServed++;
        lock.unlock();
    }

    public void reportLeavingCustomer() {
        lock.lock();
        totalCustomersLeft++;
        lock.unlock();
    }

    public void printResults() {
        System.out.printf("TIM QUEUE\nTotal customers arrived: %d\nTotal customers served: %d\nTotal customers left without service: %d\nAverage serving time: %f minutes per customer\n",
                totalCustomersArrived, totalCustomersServed, totalCustomersLeft, (double)(Main.clock.getTime() / 60) / (double)totalCustomersServed);
    }
}
