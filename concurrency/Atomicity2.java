package multithreading.concurrency;

import java.util.Random;

public class Atomicity2 {

    public static void main(String[] args) {
        Metrics metrics = new Metrics();

        BusinessLogic businessLogic1 = new BusinessLogic(metrics);
        BusinessLogic businessLogic2 = new BusinessLogic(metrics);

        MetricsPrinter metricsPrinter = new MetricsPrinter(metrics);

        businessLogic1.start();
        businessLogic2.start();
        metricsPrinter.start();
    }

    static class MetricsPrinter extends Thread {
        private Metrics metrics;

        MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }

                System.out.println("Current average is: " + metrics.getAverage() + " miliseconds");
            }
        }
    }

    static class BusinessLogic extends Thread {
        private Metrics metrics;
        private Random random = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                }
                long end = System.currentTimeMillis();

                metrics.addSample(end - start);
            }
        }
    }

    static class Metrics {
        volatile long count = 0;
        volatile double average = 0;

        public synchronized void addSample(long sample) {
            double total = count * average;
            count++;
            average = (total + sample) / count;
        }

        public double getAverage() {
            return average;

        }
    }
}
