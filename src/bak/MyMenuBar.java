package personnel;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.print.*;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.event.*;
import javax.swing.table.*;


@SuppressWarnings("serial")
class MyMenuBar extends JMenuBar{
 private JFrame frameToPrint;
 private int ICNSZ = 20;
 private Dimension pBS = new Dimension(50,25);
 private Dimension tBS = new Dimension(50,25);
 private DefaultListModel l1 = new DefaultListModel();
 private DefaultListModel l2 = new DefaultListModel();

 public MyMenuBar(JFrame ttab){
  frameToPrint = ttab;
  add(Box.createHorizontalGlue());

  URL pimageURL0 = VM.class.getResource("/img/company.png");
  ImageIcon pico0 = new ImageIcon();
  if (pimageURL0 != null) {
    pico0 = new ImageIcon(Toolkit.getDefaultToolkit().createImage(pimageURL0)
     .getScaledInstance(ICNSZ,ICNSZ,Image.SCALE_SMOOTH)); }
  JButton rep0 = new JButton(pico0);
  add(rep0);
  rep0.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent ev){
    new ReportTable(0);
  }});
  rep0.setMinimumSize(tBS);
  rep0.setPreferredSize(tBS);
  rep0.setMaximumSize(tBS);
  rep0.setToolTipText(Bund.p("PR_rep_org"));

  URL pimageURL1 = VM.class.getResource("/img/contract.png");
  ImageIcon pico1 = new ImageIcon();
  if (pimageURL1 != null) {
    pico1 = new ImageIcon(Toolkit.getDefaultToolkit().createImage(pimageURL1)
     .getScaledInstance(ICNSZ,ICNSZ,Image.SCALE_SMOOTH)); }
  JButton rep1 = new JButton(pico1);
  add(rep1);
  rep1.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent ev){
    new ReportTable(1);
  }});
  rep1.setMinimumSize(tBS);
  rep1.setPreferredSize(tBS);
  rep1.setMaximumSize(tBS);
  rep1.setToolTipText(Bund.p("PR_rep_con"));

  URL pimageURL2 = VM.class.getResource("/img/money.png");
  ImageIcon pico2 = new ImageIcon();
  if (pimageURL2 != null) {
    pico2 = new ImageIcon(Toolkit.getDefaultToolkit().createImage(pimageURL2)
     .getScaledInstance(ICNSZ,ICNSZ,Image.SCALE_SMOOTH)); }
  JButton rep2 = new JButton(pico2);
  add(rep2);
  rep2.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent ev){
    new ReportTable(2);
  }});
  rep2.setMinimumSize(tBS);
  rep2.setPreferredSize(tBS);
  rep2.setMaximumSize(tBS);
  rep2.setToolTipText(Bund.p("PR_rep_mon"));

  URL pimageURL3 = VM.class.getResource("/img/qualif.png");
  ImageIcon pico3 = new ImageIcon();
  if (pimageURL3 != null) {
    pico3 = new ImageIcon(Toolkit.getDefaultToolkit().createImage(pimageURL3)
     .getScaledInstance(ICNSZ,ICNSZ,Image.SCALE_SMOOTH)); }
  JButton rep3 = new JButton(pico3);
  add(rep3);
  rep3.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent ev){
    new ReportTable(3);
  }});
  rep3.setMinimumSize(tBS);
  rep3.setPreferredSize(tBS);
  rep3.setMaximumSize(tBS);
  rep3.setToolTipText(Bund.p("PR_rep_qlf"));

  URL pimageURL4 = VM.class.getResource("/img/address.png");
  ImageIcon pico4 = new ImageIcon();
  if (pimageURL4 != null) {
    pico4 = new ImageIcon(Toolkit.getDefaultToolkit().createImage(pimageURL4)
     .getScaledInstance(ICNSZ,ICNSZ,Image.SCALE_SMOOTH)); }
  JButton rep4 = new JButton(pico4);
  add(rep4);
  rep4.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent ev){
    new ReportTable(4);
  }});
  rep4.setMinimumSize(tBS);
  rep4.setPreferredSize(tBS);
  rep4.setMaximumSize(tBS);
  rep4.setToolTipText(Bund.p("PR_rep_adr"));

//JButton tb = new JButton("<html><b><u>"+Bund.p("test")+"</u></b></html>"); add(tb); tb.addMouseListener(new MouseAdapter(){   public void mousePressed(MouseEvent ev){  }});  tb.setMinimumSize(tBS);  tb.setPreferredSize(tBS);  tb.setMaximumSize(tBS);

  URL pimageURL = VM.class.getResource("/img/printer.png");
  ImageIcon pico = new ImageIcon();
  if (pimageURL != null) {
    pico = new ImageIcon(Toolkit.getDefaultToolkit().createImage(pimageURL)
     .getScaledInstance(ICNSZ,ICNSZ,Image.SCALE_SMOOTH)); }
  JButton   pribut = new JButton(pico); 
  pribut.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent e) {
    try { 
     BizCardPrinter.printCards(Porter.getList());
    } catch(Exception ex) { 
    SAY("Error while printing:" + ex); }
    SAY("Printing finished!");
  }});
  add(pribut);
  pribut.setMinimumSize(pBS);
  pribut.setPreferredSize(pBS);
  pribut.setMaximumSize(pBS);
  pribut.setToolTipText(Bund.p("PR_bc"));

  makeTrayIcon();
 }// EOM MyMenuBar()

 private static void makeTrayIcon(){
  if (SystemTray.isSupported()){
   URL imageURL = VM.class.getResource("/img/logo.gif");
   if (imageURL != null) {
    final PopupMenu popup = new PopupMenu();
    final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(imageURL));
    final SystemTray tray = SystemTray.getSystemTray();
    MenuItem aboutItem = new MenuItem("About");
    CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
    Menu displayMenu = new Menu("Display");
    MenuItem infoItem = new MenuItem("Info");
    MenuItem exitItem = new MenuItem("Exit");
    aboutItem.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
      trayIcon.displayMessage("Action Event","An Action Event Has Been Performed!",
      TrayIcon.MessageType.INFO);
    }});
    exitItem.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
      ConWrap.stop();
      System.exit(0);
    }});
    popup.add(aboutItem);
    popup.addSeparator();
    popup.add(cb1);
    popup.addSeparator();
    popup.add(displayMenu);
    displayMenu.add(infoItem);
    popup.add(exitItem);
    trayIcon.setPopupMenu(popup);
    trayIcon.setImageAutoSize(true);
    try{ tray.add(trayIcon); } catch (AWTException e) { }}}
 } //EOM makeTrayIcon()

 private void SAY(String s){
  System.out.println(new Object(){}.getClass().getEnclosingClass()+":\t"+s);
 }

}