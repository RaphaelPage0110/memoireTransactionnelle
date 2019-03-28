package fr.univ.nantes.inter;

import fr.univ.nantes.except.AbortException;
import fr.univ.nantes.impl.ConcreteTransaction;

public interface Register<T> {
    T read ( ConcreteTransaction t ) throws AbortException;
    void write ( ConcreteTransaction t , T v ) throws AbortException ;
    Integer getDate();
    void setDate(Integer date);
    T getValue();
    void setValue(T value);
    T getLocalValue();
}
