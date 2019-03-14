package fr.univ.nantes.inter;

import com.sun.org.apache.xerces.internal.dom.AbortException;
import fr.univ.nantes.impl.ConcreteRegister;

public interface Transaction {
    public void begin () ;
    public void try_to_commit () throws AbortException;
    public boolean isCommited () ;
    public void addWritedRegister(Register r);

}
