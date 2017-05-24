/* vim: set tabstop=2: */
/* vim: set softtabstop=2: */
/* vim: set shiftwidth=2: */

package dk_ge;

public class PolygonWrapper implements Comparable<PolygonWrapper>{
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
