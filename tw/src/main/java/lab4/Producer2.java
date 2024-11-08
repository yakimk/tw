package lab4;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Producer2 extends Thread {
    private final BufferMonitor monitor;

    public Producer2(BufferMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                monitor.produce(genValue());
                try {
                    Thread.sleep((int) (Math.random() * 5000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private int genValue() {
        return ThreadLocalRandom.current().nextInt(1, monitor.buffer.M + 1);
    }

}