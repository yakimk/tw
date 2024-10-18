package a;

public class Solution3 {
    public static void main(String[] args) {
        Philosophers table = new Philosophers();

        for (int i = 0; i < 5; i++) {
            final int philosopher = i;
            new Thread(() -> {
                try {
                    while (true) {
                        table.think(philosopher + 1);
//                        table.eat(philosopher + 1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
