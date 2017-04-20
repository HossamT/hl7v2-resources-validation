package gov.nist.healthcare.resources.xds;

import gov.nist.healthcare.resources.domain.XMLError;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidateXSD {

	private InputStream profileXSD;
	private InputStream valueSetsXSD;
	private InputStream conformanceContextXSD;

	public ValidateXSD() {
		super();
	}
	

	private List<XMLError> validate(String file, InputStream xsd) throws IOException {
		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			StringReader reader = new StringReader(file);
			Schema schema;
			schema = factory.newSchema(new StreamSource(xsd));

			Validator validator = schema.newValidator();
			final ArrayList<XMLError> errors = new ArrayList<>();
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
	}

	public List<XMLError> validateProfile(String profile) throws IOException {
		this.profileXSD = ValidateXSD.class.getResourceAsStream("/xsd/Profile.xsd");
		List<XMLError> results = this.validate(profile, profileXSD);
		profileXSD.close();
		return results;
	}

	public List<XMLError> validateVocabulary(String vocab) throws IOException {
		this.valueSetsXSD = ValidateXSD.class.getResourceAsStream("/xsd/ValueSets.xsd");
		List<XMLError> results = this.validate(vocab, valueSetsXSD);
		valueSetsXSD.close();
		return results;
	}

	public List<XMLError> validateConstraints(String constraints) throws IOException {
		this.conformanceContextXSD = ValidateXSD.class.getResourceAsStream("/xsd/ConformanceContext.xsd");	
		List<XMLError> results = this.validate(constraints, conformanceContextXSD);
		conformanceContextXSD.close();
		return results;
	}

	public static void main(String[] args) {
		String profile = new Scanner(
				ValidateXSD.class
						.getResourceAsStream("/files/VXU-Z22_Profile.xml"),
				"UTF-8").useDelimiter("\\A").next();
		String constraints = new Scanner(
				ValidateXSD.class
						.getResourceAsStream("/files/VXU-Z22_Constraints.xml"),
				"UTF-8").useDelimiter("\\A").next();
		String valueSet = new Scanner(
				ValidateXSD.class
						.getResourceAsStream("/files/VXU-Z22_ValueSetLibrary.xml"),
				"UTF-8").useDelimiter("\\A").next();

		try {
			ValidateXSD v = new ValidateXSD();
			System.out.println("Profile");
			System.out.println(v.validateProfile(profile));
			System.out.println("Constraints");
			System.out.println(v.validateConstraints(constraints));
			System.out.println("ValueSet");
			System.out.println(v.validateVocabulary(valueSet));

		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
