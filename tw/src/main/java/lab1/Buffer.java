package lab1;

public class Buffer {
    private String message;
    private boolean empty = true;

    public synchronized void put(String message) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.message = message;
        empty = false;
        notifyAll();
    }

    public synchronized String take() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        String msg = message;
        empty = true;
        notifyAll();
        return msg;
    }
}

