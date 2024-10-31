package lab3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Kelner {
    private final int pairCount;
    private final Lock lock = new ReentrantLock();
    private final Condition[] paraGotowa;
    private final int[] czekaOsoba;
    private boolean stolikZajety = false;
    private int pairAtTable = -1;

    public Kelner(int pairCount) {
        this.pairCount = pairCount;
        this.czekaOsoba = new int[pairCount];
        this.paraGotowa = new Condition[pairCount];

        for (int i = 0; i < pairCount; i++) {
            paraGotowa[i] = lock.newCondition();
        }
    }

    public void chce_stolik(int j) throws InterruptedException {
        lock.lock();
        try {
            czekaOsoba[j]++;

            while (!(czekaOsoba[j] >= 2 && !stolikZajety) && pairAtTable != j) {
                paraGotowa[j].await();
            }

            if (!stolikZajety) {
                stolikZajety = true;
                pairAtTable = j;
            }

            czekaOsoba[j]--;

            if (czekaOsoba[j] > 0) {
                paraGotowa[j].signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void zwalniam(int j) {
        lock.lock();
        try {
            if (czekaOsoba[j] == 0) {
                stolikZajety = false;
                pairAtTable = -1;

                for (int i = 0; i < pairCount; i++) {
                    if (czekaOsoba[i] >= 2) {
                        paraGotowa[i].signalAll();
                        break;
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
