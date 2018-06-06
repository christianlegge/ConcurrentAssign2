public class TimServiceStation implements Runnable {

    private TimQueue parentQueue;

    public TimServiceStation(TimQueue q) {
        parentQueue = q;
    }

    @Override
    public void run() {
        while (Main.clock.getTime() < Main.endTime) {
            if (parentQueue.getCustomer()) {
                serve();
            }
        }
    }

    private void serve() {
        int serveStartTime = Main.clock.getTime();
        int serveTime = (int) (Math.random() * (600 - 120) + 120);
        while (Main.clock.getTime() - serveStartTime < serveTime) {
            if (Main.clock.getTime() >= Main.endTime) {
                return;
            }
        }
        parentQueue.reportServed();
    }
}
