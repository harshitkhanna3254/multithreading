package basics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Exercise1 {
    public static final int MAX_VAL = 9999;

    public static void main(String[] args) {
        Random random = new Random();

        Vault vault = new Vault(random.nextInt(MAX_VAL));

        Thread ascendingThread = new AscendingThread(vault);
        Thread descendingThread = new DescendingThread(vault);
        Thread policeThread = new PoliceThread();

        List<Thread> threads = new ArrayList<>();
        threads.add(ascendingThread);
        threads.add(descendingThread);
        threads.add(policeThread);
        

        for(Thread thread: threads) {
            thread.start();
        }
    }

    private static class Vault {
        int password;

        public Vault(int val) {
            this.password = val;
        }

        boolean checkPassword(int guess) {

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return this.password == guess;
        }
    }

    private static abstract class HackerThread extends Thread {
        Vault vault;

        public HackerThread(Vault vault) {
            this.vault = vault;
            setName(getClass().getSimpleName());
            setPriority(MAX_PRIORITY);
        }

        @Override
        public void start() {
            System.out.println("starting thread: " + getName());
            super.start();
        }
    }

    private static class AscendingThread extends HackerThread {
        public AscendingThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for(int i=0; i<MAX_VAL; i++) {
                // System.out.println("Asc: " + i);
                if(vault.checkPassword(i)) {
                    System.out.println(getName() + " guessed the password: " + i);
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingThread extends HackerThread {
        public DescendingThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for(int i=MAX_VAL; i >= 0; i--) {
                // System.out.println("Dsc: " + i);
                if(vault.checkPassword(i)) {
                    System.out.println(getName() + " guessed the password: " + i);
                    System.exit(0);
                }
            }
        }
    }

    private static class PoliceThread extends Thread {

        public PoliceThread() {
            setName(getClass().getSimpleName());
        }

        @Override
        public synchronized void start() {
            System.out.println("starting thread: " + getName());
            super.start();
        }

        @Override
        public void run() {
            for(int i=10; i>0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }

            System.out.println("Game over for you hackers");
            System.exit(0);
        }
    }

}
