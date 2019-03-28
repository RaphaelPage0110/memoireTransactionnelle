package fr.univ.nantes.impl;

import com.sun.org.apache.xerces.internal.dom.AbortException;
import fr.univ.nantes.inter.Register;
import fr.univ.nantes.inter.Transaction;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcreteRegister<T> extends ReentrantLock implements Register<T> {

    private Integer date;
    private T value;
    private Lock myLock = new ReentrantLock();
    private ThreadLocal<T> lc_value;
    private ThreadLocal<Integer> lc_date;
    private ThreadLocal<Boolean> lc_isCopied;

    public ConcreteRegister(Integer date, final T value) {
        this.date = date;
        this.value = value;
        lc_value = new ThreadLocal<T>();
        lc_value.set(value);
        lc_date = new ThreadLocal<Integer>();
        lc_date.set(date);
        lc_isCopied.set(false);

    }

    public T read(ConcreteTransaction t) throws AbortException {
        if (lc_isCopied.get()) {
            return lc_value.get();
        } else {
            lc_value.set(value);
            lc_date.set(date);
            lc_isCopied.set(true);
            t.addReadRegister(this);
            if(lc_date.get() > t.getBirthDate()) {
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

    public void setValue(T value) {
        this.value = value;
    }

    public ThreadLocal<T> getLocalValue() {
        return lc_value;
    }
}
