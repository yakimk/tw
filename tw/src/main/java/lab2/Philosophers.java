package lab2;

import static lab2.BinarySemaphore.State.free;

public class Philosophers {

    final double waitTime = 0.1;
    final int philosophersCount = 5;

    private final BinarySemaphore[] forks = new BinarySemaphore[philosophersCount];

    private final CounterSemaphore waiter = new CounterSemaphore(philosophersCount - 1);

    public Philosophers() {
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new BinarySemaphore(free);
        }
    }

    private void eat(int philosopher) throws InterruptedException {
        forks[philosopher].get(); // left
        forks[(philosopher + 1) % philosophersCount].get(); // right

        System.out.println("Philosopher " + (philosopher +1) + " is eating.");
        Thread.sleep((int)(Math.random() * waitTime));

        forks[philosopher].release(); // left
        forks[(philosopher + 1) % philosophersCount].release(); // right
    }


    public void eat1(int philosopher) throws InterruptedException {
        eat(philosopher);
    }


    public void eat2(int philosopher) throws InterruptedException {
        if (philosopher % 2 == 1) {
            forks[philosopher].get(); // left
            forks[(philosopher + 1) % philosophersCount].get(); // right
        }else{
            forks[(philosopher + 1) % philosophersCount].get(); // right
            forks[philosopher].get(); // left
        }
        System.out.println("Philosopher " + (philosopher + 1) + " is eating.");
        Thread.sleep((int)(Math.random() * waitTime));

        forks[philosopher].release(); // left
        forks[(philosopher + 1) % philosophersCount].release(); // right
    }

    public void eat3(int philosopher) throws InterruptedException {
        waiter.get();

        eat(philosopher);

        waiter.release();
    }



    public void think(int philosopher) throws InterruptedException {
        System.out.println("Philosopher " + (philosopher + 1) + " is thinking.");
        Thread.sleep((int)(Math.random() * waitTime));
    }


}