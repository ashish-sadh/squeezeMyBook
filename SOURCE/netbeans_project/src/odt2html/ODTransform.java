/*
 * ODTransform.java
 *
 */

package odt2html;

/*
 *
 * Derived from: ODTransform.java
 * (c) 2003-2005 J. David Eisenberg
 * Licensed under LGPL
 *
 * Program purpose: to perform an XSLT transformation
 * on a member of an OpenDocument file, either
 * after unzipping or while still in its zipped state.
 * Output may go to a normal file or a zipped file.
 *
 *
 */


import java.io.ByteArrayInputStream;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.jar.JarOutputStream;
import java.util.jar.JarEntry;
import java.util.zip.ZipException;


public class ODTransform {
    
    
    
      public String getInputFileName()
      { return inputFileName; }
      
      public void setInputFileName(String fname)
      { inputFileName=fname; }

      public String  getInputODName()
      { return inputODName; }
      
      public void    setInputODName(String fname)
      { inputODName=fname; }

      public String getOutputFileName()
      { return outputFileName; }
      
      public void setOutputFileName(String fname)
      { outputFileName=fname; }

      public String  getOutputODName()
      { return outputODName; }
      
      public void    setOutputODName(String fname)
      { outputODName=fname; }
      
      public String   getXsltFileName()
      { return xsltFileName; }
      
      public void    setXsltFileName(String fname)
      { xsltFileName=fname; }
    
    
      public boolean  isXsltInClasspath()
      { return xsltInClasspath; }
      
      public void  setXsltInClasspath(boolean value)
      { xsltInClasspath=value; }
      
      
    
    public void doTransform( )    throws TransformerException, TransformerConfigurationException,
            SAXException, ZipException, IOException, ProcessingException 
    {
        /* Set up the XSLT transformation based on the XSLT file */
        StreamSource streamSource=null;
        if (isXsltInClasspath()) {
          InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(xsltFileName); 
          if (inputStream==null) {
              throw new ProcessingException(xsltFileName+" is not found in classpath");
          }
          streamSource = new StreamSource( inputStream );
        }else{
          File xsltFile = new File( xsltFileName );
          streamSource = new StreamSource( xsltFile );
        }
       
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer( streamSource );
        
        /* Set up parameters for transform */
        for (Map.Entry<String,String> e: params.entrySet()) {
            transformer.setParameter(e.getKey(),e.getValue());
        }
        
        
        /* Create an XML reader which will ignore any DTDs */
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setEntityResolver( new ResolveDTD() );
        
        InputSource inputSource;
        
        if (inputODName == null) {
            /* This is an unpacked file. */
            inputSource =
                    new InputSource( new FileInputStream( inputFileName ) );
        } else {
            /* The input file should be a member of an OD file.
               Check to see if the input file name really exists
               within the JAR file */
            if (inputFileName==null) {
                inputSource = new InputSource(createSumarryODTInputStream(inputODName));
            }else{
                inputSource = new InputSource(JarHelper.getEntryInputStream(inputODName,inputFileName));
            }
        }
        
        SAXSource saxSource = new SAXSource( reader, inputSource );
        saxSource.setSystemId( inputFileName );
        
        if (outputODName == null) {
            /* We want a regular file as output */
            FileOutputStream outputStream =
                    new FileOutputStream( outputFileName );
            transformer.transform( saxSource,
                    new StreamResult( outputStream ) );
            outputStream.close();
        } else {
            /* The output file name is the name of a member of
               a JAR file (which we will build without a manifest) */
            JarOutputStream jarStream =
                    new JarOutputStream( new FileOutputStream( outputODName ) );
            JarEntry jarEntry = new JarEntry( outputFileName );
            jarStream.putNextEntry( jarEntry );
            transformer.transform( saxSource,
                    new StreamResult( jarStream ) );
            
            /* Close the member file and the JAR file
               to complete the file */
            jarStream.closeEntry();
            
            createManifestFile( jarStream );
            
            /* Close the JAR file to complete the file */
            jarStream.close();
        }
    }
    
    /* Check to see if the command line arguments make sense */
    private void checkArgs( String[] args ) {
        int     i;
        
        if (args.length == 0) {
            showUsage( );
            System.exit( 1 );
        }
        i = 0;
        while ( i < args.length ) {
            if (args[i].equalsIgnoreCase("-in")) {
                if ( i+1 >= args.length) {
                    badParam("-in");
                }
                inputFileName = args[i+1];    
                i += 2;
            } else if (args[i].equalsIgnoreCase("-out")) {
                if ( i+1 >= args.length) {
                    badParam("-out");
                }
                outputFileName = args[i+1];
                i += 2;
            } else if (args[i].equalsIgnoreCase("-xsl")) {
                if ( i+1 >= args.length) {
                    badParam("-xsl");
                }
                xsltFileName = args[i+1];
                i += 2;
            } else if (args[i].equalsIgnoreCase("-inod")) {
                if ( i+1 >= args.length) {
                    badParam("-inOD");
                }
                inputODName = args[i+1];
                i += 2;
            } else if (args[i].equalsIgnoreCase("-outod")) {
                if ( i+1 >= args.length) {
                    badParam("-outOD");
                }
                outputODName = args[i+1];
                i += 2;
            } else if (args[i].equalsIgnoreCase("-param")) {
                if ( i+2 >= args.length) {
                    badParam("-param");
                }
                params.put(args[i+1],args[i+2]);
                i += 3;
            } else {
                System.out.println( "Unknown argument " + args[i] );
                System.exit( 1 );
            }
        }
        
        if (inputFileName == null) {
            System.out.println("No input file name specified.");
            System.exit( 1 );
        }
        if (outputFileName == null) {
            System.out.println("No output file name specified.");
            System.exit( 1 );
        }
        if (xsltFileName == null) {
            System.out.println("No XSLT file name specified.");
            System.exit( 1 );
        }
    }
    
    /* If not enough arguments for a parameter, show error and exit */
    private void badParam( String paramName ) {
        System.out.println("Not enough parameters to " + paramName);
        System.exit(1);
    }
    
    /*
        Creates the manifest file for a compressed OpenDocument
        file.  The mType array contains pairs of filename
        extensions and corresponding mimetypes.  The comparison
        to find the extension is done in a case-insensitive manner.
     */
    private void createManifestFile( JarOutputStream jarStream ) {
        String [] mType = {
            "odt", "application/vnd.oasis.opendocument.text",
            "ott", "application/vnd.oasis.opendocument.text-template",
            "odg", "application/vnd.oasis.opendocument.graphics",
            "otg",
            "application/vnd.oasis.opendocument.graphics-template",
            "odp", "application/vnd.oasis.opendocument.presentation",
            "otp",
            "application/vnd.oasis.opendocument.presentation-template",
            "ods", "application/vnd.oasis.opendocument.spreadsheet",
            "ots",
            "application/vnd.oasis.opendocument.spreadsheet-template",
            "odc", "application/vnd.oasis.opendocument.chart",
            "otc", "application/vnd.oasis.opendocument.chart-template",
            "odi", "application/vnd.oasis.opendocument.image",
            "oti", "application/vnd.oasis.opendocument.image-template",
            "odf", "application/vnd.oasis.opendocument.formula",
            "otf", "application/vnd.oasis.opendocument.formula-template",
            "odm", "application/vnd.oasis.opendocument.text-master",
            "oth", "application/vnd.oasis.opendocument.text-web",
        };
        
        JarEntry jarEntry;
        
        int dotPos;
        String extension;
        String mimeType = null;
        String outputStr;
        
        dotPos = outputODName.lastIndexOf(".");
        extension = outputODName.substring( dotPos + 1 );
        for (int i=0; i < mType.length && mimeType == null; i+=2) {
            if (extension.equalsIgnoreCase( mType[i] )) {
                mimeType = mType[i+1];
            }
        }
        
        if (mimeType == null) {
            System.err.println("Cannot find mime type for extension "
                    + extension );
            mimeType = "UNKNOWN";
        }
        
        try {
            jarEntry = new JarEntry( "META-INF/manifest.xml");
            jarStream.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    .getBytes() );
            jarStream.write( "<!DOCTYPE manifest:manifest PUBLIC \"-//OpenOffice.org//DTD Manifest 1.0//EN\" \"Manifest.dtd\">"
                    .getBytes() );
            jarStream.write("<manifest:manifest xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\">"
                    .getBytes() );
            
            outputStr = "<manifest:file-entry manifest:media-type=\"" +
                    mimeType + "\" manifest:full-path=\"/\"/>";
            jarStream.write( outputStr.getBytes() );
            
            outputStr = "<manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"" + outputFileName + "\"/>";
            jarStream.write( outputStr.getBytes() );
            jarStream.write("</manifest:manifest>".getBytes() );
            jarStream.closeEntry();
        } catch (IOException e) {
            System.err.println("Cannot write file:");
            System.err.println( e.getMessage() );
        }
    }
    
    
    private InputStream createSumarryODTInputStream(String odtJarName) throws FileNotFoundException, IOException   
    {
       InputStream begin = new ByteArrayInputStream(
          "<office:document xmlns:office='urn:oasis:names:tc:opendocument:xmlns:office:1.0'>\n".getBytes(Charset.defaultCharset())           
               );
       InputStream end = new ByteArrayInputStream(
          "</office:document>\n".getBytes(Charset.defaultCharset())           
               );

       InputStream metaInputStream = JarHelper.getEntryInputStream(odtJarName,"meta.xml");
       InputStream contentInputStream = JarHelper.getEntryInputStream(odtJarName,"content.xml");
       InputStream stylesInputStream = JarHelper.getEntryInputStream(odtJarName,"styles.xml");
       
       LinkedList<InputStream> streams = new LinkedList<InputStream>();
       streams.add(begin);
       if (metaInputStream!=null) {
            streams.add(new SkipFirstLineFilterInputStream(metaInputStream));
       }
       if (contentInputStream!=null){
           streams.add(new SkipFirstLineFilterInputStream(contentInputStream));
       }
       if (stylesInputStream!=null) {
           streams.add(new SkipFirstLineFilterInputStream(stylesInputStream));
       }
       streams.add(end);
       
       return new SummaryInputStream(streams);
       
    }
    
    
    /* If no arguments are provided, show this brief help section */
    private void showUsage( ) {
        System.out.println("Usage: ODTransform options");
        System.out.println("Options:");
        System.out.println("   -in inputFilename");
        System.out.println("   -xsl transformFilename");
        System.out.println("   -out outputFilename");
        System.out.println("If the input filename is within an OpenDocument file, then:");
        System.out.println("   -inOD inputOpenDocFileName");
        System.out.println("If you wish to output an OpenDocument file, then:");
        System.out.println("   -outOD outputOpenDocumentFileName");
        System.out.println( );
        System.out.println("Argument names are case-insensitive.");
    }
    
    public static void main(String[] args) {
        ODTransform transformApp = new ODTransform( );
        transformApp.checkArgs( args );
        try {
            transformApp.doTransform( );
        } catch (Exception e) {
            System.out.println("Unable to transform");
            System.out.println(e.getMessage());
        }
    }
    
    
    String  inputFileName = null;   // input file name, or member name...
    String  inputODName = null;     // ...if given an OpenDocument input file
    String  outputFileName = null;  // output file name, or member name...
    String  outputODName = null;    // ...if given an OpenDocument output file
    
    
    String  xsltFileName = null;    // XSLT file is always a regular file if set.
    boolean xsltInClasspath = false;
    
    Map<String,String>  params = new HashMap<String,String>();  // parameters to be passed to transform
    
    
}


