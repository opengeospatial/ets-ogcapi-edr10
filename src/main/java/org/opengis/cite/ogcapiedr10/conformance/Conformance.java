package org.opengis.cite.ogcapiedr10.conformance;

import com.reprezen.kaizen.oasparser.model3.OpenApi3;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.hamcrest.CoreMatchers.containsString;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.*;
import static org.opengis.cite.ogcapiedr10.conformance.RequirementClass.CORE;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Conformance Path {root}/conformance
 *
 *
 */
public class Conformance extends CommonFixture {

    private List<RequirementClass> requirementClasses;

    @DataProvider(name = "conformanceUris")
    public Object[][] conformanceUris( ITestContext testContext ) {
        OpenApi3 apiModel = (OpenApi3) testContext.getSuite().getAttribute( API_MODEL.getName() );
        URI iut = (URI) testContext.getSuite().getAttribute( IUT.getName() );
        
        //https://github.com/opengeospatial/ets-ogcapi-edr10/issues/110
        //check for trailing forward slash
        String serverUrl = rootUri.toString();
        serverUrl = serverUrl.endsWith("/") ? serverUrl.substring(0, serverUrl.length() - 1) : serverUrl;
        
        TestPoint tp = new TestPoint(serverUrl, "/conformance" ,null);


        List<TestPoint> testPoints = new ArrayList<TestPoint>();
        testPoints.add(tp);
        Object[][] testPointsData = new Object[1][];
        int i = 0;
        for ( TestPoint testPoint : testPoints ) {
            testPointsData[i++] = new Object[] { testPoint };
        }
        return testPointsData;
    }

    @AfterClass
    public void storeRequirementClassesInTestContext( ITestContext testContext ) {
        testContext.getSuite().setAttribute( REQUIREMENTCLASSES.getName(), this.requirementClasses );
    }
    
    /**
     * <pre>
     * Abstract Test 1: Validate that the resource paths advertised through the API conform with HTTP 1.1 and, where approprate, TLS.
     * </pre>
     */
    @Test(description = "Implements Abstract Test 1 and Requirement /req/core/http")
    public void http() {
        Response response = init().baseUri( rootUri.toString() ).when().request( GET, "/" );
        response.then().statusLine( containsString( "HTTP/1.1" ) );
    }    

    /**
     * Abstract Test 6: Validate that a Conformance Declaration can be retrieved from the expected location.
     * Abstract Test 7: Validate that the Conformance Declaration response complies with the required structure and contents.
     *
     * @param testPoint
     *            the test point to test, never <code>null</code>
     */
    @Test(description = "Implements Abstract Test 6 (/conf/core/conformance) and Abstract Test 7 (/conf/core/conformance-success)", groups = "conformance", dataProvider = "conformanceUris")
    public void validateConformanceOperationAndResponse( TestPoint testPoint ) {

    	String f = "";
    	if(rootUri.toString().contains("f=json") || rootUri.toString().contains("f=application/json")) {}
    	else { 
    		//f = "f=application/json&f=json"; 
    		 f = "f=json"; 
    		}    	
    	
        String testPointUri = new UriBuilder( testPoint ).buildUrl();
   
        Response response = init().baseUri( testPointUri ).accept( JSON ).when().request( GET ,"?"+f);
        validateConformanceOperationResponse( testPointUri, response );
    }

    /**
     * private method to support Abstract Test 6 and Abstract Test 7
     *
     */
    private void validateConformanceOperationResponse( String testPointUri, Response response ) {
        response.then().statusCode( 200 );

        JsonPath jsonPath = response.jsonPath();
        this.requirementClasses = parseAndValidateRequirementClasses( jsonPath );
        assertTrue( this.requirementClasses.contains( CORE ),
                    "Requirement class \"http://www.opengis.net/spec/ogcapi-edr-1/1.0/conf/core\" is not available from path "
                                                              + testPointUri );
    }

    /**
     * @param jsonPath
     *            never <code>null</code>
     * @return the parsed requirement classes, never <code>null</code>
     * @throws AssertionError
     *             if the json does not follow the expected structure
     */
    List<RequirementClass> parseAndValidateRequirementClasses( JsonPath jsonPath ) {
        List<Object> conformsTo = jsonPath.getList( "conformsTo" );
        assertNotNull( conformsTo, "Missing member 'conformsTo'." );

        List<RequirementClass> requirementClasses = new ArrayList<>();
        for ( Object conformTo : conformsTo ) {
   
            if ( conformTo instanceof String ) {
                String conformanceClass = (String) conformTo;
                RequirementClass requirementClass = RequirementClass.byConformanceClass( conformanceClass );
                if ( requirementClass != null )
                    requirementClasses.add( requirementClass );
            } else
                throw new AssertionError( "At least one element array 'conformsTo' is not a string value (" + conformTo
                                          + ")" );
        }
        return requirementClasses;
    }

    
    
    
}
