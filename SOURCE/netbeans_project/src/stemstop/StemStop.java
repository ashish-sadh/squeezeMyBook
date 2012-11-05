/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stemstop;
import Gui.Gui;
import cluster.*;
import java.io.*;
//import java.util.*;
import findcaption.FindCaption;

/**
 *d
 * @author compaq
 */

public class StemStop {

    public static void get(int value){
     //   summary=(value/100);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String [][] file_output=new String[][]{};
        int number=args.length;
		String filename="";
        int ch;
        String start="<s>";
        String end="</s>";
        String[][] inputfile=new String[1000][1000];
        for(int i=0;i<number;i++)
        {
            String input[]=args[i].split("/");
            String dumb = input[input.length-1];
            int count=0;
        try
        {
            ch=1;
            filename="";
            filename="./misc/output_sentence/"+dumb;
            FileInputStream in = new FileInputStream(filename);
            count=0;
            try
            {
                ch=1;
                String temp=new String();
                temp="";
                inputfile[i][count]="";
                int flag=0;
                while(ch>0)
                {
                     ch=in.read();
                     temp=temp+(char)ch;
                     int si=temp.indexOf(start);
                     int ei=temp.indexOf(end);
                     if(flag==1)
                     {
                         inputfile[i][count]=inputfile[i][count]+(char)ch;
                         //System.out.println(i + "   "+count +"<original_sent>"+inputfile[i][count]);

                     }
                     if(si!=-1)
                     {
                        flag=1;
                        temp="";
                      }
                      if(ei!=-1)
                      {
                          inputfile[i][count]=inputfile[i][count].replace(end, "");
                          System.out.println(i + "   "+count +"<original_sent>"+inputfile[i][count]);

                            count++;

                            inputfile[i][count]="";
                            temp="";
                            flag=0;
                       }
                   }
                }catch (IOException e)
                 {
                    System.out.println("error reading " + filename);
                 }
            } catch (FileNotFoundException e)
            {
            System.out.println("file " + filename + " not found");
            }
        }
        filter d=new filter();
        file_output=d.process1(number,args);
        d.create_tdf();
        d.findbest();
        System.out.println("the best document is"+d.best);
        try
        {

            for(int i=0;i<number;i++)
            {
                Writer output = null;
                String outfile="./misc/output_stem/stemstopout"+Integer.toString(i+1)+".txt";
                File file = new File(outfile);
                output = new BufferedWriter(new FileWriter(file));
                String[] temp=new String[]{};
                temp=file_output[i];
                for(int j=0; temp[j]!=null;j++)
                {
                    //System.out.println(temp[j]);
                    output.write(temp[j]);
                    output.write('\n');
                }
                output.close();
            }
	    FindCaption findcap = new FindCaption();	// By amit
	    findcap.func(number,d,inputfile);			//By amit
	    
        }
        catch(IOException e)
        {
            System.out.println("could not write file");
        }
        Cluster.main(number);
        for (String s : d.tdf_final.keySet())
            {
                Float freq=d.tdf_final.get(s);
                System.out.println(s+":"+freq);
            }
        selectone cr=new selectone();
        cr.rank(d);
        System.out.println("printing the final sentences");
        System.out.println(d.totalsent);
        double problem = ((double)Gui.get())/100;
        double numbersentences=problem*d.totalsent;
         
         for(int i=0;i<cr.sentence.length && cr.sentence[i]!=null;i++)
         {
            for(int j=i+1;j<cr.sentence.length && cr.sentence[j]!=null;j++)
            {
               if(cr.groupsum[i]<cr.groupsum[j])
               {
                    float temp3=cr.groupsum[i];
                    cr.groupsum[i]=cr.groupsum[j];
                    cr.groupsum[j]=temp3;
					temp3=cr.sentences[i];
                    cr.sentences[i]=cr.sentences[j];
                    cr.sentences[j]=temp3;
                 int temp=cr.index[i][0];
                    cr.index[i][0]=cr.index[j][0];
                    cr.index[j][0]=temp;
                    temp=cr.index[i][1];
                    cr.index[i][1]=cr.index[j][1];
                    cr.index[j][1]=temp;
                    temp=cr.index[i][2];
                    cr.index[i][2]=cr.index[j][2];
                    cr.index[j][2]=temp;
                    String temp1="";
                    temp1=cr.sentence[i];
                    cr.sentence[i]="";
                    cr.sentence[i]=cr.sentence[j];
                    cr.sentence[j]="";
                    cr.sentence[j]=temp1;
               }
            }
         }
         for(int i=0;i<cr.sentence.length && cr.sentence[i]!=null;i++)
        {
             if(i>numbersentences)
             {
            //System.out.println(cr.index[i][0]+","+cr.index[i][1]+","+cr.index[i][2]+":"+cr.sentence[i]);
                 cr.sentence[i]=null;
                 cr.index[i][0]=-1;
                 cr.index[i][2]=-1;
                 cr.index[i][1]=-1;
				  cr.sentences[i]=-1;
             }
        }
         System.out.println("finally");
         for(int i=0;i<cr.sentence.length && cr.sentence[i]!=null;i++)
        {

            System.out.println(cr.groupsum[i]+","+cr.index[i][0]+","+cr.index[i][1]+","+cr.index[i][2]+":"+cr.sentence[i]);


        }
        System.out.println("ordering the senteces");
        for(int i=0;i<cr.sentence.length && cr.sentence[i]!=null;i++)
        {
            for(int j=i+1;j<cr.sentence.length && cr.sentence[j]!=null;j++)
            {
                if(cr.sentences[i]>cr.sentences[j])
                {
                    int temp=cr.index[i][0];
                    cr.index[i][0]=cr.index[j][0];
                    cr.index[j][0]=temp;
                    temp=cr.index[i][1];
                    cr.index[i][1]=cr.index[j][1];
                    cr.index[j][1]=temp;
                    temp=cr.index[i][2];
                    cr.index[i][2]=cr.index[j][2];
                    cr.index[j][2]=temp;
                    String temp1="";
                    temp1=cr.sentence[i];
                    cr.sentence[i]="";
                    cr.sentence[i]=cr.sentence[j];
                    cr.sentence[j]="";
                    cr.sentence[j]=temp1;
					float temp3=cr.sentences[i];
                    cr.sentences[i]=cr.sentences[j];
                    cr.sentences[j]=temp3;
                }
            }
        }
        System.out.println(d.totalsent+" "+numbersentences);
        try
        {
            Writer output = null;
            String outfile="./misc/final_summary/final_summary.txt";
            File file = new File(outfile);
            output = new BufferedWriter(new FileWriter(file));
            for(int i=0;i<cr.sentence.length && cr.sentence[i]!=null;i++)
            {
                System.out.println(inputfile[cr.index[i][0]][cr.index[i][1]]);
                 output.write(inputfile[cr.index[i][0]][cr.index[i][1]]);
            }
            output.close();
        }
        catch(IOException e)
        {
            System.out.println("could not write file");
        }

    }

}
