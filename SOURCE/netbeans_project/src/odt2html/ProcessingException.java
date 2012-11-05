/*
 * ProcessingException.java
 */

package odt2html;

/**
 *Exception, which thrown when 
 * @author rssh
 */
public class ProcessingException extends Exception {
    
    /** Creates a new instance of ProcessingException */
    public ProcessingException(String message, Exception e) {
        super(message,e);
    }

    /** Creates a new instance of ProcessingException */
    public ProcessingException(String message) {
        super(message);
    }
    
    
    /** Creates a new instance of ProcessingException */
    public ProcessingException(Exception e) {
        this("Exception during processing",e);
    }
    
    
}
