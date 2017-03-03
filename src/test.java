import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class test{
	public static void main(String[] args){
		String s;
		try {s = new String(args[0]);} catch(Exception e) {}
		javax.swing.SwingUtilities.invokeLater(new Runnable(){ public void run(){

JFrame f = new JFrame("Demo");
f.getContentPane().setLayout(new BorderLayout());    
f.add(new JComponent() {
public void paintComponent(Graphics g) { 
Graphics2D g2d = (Graphics2D) g;

GeneralPath theShape = new GeneralPath();
theShape.moveTo(0, 0);
theShape.lineTo(1, 0);
theShape.lineTo(1, 1);
theShape.lineTo(0, 1);
theShape.closePath();

g2d.scale(100, 100);
g2d.setStroke(new BasicStroke(0.01f));
g2d.draw(theShape);
}
});

f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
f.setSize(300, 300);
f.setVisible(true);

		}});
	}
}

