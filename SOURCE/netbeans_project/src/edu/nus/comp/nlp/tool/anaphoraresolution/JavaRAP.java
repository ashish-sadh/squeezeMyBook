/*
 JavaRAP: a freely-available JAVA anaphora resolution implementation
 of the classic Lappin and Leass (1994) paper:

 An Algorithm for Pronominal Anaphora Resolution.
 Computational Linguistics, 20(4), pp. 535-561.

 Copyright (C) 2005,2006  Long Qiu

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


package edu.nus.comp.nlp.tool.anaphoraresolution;

import java.util.*;
import java.io.*;

/**
 * <p>Title: Anaphora Resolution</p>
 * <p>Description:
 * JavaRAP tries to be a close implementation of the resolution
 *  of anaphora procedure given by Lappin and Leass. It resolves
 * third person pronouns. The results can be shown in three ways:
 * <li>an anaphor - antecedent pair list</li>
 * <li>in-place annotation</li>
 * <li>in-place substitution</li>
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Qiu Long qiul@comp.nus.edu.sg
 * @version 1.0
 * @history
 * Sep 27, 2004 change package name from edu.nus.comp.NLP.tool.anaphoraresolution to edu.nus.comp.nlp.tool.anaphoraresolution
 */

public class JavaRAP {
  public JavaRAP() {
  }

  public static void main(String[] args) {

    if (args.length == 0) {
      showHelpMessage();
      System.exit( -1);
    }

    //init
    System.setProperty("AAPair", "true");
    /** @todo in-place annotation */
    System.setProperty("Annotation", "false"); //in place annotation
    Env env = new Env();
    //end of init

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-s")) {
        System.setProperty("Substitution", "true");
      }

      if (args[i].equals("-rs")) {
        System.setProperty("display substitution results", "true");
      }

      if (args[i].equals("-rp")) {
        System.setProperty("display resolving results", "true");
      }

      if (args[i].equals("-p")) {
        System.setProperty("AAPair", "true");
      }

      if (args[i].equals("-log")) {
        System.setProperty("keep log", "true");
      }

      if (args[i].equals("-a")) {
        System.setProperty("Annotation", "true");
      }
      if (args[i].equals("-q")) {
        System.setProperty("display log", "false");
      }
      if (args[i].endsWith("-help")) {
        showHelpMessage();
        System.exit(1);
      }
    }

    String inputFileName = args[args.length - 1];


    Vector vet = MainProcess.stageTwoTestV1(new File(inputFileName));

  }

  private static void showHelpMessage() {
    System.out.println(
        "Usage: java edu.nus.comp.nlp.tool.anaphoraresolution.JavaRAP [options] file...");
    System.out.println(
        "Resolve all the third person pronouns (nominative, objective, possessive,");
    System.out.println(
        "reflective) in the text, which could be in the form of pure text or text");
    System.out.println("with XML-style tags. ");
    System.out.println("\nOptions:");
    System.out.println("\t -a\t in-place annotation (yet to come)"); //not sure what format is preferred
    System.out.println("\t -help\t display this message");
    System.out.println("\t -log\t keep a log file");
    System.out.println("\t -p\t generate anaphor-antecedent pairs (defalut)");
    System.out.println("\t -q\t dampen the log display");
    System.out.println("\t -rp\t enable result display (anaphor-antecedent pairs)");
    System.out.println("\t -rs\t enable result display (text with substitution)");
    System.out.println("\t -s\t in-place substitution (carried out by default)");
    System.out.println(
        "Contact\n\t Qiu Long <qiul@comp.nus.edu.sg> for bug-reporting, please.");
  }

}
