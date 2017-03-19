/* vim: set tabstop=2: */
/* vim: set softtabstop=2: */
/* vim: set shiftwidth=2: */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.event.*;

public class MyDemo {
  public static JSlider headingSlider = new JSlider(-180, 180, 0);
	public static	JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -90, 90, 0);
	public static JPanel renderPanel;
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(headingSlider, BorderLayout.SOUTH);
		pane.add(pitchSlider, BorderLayout.EAST);

		// panel to display render results
		renderPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				// Создаем черный экран
				g2.setColor(Color.BLACK);
				g2.fillRect(0, 0, getWidth(), getHeight());
				// Создаем первоначальный тетраэдр из четырех треугольников
				List<Triangle> tris = new ArrayList<>();
				tris.add(new Triangle(new Vertex(100, 100, 100),
					new Vertex(-100, -100, 100),
					new Vertex(100, -100, -100), Color.RED));

				List<Cone> conlist = new ArrayList<>();
				conlist.add(new Cone(new Vertex(0, 0, 100),
					new Vertex(0, 0, -100), 100.0, 100.0, Color.RED));
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
				Matrix3 transform = headingTransform.multiply(pitchTransform);
				// Инициализируем видеобуфер
				BufferedImage img = new BufferedImage(getWidth() 
					, getHeight(), BufferedImage.TYPE_INT_ARGB);
				g2.translate(getWidth() / 2, getHeight() / 2);

				//DRAWING CORE FOR MESHED GRAPHICS - general version
				/*g2.setColor(Color.WHITE);
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
				}// */

				for (Cone c : conlist) {
					Vertex v1 = transform.transform(c.v1);
					Vertex v2 = transform.transform(c.v2);
					Path2D path = new Path2D.Double();
					g2.setColor(c.color);
					path.moveTo(v1.x, v1.y);
					path.lineTo(v2.x, v2.y);
					path.closePath();
					g2.draw(path);
					g2.drawOval( (int) (v1.x-c.r1/2), (int) (v1.y-c.r1/2)
						, (int) c.r1, (int) c.r1);
					g2.drawOval( (int) (v2.x-c.r2/2), (int) (v2.y-c.r2/2)
						, (int) c.r2, (int) c.r2);
				}// */

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
		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

class Vertex {
    double x;
    double y;
    double z;
    Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

class Triangle {
    Vertex v1;
    Vertex v2;
    Vertex v3;
    Color color;
    Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }
}
class Cone {
	Vertex v1;
	Vertex v2;
	double r1;
	double r2;
	Color color;
	Cone(Vertex v1, Vertex v2, double r1, double r2, Color color) {
		this.v1 = v1;
		this.v2 = v2;
		this.r1 = r1;
		this.r2 = r2;
		this.color = color;
	}
}

class Matrix3 {
	double[] values;
	Matrix3(double[] values) {
		this.values = values;
	}
	Matrix3 multiply(Matrix3 other) {
		double[] result = new double[9];
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				for (int i = 0; i < 3; i++) {
					result[row * 3 + col] +=
						this.values[row * 3 + i] * other.values[i * 3 + col];
				}
			}
		}
		return new Matrix3(result);
	}
	Vertex transform(Vertex in) {
		return new Vertex(
				in.x * values[0] + in.y * values[3] + in.z * values[6],
				in.x * values[1] + in.y * values[4] + in.z * values[7],
				in.x * values[2] + in.y * values[5] + in.z * values[8]
				);
	}
}

class PolygonWrapper implements Comparable<PolygonWrapper>{
	private Polygon pol;
	private int Z;
	private Color col;

	public PolygonWrapper(int Z, Polygon pol, Color col){
		this.pol = pol;
		this.Z = Z;
		this.col = col;
	}

	public int getZ(){ return Z; }
	public Color getColor(){ return col; }
	public Polygon getPolygon(){ return pol; }

	@Override
	public int compareTo(PolygonWrapper pw){
		return (this.getZ() < pw.getZ() ? -1 : (this.getZ() == pw.getZ() ? 0 : 1));
	}
	@Override
	public String toString(){
		return "Poligon: Z-ind="+this.getZ()+"; coords: "+pol.xpoints[0]+", "+pol.ypoints[0]+"; "+pol.xpoints[1]+", "+pol.ypoints[1]+"; "+pol.xpoints[2]+", "+pol.ypoints[2]+"; ";
	}
}