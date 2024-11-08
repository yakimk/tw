package lab4;

public class Buffer {
    final int M = 10;
    private final int limitPortions = 2 * M;
    private int portions = 0;


    public boolean isValid(int numberOfPortions){
        return numberOfPortions <= M && numberOfPortions + portions <= limitPortions;
    }

    public void add(int portion){
        portions += portion;
    }

    public void remove(int portion){
        portions -= portion;
    }

    public int getPortions(){
        return portions;
    }

}
