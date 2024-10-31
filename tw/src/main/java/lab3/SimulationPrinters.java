package lab3;

public class SimulationPrinters {
    public static void main(String[] args) {
        int liczbaDrukarek = 3;
        int liczbaWatkow = 5;

        Monitor monitor = new Monitor(liczbaDrukarek);

        for (int i = 0; i < liczbaWatkow; i++) {
            new User(monitor, i).start();
        }
    }
}
