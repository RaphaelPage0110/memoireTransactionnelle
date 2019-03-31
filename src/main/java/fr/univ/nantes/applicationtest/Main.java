package fr.univ.nantes.applicationtest;

import fr.univ.nantes.TL2.impl.ConcreteRegister;

import java.util.concurrent.atomic.AtomicInteger;

class Main {

  public static void main(String[] argv){

    AtomicInteger clock = new AtomicInteger(0);
    AtomicInteger idCount = new AtomicInteger(0);
    ConcreteRegister<Integer> partage = new ConcreteRegister<>(clock.get(), 1, idCount.getAndIncrement());
    ConcreteRegister<Integer> partage2 = new ConcreteRegister<>(clock.get(), 1, idCount.getAndIncrement());

    Thread[] threads = new Thread[10];

    for (int i=0; i < threads.length ;i++){
      threads[i] = new Thread(new MyThreadTest(clock, partage, partage2));
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
