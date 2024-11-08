package lab4;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class PipelineMonitor {
        final int size = 100;
        final int producerCount = 5;
        final int consumerCount = 10;
        private int[] productionLine = new int[size];
        private final Lock lock = new ReentrantLock();
        private Condition[] producerConditions = new Condition[producerCount];

        // Condition where a number of consumers are waiting for the first call when the last producer has finished
        // modifying the first cell
        private Condition consumersCondition = lock.newCondition();

        // Condition where the current consumer (that was called when the last producer has finished
        // modifying the first cell) is called each time the last producer finishes modifying the next cell
        private Condition consumerCondition = lock.newCondition();

        public PipelineMonitor() {
            for (int i = 0; i < size; i++) {
                productionLine[i] = 0;
            }

            for (int i = 0; i < producerCount; i++) {
                producerConditions[i] = lock.newCondition();
            }
        }

        public void produce(int producerId, int position) throws InterruptedException {
            lock.lock();
            try {

                while (productionLine[position] != producerId) {
                    producerConditions[producerId].await();
                }


            } finally {
                lock.unlock();
            }
        }

        public void startConsumption() throws InterruptedException {
            lock.lock();
            try {
                while (productionLine[0] != producerCount) {
                    consumersCondition.await();
                }


            } finally {
                lock.unlock();
            }
        }

        public void consume(int cell) throws InterruptedException {
            lock.lock();
            try {
                while (productionLine[cell] != producerCount) {
                    consumerCondition.await();
                }

                productionLine[cell] = 0;
                printBuffer();
                producerConditions[0].signal();
            } finally {
                lock.unlock();
            }
        }

        public void finishProduction(int producerId, int position) throws InterruptedException {
            lock.lock();
            try {
                productionLine[position]++;
                printBuffer();
                if (producerId == producerCount - 1) {
                    if (position == 0) {
                        consumersCondition.signal();
                    } else {
                        consumerCondition.signal();
                    }
                } else {
                    producerConditions[producerId + 1].signal();
                }
            }finally {
                lock.unlock();
            }

        }

        public void finishConsumption(int consumerId) throws InterruptedException {

        }

        public int nextCell(int currentCell) {
            if (currentCell == size - 1) {
                return 0;
            } else {
                return currentCell + 1;
            }
        }


        public void printBuffer(){
            System.out.println(Arrays.toString(productionLine));
        }

    }
