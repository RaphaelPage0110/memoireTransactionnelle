package fr.univ.nantes.except;

public class AbortCommitException extends Exception{
    public AbortCommitException(long id) {
        System.out.println("oups ! J'ai avorté dans le commit!"+id);
    }
}
