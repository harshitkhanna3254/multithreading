package basics;

import java.math.BigInteger;

public class Exercise2 {
    public static void main(String[] args) {
        BigInteger base = new BigInteger("2");
        BigInteger pow = new BigInteger("100000");
        
        Thread lc = new Thread(new LongComputationTask(base, pow));
        lc.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        lc.interrupt();
    }

    static class LongComputationTask implements Runnable {

        BigInteger base;
        BigInteger pow;

        public LongComputationTask(BigInteger base, BigInteger pow) {
            this.base = base;
            this.pow = pow;
        }

        @Override
        public void run() {
            System.out.println(this.base + "^" + this.pow + " = " + calculatePower(this.base, this.pow));
        }

        private BigInteger calculatePower(BigInteger base, BigInteger pow) {
            BigInteger result = BigInteger.ONE;

            if(Thread.currentThread().interrupted()) {
                System.out.println("Interrupted");
                return BigInteger.ZERO;
            }

            for(BigInteger i=BigInteger.ZERO; i.compareTo(pow) != 0; i=i.add(BigInteger.ONE))
                result = result.multiply(base);
            
            return result;
        }
    }
}
