package pl.edu.pg;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

public class DataSet {
    private SecretKey key; //potem jakos najlepiej nie przechowywac tego klucza
    private GCMParameterSpec iv;
    //private LinkedBlockingQueue<Query> queue = new LinkedBlockingQueue<>();//potem przeniesc do innej klasy
    //private final int NUMBERS_OF_THREADS = 3;
    //private ExecutorService producerExecutor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);
    //private ExecutorService consumerExecutor = Executors.newFixedThreadPool(NUMBERS_OF_THREADS);
    public DataSet() throws Exception {
        this.key = AESUtil.generateKey();
        this.iv = AESUtil.generateIV();
    }

    public void createData(String fileName) {
        long start_time = System.currentTimeMillis();
        Random random = new Random();
        ExecutorService executor = Executors.newFixedThreadPool(1); // szybciej na jednym i tak bo po kolei pisac trzeba
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            TestRepo.recursivelyApplyFunction(czlowiek -> {
                executor.submit(() -> {
                    try {
                        Query query;
                        if (random.nextInt(2) == 0) {
                            query = new Query(QueryType.ENCRYPT, czlowiek);
                        } else {
                            SealedObject sealedObject = AESUtil.encryptObject(czlowiek, key, iv);
                            query = new Query(QueryType.DECRYPT, sealedObject);
                        }
                        synchronized (oos) {
                            oos.writeObject(query);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end_time = System.currentTimeMillis();
        System.out.println("Czas wykonania: " + (end_time - start_time) + "ms");
    }

    public void processQueriesNoQueue(String fileName) {
        long start_time = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(1);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            boolean endOfFile = false;
            while (!endOfFile) {
                try {
                    Query query = (Query) ois.readObject();
                    executor.submit(() -> {
                        //System.out.println("Thread: " + Thread.currentThread().getName());
                        try {
                            if (query.getQueryType() == QueryType.DECRYPT) {
                                SealedObject sealedObject = (SealedObject) query.getData();
                                Czlowiek decryptedCzlowiek = (Czlowiek) AESUtil.decryptObject(sealedObject, key, iv);
                                //System.out.println("Decrypted: " + decryptedCzlowiek);
                            } else {
                                Czlowiek czlowiek = (Czlowiek) query.getData();
                                SealedObject sealedObject = AESUtil.encryptObject(czlowiek, key, iv);
                                //System.out.println("Encrypted: " + czlowiek);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (EOFException e) {
                    endOfFile = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end_time = System.currentTimeMillis();
        System.out.println("Czas wykonania: " + (end_time - start_time) + "ms");
    }

    /*public void produce(String fileName) { //jeszcze je trzeba jakos umiejetnie zamknac
        for (int i = 0;i<NUMBERS_OF_THREADS;i++) {
            producerExecutor.submit(() -> {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
                    boolean endOfFile = false;
                    while (!endOfFile) {
                        try {
                            Query query = (Query) ois.readObject();
                            queue.put(query);
                            System.out.println("Thread: " + Thread.currentThread().getName());
                        } catch (EOFException e) {
                            endOfFile = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void consume() {
        for(int i=0;i<NUMBERS_OF_THREADS;i++) {
            consumerExecutor.submit(() -> {
                while (true) {
                    try {
                        Query query = queue.take();
                        if (query.getQueryType() == QueryType.DECRYPT) {
                            SealedObject sealedObject = (SealedObject) query.getData();
                            Czlowiek decryptedCzlowiek = (Czlowiek) AESUtil.decryptObject(sealedObject, key, iv);
                            System.out.println("DECRYPTED: " + decryptedCzlowiek+
                                    " Thread: " + Thread.currentThread().getName());
                        } else {
                            Czlowiek czlowiek = (Czlowiek) query.getData();
                            SealedObject sealedObject = AESUtil.encryptObject(czlowiek, key, iv);
                            System.out.println("ENCRYPTED: " + czlowiek +
                                    " Thread: " + Thread.currentThread().getName());
                        }
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break; // Exit the loop if interrupted
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public ExecutorService getProducerExecutor() {
        return producerExecutor;
    }

    public ExecutorService getConsumerExecutor() {
        return consumerExecutor;
    }*/
}
