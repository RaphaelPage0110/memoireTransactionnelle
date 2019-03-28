package fr.univ.nantes.impl;

import com.sun.org.apache.xerces.internal.dom.AbortException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteTransaction implements Transaction {

    private List<ConcreteRegister> localReadSet = new ArrayList<ConcreteRegister>();
    private List<ConcreteRegister> localWroteSet = new ArrayList<ConcreteRegister>();
    private int birthDate;
    private AtomicInteger clock;
    private boolean committed;

    public ConcreteTransaction(AtomicInteger clock) {
        this.clock = clock;
        committed = false;
    }

    public void addWrittenRegister(ConcreteRegister r){
        localWroteSet.add(r);
    }

    public void addReadRegister(ConcreteRegister r){ localReadSet.add(r);}

    public int getBirthDate(){
        return birthDate;
    }

    @Override
    public void begin() {
        localReadSet.clear();
        localWroteSet.clear();
        this.birthDate = clock.get();
    }

    @Override
    public void try_to_commit() throws AbortException {
        for (ConcreteRegister registerRead: localReadSet) {
            registerRead.lock();
        }

        for (ConcreteRegister registerWrite: localWroteSet) {
            registerWrite.lock();
        }

        for (Register registerRead: localReadSet) {
            if (registerRead.getDate() > birthDate) {
                throw new AbortException();
            }
        }

        int commitDate = clock.getAndIncrement();

        for (Register registerWrite: localWroteSet) {
            registerWrite.setValue(registerWrite.getLocalValue());
            registerWrite.setDate(commitDate);
        }

        for (ConcreteRegister registerRead: localReadSet) {
            registerRead.unlock();
        }

        for (ConcreteRegister registerWrite: localWroteSet) {
            registerWrite.unlock();
        }
    }

    @Override
    public boolean isCommitted() {
        return committed;
    }
}
