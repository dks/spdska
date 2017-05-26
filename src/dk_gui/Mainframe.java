/* vim: set tabstop=2: */
/* vim: set softtabstop=2: */
/* vim: set shiftwidth=2: */

package dk_gui;

import dk_ge.*;
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

		// 1. ADD STATUS WINDOW - START
		JInternalFrame jint = new JInternalFrame();
		//JInternalFrame jint = new JInternalFrame("Status",true,true,true,true);
		desktop.add(jint);
		Dimension mfsize = jf.getSize();
		Insets mwi = jf.getInsets();
		int mww = mfsize.width - mwi.left - mwi.right;
		int mwh = mfsize.height - mwi.top - mwi.bottom;
		//jint.setSize(mww/3,mwh);
		jint.setBounds(0,0,mww/3,mwh);
		((BasicInternalFrameUI) jint.getUI()).setNorthPane(null);
		jint.setBorder(null);
		jint.setBackground(Color.BLACK);
		JLabel statusWindow = Log.getStatusWindow();
		statusWindow.setForeground(Color.YELLOW);
		jint.setLayout(new BorderLayout());
		jint.add(statusWindow,BorderLayout.NORTH);
		jint.setVisible(true);
		// 1. ADD STATUS WINDOW - END

		// 2. ADD 3D VIEWPORTS - START
		ViewPort3d vp3d = new ViewPort3d(ViewPort3d.RenderType.MESHED);
		desktop.add(vp3d);
		vp3d.setBounds(mww/3,0,mww/3,mwh/2);
		vp3d.setVisible(true);

		ViewPort3d vp3d2 = new ViewPort3d(ViewPort3d.RenderType.HIDDEN);
		desktop.add(vp3d2);
		vp3d2.setBounds(mww/3*2,0,mww/3,mwh/2);
		vp3d2.setVisible(true);

		ViewPort3d vp3d3 = new ViewPort3d(ViewPort3d.RenderType.SOLID);
		desktop.add(vp3d3);
		vp3d3.setBounds(mww/3,mwh/2,mww/3,mwh/2);
		vp3d3.setVisible(true);
		// 3. ADD 3D VIEWPORTS - END
		

	}
}
