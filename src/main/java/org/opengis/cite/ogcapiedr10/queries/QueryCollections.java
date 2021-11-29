package org.opengis.cite.ogcapiedr10.queries;

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
public class QueryCollections extends CommonFixture {
	
	boolean disable = true; //TODO Remove

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
        	
        	if (disable) { throw new SkipException("Test has been Disabled");}   //TODO REMOVE
        	
            Set<String> collectionIds = (Set<String>) collectionIdentifiers;
            for (String colletionId : collectionIds) {
                String url = rootUri.toString() + "/collections/" + colletionId;
                Response response = init().baseUri(url).accept(JSON).when().request(GET, "/position");
                assertTrue(response.getStatusCode() == 400,
                        "Fails Abstract Test 34. Expected status code 400 when a Position query with no query parameters are specified for collection " + colletionId);

   
                
                response = init().baseUri(url).accept(JSON).when().request(GET, "/area");
                assertTrue(response.getStatusCode() == 400,
                        "Fails Abstract Test 50. Expected status code 400 when a Area query with no query parameters are specified for collection " + colletionId);

                response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory");
                assertTrue(response.getStatusCode() == 400,
                        "Fails Abstract Test 82. Expected status code 400 when a Trajectory query with no query parameters are specified for collection " + colletionId);

                response = init().baseUri(url).accept(JSON).when().request(GET, "/locations");
                assertTrue(response.getStatusCode() == 400,
                        "Fails Abstract Test 136. Expected status code 400 when a Locations query with no query parameters are specified for collection " + colletionId);
            }
        }

        /**
         * Abstract Test 35 : Validate that an error is returned by a Position query when the coords query parameter is not specified.
         * Abstract Test 36 : Validate that an error is returned by a Position query when the coords query parameter does not contain a valid POINT Well Known Text value.
         * Abstract Test 51 : Validate that an error is returned by a Area query when the coords query parameter is not specified.
         * Abstract Test 83 : Validate that an error is returned by a Trajectory query when the coords query parameter is not specified.
         * Abstract Test 101 : Validate that an error is returned by a Corridor query when the coords query parameter is not specified.
         *
         * @param collectionIdentifiers
         */
        @Test(dataProvider = "collectionIDs", description = "Implements Abstract Test 35 (/conf/position),Abstract Test 36 (/conf/position), Abstract Test 51 (/conf/area), Abstract Test 83 (/conf/trajectory), Abstract Test 101 (/conf/corridor)")
        public void validateCoordsQueryParameters(Object collectionIdentifiers) {
        	
        	if (disable) { throw new SkipException("Test has been Disabled");}   //TODO REMOVE
        	
            Set<String> collectionIds = (Set<String>) collectionIdentifiers;
            for (String colletionId : collectionIds) {
                String url = rootUri.toString() + "/collections/" + colletionId;
                Response response = init().baseUri(url).accept(JSON).when().request(GET, "/position?coords=");
                assertTrue(response.getStatusCode() == 400,
                        "Fails Abstract Test 35. Expected status code 400 when a Position query with coords query parameter is not specified for collection " + colletionId);

                response = init().baseUri(url).accept(JSON).when().request(GET, "/position?coords=POINT()");
                assertTrue(response.getStatusCode() == 400,
                        "Fails Abstract Test 36. Expected status code 400 when a Position coords query parameter does not contain a valid POINT Well Known Text value for collection " + colletionId);
                             
                
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


}
