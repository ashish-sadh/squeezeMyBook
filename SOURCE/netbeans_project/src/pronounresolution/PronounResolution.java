/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pronounresolution;
import java.io.*;
import edu.nus.comp.nlp.tool.anaphoraresolution.*;

/**
 *
 * @author Aram Bhusal
 */
public class PronounResolution {

    /**
     * @param args the command line arguments
     * The arguements will be file names with their paths.
     * You cannot use it without specifying a path
     */




    public static void main(String[] args) {
        // TODO code application logic here

        int duration=args.length;
        String[] dummy_arg = new String[1];
           dummy_arg[0]=args[0];
           System.out.println(dummy_arg[0]);
           System.out.print(dummy_arg.length);
           System.out.print(args.length);


           int i=0;
           while(duration>0){
               dummy_arg[0]=args[i];
           String[] out_file=dummy_arg[0].split("/");
           System.out.println(out_file[out_file.length-1]);

           /**
            * Now redirect all output on the command prompt to a file.
            * This spares us all the pains for file handling =]
            */

                 PrintStream orgStream   = null;
                 PrintStream fileStream  = null;
                 try
                 {
                     // Saving the orginal stream
                     orgStream = System.out;
                    fileStream = new PrintStream(new FileOutputStream("./misc/output_lla/"+out_file[out_file.length-1],false));
                     // Redirecting console output to file
                     System.setOut(fileStream);
                     JavaRAP.main(dummy_arg);

                     // Redirecting runtime exceptions to file
                     System.setErr(fileStream);

         //            throw new Exception("Test Exception");           //This was to check if it can catch exceptions


                 }
                 catch (FileNotFoundException fnfEx)
                 {
                     System.out.println("Error in IO Redirection");
                     fnfEx.printStackTrace();
                 }
                 catch (Exception ex)
                 {
                     //Gets printed in the file
                     System.out.println("Redirecting output & exceptions to file");
                     ex.printStackTrace();
                 }
                 finally
                 {
                     //Restoring back to console
                     System.setOut(orgStream);
                     //Gets printed in the console
                     System.out.println("Redirecting file output back to console");

                 }
                 duration--;
         Change c= new Change(dummy_arg[0],"./misc/output_lla/"+out_file[out_file.length-1]);
                 i++;
            }


    }

}
