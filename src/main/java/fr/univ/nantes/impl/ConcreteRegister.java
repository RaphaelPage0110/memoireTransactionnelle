package fr.univ.nantes.impl;

import fr.univ.nantes.except.AbortReadingException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;

import java.util.concurrent.locks.ReentrantLock;

/**
 * This Register implementation is a way to read and write the value of a register.
 *
 * @param <T> the type of value contained in the registers.
 *
 * @author Raphael PAGE
 * @author Glenn PLOUHINEC
 */
public class ConcreteRegister<T> extends ReentrantLock implements Register<T> {

    /**
     * The ID of the register. Should be unique in the application.
     */
    private final int id;

    /**
     * The date of the last writing in the register.
     */
    private Integer date;

    /**
     * The value of the last writing in the register.
     */
    private T value;

    /**
     * The local copy of the register value, specific to each thread.
     */
    private final ThreadLocal<T> lc_value = new ThreadLocal<>();

    /**
     * The local copy of the register date, specific to each thread.
     */
    private final ThreadLocal<Integer> lc_date = new ThreadLocal<>();

    /**
     * ConcreteRegister constructor, initializes the id, date and value of the register, but not the local copies.
     * @param date the initial date.
     * @param value the initial value.
     * @param id the ID.
     */
    public ConcreteRegister(Integer date, T value, int id) {
        this.id = id;
        this.date = date;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public Integer getDate() {
        return this.date;
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public void setDate(Integer date) {
        this.date = date;
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public T getValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public T getLocalValue() {
        return this.lc_value.get();
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public void clearLocal() {
        this.lc_date.remove();
        this.lc_value.remove();
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public T read(Transaction<T> transaction) throws AbortReadingException {

        //if the value has already been copied during the transaction
        //there is no need to copy it again
        if (this.lc_value.get() != null) {
            return this.lc_value.get();
        } else {
            this.lc_value.set(this.value);
            this.lc_date.set(this.date);
            transaction.addReadRegister(this);

            //if the register have been modified after the transaction began we abort
            if(this.lc_date.get() > transaction.getBirthDate()) {
                throw new AbortReadingException(Thread.currentThread().getId());
            } else {
                return this.lc_value.get();
            }
        }
    }

    /**
     * {@inheritDoc}
     * @see Register
     */
    @Override
    public void write(Transaction<T> transaction, T value) {
        this.lc_value.set(value);
        transaction.addWrittenRegister(this);
    }

}
