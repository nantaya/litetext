# Overview #

This small package (less than 1000 lines of Java code) was developed to provide a small and simple package for rendering text into an image (bitmap).  It was developed for Google App Engine use where AWT and BufferedImage et al do not exist. This small utility can be used to render text on App Engine within the constraints of the JRE Class White List. This is derived from the "pbmtext" utility from NETpbm Copyright (C) 1991 by Jef Poskanzer.

These are crude black-and-white bitmap fonts - no anti-aliasing. A small number of BDF fonts in fixed-sizes are bundled into the package: Courier, Lucida-Bold-Italic, Lucida-Medium, Lucida-Medium-Italic, LucidaBright-DemiBold, LucidaTypewriter, LucidaTypewriter-Bold, and a default serif font.

The following code snippet demonstrates use of litetext:

```
    byte[] bmp_data = doRender("Render this text into a bitmap");

    // bmp_data contains a BMP file
```

# More Information #

  * UsageOverview - Basic Java Usage and Options
  * AppEngine  - Using litetext in App Engine
  * [Live Demo](http://quickbitnotes.appspot.com/litetextform.html) - running on Google App Engine
