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
	public enum RenderType{ MESHED, HIDDEN, SOLID	}
	public RenderType rt;

	public ViewPort3d(){
		super("3D",true,true,true,true);
		tris = new ArrayList<>();
		conlist = new ArrayList<>();
		getRenderingObjects();
		setViewPort3d((Container)this.getContentPane());
	}

	public ViewPort3d(RenderType rt){
		super("3D",true,true,true,true);
		this.rt = rt;
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

		tris.add(new Triangle(new Vertex(100, 100, 100),
					new Vertex(-100, -100, 100),
					new Vertex(-100, 100, -100),
					Color.GRAY));
		tris.add(new Triangle(new Vertex(100, 100, 100),
					new Vertex(-100, -100, 100),
					new Vertex(100, -100, -100),
					Color.RED));
		tris.add(new Triangle(new Vertex(-100, 100, -100),
					new Vertex(100, -100, -100),
					new Vertex(100, 100, 100),
					Color.GREEN));
		tris.add(new Triangle(new Vertex(-100, 100, -100),
					new Vertex(100, -100, -100),
					new Vertex(-100, -100, 100),
					Color.BLUE));
		for (int i = 0; i < 4; i++) { tris = inflate(tris); }
		tris.add(new Triangle(new Vertex(-300, -100, -100),
					new Vertex(-300, 100, -100),
					new Vertex(300, 0, 100),
					Color.PINK));

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

	void renderSolid(Graphics2D g2){
		BufferedImage img = new BufferedImage(getWidth() 
			, getHeight(), BufferedImage.TYPE_INT_ARGB);
		//DRAWING CORE FOR SOLID GRAPHICS
		double[] zBuffer = new double[img.getWidth() * img.getHeight()];
		for (int q = 0; q < zBuffer.length; q++) {
			zBuffer[q] = Double.NEGATIVE_INFINITY;
		}
		g2.translate(-getWidth() / 2,-getHeight() / 2);
		for (Triangle t : tris) { 
			// Поворот вершин
			Vertex v1 = transform.transform(t.v1);
			Vertex v2 = transform.transform(t.v2);
			Vertex v3 = transform.transform(t.v3);
			// Сдвиг вершин на середину экрана
			v1.x += getWidth() / 2;
			v1.y += getHeight() / 2;
			v2.x += getWidth() / 2;
			v2.y += getHeight() / 2;
			v3.x += getWidth() / 2;
			v3.y += getHeight() / 2;
			// Расчет вектора нормали
			Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
			Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
			Vertex norm = new Vertex(ab.y * ac.z - ab.z * ac.y,
					ab.z * ac.x - ab.x * ac.z,
					ab.x * ac.y - ab.y * ac.x);
			double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y 
					+ norm.z * norm.z);
			norm.x /= normalLength;
			norm.y /= normalLength;
			norm.z /= normalLength;
			// Задаем косинус между нормалью треугольника и направлением света
			// так как наш источник света идет из (0,0,1) изменяется только одна величина
			double angleCos = Math.abs(norm.z);
			// Определение прямоугольных границ расчитываемых треугольников
			int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x
							, Math.min(v2.x, v3.x))));
			int maxX = (int) Math.min(img.getWidth()-1, Math.floor(Math.max(v1.x
							, Math.max(v2.x, v3.x))));
			int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y
							, Math.min(v2.y, v3.y))));
			int maxY = (int) Math.min(img.getHeight()-1, Math.floor(Math.max(v1.y
							, Math.max(v2.y, v3.y))));
			double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) 
				+ (v2.y - v3.y) * (v3.x - v1.x);
			// Основной цикл рендеринга
			// Построчная развертка по пикселям в границах прямоугольников рассчитанных выше
			for (int y = minY; y <= maxY; y++) {
				for (int x = minX; x <= maxX; x++) {
					double b1 = ((y - v3.y) * (v2.x - v3.x) 
							+ (v2.y - v3.y) * (v3.x - x)) / triangleArea;
					double b2 = ((y - v1.y) * (v3.x - v1.x) 
							+ (v3.y - v1.y) * (v1.x - x)) / triangleArea;
					double b3 = ((y - v2.y) * (v1.x - v2.x) 
							+ (v1.y - v2.y) * (v2.x - x)) / triangleArea;
					if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
						// Глубина каждого пикселя определяется путем умножения 
						// барицентрических координат на координаты углов
						double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
						int zIndex = y * img.getWidth() + x;
						if (zBuffer[zIndex] < depth) {
							img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
							zBuffer[zIndex] = depth;
						}
					}
				}
			}
		}//End of main solid graphics loop */
		// Print buffer to screen
		g2.drawImage(img, 0, 0, null);
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
				//BufferedImage img = new BufferedImage(getWidth() 
				//	, getHeight(), BufferedImage.TYPE_INT_ARGB);
				g2.translate(getWidth() / 2, getHeight() / 2);

				matrixPreCalc();
				
				switch(rt){
					case HIDDEN: renderHidden(g2); break;
					case MESHED: renderMeshed(g2); break;
					case SOLID: renderSolid(g2); break;
				}

				//g2.drawImage(img, 0, 0, null);
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

	public static List<Triangle> inflate(List<Triangle> tris) {
		List<Triangle> result = new ArrayList<>();
		for (Triangle t : tris) {
			Vertex m1 = new Vertex((t.v1.x + t.v2.x)/2, (t.v1.y + t.v2.y)/2
				, (t.v1.z + t.v2.z)/2);
			Vertex m2 = new Vertex((t.v2.x + t.v3.x)/2, (t.v2.y + t.v3.y)/2
				, (t.v2.z + t.v3.z)/2);
			Vertex m3 = new Vertex((t.v1.x + t.v3.x)/2, (t.v1.y + t.v3.y)/2
				, (t.v1.z + t.v3.z)/2);
			result.add(new Triangle(t.v1, m1, m3, t.color));
			result.add(new Triangle(t.v2, m1, m2, t.color));
			result.add(new Triangle(t.v3, m2, m3, t.color));
			result.add(new Triangle(m1, m2, m3, t.color));
		}
		for (Triangle t : result) {
			for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
				double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(30000);
				//double l = Math.sqrt(0.2 * v.x * v.x + 2 *  v.y * v.y + v.z * v.z) / Math.sqrt(50000);
				v.x /= l;
				v.y /= l;
				v.z /= l;
			}
		}
		return result;
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
