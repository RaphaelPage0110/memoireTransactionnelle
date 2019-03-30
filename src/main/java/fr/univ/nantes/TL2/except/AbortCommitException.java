package fr.univ.nantes.TL2.except;

public class AbortCommitException extends Exception{

    public AbortCommitException() {
        System.out.println("Echec du commit.");
    }

    public AbortCommitException(long id) {
        System.out.println("Echec du commit pour le thread " + id);
    }
}
