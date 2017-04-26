package dk_util;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

/** A simple utility class that lets you very simply print
 *  an arbitrary component. Just pass the component to the
 *  PrintUtilities.printComponent. The component you want to
 *  print doesn't need a print method and doesn't have to
 *  implement any interface or do anything special at all.
 *  <P>
 *  If you are going to be printing many times, it is marginally more 
 *  efficient to first do the following:
 *  <PRE>
 *    PrintUtilities printHelper = new PrintUtilities(theComponent);
 *  </PRE>
 *  then later do printHelper.print(). But this is a very tiny
 *  difference, so in most cases just do the simpler
 *  PrintUtilities.printComponent(componentToBePrinted).
 *
 *  7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 *  May be freely used or adapted.
 */

public class PrintUtilities implements Printable {
 private Component ctbp;
 double pageHeight;
 double pageWidth;

 private void SAY(String s){
  System.out.println(new Object(){}.getClass().getEnclosingClass()+": "+s);
 }

 public static void printComponent(Component c) {
  new PrintUtilities(c).print();
 }
  
 public PrintUtilities(Component componentToBePrinted) {
  this.ctbp = componentToBePrinted;
 }
  
 public void print() {
  PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet
    (new MediaPrintableArea(10,10,190,277,MediaPrintableArea.MM));
  // A4 size is 210 * 297 mm ( sqrt(2):1 = 1,4142:1 )
  PrinterJob printJob = PrinterJob.getPrinterJob();
  printJob.setPrintable(this);
  if (printJob.printDialog(aset))
   try { printJob.print(aset); }
   catch(PrinterException pe){ SAY("Print Error: " + pe); }
 }

 public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
  if (pageIndex > 0) {
   return(NO_SUCH_PAGE);
  } else {
   disableDoubleBuffering(ctbp);
   Graphics2D g2d = (Graphics2D)g;
   g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
   pageHeight = pageFormat.getImageableHeight(); //height in pts
   pageWidth = pageFormat.getImageableWidth();   //width in pts

   Rectangle olddim = ctbp.getBounds();
 
   ctbp.setSize((int)pageWidth,(int)pageHeight);
   ctbp.validate();
   ctbp.repaint();

   ctbp.paint(g2d);
   ctbp.setBounds(olddim);

   enableDoubleBuffering(ctbp);
   return(PAGE_EXISTS);
  }
 }

 public static void disableDoubleBuffering(Component c) {
  RepaintManager currentManager = RepaintManager.currentManager(c);
  currentManager.setDoubleBufferingEnabled(false);
 }

 public static void enableDoubleBuffering(Component c) {
  RepaintManager currentManager = RepaintManager.currentManager(c);
  currentManager.setDoubleBufferingEnabled(true);
 }
}
