package personnel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


class TabPages extends JTabbedPane{
 private static final long serialVersionUID = 1L;
 public TabPages(){ 
   /* 1 */
   ImageIcon icon = new ImageIcon("res/icon.gif");
   addTab(Bund.p("tab1"), icon, new ContactList(), 
   Bund.p("clist"));
   setMnemonicAt(0,KeyEvent.VK_1);
   /* 2 */
   addTab(Bund.p("tab2"), new JLabel("Here Will BE Something"));
   setMnemonicAt(1,KeyEvent.VK_2);
 }// EOM TabPages()
}// EOC TabPages