import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.Font;

/*
 * ﬂÎ˚Í Windows: 	"..\java.exe" -cp œ”“‹ ‘¿…À”\ ‘¿…À " Œ√ŒŒ“ –€“‹"
 * «‡ÔÛÒÍ Ò ÍÓÏÒÓÍË:	java -jar JTE.jar »Ãﬂ‘¿…À¿
 * —ÓÁ‰‡Ú¸ ‰Ê‡:	jar cfe ‘¿…À.jar —“¿–“Œ¬€… À¿—— ◊≈√Œ¿–’»¬»–Œ¬¿“‹
 */


public class JTE {
 public static void main(String[] args){ 
  try { final File fl = new File(args[0]); 
  SwingUtilities.invokeLater(new Runnable(){
   public void run(){ dk_MF mainf = new dk_MF();
   if (fl.isFile()) mainf.constructTable(fl);}});} 
  catch(Exception e) {
  SwingUtilities.invokeLater(new Runnable(){
   public void run(){ dk_MF mainf = new dk_MF();}});}}
}

class dk_MF{
 JTable tab;
 JFrame win;
 JFileChooser fc;
 TableRowSorter<Model> sorter;
 JTextField filterText;
 String valSeparator;
 Hashtable<Integer,JComboBox> DDMenus = new Hashtable<Integer,JComboBox>();
 Boolean changed = false;
 Boolean fLoaded = false;
 ResourceBundle m = ResourceBundle.getBundle("MB", Locale.getDefault());
 JLabel l1;

 dk_MF(){
  win = new JFrame("Java Table Editor");
  win.addWindowListener(new WindowAdapter(){ public void
  windowClosing(WindowEvent ev){ 
   if (changed){
   int n1 = JOptionPane.showConfirmDialog(win,
   m.getString("inputmes2"), m.getString("inputmes2.1"),
   JOptionPane.YES_NO_OPTION);
   if (n1==0) saveFile(); }
   System.exit(0); 
  }});

  win.setJMenuBar(makeMB());
  win.setSize(410,220);
  win.setVisible(true);
  fc = new JFileChooser();
  javax.swing.filechooser.FileFilter filter1 = 
   new FileNameExtensionFilter("Comma Separated Table", "csv");
  javax.swing.filechooser.FileFilter filter2 = 
   new FileNameExtensionFilter("Tab Separated Table", "txt");
  fc.addChoosableFileFilter(filter2);
  fc.addChoosableFileFilter(filter1);
  tab = new JTable();
  tab.getTableHeader().setBackground(Color.ORANGE);
  tab.setPreferredScrollableViewportSize(new Dimension(400,200));
  tab.setFillsViewportHeight(true);
  tab.setCellSelectionEnabled(true);
  tab.addMouseListener(new myMA());

  JScrollPane jsp = new JScrollPane(tab);
  JPanel p = new JPanel(new BorderLayout());
  p.add(jsp, BorderLayout.CENTER);
  JPanel pp = new JPanel(new BorderLayout());
  p.add(pp, BorderLayout.NORTH);
  l1 = new JLabel(m.getString("lbl"), SwingConstants.TRAILING);
  pp.add(l1, BorderLayout.WEST);
  filterText = new JTextField();
  filterText.getDocument().addDocumentListener(new DocumentListener(){
   public void changedUpdate(DocumentEvent e) { newFilter(); }
   public void insertUpdate(DocumentEvent e) { newFilter(); }
   public void removeUpdate(DocumentEvent e) { newFilter(); }
  });
  l1.setLabelFor(filterText);
  pp.add(filterText);

  win.getContentPane().add(p);
  win.pack();
 }

 class myMA extends MouseAdapter{
  public void mousePressed(MouseEvent e){
   if (SwingUtilities.isRightMouseButton(e)){
     Point p = e.getPoint();
     int row = tab.rowAtPoint(p);
     int mcol = tab.convertColumnIndexToModel(tab.columnAtPoint(p)); 
     tab.changeSelection(row,mcol,false,false);
     JPopupMenu coMe = createCoMe(row,mcol);
     if (coMe!=null && coMe.getComponentCount()>0) coMe.show(tab, p.x, p.y);
 }}
  private JPopupMenu createCoMe(final int rI,final int cI){
   JPopupMenu contextMenu = new JPopupMenu();
   JMenuItem copyMenu = new JMenuItem();
   copyMenu.setText(m.getString("copy"));
   copyMenu.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
     Object value = tab.getValueAt(rI,cI);
     setClipboardContents(value == null ? "" : value.toString());
   }});
   contextMenu.add(copyMenu);
  return contextMenu;
  }
  private void setClipboardContents(String s) {
   StringSelection selection = new StringSelection(s);
   Toolkit.getDefaultToolkit().getSystemClipboard().
   setContents(selection, selection);
 }}

 public JMenuBar makeMB() {
  JMenuBar mb = new JMenuBar();

  JMenu lev1men = new JMenu(m.getString("file"));
  JMenu lev1me2 = new JMenu(m.getString("lang"));
  JMenuItem lev2me4 = new JMenuItem(m.getString("rus"));
  JMenuItem lev2me5 = new JMenuItem(m.getString("eng"));
  lev2me4.addActionListener(new ActionListener(){
   public void actionPerformed(ActionEvent e) {
    m = ResourceBundle.getBundle("MB", new Locale("ru"));
    win.setJMenuBar(makeMB());
    l1.setText(m.getString("lbl"));
    win.validate();
    win.repaint();
  }});
  lev2me5.addActionListener(new ActionListener(){
   public void actionPerformed(ActionEvent e) {
    m = ResourceBundle.getBundle("MB", new Locale("en"));
    win.setJMenuBar(makeMB());
    l1.setText(m.getString("lbl"));
    win.validate();
    win.repaint();
  }});
  lev1me2.add(lev2me4);
  lev1me2.add(lev2me5);

  JMenuItem lev2me0 = new JMenuItem(m.getString("fnew"));
  JMenuItem lev2men = new JMenuItem(m.getString("fopen"));
  JMenuItem lev2me2 = new JMenuItem(m.getString("fsave"));
  JMenuItem lev2me3 = new JMenuItem(m.getString("fexit"));
  lev2me0.addActionListener(new ActionListener(){ 
   public void actionPerformed(ActionEvent e) { 
    Vector<Vector<Object>> bigvec =  new Vector<Vector<Object>>();
    Vector<Object> smallvec = new Vector<Object>();
    Vector<Object> subvec = new Vector<Object>();
    
    String cn1 = JOptionPane.showInputDialog(
     m.getString("inputmes1") + "1","1");
    if ((cn1 != null) && (cn1.length() > 0)) {
    String cn2 = JOptionPane.showInputDialog(
     m.getString("inputmes1") + "2","2");
    if ((cn2 != null) && (cn2.length() > 0)) {

    smallvec.add(cn1); smallvec.add(cn2);   
    subvec.add("..."); subvec.add("...");
    bigvec.add(new Vector<Object>(subvec));
    bigvec.add(new Vector<Object>(subvec));
    Model mymod = new Model(bigvec,smallvec);
    tab.setModel(mymod);
    sorter = new TableRowSorter<Model>(mymod);
    tab.setRowSorter(sorter);
    win.setTitle(m.getString("wnew"));
    changed = true;
    fLoaded = true;
  }}}});
  lev2men.addActionListener(new ActionListener(){ 
   public void actionPerformed(ActionEvent e) { 
   int rv =  fc.showOpenDialog(win);
   if (rv == JFileChooser.APPROVE_OPTION) {
    File file = fc.getSelectedFile();
    constructTable(file);
  }}});
  lev2me2.addActionListener(new ActionListener(){
   public void actionPerformed(ActionEvent e) {
    saveFile();
  }});
  lev2me3.setAccelerator(KeyStroke.getKeyStroke(
   KeyEvent.VK_1, ActionEvent.ALT_MASK));
  lev2me3.addActionListener(new ActionListener(){ 
   public void actionPerformed(ActionEvent e) { 
    if (changed){
    int n1 = JOptionPane.showConfirmDialog(win,
    m.getString("inputmes2"), m.getString("inputmes2.1"),
    JOptionPane.YES_NO_OPTION);
    if (n1==0) saveFile(); }
    System.exit(0); 
  }});
  lev1men.add(lev2me0);
  lev1men.add(lev2men);
  lev1men.add(lev2me2);
  lev1men.add(new JSeparator());
  lev1men.add(lev2me3);
  mb.add(lev1men);
  mb.add(lev1me2);
  mb.add(Box.createHorizontalGlue());

  BufferedImage image1 =  new BufferedImage(30, 16,
  BufferedImage.TYPE_INT_ARGB);
  Graphics2D g2 = image1.createGraphics();
  int[] xs = {18,11,18};
  int[] ys = {10,12,15};
  g2.setColor(Color.BLACK);
  g2.draw(new Rectangle2D.Double(0, 0, 10, 5));
  g2.draw(new Rectangle2D.Double(0, 5, 10, 5));
  g2.draw(new Rectangle2D.Double(19, 10, 10, 5));
  g2.fillPolygon(xs, ys, 3);
  JButton rowbut = new JButton(new ImageIcon(image1));
  rowbut.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent e) {
    if (fLoaded==true) ((Model)tab.getModel()).addRow(); }});
  rowbut.setToolTipText(m.getString("ttip1"));
  mb.add(rowbut);

  BufferedImage image2 =  new BufferedImage(30, 16,
  BufferedImage.TYPE_INT_ARGB);
  Graphics2D g2a = image2.createGraphics();
  int[] xs2 = {20,25,29};
  int[] ys2 = {7,11,7};
  g2a.setColor(Color.BLACK);
  g2a.draw(new Rectangle2D.Double(0, 12, 9, 6));
  g2a.draw(new Rectangle2D.Double(9, 12, 9, 6));
  g2a.draw(new Rectangle2D.Double(20, -1, 9, 6));
  g2a.fillPolygon(xs2, ys2, 3);
  JButton colbut = new JButton(new ImageIcon(image2));
  colbut.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent e) {
    if (fLoaded==true) ((Model)tab.getModel()).addCol(); }});
  colbut.setToolTipText(m.getString("ttip2"));
  mb.add(colbut);

  BufferedImage image4 =  new BufferedImage(30, 16,
  BufferedImage.TYPE_INT_ARGB);
  Graphics2D g2c = image4.createGraphics();
  g2c.setColor(Color.BLACK);
  g2c.draw(new Rectangle2D.Double(10, 0, 10, 5));
  g2c.draw(new Rectangle2D.Double(10, 5, 10, 5));
  g2c.draw(new Rectangle2D.Double(10, 10,10, 5));
  g2c.draw(new Line2D.Double(7,10,23,15));
  g2c.draw(new Line2D.Double(7,15,23,10));
  JButton drbut = new JButton(new ImageIcon(image4));
   drbut.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent e) {
    if (fLoaded==true) ((Model)tab.getModel()).delRow(); 
  }});
  drbut.setToolTipText(m.getString("ttip3"));
  mb.add(drbut);

  BufferedImage image5 =  new BufferedImage(30, 16,
  BufferedImage.TYPE_INT_ARGB);
  Graphics2D g2d = image5.createGraphics();
  g2d.setColor(Color.BLACK);
  g2d.draw(new Rectangle2D.Double(0, 7, 9, 9));
  g2d.draw(new Rectangle2D.Double(10, 7, 9, 9));
  g2d.draw(new Rectangle2D.Double(20, 7, 9, 9));
  g2d.draw(new Line2D.Double(20,5,30,16));
  g2d.draw(new Line2D.Double(20,16,29,5));
  JButton dcbut = new JButton(new ImageIcon(image5));
   dcbut.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent e) {
    if (fLoaded==true) ((Model)tab.getModel()).delCol(); 
  }});
  dcbut.setToolTipText(m.getString("ttip4"));
  mb.add(dcbut);

  BufferedImage image3 =  new BufferedImage(30, 16,
  BufferedImage.TYPE_INT_ARGB);
  Graphics2D g2b = image3.createGraphics();
  int[] xs3 = {9,12,21,17};
  int[] ys3 = {7,1,1,7};
  g2b.setColor(Color.BLACK);
  g2b.draw(new Rectangle2D.Double(6, 7, 15, 5));
  g2b.draw(new Rectangle2D.Double(7, 12, 13, 2));
  g2b.draw(new Line2D.Double(13, 3, 18, 3));
  g2b.draw(new Line2D.Double(12, 5, 17, 5));
  g2b.drawPolygon(xs3, ys3, 4);
  JButton pribut = new JButton(new ImageIcon(image3));
  pribut.addMouseListener(new MouseAdapter(){
   public void mousePressed(MouseEvent e) {
    if (fLoaded==true) { try { tab.print(); } catch(Exception ex) { 
    System.out.println("Error while printing:" + ex); }}}});
  pribut.setToolTipText(m.getString("ttip5"));
  mb.add(pribut);

  JButton hbut = new JButton(m.getString("help"));
  hbut.addActionListener(new ActionListener(){ 
   public void actionPerformed(ActionEvent e) { 
    JTextArea tx = new JTextArea(m.getString("helpAll"));
    tx.setEditable(false);
    tx.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
    JScrollPane sp = new JScrollPane(tx);
    JFrame nf = new JFrame(m.getString("help"));
    nf.getContentPane().add(sp);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    nf.setBounds((screenSize.width-670)/2, 100,670,screenSize.height-100*2);
    nf.setVisible(true);
  }});
  mb.add(hbut);
  return mb;
 }

 public void saveFile(){
  int rv =  fc.showSaveDialog(win);
  if (rv == JFileChooser.APPROVE_OPTION) {
  File file = fc.getSelectedFile();
  boolean exs = (file.exists());
  if (exs) {
   int n = JOptionPane.showConfirmDialog(win,
   m.getString("inputmes3"), m.getString("inputmes3.1"),
   JOptionPane.YES_NO_OPTION);
   if (n==0) {
    Exception exep = setTabData(tab,file);
    if (exep!=null) JOptionPane.showMessageDialog
    (win, m.getString("inputmes5") + exep); 
    else { JOptionPane.showMessageDialog(win,
    m.getString("inputmes4")); changed = false; }
  }} else {
    if ((!file.getAbsoluteFile().getName().matches(".*[.]csv$"))
    && (!file.getAbsoluteFile().getName().matches(".*[.]txt$"))) 
     file = new File(file.toString() + ".csv");
    Exception exep = setTabData(tab,file);
    if (exep!=null) JOptionPane.showMessageDialog
    (win, m.getString("inputmes5") + exep); 
    else { JOptionPane.showMessageDialog(win,
    m.getString("inputmes4")); changed = false; }
 }}}

 public void constructTable(File fil){
  Vector<Vector<Object>> bigvec = getTabData(fil);
  Vector<Object> smallvec = null;
  Vector<Object> prevvec = null;
  Boolean invalidTab = false;
  Boolean colNumMismatch = false;

  a: for(int vc = 1; vc<bigvec.size(); vc++){ 
   if (vc!=1) { 
    for(int subvc = 0; subvc<bigvec.elementAt(vc).size(); subvc++){
     if (bigvec.elementAt(vc).elementAt(subvc).getClass()!=
     prevvec.elementAt(subvc).getClass()){ invalidTab=true; break a; }
     if (bigvec.elementAt(vc).size()!=prevvec.size()) {
      colNumMismatch = true; break a; }
    }}
   prevvec = new Vector<Object>(bigvec.elementAt(vc));
  }
  
  if (bigvec.size()<3) { JOptionPane.showMessageDialog
   (win, m.getString("error1")); }
  else if (invalidTab) { JOptionPane.showMessageDialog
   (win, m.getString("error2")); }
  else if (colNumMismatch) { JOptionPane.showMessageDialog
   (win, m.getString("error3")); }
  else { 
   smallvec = new Vector<Object>(bigvec.elementAt(0));
   bigvec.remove(0); 
   Model mymod = new Model(bigvec,smallvec);
   tab.setModel(mymod);
   sorter = new TableRowSorter<Model>(mymod);
   tab.setRowSorter(sorter);
   win.setTitle(fil.getAbsoluteFile().getName());
   fLoaded = true;
   if (DDMenus.size()>0) {
    for (Enumeration e = DDMenus.keys(); e.hasMoreElements();) {
     Integer cntr = (Integer) e.nextElement();
     TableColumn fcolumn = tab.getColumnModel().getColumn(cntr); 
     fcolumn.setCellEditor(new DefaultCellEditor(DDMenus.get(cntr)));
 }}}}

 private void newFilter() {
  if (tab.getColumnCount()>0) {
  RowFilter<Model, Object> rf = null;
  try { rf = RowFilter.regexFilter(filterText.getText(), 
   tab.convertColumnIndexToModel(tab.getSelectedColumn())>=0 
   ? tab.convertColumnIndexToModel(tab.getSelectedColumn()) : 0);} 
  catch (java.util.regex.PatternSyntaxException e) { return; }
  sorter.setRowFilter(rf); }
 }

 public Vector<Vector<Object>> getTabData(File fn){
  Vector<Vector<Object>> bv = new Vector<Vector<Object>>();
  Vector<Object> sv = new Vector<Object>();
  String ln;
  String[] pcs;

  if (fn.getAbsoluteFile().getName().matches(".*[.]csv$")) 
  valSeparator = ","; else valSeparator = "\t";

  try{ 
  BufferedReader 
  br = new BufferedReader(new InputStreamReader(new 
   DataInputStream(new FileInputStream(fn))));
  while((ln = br.readLine())!=null){ 

  if (ln.matches(".*<DropDownMenu>:.*")) {
  int ix = 0;
  ln = ln.replaceAll("<DropDownMenu>:",""); 
  String[] ddm = ln.split(valSeparator);
   for(String dv : ddm) {
    if (dv.length()>0) {
     String[] subdv = dv.split(";");
     JComboBox comboBox = new JComboBox();
     for(String sdv : subdv) { comboBox.addItem(sdv); } 
     DDMenus.put(new Integer(ix),comboBox);
   } ix++;
  }} else {

   pcs = ln.split(valSeparator);
   for(int x=0; x < pcs.length; x++) { 
    if (pcs[x].matches("[0-9.]+")) { sv.add(new Double(pcs[x])); }
    else if (pcs[x].matches("true")) { sv.add(new Boolean(true)); }
    else if (pcs[x].matches("false")) { sv.add(new Boolean(false)); }
    else { sv.add(pcs[x]); 
   }}
   bv.add(new Vector<Object>(sv));
   sv.clear(); }}
  br.close();
  } catch (Exception e) { 
   if (e!=null) JOptionPane.showMessageDialog(win, m.getString("error4") + e); }
  return bv;
 }

 public Exception setTabData(JTable data,File fn){
  if (fn.getAbsoluteFile().getName().matches(".*[.]csv$")) 
  valSeparator = ","; else valSeparator = "\t";
  Exception ex = null;

  try{ 
   PrintWriter pw = new PrintWriter(new FileWriter(fn));
   for(int col=0; col<data.getColumnCount(); col++) { 
    pw.print(data.getColumnName(col));
    if (col!=data.getColumnCount()-1) pw.print(valSeparator);
   } pw.println();
   for(int col=0; col<data.getRowCount(); col++) {
    for(int row=0; row<data.getColumnCount(); row++){
     pw.print(data.getValueAt(col,row));
     if (row!=data.getColumnCount()-1) pw.print(valSeparator);
    } pw.println();
   } 

   Boolean gotmenu = false;
   for(int mycol = 0; mycol<data.getColumnCount(); mycol++) {
    JComboBox myb = new JComboBox();
    if (data.prepareEditor(data.getCellEditor(0,mycol), 0, mycol)
    .getClass()==myb.getClass()) { gotmenu = true; }
   }

   if (gotmenu) { 
    pw.print("<DropDownMenu>:");
    for(int mycol = 0; mycol<data.getColumnCount(); mycol++) {
     pw.print(mycol>0 ? "," : "");
     JComboBox myb = new JComboBox();
      if (data.prepareEditor(data.getCellEditor(0,mycol), 0, mycol)
      .getClass()==myb.getClass()) {
       myb = (JComboBox) data.prepareEditor(
        data.getCellEditor(0,mycol), 0, mycol);
       for(int zz=0; zz<myb.getItemCount(); zz++) { 
        pw.print(zz>0 ? ";" : ""); pw.print(myb.getItemAt(zz)); }
    }}}

   pw.close();
  } catch (Exception e) { ex = e; } return ex;
 }

 class Model extends AbstractTableModel {
  static final long serialVersionUID = 45;
  Vector<Object> columnNames;
  Vector<Vector<Object>> data;

  public int getRowCount() { return data.size(); }
  public int getColumnCount() { return columnNames.size(); }
  public Object getValueAt(int row, int col){ 
   return data.elementAt(row).elementAt(col); }

  public void setValueAt(Object value, int row, int col) {
   data.elementAt(row).setElementAt(value,col); 
   fireTableCellUpdated(row, col); 
   changed = true; }

  public String getColumnName(int col) { 
   return columnNames.get(col).toString(); }
  public Class getColumnClass(int c) { 
   return getValueAt(1, c).getClass(); }
  public boolean isCellEditable(int row, int col) { return true; }

  public void addRow(){
   Vector<Object> addedvec = new Vector<Object>();
   Class NUM = new Double(0.0).getClass();
   Class BUL = new Boolean(true).getClass();
   for(Object vals : data.elementAt(0)) { 
    if (NUM==vals.getClass()) addedvec.add(new Double(0.0));
    else if (BUL==vals.getClass()) addedvec.add(new Boolean(false));
    else addedvec.add("..."); }
   data.add(new Vector<Object>(addedvec));
   sorter = new TableRowSorter<Model>(this);
   tab.setRowSorter(sorter);
   tab.revalidate();
   tab.repaint(); 
   changed = true; }

  public void delRow(){
   data.remove(tab.convertRowIndexToModel(tab.getSelectedRow()));
   sorter = new TableRowSorter<Model>(this);
   tab.setRowSorter(sorter);
   tab.revalidate();
   tab.repaint(); 
   changed = true; }

  public void delCol(){
   tab.removeColumn(tab.getColumnModel().getColumn(
    tab.convertColumnIndexToModel(tab.getSelectedColumn())));
   /*for (Vector<Object> vals : data) { 
    vals.remove(tab.convertColumnIndexToModel(
    tab.getSelectedColumn())); }
   columnNames.remove(tab.convertColumnIndexToModel(
   tab.getSelectedColumn()));*/
   changed = true; 
  }

  public void addCol(){
   String s = JOptionPane.showInputDialog(m.getString("inputmes6"),m.getString("inputmes6.1"));
   if ((s != null) && (s.length() > 0)) {
    columnNames.add(s);
    for (Vector<Object> vals : data) { vals.add("..."); }
    tab.addColumn(new TableColumn(columnNames.size()-1));
    changed = true;
  }}

  Model(Vector<Vector<Object>> big, Vector<Object> small) {
   columnNames = new Vector<Object>(small);
   data = new Vector<Vector<Object>>(big);
  }
 }
}