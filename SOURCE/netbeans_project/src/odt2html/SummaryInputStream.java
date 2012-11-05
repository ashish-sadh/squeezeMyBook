/*
 * SummaryInputStream.java
 *
 * Created on October 22, 2007, 11:31 AM
 */

package odt2html;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 *Summary inputStream, which consists from sequence of other input streams.
 * @author rssh
 */
public class SummaryInputStream extends InputStream
{
    
    public SummaryInputStream(Collection<InputStream> streams)
    {
     streams_=streams;   
     streamIterator_=streams_.iterator();   
     if (streamIterator_.hasNext()) {
       current_=streamIterator_.next();
     }
    }
    
    public int available() throws IOException
    {
        if (current_!=null) {
            return current_.available();
        }else{
            return 0;
        }
    }
    
    public void close() throws IOException
    {
        for(InputStream is:streams_) {
            is.close();
        }
    }

    public int read() throws IOException
    {
        if (current_==null) {
            return -1;
        }else{
            int ch = current_.read();
            if (ch==-1) {
                while (streamIterator_.hasNext()) {
                    current_=streamIterator_.next();
                    int ch1 = current_.read();
                    if (ch1!=-1) {
                        return ch1;
                    }
                }                
                return -1;
            }else{
                return ch;
            }
        }
    }
    
    private Collection<InputStream>  streams_;
    private Iterator<InputStream> streamIterator_;
    private InputStream current_=null;
    
}
