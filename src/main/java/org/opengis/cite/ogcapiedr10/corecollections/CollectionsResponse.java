package org.opengis.cite.ogcapiedr10.corecollections;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertFalse;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.GEOJSON_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.IUT;
import static org.opengis.cite.ogcapiedr10.openapi3.OpenApiUtils.retrieveParameterByName;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDate;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRange;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRangeWithDuration;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.parseTemporalExtent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.opengis.cite.ogcapiedr10.util.TemporalExtent;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.reprezen.kaizen.oasparser.model3.OpenApi3;
import com.reprezen.kaizen.oasparser.model3.Operation;
import com.reprezen.kaizen.oasparser.model3.Parameter;
import com.reprezen.kaizen.oasparser.model3.Path;
import com.reprezen.kaizen.oasparser.model3.Schema;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * /collections/{collectionId}/
 *
 */
public class CollectionsResponse extends CommonFixture {

	protected URI iut;
    
    @DataProvider(name = "collectionIDs")
    public Iterator<Object[]> collectionIDs( ITestContext testContext ) {

    
        this.iut = (URI) testContext.getSuite().getAttribute( IUT.getName() );
        
        
        Set<String> collectionTypes = extractCollectionIDs(this.iut);
        List<Object[]> collectionsData = new ArrayList<>();
       
        collectionsData.add(new Object[] {collectionTypes});
        
        return collectionsData.iterator();
        
    }    
	
    private Set<String> extractCollectionIDs(URI rootUri)
    {
    	JsonPath response;
        Response request = init().baseUri( rootUri.toString() ).accept( JSON ).when().request( GET, "/collections" );
        
        request.then().statusCode( 200 );
        response = request.jsonPath();
        List<Object> collections = response.getList( "collections" );
        Set<String> collectionTypes = new HashSet<>();
        for ( Object collection : collections ) {
            Map<String, Object> collectionMap = (Map<String, Object>) collection;
            Object collectionType = collectionMap.get( "id" );
            collectionTypes.add( (String) collectionType );
        }
        
        return collectionTypes;
    	
    }
    
    
    /*
     * Abstract Test 9 /conf/collections/rc-md-op 
     * Abstract Test 10 /conf/rc-md-success
     * Validate that information about the Collections can be retrieved from the expected location.
     * Validate that the Collections content complies with the required structure and contents.
     */
    
	@Test(description = "Implements Abstract Test 9 /conf/collections/rc-md-op and Abstract Test 10 /conf/rc-md-success", dataProvider = "collectionIDs", alwaysRun = true)
	public void validateRetrievalOfCollections(Object collectionIdentifiers) {

		Set<String> collectionTypes = (Set<String>) collectionIdentifiers;
	
		//The dataProvider validated the response code was 200
		//Therefore if the size of the collection is greater than zero, we know the iut passed the test
		
		assertTrue(collectionTypes.size()>0, "Was not able to validate that information about the Collections was retrieved correctly");

	}    
	
    /*
     * Abstract Test 11 /conf/collections/src-md-op 
     * Abstract Test 12 /conf/collections/src-md-success
     * Validate that the Collection content can be retrieved from the expected location.
     * Validate that the Collections content complies with the required structure and contents.
     */
    
	@Test(description = "Implements Abstract Test 11 /conf/collections/src-md-op and Abstract Test 12 /conf/src-md-success", dataProvider = "collectionIDs", alwaysRun = true)
	public void validateRetrievalOfEnvironmentalDataCollections(Object collectionIdentifiers) {

		Set<String> collectionTypes = (Set<String>) collectionIdentifiers;
			
		boolean identicalID = false;
		int i=0;
		for(String collection:collectionTypes)
		{
		
			if(i==0)  //We choose to test one
			{
				System.out.println(collection);
		    	JsonPath response;
		        Response request = init().baseUri( rootUri.toString() ).accept( JSON ).when().request( GET, "/collections/"+collection );
		        
		        request.then().statusCode( 200 );
		        response = request.jsonPath();
	    
		        if(response.getString("id").equals(collection)) identicalID = true;
	
				
			}
			i++;
		}
		
		assertTrue(identicalID, "The id of the collection returned by the response did not match the collectionID in the URL");

	} 	
	
        /**
         * Abstract Test 32 : Validate that an error is returned by a Position query if no query parameters are specified.
         * Abstract Test 48 : Validate that an error is returned by a Area query if no query parameters are specified.
         * Abstract Test 64 : Validate that an error is returned by a Trajectory query if no query parameters are specified.
         * Abstract Test 98 : Validate that an error is returned by a Locations query if no query parameters are specified.
         * 
         * @param collectionIdentifiers
         */
        @Test(dataProvider = "collectionIDs", description = "Implements Abstract Test 32 + 48 + 64 + 98, Requirements /req/queries/position + /req/queries/area + /req/queries/trajectory + /req/queries/locations")
        public void validateNoQueryParameters(Object collectionIdentifiers) {
            Set<String> collectionIds = (Set<String>) collectionIdentifiers;
            for (String colletionId : collectionIds) {
                String url = rootUri.toString() + "/collections/" + colletionId;
                Response response = init().baseUri(url).accept(JSON).when().request(GET, "/position");
                assertTrue(response.getStatusCode() == 400,
                        "Expected status code 400 when a Position query with no query parameters are specified for collection " + colletionId);

                response = init().baseUri(url).accept(JSON).when().request(GET, "/area");
                assertTrue(response.getStatusCode() == 400,
                        "Expected status code 400 when a Area query with no query parameters are specified for collection " + colletionId);

                response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory");
                assertTrue(response.getStatusCode() == 400,
                        "Expected status code 400 when a Trajectory query with no query parameters are specified for collection " + colletionId);

                response = init().baseUri(url).accept(JSON).when().request(GET, "/locations");
                assertTrue(response.getStatusCode() == 400,
                        "Expected status code 400 when a Locations query with no query parameters are specified for collection " + colletionId);
            }
        }

        /**
         * Abstract Test 33 : Validate that an error is returned by a Position query when the coords query parameter is not specified.
         * Abstract Test 49 : Validate that an error is returned by a Area query when the coords query parameter is not specified.
         * Abstract Test 65 : Validate that an error is returned by a Trajectory query when the coords query parameter is not specified.
         * 
         * @param collectionIdentifiers
         */
        @Test(dataProvider = "collectionIDs", description = "Implements Abstract Test 33 + 49 + 65, Requirements /req/queries/position + /req/queries/area + /req/queries/trajectory")
        public void validateCoordsQueryParameters(
                Object collectionIdentifiers) {
            Set<String> collectionIds = (Set<String>) collectionIdentifiers;
            for (String colletionId : collectionIds) {
                String url = rootUri.toString() + "/collections/" + colletionId;
                Response response = init().baseUri(url).accept(JSON).when().request(GET, "/position?coords=");
                assertTrue(response.getStatusCode() == 400,
                        "Expected status code 400 when a Position query with coords query parameter is not specified for collection " + colletionId);

                response = init().baseUri(url).accept(JSON).when().request(GET, "/area?coords=");
                assertTrue(response.getStatusCode() == 400,
                        "Expected status code 400 when a Area query with coords query parameter is not specified for collection " + colletionId);

                response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory?coords=");
                assertTrue(response.getStatusCode() == 400,
                        "Expected status code 400 when a Trajectory query with coords query parameter is not specified for collection " + colletionId);
            }
        }        
}
