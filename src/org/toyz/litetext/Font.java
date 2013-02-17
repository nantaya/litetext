package org.toyz.litetext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bitmap Font object
 * 
 * @author David Beckemeyer
 *
 */
public class Font implements Serializable {

	private static final long serialVersionUID = -2114235063206424377L;
	private	int maxwidth;
	private int maxheight;
	private int x;
	private int y;
	private List<Glyph> glyph = new ArrayList<Glyph>(256);
	
		/* for compatibility with old pbmtext routines */
		/* oldfont is 0 if the font is BDF derived */
	private int oldfont;
	private int fcols;
	private int frows;

	Font() {
		// need to fill in what this should do and methods
	}

	public void setMaxwidth(int maxwidth) {
		this.maxwidth = maxwidth;
	}

	public int getMaxwidth() {
		return maxwidth;
	}

	public void setMaxheight(int maxheight) {
		this.maxheight = maxheight;
	}

	public int getMaxheight() {
		return maxheight;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public void setOldfont(int oldfont) {
		this.oldfont = oldfont;
	}

	public int getOldfont() {
		return oldfont;
	}

	public void setFcols(int fcols) {
		this.fcols = fcols;
	}

	public int getFcols() {
		return fcols;
	}

	public void setFrows(int frows) {
		this.frows = frows;
	}

	public int getFrows() {
		return frows;
	}
	
	public Glyph getGlyph(int i) {
		return glyph.get(i & 255);
	}
	
	public void addGlyph(Glyph g) {
		this.glyph.add(g);
	}
	public String toString() {
		return "[maxwidth: " + maxwidth + " maxheight: " + maxheight 
		+ " x: " + x + " y: " + y + "]";
	}
}
