package lab4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufferMonitor {
    Buffer buffer;

    private final Lock lock = new ReentrantLock();
    private Condition consumersCondition = lock.newCondition();
    final int producerCount = 5;
    final int consumerCount = 10;
    private Condition producerConditions = lock.newCondition();
    private Condition consumerConditions = lock.newCondition();

    public BufferMonitor(Buffer startBuffer) {
        buffer = startBuffer;
    }

    public void produce(int portions) throws InterruptedException {
        lock.lock();
        try {

            while (!buffer.isValid(portions)) {
                producerConditions.await();
            }

            buffer.add(portions);
            System.out.println("Produced " + portions + " portions");
            System.out.println("Current number of portions" + buffer.getPortions() + "\n\n");
            consumersCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void consume(int portions) throws InterruptedException {
        lock.lock();
        try {

            while (portions > buffer.getPortions()) {
                consumerConditions.await();
            }

            buffer.remove(portions);
            System.out.println("Consumed " + portions + " portions");
            System.out.println("Current number of portions" + buffer.getPortions() + "\n\n");
            producerConditions.signal();
        } finally {
            lock.unlock();
        }
    }

}
