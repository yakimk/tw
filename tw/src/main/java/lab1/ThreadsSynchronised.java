package lab1;

public class ThreadsSynchronised {
    static SyncCounter counter = new SyncCounter();


    public static class Decrementer extends Thread {
        synchronized public void run(){
            int count = 100000000;
            for(int i = 0; i < count; i++){
                counter.decrement();
            }
        }
    }

    public static class Incrementer extends Thread {

        synchronized public void run(){
            int count = 100000000;
            for(int i = 0; i < count; i++){
                counter.increment();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new ThreadsSynchronised.Incrementer();
        Thread thread2 = new ThreadsSynchronised.Decrementer();

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

counter.printCounter();
    }
}