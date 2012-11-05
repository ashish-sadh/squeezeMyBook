package edu.nus.comp.nlp.tool.anaphoraresolution;

/**
 * <p>Title: Anaphora Resolution</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Qiu Long
 * @version 1.0
 */

public class FilePorter {
  public FilePorter() {
  }
  public static void main(String[] args) {
    FilePorter filePorter1 = new FilePorter();
    Master master1 = new Master();
    while (true) {
        master1.moveFiles("/home/qiulong/swap/cuihang","rpnlpir@sunfire:" + "/specproj2/rpnlpir/workspace/qiul/forCuiHang","chanp");
        try {
          System.out.println("sleep for a while");
          Thread.sleep(10000); //sleeps for 30 sec
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
  }
}