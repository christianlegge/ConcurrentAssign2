/*

Concurrent Programming Assignment 2
Christian Legge 201422748

Sample outputs:

DOMINION QUEUE
Total customers arrived: 173
Total customers served: 121
Total customers left without service: 53
Average serving time: 1.983471 minutes per customer

TIM QUEUE
Total customers arrived: 178
Total customers served: 80
Total customers left without service: 105
Average serving time: 3.000000 minutes per customer

*/

public class Main {

    public static int endTime;
    public static Clock clock;
    private static Queue queue;

    public static void main(String[] args) {
        try {
            endTime = Integer.parseInt(args[0]) * 60;
        }
        catch (Exception e) {
            System.out.println("Usage: java ConcurrentAssign2 <minutes> <Tim/Dominion>");
            return;
        }
        clock = new Clock(5);
        if ("Tim".equals(args[1])) {
            queue = new TimQueue(2, 6);
        }
        else if ("Dominion".equals(args[1])) {
            queue = new DominionQueue(3, 2);
        }
        else {
            System.out.println("Usage: java ConcurrentAssign2 <minutes> <Tim/Dominion>");
            return;
        }
    }

    public static void printAllResults() {
        queue.printResults();
    }
}
