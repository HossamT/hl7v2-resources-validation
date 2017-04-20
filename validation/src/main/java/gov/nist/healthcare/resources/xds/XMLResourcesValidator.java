package gov.nist.healthcare.resources.xds;

import gov.nist.healthcare.resources.domain.XMLError;
import gov.nist.healthcare.resources.resolver.ResourceResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLResourcesValidator {

	private String folder;
	private String profile;
	private String constraints;
	private String valueSets;

	public static XMLResourcesValidator createValidatorFromClasspath(String root){
		root = sanitizeRoot(root);
		String constraints = root+"ConformanceContext.xsd";
		String profile = root+"Profile.xsd";
		String valueSets = root+"ValueSets.xsd";
		
		return new XMLResourcesValidator(root,profile,constraints,valueSets);
	}
	
	public static String sanitizeRoot(String root){
		return root.startsWith("/") ? (root.endsWith("/") ? root : root + "/") : (root.endsWith("/") ? "/" + root : "/" + root + "/");
	}
	
	
	private XMLResourcesValidator(String folder, String profile, String constraints, String valueSets) {
		super();
		this.folder = folder;
		this.profile = profile;
		this.constraints = constraints;
		this.valueSets = valueSets;
	}

	//-- Main validation method
	private List<XMLError> validate(InputStream reader, String pathSchema) throws IOException {
		
		InputStream is = this.getClass().getResourceAsStream(pathSchema);
		
		try {
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			factory.setResourceResolver(new ResourceResolver(folder));

			Schema schema = factory.newSchema(new StreamSource(is));

			Validator validator = schema.newValidator();
			
			final ArrayList<XMLError> errors = new ArrayList<>();
			
			//-- Custom ErrorHandler
			validator.setErrorHandler(new ErrorHandler() {
				@Override
				public void warning(SAXParseException exception)
						throws SAXException {
					errors.add(new XMLError(exception));
				}

				@Override
				public void fatalError(SAXParseException exception)
						throws SAXException {
					errors.add(new XMLError(exception));
				}

				@Override
				public void error(SAXParseException exception)
						throws SAXException {
					errors.add(new XMLError(exception));
				}
			});
			
			validator.validate(new StreamSource(reader));
			return errors;
			
		} catch (SAXParseException e) {
			return Arrays.asList(new XMLError(e));
		} catch (SAXException e) {
			return null;
		}
		finally {
			is.close();
		}
	}
	
	//---------------------Shortcut Helper Methods -------------------------------

	public List<XMLError> validateProfile(InputStream profile) throws IOException {
		return this.validate(profile, this.profile);
	}

	public List<XMLError> validateVocabulary(InputStream vocab) throws IOException {
		return this.validate(vocab, this.valueSets);
	}

	public List<XMLError> validateConstraints(InputStream constraints) throws IOException {
		return this.validate(constraints, this.constraints);
	}

	//---------------------------------- MAIN --------------------------------------
	public static void main(String[] args) {

		try {
			XMLResourcesValidator v = XMLResourcesValidator.createValidatorFromClasspath("/xsds/");
			System.out.println("Profile");
			System.out.println(v.validateProfile(XMLResourcesValidator.class.getResourceAsStream("/files/VXU-Z22_Profile.xml")));
			System.out.println("Constraints");
			System.out.println(v.validateConstraints(XMLResourcesValidator.class.getResourceAsStream("/files/VXU-Z22_Constraints.xml")));
			System.out.println("ValueSet");
			System.out.println(v.validateVocabulary(XMLResourcesValidator.class.getResourceAsStream("/files/VXU-Z22_ValueSetLibrary.xml")));

		} catch ( IOException e) {
			e.printStackTrace();
		}

	}

}
