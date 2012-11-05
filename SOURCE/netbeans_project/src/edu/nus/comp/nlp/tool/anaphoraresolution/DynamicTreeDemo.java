/*
 JavaRAP: a freely-available JAVA anaphora resolution implementation
 of the classic Lappin and Leass (1994) paper:

 An Algorithm for Pronominal Anaphora Resolution.
 Computational Linguistics, 20(4), pp. 535-561.

 Copyright (C) 2005  Long Qiu

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
/**
 * Responsible for showing parse trees visually and is not part of the
 * anaphora resolution process.
 *
 * The code of class DynamicTreeDemo is removed from
 * this source distribution of JavaRAP since it is
 * borrowed from the copyright-protected "The Java Tutorial"
 * by Sun Microsystems, Inc.
 */

/*
 * This code is based on an example provided by Richard Stanford,
 * a tutorial reader.
 */


import javax.swing.*;
import javax.swing.tree.*;

public class DynamicTreeDemo extends JPanel {
    private int newNodeSuffix = 1;
    JFrame frame = new JFrame("ParseTree");

    public DynamicTreeDemo(DefaultMutableTreeNode node) {

    }


    public void show(){

    }

}
