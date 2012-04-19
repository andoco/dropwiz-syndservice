package com.example.feedservice.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

@Provider
@Produces(MediaType.APPLICATION_XML)
public class SyndFeedMessageBodyWriter implements MessageBodyWriter<SyndFeed> {

	@Override
	public long getSize(SyndFeed arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return SyndFeed.class.isAssignableFrom(arg0);
	}

	@Override
	public void writeTo(SyndFeed arg0, Class<?> arg1, Type arg2,
			Annotation[] arg3, MediaType arg4,
			MultivaluedMap<String, Object> arg5, OutputStream arg6)
			throws IOException, WebApplicationException {
		
		Writer writer = new OutputStreamWriter(arg6);
		
		SyndFeedOutput output = new SyndFeedOutput();
		try {
			output.output(arg0, writer);
		} catch (FeedException e) {
			e.printStackTrace();
		}
	}

}
