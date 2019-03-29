package fr.univ.nantes.except;

public class AbortReadingException extends Exception{
    public AbortReadingException(long id) {
        System.out.println("oups ! J'ai avorté dans la lecture !"+id);
    }
}
