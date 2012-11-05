package edu.nus.comp.nlp.tool.anaphoraresolution;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;

/**
 * <p>Title: Anaphora Resolution</p>
 * <p>Description: It occurs to me that this will require that the client has JDK 1.4+ installed,
 * which is not what the demo meant to be. But it's a fast way to make the demo. :P
 * ... only for the sentence splitter.
 * As to the resolver, client-end has no access to parser.
 * Consequently, the plan to build a demo as an applet has to call a stop. :(</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Qiu Long
 * @version 1.0
 */

public class Applet1 extends Applet {
  private boolean isStandalone = false;
  JEditorPane jEditorPane_input = new JEditorPane();
  JTextArea jTextArea_output = new JTextArea();
  JButton jButton_SplitSentence = new JButton();
  JButton jButton2 = new JButton();
  JLabel jLabel1 = new JLabel();
  JButton jButton_Clear = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public Applet1() {
  }
  //Initialize the applet
  public void init() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception {
    this.setLayout(null);
    jTextArea_output.setEditable(false);
    jTextArea_output.setText("");
    jTextArea_output.setLineWrap(true);
    jButton_SplitSentence.setBounds(new Rectangle(66, 143, 85, 23));
    jButton_SplitSentence.setText("Split");
    jButton_SplitSentence.addActionListener(new Applet1_jButton_SplitSentence_actionAdapter(this));
    jButton2.setText("Resolve");
    jButton2.addActionListener(new Applet1_jButton2_actionAdapter(this));
    jButton2.setBounds(new Rectangle(173, 143, 92, 23));
    jButton2.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel1.setText("Paste your text here:");
    jLabel1.setBounds(new Rectangle(15, 15, 142, 22));
    jEditorPane_input.setText("JavaRAP is a publicly avaliable implementation of the classic Lappin and Leass' pronomial anaohora resolution algorithm. It's written in Java.");
    jEditorPane_input.setBounds(new Rectangle(15, 44, 358, 81));
    jButton_Clear.addActionListener(new Applet1_jButton_Clear_actionAdapter(this));
    jButton_Clear.setText("Clear");
    jButton_Clear.addActionListener(new Applet1_jButton_Clear_actionAdapter(this));
    jButton_Clear.setBounds(new Rectangle(287, 143, 85, 23));
    jScrollPane1.setBounds(new Rectangle(16, 189, 357, 102));
    this.add(jLabel1, null);
    this.add(jButton_Clear, null);
    this.add(jButton2, null);
    this.add(jButton_SplitSentence, null);
    this.add(jEditorPane_input, null);
    this.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(jTextArea_output, null);
  }
  //Get Applet information
  public String getAppletInfo() {
    return "Applet Information";
  }
  //Get parameter info
  public String[][] getParameterInfo() {
    return null;
  }

  void jButton_SplitSentence_actionPerformed(ActionEvent e) {
    String input = this.jEditorPane_input.getText();
    String output = new String();

    if(input.trim().length() == 0){
      output = "Forget the input?";
    }else{
      PlainText plainText1 = new PlainText(new StringBuffer(input));
      plainText1.setSingleLine(true);
      plainText1.removeTag();
      output = plainText1.addQuote(false, "\n");
    }
    this.jTextArea_output.setText(output);
  }

  void jButton2_actionPerformed(ActionEvent e) {

  }

  void jButton_Clear_actionPerformed(ActionEvent e) {
    this.jEditorPane_input.setText("");
    this.jTextArea_output.setText("");
  }

}

class Applet1_jButton_SplitSentence_actionAdapter implements java.awt.event.ActionListener {
  Applet1 adaptee;

  Applet1_jButton_SplitSentence_actionAdapter(Applet1 adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton_SplitSentence_actionPerformed(e);
  }

}

class Applet1_jButton2_actionAdapter implements java.awt.event.ActionListener {
  Applet1 adaptee;

  Applet1_jButton2_actionAdapter(Applet1 adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton2_actionPerformed(e);
  }
}

class Applet1_jButton_Clear_actionAdapter implements java.awt.event.ActionListener {
  Applet1 adaptee;

  Applet1_jButton_Clear_actionAdapter(Applet1 adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton_Clear_actionPerformed(e);
  }
}
