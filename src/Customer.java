public class Customer implements Runnable {

    private int arrivalTime;
    private CustomerHandler parentHandler;

    public Customer(CustomerHandler handler, int arrivTime) {
        parentHandler = handler;
        arrivalTime = arrivTime;
    }

    public boolean timeElapsed() {
        return Main.clock.getTime() - arrivalTime > 20;
    }

    public void run() {
        while (!timeElapsed());
        parentHandler.removeCustomer(this);
    }

}
