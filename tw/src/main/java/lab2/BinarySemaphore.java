package lab2;

public class BinarySemaphore {
    enum State {
        busy,
        free
    }

    private State state = State.free;


    synchronized void release() {
        state = State.free;

        notifyAll();
    }


    synchronized void get(){
        while (state != State.free) {
            try {
                wait();
            }catch (InterruptedException e) { e.printStackTrace(); }
        }
        state = State.busy;
    }
}
