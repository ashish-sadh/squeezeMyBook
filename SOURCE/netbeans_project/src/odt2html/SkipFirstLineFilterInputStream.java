/*
 * SkipFirstLineFilterInputStream.java
 *
 * Created on October 22, 2007, 12:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package odt2html;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author rssh
 */
public class SkipFirstLineFilterInputStream extends FilterInputStream
{
    
    /**
     * Creates a new instance of SkipFirstLineFilterInputStream
     */
    public SkipFirstLineFilterInputStream(InputStream other) {
        super(other);
        inFirstLine=true;
        //firstLine=null;
    }
    
    void initFirstLine() throws IOException
    {
        if (inFirstLine) {
             int ch=0;
             do {
                ch = in.read();
             } while (ch !=-1 && ch!='\n');
             inFirstLine=false;                           
        }
    }
    
    public int available() throws IOException
    {
        initFirstLine();
        return in.available();
    }
    
    public boolean markSupported()
    { return false; }
    
    public int read() throws IOException
    {
        initFirstLine();
        return in.read();
    }
    
    public int read(byte[] b) throws IOException
    {
        initFirstLine();
        return in.read(b);        
    }
    
    public int read(byte[] b, int off, int len) throws IOException
    {
        initFirstLine();
        return in.read(b,off,len);        
    }
    
    public void reset() throws IOException
    {
      if (!in.markSupported()) {
        in.reset();
        inFirstLine=false;
      }else{
          throw new IOException("reset call on filter on stream, which cas support mark.");
      }
    }
    
    public long skip(long l) throws IOException
    {
        initFirstLine();
        return in.skip(l);
    }         
    
    private boolean inFirstLine;
   // private byte[]  firstLine;
   // int     firstLineIndex=0;
    
    
}
