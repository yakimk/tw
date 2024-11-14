package lab5;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MandelbrotTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int[] threadCounts = {1, 2, Runtime.getRuntime().availableProcessors(), 4 * Runtime.getRuntime().availableProcessors()};
        int numTests = 10;
        int width = 800;
        int height = 600;

        System.out.println("MAX_ITER: " + 10000);
        System.out.println("Liczba wątków | Liczba zadań | Średni czas (ms) | Odchylenie standardowe (ms)");

        for (int numThreads : threadCounts) {

            if (numThreads != 1) {
                runTest(numThreads, 1, numTests);

            }
            runTest(numThreads, numThreads, numTests);

            runTest(numThreads, numThreads * 10, numTests);

            runTest(numThreads, width * height, numTests);
        }
    }

    private static void runTest(int numThreads, int numTasks, int numTests) throws InterruptedException, ExecutionException {
        ArrayList<Long> times = new ArrayList<>();

        for (int i = 0; i < numTests; i++) {
            MandelbrotConcurrent mandelbrot = new MandelbrotConcurrent(numThreads, numTasks);

            long startTime = System.nanoTime();
            mandelbrot.generateMandelbrot();
            long endTime = System.nanoTime();

            times.add(endTime - startTime);
        }

        double averageTime = times.stream().mapToLong(Long::longValue).average().orElse(0.0) / 1_00_000.0;
        double stdDev = Math.sqrt(times.stream()
                .mapToDouble(t -> Math.pow((t / 1_000_00.0) - averageTime, 2))
                .average().orElse(0.0));

        System.out.printf("%13d | %11d | %15.3f | %23.3f%n", numThreads, numTasks, averageTime, stdDev);
    }
}

