package lab2;

public class CounterSemaphore {
    private int state = 0;

    synchronized void get() {
        while (state == 0) {
           try {
               wait();
           } catch (InterruptedException e) {e.printStackTrace();}
        }

        state--;
    }

    synchronized void release() {
        state++;
    }
}
