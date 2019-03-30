package fr.univ.nantes.applicationTest;

import fr.univ.nantes.carlo.PiMonteCarlo;
import fr.univ.nantes.except.AbortCommitException;
import fr.univ.nantes.except.AbortReadingException;
import fr.univ.nantes.impl.ConcreteRegister;
import fr.univ.nantes.impl.ConcreteTransaction;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String argv[]){

//        AtomicInteger clock = new AtomicInteger(0);
//        AtomicInteger idCount = new AtomicInteger(0);
//
//        Register<Integer> partage = new ConcreteRegister<Integer>(clock.get(), 0, idCount.getAndIncrement());
//        Register<Integer> partage2 = new ConcreteRegister<Integer>(clock.get(), 1000, idCount.getAndIncrement());
//
//        Thread threads[] = new Thread[1000];
//
//        for (int i=0; i < threads.length ;i++){
//            threads[i] = new Thread(() -> {
//
//                    Transaction<Integer> transaction = new ConcreteTransaction<Integer>(clock);
//
//                    while(!transaction.isCommitted()) {
//
//                        try {
//                            transaction.begin();
//
//                            int j = partage.read(transaction);
//                            int k = partage2.read(transaction);
//
//                            int oldValueJ = j;
//                            j++;
//                            int oldValueK = k;
//                            k++;
//
//                            partage.write(transaction, j);
//                            partage.read(transaction);
//                            partage2.write(transaction,k);
//                            partage2.read(transaction);
//
//                            transaction.try_to_commit();
//
//                            System.out.println("Je "+Thread.currentThread().getId()+" lis une valeur de j de " + oldValueJ + "\t et k de " + oldValueK);
//                            System.out.println("Je "+Thread.currentThread().getId()+" écris une valeur de j de " + j + "\t et k de " + k);
//                            System.out.println("Je "+Thread.currentThread().getId()+" ai réussi à commit");
//                        } catch(AbortCommitException | AbortReadingException e){
//                            e.printStackTrace();
//                        }
//
//                    }
//            });
//
//        }
//
//        for (Thread thread : threads) {
//            thread.start();
//        }
//
//        //we make sure all the threads finish
//        for (Thread thread : threads) {
//            try {
//                thread.join();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println(partage.getValue());
        PiMonteCarlo pi = new PiMonteCarlo(100000);
        System.out.println(pi.getPi());
    }


}
