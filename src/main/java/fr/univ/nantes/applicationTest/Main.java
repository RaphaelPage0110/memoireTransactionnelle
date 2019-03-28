package fr.univ.nantes.applicationTest;

import fr.univ.nantes.except.AbortException;
import fr.univ.nantes.impl.ConcreteRegister;
import fr.univ.nantes.impl.ConcreteTransaction;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String argv[]){

        AtomicInteger clock = new AtomicInteger(0);
        AtomicInteger idCount = new AtomicInteger(0);
        ConcreteRegister<Integer> partage = new ConcreteRegister<Integer>(clock.get(), 0, idCount.getAndIncrement());

        Thread threads[] = new Thread[10000];

        for (int i=0; i < threads.length ;i++){
            threads[i] = new Thread(){

                public void run() {


                        ConcreteTransaction transaction = new ConcreteTransaction(clock);
                        while(!transaction.isCommitted())
                        {
                            try{
                                transaction.begin();

                                int j = partage.read(transaction);
                                System.out.println("Je lis une valeur de j de "+j);
                                j++;
                                partage.write(transaction, j);

                                transaction.try_to_commit();
                            } catch(AbortException e){
                                e.printStackTrace();
                            }

                        }
                }
            };

            threads[i].start();
        }
    }


}
