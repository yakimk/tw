package a;

public class CounterSemaphore {
    private int state = 0;

    public CounterSemaphore(int startingState) {
        state = startingState;
    }

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
