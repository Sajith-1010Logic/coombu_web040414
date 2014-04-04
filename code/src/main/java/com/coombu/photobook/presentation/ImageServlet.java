package com.coombu.photobook.presentation;


import static com.coombu.photobook.util.Constants.DEFAULT_BUFFER_SIZE;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import com.coombu.photobook.model.Image;
import com.coombu.photobook.service.IImageService;
/**
 * Servlet implementation class ImageServlet
 */
@Component("ImageServlet")
public class ImageServlet  implements HttpRequestHandler, Serializable
{
	
	private static Logger log = LoggerFactory.getLogger(ImageServlet.class);
	
	 // Constants ----------------------------------------------------------------------------------

	private static final long serialVersionUID = 1L;
	

    // Statics ------------------------------------------------------------------------------------

    @Autowired
    private IImageService imageService;

    @Autowired
    private DashBean dashBean;
    
    @Autowired
    private FileUploadController fileUploadController;
    
	private @Value("${image.root.dir}") String imageRoot;
    // Actions ------------------------------------------------------------------------------------

    /**
	 * @return the imageService
	 */
	public IImageService getImageService() {
		return imageService;
	}

	/**
	 * @param featuredImageService the featuredImageService to set
	 */
	public void setImageService(IImageService imageService) {
		this.imageService = imageService;
	}

	public DashBean getDashBean() {
		return dashBean;
	}

	public void setDashBean(DashBean dashBean) {
		this.dashBean = dashBean;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    }

    // Helpers (can be refactored to public utility class) ----------------------------------------

    private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
            		log.error("Resource could not be cloased:", resource.toString());
            }
        }
    }

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
        // Get ID from request.
        String imageName = request.getParameter("id");
        String type = request.getParameter("type");
        // Check if image name is supplied to the request.
        if (imageName == null) {
            // Do your thing if the ID is not supplied to the request.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        boolean fileFound = false;
        Image imageToServe = null;
       //check if image has just been uploaded
        List<Image> imageList = fileUploadController.getUploadedFiles();
        if (imageList != null)
        {
        	for(Image img: imageList)
        	{
        		if ( imageName.equals(img.getFileName()))
        		{
        			fileFound = true;
        			imageToServe = img;
        			break;
        		}
        	}
        }
        
        // Lookup Image by ImageId in database.
        // Do your "SELECT * FROM Image WHERE ImageID" thing.
        if (fileFound == false)
        	imageToServe = imageService.getImage(imageName, dashBean.getCurrentEventId());

        // Check if image is actually retrieved from database.
        if (imageToServe == null) {
            // Do your thing if the image does not exist in database.
            // Throw an exception, or send 404, or show default/warning image, or just ignore it.
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        // Init servlet response.
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType("image");
        //response.setContentLength((int)imageToServe.getFileSize());
        response.setHeader("Content-Disposition", "inline; filename=\"" + imageToServe.getFileName() + "\"");

        // Prepare streams.
        BufferedOutputStream output = null;
        FileInputStream fis = null;

        try 
        {
        	StringBuffer fullPath = new StringBuffer(this.imageRoot);
        	fullPath.append(imageToServe.getFilePath())
        	        .append(File.separator);
        	if ( type != null)
        	{
        		fullPath.append(type).append("-");
        	}
        	fullPath.append(imageToServe.getFileName());
        	// Open streams.
        	String filePath = fullPath.toString();
        	
        	File file = new File(filePath);
        	response.setContentLength((int)file.length());
        	
        	fis = new FileInputStream(fullPath.toString());
           
        	output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            FileChannel ch = fis.getChannel( );
            MappedByteBuffer mb = ch.map( MapMode.READ_ONLY, 0L, ch.size( ) );
            byte[] barray = new byte[DEFAULT_BUFFER_SIZE];
            long checkSum = 0L;
            int nGet;
            while( mb.hasRemaining( ) )
            {
                nGet = Math.min( mb.remaining( ), DEFAULT_BUFFER_SIZE );
                mb.get( barray, 0, nGet );
                for ( int i=0; i<nGet; i++ )
                    checkSum += barray[i];
                output.write(barray);
            }
            log.debug("Checksum = {}", checkSum);
            // Write file contents to response.

        } finally {
            // Gently close streams.
        	output.flush();
            close(output);
            close(fis);
        }

		
	}

}

