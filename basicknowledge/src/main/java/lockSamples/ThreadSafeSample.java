package main.java.lockSamples;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadSafeSample {
    public int sharedState;
    public void nonSafeAction() {
        while (sharedState < 100000) {
            int former = sharedState++;
            int latter = sharedState;
            if (former != latter -1) {
                System.out.printf("Observed data race, former is " + former + ", "+"latter is " + latter);
            }
        }
    }
    public static void ReentrantLockSample() {
        ReentrantLock fairLock = new ReentrantLock(true); //创建公平锁，平时不需要
        fairLock.lock();
        try {
            int s = 1;
        } finally {
            fairLock.unlock();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        final ThreadSafeSample sample = new ThreadSafeSample();
        Thread threadA = new Thread() {
            public void run() {
                sample.nonSafeAction();
            }
        };
        Thread threadB = new Thread() {
            public void run() {
                sample.nonSafeAction();
            }
        };
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
    }

}
