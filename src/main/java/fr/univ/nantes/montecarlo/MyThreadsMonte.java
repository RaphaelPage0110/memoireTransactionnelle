package fr.univ.nantes.montecarlo;

import fr.univ.nantes.TL2.except.AbortCommitException;
import fr.univ.nantes.TL2.except.AbortReadingException;
import fr.univ.nantes.TL2.impl.ConcreteRegister;
import fr.univ.nantes.TL2.impl.ConcreteTransaction;
import fr.univ.nantes.TL2.inter.Transaction;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadsMonte implements Runnable {

  private AtomicInteger clock;
  private ConcreteRegister<Integer> hits;
  private Random randomGen;

  MyThreadsMonte(AtomicInteger clock, ConcreteRegister<Integer> register, Random randomGen){
    this.clock = clock;
    this.hits = register;
    this.randomGen = randomGen;
  }

  @Override
  public void run(){
    Transaction<Integer> transaction = new ConcreteTransaction<>(clock);
    // Create a random coordinate result to test
    double xPos = (randomGen.nextDouble()) * 2 - 1.0;
    double yPos = (randomGen.nextDouble()) * 2 - 1.0;

    // Was the coordinate hitting the dart board?
    if (isInside(xPos, yPos)) {
      while(!transaction.isCommitted()) {

        try {
          transaction.begin();
          int j = hits.read(transaction);
          j++;
          hits.write(transaction, j);
          transaction.try_to_commit();
        } catch(AbortCommitException | AbortReadingException e) {
          //e.printStackTrace();
        }

      }
    }
  }

  /**
   * Determines if dart thrown is inside the dart board.
   * @param xPos the abscissa of the dart.
   * @param yPos the ordinate of the dart.
   * @return true if the dart thrown is inside the dart board.
   */
  private static boolean isInside (double xPos, double yPos) {
    double distance = Math.sqrt((xPos * xPos) + (yPos * yPos));

    return (distance < 1.0);
  }
}
