package fr.univ.nantes.except;

public class AbortException extends Exception{
    public AbortException() {
        System.out.println("oups ! J'ai avorté !");
    }
}
