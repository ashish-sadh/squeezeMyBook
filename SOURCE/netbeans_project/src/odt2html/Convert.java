/*
 * Main.java
 *
 * Created on October 20, 2007, 8:52 PM
 *
 */

package odt2html;

import java.io.IOException;
import java.util.zip.ZipException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author rssh
 */
public class Convert {
    
    /** Creates a new instance of Main */
    public Convert() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {     
     try{
	//File nd1 = new File("Temp");
	//nd1.mkdir();
	for(int i=0;i<args.length;i++){
		//File newdir = new File("Temp"+ File.separator +"doc_" +i);
		File newdir = new File("misc"+ File.separator+"doc_parsing"+File.separator+"doc_" +i);
		newdir.mkdir();
		unzipPictures(args[i],i);
       		ODTransform transform = new ODTransform();
       		//transform.setXsltInClasspath(true);
      		//transform.setXsltFileName("odt2html"+ File.separator +"odt2html.xsl");
		transform.setXsltFileName("misc"+File.separator+"odt2html.xsl");       
       		if (parseArgs(args,transform,i)) {    
          		doTransform(transform);
       		}
	}
     }catch(ConfigurationException ex){
        	 System.err.println(ex.getMessage());
     }catch(ProcessingException ex){
        	 System.err.println("exception during processing");
        	 ex.printStackTrace();
     }
    }	
    
    public static void doTransform(ODTransform transform) throws ProcessingException
    {
       try {
         transform.doTransform();
       }catch(ZipException e){
           throw new ProcessingException("Exception during processing, may be " + transform.getInputODName()+" is  not odt file",e);           
       }catch(SAXException e){
           throw new ProcessingException(e);
       }catch(TransformerException e){
           throw new ProcessingException(e); 
       }catch(IOException e){
           throw new ProcessingException(e);
       }
    }
    
    private static boolean parseArgs(String[] args,ODTransform transform,int doc_num) throws ConfigurationException
    {
        boolean retval=false;
        /*if (args.length<2){
            usage();
        }else{*/        
        /*  for(int i=0; i<args.length-2; ++i) {
            if (args[i].equals("--external-xslt")) {
                if (i+1==args.length) {
                    throw new ConfigurationException("option --external-xslt must hve argument");
                }else{                    
                    transform.setXsltFileName(args[++i]);
                }
            }else{
                throw new ConfigurationException("unknown option "+args[i]);
            }
          }*/
          transform.setInputODName(args[doc_num]);
          transform.setOutputFileName("misc"+ File.separator+"doc_parsing"+File.separator+"doc_"+doc_num+ File.separator + "doc"+doc_num +".html" );          
          retval=true;
        //}
        return retval;
    }
    
    private static void usage()
    {
        System.out.println("Usage:");
        System.out.println("ua.gradsoft.odt2html.Main [--external-xslt xsltfile] input output");
        System.out.println();
        System.out.println("See http://odt2html.gradsoft.ua for details.");
    }
	public static void unzipPictures( String zipFile,int doc_num) {
	try {
		ZipFile zf = new ZipFile(zipFile);
		Enumeration< ? extends ZipEntry> zipEnum = zf.entries();
		//String dir = "Temp"+ File.separator +"doc_" + doc_num;
		String dir = "misc"+ File.separator+"doc_parsing"+File.separator+"doc_" + doc_num;

		while( zipEnum.hasMoreElements() ) {
			ZipEntry item = (ZipEntry) zipEnum.nextElement();
			if( (item.getName()).charAt(0) == 'P' ){
				if (item.isDirectory()) {
					File newdir = new File(dir + File.separator + item.getName());
					newdir.mkdir();
				} else {
					String newfilePath = dir + File.separator + item.getName();
					File newFile = new File(newfilePath);
					if (!newFile.getParentFile().exists()) {
						newFile.getParentFile().mkdirs();
					}
					InputStream is = zf.getInputStream(item);
					FileOutputStream fos = new FileOutputStream(newfilePath);
                                        FileOutputStream fos_final = new FileOutputStream("misc"+ File.separator+"final_summary"+File.separator+item.getName());
					int ch;
					while( (ch = is.read()) != -1 ) {
						fos.write(ch);
                                                fos_final.write(ch);
						}
					is.close();
					fos.close();
                                        fos_final.close();
				}
			}
		}
		zf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
}
