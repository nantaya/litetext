# Overview #

Fonts are simple bitmap fonts.  Ultimately fonts are contained in the Font class:

```
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
```

Each printable character known by the font is stored in a Glyph, indexed by its ASCII code.

```
public class Glyph implements Serializable {

	private static final long serialVersionUID = 1L;

	private	int width; 
	private int height;
	private int x;
	private int y;
	private int xadd;
	byte[] bmap;

	Glyph() {
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setXadd(int xadd){
		this.xadd = xadd;
	}
	public void setBmap(byte[] bmap) {
		this.bmap = bmap;
	}
	public void setBmap(int i, byte v) {
		this.bmap[i] = v;
	}
	public int getPixel(int i) {
		return (int)bmap[i];
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getXadd() {
		return xadd;
	}
	public byte[] getBmap() {
		return bmap;
	}
	public String toString() {
		return "[ width: " + width + " height: " + height + " x: " + x
		+ " y: " + y + " xadd: " + xadd + "]";
	}
}
```

## Font Loading ##

Fonts are stuffed into the JAR and loaded from file-data into the above classes.  See the `loadfont(String fontname)` method in FontUtils for more details.

## Font Files ##

The font data for a given font is stored in font files located in the JAR as `/fonts/{fontname}/*`

Each font has a `font.txt` file and N `glyph.*` files (`glyph.32` - `glyph.255` one per ASCII printable character in this font).  These files are Properties format compatible with the java.util.Properties class.

In addition, each glyph (printable character) has a corresponding file containing the bitmap for the character `glyph_bmap.*` a simple array of bytes, each representing a bit.

Additional bitmap fonts can be used by organizing the font data and bitmaps into these files in the proper format.

You may add them into the JAR by placing the new font data files into a `resources/font/{fontname}/` directory and running `ant jar`. Applications then select the font by name in `doRender()`.

Example font.txt:
```
maxwidth = 14
maxheight = 20
x = -3
y = -5
oldfont = 0
fcols = 0
frows = 0
```

Example glyph.97 (lower-case 'a' data) file:
```
width = 7
height = 7
x = 1
y = 0
xadd = 9
```

Hexdump of corresponding glyph\_bmap.97 (bitmap for lower-case 'a'):
```
0000000 00 01 01 01 01 00 00 01 00 00 00 00 01 00 00 00
0000010 00 00 00 01 00 00 01 01 01 01 01 00 01 00 00 00
0000020 00 01 00 01 00 00 00 00 01 00 00 01 01 01 01 00
0000030 01                                             
0000031
```