package lab2;

public class Solution1 {
    public static void main(String[] args) {
        Philosophers table = new Philosophers();

        for (int i = 0; i < 5; i++) {
            final int philosopher = i;
            new Thread(() -> {
                try {
                    while (true) {
                        table.think(philosopher);
                        table.eat1(philosopher);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
