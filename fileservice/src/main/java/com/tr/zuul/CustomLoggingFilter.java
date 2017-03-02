package com.tr.zuul;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.message.internal.ReaderWriter;

@Provider
public class CustomLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
	@Context
	private ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String newline = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append(" - Path: ").append(requestContext.getUriInfo().getPath()).append(newline);
		sb.append(" - Header: ").append(requestContext.getHeaders()).append(newline);
		sb.append(" - Entity: ").append(getEntityBody(requestContext)).append(newline);
		
		System.out.println("==================================================================");
		System.out.println("HTTP REQUEST : " + sb.toString());
		System.out.println("==================================================================");
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		String newline = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		sb.append("Header: ").append(responseContext.getHeaders()).append(newline);
		sb.append(" - Entity: ").append(responseContext.getEntity()).append(newline);
		
		System.out.println("==================================================================");
		System.out.println("HTTP RESPONSE : " + sb.toString());
		System.out.println("==================================================================");
	}
	
	private String getEntityBody(ContainerRequestContext requestContext) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = requestContext.getEntityStream();

		final StringBuilder b = new StringBuilder();
		try {
			ReaderWriter.writeTo(in, out);

			byte[] requestEntity = out.toByteArray();
			if (requestEntity.length == 0) {
				b.append("").append("\n");
			} else {
				b.append(new String(requestEntity)).append("\n");
			}
			requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));

		} catch (IOException ex) {
		}
		return b.toString();
	}
}