package org.opengis.cite.ogcapiedr10.conformance;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.OPEN_API_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.API_MODEL;
import com.reprezen.kaizen.oasparser.OpenApiParser;
import com.reprezen.kaizen.oasparser.model3.OpenApi3;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reprezen.kaizen.oasparser.OpenApi3Parser;

import com.reprezen.kaizen.oasparser.val.ValidationResults;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * A.2.3. API Definition Path {root}/api (link)
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class ApiDefinition extends CommonFixture {

    private String response;

    private String apiUrl;

    @BeforeClass(dependsOnMethods = "initCommonFixture")
    public void retrieveApiUrl() {
        Response request = init().baseUri( rootUri.toString() ).accept( JSON ).when().request( GET );
        JsonPath jsonPath = request.jsonPath();

        this.apiUrl = parseApiUrl( jsonPath );
    }

    /**
     * <pre>
     * Abstract Test 4: Test Purpose: Validate that the API Definition document can be retrieved from the expected location.
     * </pre>
     */
    @Test(description = "Implements Abstract Test 4 and /req/core/api-definition-op", groups = "apidefinition", dependsOnGroups = "landingpage")
    public void openapiDocumentRetrieval() {
   
        if ( apiUrl == null || apiUrl.isEmpty() )
            throw new AssertionError( "Path to the API Definition could not be constructed from the landing page" );
        Response request = init().baseUri( apiUrl ).accept( OPEN_API_MIME_TYPE ).when().request( GET );
        request.then().statusCode( 200 );
        response = request.asString();
    }

    /**
     * <pre>
     * Abstract Test 5: Validate that the API Definition complies with the required structure and contents.
     * </pre>
     *
     * @param testContext
     *            never <code>null</code>
     * @throws MalformedURLException
     *             if the apiUrl is malformed
     */
    @Test(description = "Implements Abstract Test 5 and Requirement /req/core/api-definition-success", groups = "apidefinition", dependsOnMethods = "openapiDocumentRetrieval")
    public void apiDefinitionValidation( ITestContext testContext )
                            throws MalformedURLException {
    	
    
        OpenApiParser parser = new OpenApiParser();
 

        OpenApi3 apiModel = null;
        
        //OpenApi3 apiModel = parser.parse( response, new URL( apiUrl ), true );
     
        try {
			apiModel = (OpenApi3) parser.parse(new URL( apiUrl ).toURI(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
   
        assertTrue( apiModel.isValid(), createValidationMsg( apiModel ) );
 
        testContext.getSuite().setAttribute( API_MODEL.getName(), apiModel );
    }

    private String parseApiUrl( JsonPath jsonPath ) {
    	
        for ( Object link : jsonPath.getList( "links" ) ) {
            Map<String, Object> linkMap = (Map<String, Object>) link;
            Object rel = linkMap.get( "rel" );
            Object type = linkMap.get( "type" );
            if ( ("service-doc".equals( rel ) || "service-desc".equals( rel ))  )  //TODO should only be service-desc
            {

            	//if(OPEN_API_MIME_TYPE.equals( type ))    //TODO should be enabled
            	  {

            		return (String) linkMap.get( "href" );
            	  }
            }
        }
        return null;
    }

    private String createValidationMsg( OpenApi3 model ) {
        StringBuilder sb = new StringBuilder();
        sb.append( "Landing Page is not valid. Found following validation items:" );
        if ( !model.isValid() ) {
            for ( ValidationResults.ValidationItem item : model.getValidationItems() ) {
                sb.append( "  - " ).append( item.getSeverity() ).append( ": " ).append( item.getMsg() );

            }
        }
        return sb.toString();
    }

}