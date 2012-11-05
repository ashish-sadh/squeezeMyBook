package findcaption;

//import stemstop.*;
//import java.util.Scanner;
//import findcaption.*;
import java.io.*;
//import java.util.*;

public class Captionstemstop{
    public static String func(String[] args) {
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
            //String input[]=args[i].split("/");
            String input[] = new String[1];
            input[0] = args[i];
            String dumb = input[input.length-1];
            int count=0;
        try
        {
            ch=1;
            filename="";
            filename = dumb; //changed
            FileInputStream in = new FileInputStream(filename);
            count=0;
            try
            {
                ch=1;
                String temp = new String();
                temp ="";
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

                     }
                     if(si!=-1)
                     {
                        flag=1;
                        temp="";
                      }
                      if(ei!=-1)
                      {
                          inputfile[i][count]=inputfile[i][count].replace(end, "");

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
        //d.create_tdf();
        //d.findbest();				changed
        //System.out.println("the best document is"+d.best);		/changed
        //try
        //{

            //for(int i=0;i<number;i++)
            //{
               
                //for(int j=0; file_output[i][j] !=null;j++)
                //{
		//System.out.println("sent " + file_output[0][0] );
		return file_output[0][0];
		//String outfile = "/op_ss_" + args[0]; 
		//FileWriter filewriter = new FileWriter(outfile);
                                    
		//filewriter.write(file_output[0][0]);		
		//System.out.println(file_output[0][0]);
                    
                //}
            //}
        //}
        //catch(IOException e)
        //{
          //  System.out.println("could not write file");
        //}
	}
}
