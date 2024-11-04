package lab4;

public class PipelineSimulation {

    public static void main(String[] args) {
        PipelineMonitor monitor = new PipelineMonitor();

        for (int i = 0; i < monitor.producerCount; i++) {
            new Producer(monitor, i).start();
        }

        for (int i = 0; i < monitor.consumerCount; i++) {
            new Consumer(monitor).start();
        }
    }

}
