package fr.univ.nantes.inter;

import fr.univ.nantes.except.AbortReadingException;

/**
 * Interface that allows to read and write a value in a register through a transaction.
 *
 * @param <T> the type of value contained in the registers.
 *
 * @author Raphael PAGE
 * @author Glenn PLOUHINEC
 */
public interface Register<T> {

    /**
     * Returns the ID of the register.
     * @return this.id.
     */
    int getId();

    /**
     * Returns the date of the last writing in the register.
     * @return this.date.
     */
    Integer getDate();

    /**
     * Sets the date of the register.
     * @param date the writing date.
     */
    void setDate(Integer date);

    /**
     * Returns the value of the last writing in the register.
     * @return this.value.
     */
    T getValue();

    /**
     * Sets the value of the register.
     * @param value the new value of the register.
     */
    void setValue(T value);

    /**
     * Returns the value of the local copy of the register.
     * @return the local value.
     */
    T getLocalValue();

    /**
     * Allows to avoid any access to the register by setting a lock on the instance (this).
     */
    void lock();

    /**
     * Releases the lock that has been set, and allows other threads to access the registry
     */
    void unlock();

    /**
     * Re-initializes local variables : local date, and local value.
     */
    void clearLocal();

    /**
     * Allows to read a register during a transaction. Returns the local value if it exists. Otherwise, the register is placed
     * in localReadSet of the transaction, and the value of the register is returned if it has not been modified
     * in the meantime.
     * @param transaction the transaction that requires a reading.
     * @return the local value if it exists, otherwise, the value of the register.
     * @throws AbortReadingException if the register has been modified since the beginning of the transaction.
     */
    T read (Transaction<T> transaction) throws AbortReadingException;

    /**
     * Allows to write in a register during a transaction. Creates a local copy of the value, and adds the register to the localWroteSet of the transaction.
     * @param transaction the transaction that requires a writing.
     * @param value the value to be written.
     */
    void write (Transaction<T> transaction, T value);
}
