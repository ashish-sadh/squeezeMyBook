
package composeHtml;

import java.io.*;

public class compose {
    public static void main() {
	String dir_path = "./misc/final_summary/";
	String summary = "";
        try {
			File f = new File(dir_path + "final_summary.txt");
			BufferedReader bf=new BufferedReader(new FileReader(f));
			String pick_one = bf.readLine();
			//double no_fig= ((double)Gui.Gui.get())/100;
			while(pick_one  != null) {
				summary += pick_one + "<br>\n";
				pick_one = bf.readLine();
				System.out.println("Composing started....");
			}
                        
			bf.close();
                        String imgname = "";
                        if(summary.contains("FIGURE_TAG")&& summary.contains("CLOSE_GIGURE_TAG") ){
                            imgname = summary.substring(summary.indexOf("doc"),summary.indexOf("CLOSE") - 1);
                            System.out.println(imgname + "this is what the reality");
                            summary = summary.replace("FIGURE_TAG " + imgname + " CLOSE_FIGURE_TAG","");
                        }
			System.out.println(summary);
			f = new File(dir_path + "figure_ranking.txt");
			bf=new BufferedReader(new FileReader(f));
			pick_one = bf.readLine();
                        pick_one = bf.readLine();
			while(pick_one  != null) {
				String [] numbers = pick_one.split("\t");
				int doc_num = Integer.parseInt(numbers[0]);
				int img_num = Integer.parseInt(numbers[1]);
				String ref_name = "REF_IMG "+(doc_num-1)+"."+img_num;
				String dref_name = "DREF_IMG"+(doc_num-1)+"."+img_num;
				BufferedReader bf3 = new BufferedReader(new FileReader("./misc/doc_parsing/doc_"+(doc_num-1)+"/doc"+(doc_num-1)+"_img"+img_num +"_caption.txt"));
				String sent_one = bf3.readLine();
				String raw_img_code = "<p>";
				while(sent_one != null) {
					String caption = sent_one;
					caption = caption.replace("<s>", "");
                                	caption = caption.replace("</s>", "");
					String dir_ref = new String();
					int c1 = -1;
					int t1 = caption.indexOf(":");
					if(t1 >0){
						char buf[] = new char[t1];
						caption.getChars(0,t1,buf,0);
						caption = new String(buf);
						caption = sent_one.trim();
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
					raw_img_code+= sent_one.replace(dir_ref,dref_name) + "\n";
					sent_one = bf3.readLine();
					//System.out.println(sent_one);
				}
                                System.out.println("not expected this bug");
				raw_img_code= raw_img_code.replace("<s>","");
				raw_img_code = raw_img_code.replace("</s>","");
				raw_img_code +="</p>";
				bf3.close();
				BufferedReader bf2 = new BufferedReader(new FileReader("./misc/doc_parsing/doc_"+(doc_num-1)+"/doc"+(doc_num-1)+"_img"+img_num));
				sent_one = bf2.readLine();
				while(sent_one != null) {
					raw_img_code+= sent_one + "\n";
					sent_one = bf2.readLine();
					System.out.println(sent_one);
				}
				bf2.close();
				int idx = 0;

				if( (idx = summary.indexOf(ref_name, idx)) != -1){
					summary = summary.substring(0,idx) + ref_name +"<br>"+raw_img_code + summary.substring(idx+ref_name.length(),summary.length());
				}else if ((idx = summary.indexOf(dref_name, idx)) != -1){
					summary = summary.substring(0,idx) + dref_name +"<br>"+raw_img_code + summary.substring(idx+dref_name.length(),summary.length());
				}
				pick_one = bf.readLine();
			}
			bf.close();
			bf = new BufferedReader(new FileReader(dir_path + "table_ranking.txt"));
			pick_one = bf.readLine();
                        pick_one = bf.readLine();
			while(pick_one  != null) {
				String [] numbers = pick_one.split("\t");
				int doc_num = Integer.parseInt(numbers[0]);
				int table_num = Integer.parseInt(numbers[1]);
				String ref_name = "REF_TABLE "+(doc_num-1)+"."+table_num;
				String dref_name = "DREF_TABLE "+(doc_num-1)+"."+table_num;
				BufferedReader bf3 = new BufferedReader(new FileReader("./misc/doc_parsing/doc_"+(doc_num-1)+"/doc"+(doc_num-1)+"_table"+table_num +"_caption.txt"));
				String sent_one = bf3.readLine();
				//String sent_one;
				String raw_table_code = "<p>";
				while(sent_one != null) {
					String caption = sent_one;
					caption = caption.replace("<s>", "");
                                	caption = caption.replace("</s>", "");
					String dir_ref = new String();
					int c1 = -1;
					int t1 = caption.indexOf(":");
					if(t1 >0){
						char buf[] = new char[t1];
						caption.getChars(0,t1,buf,0);
						caption = new String(buf);
						caption = sent_one.trim();
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
						raw_table_code += sent_one;
           				}else{
					raw_table_code+= sent_one.replace(dir_ref,dref_name) + "\n";}
					sent_one = bf3.readLine();
					//System.out.println(sent_one);
				}
				raw_table_code= raw_table_code.replace("<s>","");
				raw_table_code = raw_table_code.replace("</s>","");
				raw_table_code +="</p>";
				bf3.close();	
				BufferedReader bf2 = new BufferedReader(new FileReader("./misc/doc_parsing/doc_"+(doc_num-1)+"/doc"+(doc_num-1)+"_table"+table_num));
				sent_one = bf2.readLine();
				sent_one = bf2.readLine();
				raw_table_code += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"4\">";
				while(sent_one != null) {
					raw_table_code+= sent_one + "\n";
					sent_one = bf2.readLine();
					System.out.println(sent_one);
				}
				bf2.close();
				int idx = 0;

				if( (idx = summary.indexOf(ref_name, idx)) != -1){
					summary = summary.substring(0,idx) + ref_name +"<br>"+raw_table_code + summary.substring(idx+ref_name.length(),summary.length());
				}else if ((idx = summary.indexOf(dref_name, idx)) != -1){
					summary = summary.substring(0,idx) + dref_name +"<br>"+raw_table_code + summary.substring(idx+dref_name.length(),summary.length());
				}		
				pick_one = bf.readLine();
			}
			bf.close();
			FileWriter filewrite = null;
			filewrite = new FileWriter(dir_path + "final.html");
			filewrite.write("<html> \n <body> \n ");
			filewrite.write(summary);
			filewrite.write(" </body> \n </html>");
			filewrite.close();
			filewrite = null;
		}catch(FileNotFoundException e){
			System.out.println("exception: " + e.getMessage());
			e.printStackTrace();
			System.out.println("can't find final summary");
		}catch(IOException e){
            		System.out.println(e);
		}
    }

}
