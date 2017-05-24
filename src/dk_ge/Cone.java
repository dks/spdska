/* vim: set tabstop=2: */
/* vim: set softtabstop=2: */
/* vim: set shiftwidth=2: */

package dk_ge;

import java.awt.*;

public class Cone {
	public Vertex v1;
	public Vertex v2;
	public double d1;
	public double d2;
	public Color color;
	public Cone(Vertex v1, Vertex v2, double d1, double d2, Color color) {
		this.v1 = v1;
		this.v2 = v2;
		this.d1 = d1;
		this.d2 = d2;
		this.color = color;
	}
}
