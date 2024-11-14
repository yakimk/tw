package lab5;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class MandelbrotConcurrent extends JFrame {
    private final int MAX_ITER = 10000;
    private final double ZOOM = 150;
    private BufferedImage image;
    private ExecutorService executor;
    private int numTasks;

    public MandelbrotConcurrent(int numThreads, int numTasks) {
        super("Mandelbrot Set - Concurrent");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        executor = Executors.newFixedThreadPool(numThreads);
        this.numTasks = numTasks;
    }

    private int computePixelColor(int x, int y) {
        double zx = 0, zy = 0, cX = (x - 400) / ZOOM, cY = (y - 300) / ZOOM, tmp;
        int iter = MAX_ITER;
        while (zx * zx + zy * zy < 4 && iter > 0) {
            tmp = zx * zx - zy * zy + cX;
            zy = 2.0 * zx * zy + cY;
            zx = tmp;
            iter--;
        }
        return iter | (iter << 8);
    }

    public void generateMandelbrot() throws InterruptedException, ExecutionException {
        int width = getWidth();
        int height = getHeight();
        int totalPixels = width * height;
        int pixelsPerTask = totalPixels / numTasks;
        if (pixelsPerTask == 1) {
            Future<?>[][] futures = new Future[width][height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final int px = x;
                    final int py = y;
                    futures[x][y] = executor.submit(() -> {
                        int color = computePixelColor(px, py);
                        image.setRGB(px, py, color);
                    });
                }
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    futures[x][y].get();
                }
            }
            executor.shutdown();
            return;
        }

        List<int[]> pixels = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels.add(new int[]{x, y});
            }
        }

        Collections.shuffle(pixels);

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            final List<int[]> taskPixels = pixels.subList(i * pixelsPerTask, (i + 1) * pixelsPerTask);

            tasks.add(() -> {
                for (int[] pixel : taskPixels) {
                    int x = pixel[0];
                    int y = pixel[1];
                    int color = computePixelColor(x, y);
                    image.setRGB(x, y, color);
                }
                return null;
            });
        }

        List<Future<Void>> futures = executor.invokeAll(tasks);

        for (Future<Void> future : futures) {
            future.get();
        }

        executor.shutdown();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        int numTasks = 10;

        MandelbrotConcurrent mandelbrot = new MandelbrotConcurrent(numThreads, numTasks);

        long startTime = System.nanoTime();
        mandelbrot.generateMandelbrot();
        long endTime = System.nanoTime();
        mandelbrot.setVisible(true);

        System.out.printf("Czas generacji: %.3f ms%n", (endTime - startTime) / 1_000_000.0);
    }
}
