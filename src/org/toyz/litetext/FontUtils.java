package org.toyz.litetext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/* Derived from pbmtext.c - render text into a bitmap
**
** Copyright (C) 1991 by Jef Poskanzer.
**
** Permission to use, copy, modify, and distribute this software and its
** documentation for any purpose and without fee is hereby granted, provided
** that the above copyright notice appear in all copies and that both that
** copyright notice and this permission notice appear in supporting
** documentation.  This software is provided "as is" without express or
** implied warranty.
*/


public class FontUtils {
	
	private int maxwidth;
	private int maxleftb;
	private int bwid;
	private int backup_space_needed;
	private int width;
	private int height;
	private int byte_width;
	private int pad_width;
	private int bmp_len;
	
	private int cols;
	private int rows;
	
    // Returns the contents of the file in a byte array.
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    public static byte[] getBytesFromInputStream(InputStream is, int bufSize) throws IOException {

        // Create the byte array to hold the data
        byte[] bytes = new byte[bufSize];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "/*+file.getName()*/);
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    static public Font loadfont(String fontname) {
		
		Font font = new Font();
		Properties fontprops = new Properties();
//		FileInputStream in;
        InputStream in;
        in = FontUtils.class.getResourceAsStream("/fonts/" + fontname + "/font.txt");
        if (in == null) {
        	return null;
        }
		try {
			fontprops.load(in);
            in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    font.setMaxwidth(Integer.parseInt(fontprops.getProperty("maxwidth")));
	    font.setMaxheight(Integer.parseInt(fontprops.getProperty("maxheight")));
	    font.setX(Integer.parseInt(fontprops.getProperty("x")));
	    font.setY(Integer.parseInt(fontprops.getProperty("y")));
	    font.setOldfont(Integer.parseInt(fontprops.getProperty("oldfont")));
	    font.setFcols(Integer.parseInt(fontprops.getProperty("fcols")));
	    font.setFrows(Integer.parseInt(fontprops.getProperty("frows")));
	    for (int i = 0; i < 256; i++) {
            in = FontUtils.class.getResourceAsStream("/fonts/" + fontname + "/glyph." + i);
            if (in == null) {
            	font.addGlyph(null);
				continue;
            }
			Properties gprops = new Properties();
			try {
				gprops.load(in);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				continue;
			}
			Glyph g = new Glyph();
			g.setWidth(Integer.parseInt(gprops.getProperty("width")));
			g.setHeight(Integer.parseInt(gprops.getProperty("height")));
			g.setX(Integer.parseInt(gprops.getProperty("x")));
			g.setY(Integer.parseInt(gprops.getProperty("y")));
			g.setXadd(Integer.parseInt(gprops.getProperty("xadd")));

			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
                InputStream is = FontUtils.class.getResourceAsStream("/fonts/" + fontname + "/glyph_bmap." + i);
                g.setBmap(getBytesFromInputStream(is, g.getWidth() * g.getHeight()));
//				g.setBmap(getBytesFromFile(new File("fonts/" + fontname + "/glyph_bmap." + i)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			font.addGlyph(g);

	    }
		return font;
	}
    
    public Font loadfontobject(String fontfile) throws IOException, ClassNotFoundException {
    	FileInputStream fis;
    	fis = new FileInputStream(fontfile);
    	ObjectInputStream ois = new ObjectInputStream(fis);
    	Font font = (Font) ois.readObject();
    	ois.close();
    	return font;
    }
    
	public void get_line_dimensions(String line, Font font, float intercharacter_space) {
		/*----------------------------------------------------------------------------
		   Determine the width in pixels of the line of text line[] in the font
		   *font_p, and return it as *bwid_p.  Also determine how much of this
		   width goes to the left of the nominal starting point of the line because
		   the first character in the line has a "backup" distance.  Return that
		   as *backup_space_needed_p.
		-----------------------------------------------------------------------------*/
	    int cursor;  /* cursor into the line of text */
	    float accumulated_ics;
	       /* accumulated intercharacter space so far in the line we are
        stepping through.  Because the intercharacter space might not be
        an integer, we accumulate it here and realize full pixels whenever
        we have more than one pixel.
        */
 
	    boolean no_chars_yet;
	       /* logical: we haven't seen any renderable characters yet in
        the line.
     */
 	Glyph lastGlyph = null;
     /* Glyph of last character processed so far.  Undefined if
        'no_chars_yet'.
     */

 	no_chars_yet = true;   /* initial value */
 	bwid = 0;  /* initial value */
 	accumulated_ics = (float) 0.0;  /* initial value */
 	
    for (cursor = 0; cursor < line.length(); cursor++) {
        
        Glyph g = font.getGlyph((int)line.charAt(cursor));    
        if (g == null) {
        	g = font.getGlyph(' ');
        }
        if (g != null) {
            if (no_chars_yet) {
                no_chars_yet = false;
                if (g.getX() < 0)
                    backup_space_needed = -g.getX();
                else {
                    backup_space_needed = 0;
                    bwid += g.getX();
                }
            } else { 
                /* handle extra intercharacter space (-space option) */
                int full_pixels;  /* integer part of accumulated_ics */
                accumulated_ics += intercharacter_space;
                full_pixels = (int) accumulated_ics;
                bwid += full_pixels;
                accumulated_ics -= full_pixels;
            }
            lastGlyph = g;
            bwid += g.getXadd();
        }       
    }
    if (no_chars_yet)
        /* Line has no renderable characters */
        backup_space_needed = 0;
    else {
        /* Line has at least one renderable character.
           Recalculate width of last character in line so it ends
           right at the right edge of the glyph (no extra space to
           anticipate another character).
        */
        bwid -= lastGlyph.getXadd();
        bwid += lastGlyph.getWidth() + lastGlyph.getX();
    }

	}


	public void compute_image_width(String [] lines, Font font, float intercharacter_space) {
		maxwidth = 0;  /* initial value */
		maxleftb = 0;  /* initial value */
		for (int i = 0; i < lines.length; i++) {
			get_line_dimensions(lines[i], font, intercharacter_space);
			if (bwid > maxwidth)
				maxwidth = bwid;
			if (backup_space_needed > maxleftb)
				maxleftb = backup_space_needed;
		}
	}

	public void insert_character(Glyph glyph, int toprow, int leftcol, byte[] bits)	{
		/*----------------------------------------------------------------------------
		   Insert one character (whose glyph is 'glyph') into the image bits[].
		   Its top left corner shall be row 'toprow', column 'leftcol'.
		-----------------------------------------------------------------------------*/
		                  
		    int glyph_y; 
		    int glyph_x;  /* position within the glyph */
		                  
		    for (glyph_y = 0; glyph_y < glyph.getHeight(); glyph_y++) {
		        for (glyph_x = 0; glyph_x < glyph.getWidth(); glyph_x++) {
		            if (glyph.getPixel((int)(glyph_y * glyph.getWidth() + glyph_x)) != 0) {
		            	int g_row = toprow+glyph_y;
		            	int g_col = leftcol + glyph.getX() + glyph_x;
		            	int basep = g_row * pad_width + g_col*3;
		            	if (basep > bmp_len) {
		            		System.out.println("basep: " + basep + " bmp_len: " + bmp_len +
		            				" g_row: " + g_row + " g_col: " + g_col +
		            				" toprow: " + toprow + " leftcol: " + leftcol +
		            				" glyph_x: " + glyph_x + " glyph_y: " + glyph_y +
		            				" glyph: " + glyph.toString());
		            		return;
		            		
		            	}
		                bits[basep] = 0; /* Black #000000 */
		                bits[basep+1] = 0;
		                bits[basep+2] = 0;

		            }
		        }
		    }
		}
	
	public void insert_characters(byte[] bits, String[] lines, Font font,
			int topmargin, int leftmargin) {
		float intercharacter_space = (float) 0.0;
		int lspace = 0;

		/*----------------------------------------------------------------------------
		   Render the text 'lp' into the image 'bits' using font *fontP and 
		   putting 'intercharacter_space' pixels between characters and
		   'lspace' pixels between the lines.
		-----------------------------------------------------------------------------*/
		for (int line = 0; line < lines.length; ++line) {

			int row; /* row in image of top of current typeline */
			int leftcol; /* Column in image of left edge of current glyph */
			int cursor; /* cursor into a line of input text */
			float accumulated_ics;
			/*
			 * accumulated intercharacter space so far in the line we are
			 * building. Because the intercharacter space might not be an
			 * integer, we accumulate it here and realize full pixels whenever
			 * we have more than one pixel.
			 */

			row = topmargin + line * (font.getMaxheight() + lspace);
			leftcol = leftmargin;
			accumulated_ics = (float) 0.0; /* initial value */

			for (cursor = 0; cursor < lines[line].length(); ++cursor) {
				int glyphIndex = (int) lines[line].charAt(cursor);
				Glyph glyph; /* the glyph for this character */

				glyph = font.getGlyph(glyphIndex);
				if (glyph == null) {
					glyph = font.getGlyph(' ');
				}
				if (glyph != null) {
					int toprow = row + font.getMaxheight() + font.getY()
							- glyph.getHeight() - glyph.getY();
					/* row number in image of top row in glyph */

					insert_character(glyph, toprow, leftcol, bits);

					leftcol += glyph.getXadd();
					{
						/* handle extra intercharacter space (-space option) */
						int full_pixels; /* integer part of accumulated_ics */
						accumulated_ics += intercharacter_space;
						full_pixels = (int) accumulated_ics;
						if (full_pixels > 0) {
							leftcol += full_pixels;
							accumulated_ics -= full_pixels;
						}
					}

				}
			}
		}
        
	}
	
	static String [] wrapText (String text, int len)
	{
	  // return empty array for null text
	  if (text == null)
	  return new String [] {};

	  // return text if len is zero or less
	  if (len <= 0)
	  return new String [] {text};

	  // return text if less than length
	  if (text.length() <= len)
	  return new String [] {text};

	  char [] chars = text.toCharArray();
	  Vector<String> lines = new Vector<String>();
	  StringBuffer line = new StringBuffer();
	  StringBuffer word = new StringBuffer();

	  for (int i = 0; i < chars.length; i++) {
		if (chars[i] == '\r')
			continue;
		if (chars[i] == '\n') {
				if ((line.length() + word.length()) > len) {
					lines.add(line.toString());
					line.delete(0, line.length());
				}

				line.append(word);
				word.delete(0, word.length());
				lines.add(line.toString());
				line.delete(0, line.length());
				continue;
		}
	    word.append(chars[i]);

	    if (chars[i] == ' ') {
	      if ((line.length() + word.length()) > len) {
	        lines.add(line.toString());
	        line.delete(0, line.length());
	      }

	      line.append(word);
	      word.delete(0, word.length());
	      
	    }
	  }

	  // handle any extra chars in current word
	  if (word.length() > 0) {
	    if ((line.length() + word.length()) > len) {
	      lines.add(line.toString());
	      line.delete(0, line.length());
	    }
	    line.append(word);
	  }

	  // handle extra line
	  if (line.length() > 0) {
	    lines.add(line.toString());
	  }

	  String [] ret = new String[lines.size()];
	  int c = 0; // counter
	  for (Enumeration<String> e = lines.elements(); e.hasMoreElements(); c++) {
	    ret[c] = (String) e.nextElement();
	  }

	  return ret;
	}
	
	public int getCols() {
		return cols;
	}
	
	public int getRows() {
		return rows;	
	}
	
	public byte[] doRender(String inputText, String fontname)
			throws IOException {
        int info_len = 40;
        int header_len = 14;
        int rgb_len = 0;

        //log.info(getServletName() + " started");
 
        if (fontname == null || fontname.length() == 0) {
        	fontname = "default";
        }
        //log.info("loading font " + fontname);

        Font font;
		font = loadfont(fontname);
		if (font == null) {
			//log.severe("cannot load font: " + fontname);
			return null;
		}
        //log.info("font " + fontname + " loaded: " + font.toString());
        int vmargin = font.getMaxheight() / 2;
        int hmargin = font.getMaxwidth();
        
        String [] lines = wrapText(inputText, 80);
        //log.info("text wrapped into " + lines.length + " lines");
        
        this.rows = 2 * vmargin + lines.length * font.getMaxheight();

        compute_image_width(lines, font, (float)0.0);
       
        this.cols = 2 * hmargin + maxwidth;
        //log.info("image width computed: " + cols);

        height = rows+40;
        width = cols+40;
        byte_width = width * 3;

        pad_width = (byte_width+3) & ~3;
        bmp_len = pad_width * height;
                
        byte[] bmp_data = new byte[(int)bmp_len];
        for (int i = 0; i < bmp_len; i++) {
        	bmp_data[i] = (byte) 255;
        }
       /* log.info("width: " + width + " height: " + height +
        		" pad_width: " + pad_width + " bmp_len: " + bmp_len);
        		*/
        
        insert_characters(bmp_data, lines, font, vmargin, hmargin + maxleftb);
        //log.info("raw bitmap rendered");
        
		LittleEndianOutputStream sout;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			sout = new LittleEndianOutputStream(bos);
			// first, the identifying bytes
			//sout.write('B');
			//sout.write('M');
			sout.writeShortLE((short)19778);

			// calculate and output the total bmp size
			sout.writeIntLE(header_len + info_len + rgb_len + bmp_len);

			// reserved bytes
			sout.writeShortLE((short) 0);
			sout.writeShortLE((short)0);
			
			// offset to bmp data
			sout.writeIntLE(header_len + info_len + rgb_len);
			
			// now output the info block (v3-style .bmp)
			sout.writeIntLE(40);
			sout.writeIntLE(width);
			sout.writeIntLE(height);
			
			sout.writeShortLE((short)0);
			sout.writeShortLE((short)24); // bpp
			
			sout.writeIntLE(0);
			sout.writeIntLE(0);

			sout.writeIntLE(0);
			sout.writeIntLE(0);
			
			// rgb_quads
			sout.writeIntLE(0);
			sout.writeIntLE(0);
			
		    /*
		     * at last, the bitmap data.  note that each bitmap format has enough padding
		     * added to the line to ensure proper dword-alignment, no matter how much
		     * pixel data is provided.
		     */
			for (int row = 0; row < height; row++) {
				
				int col = 0;
				for (; col < width; col++) {
					int r, g, b;
					{
						r = bmp_data[pad_width * row + col * 3];
						g = bmp_data[pad_width * row + col * 3 + 1];
						b = bmp_data[pad_width * row + col * 3 + 2];
					}
					sout.write(b);
					sout.write(g);
					sout.write(r);
				}
				for (int pad = byte_width; pad < pad_width; pad++) {
					sout.write(0);
				}

			}


			sout.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return bos.toByteArray();

	}
	
	public byte[] doRender(String inputText) throws IOException {
		return doRender(inputText, "default");
	}
}
