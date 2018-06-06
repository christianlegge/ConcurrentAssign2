import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class DominionQueue implements Queue {

    private int numServers;
    private int capacity;
    private CustomerHandler customerAdder = new CustomerHandler(this);
    private ArrayList<DominionCheckoutStation> stations = new ArrayList<>();
    private ArrayList<Thread> stationThreads = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    private int totalCustomersArrived = 0;
    private int totalCustomersServed = 0;
    private int totalCustomersLeft = 0;


    public DominionQueue(int servers, int cap) {

        for (int i = 0; i < servers; i++) {
            DominionCheckoutStation s = new DominionCheckoutStation(this);
            stations.add(s);
            Thread t = new Thread(s);
            stationThreads.add(t);
            t.start();
        }

        Thread t = new Thread(customerAdder);
        t.start();

        numServers = servers;
        capacity = cap;
    }

    public void reportArrived() {
        lock.lock();
        totalCustomersArrived++;
        lock.unlock();
    }

    public void reportServed() {
        lock.lock();
        totalCustomersServed++;
        lock.unlock();
    }

    public boolean addCustomer() {
        lock.lock();
        int shortestLine = findShortestLine();
        if (shortestLine < 0) {
            lock.unlock();
            return false;
        }
        else {
            stations.get(shortestLine).addCustomer();
            lock.unlock();
            return true;
        }
    }

   public void reportLeavingCustomer() {
       lock.lock();
       totalCustomersLeft++;
       lock.unlock();
   }

    public void printResults() {
        System.out.printf("DOMINION QUEUE\nTotal customers arrived: %d\nTotal customers served: %d\nTotal customers left without service: %d\nAverage serving time: %f minutes per customer\n",
                totalCustomersArrived, totalCustomersServed, totalCustomersLeft, (double)(Main.clock.getTime() / 60) / (double)totalCustomersServed);
    }

    private int findShortestLine() {
        ArrayList<Integer> shortest = new ArrayList<>();
        for (int i = 0; i < numServers; i++) {
            if (stations.get(i).getLineLength() < capacity) {
                if (shortest.isEmpty() || stations.get(i).getLineLength() < stations.get(shortest.get(0)).getLineLength()) {
                    shortest = new ArrayList<>();
                    shortest.add(i);
                } else if (stations.get(i).getLineLength() == stations.get(shortest.get(0)).getLineLength()) {
                    shortest.add(stations.get(i).getLineLength());
                }
            }
        }
        return shortest.isEmpty() ? -1 : shortest.get((int) (Math.random() * shortest.size()));
    }
}
