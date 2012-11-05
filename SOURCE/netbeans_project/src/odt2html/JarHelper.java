/*
 * JarHelper.java
 *
 * Created on October 22, 2007, 5:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package odt2html;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 *Static class for functions, related to jar.
 * @author rssh
 */
public class JarHelper {
    

    public static InputStream   getEntryInputStream(String jarFileName, String entryName) throws FileNotFoundException, IOException
    {
        JarInputStream jarStream = new JarInputStream( new FileInputStream( jarFileName ), false );
        JarEntry jarEntry;
        while ( (jarEntry = jarStream.getNextJarEntry() ) != null) {
            if (entryName.equals(jarEntry.getName())) {
                return jarStream;
            }
        }
        return null;
    }
    
}
