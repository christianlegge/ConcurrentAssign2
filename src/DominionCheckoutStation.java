public class DominionCheckoutStation implements Runnable {

    private DominionQueue parentQueue;
    private int customersInLine = 0;

    public DominionCheckoutStation(DominionQueue q) {
        parentQueue = q;
    }

    @Override
    public void run() {
        while (Main.clock.getTime() < Main.endTime) {
            if (customersInLine > 0) {
                serve();
            }
        }
    }

    private void serve() {
        customersInLine--;
        int serveStartTime = Main.clock.getTime();
        int serveTime = (int) (Math.random() * (600 - 120) + 120);
        while (Main.clock.getTime() - serveStartTime < serveTime && Main.clock.getTime() < Main.endTime) {
            if (Main.clock.getTime() >= Main.endTime) {
                return;
            }
        }
        parentQueue.reportServed();
    }

    public void addCustomer() {
        customersInLine++;
    }

    public int getLineLength() {
        return customersInLine;
    }
}
