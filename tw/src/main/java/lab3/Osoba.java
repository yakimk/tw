package lab3;

class Osoba extends Thread {
    private final Kelner kelner;
    private final int paraId;
    private final int osobaId;

    public Osoba(Kelner kelner, int paraId, int osobaId) {
        this.kelner = kelner;
        this.paraId = paraId;
        this.osobaId = osobaId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                wlasneSprawy();
                System.out.println("Osoba " + osobaId + " z pary " + paraId + " chce stolik.");
                kelner.chce_stolik(paraId);
                System.out.println("Osoba " + osobaId + " z pary " + paraId + " je.");
                jedzenie();
                kelner.zwalniam(paraId);
                System.out.println("Osoba " + osobaId + " z pary " + paraId + " zwolniła stolik.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Osoba " + osobaId + " z pary " + paraId + " została przerwana.");
        }
    }

    private void wlasneSprawy() {
        try {
            Thread.sleep((int) (Math.random() * 5000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void jedzenie() {
        try {
            Thread.sleep((int) (Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
