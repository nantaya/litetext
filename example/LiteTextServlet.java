

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
