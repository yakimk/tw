package lab3;

class User extends Thread {
    private final Monitor monitor;
    private final int id;

    public User(Monitor monitor, int id) {
        this.monitor = monitor;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            utworzZadanieDoDruku();

            try {
                int nrDrukarki = monitor.zarezerwuj();
                System.out.println("Wątek " + id + " drukuje na drukarce nr " + nrDrukarki);
                drukuj(nrDrukarki);
                monitor.zwolnij(nrDrukarki);
                System.out.println("Wątek " + id + " zwolnił drukarkę nr " + nrDrukarki);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Wątek " + id + " został przerwany");
                break;
            }
        }
    }

    private void utworzZadanieDoDruku() {
        System.out.println("Wątek " + id + " tworzy zadanie do druku.");
        try {
            Thread.sleep((int) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void drukuj(int nrDrukarki) {
        System.out.println("Wątek " + id + " rozpoczął drukowanie na drukarce nr " + nrDrukarki);
        try {
            Thread.sleep((int) (Math.random() * 2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
