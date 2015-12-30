# App Engine Java Images API #

The resulting BMP file returned by `doRender()` can be passed to the App Engine Java Images API to produce, e.g. a JPEG image as follows:

```
        byte[] bmp_data = fm.doRender(inputText, fontname);
		
        com.google.appengine.api.images.ImagesService imagesService = ImagesServiceFactory.getImagesService();

	Image bmpImage = ImagesServiceFactory.makeImage(bmp_data);
        com.google.appengine.api.images.Transform flipit = ImagesServiceFactory.makeVerticalFlip();
        Image newImage = imagesService.applyTransform(flipit, bmpImage, com.google.appengine.api.images.ImagesService.OutputEncoding.JPEG);
    
```

# App Engine Limitations #

The Images API limits requests to 1MB.  This means that the resulting text image can be only about 500x500 pixels or so. Therefore, litetxt is restricted to a few short paragraphs of text per image on App Engine.