package fr.univ.nantes.inter;

import com.sun.org.apache.xerces.internal.dom.AbortException;

public interface Register<T> {
    T read ( Transaction t ) throws AbortException;
    void write ( Transaction t , T v ) throws AbortException ;
}
