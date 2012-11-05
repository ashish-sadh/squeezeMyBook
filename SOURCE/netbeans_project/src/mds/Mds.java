
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mds;
import pronounresolution.*;
import stemstop.*;
import cluster.*;

/**
 *
 * @author aramchanchal
 */
public class Mds {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        PronounResolution.main(args);

        String[] stem_file =new String[args.length];
        String[] x = null;
        System.out.println("Mera itna bada"+args.length);
        for(int i=0; i<args.length;i++)
        {
            x = args[i].split("/");
             System.out.println(x[x.length-1]);
            stem_file[i]="./misc/output_for_stem/"+x[x.length-1];
            System.out.println(stem_file[i]);
        }
        StemStop.main(stem_file);

        //int num_file=args.length;
        



    }

}
