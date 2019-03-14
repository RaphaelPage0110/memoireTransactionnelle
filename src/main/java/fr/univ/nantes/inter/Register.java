package fr.univ.nantes.inter;

import com.sun.org.apache.xerces.internal.dom.AbortException;

public interface Register<T> {
    public T read ( Transaction t ) throws AbortException;
    public void write ( Transaction t , T v ) throws AbortException ;
}
