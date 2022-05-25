package org.opengis.cite.ogcapiedr10;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Verifies the results of executing a test run using the main controller
 * (TestNGController).
 */
public class VerifyTestNGController {

    private static DocumentBuilder docBuilder;
    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testValidateTestRunArgs() throws Exception {  
    	// Integration Test
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	DocumentBuilder db = dbf.newDocumentBuilder();
        Document testRunProps = db.parse(this.getClass().getResourceAsStream("/test-run-props.xml"));
        TestNGController controller = new TestNGController(System.getProperty("java.io.tmpdir"));
        Source testResults = controller.doTestRun(testRunProps);
        System.out.println("Test results: " + testResults.getSystemId());
    }
}