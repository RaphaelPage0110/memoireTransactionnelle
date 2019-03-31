package fr.univ.nantes.applicationtest;

import fr.univ.nantes.TL2.except.AbortCommitException;
import fr.univ.nantes.TL2.except.AbortReadingException;
import fr.univ.nantes.TL2.impl.ConcreteRegister;
import fr.univ.nantes.TL2.impl.ConcreteTransaction;
import fr.univ.nantes.TL2.inter.Transaction;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadTest implements Runnable {

  private AtomicInteger clock;
  private ConcreteRegister<Integer> partage;

  MyThreadTest(AtomicInteger clock, ConcreteRegister<Integer> register){
    this.clock = clock;
    this.partage = register;
  }

  @Override
  public void run(){
    Transaction<Integer> transaction = new ConcreteTransaction<>(clock);

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
  }
}
