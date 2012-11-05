/*
 * To Change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pronounresolution;
import java.io.*;



/**
 *
 * @author Aram Bhusal
 */
public class Change {

    public Change(String iFile, String oFile){
        modify(iFile,oFile);
    }

    /**
     *
     * @param iFile: It represents the original file
     * @param oFile: It represents the output of JavaRAP
     */





    public static void modify(String iFile, String oFile){

        Sentencesplitter s = new Sentencesplitter(iFile,1);                       //split the sentence

        String[] ss = iFile.split("/");
        String newfile="./misc/output_sentence/"+ss[ss.length-1];

        File nFile = new File(newfile);
         String test = getContents(nFile);
         String nString[]= test.split("</s>");                                  //nString stores the whole file content

         for (int k=0; k<nString.length; k++){
             nString[k]=nString[k].replaceAll("<s>","");
            // System.out.println(nString[k]);
         }

         /**
          * In the above sentence I have removed all <s> and </s> tags and each element of array represents a statement
          */

         File outputFile = new File(oFile);
         String check = getContents(outputFile);
         String[] bakar=check.split("\\*");
         String data=bakar[25];
         String[]imp = data.split("\\n");                                       //imp houses the lla output in lines
         for(int zz=0;zz<imp.length;zz++){
        //     System.out.println(imp[zz]);
         }
         


         for(int i=1;i<imp.length;i++){
             if(imp[i].charAt(0)=='('){

                 //LINE_NOUN IS LINE OF NOUN
                 int line_noun=(int)imp[i].charAt(1) - 48;
                 //WORD_NOUN IS WORD OF NOUN
                 int word_noun=(int)imp[i].charAt(3)-48;

                 

                 String[] dummy = imp[i].split("\\) ");
               
                    //DUMMY[2] IS MY PRONOUN
                 String pronoun=dummy[2].replaceAll("\\s", "");
                 pronoun=pronoun.replaceAll(",", "");
                 System.out.println(pronoun);
      
                 //System.out.println(pronoun.charAt(0));
                System.out.println("pronoun =>"+pronoun);
                    String[] dummy1 = dummy[1].split(" <-- "); //I am doing this because dummy1 has the required noun

                    //DUMMY1[0] HAS MY NOUN
                    String noun=dummy1[0];
                    noun=noun.replace("\\s+", "\\s");
                    System.out.println("noun=>"+noun);

                    
                    String[] map = dummy1[1].substring(1).split(",");
                    int pronoun_line=Integer.parseInt(map[0]);
                    int pronoun_word=Integer.parseInt(map[1]);
                    System.out.println(pronoun_line+" "+map[0]);
                    System.out.println(pronoun_word+" "+map[1]);

               //System.out.println(nString[pronoun_line]);
                    
                    nString[pronoun_line]=nString[pronoun_line].replaceFirst(pronoun,noun);
                    System.out.println(nString[pronoun_line]);
                 
             }
         }

                                   PrintStream orgStream   = null;
                          PrintStream fileStream  = null;

                          String finalfile="./misc/output_pronoun/"+ss[ss.length-1];
                     try
                     {
                          //Saving the orginal stream
                         orgStream = System.out;
                        fileStream = new PrintStream(new FileOutputStream(finalfile,false));
                          //Redirecting console output to file
                         System.setOut(fileStream);

 
                            for(int i=0;i<nString.length;i++){
                                System.out.println(nString[i]);
                            }


                          //Redirecting runtime exceptions to file
                         System.setErr(fileStream);

          //               throw new Exception("Test Exception");


                     }
                     catch (FileNotFoundException fnfEx)
                     {
                        // System.out.println("Error in IO Redirection");
                       //  fnfEx.printStackTrace();
                     }
                     catch (Exception ex)
                     {
                         //Gets printed in the file
                     //    System.out.println("Redirecting output & exceptions to file");
                   //      ex.printStackTrace();
                     }
                     finally
                     {
                         //Restoring back to console
                         System.setOut(orgStream);
                         //Gets printed in the console
                         System.out.println("Redirecting file output back to console");

                     }

                     Sentencesplitter s1 = new Sentencesplitter(finalfile,2);





    }











    static public String getContents(File aFile) {
    //...checks on aFile are elided
    StringBuilder contents = new StringBuilder();

    try {
      //use buffering, reading one line at a time
      //FileReader always assumes default encoding is OK!
      BufferedReader input =  new BufferedReader(new FileReader(aFile));
      try {
        String line = null; //not declared within while loop
        /*
        * readLine is a bit quirky :
        * it returns the content of a line MINUS the newline.
        * it returns null only for the END of the stream.
        * it returns an empty String if two newlines appear in a row.
        */
        while (( line = input.readLine()) != null){
          contents.append(line);
          contents.append(System.getProperty("line.separator"));
        }
      }
      finally {
        input.close();
      }
    }
    catch (IOException ex){
      ex.printStackTrace();
    }

    return contents.toString();
  }



}
