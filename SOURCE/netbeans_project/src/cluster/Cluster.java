
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cluster;

import java.io.*;
import java.util.*;


/**
 *
 * @author devsri
 */
public class Cluster {

    /**
     * @param args the command line arguments
     */
    public static void main(int num_file) {
        try
        {
            String in_name;
            
            int num = 1;
            String s1,s2;
            Writer output = null;
            File file = new File("./misc/output_stem/sent.txt");
            output = new BufferedWriter(new FileWriter(file));       
            while(num <= num_file)
            {
                String data_name = "./misc/output_stem/stemstopout";
                data_name = data_name + num + ".txt";
                FileInputStream fstream = new FileInputStream(data_name);
                DataInputStream f2 = new DataInputStream(fstream);
                int line_count = 1;
                while(f2.available() != 0)
                {
                    s1 = "<" + num + "," + line_count + ">";
                    s2 = f2.readLine();
                    s1 = s1 + s2 + "\n";
                    output.write(s1);
                    line_count++;
                }
                num++;
            }
            output.close();

            FileInputStream fstream = new FileInputStream("./misc/output_stem/sent.txt");
            DataInputStream in = new DataInputStream(fstream);
            int index=0,i,j;
            ArrayList<ArrayList<String> > vvs =new ArrayList<ArrayList<String> >();
            ArrayList<String> vs=new ArrayList<String>();
            s1=in.readLine();
            vs.add(s1);
            vvs.add(vs);
            while (in.available() !=0)
            {
                s2=in.readLine();
                double max=0.0;
		for(i=0;i<vvs.size();i++){
                    double sum=0.0;
                    for(j=0;j<vvs.get(i).size();j++){
                       sum = sum + compare(s2,vvs.get(i).get(j).toString());
                    }
                    sum=(float)sum/j;
                    if(sum > max){
                            max=sum;
                            index=i;
                    }
                }
		if(max > 0.3){
                    ArrayList<String> t=(ArrayList<String>) vvs.get(index);
                    t.add(s2);
                    vvs.remove(index);
                    vvs.add(t);
                    index=0;
                }
		else{
                    ArrayList<String> t= new ArrayList<String>();
			t.add(s2);
			vvs.add(t);
		}
               
            }
           in.close();

           Writer write_output = null;
           File output_file = new File("./misc/output_cosine/cluster.txt");
           write_output = new BufferedWriter(new FileWriter(output_file));


            for(i=0;i<vvs.size();i++){
                write_output.write("<g>\n");
                for(j=0;j<vvs.get(i).size();j++){
                    String temp = vvs.get(i).get(j) + "\n";
                    write_output.write(temp);
                }
                write_output.write("</g>\n");
            }
            write_output.close();
        }
        catch (Exception e)
        {
            System.err.println("File input error" );
        }
    }

    
    public static double compare(String s1, String s2) {

        String[] word1,word2;
        word1=null;
        word2=null;
        int index;
        index = s1.indexOf('>');
        s1 = s1.substring(index+1, s1.length());

        index = s2.indexOf('>');
        s2 = s2.substring(index+1, s2.length());

        Map<String, int[]> m_val = new TreeMap<String, int[]>();
	int[] temp=new int[2];
	int min=s1.length();
	int flag=0,i=0;
	int sum =0,sum1=0,sum2=0;
	if(min > s2.length())
		min=s2.length();
        word1=s1.split("\\s"); 
        word2=s2.split("\\s");
        for(i=0;i<word1.length;i++){            
            m_val=update(m_val,word1[i],0);

        }
        for(i=0;i<word2.length;i++){
            m_val=update(m_val,word2[i],1);
        }
        Iterator it = m_val.keySet().iterator();
	while(it.hasNext()){
            String key = it.next().toString();
            temp=m_val.get(key);
            sum+=temp[0]*temp[1];
            sum2+=temp[1]*temp[1];
            sum1+=temp[0]*temp[0];
            temp=null;
        }
	double val;
	val=(double)sum/Math.sqrt(sum1*sum2);
        return val;
    }

    public static Map<String, int[]> update(Map<String, int[]> m, String word, int i){
        int[] temp = new int[2];
        if(m.get(word) != null){
            temp=m.get(word);
        }
        else{
            temp[0]=0;
            temp[1]=0;
	}
	temp[i]++;
        
	m.put(word, temp);
        return m;
    }

}
