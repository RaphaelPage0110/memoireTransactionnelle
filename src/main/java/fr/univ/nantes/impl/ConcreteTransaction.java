package fr.univ.nantes.impl;

import com.sun.org.apache.xerces.internal.dom.AbortException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteTransaction implements Transaction {

    private List<Register> localReadedSet;
    private List<Register> localWritedSet;
    private int birthDate;
    private AtomicInteger clock;

    public ConcreteTransaction(AtomicInteger clock) {
        this.clock = clock;
    }

    @Override
    public void begin() {
        localReadedSet.clear();
        localWritedSet.clear();
        this.birthDate = clock.get();
    }

    @Override
    public void try_to_commit() throws AbortException {
        for (Register registerRead: localReadedSet) {
            registerRead.lock();
        }

        for (Register registerWrite: localWritedSet) {
            registerWrite.lock();
        }

        for (Register registerRead: localReadedSet) {
            if (registerRead.getDate() > birthDate) {
                throw new AbortException();
            }
        }

        commitDate = clock.getAndIncrement();

        for (Register registerWrite: localWritedSet) {
            registerWrite.setValue();
            registerWrite.setDate();
        }
    }

    @Override
    public boolean isCommited() {
        return false;
    }
}
