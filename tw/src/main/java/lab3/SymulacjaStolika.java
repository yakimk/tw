package lab3;

public class SymulacjaStolika {
    public static void main(String[] args) {
        int liczbaPar = 3; // Liczba par osób
        Kelner kelner = new Kelner(liczbaPar);

        for (int i = 0; i < liczbaPar; i++) {
            new Osoba(kelner, i, 1).start(); // Osoba 1 z pary i
            new Osoba(kelner, i, 2).start(); // Osoba 2 z pary i
        }
    }
}
