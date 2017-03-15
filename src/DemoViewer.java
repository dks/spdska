/* vim: set tabstop=2: */
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

public class DemoViewer {
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
				// Превращаем тетраэдр в сферу
				for (int i = 0; i < 4; i++) { tris = inflate(tris); }
				tris.add(new Triangle(new Vertex(-300, -100, -100),
							new Vertex(-300, 100, -100),
							new Vertex(300, 0, 100),
							Color.PINK));
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
				BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
				g2.translate(getWidth() / 2, getHeight() / 2);

/*				//ГЛАВНЫЙ МЕТОД ОТРИСОВКИ - МОЙ
				g2.setColor(Color.WHITE);
				// Первый проход - преобразования из 3Д в 2Д и отправка в список
				List<PolygonWrapper> polys = new ArrayList<>();
				for (Triangle t : tris) {
					// Вычисление 3Д координат после преобразования
					Vertex v1 = transform.transform(t.v1);
					Vertex v2 = transform.transform(t.v2);
					Vertex v3 = transform.transform(t.v3);
					// Для проскопараллельной проекции просто берем координаты х и у
					int[] xs = {(int)v1.x,(int)v2.x,(int)v3.x};
					int[] ys = {(int)v1.y,(int)v2.y,(int) v3.y};
					Polygon plg = new Polygon(xs,ys,3);
					// Вычисление вектора нормали для плоского затенения
					Vertex n = new Vertex(		
						((v2.y-v1.y)*(v3.z-v1.z))-((v2.z-v1.z)*(v3.y-v1.y)),
						(-1)*(((v2.x-v1.x)*(v3.z-v1.z))-((v3.x-v1.x)*(v2.z-v1.z))),
						((v2.x-v1.x)*(v3.y-v1.y))-((v2.y-v1.y)*(v3.x-v1.x))
					);
					// Set lightsource vector
					Vertex ls = new Vertex(0,0,1);
					// Вычисляем косинус, берем абсолютную величину как множитель для цвета
					double cos = Math.abs( (ls.x*n.x+ls.y*n.y+ls.z*n.z)/
						(Math.sqrt(ls.x*ls.x+ls.y*ls.y+ls.z*ls.z)*Math.sqrt(n.x*n.x+n.y*n.y+n.z*n.z)) );
					// Добавляем результат всех вычислений в общий массив
					polys.add(new PolygonWrapper((int)(v1.z+v2.z+v3.z)/3,plg,getShade(t.color, cos)) );
				}
				// Сортировка по глубине (Z-буфер)
				Collections.sort(polys);
				// Второй проход - отрисовка отсортированных многоугольников
				for (PolygonWrapper p : polys) {
					// Можно задать текстуру вместо цвета
					g2.setPaint(p.getColor());
					g2.fill(p.getPolygon());
					//g2.setPaint(Color.WHITE);
					//g2.draw(p.getPolygon());
				}// Конец рендеринга */

/*			//DRAWING CORE FOR MESHED GRAPHICS - general version
				g2.translate(getWidth() / 2, getHeight() / 2);
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
				}// */

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
					double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
					norm.x /= normalLength;
					norm.y /= normalLength;
					norm.z /= normalLength;
					// Задаем косинус между нормалью треугольника и направлением света
					// так как наш источник света идет из (0,0,1) изменяется только одна величина
					double angleCos = Math.abs(norm.z);
					// Определение прямоугольных границ расчитываемых треугольников
					int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
					int maxX = (int) Math.min(img.getWidth()-1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
					int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
					int maxY = (int) Math.min(img.getHeight()-1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));
					double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
					// Основной цикл рендеринга
					// Построчная развертка по пикселям в границах прямоугольников рассчитанных выше
					for (int y = minY; y <= maxY; y++) {
						for (int x = minX; x <= maxX; x++) {
							double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
							double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
							double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
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
		/* int red = (int) (color.getRed() * shade);
    		int green = (int) (color.getGreen() * shade);
		int blue = (int) (color.getBlue() * shade); */
		return new Color(red, green, blue);
	}

	public static List<Triangle> inflate(List<Triangle> tris) {
		List<Triangle> result = new ArrayList<>();
		for (Triangle t : tris) {
			Vertex m1 = new Vertex((t.v1.x + t.v2.x)/2, (t.v1.y + t.v2.y)/2, (t.v1.z + t.v2.z)/2);
			Vertex m2 = new Vertex((t.v2.x + t.v3.x)/2, (t.v2.y + t.v3.y)/2, (t.v2.z + t.v3.z)/2);
			Vertex m3 = new Vertex((t.v1.x + t.v3.x)/2, (t.v1.y + t.v3.y)/2, (t.v1.z + t.v3.z)/2);
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
