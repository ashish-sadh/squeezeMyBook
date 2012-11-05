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

import java.util.*;
import java.io.*;
import findcaption.imgCluster;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import java.text.DecimalFormat;
import java.lang.Double;

class imgfscore{
            public int docno;
            public int imgno;
            public double score;
        }

public class sortImage{
    public static void func(mymaps mymap1, boolean table){
        
        imgfscore[] arrifs;
        arrifs = new imgfscore[200];
        imgfscore temp = new imgfscore();
        Set set = mymap1.mapdocimg_countscore.entrySet();
        Iterator it = set.iterator();
        float fir,sec;
        int i = 0,j = 0,N = 0;

        
	while(it.hasNext()){
                arrifs[i] = new imgfscore();
		Map.Entry me = (Map.Entry)it.next();
                arrifs[i].score = ((countscore)me.getValue()).indref + ((countscore)me.getValue()).dref; //final score

                arrifs[i].docno = ((docimg)me.getKey()).docno;
                arrifs[i].imgno = ((docimg)me.getKey()).imgno;
                i++;
                //System.out.println("work" + i);
        }
        //System.out.println("workinghere");
        N = i;
        for(i = 0;i < N;i++){
            for(j = 0;j < N - 1; j++ ){
                if(arrifs[j].score > arrifs[j+1].score ){
                    temp = arrifs[j];
                    arrifs[j] = arrifs[j+1];
                    arrifs[j+1] = temp;
                }
            }
        }
        //removing redundancy of images
        for(int p = 0;p < N;p++){
            for(int q = p;q < N;q++){
                String img1[] = {arrifs[p].docno+ "." + arrifs[p].imgno};
                String img2[] = {arrifs[q].docno+ "." + arrifs[q].imgno};
                String bimg[] = {img1[0],img2[0]};
                int ncluster = imgCluster.func(bimg);//-(imgCluster.func(img1)+imgCluster.func(img2));
                int a = imgCluster.func(img1);
                int b = imgCluster.func(img2);
                //System.out.println("mynameisamit " + ncluster + " " + a + " "+b);
                if(ncluster > 100){
                    for(int r = q;r < N-1;r++){
                        arrifs[r] = arrifs[r+1];
                    }
                    N--;
                }
            }
        }

        try{
            String filename;
            if(table){
                filename = "misc/final_summary/table_ranking.txt";
            }else{
                filename = "misc/final_summary/figure_ranking.txt";
            }
            FileWriter filewrite = new FileWriter(filename);

            if(table){
                 filewrite.write(  "Document\t" + "Table"+ "\t" + "Score" + "\n");
            }else{
                 filewrite.write(  "Document\t" + "Figure"+ "\t" + "Score" + "\n");
            }
            for(i = N - 1; i >= 0; i--){
                filewrite.write( (arrifs[i].docno+1) + "\t" + arrifs[i].imgno + "\t" + (new DecimalFormat("#.##")).format(arrifs[i].score) + "\n");
            }
            filewrite.close();
        }
        catch(IOException e){
            System.out.println(e);
        }

    }
}