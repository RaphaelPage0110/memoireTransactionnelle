package fr.univ.nantes.except;

public class AbortReadingException extends Exception{

    public AbortReadingException() {
        System.out.println(" Echec de la lecture.");
    }

    public AbortReadingException(long id) {
        System.out.println(" Echec de la lecture pour le thread " + id);
    }
}
