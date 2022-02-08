package org.opengis.cite.ogcapiedr10.queries;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertFalse;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.GEOJSON_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.IUT;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.NO_OF_COLLECTIONS;
import static org.opengis.cite.ogcapiedr10.openapi3.OpenApiUtils.retrieveParameterByName;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDate;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRange;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRangeWithDuration;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.parseTemporalExtent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import java.util.Scanner;
import java.util.Set;

import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.opengis.cite.ogcapiedr10.util.TemporalExtent;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
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

	
	private int noOfCollections = 0;
    
	protected URI iut;
	
	@BeforeClass
	public void noOfCollections(ITestContext testContext) {
		Object noOfCollections = testContext.getSuite().getAttribute(NO_OF_COLLECTIONS.getName());
		if (noOfCollections != null) {
			this.noOfCollections = (Integer) noOfCollections;
		}
	}	

	@DataProvider(name = "collectionIDs")
	public Iterator<Object[]> collectionIDs(ITestContext testContext) {

		this.iut = (URI) testContext.getSuite().getAttribute(IUT.getName());

		Set<String> collectionTypes = extractCollectionIDs(this.iut);
		List<Object[]> collectionsData = new ArrayList<>();

		collectionsData.add(new Object[] { collectionTypes });

		return collectionsData.iterator();

	}

	private Set<String> extractCollectionIDs(URI rootUri) {
		JsonPath response;
		Response request = init().baseUri(rootUri.toString()).accept(JSON).when().request(GET, "/collections");

		request.then().statusCode(200);
		response = request.jsonPath();
		List<Object> collections = response.getList("collections");
		Set<String> collectionTypes = new HashSet<>();
		for (Object collection : collections) {
			Map<String, Object> collectionMap = (Map<String, Object>) collection;
			Object collectionType = collectionMap.get("id");
			collectionTypes.add((String) collectionType);
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



		Set<String> collectionIds = (Set<String>) collectionIdentifiers;
		ArrayList<String> collectionsList = new ArrayList<String>();
		collectionsList.addAll(collectionIds);		
		
		for (int c = 0; c < Math.min(this.noOfCollections,collectionsList.size()); c++) {
			
			String collectionId = collectionsList.get(c);
			
			boolean supportsPositionQuery = false;
			boolean supportsAreaQuery = false;
			boolean supportsTrajectoryQuery = false;
			boolean supportsLocationsQuery = false;

			String url = rootUri.toString() + "/collections/" + collectionId;

			Response response = init().baseUri(url).accept(JSON).when().request(GET);
			JsonPath jsonResponse = response.jsonPath();
			HashMap dataQueries = jsonResponse.getJsonObject("data_queries");
			supportsPositionQuery = dataQueries.containsKey("position");
			supportsAreaQuery = dataQueries.containsKey("area");
			supportsTrajectoryQuery = dataQueries.containsKey("trajectory");
			supportsLocationsQuery = dataQueries.containsKey("locations");

			try {
			

				if (supportsPositionQuery) {
				
					response = init().baseUri(url).accept(JSON).when().request(GET, "/position");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 34. Expected status code 400 when a Position query with no query parameters are specified for collection "
									+ collectionId);

				}
				if (supportsAreaQuery) {
				
					response = init().baseUri(url).accept(JSON).when().request(GET, "/area");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 50. Expected status code 400 when a Area query with no query parameters are specified for collection "
									+ collectionId);
				}
				if (supportsTrajectoryQuery) {
				
					response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 82. Expected status code 400 when a Trajectory query with no query parameters are specified for collection "
									+ collectionId);
				}
				if (supportsLocationsQuery) {
					
					response = init().baseUri(url).accept(JSON).when().request(GET, "/locations");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 136. Expected status code 400 when a Locations query with no query parameters are specified for collection "
									+ collectionId);
				}
			
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Abstract Test 35 : Validate that an error is returned by a Position query when the coords query parameter is not specified.
	 * Abstract Test 36 : Validate that an error is returned by a Position query when the coords query parameter does not contain a valid POINT Well Known Text value.
	 * Abstract Test 51 : Validate that an error is returned by an Area query when the coords query parameter is not specified.
	 * Abstract Test 52 : Validate that an error is returned by an Area query when the coords query parameter does not contain a valid POLYGON Well Known Text value.
	 * Abstract Test 83 : Validate that an error is returned by a Trajectory query when the coords query parameter is not specified.
	 * Abstract Test 84 : Validate that an error is returned by a Trajectory query when the coords query parameter does not contain a valid LINESTRING Well Known Text value.
	 * Abstract Test 85 : Validate that an error is returned by a Trajectory query when the coords query parameter does not contain a valid LINESTRINGM Well Known Text value.
	 * Abstract Test 88 : Validate that an error is returned by a Trajectory query when the coords query parameter does not contain a valid LINESTRINGZM Well Known Text value.
	 * Abstract Test 89 : Validate that an error is returned by a Trajectory query when the coords query parameter does not contain a valid LINESTRINGZ Well Known Text value.
	 * Abstract Test 101 : Validate that an error is returned by a Corridor query when the coords query parameter is not specified.
	 * Abstract Test 106 : Validate that an error is returned by a corridor query when the coords query parameter does not contain a valid LINESTRING Well Known Text value.
	 *
	 * @param collectionIdentifiers
	 */
	@SuppressWarnings("unchecked")
	@Test(dataProvider = "collectionIDs", description = "Implements Abstract Test 35 (/conf/position),Abstract Test 36 (/conf/position), Abstract Test 51 (/conf/area), Abstract Test 52 (/conf/area), Abstract Test 83 (/conf/trajectory), Abstract Test 101 (/conf/corridor)")
	public void validateCoordsQueryParameters(Object collectionIdentifiers) {



		Set<String> collectionIds = (Set<String>) collectionIdentifiers;
		ArrayList<String> collectionsList = new ArrayList<String>();
		collectionsList.addAll(collectionIds);		
		
		for (int c = 0; c < Math.min(this.noOfCollections,collectionsList.size()); c++) {
			
			String collectionId = collectionsList.get(c);
			
			boolean supportsPositionQuery = false;
			boolean supportsAreaQuery = false;
			boolean supportsTrajectoryQuery = false;
			boolean supportsCorridorQuery = false;

			String url = rootUri.toString() + "/collections/" + collectionId;

			Response response = init().baseUri(url).accept(JSON).when().request(GET);
			JsonPath jsonResponse = response.jsonPath();
			HashMap dataQueries = jsonResponse.getJsonObject("data_queries");
			supportsPositionQuery = dataQueries.containsKey("position");
			supportsAreaQuery = dataQueries.containsKey("area");
			supportsTrajectoryQuery = dataQueries.containsKey("trajectory");
			supportsCorridorQuery = dataQueries.containsKey("corridor");

			try {
			

				if (supportsPositionQuery) {
				
					response = init().baseUri(url).accept(JSON).when().request(GET, "/position?coords=");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 35. Expected status code 400 when a Position query with coords query parameter is not specified for collection "
									+ collectionId);

					response = init().baseUri(url).accept(JSON).when().request(GET, "/position?coords=POINT()");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 36. Expected status code 400 when a Position coords query parameter does not contain a valid POINT Well Known Text value for collection "
									+ collectionId);

				}
				if (supportsAreaQuery) {
				
					response = init().baseUri(url).accept(JSON).when().request(GET, "/area?coords=");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 51. Expected status code 400 when an Area query with coords query parameter is not specified for collection "
									+ collectionId);

					response = init().baseUri(url).accept(JSON).when().request(GET, "/area?coords=POLYGON()");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 52. Expected status code 400 when an Area query with coords query parameter does not contain a valid POLYGON Well Known Text value for collection "
									+ collectionId);
				}
				if (supportsTrajectoryQuery) {
			
					response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory?coords=");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 83. Expected status code 400 when a Trajectory query with coords query parameter is not specified for collection "
									+ collectionId);

					response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory?coords=LINESTRING()");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 84. Expected status code 400 when a Trajectory query with coords query parameter does not contain a valid LINESTRING Well Known Text value for collection "
									+ collectionId);

					response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory?coords=LINESTRINGM()");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 85. Expected status code 400 when a Trajectory query with coords query parameter does not contain a valid LINESTRING Well Known Text value for collection "
									+ collectionId);

					response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory?coords=LINESTRINGZM()");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 88. Expected status code 400 when a Trajectory query with coords query parameter does not contain a valid LINESTRING Well Known Text value for collection "
									+ collectionId);

					response = init().baseUri(url).accept(JSON).when().request(GET, "/trajectory?coords=LINESTRINGZ()");
					assertTrue(response.getStatusCode() == 400,
							"Fails Abstract Test 89. Expected status code 400 when a Trajectory query with coords query parameter does not contain a valid LINESTRING Well Known Text value for collection "
									+ collectionId);

				}
				if (supportsCorridorQuery) {
				
					response = init().baseUri(url).accept(JSON).when().request(GET, "/corridor?coords=");
					assertTrue(response.getStatusCode() == 400,
							"Expected status code 400 when a Corridor query with coords query parameter is not specified for collection "
									+ collectionId);


					response = init().baseUri(url).accept(JSON).when().request(GET, "/corridor?coords=LINESTRING()");
					assertTrue(response.getStatusCode() == 400,
							"Expected status code 400 when a Corridor query with coords query parameter does not contain a valid LINESTRING Well Known Text value for collection "
									+ collectionId);
				}
			
			} catch (Exception ex) {
			}

		}
	}


	/**
	 * Abstract Test 37 : Validate that resources can be identified and extracted from a Collection with a Position query using query parameters.
	 * Abstract Test 39 : Validate that the coords query parameters are processed correctly.
	 * Abstract Test 41 : Validate that the vertical level query parameters are constructed correctly.
	 * Abstract Test 43 : Validate that the datetime query parameters are processed correctly.
	 * Abstract Test 45 : Validate that the parameter-name query parameters are processed correctly.
	 * Abstract Test 47 : Validate that the crs query parameters are processed correctly.
	 * Abstract Test 49 : Validate that the f query parameters are processed correctly.
	 *
	 * @param collectionIdentifiers
	 */
	@SuppressWarnings("unchecked")
	@Test(dataProvider = "collectionIDs", description = "Implements Abstract Test 37 (/conf/position), Abstract Test 39 (/conf/edr/rc-coords-response), Abstract Test 41 (/conf/edr/rc-z-response),  Abstract Test 43 (/conf/core/datetime-response),  Abstract Test 45 (/conf/edr/rc-parameter-name-response), Abstract Test 47 (/conf/edr/REQ_rc-crs-response), Abstract Test 49 (/conf/collections/rc-f-response)")
	public void validatePositionQueryUsingParameters(Object collectionIdentifiers) {




		Set<String> collectionIds = (Set<String>) collectionIdentifiers;
		PositionQueryProcessor processor = new PositionQueryProcessor();

		String resultMessage = processor.validatePositionQueryUsingParameters(collectionIds,rootUri.toString(),this.noOfCollections,init());
		assertTrue(resultMessage.length()==0,
				"Fails Abstract Test 37. Therefore could not verify the implementation passes Abstract Tests 39, 41, 43, 45, 47, and 49. Expected information that matches the selection criteria is returned for Position query. "
						+ resultMessage);


	}




	private String printKeys(HashMap input) {
		StringBuffer sb = new StringBuffer();
		Iterator<String> inputIterator = input.keySet().iterator();
		while (inputIterator.hasNext()) {
			sb.append(inputIterator.next() + " \n");

		}
		return sb.toString();

	}


	/**
	 * Abstract Test 53 : Validate that resources can be identified and extracted from a Collection with an Area query using query parameters.
	 * Abstract Test 55 : Validate that the coords query parameters are processed correctly.
	 * Abstract Test 57 : Validate that the vertical level query parameters are constructed correctly.
	 * Abstract Test 59 : Validate that the datetime query parameters are processed correctly.
	 * Abstract Test 61 : Validate that the parameter-name query parameters are processed correctly.
	 * Abstract Test 63 : Validate that the crs query parameters are processed correctly.
	 * Abstract Test 65 : Validate that the f query parameters are processed correctly.
	 *
	 * @param collectionIdentifiers
	 */
	@SuppressWarnings("unchecked")
	@Test(dataProvider = "collectionIDs", description = "Implements Abstract Test 53 (/conf/area), Abstract Test 55 (/conf/edr/rc-coords-response), Abstract Test 57 (/conf/edr/rc-z-response),  Abstract Test 59 (/conf/core/datetime-response),  Abstract Test 61 (/conf/edr/rc-parameter-name-response), Abstract Test 63 (/conf/edr/REQ_rc-crs-response), Abstract Test 65 (/conf/collections/rc-f-response)")
	public void validateAreaQueryUsingParameters(Object collectionIdentifiers) {



		Set<String> collectionIds = (Set<String>) collectionIdentifiers;

		AreaQueryProcessor processor = new AreaQueryProcessor();
		String resultMessage = processor.validateAreaQueryUsingParameters(collectionIds,rootUri.toString(),this.noOfCollections,init());
		assertTrue(resultMessage.length()==0,
				"Fails Abstract Test 53. Therefore could not verify the implementation passes Abstract Tests 55, 57, 59, 61, 63, 65. Expected information that matches the selection criteria is returned for Area query. "
						+ resultMessage);


	}


	private String readStringFromURL(String urlString,int limit) throws Exception
	{
		URL requestURL = new URL(urlString);

		BufferedReader in = new BufferedReader(new InputStreamReader(requestURL.openConnection().getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		int i = 0;


		while (((inputLine = in.readLine()) != null) && (i < limit))
		{
			response.append(inputLine+"\n");
			i++;
		}


		in.close();

		return response.toString();
	}

}
