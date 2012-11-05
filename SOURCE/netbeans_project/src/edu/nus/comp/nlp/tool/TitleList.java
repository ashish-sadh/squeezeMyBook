package edu.nus.comp.nlp.tool;

/**
 * <p>Title: NLP Tools</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: NUS</p>
 * @author Qiu Long qiul@comp.nus.edu.sg
 * @version 1.0
 */

public class TitleList {
  //Following list is form mmunoz@uiuc.edu 's sentence spliter, augmented of course
  final static String list = new String("A|Adj|Adm|Adv|Asst|B|Bart|Bldg|Brig|Bros|C|Capt"
      +"|Cmdr|Col|Comdr|Con|Cpl|D|DR|Dr|E|Ens|F|G|Gen|Gov|H|Hon|Hosp|I|Insp|J"
      +"|K|L|Lt|M|MM|MR|MRS|MS|Maj|Messrs|Mlle|Mme|Mr|Mrs|Ms|Msgr|Mt|N|No|O|Op|Ord"
      +"|P|Pfc|Ph|Prof|Pvt|Q|R|Rep|Reps|Res|Rev|Rt|S|Sen|Sens|Sfc|Sgt|Sr|St"
      +"|Supt|Surg|T|U|V|W|X|Y|Z|v|vs").toLowerCase();

  public TitleList() {

  }

  static public boolean contains(String wd) {
    return list.indexOf(wd.toLowerCase()) > -1 ? true : false;
  }
}
