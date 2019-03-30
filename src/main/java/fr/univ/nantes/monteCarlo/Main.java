package fr.univ.nantes.monteCarlo;

import fr.univ.nantes.except.AbortCommitException;
import fr.univ.nantes.except.AbortReadingException;
import fr.univ.nantes.impl.ConcreteRegister;
import fr.univ.nantes.impl.ConcreteTransaction;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main
{

  // Print a very basic program description and ask for number of throws

  public static void main (String[] args)
  {
    Scanner reader = new Scanner (System.in);
    System.out.println("This program approximates PI using the Monte Carlo method.");
    System.out.println("It simulates throwing darts at a dartboard.");
    System.out.print("Please enter number of throws: ");
    int numThrows = reader.nextInt();
    double PI = computePI(numThrows);

    // Determine the difference from the PI constant defined in Math
    double Difference = PI - Math.PI;

    // Print out the total results of our trials
    System.out.println ("Number of throws = " + numThrows + ", Computed PI = " + PI + ", Difference = " + Difference );
  }



  // Determine if dart thrown is inside the dart board

  public static boolean isInside (double xPos, double yPos)
  {
    double distance = Math.sqrt((xPos * xPos) + (yPos * yPos));

    return (distance < 1.0);
  }



  // Calculates PI based on the number of throws versus misses
  public static double computePI (int numThrows)
  {
    Random randomGen = new Random (System.currentTimeMillis());
    double PI = 0;

    AtomicInteger clock = new AtomicInteger(0);
    AtomicInteger idCount = new AtomicInteger(0);
    ConcreteRegister<Integer> hits = new ConcreteRegister<Integer>(clock.get(), 0, idCount.getAndIncrement());

    Thread threads[] = new Thread[numThrows];

    for (int i=0; i < threads.length ;i++){
      threads[i] = new Thread(() -> {
        ConcreteTransaction transaction = new ConcreteTransaction(clock);
        // Create a random coordinate result to test
        double xPos = (randomGen.nextDouble()) * 2 - 1.0;
        double yPos = (randomGen.nextDouble()) * 2 - 1.0;

        // Was the coordinate hitting the dart board?
        if (isInside(xPos, yPos))
        {
          while(!transaction.isCommitted())
          {
            try{
              transaction.begin();
              int j = hits.read(transaction);
              int oldvalue = j;
              j++;
              hits.write(transaction, j);
              transaction.try_to_commit();
            } catch(AbortCommitException | AbortReadingException e){
            }
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
      }
    }

    // Use Monte Carlo method formula
    PI = (4.0 * (hits.getValue()/(double)numThrows));

    return PI;
  }
}
