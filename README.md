# hl7v2-resources-validation

Validates HL7 V2 Resources (Profiles, Conformance Contexts, ValueSets) against xsd.

### Maven Dependency
```xml
	<dependency>
		<groupId>gov.nist.healthcare.resources</groupId>
		<artifactId>validation</artifactId>
		<version>1.0.1</version>
	</dependency>
```
### XSD Folder Format 

path_to_folder/ <br>
├── Commons.xsd <br>
├── ConformanceContext.xsd <br>
├── Expressions.xsd <br>
├── Profile.xsd <br>
└── ValueSets.xsd <br>

### Create a xml validator instance

Use factory method to create a new instance, path_to_folder is the path to the folder containing xsd files from classloader root.

```java 
XMLResourcesValidator validator = XMLResourcesValidator.createValidatorFromClasspath(path_to_folder);
```

## Validation Methods

1) Validate an XML Profile
```java 
public List<XMLError> validateProfile(InputStream profile) throws IOException;
```
2) Validate an XML ValueSetLibrary 
```java 
public List<XMLError> validateVocabulary(InputStream valueSetsLibrary) throws IOException;
```
3) Validate an XML Constraints 
```java 
public List<XMLError> validateConstraints(InputStream constraints) throws IOException;
```

## XMLError Model
```java
public class XMLError {

	//-- Getters
	public int getLine();
	public int getColumn();
	public String getMessage();
	
	//-- Setters
	public void setLine(int line);
	public void setColumn(int column);
	public void setMessage(String message);	
	
}
```
