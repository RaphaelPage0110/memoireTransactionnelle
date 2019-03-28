package fr.univ.nantes.impl;

import com.sun.org.apache.xerces.internal.dom.AbortException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteTransaction implements Transaction {

    private List<Register> localReadSet = new ArrayList<Register>();
    private List<Register> localWroteSet = new ArrayList<Register>();
    private int birthDate;
    private AtomicInteger clock;
    private boolean committed;

    public ConcreteTransaction(AtomicInteger clock) {
        this.clock = clock;
        committed = false;
    }

    public void addWrittenRegister(Register r){
        localWroteSet.add(r);
    }

    public void addReadRegister(Register r){ localReadSet.add(r);}

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
        for (Register registerRead: localReadSet) {
            registerRead.lock();
        }

        for (Register registerWrite: localWroteSet) {
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
    }

    @Override
    public boolean isCommitted() {
        return committed;
    }
}
