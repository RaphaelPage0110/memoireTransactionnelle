package fr.univ.nantes.inter;

import fr.univ.nantes.except.AbortCommitException;
import fr.univ.nantes.impl.ConcreteRegister;

public interface Transaction<T> {
    public void begin () ;
    public void try_to_commit () throws AbortCommitException;
    public boolean isCommitted() ;
    public void addWrittenRegister(ConcreteRegister<T> r);

}
