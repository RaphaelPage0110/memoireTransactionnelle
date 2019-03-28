package fr.univ.nantes.inter;

import com.sun.org.apache.xerces.internal.dom.AbortException;
import fr.univ.nantes.impl.ConcreteTransaction;

public interface Register<T> {
    T read ( ConcreteTransaction t ) throws AbortException;
    void write ( ConcreteTransaction t , T v ) throws AbortException ;
    Integer getDate();
    void setDate(Integer date);
    T getValue();
    void setValue(T value);
    ThreadLocal<T> getLocalValue();
}
