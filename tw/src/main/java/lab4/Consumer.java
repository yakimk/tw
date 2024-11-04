package lab4;

public class Consumer extends Thread {
    private final PipelineMonitor monitor;
//    private final int producerId;
//    private int producerPosition = 0;

    public Consumer(PipelineMonitor monitor) {
        this.monitor = monitor;
//        this.producerId = producerId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                monitor.startConsumption();

                for (int i = 0; i < monitor.size; i++) {
                    Thread.sleep((int) (Math.random() * 5000));
                    monitor.consume(i);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}