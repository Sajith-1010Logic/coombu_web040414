package com.coombu.photobook.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.coombu.photobook.model.Comment;
import com.coombu.photobook.model.Event;
import com.coombu.photobook.model.EventSecurityUser;
import com.coombu.photobook.model.Image;
import com.coombu.photobook.model.ImageTag;
import com.coombu.photobook.model.ImageVote;
import com.coombu.photobook.model.UserProfile;

public class JsonGenerator 
{
	public static void main(String[] args) 
	{
		//ApplicationContext context= new ClassPathXmlApplicationContext(
		//	    "/spring/application-config.xml");
		AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
	    AnnotationIntrospector secondary = new JaxbAnnotationIntrospector();
	    AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
		ObjectMapper mapper = new ObjectMapper();
		mapper.getDeserializationConfig().setAnnotationIntrospector(pair);
		mapper.getSerializationConfig().setAnnotationIntrospector(pair);
		
	 
	try {
		Image image = new Image();
		image.setImageId(7);
		Event event = new Event();
		EventSecurityUser eventSecurityUser = new EventSecurityUser();
		UserProfile profile = new UserProfile();
		Comment comment = new Comment();
		ImageVote vote = new ImageVote();
		ImageTag tag = new ImageTag();
		comment.setImage(image);
		vote.setImage(image);
		tag.setImage(image);
		// display to console
		ObjectWriter writer = mapper.defaultPrettyPrintingWriter();
		//System.out.println("Event = " + writer.writeValueAsString(event));
		//System.out.println("EventSecurityUser = " + writer.writeValueAsString(eventSecurityUser));
		//System.out.println("UserProfile = " + writer.writeValueAsString(profile));
//		System.out.println("Comment = " + writer.writeValueAsString(comment));
		//System.out.println("Vote = " + writer.writeValueAsString(vote));
//		System.out.println("ImageTag = " + writer.writeValueAsString(tag));
		System.out.println("Image = " + writer.writeValueAsString(image));
 
	} catch (JsonGenerationException e) {
 
		e.printStackTrace();
 
	} catch (JsonMappingException e) {
 
		e.printStackTrace();
 
	} catch (IOException e) {
 
		e.printStackTrace();
 
	}
	}
}
