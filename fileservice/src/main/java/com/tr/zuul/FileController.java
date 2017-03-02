package com.tr.zuul;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Controller;

@Controller
@Path(value = "file")
public class FileController {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/post")
	public String postFile(@FormDataParam("file") final InputStream file) {
		return "post file success";
	}

	@PUT
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/put")
	public String putFile(@FormDataParam("file") final InputStream file) {
		return "put file success";
	}

	@GET
	@Path("/get")
	public String getFile() {
		return "get file success";
	}
}
