/*
 * ResolveDTD.java
 *
 * Created on October 20, 2007, 11:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package odt2html;


import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import java.io.StringReader;

public class ResolveDTD implements EntityResolver {
    public InputSource resolveEntity (String publicId, String systemId)
    {
        if (systemId.endsWith(".dtd"))
        {
            StringReader stringInput =
                new StringReader(" ");
            return new InputSource(stringInput);
        }
        else
        {
            //???
            return null;    // default behavior
        }
    }
}



