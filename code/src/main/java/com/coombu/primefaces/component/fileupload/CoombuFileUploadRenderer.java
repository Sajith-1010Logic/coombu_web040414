/**
 * 
 */
package com.coombu.primefaces.component.fileupload;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.fileupload.FileUploadRenderer;

/**
 * @author Fekade Aytaged
 *
 */
public class CoombuFileUploadRenderer extends FileUploadRenderer 
{
	@Override
    public void decode(FacesContext context, UIComponent component) {
        if (context.getExternalContext().getRequestContentType().toLowerCase().startsWith("multipart/")) {
            super.decode(context, component);
        }
    }
}
