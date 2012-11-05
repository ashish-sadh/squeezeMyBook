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

import java.util.Scanner;
//import findcaption.*;
import java.io.*;
//import mapImgSen.MapCapScore;
import stemstop.filter;
import stemstop.selectone;
 
class FindCount{
	static int count_ref(int doc_no,int comp_no, String inputfile[][], String dir_ref,String dref_id){
		try {
	
			String pick_one = new String();
			int total = 0;
                        int i=0;
			while(inputfile[doc_no][i]  != null) {
                                int idx = 0;
				/*if(pick_one.contains(dir_ref)){
					total++;
				}*/

                                while ((idx = inputfile[doc_no][i].indexOf(dir_ref, idx)) != -1){
                                    idx++;
                                    inputfile[doc_no][i] = inputfile[doc_no][i].replace(dir_ref,dref_id +doc_no+"."+comp_no);
                                    total++;
                                }
                                i++;
                                //System.out.println(s);
                        }
                        //System.out.println("here is pick_one " + pick_one);
                        File f = new File("misc/doc_parsing/doc_" + doc_no + File.separator + "doc"+doc_no+ "_img" + comp_no+"_caption.txt");
			BufferedReader bf=new BufferedReader(new FileReader(f));
                        pick_one = bf.readLine();
                        //FileReader filerd = new FileReader("misc/doc_parsing/doc_" + doc_no + File.separator + "doc"+doc_no+ "_img" + comp_no+"_caption.txt");
                        //Scanner scan = new Scanner(filerd);
                        //pick_one = scan.nextLine();
                        //while(pick_one == null){
                        System.out.println("here is again pick_one " + pick_one);
                        //scan.close();
                        //filerd.close();
                        pick_one.replace(dir_ref,dref_id +doc_no+"."+comp_no);
                        bf.close();
                        FileWriter ofstream = new FileWriter("misc/doc_parsing/doc_" + doc_no + File.separator + "doc"+doc_no+ "_img" + comp_no+"_caption.txt",true);
                        ofstream.write(pick_one);
                        
              
                        ofstream.close();
                        pick_one = bf.readLine();
                        //}
                        return (total);
		}catch(Exception e){
			System.out.println("exception: " + e.getMessage());
			e.printStackTrace();
			System.out.println("exception in the stop word removal");
                        return 0;
		}
                
        }
}
		
	
public class FindCaption{
        public MapCapScore mapcapscore1;
        public MapCapScore mapcapscore2;
        public FindCaption(){
              mapcapscore1 = new MapCapScore();
              mapcapscore2 = new MapCapScore();
        }
	public void func(int ndoc, filter ter_freq, String inputfile[][]){
	
	String foldername = "";
	String filename = "";

	try{
	for(int i = 0;i < ndoc;i++){
		filename = "doc"+i;
		foldername = "misc/output_for_stem";
		filename = foldername + File.separator + filename;

		//MapCapScore mapcapscore = new MapCapScore();
		FileReader fileread = new FileReader(filename + ".txt");
		Scanner scanner = new Scanner(fileread);
                FileWriter filewrite = null;
		//BufferedReader buffread = new BufferedReader(fileread); 
		String strnext = "am";
		String strprev = "am";
		String imgname = "am";
                int imgno;
                System.out.println("workedonce");

		while (scanner.hasNextLine()){
        		strnext = "";
                        while(scanner.hasNextLine()){
                                    String cmpline = scanner.nextLine();
                                    strnext += cmpline;
                                    if(strnext.contains("</s>")){
                                        System.out.println("file " + strnext);
                                        break;
                                    }
                                }
                        //strnext = scanner.nextLine();
			if(strnext.contains("FIGURE_TAG")){
				//System.out.println("");
                                imgname = strnext;
                                int num = 0;
                                num = imgname.indexOf("CLOSE");
                                System.out.println("hiwhat" + num);
				imgname = imgname.substring(imgname.indexOf("doc"),num - 1);

                                strnext = "";
                                while(scanner.hasNextLine()){

                                    String cmpline = scanner.nextLine();
                                    strnext += cmpline;
                                    if(cmpline.contains("</s>")){
                                        System.out.println("hey2 " + strnext);
                                        break;
                                    }
                                }

				while(strnext.contains("FIGURE_TAG") ){

                                    FileWriter ofstream = new FileWriter("misc/doc_parsing/doc_" + i + File.separator + imgname,true);
                                    System.out.println(imgname);
                                    int num1 = 0;
                                    num1 = strnext.indexOf("CLOSE");
                                    System.out.println("hiwhat" + num1);
                                    strnext = strnext.substring(strnext.indexOf("doc"),num1 - 1);
                                    FileReader ifstream = new FileReader("misc/doc_parsing/doc_" + i + File.separator + strnext);
                                    Scanner scan = new Scanner(ifstream);
                                    while(scan.hasNextLine()){
                                        ofstream.write(scan.nextLine());
                                    }
                                    ofstream.close();
                                    ifstream.close();
                                    scan.close();

                                    strnext = "";
                                    while(scanner.hasNextLine()){
                                        String cmpline = scanner.nextLine();
                                        strnext += cmpline;
                                        if(cmpline.contains("</s>")){
                                            System.out.println("hey2 " + strnext);
                                            break;
                                        }
                                    }
                                }

		

                                System.out.println("hello " + imgname);
                                imgname = "misc/doc_parsing/doc_" + i + File.separator + imgname;				
				//System.out.println("creating file");
                                
                                filewrite = new FileWriter(imgname + "_caption.txt");
                                System.out.println("file created " + strprev + "he");
                                
				String caption = ""; 
				if(strprev.contains("diag") || strprev.contains("fig") || strprev.contains("Diag") || strprev.contains("Fig") || strprev.contains(":")) {//strprev.charAt(tempstrprev.length() - 1) == ':'){
					System.out.println("hey " + strprev);
                                        System.out.println("something");
					caption = strprev;
                                        
				}
                                if(strnext.contains("diag") || strnext.contains("fig") || strnext.contains("Diag") || strnext.contains("Fig") || strnext.contains(":")){//tempstrnext.charAt(tempstrnext.length() - 1) == ':'){
					System.out.println("hey " + strprev);
                                        System.out.println("something");
					caption = strnext;
                                        //filewrite.write("<s>" + strnext + "</s>");
					//filewrite.write(strnext);
				}else{
                                        System.out.println("hey " + strprev);
					System.out.println("something");
					caption = strprev;
                                        //filewrite.write("<s>" + strprev + "</s>");
				}
                                
                                filewrite.write(caption);
                                
                                caption = caption.replace("<s>", "");
                                caption = caption.replace("</s>", "");
				String dir_ref = new String();
				int c1 = -1;
				int t1 = caption.indexOf(":");
				if(t1 >0){
					char buf[] = new char[t1];
					caption.getChars(0,t1,buf,0);
					caption = new String(buf);
					caption = caption.trim();
				}
				String [] words = caption.split(" ");
                                int other_bit = 0;
				for(int j=0;j<words.length;j++){
					if( words[j].contains("Fig") || words[j].contains("fig")){
						c1 = j;
						break;
					}
				}
				if( c1>=0 ){
                                        other_bit = 1;
					dir_ref += words[c1++];
                                        while(words[c1].equals(" ")){
                                            c1++;
                                        }

					if(c1 <words.length){
                                            dir_ref+= " " + words[c1];
                                            c1++;
					}
				}
                                if(other_bit == 0){
                                    dir_ref = caption;
                                }
                                int dref_count = 0;
                                int pos;
                                pos = imgname.indexOf("img");
                                imgno = Integer.parseInt(imgname.substring(pos+3,imgname.length()));

				if(!dir_ref.equals("")){
                                        System.out.println("direct reference is "+ dir_ref);
                                        dref_count = FindCount.count_ref(i,imgno,inputfile,dir_ref,"DREF_IMG");
					System.out.println(dref_count + " hey thats the fig count");

				}
                                System.out.println("file written");
                                
				filewrite.close();
                                filewrite = null;
                                System.out.println("workedonce");
				mapcapscore1.func(imgname,i,ter_freq,dref_count, inputfile,false);
                                System.out.println("workedtwo");
			}
                        if(strnext.contains("TABLE_TAG")){
				//System.out.println("");
                                imgname = strnext;

                                strnext = "";
                                while(scanner.hasNextLine()){

                                    String cmpline = scanner.nextLine();
                                    strnext += cmpline;
                                    if(strnext.contains("</s>")){
                                        System.out.println("file " + strnext);
                                        break;
                                    }
                                }
				while(strnext.contains("TABLE_TAG") ){
                                    strnext = "";
                                    while(scanner.hasNextLine()){

                                        String cmpline = scanner.nextLine();
                                        strnext += cmpline;
                                        if(strnext.contains("</s>")){
                                            System.out.println("file " + strnext);
                                            break;
                                        }
                                    }
                                }

                                int num = 0;
                                num = imgname.indexOf("CLOSE");
                                System.out.println("hiwhat" + num);
				imgname = imgname.substring(imgname.indexOf("doc"),num - 1);
                                System.out.println("hello " + imgname);
                                imgname = "misc/doc_parsing/doc_" + i + File.separator + imgname;
				//System.out.println("creating file");

                                filewrite = new FileWriter(imgname + "_caption.txt");
                                System.out.println("file created " + strprev + "he");

				/*tempstrprev = "am";
				tempstrnext = "am";
				tempstrprev += strprev;
				tempstrnext += strnext;*/
				String caption = "";
				if(strprev.contains("Table") || strprev.contains("table") || strprev.contains("TABLE") || strprev.contains(":")) {//strprev.charAt(tempstrprev.length() - 1) == ':'){
					System.out.println("hey " + strprev);
                                        System.out.println("something");
					caption = strprev;

				}
                                if(strnext.contains("Table") || strnext.contains("table") || strnext.contains("TABLE") || strnext.contains(":")){//tempstrnext.charAt(tempstrnext.length() - 1) == ':'){
					System.out.println("hey " + strprev);
                                        System.out.println("something");
					caption = strnext;
                                        //filewrite.write("<s>" + strnext + "</s>");
					//filewrite.write(strnext);
				}else{
                                        System.out.println("hey " + strprev);
					System.out.println("something");
					caption = strprev;
                                        //filewrite.write("<s>" + strprev + "</s>");
				}
                                if(caption.contains("<s>") && caption.contains("</s>") ){
                                    filewrite.write(caption);
                                }else if(caption.contains("<s> ")){
                                    filewrite.write(caption + " </s>");
                                }else if(caption.contains("</s> ")){
                                    filewrite.write("<s> " + caption);
                                }else{
                                    filewrite.write("<s> " + caption + " </s>");
                                }
                                caption = caption.replace("<s>", "");
                                caption = caption.replace("</s>", "");

				String dir_ref = new String();
				int c1 = -1;
				int t1 = caption.indexOf(":");

				if(t1 >0){
					char buf[] = new char[t1];
					caption.getChars(0,t1,buf,0);
					caption = new String(buf);
					caption = caption.trim();
				}
				String [] words = caption.split(" ");
                                int other_bit = 0;
				for(int j=0;j<words.length;j++){
					if( words[j].contains("Table") || words[j].contains("table") || words[j].contains("TABLE")){
						c1 = j;
						break;
					}
				}
				if( c1>=0 ){
                                        other_bit = 1;
					dir_ref += words[c1++];
                                        while(words[c1].equals(" ")){
                                            c1++;
                                        }

					if(c1 <words.length){
                                            dir_ref+= " " + words[c1];
                                            c1++;
					}
				}
                                if(other_bit == 0){
                                    dir_ref = caption;
                                }
                                int dref_count = 0;
                                int pos;
                                int tbl_no =0;
                                pos = imgname.indexOf("table");
                                tbl_no = Integer.parseInt(imgname.substring(pos+5,imgname.length()));
				if(!dir_ref.equals("")){
                                        System.out.println("direct reference is "+ dir_ref);
                                        dref_count = FindCount.count_ref(i,tbl_no,inputfile,dir_ref,"DREF_TABLE");
					System.out.println(dref_count + " hey thats the fig count");

				}
                                System.out.println("file written");

				filewrite.close();
                                filewrite = null;
                                System.out.println("workedonce");
				mapcapscore2.func(imgname,i,ter_freq,dref_count, inputfile,true);
                                System.out.println("workedtwo");
			}
			strprev = strnext;
                        
                }
                
		scanner.close();
		fileread.close();
		selectone vc = new selectone();
		System.out.println(vc.retbest(ter_freq,"fig sampl graph color contrast well screen blackandwhit hardcopyfig exampl lowresolu imag accept fig exampl imag adequ resolu") + " yahi hai bas kam ka ********");
		/*Set set = mapcapscore.mapdocimg_sen.entrySet();
			Iterator it = set.iterator();
			
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				System.out.println(((docimg)me.getKey()).docno + " " + ((docimg)me.getKey()).imgno + " :");
				System.out.println(me.getValue());
			}
		*/
	}
        System.out.println("workinghere0");
        sortImage.func(mapcapscore1.mymap1, false);
        sortImage.func(mapcapscore2.mymap1, true);
	}
	catch(FileNotFoundException e){
            System.out.println(e);
	}
	catch(IOException e){
            System.out.println("could not read file");
	}
	//Writer filewrite = new FileWriter(filename);			

	
	}
}
