package com.coombu.photobook.webservice;

import java.io.IOException;
import java.io.InputStream;

import org.primefaces.model.UploadedFile;

public class WSUploadedFile implements UploadedFile {


	private InputStream inputstream;
	private String contentType;
	private long size;
	private String fileName;
	
	WSUploadedFile(InputStream inputStream, String contentType, long size, String fileName)
	{
		this.inputstream = inputStream;
		this.contentType = contentType;
		this.size = size;
		this.fileName = fileName;
	}
	
	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public InputStream getInputstream() throws IOException {
		return inputstream;
	}

	@Override
	public long getSize() {
		return size;
	}

	@Override
	public byte[] getContents() {
		return null;
	}

	@Override
	public String getContentType() {		
		return contentType;
	}

	public void setInputstream(InputStream inputStream) {
		this.inputstream = inputStream;
	}

}
