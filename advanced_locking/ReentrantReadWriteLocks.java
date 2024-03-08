package advanced_locking;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ReentrantReadWriteLocks {
    public static final int HIGHEST_PRICE = 10000;

    public static void main(String[] args) throws InterruptedException {
        // Your code here
        Inventory inventory = new Inventory();

        Random random = new Random();
        for (int i = 0; i < 1_00_000; i++) {
            inventory.addItem(random.nextInt(HIGHEST_PRICE));
        }

        Thread writerThread = new Thread(() -> {
            while (true) {
                inventory.addItem(random.nextInt(HIGHEST_PRICE));
                inventory.removeItem(random.nextInt(HIGHEST_PRICE));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        writerThread.setDaemon(true);
        writerThread.start();

        int numReaderThreads = 7;
        List<Thread> readerThreadList = new ArrayList();

        for (int i = 0; i < numReaderThreads; i++) {
            Thread reader = new Thread(() -> {
                for (int j = 0; j < 1_00_000; j++) {
                    int ubp = random.nextInt(HIGHEST_PRICE);
                    int lbp = ubp > 0 ? random.nextInt(ubp) : 0;

                    inventory.getCountWithinPriceRange(lbp, ubp);

                }
            });
            readerThreadList.add(reader);
        }

        long startReadTime = System.currentTimeMillis();
        for (Thread reader : readerThreadList)
            reader.start();

        for (Thread reader : readerThreadList)
            reader.join();

        long endReadTime = System.currentTimeMillis();

        System.out.println("Total read time: " + (endReadTime - startReadTime));
    }

    public static class Inventory {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();

        /* approach 1 */
        ReentrantLock reentrantLock = new ReentrantLock();

        /* approach 2 */
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        ReadLock readLock = reentrantReadWriteLock.readLock();
        WriteLock writeLock = reentrantReadWriteLock.writeLock();

        public int getCountWithinPriceRange(int lowerBound, int upperBound) {
            reentrantLock.lock();
            try {
                int fromKey = treeMap.ceilingKey(lowerBound);
                int toKey = treeMap.floorKey(upperBound);

                NavigableMap<Integer, Integer> navigableMap = treeMap.subMap(fromKey, true, toKey, true);
                int sum = 0;

                for (int count : navigableMap.values())
                    sum += count;

                return sum;
            } finally {
                reentrantLock.unlock();
            }
        }

        public void addItem(int price) {
            reentrantLock.lock();
            try {
                this.treeMap.put(price, treeMap.getOrDefault(price, 0) + 1);

            } finally {
                reentrantLock.unlock();
            }
        }

        public void removeItem(int price) {
            reentrantLock.lock();
            try {

                if (this.treeMap.containsKey(price)) {
                    int count = this.treeMap.get(price);

                    if (count > 1)
                        this.treeMap.put(price, count - 1);
                    else
                        this.treeMap.remove(price);
                } else {
                    System.out.println("Sorry item not present");
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
