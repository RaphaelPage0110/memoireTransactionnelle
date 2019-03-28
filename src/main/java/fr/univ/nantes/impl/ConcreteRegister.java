package fr.univ.nantes.impl;

import fr.univ.nantes.except.AbortException;
import fr.univ.nantes.inter.Register;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcreteRegister<T> extends ReentrantLock implements Register<T> {

    private int id;
    private Integer date;
    private T value;
    private Lock myLock = new ReentrantLock();
    private ThreadLocal<T> lc_value;
    private ThreadLocal<Integer> lc_date;
    private ThreadLocal<Boolean> lc_isCopied;

    public ConcreteRegister(Integer date, final T value, int id) {
        this.id = id;
        this.date = date;
        this.value = value;
        lc_value = new ThreadLocal<>();
        lc_value.set(value);
        lc_date = new ThreadLocal<>();
        lc_date.set(date);
        lc_isCopied = new ThreadLocal<>();
        lc_isCopied.set(false);

    }

    public T read(ConcreteTransaction t) throws AbortException {
        if (lc_isCopied.get() != null) {
            return lc_value.get();
        } else {
            lc_value.set(value);
            lc_date.set(date);
            lc_isCopied.set(true);
            t.addReadRegister(this);
            if(lc_date.get() > t.getBirthDate()) {
                System.out.println("J'ai avorté dans le registre");
                throw new AbortException();
            } else {
                return lc_value.get();
            }
        }
    }

    public void write(ConcreteTransaction t, T v) throws AbortException {
        lc_value.set(v);
        lc_isCopied.set(true);
        t.addWrittenRegister(this);
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public T getValue() {
        return value;
    }

    public boolean getIsCopied(){
        return lc_isCopied.get();
    }


    public void setValue(T value) {
        this.value = value;
    }

    public T getLocalValue() {
        return lc_value.get();
    }

    public int getId(){
        return id;
    }

    public void clearLocal(){
        lc_isCopied.remove();
    }
}
