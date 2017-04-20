package gov.nist.healthcare.resources.resolver;

import java.io.InputStream;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import gov.nist.healthcare.resources.domain.Input;

public class ResourceResolver  implements LSResourceResolver {

	private String root;
	
	public ResourceResolver(String root) {
		super();
		this.root = root;
	}


	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		systemId = this.root+systemId;
		InputStream resourceAsStream = this.getClass().getResourceAsStream(systemId);
		return new Input(publicId, this.root+systemId, resourceAsStream);
	}

 }