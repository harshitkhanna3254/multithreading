package multithreading.concurrency;

public class Atomicity {
    public static void main(String[] args) throws InterruptedException {

        InventoryCounter inventoryCounter = new InventoryCounter();

        IncrementThread incrementThread = new IncrementThread(inventoryCounter);
        DecrementThread decrementThread = new DecrementThread(inventoryCounter);

        incrementThread.start();
        decrementThread.start();

        incrementThread.join();
        decrementThread.join();

        System.out.println(inventoryCounter.getCount());
    }

    static class IncrementThread extends Thread {

        private InventoryCounter inventoryCounter;

        public IncrementThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                this.inventoryCounter.incrementCount();
            }
        }
    }

    static class DecrementThread extends Thread {

        private InventoryCounter inventoryCounter;

        public DecrementThread(InventoryCounter inventoryCounter) {
            this.inventoryCounter = inventoryCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                this.inventoryCounter.decrementCount();
            }
        }
    }

    static class InventoryCounter {
        private int count = 0;
        Object object1 = new Object();
        Object object2 = new Object();


        public void incrementCount() {
            synchronized (object1) {
                this.count++;
            }
        }

        public void decrementCount() {
            synchronized (object1) {
                this.count--;
            }
        }

        public int getCount() {
            return this.count;
        }
    }
}
