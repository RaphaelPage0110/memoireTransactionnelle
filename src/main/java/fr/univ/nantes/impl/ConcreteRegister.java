package fr.univ.nantes.impl;

import fr.univ.nantes.except.AbortReadingException;
import fr.univ.nantes.inter.Register;
import java.util.concurrent.locks.ReentrantLock;

public class ConcreteRegister<T> extends ReentrantLock implements Register<T> {

    private int id;
    private Integer date;
    private T value;
    private ThreadLocal<T> lc_value = new ThreadLocal<>();
    private ThreadLocal<Integer> lc_date = new ThreadLocal<>();

    public ConcreteRegister(Integer date, T value, int id) {
        this.id = id;
        this.date = date;
        this.value = value;
    }

    //allow to read a register during a transaction
    public T read(ConcreteTransaction t) throws AbortReadingException {

        //if the value has already been copied during the transaction
        //there is no need to copy it again
        if (lc_value.get() != null) {
            return lc_value.get();
        } else {
            lc_value.set(value);
            lc_date.set(date);
            t.addReadRegister(this);

            //if the register have been modified after the transaction began we abort
            if(lc_date.get() > t.getBirthDate()) {
                throw new AbortReadingException(Thread.currentThread().getId());
            } else {
                return lc_value.get();
            }
        }
    }

    //allow to write a register during a transaction
    public void write(ConcreteTransaction t, T v){
        lc_value.set(v);
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

    public T getLocalValue() {
        return lc_value.get();
    }

    public int getId(){
        return id;
    }

    //clear local value
    public void clearLocal(){
        lc_date.remove();
        lc_value.remove();
    }
}
