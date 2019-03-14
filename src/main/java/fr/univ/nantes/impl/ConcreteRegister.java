package fr.univ.nantes.impl;

import com.sun.org.apache.xerces.internal.dom.AbortException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcreteRegister<T> implements Register<T> {

    Integer date;
    T value;
    Lock myLock = new ReentrantLock();
    ThreadLocal<T> lc_value;
    ThreadLocal<Integer> lc_date;
    ThreadLocal<Boolean> lc_isCopied;

    public ConcreteRegister(Integer date, final T value) {
        this.date = date;
        this.value = value;
        lc_value = new ThreadLocal<T>();
        lc_value.set(value);
        lc_date = new ThreadLocal<Integer>();
        lc_date.set(date);
        lc_isCopied.set(false);

    }

    public T read(Transaction t) throws AbortException {
        return null;
    }

    public void write(Transaction t, T v) throws AbortException {
        lc_value.set(v);
        lc_isCopied.set(true);
    }
}
