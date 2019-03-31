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
  private ConcreteRegister<Integer> partage2;

  MyThreadTest(AtomicInteger clock, ConcreteRegister<Integer> register, ConcreteRegister<Integer> register2){
    this.clock = clock;
    this.partage = register;
    this.partage2=register2;
  }

  @Override
  public void run(){
    Transaction<Integer> transaction = new ConcreteTransaction<>(clock);

    while(!transaction.isCommitted()) {

      try {
        transaction.begin();

        int j = partage.read(transaction);
        int h = partage2.read(transaction);
        int oldvalueJ = j;
        int oldvalueH = h;
        j = oldvalueJ+oldvalueH;
        h = oldvalueJ+oldvalueH;
        partage.write(transaction, j);
        partage2.write(transaction, h);

        transaction.try_to_commit();

        System.out.println("Je "+Thread.currentThread().getId()+" lis une valeur de j de "+oldvalueJ);
        System.out.println("Je "+Thread.currentThread().getId()+" lis une valeur de h de "+oldvalueH);
        System.out.println("Je "+Thread.currentThread().getId()+" écris une valeur de j de "+j);
        System.out.println("Je "+Thread.currentThread().getId()+" écris une valeur de j de "+h);
        System.out.println("Je "+Thread.currentThread().getId()+" ai réussi à commit");
      } catch(AbortCommitException | AbortReadingException e) {
        e.printStackTrace();
      }
    }
  }
}
