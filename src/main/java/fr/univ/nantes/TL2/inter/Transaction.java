package fr.univ.nantes.TL2.inter;

import fr.univ.nantes.TL2.except.AbortCommitException;
import fr.univ.nantes.TL2.inter.Register;

/**
 * Interface that allows to manage transactions that reference a set of read and write operations on atomic registers,
 * trying to have similar guarantees to the ACID properties.
 *
 * @param <T> the type of value contained in the registers that we manage.
 *
 * @author Raphael PAGE
 * @author Glenn PLOUHINEC
 */
public interface Transaction<T> {

    /**
     * Indicates when the transaction has begun.
     * @return this.birthDate.
     */
    int getBirthDate();

    /**
     * Indicates if the transaction has been committed.
     * @return this.committed.
     */
    boolean isCommitted();

    /**
     * Adds a register that will be written to the localReadSet.
     * @param newRegister the register to add.
     */
    void addWrittenRegister(Register<T> newRegister);

    /**
     * Adds a register that will be read to the localReadSet.
     * @param newRegister the register to add.
     */
    void addReadRegister(Register<T> newRegister);

    /**
     * Allows to start a transaction.
     */
    void begin() ;

    /**
     * Allows to start an attempt to commit a transaction.
     * @throws AbortCommitException if the transaction is invalid.
     */
    void try_to_commit() throws AbortCommitException;
}
