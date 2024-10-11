//package lab1;
//
//public class Producer implements Runnable {
//
//    private int count = 1000;
//    private Buffer buffer;
//
//    public Producer(Buffer buffer) {
//        this.buffer = buffer;
//    }
//
//    public void run() {
//
//        for(int i = 0;  i < count;   i++) {
//            buffer.put("message "+i);
//        }
//
//    }
//}
