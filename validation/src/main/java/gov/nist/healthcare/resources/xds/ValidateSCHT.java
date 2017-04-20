package gov.nist.healthcare.resources.xds;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Scanner;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.oclc.purl.dsdl.svrl.FailedAssert;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.xslt.SchematronResourceSCH;

public class ValidateSCHT {
	public static void main(String[] args) throws Exception {
		URL xmlURL = ValidateSCHT.class.getResource("/scht/VXU-Z22_Profile.xml");
		URL schURL = ValidateSCHT.class.getResource("/scht/profile.sch");

		System.out.println(validateXMLViaXSLTSchematron(new File(schURL.toURI()), new File(xmlURL.toURI())));
	}

	public static boolean validateXMLViaXSLTSchematron (final File aSchematronFile,final File aXMLFile) throws Exception
	{
	  final ISchematronResource aResSCH = SchematronResourceSCH.fromFile (aSchematronFile);
	  if (!aResSCH.isValidSchematron ())
	    throw new IllegalArgumentException ("Invalid Schematron!");
	  SchematronOutputType out = aResSCH.applySchematronValidationToSVRL(new StreamSource(aXMLFile));
//	  System.out.println(out);
//	  System.out.println(out.getText());
	  int i = out.getActivePatternAndFiredRuleAndFailedAssertCount();
	  for(int j = 0; j < i; j++){
		  if(out.getActivePatternAndFiredRuleAndFailedAssertAtIndex(j) instanceof FailedAssert){
			  FailedAssert fa = (FailedAssert) out.getActivePatternAndFiredRuleAndFailedAssertAtIndex(j);
			  System.out.println(fa);
			  System.out.println(fa.getDiagnosticReference());
		  }
	  }
	  Document d = aResSCH.applySchematronValidation(new StreamSource(aXMLFile));
	  System.out.println(d);
	  return false;
	}

}
