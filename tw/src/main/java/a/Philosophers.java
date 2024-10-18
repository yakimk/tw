package a;

import static a.BinarySemaphore.State.free;

public class Philosophers {

    final int waitTime = 1000;
    private BinarySemaphore[] forks = new BinarySemaphore[5];

    private CounterSemaphore waiter = new CounterSemaphore(4);

    public Philosophers() {
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new BinarySemaphore(free);
        }
    }

    public void eat1(int philosopher) throws InterruptedException {
        forks[philosopher].get(); // left
        forks[(philosopher + 1) % 5].get(); // right

        System.out.println("Philosopher " + philosopher + " is eating.");
        Thread.sleep((int)(Math.random() * waitTime));

        forks[philosopher].release(); // left
        forks[(philosopher + 1) % 5].release(); // right
    }

    public void eat2(int philosopher) throws InterruptedException {
        if (philosopher % 2 == 1) {
            forks[philosopher].get(); // left
            forks[(philosopher + 1) % 5].get(); // right
        }else{
            forks[(philosopher + 1) % 5].get(); // right
            forks[philosopher].get(); // left
        }
        System.out.println("Philosopher " + philosopher + " is eating.");
        Thread.sleep((int)(Math.random() * waitTime));

        forks[philosopher].release(); // left
        forks[(philosopher + 1) % 5].release(); // right
    }

    public void eat3(int philosopher) throws InterruptedException {
        waiter.get();

        forks[philosopher].get(); // left
        forks[(philosopher + 1) % 5].get(); // right

        System.out.println("Philosopher " + philosopher + " is eating.");
        Thread.sleep((int)(Math.random() * waitTime));

        forks[philosopher].release(); // left
        forks[(philosopher + 1) % 5].release(); // right

        waiter.release();
    }



    public void think(int philosopher) throws InterruptedException {
        System.out.println("Philosopher " + philosopher + " is thinking.");
        Thread.sleep((int)(Math.random() * waitTime));
    }


}