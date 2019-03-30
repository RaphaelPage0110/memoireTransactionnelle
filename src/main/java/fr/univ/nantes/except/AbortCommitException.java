package fr.univ.nantes.except;

public class AbortCommitException extends Exception{

//    public AbortCommitException() {
//        System.out.println("Commit failure.");
//    }

    public AbortCommitException(long id) {
        //System.out.println("Commit failure for the thread " + id);
    }
}
