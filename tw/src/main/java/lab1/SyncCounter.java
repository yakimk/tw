package lab1;

public class SyncCounter {
    int count = 0;
    void printCounter(){
        System.out.println(count);
    }

    synchronized void increment(){
        count++;
    }

    synchronized void decrement(){
        count--;
    }

}
