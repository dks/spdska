/* vim: set tabstop=2: */
/* vim: set softtabstop=2: */
/* vim: set shiftwidth=2: */

package dk_ge;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.event.*;

public class ViewPort3d extends JInternalFrame {
  public JSlider headingSlider = new JSlider(-180, 180, 0);
	public JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
	public JPanel renderPanel;
	List<Triangle> tris;
	List<Cone> conlist;
	Matrix3 transform;

	public ViewPort3d(){
		//JInternalFrame vp3d = new JInternalFrame("3D",true,true,true,true);
		super("3D",true,true,true,true);
		//super();
		tris = new ArrayList<>();
		conlist = new ArrayList<>();
		getRenderingObjects();
		
		setViewPort3d((Container)this.getContentPane());
	}

	void getRenderingObjects(){
		// Получаем данные трехмерных объектов
		tris.add(new Triangle(new Vertex(100, 100, 100),
			new Vertex(-100, -100, 100),
			new Vertex(100, -100, -100), Color.RED));
		conlist.add(new Cone(new Vertex(0, 0, 100),
			new Vertex(0, 0, -100), 100.0, 100.0, Color.RED));
	}
	
	void matrixPreCalc(){
		// Получаем значения ползунков
		double heading = Math.toRadians(headingSlider.getValue());
		double pitch = Math.toRadians(pitchSlider.getValue());
		// Задаем матрицы поворота
		Matrix3 headingTransform = new Matrix3(new double[] {
				Math.cos(heading), 0, -Math.sin(heading),
				0, 1, 0,
				Math.sin(heading), 0, Math.cos(heading)
				});
		Matrix3 pitchTransform = new Matrix3(new double[] {
				1, 0, 0,
				0, Math.cos(pitch), Math.sin(pitch),
				0, -Math.sin(pitch), Math.cos(pitch)
				});
		// Задаем общую матрицу поворота
		transform = headingTransform.multiply(pitchTransform);
	}

	// Main Rendering Method - Hidden Shaded Style
	void renderHidden(Graphics2D g2){
		for (Cone c : conlist) {
			Vertex v1 = transform.transform(c.v1);
			Vertex v2 = transform.transform(c.v2);
			Path2D path = new Path2D.Double();
			g2.setColor(c.color);
			path.moveTo(v1.x, v1.y);
			path.lineTo(v2.x, v2.y);
			path.closePath();
			//g2.draw(path);
			// Вместо нормали - задающий вектор (ось цилиндра)
			// Vertex n = new Vertex(v2.x-v1.x, v2.y-v1.y, v2.z-v1.z);
			// Вместо нормали - Радиус вектор точки 1
			Vertex n = new Vertex(v1.x, v1.y, v1.z);
			Vertex ls = new Vertex(0,0,1); // Вектор "от зрителя"
			Vertex rg = new Vertex(1,0,0); // Вектор отсчета вращения
			double cos = Math.abs( (ls.x*n.x+ls.y*n.y+ls.z*n.z)/
					(Math.sqrt(ls.x*ls.x+ls.y*ls.y+ls.z*ls.z)
					 *Math.sqrt(n.x*n.x+n.y*n.y+n.z*n.z)) );

			int w1 = (int)c.d1;
			int w2 = (int)c.d2;
			int h1 = (int)(c.d1*cos);
			int h2 = (int)(c.d2*cos);

			AffineTransform old = g2.getTransform();
			g2.translate(v1.x,v1.y);
			g2.rotate(-Math.atan2(v1.x,v1.y));
			g2.setColor(Color.CYAN);
			g2.draw(new Line2D.Double(0,0,0,0));
			//g2.draw(new Line2D.Double(-w1/2,0,w1/2,0));
			//g2.draw(new Line2D.Double(0,-h1/2,0,h1/2));
			g2.setColor(Color.RED);
			if (v1.z>0) g2.drawOval((int)(-w1/2),(int)(-h1/2),w1,h1);
			g2.setColor(Color.GREEN);
			g2.drawArc( (int)(-w1/2),(int)(-h1/2),w1,h1,180,180);
			g2.setTransform(old);

			g2.translate(v2.x,v2.y);
			g2.rotate(-Math.atan2(v2.x,v2.y));
			g2.setColor(Color.CYAN);
			g2.draw(new Line2D.Double(0,0,0,0));
			g2.setColor(Color.RED);
			if (v2.z>0) g2.drawOval((int)(-w2/2),(int)(-h2/2),w2,h2);
			g2.setColor(Color.GREEN);
			g2.drawArc( (int)(-w2/2),(int)(-h2/2),w2,h2,180,180);
			g2.setTransform(old);

			double alpha = -Math.atan2(v1.x,v1.y);
			int c1Lx = (int) (v1.x - ( (w1/2) * Math.cos(alpha)) );
			int c1Ly = (int) (v1.y - ( (w1/2) * Math.sin(alpha)) );
			int c2Lx = (int) (v2.x - ( (w2/2) * Math.cos(alpha)) );
			int c2Ly = (int) (v2.y - ( (w2/2) * Math.sin(alpha)) );
			int c1Rx = (int) (v1.x + ( (w1/2) * Math.cos(alpha)) );
			int c1Ry = (int) (v1.y + ( (w1/2) * Math.sin(alpha)) );
			int c2Rx = (int) (v2.x + ( (w2/2) * Math.cos(alpha)) );
			int c2Ry = (int) (v2.y + ( (w2/2) * Math.sin(alpha)) );
			g2.setColor(Color.GREEN);
			g2.draw(new Line2D.Double(c1Lx,c1Ly,c2Lx,c2Ly));
			g2.draw(new Line2D.Double(c1Rx,c1Ry,c2Rx,c2Ry));

			//g2.setColor(Color.YELLOW);
			//QuadCurve2D q = new QuadCurve2D.Float();
			//q.setCurve(c1Lx,c1Ly,v1.x,v1.y-h1,c1Rx,c1Ry);
			//g2.draw(q);

			//g2.drawString(Double.toString(v1.z),0,0);
		}// */
	}

	// Main Rendering Method - Simple Meshed Style
	void renderMeshed(Graphics2D g2){
		g2.setColor(Color.WHITE);
		for (Triangle t : tris) {
			Vertex v1 = transform.transform(t.v1);
			Vertex v2 = transform.transform(t.v2);
			Vertex v3 = transform.transform(t.v3);
			Path2D path = new Path2D.Double();
			path.moveTo(v1.x, v1.y);
			path.lineTo(v2.x, v2.y);
			path.lineTo(v3.x, v3.y);
			path.closePath();
			g2.draw(path);
		}
	}

	public void setViewPort3d(Container pane) {
		pane.setLayout(new BorderLayout());
		pane.add(headingSlider, BorderLayout.SOUTH);
		pane.add(pitchSlider, BorderLayout.EAST);

		renderPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, getWidth(), getHeight());
				BufferedImage img = new BufferedImage(getWidth() 
					, getHeight(), BufferedImage.TYPE_INT_ARGB);
				g2.translate(getWidth() / 2, getHeight() / 2);

				matrixPreCalc();
				//renderHidden(g2);
				renderMeshed(g2);

				g2.drawImage(img, 0, 0, null);
			}
		};
		
		pane.add(renderPanel, BorderLayout.CENTER);
		headingSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){renderPanel.repaint();}
		});
		pitchSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){renderPanel.repaint();}
		});
	}

	public static Color getShade(Color color, double shade) {
		double redLinear = Math.pow(color.getRed(), 2.4) * shade;
		double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
		double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;
		int red = (int) Math.pow(redLinear, 1/2.4);
		int green = (int) Math.pow(greenLinear, 1/2.4);
		int blue = (int) Math.pow(blueLinear, 1/2.4);
		return new Color(red, green, blue);
	}

}
