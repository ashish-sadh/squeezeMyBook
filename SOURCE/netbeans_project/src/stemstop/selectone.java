/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package stemstop;
import java.io.*;
import java.util.StringTokenizer;
/**
 *
 * @author compaq
 */
public class selectone {
        String[] sentence=new String[1000];
             int[][] index=new int[1000][1000];
             int groupcount=0;
			 float [] groupsum=new float[1000];
			 float [] sentences=new float[1000];
        public void rank(filter d)
        {
            System.out.println("reading group sentences");
            String filename="";
             
            try
            {
                filename="./misc/output_cosine/cluster.txt";
                BufferedReader br=new BufferedReader(new FileReader(filename));
                String strLine;
                try
                {
                    while ((strLine = br.readLine()) != null)
                    {
                    // Print the content on the console
                        //System.out.println (strLine);

                        int si=strLine.indexOf("<g>");
                        int count=-1;
                        String[] groupsent=new String[1000];
                        int[][] groupindex=new int[1000][1000];

                        if(si!=-1)
                        {
                            strLine=br.readLine();
                            int ei=strLine.indexOf("</g>");
                            while(ei==-1)
                            {


                                String sentnum="";
                                 String docnum="";
                                int i=0;
                                int flag2=0;
                                while(i<strLine.length())
                                {
                                    if(strLine.indexOf("<")!=-1 && flag2==0)
                                    {
                                        count++;
                                        flag2=1;
                                        groupsent[count]="";
                                        if(strLine.charAt(i)=='<')
                                        {
                                            i++;
                                            sentnum="";
                                            docnum="";
                                            int flag=0;
                                            while(strLine.charAt(i)!='>')
                                            {

                                                while(strLine.charAt(i)!=','&&flag==0)
                                                {
                                                 docnum=docnum+strLine.charAt(i);
                                                    i++;
                                                }
                                                //System.out.println(count+docnum);
                                                i++;
                                                flag=1;
                                                if(strLine.charAt(i)!='>')
                                                sentnum=sentnum+strLine.charAt(i);
                                            }
                                             //System.out.println(sentnum);
                                        }
                                        i++;
                                        groupindex[count][0]=Integer.parseInt(docnum);
                                        groupindex[count][1]=Integer.parseInt(sentnum);
                                        groupindex[count][0]=groupindex[count][0]-1;
                                        groupindex[count][1]=groupindex[count][1]-1;
                                        //System.out.println(groupindex[count][0]);
                                    }
                                    if(i<strLine.length())
                                    {
                                        char temp=strLine.charAt(i);
                                        groupsent[count]=groupsent[count]+temp;
                                        //System.out.println(groupsent[count]);
                                        i++;
                                    }

                                }
                                strLine=br.readLine();
                                ei=strLine.indexOf("</g>");
                            }
                        }
                        float min=-1.0F;
                        int sentnum=300;
						groupsum[groupcount]=0.0F;
                        for(int i=0;i<groupsent.length && groupsent[i]!=null;i++)
                        {
                            //System.out.println("group no:"+groupcount);
                            System.out.println(groupindex[i][0]+","+groupindex[i][1]+":"+groupsent[i]);
                            
                            if(groupindex[i][0]==d.best)
                            {
								if(sentnum>groupindex[i][1])
									sentnum=groupindex[i][1];
                            }
                            float val=retbest(d,groupsent[i]);
							groupsum[groupcount]=groupsum[groupcount]+val;
                            if(val>min)
                            {
                                min=val;
                                System.out.println("group no:"+groupcount);
                                sentence[groupcount]="";
                                sentence[groupcount]=groupsent[i];
                                index[groupcount][0]=groupindex[i][0];
                                index[groupcount][1]=groupindex[i][1];
                                index[groupcount][2]=sentnum;
								sentences[groupcount]=(float)groupindex[i][1]/d.count[groupindex[i][0]];
                            }

                        }
                        groupcount++;
                    }
                }
                catch (IOException e)
                 {
                    System.out.println("error reading " + filename);
                 }
            }
            catch (FileNotFoundException e)
            {
                System.out.println("file " + filename + " not found");
            }
        }
        public float retbest(filter d,String group)
        {
            float sum=0.0F;
            StringTokenizer stk=new StringTokenizer(group," ");
            while(stk.hasMoreTokens())
            {
                String s=stk.nextToken();
                Float freq=d.tdf_final.get(s);
                if(freq!=null)
                    sum=sum+freq;
            }
            return sum;
        }
        
}
