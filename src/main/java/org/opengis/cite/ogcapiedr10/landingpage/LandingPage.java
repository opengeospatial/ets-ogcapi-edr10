package org.opengis.cite.ogcapiedr10.landingpage;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * Updated at the OGC API - EDR Sprint 2020 by ghobona
 *
 * A.2.2. Landing Page {root}/
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class LandingPage extends CommonFixture {

    private JsonPath response;




    /**
     * <pre>
     * Abstract Test 2: Validate that a landing page can be retrieved from the expected location.
     * Abstract Test 3: Validate that the landing page complies with the require structure and contents.
     * Requirement 1: The API implementation SHALL demonstrate conformance with the following Requirements Classes of the OGC API-Common version 1.0 Standard.
     * </pre>
     */
    @Test(description = "Implements Abstract Test 2 and Abstract Tes 3 - Landing Page and part of Requirement 1 (/req/core/api-common)", groups = "landingpage")
    public void edrLandingPageValidation() {

    	String f = "";
    	
    	if(rootUri.toString().contains("f=json") || rootUri.toString().contains("f=application/json")) {}
    	else { f = "f=application/json"; }
    	
        Response request = init().baseUri( rootUri.toString() ).accept( JSON ).when().request( GET, "?"+f );
    
        request.then().statusCode( 200 );
        response = request.jsonPath();
        List<Object> links = response.getList( "links" );
        Set<String> linkTypes = collectLinkTypes( links );
        boolean expectedLinkTypesExists = ( linkTypes.contains( "service-desc" )
                || linkTypes.contains( "service-doc" ) )
              && linkTypes.contains( "conformance" ) && linkTypes.contains( "data" );
        
        assertTrue( expectedLinkTypesExists,
                    "The landing page must include at least links with relation type 'conformance', but contains "
                                             + String.join( ", ", linkTypes ) );
    }

    private Set<String> collectLinkTypes( List<Object> links ) {
        Set<String> linkTypes = new HashSet<>();
        for ( Object link : links ) {
            Map<String, Object> linkMap = (Map<String, Object>) link;
            Object linkType = linkMap.get( "rel" );
            linkTypes.add( (String) linkType );
        }
        return linkTypes;
    }

}
