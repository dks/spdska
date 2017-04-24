/* vim: set tabstop=2: */
/* vim: set softtabstop=2: */
/* vim: set shiftwidth=2: */

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class Mainframe{
	public static void main(String[] args) {
		String s;
		try {s = new String(args[0]);} catch(Exception e) {}
		javax.swing.SwingUtilities.invokeLater(new Runnable(){ public void run(){
			JFrame jf = new JFrame("SPDSKA");
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jf.setVisible(true);
			// SET SIZE ROUTINE START
			Rectangle bounds = jf.getGraphicsConfiguration().getBounds();
			Insets ins = Toolkit.getDefaultToolkit().getScreenInsets(
				GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDefaultConfiguration());
			bounds.x+=ins.left;
			bounds.y+=ins.top;
			bounds.width-=(ins.left+ins.right);
			bounds.height-=(ins.top+ins.bottom);
			jf.setBounds(bounds.x,bounds.y,bounds.width,bounds.height);
			// SET SIZE ROUTINE END
			JDesktopPane desktop = new JDesktopPane();
			jf.setContentPane(desktop);
			desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
			JInternalFrame jint = new JInternalFrame();
			//JInternalFrame jint = new JInternalFrame("Status",true,true,true,true);
			desktop.add(jint);
			jint.add(new JLabel("TEST"));
			Dimension mfsize = jf.getSize();
			Insets mwi = jf.getInsets();
			int mww = mfsize.width - mwi.left - mwi.right;
			int mwh = mfsize.height - mwi.top - mwi.bottom;
			jint.setSize(mww/3,mwh);
			((BasicInternalFrameUI) jint.getUI()).setNorthPane(null);
			jint.setBorder(null);
			jint.setVisible(true);
		}});
	}
}
