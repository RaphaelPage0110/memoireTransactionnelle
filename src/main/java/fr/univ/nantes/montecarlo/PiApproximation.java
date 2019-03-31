package fr.univ.nantes.montecarlo;

import fr.univ.nantes.TL2.impl.ConcreteRegister;
import fr.univ.nantes.TL2.inter.Register;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A short application to calculate a pi approximation by the monte carlo method.
 */
class PiApproximation {

  /**
   *Asks for the number of throws, and displays an approximation of pi.
   * @param args program arguments.
   */
  public static void main (String[] args) {

    Scanner reader = new Scanner(System.in);
    System.out.println("This program approximates PI using the Monte Carlo method.\nIt simulates throwing darts at a " +
            "dartboard.\nPlease enter number of throws: ");
    int numThrows = reader.nextInt();
    double PI = computePI(numThrows);

    // Determine the difference from the PI constant defined in Math
    double difference = PI - Math.PI;

    // Print out the total results of our trials
    System.out.println ("Number of throws = " + numThrows + ", Computed PI = " + PI + ", difference = " + difference );
  }


  /**
   * Calculates PI based on the number of throws versus misses.
   * @param numThrows the number of throws.
   * @return the approximation of pi.
   */
  private static double computePI (int numThrows) {
    Random randomGen = new Random (System.currentTimeMillis());
    double pi;

    AtomicInteger clock = new AtomicInteger(0);
    AtomicInteger idCount = new AtomicInteger(0);
    Register<Integer> hits = new ConcreteRegister<>(clock.get(), 0, idCount.getAndIncrement());

    Thread[] threads = new Thread[numThrows];

    for (int i=0; i < threads.length ;i++) {

      threads[i] = new Thread(new MyThreadsMonte(clock, hits, randomGen));

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

    // Use Monte Carlo method formula
    pi = (4.0 * (hits.getValue()/(double)numThrows));

    return pi;
  }
}
