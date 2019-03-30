package fr.univ.nantes.applicationTest;

import fr.univ.nantes.except.AbortCommitException;
import fr.univ.nantes.except.AbortReadingException;
import fr.univ.nantes.impl.ConcreteRegister;
import fr.univ.nantes.impl.ConcreteTransaction;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String argv[]){

        AtomicInteger clock = new AtomicInteger(0);
        AtomicInteger idCount = new AtomicInteger(0);
        Register<Integer> partage = new ConcreteRegister<Integer>(clock.get(), 0, idCount.getAndIncrement());

        Thread threads[] = new Thread[10000];

        for (int i=0; i < threads.length ;i++){
            threads[i] = new Thread(() -> {
                Transaction<Integer> transaction = new ConcreteTransaction<Integer>(clock);

                while(!transaction.isCommitted()) {

                    try {
                        transaction.begin();

                        int j = partage.read(transaction);
                        int oldvalue = j;
                        j++;
                        partage.write(transaction, j);
                        partage.read(transaction);

                        transaction.try_to_commit();

                        System.out.println("Je "+Thread.currentThread().getId()+" lis une valeur de j de "+oldvalue);
                        System.out.println("Je "+Thread.currentThread().getId()+" écris une valeur de j de "+j);
                        System.out.println("Je "+Thread.currentThread().getId()+" ai réussi à commit");
                    } catch(AbortCommitException | AbortReadingException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        //we make sure all the threads finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(partage.getValue());
    }


}