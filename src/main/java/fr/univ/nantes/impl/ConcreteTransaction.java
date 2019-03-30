package fr.univ.nantes.impl;

import fr.univ.nantes.except.AbortCommitException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This Transaction implementation is a way to manage write and read access to registers in a consistent way.
 *
 * @param <T> the type of value contained in the registers that we manage.
 *
 * @author Raphael PAGE
 * @author Glenn PLOUHINEC
 * @see Transaction
 */
public class ConcreteTransaction<T> implements Transaction<T> {

    /**
     * This is the set of registers where reading operations are performed.
     */
    private List<Register<T>> localReadSet = new ArrayList<>();

    /**
     * This is the set of registers where writing operations are performed.
     */
    private List<Register<T>> localWroteSet = new ArrayList<>();

    /**
     * This is the start date of the transaction, initialized by the general clock, in begin().
     */
    private int birthDate;

    /**
     * This is the general system clock.
     */
    private AtomicInteger clock;

    /**
     * Indicates whether a transaction has been committed.
     */
    private boolean committed;

    /**
     * ConcreteTransaction constructor, initializes the clock and the committed attribute.
     * @param clock the general system clock.
     */
    public ConcreteTransaction(AtomicInteger clock) {
        this.clock = clock;
        this.committed = false;
    }

    /**
     * {@inheritDoc}
     * @see Transaction
     */
    @Override
    public int getBirthDate() {
        return this.birthDate;
    }

    /**
     * {@inheritDoc}
     * @see Transaction
     */
    @Override
    public boolean isCommitted() {
        return this.committed;
    }

    /**
     * {@inheritDoc}
     * @see Transaction
     */
    @Override
    public void addWrittenRegister(Register<T> newRegister) {
        this.localWroteSet.add(newRegister);
    }

    /**
     * {@inheritDoc}
     * @see Transaction
     */
    @Override
    public void addReadRegister(Register<T> newRegister) {
        this.localReadSet.add(newRegister);
    }

    /**
     * {@inheritDoc}
     * @see Transaction
     */
    @Override
    public void begin() {

        //clear the local value of all the register
        for (Register<T> registerRead: this.localReadSet) {
            registerRead.clearLocal();
        }
        //and remove all the register from the list
        this.localReadSet.clear();

        for (Register<T> registerWrite: this.localWroteSet) {
            registerWrite.clearLocal();
        }
        this.localWroteSet.clear();

        this.birthDate = this.clock.get();
        this.committed = false;
    }

    /**
     * {@inheritDoc}
     * @see Transaction
     */
    @Override
    public void try_to_commit() throws AbortCommitException {

        //in order to avoid deadLocks, we sort the register set by id
        List<Register<T>> registers = new ArrayList<>(this.localReadSet);
        registers.addAll(this.localWroteSet);
        registers.sort(Comparator.comparingInt(Register::getId));

        //we lock all the registers
        for (Register<T> register : registers) {
            register.lock();
        }

        //If the registers have been modfied by another thread since the transaction started we abort
        for (Register<T> registerRead: this.localReadSet) {

            if (registerRead.getDate() > this.birthDate) {

                for (Register<T> register : registers) {
                    register.unlock();
                }
                throw new AbortCommitException(Thread.currentThread().getId());
            }
        }

        this.committed = true;

        //if we can commit, the we modify the value of the sets
        for (Register<T> registerWrite : this.localWroteSet) {
            registerWrite.setValue(registerWrite.getLocalValue());
            registerWrite.setDate(this.clock.incrementAndGet());
        }

        //and we unlock them
        for (Register<T> register : registers) {
            register.unlock();
        }
    }
}
