package org.opengis.cite.ogcapiedr10.conformance;

import com.reprezen.kaizen.oasparser.OpenApiParser;
import com.reprezen.kaizen.oasparser.model3.OpenApi3;
import com.reprezen.kaizen.oasparser.val.ValidationResults;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.util.Link;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.OPEN_API_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.API_MODEL;

/**
 * A.2.3. API Definition Path {root}/api (link)
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class ApiDefinition extends CommonFixture {

    private String response;

    private Link apiUrl = null;

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
    @Test(description = "Implements Abstract Test 4 (/conf/core/api-definition)", groups = "apidefinition", dependsOnGroups = "landingpage")
    public void openapiDocumentRetrieval() {
    	
        if ( apiUrl == null || apiUrl.getHref().isEmpty() )
            throw new AssertionError( "Path to the API Definition could not be constructed from the landing page" );
        Response request = init().baseUri( apiUrl.getHref() ).accept( apiUrl.getType() ).when().request( GET );
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
    @Test(description = "Implements Abstract Test 5 (/conf/core/api-definition-success)", groups = "apidefinition", dependsOnMethods = "openapiDocumentRetrieval")
    public void apiDefinitionValidation( ITestContext testContext )
                            throws MalformedURLException {
    	
    
        OpenApiParser parser = new OpenApiParser();
 

        OpenApi3 apiModel = null;
   
        
        Response response = init().baseUri( apiUrl.getHref() ).accept( apiUrl.getType() ).when().request( GET );
        
        
        try {
            	
        	URL resolutionBase = new URL(apiUrl.getHref()); //https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/83c47220d21fe7569f46eeacd3f5bdecb58da69a/API-Overview.md#parsing-options
			apiModel = (OpenApi3) parser.parse(response.asString(), resolutionBase);
		} catch (Exception e) {
			
			e.printStackTrace();
			assertTrue( false, "The API definition linked from the Landing Page resulted in "+apiUrl+" \n"+e.getMessage() );
		}

        if(apiModel.isValid() )
        {
        	testContext.getSuite().setAttribute( API_MODEL.getName(), apiModel );
        }
        
        if(apiModel.isValid() 	&&	(!apiUrl.getType().equals(OPEN_API_MIME_TYPE)))
        {
        	throw new SkipException("The API Definition was found to be valid. However, the Media Type identified by the Link to the API Definition document was not "+OPEN_API_MIME_TYPE);
        }
   
        assertTrue( apiModel.isValid(), createValidationMsg( apiModel ) );
 
    }

    private Link parseApiUrl( JsonPath jsonPath ) {
    	
    	
    	
        for ( Object link : jsonPath.getList( "links" ) ) {
            Map<String, Object> linkMap = (Map<String, Object>) link;
            Object rel = linkMap.get( "rel" );
            Object type = linkMap.get( "type" );
            if ("service-desc".equals( rel ))  //Check service-desc first
            {
            	return new Link((String) linkMap.get( "href" ),
            			(String)rel,
            			(String)type);
            	
            	  
            }
            else if ("service-doc".equals( rel )) 
            {

            	return new Link((String) linkMap.get( "href" ),
            			(String)rel,
            			(String)type);
            	  
            }
        }
        return null;
    }

    private String createValidationMsg( OpenApi3 model ) {
        StringBuilder sb = new StringBuilder();
        sb.append( "API definition is not valid. Found following validation items:" );
        if ( !model.isValid() ) {
            for ( ValidationResults.ValidationItem item : model.getValidationItems() ) {            	
            	sb.append( "  @ " ).append( item.getPositionInfo() ).append( "  - " ).append( item.getSeverity() ).append( ": " ).append( item.getMsg() );

            }
        }
        return sb.toString();
    }

}