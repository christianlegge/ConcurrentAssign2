import java.util.ArrayDeque;
import java.util.concurrent.locks.ReentrantLock;

public class CustomerHandler implements Runnable {

    private Queue parentQueue;
    private int startTime;
    private int nextSendTime;
    private ArrayDeque<Customer> waitingCustomers = new ArrayDeque<>();
    private ReentrantLock lock = new ReentrantLock();

    public CustomerHandler(Queue q) {
        parentQueue = q;
        startTime = Main.clock.getTime();
    }

    @Override
    public void run() {
        nextSendTime = startTime + (int) (Math.random() * (120 - 40) + 40);
        while (Main.clock.getTime() - startTime < Main.endTime) {
            if (Main.clock.getTime() >= nextSendTime) {
                sendCustomer();
                parentQueue.reportArrived();
                nextSendTime = Main.clock.getTime() + (int) (Math.random() * (120 - 40) + 40);
            }
            if (!waitingCustomers.isEmpty()) {
                if (parentQueue.addCustomer()) {
                    waitingCustomers.removeFirst();
                }
            }
        }
        for (Customer c : waitingCustomers) {
            waitingCustomers.remove(c);
        }
    }

    private void sendCustomer() {
        if (!parentQueue.addCustomer()) {
            Customer c = new Customer(this, Main.clock.getTime());
            waitingCustomers.add(c);
            Thread t = new Thread(c);
            t.start();
        }
    }

    public void removeCustomer(Customer c) {
        lock.lock();
        waitingCustomers.remove(c);
        parentQueue.reportLeavingCustomer();
        lock.unlock();
    }


}
