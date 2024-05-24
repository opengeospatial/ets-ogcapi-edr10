package org.opengis.cite.ogcapiedr10.landingpage;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.SuiteAttribute;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.opengis.util.FactoryException;
import org.testng.ITestContext;
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

    private JsonPath jsonPath;


    /**
     * <pre>
     * Abstract Test 2: Validate that a landing page can be retrieved from the expected location.
     * Abstract Test 3: Validate that the landing page complies with the require structure and contents.
     * </pre>
     */
    @Test(description = "Implements Abstract Test 2 (/conf/core/root-op) and Abstract Test 3 (/conf/core/root-success) - Landing Page validation", groups = "landingpage")
    public void edrLandingPageValidation(ITestContext testContext) {
        
        jsonPath = (JsonPath) testContext.getSuite().getAttribute(SuiteAttribute.LANDINGPAGEJSONPATH.getName());
        
        List<Object> links = jsonPath.getList( "links" );
        Set<String> linkTypes = collectLinkTypes( links );
        boolean expectedLinkTypesExists = ( linkTypes.contains( "service-desc" )
                || linkTypes.contains( "service-doc" ) )
              && linkTypes.contains( "conformance" ) && linkTypes.contains( "data" );
        
        assertTrue( expectedLinkTypesExists,
                    "The landing page must include at least links with relation types ('service-desc' and/or 'service-doc' ) and 'data' and 'conformance', but contains "
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
