package lab3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final int printerCount;
    private final boolean[] printerBusy;
    private final Lock lock;
    private final Condition condition;

    public Monitor(int M) {
        this.printerCount = M;
        this.printerBusy = new boolean[M];
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public int zarezerwuj() throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                for (int i = 0; i < printerCount; i++) {
                    if (!printerBusy[i]) {
                        printerBusy[i] = true;
                        return i;
                    }
                }
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void zwolnij(int nr_drukarki) {
        lock.lock();
        try {
            printerBusy[nr_drukarki] = false;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}