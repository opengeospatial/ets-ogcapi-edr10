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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.sis.referencing.CRS;
import org.apache.sis.referencing.crs.DefaultGeographicCRS;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.opengis.cite.ogcapiedr10.util.TemporalExtent;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
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
    
	@Test(description = "Implements Abstract Test 9 (/conf/collections/rc-md-op) and Abstract Test 10 (/conf/rc-md-success)", dataProvider = "collectionIDs", alwaysRun = true)
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
    
	@Test(description = "Implements Abstract Test 11 (/conf/collections/src-md-op) and Abstract Test 12 (/conf/collections/src-md-success)", dataProvider = "collectionIDs", alwaysRun = true)
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
         * Abstract Test 34 : Validate that an error is returned by a Position query if no query parameters are specified.
         * Abstract Test 50 : Validate that an error is returned by a Area query if no query parameters are specified.
         * Abstract Test 66 : Validate that an error is returned by a Cube query if no query parameters are specified.         
         * Abstract Test 82 : Validate that an error is returned by a Trajectory query if no query parameters are specified.
         * Abstract Test 100 : Validate that an error is returned by a Corridor query if no query parameters are specified.
         * Abstract Test 136 : Validate that an error is returned by a Locations query if no query parameters are specified.          
         * 
         * @param collectionIdentifiers
         */
        @Test(dataProvider = "collectionIDs", description = "Implements Abstract Test 34 (/conf/position), Abstract Test 50 (/conf/area), Abstract Test 66 (/conf/cube), Abstract Test 82 (/conf/trajectory), Abstract Test 100 (/conf/corridor), Abstract Test 136 (/conf/locations) ")
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
         * Abstract Test 35 : Validate that an error is returned by a Position query when the coords query parameter is not specified.
         * Abstract Test 51 : Validate that an error is returned by a Area query when the coords query parameter is not specified.
         * Abstract Test 83 : Validate that an error is returned by a Trajectory query when the coords query parameter is not specified.
         * Abstract Test 101 : Validate that an error is returned by a Corridor query when the coords query parameter is not specified.
         * 
         * @param collectionIdentifiers
         */
        @Test(dataProvider = "collectionIDs", description = "Implements Abstract Test 35 (/conf/position), Abstract Test 51 (/conf/area), Abstract Test 83 (/conf/trajectory), Abstract Test 101 (/conf/corridor)")
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
                
                
                response = init().baseUri(url).accept(JSON).when().request(GET, "/corridor?coords=");
                assertTrue(response.getStatusCode() == 400,
                        "Expected status code 400 when a Corridor query with coords query parameter is not specified for collection " + colletionId);                
                
            }
        }
        

    	/**
    	 * <pre>
    	 * Abstract Test 8: Validate that all spatial geometries provided through the API are in the CRS84 spatial reference system unless otherwise requested by the client.
    	 * </pre>
    	 *
    	 */
    	@Test(description = "Implements Abstract Test 8 (/conf/core/crs84)")
    	public void collectionsCRS84() {

    		boolean compliesWithCRS84Requirement = true;
    		StringBuffer resultMessage = new StringBuffer();

    		TestPoint testPoint = new TestPoint(rootUri.toString(), "/collections", null);
    		String testPointUri = new UriBuilder(testPoint).buildUrl();
    		Response response = init().baseUri(testPointUri).accept(JSON).when().request(GET);
    		JsonPath jsonPath = response.jsonPath();

    		List<Object> collectionsList = jsonPath.getList("collections");

    		for (int t = 0; t < collectionsList.size(); t++) {
    			boolean supportsCRS84 = false;
    			HashMap collectionMap = (HashMap) collectionsList.get(t);

    			String crsText = collectionMap.get("crs").toString();
    			ArrayList crsList = (ArrayList) collectionMap.get("crs");
    			HashMap crsMap = (HashMap) crsList.get(0);

    			CoordinateReferenceSystem source = null;

    			try {

    				source = CRS.fromWKT(crsMap.get("wkt").toString());
    			} catch (Exception e) {

    				e.printStackTrace();
    			}

    			DefaultGeographicCRS crs = (DefaultGeographicCRS) source;

    			if (crs.getDatum().getEllipsoid().getName().toString().equals("WGS 84")
    					|| crs.getDatum().getEllipsoid().getName().toString().equals("WGS_1984")
    					|| crs.getDatum().getEllipsoid().getName().toString().equals("WGS84")) {

    				if (source.getCoordinateSystem().getAxis(0).toString().toLowerCase().contains("longitude")
    						&& source.getCoordinateSystem().getAxis(1).toString().toLowerCase().contains("latitude")) {

    					supportsCRS84 = true;

    				}
    			}
    			if (supportsCRS84 == false) {
    				compliesWithCRS84Requirement = false;
    				resultMessage.append("Collection " + collectionMap.get("id").toString() + " fails. ");
    			}

    		}

    		org.testng.Assert.assertTrue(compliesWithCRS84Requirement,
    				"Fails Abstract Test 8 because " + resultMessage.toString());

    	}

    	/**
    	 * <pre>
    	 * Abstract Test 17: Validate that each parameter in a collection is correctly defined.
    	 * </pre>
    	 *
    	 */
    	@Test(description = "Implements Abstract Test 17 (/conf/edr/rc-parameters)")
    	public void collectionsParameters() {
    		boolean compliesWithCollectionParametersRequirement = true;
    		StringBuffer resultMessage = new StringBuffer();

    		TestPoint testPoint = new TestPoint(rootUri.toString(), "/collections", null);
    		String testPointUri = new UriBuilder(testPoint).buildUrl();
    		Response response = init().baseUri(testPointUri).accept(JSON).when().request(GET);
    		JsonPath jsonPath = response.jsonPath();

    		List<Object> collectionsList = jsonPath.getList("collections");
    		
    		

    		for (int t = 0; t < collectionsList.size(); t++) {
    			boolean supportsCRS84 = false;
    			HashMap collectionMap = (HashMap) collectionsList.get(t);

    			String parameterNameText = collectionMap.get("parameter_names").toString();
    			HashMap parameterNameMap = (HashMap) collectionMap.get("parameter_names");
    			
    			
    			ArrayList<String> parameterNameList = new ArrayList<String>();
    			
    			//Implements Test Method 2: Verify that each parameter property has a unique name (in the collection).
    			for (Object k: parameterNameMap.keySet())
    			{
    				if(parameterNameList.contains(k.toString())) {
    					org.testng.Assert.fail(	"Fails Abstract Test 17 because the parameter " + k.toString()+" is duplicated in collection "+collectionMap.get("id"));		
    				}
    				else {
    					parameterNameList.add(k.toString());
    				}
    			}
    			
    			//Implements Test Method 1: Verify that all parameters listed in a collection have the required properties.
    			//Implements Test Method 3: Verify that each parameter property has a type property.
    			//Implements Test Method 4: Verify that each parameter property has a observedProperty property.
    			for(int i=0; i < parameterNameList.size(); i++)
    			{
    				String parameterName = parameterNameList.get(i);
    		
    				
    				boolean hasType = false; 
    				boolean hasObservedProperty = false;
    				
    				HashMap parameterValueMap = (HashMap) parameterNameMap.get(parameterName);
    				for (Object prop: parameterValueMap.keySet())
    				{
    					if(prop.toString().equals("type")) hasType = true;
    					if(prop.toString().equals("observedProperty")) hasObservedProperty = true;
    					
    				}
    				hasType = false;
    				
    				org.testng.Assert.assertTrue(hasType,	"Fails Abstract Test 17 because parameter " + parameterName+" in collection "+collectionMap.get("id") + " is missing a 'type' property");
    				org.testng.Assert.assertTrue(hasObservedProperty,	"Fails Abstract Test 17 because parameter " + parameterName+" in collection "+collectionMap.get("id") + " is missing a 'observedProperty' property");				
    			}
    			

    			org.testng.Assert.assertTrue(compliesWithCollectionParametersRequirement,	"Fails Abstract Test 17 because " + resultMessage.toString());
    		}
    	}
        
        
        
}
