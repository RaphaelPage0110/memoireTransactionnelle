package fr.univ.nantes.inter;

import fr.univ.nantes.except.AbortReadingException;
import fr.univ.nantes.impl.ConcreteTransaction;

public interface Register<T> {
    T read ( ConcreteTransaction t ) throws AbortReadingException;
    void write ( ConcreteTransaction t , T v ) throws AbortReadingException;
    Integer getDate();
    void setDate(Integer date);
    T getValue();
    void setValue(T value);
    T getLocalValue();
}
