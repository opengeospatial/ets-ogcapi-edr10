package org.opengis.cite.ogcapiedr10;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.API_MODEL;
import static io.restassured.RestAssured.given;


import java.io.File;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;

import org.opengis.cite.ogcapiedr10.openapi3.OpenApiUtils;
import org.opengis.cite.ogcapiedr10.util.ClientUtils;
import org.opengis.cite.ogcapiedr10.util.Link;
import org.opengis.cite.ogcapiedr10.util.TestSuiteLogger;
import org.opengis.cite.ogcapiedr10.util.URIUtils;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.reprezen.kaizen.oasparser.OpenApiParser;
import com.reprezen.kaizen.oasparser.model3.OpenApi3;
import com.sun.jersey.api.client.Client;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * A listener that performs various tasks before and after a test suite is run, usually concerned with maintaining a
 * shared test suite fixture. Since this listener is loaded using the ServiceLoader mechanism, its methods will be
 * called before those of other suite listeners listed in the test suite definition and before any annotated
 * configuration methods.
 *
 * Attributes set on an ISuite instance are not inherited by constituent test group contexts (ITestContext). However,
 * suite attributes are still accessible from lower contexts.
 *
 * @see org.testng.ISuite ISuite interface
 */
public class SuiteFixtureListener implements ISuiteListener {

    @Override
    public void onStart( ISuite suite ) {
        processSuiteParameters( suite );
        registerClientComponent( suite );
    }

    @Override
    public void onFinish( ISuite suite ) {
        if ( null != System.getProperty( "deleteSubjectOnFinish" ) ) {
            deleteTempFiles( suite );
            System.getProperties().remove( "deleteSubjectOnFinish" );
        }
    }

    /**
     * Processes test suite arguments and sets suite attributes accordingly. The entity referenced by the
     * {@link TestRunArg#IUT iut} argument is retrieved and written to a File that is set as the value of the suite
     * attribute {@link SuiteAttribute#TEST_SUBJ_FILE testSubjectFile}.
     *
     * @param suite
     *            An ISuite object representing a TestNG test suite.
     */
    void processSuiteParameters( ISuite suite ) {
        Map<String, String> params = suite.getXmlSuite().getParameters();
        TestSuiteLogger.log( Level.CONFIG, "Suite parameters\n" + params.toString() );
        String iutParam = params.get( TestRunArg.IUT.toString() );
        if ( ( null == iutParam ) || iutParam.isEmpty() ) {
            throw new IllegalArgumentException( "Required test run parameter not found: " + TestRunArg.IUT.toString() );
        }
        URI iutRef = URI.create( iutParam.trim() );
        suite.setAttribute( SuiteAttribute.IUT.getName(), iutRef );
        File entityFile = null;
        try {
            entityFile = URIUtils.dereferenceURI( iutRef );
        } catch ( IOException iox ) {
            throw new RuntimeException( "Failed to dereference resource located at " + iutRef, iox );
        }
        TestSuiteLogger.log( Level.FINE, String.format( "Wrote test subject to file: %s (%d bytes)",
                                                        entityFile.getAbsolutePath(), entityFile.length() ) );
        suite.setAttribute( SuiteAttribute.TEST_SUBJ_FILE.getName(), entityFile );

        String noOfCollections = params.get( TestRunArg.NOOFCOLLECTIONS.toString() );
        try {
            if ( noOfCollections != null ) {
                int noOfCollectionsInt = Integer.parseInt( noOfCollections );
                suite.setAttribute( SuiteAttribute.NO_OF_COLLECTIONS.getName(), noOfCollectionsInt );
            }
        } catch ( NumberFormatException e ) {
            TestSuiteLogger.log( Level.WARNING,
                                 String.format( "Could not parse parameter %s: %s. Expected is a valid integer",
                                                TestRunArg.NOOFCOLLECTIONS.toString(), noOfCollections ) );
        }
        
   
        
        URI apiDefinitionLocation = URI.create( params.get( "apiDefinition" ));
        suite.setAttribute( SuiteAttribute.API_DEFINITION.getName(), apiDefinitionLocation );
        File apiDefinitionFile = null;
        try {
        	apiDefinitionFile = URIUtils.dereferenceURI( apiDefinitionLocation );
        } catch ( IOException iox ) {
            throw new RuntimeException( "Failed to dereference resource located at " + apiDefinitionLocation, iox );
        }      

        String f = "";
        
        if(iutRef.toString().contains("f=json") || iutRef.toString().contains("f=application/json")) {}
        else { f = "f=application/json"; }
        
        Response response = null;
        
        if(ClientUtils.is200Response(URI.create(iutRef.toString()+"?f=application/json")))
        {
                response = given().baseUri( iutRef.toString() ).accept( JSON ).when().request( GET, "?f=application/json");
                response.then().statusCode( 200 );
        }
        else if(ClientUtils.is200Response(URI.create(iutRef.toString()+"?f=json")))
        {
                response = given().baseUri( iutRef.toString() ).accept( JSON ).when().request( GET, "?f=json");
                response.then().statusCode( 200 );              
        }    
        JsonPath jsonPath = response.jsonPath();
        suite.setAttribute( SuiteAttribute.LANDINGPAGEJSONPATH.getName(), jsonPath );
        
        OpenApiParser parser = new OpenApiParser();
        OpenApi3 apiModel = null;
        
        try {
            Link apiUrl = OpenApiUtils.parseApiUrl(jsonPath);
            URL resolutionBase = new URL(apiUrl.getHref()); // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/83c47220d21fe7569f46eeacd3f5bdecb58da69a/API-Overview.md#parsing-options
            Response apiModelResponse = given().accept(apiUrl.getType()).get(apiUrl.getHref());
            apiModel = (OpenApi3) parser.parse(apiModelResponse.asString(), resolutionBase);
        } catch (Exception e) {
            TestSuiteLogger.log(Level.SEVERE, "The API definition linked from the Landing Page resulted in an exception.", e);
        }

        suite.setAttribute(API_MODEL.getName(), apiModel);
        
    }

    /**
     * A client component is added to the suite fixture as the value of the {@link SuiteAttribute#CLIENT} attribute; it
     * may be subsequently accessed via the {@link org.testng.ITestContext#getSuite()} method.
     *
     * @param suite
     *            The test suite instance.
     */
    void registerClientComponent( ISuite suite ) {
        Client client = ClientUtils.buildClient();
        if ( null != client ) {
            suite.setAttribute( SuiteAttribute.CLIENT.getName(), client );
        }
    }

    /**
     * Deletes temporary files created during the test run if TestSuiteLogger is enabled at the INFO level or higher
     * (they are left intact at the CONFIG level or lower).
     *
     * @param suite
     *            The test suite.
     */
    void deleteTempFiles( ISuite suite ) {
        if ( TestSuiteLogger.isLoggable( Level.CONFIG ) ) {
            return;
        }
        File testSubjFile = (File) suite.getAttribute( SuiteAttribute.TEST_SUBJ_FILE.getName() );
        if ( testSubjFile.exists() ) {
            testSubjFile.delete();
        }
    }
}
