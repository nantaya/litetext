package org.toyz.litetext;

import java.io.Serializable;

/**
 * Bitmap character Glyph object
 * 
 * @author David Beckemeyer
 *
 */
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
	
