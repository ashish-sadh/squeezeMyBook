// Jericho HTML Parser - Java based library for analysing and manipulating HTML
// Version 3.1
// Copyright (C) 2004-2009 Martin Jericho
// http://jericho.htmlparser.net/
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of either one of the following licences:
//
// 1. The Eclipse Public License (EPL) version 1.0,
// included in this distribution in the file licence-epl-1.0.html
// or available at http://www.eclipse.org/legal/epl-v10.html
//
// 2. The GNU Lesser General Public License (LGPL) version 2.1 or later,
// included in this distribution in the file licence-lgpl-2.1.txt
// or available at http://www.gnu.org/licenses/lgpl.txt
//
// This library is distributed on an "AS IS" basis,
// WITHOUT WARRANTY OF ANY KIND, either express or implied.
// See the individual licence texts for more details.

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

package myparser;
import net.htmlparser.jericho.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class Html2txt {
	public static void main(String[] args){
		try{
		for(int i = 0;i < Integer.parseInt(args[0]);i++){
				String filename = "doc"+i;
				
				//String foldername = "Temp"+File.separator+"doc" + args[i];
				String foldername = "misc"+ File.separator+"doc_parsing"+File.separator+"doc_" + i;		
				String sourceUrlString = foldername + File.separator + filename + ".html";
		/*String sourceUrlString="data/test.html";
		if (args.length==0)
		  System.err.println("Using default argument of \""+sourceUrlString+'"');
		else
			sourceUrlString=args[0];*/
		if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
		StreamedSource streamedSource=new StreamedSource(new URL(sourceUrlString));
		Writer writer=null;
		String filelocation = foldername + File.separator + filename + ".txt";
                System.out.println("filename: "+filelocation);
		Writer filewrite = new FileWriter(filelocation);
		try {
                        
			//writer=new FileWriter("StreamedSourceCopyOutput.html"); // my comment
			//System.out.println("Processing segments:");
			int lastSegmentEnd=0;
			int image_num = 1,table_num = 1;//my variables
			boolean flag1 = false,flag2_table = false;//my variables
			int table_flag = 0;//my variables
                        boolean flag_cref = false;
                        boolean flag_div = false;//for div tag
                        String div_data = "";
			String table_data = "";//my variables
			for (Segment segment : streamedSource) {
				//System.out.println(segment.getDebugInfo()); comment made by me
	 		
                            if (segment.getEnd()<=lastSegmentEnd) continue; // if this tag is inside the previous tag (e.g. a server tag) then ignore it as it was already output along with the previous tag.
				lastSegmentEnd=segment.getEnd();
				if (segment instanceof Tag) {
                                    
					Tag tag=(Tag)segment;
					if(tag.toString().contains("<body"))
						flag1 = true;
                                        if(tag.toString().contains("<div")){
                                            div_data = tag.toString();
                                            if(!div_data.contains("style")) continue;
                                            String split1 = "";
                                            String split2 = "";

                                            split1 = div_data.substring(0,div_data.indexOf("class"));
                                            split2 = div_data.substring(div_data.indexOf("style"),div_data.length());
                                            div_data = split1 + split2;
                                            flag_div = true;
                                        }
                                        if(tag.toString().contains("</div>")){
                                            flag_div = false;
                                        }

					if(tag.toString().contains("<img") && flag_div){
						
                                                writer = new FileWriter(foldername + File.separator + filename + "_img" + image_num);
						writer.write(div_data + "\n");
                                                writer.write(tag.toString());
                                                writer.write("</div>");
						writer.close();
						filewrite.write(". FIGURE_TAG " + filename + "_" + "img" + image_num  + " CLOSE_FIGURE_TAG." + "\n");
						image_num++;
                                                
						//System.out.println("\n\n<img>" + "<doc1>" + "<img" + image_num +">" + "</img>\n");
					}
					if(tag.toString().contains("<table")){
						table_flag++;
						flag2_table = true;						
						//System.out.println(tag.toString());
					}
					
					if(flag2_table){
						table_data += tag.toString();
					}

					if(tag.toString().contains("</table")){
						table_flag--;
						if(table_flag == 0){
							flag2_table = false;
							writer = new FileWriter(foldername + File.separator + filename + "_table" + table_num);
							writer.write(table_data);
							writer.close();						
							filewrite.write(". TABLE_TAG " + filename + "_table" + table_num + " CLOSE_TABLE_TAG."+"\n");
							//System.out.println("\n\n<table>" + "<doc1>" + "<table" + table_num +">" + "</table>\n");
							//image_num++;
							table_num++;
							table_data = "";
						}						
						//System.out.println(tag.toString());
					}
					// HANDLE TAG
					// Uncomment the following line to ensure each tag is valid XML:
					// writer.write(tag.tidy()); continue;
				} else if (segment instanceof CharacterReference) {
					CharacterReference characterReference=(CharacterReference)segment;
					//flag_cref = true;
                                        // HANDLE CHARACTER REFERENCE
					// Uncomment the following line to decode all character references instead of copying them verbatim:
					// characterReference.appendCharTo(writer); continue;
				
				} else {
					//if(segment.toString() != "\0")
					String s = segment.toString();
					if(s.length() > 1 && flag1 && !flag2_table){
                                            System.out.println("working " + s + " " + s.length());
                                            //if(flag_cref){
                                              //  filewrite.write(s);
                                                //flag_cref = false;
                                            //}else{
                                                

                                                filewrite.write(s + "\n");
                                            
						
					}

					if(flag2_table){
						table_data += s;
					}
                                        
					// HANDLE PLAIN TEXT
				}
				// unless specific handling has prevented getting to here, simply output the segment as is:				
				//writer.write(segment.toString()); my comment
				//System.out.println(segment.toString());
			}
			//System.out.println();
			filewrite.close();
			writer.close();
			//System.err.println("\nA copy of the source document has been output to StreamedSourceCopyOuput.html");
		} catch (Throwable t) {
                        System.out.println("amit there is an error1 " + t);
			if (writer!=null) try {writer.close();} catch (IOException ex) {System.out.println("amit there is an error");}
		}
	}
	}catch(Exception e){System.out.println("amit there is an error");}
  }
}
