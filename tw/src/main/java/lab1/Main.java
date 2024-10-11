package lab1;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new ThreadsUnsynchronized.Incrementer();
        Thread thread2 = new ThreadsUnsynchronized.Decrementer();

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();


    }
}
