/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stemstop;
import java.io.*;
import java.util.*;

/**
 *
 * @author compaq
 */
public class filter {
   HashMap <String,Float> tdf=new HashMap();
   HashMap <String,Float> tdf_final=new HashMap();
   String[][] readfile=new String[1000][10000];
   //String[][] inputfile=new String[1000][1000];
   int totalsent;
   int best;
   int totaldoc;
   int[] count=new int[1000];
    public String[][] process1(int number, String[] name)
    {
        totaldoc=number;
        String start="<s>";
        String end="</s>";
        
        int ch;
        totalsent=0;
        String filename="";
        for(int i=0;i<number;i++)
        {
        try
        {
            filename="";
            filename=name[i];
            //filename="D:\\study\\6th sem\\project\\stemstop\\src\\stemstop\\"+"output"+Integer.toString(i+1)+".txt";
            FileInputStream in = new FileInputStream(filename);
            count[i]=0;
            try
            {
                ch=1;
                String temp=new String();
                temp="";
                readfile[i][count[i]]="";
                //inputfile[i][count[i]]="";
                int flag=0;
                while(ch>0)
                {
                     ch=in.read();
                     temp=temp+(char)ch;
                     int si=temp.indexOf(start);
                     int ei=temp.indexOf(end);
                     if(flag==1)
                     {
                         //inputfile[i][count[i]]=inputfile[i][count[i]]+(char)ch;
                         if(Character.isLetter( (char)ch) || ch==' '||ch=='<'||ch=='>'||ch=='/')
                         readfile[i][count[i]]=readfile[i][count[i]]+(char)ch;
                         //if(Character.isLetter( (char)ch) || ch==' ')
                           // readfile[i][count[i]]=readfile[i][count[i]]+(char)ch;
                     }
                     if(si!=-1)
                     {
                        flag=1;
                        temp="";
                      }
                      if(ei!=-1)
                      {
                         // inputfile[i][count[i]]=inputfile[i][count[i]].replace(end, "");
                           readfile[i][count[i]]=readfile[i][count[i]].replace(end, "");
                           //System.out.println(readfile[i][count[i]]);
                            //readfile[i][count[i]]=readfile[i][count[i]].replace(end, "");
                          //readfile[i][count[i]]=readfile[i][count[i]]+inputfile[i][count[i]];
                            totalsent=totalsent+1;
                            count[i]++;
                            readfile[i][count[i]]="";
                            //inputfile[i][count[i]]="";
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
        /*for(int i=0;i<count;i++)
        {
            System.out.println(readfile[i]);
        }*/
       // System.out.println(readfile);
        //Map<String,String>[] stemword = new Map[10];
        for(int j=0;j<number;j++)
        {
			double numberintro=0.1*count[j];
            for(int i=0;i<count[j];i++)
            {
                StopWord d=new StopWord();
                String result="";
                try
                {
                    result=d.stopRemove(readfile[j][i]);
                 }catch(IOException e) {
                    System.out.println("error in stop word removal");
                 }
                readfile[j][i]="";
                readfile[j][i]=readfile[j][i]+result;
                StringTokenizer stk=new StringTokenizer(readfile[j][i]," ");
                //Map<String,String> temp = new LinkedHashMap<String,String>();
                String result1 ="";
                result="";
                while(stk.hasMoreTokens())
                {

                    String s1=stk.nextToken();
                    Float freq=tdf.get(s1);
                    if(freq==null)
                    {
                        if(i>numberintro)
                            tdf.put(s1, new Float(1));
                        else
                            tdf.put(s1, new Float(1.2));
                    }
                    else
                    {
                        if(i>numberintro)
                            tdf.put(s1, freq+1);
                        else
                            tdf.put(s1, freq+1.2f);
                    }
                    Porter s=new Porter();
                    String s2=s.stripAffixes(s1);
                   /* if(s1.compareTo(s2)!=0)
                        temp.put(s1, s2);*/
                     result=result+" "+s2;
                }
                //stemword[j]=temp;
                readfile[j][i]="";
                 readfile[j][i]=readfile[j][i]+result;
                //readfile[j][0][="";
            }
        }

      return readfile;
    }
    public void create_tdf()
    {
         //ArrayList<Integer> values = new ArrayList<Integer>();
         //values.addAll(tdf.values());
        // and sorting it (in reverse order)
        //Collections.sort(values, Collections.reverseOrder());
        //for (Integer i : values)
        //{
            System.out.println(totalsent);
            for (String s : tdf.keySet())
            {
                Float freq=tdf.get(s);
                freq=freq/totalsent;
                tdf.put(s,freq);
                if(freq>=0.1)
                {
                    tdf_final.put(s, freq);
                }
            }
            tdf.clear();

        //}

    }
    public void findbest()
    {
        float min=0.0F;
        for(int i=0;i<totaldoc;i++)
        {
            float sum=0.0F;
            for(int j=0;j<count[i];j++)
            {
                String line="";
                line=readfile[i][j];
                 StringTokenizer stk=new StringTokenizer(line," ");
                while(stk.hasMoreTokens())
                {
                    String s=stk.nextToken();
                    Float freq=tdf_final.get(s);
                    if(freq!=null)
                        sum=sum+freq;
                }
            }
            if(sum>min)
            {
                min=sum;
                best=i;
            }
        }
    }
}
