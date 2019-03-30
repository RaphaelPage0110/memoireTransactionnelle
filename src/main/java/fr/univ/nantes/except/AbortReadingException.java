package fr.univ.nantes.except;

public class AbortReadingException extends Exception{

//    public AbortReadingException() {
//        System.out.println("Reading failure.");
//    }

    public AbortReadingException(long id) {
        //System.out.println("Reading failure for the thread " + id);
    }
}
