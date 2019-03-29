package fr.univ.nantes.impl;

import fr.univ.nantes.except.AbortCommitException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteTransaction<T> implements Transaction<T> {

    private List<ConcreteRegister<T>> localReadSet = new ArrayList<>();
    private List<ConcreteRegister<T>> localWroteSet = new ArrayList<>();
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

    //begin a transaction
    @Override
    public void begin() {

        //clear the local value of all the register
        for (ConcreteRegister<T> registerRead: localReadSet){
            registerRead.clearLocal();
        }
        //and remove all the register from the list
        localReadSet.clear();

        for (ConcreteRegister<T> registerWrite: localWroteSet){
            registerWrite.clearLocal();
        }
        localWroteSet.clear();

        this.birthDate = clock.get();
        this.committed = false;
    }

    //try to commit a transaction
    @Override
    public void try_to_commit() throws AbortCommitException {

        //in order to avoid deadLocks, we sort the register set by id
        List<ConcreteRegister<T>> registers = new ArrayList<>(localReadSet);
        registers.addAll(localWroteSet);
        registers.sort(Comparator.comparingInt(ConcreteRegister::getId));

        //we lock all the registers
        for (ConcreteRegister<T> register1 : registers) {
            register1.lock();
        }

        //If the registers have been modfied by another thread since the transaction started we abort
        for (ConcreteRegister<T> registerRead: localReadSet) {

            if (registerRead.getDate() > birthDate) {

                for (ConcreteRegister<T> register : registers) {
                    register.unlock();
                }
                throw new AbortCommitException(Thread.currentThread().getId());
            }
        }

        committed = true;

        //if we can commit, the we modify the value of the sets
        for (Register<T> registerWrite: localWroteSet) {
            registerWrite.setValue(registerWrite.getLocalValue());
            registerWrite.setDate(clock.incrementAndGet());
        }

        //and we unlock them
        for (ConcreteRegister<T> register : registers) {
            register.unlock();
        }

    }

    @Override
    public boolean isCommitted() {
        return committed;
    }
}
