/* vim: set tabstop=2: */
/* vim: set softtabstop=2: */
/* vim: set shiftwidth=2: */

package dk_gui;

import dk_util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class Mainframe{
	public Mainframe(){
		JFrame jf = new JFrame(Bund.p("progname"));
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		// SET SIZE ROUTINE START
		Rectangle bounds = jf.getGraphicsConfiguration().getBounds();
		Insets ins = Toolkit.getDefaultToolkit()
			.getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment()	
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
		desktop.setBackground(Color.GRAY);
		JInternalFrame jint = new JInternalFrame();
		//JInternalFrame jint = new JInternalFrame("Status",true,true,true,true);
		desktop.add(jint);
		Dimension mfsize = jf.getSize();
		Insets mwi = jf.getInsets();
		int mww = mfsize.width - mwi.left - mwi.right;
		int mwh = mfsize.height - mwi.top - mwi.bottom;
		//jint.setSize(mww/3,mwh);
		jint.setBounds(mww/3*2,0,mww/3,mwh);
		((BasicInternalFrameUI) jint.getUI()).setNorthPane(null);
		jint.setBorder(null);
		jint.setBackground(Color.BLACK);
		JLabel statusWindow = Log.getStatusWindow();
		statusWindow.setForeground(Color.YELLOW);
		jint.setLayout(new BorderLayout());
		jint.add(statusWindow,BorderLayout.NORTH);
		jint.setVisible(true);
	}
}
