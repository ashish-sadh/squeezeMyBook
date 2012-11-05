/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pronounresolution;
import java.io.*;
import edu.nus.comp.nlp.tool.*;

/**
 *
 * @author aramchanchal
 */
public class Sentencesplitter {

    public Sentencesplitter(String iFile, int choice){
                    PrintStream orgStream   = null;
              PrintStream fileStream  = null;
              String[] ss = iFile.split("/");
              String[] dummy = new String[1];
              dummy[0]=iFile;
              String newfile=null;
              if(choice==1){
              newfile="./misc/output_sentence/"+ss[ss.length-1];
              }else
              {
              newfile="./misc/output_for_stem/"+ss[ss.length-1];
              }
         try
         {
              //Saving the orginal stream
             orgStream = System.out;
            fileStream = new PrintStream(new FileOutputStream(newfile,false));
              //Redirecting console output to file
             System.setOut(fileStream);
             SentenceSplitter.main(dummy);
              //Redirecting runtime exceptions to file
             System.setErr(fileStream);

             throw new Exception("Test Exception");


         }
         catch (FileNotFoundException fnfEx)
         {
    //         System.out.println("Error in IO Redirection");
      //       fnfEx.printStackTrace();
         }
         catch (Exception ex)
         {
             //Gets printed in the file
           //  System.out.println("Redirecting output & exceptions to file");
         //    ex.printStackTrace();
         }
         finally
         {
             //Restoring back to console
             System.setOut(orgStream);
             //Gets printed in the console
             System.out.println("Redirecting file output back to console");

         }
    }

}
