The following servlet can be used on AppEngine. It demonstrates the use of litetext.

This source code is included in the SVN truck in `examples/LiteTextServlet.java`
```
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.toyz.litetext.FontUtils;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;

@SuppressWarnings("serial")
public class LiteTextServlet extends HttpServlet {

	
	private static final Logger log = Logger.getLogger(LiteTextServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

        log.info(getServletName() + " started");
 
        String fontname = req.getParameter("f");
        if (fontname == null || fontname.length() == 0) {
        	fontname = "default";
        }
        log.info("using font " + fontname);

        String inputText = req.getParameter("t");
        FontUtils fm = new FontUtils();
        byte[] bmp_data = fm.doRender(inputText, fontname);
        
		log.info("BMP image created");
		
        com.google.appengine.api.images.ImagesService imagesService = ImagesServiceFactory.getImagesService();

		Image bmpImage = ImagesServiceFactory.makeImage(bmp_data);
        com.google.appengine.api.images.Transform flipit = ImagesServiceFactory.makeVerticalFlip();
        Image newImage = imagesService.applyTransform(flipit, bmpImage, com.google.appengine.api.images.ImagesService.OutputEncoding.JPEG);
    
		log.info("JPEG image created");

		resp.setContentType("image/jpeg");
		java.io.ByteArrayInputStream io = new ByteArrayInputStream(
				newImage.getImageData());

		ServletOutputStream svout = resp.getOutputStream();
		int c = -1;
		while ((c = io.read()) != -1) {
			svout.write(c);
		}
		io.close();
		svout.close();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {
		doGet(req, res);
	}
}
```

# HTML Form #

Also included is the HTML form that POSTs to the above servlet in `example/litetextdemo.html`

```
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Render Text to Image Test</title>
  </head>

  <body>
  <h2>Render Text to Image Test</h2>
	<form action="litetextdemo" method="post">
	<table><tr><td align="right" valign="top">Text:&nbsp;</td>
	<td><textarea name="t" cols="80" rows="32"></textarea></td></tr>
	<tr><td align="right">Font:</td><td><select name="f">
	<option value="default" selected>default</option>
<option value="Courier">Courier</option>
<option value="Lucida-Bold-Italic">Lucida-Bold-Italic</option>
<option value="Lucida-Medium">Lucida-Medium</option>
<option value="Lucida-Medium-Italic">Lucida-Medium-Italic</option>
<option value="LucidaBright-DemiBold">LucidaBright-DemiBold</option>
<option value="LucidaTypewriter">LucidaTypewriter</option>
<option value="LucidaTypewriter-Bold">LucidaTypewriter-Bold</option>

</select>	
	<tr><td colspan="2" align="center"><input type="submit" value="Go" /></td></tr></table>
	</form>
  </body>
</html>
```<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Render Text to Image Test</title>
  </head>

  <body>
  <h2>Render Text to Image Test</h2>
	<form action="litetextdemo" method="post">
	<table><tr><td align="right" valign="top">Text:&nbsp;</td>
	<td><textarea name="t" cols="80" rows="32"></textarea></td></tr>
	<tr><td align="right">Font:</td><td><select name="f">
	<option value="default" selected>default</option>
<option value="Courier">Courier</option>
<option value="Lucida-Bold-Italic">Lucida-Bold-Italic</option>
<option value="Lucida-Medium">Lucida-Medium</option>
<option value="Lucida-Medium-Italic">Lucida-Medium-Italic</option>
<option value="LucidaBright-DemiBold">LucidaBright-DemiBold</option>
<option value="LucidaTypewriter">LucidaTypewriter</option>
<option value="LucidaTypewriter-Bold">LucidaTypewriter-Bold</option>

</select>	
	<tr><td colspan="2" align="center"><input type="submit" value="Go" /></td></tr></table>
	</form>
  </body>
</html>
}}}```