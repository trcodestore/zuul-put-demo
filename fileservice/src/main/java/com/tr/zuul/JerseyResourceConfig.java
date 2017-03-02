package com.tr.zuul;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JerseyResourceConfig extends ResourceConfig {

	@Autowired
	public JerseyResourceConfig() {
		register(MultiPartFeature.class);
		register(FileController.class);
		register(CustomLoggingFilter.class);
	}
}
