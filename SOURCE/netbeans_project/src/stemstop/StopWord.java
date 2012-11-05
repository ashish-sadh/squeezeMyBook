/*
 * StopWord.java
 *
 * Created on November 10, 2007, 7:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package stemstop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 *
 * @author Chaitanya
 */
public class StopWord {
    
    /** Creates a new instance of StopWord */
    public StopWord() {
    }
    
    /**
     * This method is used to perform the overall task of stopword removal.
     * It returns the stop word eliminated string when given the normal
     * string as input.
     * @param str This is the string in which the stop words are still present.
     * @return The returned string has the stop words eliminated from it.
     */
    public String stopRemove(String str) throws IOException {
        StringTokenizer stk=new StringTokenizer(str," ");
        String result ="";
        
        while(stk.hasMoreTokens()) {
            String s=stk.nextToken();
            // System.out.println(s);
            //now check to see whether this word that we got is in the stop word list
            String tempCheck=stopCheck(s);
            //System.out.println(tempCheck);
            if(tempCheck.equalsIgnoreCase("@@@@@")) {
                //System.out.println("this is it");
            } else {
                tempCheck = tempCheck.toLowerCase();
                result=result+" "+tempCheck;
                //System.out.println(result);
            }
        }
        
        return result;
        
    }
    
    /**
     * This method is used to perform the function of checking whether the string passed
     * as input to the method is there in the stop word list.
     * @param str The word to be checked whether it is a  stop word or not.
     * @return @@@@@ is returned if the word inputted is a stop word.
     */
    public String stopCheck(String word) {
        try{
            //File f = new File("./misc/StemStop_data/stopword3.txt");
            FileReader fileread = new FileReader("./misc/StemStop_data/stopword3.txt");
            BufferedReader bf=new BufferedReader(fileread);
            String temp = bf.readLine();
            while(temp  != null) {//System.out.println(temp);
                if(word.equalsIgnoreCase(temp)) { //System.out.println(word);
                    fileread.close();
                     bf.close();
                    return "@@@@@";
                }
                temp=bf.readLine();
            }
            fileread.close();
           bf.close();
           
            
            
            
        } catch(Exception e){
             System.out.println("exception: " + e.getMessage());
         e.printStackTrace();

            System.out.println("exception in the stop word removal");
        }
        
        return word;
    }
    
}
