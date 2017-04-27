/* vim: set tabstop=2: */
/* vim: set softtabstop=2: */
/* vim: set shiftwidth=2: */

import dk_gui.*;
import javax.swing.*;

public class Start{
	public static void main(String[] args) {
		String s;
		try {s = new String(args[0]);} catch(Exception e) {}
		javax.swing.SwingUtilities.invokeLater(new Runnable(){ public void run(){
			new Mainframe();
		}});
	}
}
