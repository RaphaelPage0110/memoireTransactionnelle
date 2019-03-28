package fr.univ.nantes.impl;

import fr.univ.nantes.except.AbortException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteTransaction<T> implements Transaction<T> {

    private List<ConcreteRegister<T>> localReadSet = new ArrayList<ConcreteRegister<T>>();
    private List<ConcreteRegister<T>> localWroteSet = new ArrayList<ConcreteRegister<T>>();
    private int birthDate;
    private AtomicInteger clock;
    private boolean committed;

    public ConcreteTransaction(AtomicInteger clock) {
        this.clock = clock;
        committed = false;
    }

    public void addWrittenRegister(ConcreteRegister<T> r){
        localWroteSet.add(r);
    }

    public void addReadRegister(ConcreteRegister<T> r){ localReadSet.add(r);}

    public int getBirthDate(){
        return birthDate;
    }

    @Override
    public void begin() {
        localReadSet.clear();
        localWroteSet.clear();
        this.birthDate = clock.get();
        this.committed = false;
    }

    @Override
    public void try_to_commit() throws AbortException {

        //in order to avoid deadBlock, we sort the register set
        List<ConcreteRegister<T>> registers = new ArrayList<>(localReadSet);
        registers.addAll(localWroteSet);
        registers.sort(Comparator.comparingInt(ConcreteRegister::getId));

        for (ConcreteRegister<T> register1 : registers) {
            register1.lock();
        }

        for (ConcreteRegister<T> registerRead: localReadSet) {

            if (registerRead.getDate() > birthDate) {

                for (ConcreteRegister<T> register : registers) {
                    register.clearLocal();
                    register.unlock();
                }
                throw new AbortException();
            }
        }

        committed = true;
        int commitDate = clock.incrementAndGet();

        for (Register<T> registerWrite: localWroteSet) {
            registerWrite.setValue(registerWrite.getLocalValue());
            registerWrite.setDate(commitDate);
            System.out.println("J'ecris la nouvelle valeur de j: " + registerWrite.getLocalValue());
        }

        for (ConcreteRegister<T> register : registers) {
            register.clearLocal();
            register.unlock();
        }

    }

    @Override
    public boolean isCommitted() {
        return committed;
    }
}
