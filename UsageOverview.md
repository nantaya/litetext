# Usage #

This package provides a simple text rendering capability.  In its simplest form, it can be used like this:

```
   import org.toyz.litetext.FontUtils

   // ...

   byte[] bmp_data = doRender("Render this text into a bitmap");

   // bmp_data contains a BMP file
```

There is a working example Servlet in `examples/LiteTextServlet.java`


## Selecting Fonts ##

Select a font by specifying the font by name (String):

```
   byte[] bmp_data = doRender("Render this text into a bitmap using specified font", "Courier");
```

Available fonts are specified by name, as follows:

  * Courier
  * Lucida-Bold-Italic
  * Lucida-Medium
  * Lucida-Medium-Italic
  * LucidaBright-DemiBold
  * LucidaTypewriter
  * LucidaTypewriter-Bold
  * default

## Font Color ##

The font (foreground) color can be set with the `setFgcolor(int[])` method, before calling `doRender()` e.g.:

```
    FontUtils fm = new FontUtils();
    // RGB color components for font (foreground) color
    fgcolor[0] = 221; // #DDE0F4 (light gray)
    fgcolor[1] = 224; 
    fgcolor[2] = 244;
    fm.setFgcolor(fgcolor);
```

## Background Color/Gradient ##

The background is specified as a gradient, an array of RGB color components using  `setGradient(int[][])`.  For example, to set a single color background, supply a one-row array (one pixel) as shown below:

```
    int[][] graybg = new int[1][3];
    graybg[0][0] = 221; // #DDE0F4 (light gray)
    graybg[0][1] = 224;
    graybg[0][2] = 244;
    fm.setGradient(graybg);
```

To use a gradient, supply more rows (pixels), which basically represent a one-pixel wide vertical bar of pixels (rows of RGB values). E.g.:

```
    int[][] gradar = new int[16][3];
    gradar[0][0] = 210; gradar[0][1] = 213; gradar[0][2] = 240;
    gradar[1][0] = 213; gradar[1][1] = 216; gradar[1][2] = 241;
    gradar[2][0] = 216; gradar[2][1] = 219; gradar[2][2] = 242;
    gradar[3][0] = 221; gradar[3][1] = 224; gradar[3][2] = 244;
    gradar[4][0] = 224; gradar[4][1] = 226; gradar[4][2] = 245;
    gradar[5][0] = 225; gradar[5][1] = 227; gradar[5][2] = 245;
    gradar[6][0] = 230; gradar[6][1] = 232; gradar[6][2] = 247;
    gradar[7][0] = 231; gradar[7][1] = 233; gradar[7][2] = 247;
    gradar[8][0] = 236; gradar[8][1] = 237; gradar[8][2] = 249;
    gradar[9][0] = 239; gradar[9][1] = 240; gradar[9][2] = 250;
    gradar[10][0] = 242; gradar[10][1] = 243; gradar[10][2] = 251;
    gradar[11][0] = 243; gradar[11][1] = 244; gradar[11][2] = 251;
    gradar[12][0] = 246; gradar[12][1] = 247; gradar[12][2] = 252;
    gradar[13][0] = 248; gradar[13][1] = 249; gradar[13][2] = 253;
    gradar[14][0] = 251; gradar[14][1] = 251; gradar[14][2] = 254;
    gradar[15][0] = 252; gradar[15][1] = 252; gradar[15][2] = 254;
    fm.setGradient(gradar);
```

# Ant Build #

Type `ant jar` to create `build/litetext.jar`

# Developer's Guide #

The main class of litetext is:

  * FontUtils

Other classes include:

  * Font
  * Glyph
  * EndianUtils
  * LittleEndianOutputStream