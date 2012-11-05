/*
 * Odt2HtmlAntTask.java
 *
 * Created on October 24, 2007, 4:28 PM
 *
 */

package odt2html;

import java.io.IOException;
import java.util.zip.ZipException;
import javax.xml.transform.TransformerException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.xml.sax.SAXException;

/**
 *Ant task to do transformation of odt to html.
 * @author rssh
 */
public class Odt2HtmlAntTask extends Task
{
    
    /**
     * Creates a new instance of Odt2HtmlAntTask
     */
    public Odt2HtmlAntTask() {
       odtransform_ = new ODTransform();
       odtransform_.setXsltInClasspath(true);
       odtransform_.setXsltFileName("odt2html.xsl");       
    }

    public void  setInput(String filename)
    {
      odtransform_.setInputODName(filename);     
    }
        
    public void setOutput(String filename)
    {
      odtransform_.setOutputFileName(filename);          
    }
    
    public void  setExternalXslt(String filename)
    {
        odtransform_.setXsltFileName(filename);
    }
    

    public void execute() throws BuildException
    {
       try {
         odtransform_.doTransform();
       }catch(ZipException e){
           throw new BuildException(e);           
       }catch(SAXException e){
           throw new BuildException(e);
       }catch(TransformerException e){
           throw new BuildException(e); 
       }catch(IOException e){
           throw new BuildException(e);
       }catch(ProcessingException e){
           throw new BuildException(e);
       }
        
    }
    
    
    private ODTransform odtransform_;
    
}
