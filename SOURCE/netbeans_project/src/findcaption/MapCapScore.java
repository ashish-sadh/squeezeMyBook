/*Copyright 2011 Amit Sahu, Ashish Sadh
This file is part of odtsummarizer.

    Odtsummarizer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Odtsummarizer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Odtsummarizer.  If not, see <http://www.gnu.org/licenses/>

*/

package findcaption;

//import findcaption.*;
import java.util.*;
import java.io.*;
import cluster.Cluster;
import java.util.Scanner;
//import captionstemstop.Captionstemstop;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import stemstop.filter;
import stemstop.selectone;

public class MapCapScore{
        public mymaps mymap1;
	/*public Map<docimg,Object > mymap1.mapdocimg_sen;
	public Map<docimg,countscore> mymap1.mapdocimg_countscore;*/
        public MapCapScore(){
                mymap1 = new mymaps();
		mymap1.mapdocimg_sen = new HashMap<docimg,Object >();
		mymap1.mapdocimg_countscore = new HashMap<docimg,countscore>();
	}

	public void func(String cap_loc,int docno, filter ter_freq,int dref_count, String inputfile[][],boolean table){
		try{
                        FileWriter filewrite = null;
			String captionlocation = cap_loc + "_caption.txt";
			String filelocation = "misc/output_stem/stemstopout" + (docno + 1) + ".txt"; // location of document made after stem stop
			selectone sen_sc_gen = new selectone();			

			//Map<docimg,Object > mymap1.mapdocimg_sen = new HashMap<docimg,Object >();
			docimg key = new docimg();
			countscore csval = new countscore();
                        csval.dref = dref_count;
			key.docno = docno;

			int pos;
                        if(table){
                            pos = cap_loc.indexOf("table");
                            key.imgno = Integer.parseInt(cap_loc.substring(pos+5,cap_loc.length()));
                            filewrite = new FileWriter("./misc/output_for_imgsim/tablecapsim"+"_" + docno+ "." + key.imgno);
                        }else{
                            pos = cap_loc.indexOf("img");
                            key.imgno = Integer.parseInt(cap_loc.substring(pos+3,cap_loc.length()));
                            filewrite = new FileWriter("./misc/output_for_imgsim/imgcapsim"+"_" + docno+ "." + key.imgno);
                        }

                        Vector<Object> sen = new Vector<Object>(1,10);
			sen.add(0.0);			
			
			String [] args = new String[1];
			args[0] = captionlocation;
			String cap_stemstop = Captionstemstop.func(args);
			System.out.println(cap_stemstop + " here");
			cap_stemstop = "<1,1>" + cap_stemstop;
			System.out.println(cap_stemstop);
                        System.out.println(filelocation + " hello");
			FileReader fileread = new FileReader(filelocation);
			Scanner scanner = new Scanner(fileread);
			String doc_sentence = "";
			double total_cos_sim = 0;
			double cos_sim = 0;
			int sen_no = 0;
                        double dref_senscore = 0;
                        double indref_senscore = 0;

                        while (scanner.hasNextLine()){
				sen_no++;
        			doc_sentence = (scanner.nextLine());
				String stem_sen = doc_sentence;
				doc_sentence = "<" + docno +","+sen_no+">" + doc_sentence;
				//System.out.println(doc_sentence);
				//System.out.println(cap_stemstop + "\n");
				cos_sim = Cluster.compare(cap_stemstop,doc_sentence);
				//System.out.println(cos_sim);
                                
				if(cos_sim > 0.3){
					filewrite.write(doc_sentence+"\n");
                                        sen.add(sen_no);
                                        if(table){
                                            inputfile[// <editor-fold defaultstate="collapsed" desc="comment">
                                                    // </editor-fold>
docno][sen_no] += "REF_TABLE " + docno + "." + key.imgno;
                                        }else{
                                            inputfile[docno][sen_no] += "REF_IMG " + docno + "." + key.imgno;
                                        }
                                        if(cos_sim > 0.9){
                                            dref_senscore += sen_sc_gen.retbest(ter_freq,stem_sen);
                                        }else{
                                            indref_senscore += sen_sc_gen.retbest(ter_freq,stem_sen);
                                        }
					//csval.indref += cos_sim;
				}
				total_cos_sim += cos_sim;		
			}
                        csval.indref = total_cos_sim - csval.dref;
			csval.indref *= indref_senscore;//local score *global score
                        csval.dref *= dref_senscore;//local score *global score
                        System.out.println(cos_sim);
			sen.setElementAt(total_cos_sim/sen_no,0);
			//sen[0] = total_cos_sim;
			mymap1.mapdocimg_sen.put(key,sen);
			mymap1.mapdocimg_countscore.put(key,csval);
			
			fileread.close();
			scanner.close();
			//printing map
			System.out.println("\n\n\nprinting");			
			Set set = mymap1.mapdocimg_sen.entrySet();
			Set set1 = mymap1.mapdocimg_countscore.entrySet();
			Iterator it = set.iterator();
			Iterator it1 = set1.iterator();
			
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				System.out.println(((docimg)me.getKey()).docno + " " + ((docimg)me.getKey()).imgno + " :");
				System.out.println(me.getValue());
			} 
			System.out.println("printing count and score");

			while(it1.hasNext()){
				Map.Entry me = (Map.Entry)it1.next();
				System.out.println(((docimg)me.getKey()).docno + " " + ((docimg)me.getKey()).imgno + " :");
				System.out.println(((countscore)me.getValue()).indref + "   " + ((countscore)me.getValue()).dref);
			} 
			System.out.println("printed\n\n\n");
			//printed
                        filewrite.close();
                        return;
		}
		catch(IOException e)
        	{
            		System.out.println("could not read file");
        	}
				
		 	
	}
}


