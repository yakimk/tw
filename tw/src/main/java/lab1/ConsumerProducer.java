package lab1;

import java.util.ArrayList;

public class ConsumerProducer {

    static final int producerCount = 7;
    static final int consumerCount = 6;

    static final int count = 1000;
    private static final Buffer buffer = new Buffer();

    public static class Producer implements Runnable {

        public void run() {

            for(int i = 0;  i < count;   i++) {
                buffer.put("message "+i);

            }

        }
    }

    public static class Consumer implements Runnable {
        public void run() {

            for(int i = 0;  i < count;   i++) {
                String message = buffer.take();
                System.out.println(message);
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Thread> producerThreads = new ArrayList<>();
        ArrayList<Thread> consumerThread = new ArrayList<>();

        for(int i = 0; i < producerCount; i++) {
            producerThreads.add(new Thread(new Producer()));
        }

        for(int i = 0; i < consumerCount; i++) {
            consumerThread.add(new Thread(new Consumer()));
        }

        for(int i = 0; i < producerCount; i++) {
            producerThreads.get(i).start();
        }

        for(int i = 0; i < consumerCount; i++) {
            consumerThread.get(i).start();
        }
        for(int i = 0; i < consumerCount; i++) {
            consumerThread.get(i).join();
        }
        for(int i = 0; i < producerCount; i++) {
            producerThreads.get(i).join();
        }
    }

}




