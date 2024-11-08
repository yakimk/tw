package lab4;

public class BufferSimulation {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        BufferMonitor monitor = new BufferMonitor(buffer);

        for (int i = 0; i < monitor.producerCount; i++) {
            new Producer2(monitor).start();
        }

        for (int i = 0; i < monitor.consumerCount; i++) {
            new Consumer2(monitor).start();
        }
    }
}
