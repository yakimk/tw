package lab4;

public class Producer extends Thread {
    private final PipelineMonitor monitor;
    private final int producerId;
    private int producerPosition = 0;

    public Producer(PipelineMonitor monitor, int producerId) {
        this.monitor = monitor;
        this.producerId = producerId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                monitor.produce(producerId, producerPosition);
                produce();
                monitor.finishProduction(producerId, producerPosition);
                producerPosition = monitor.nextCell(producerPosition);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void produce() {
        try {
            Thread.sleep((int) (Math.random() * 5000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
