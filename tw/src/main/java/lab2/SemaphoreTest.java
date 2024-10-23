package lab2;

public class SemaphoreTest {

    static private final int iters = 100000000;
    static private final BinarySemaphore semaphore = new BinarySemaphore(BinarySemaphore.State.free);


    public static class Counter {
        int count = 0;
        void printCounter(){
            System.out.println(count);
        }

        void increment(){
            semaphore.get();
            count++;
            semaphore.release();
        }

        void decrement(){
            semaphore.get();
            count--;
            semaphore.release();
        }
    }


    static SemaphoreTest.Counter counter = new SemaphoreTest.Counter();

    public static class UnsyncDecrementer extends Thread {
        synchronized public void run(){
            for(int i = 0; i < iters; i++) counter.decrement();
        }
    }

    public static class UnsyncIncrementer extends Thread {

        synchronized public void run(){
            for(int i = 0; i < iters; i++) counter.increment();
        }
    }


    public static void main(String[] args) throws InterruptedException {

        Thread unsyncThread1 = new SemaphoreTest.UnsyncIncrementer();
        Thread unsyncThread2 = new SemaphoreTest.UnsyncDecrementer();

        unsyncThread1.start();
        unsyncThread2.start();

        unsyncThread1.join();
        unsyncThread2.join();

        System.out.print("Synchronized result: ");
        counter.printCounter();
    }
}
