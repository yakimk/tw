package lab2;

public class BinarySemaphore {
    public enum State {
        busy,
        free
    }


    private State state = State.free;

    public BinarySemaphore(State startingState) {
        state = startingState;
    }

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
