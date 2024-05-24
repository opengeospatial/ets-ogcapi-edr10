package org.opengis.cite.ogcapiedr10.collections;

import com.reprezen.kaizen.oasparser.model3.Parameter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.opengis.cite.ogcapiedr10.CommonDataFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.*;

import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.GEOJSON_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.OPEN_API_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.collections.FeaturesAssertions.assertNumberReturned;
import static org.opengis.cite.ogcapiedr10.collections.FeaturesAssertions.assertTimeStamp;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class AbstractFeatures extends CommonDataFixture {

	String apiDef = null;

	protected final Map<CollectionResponseKey, ResponseData> collectionIdAndResponse = new HashMap<>();

	// protected List<Map<String, Object>> collections;

	protected URI iut;

	@DataProvider(name = "collectionPaths")
	public Iterator<Object[]> collectionPaths(ITestContext testContext) {

		List<TestPoint> testPointsForCollections = new ArrayList<TestPoint>();

		// "Locations", "Position", "Radius", "Trajectory", "Cube", "Corridor"

		String[] resources = { "locations", "position", "cube", "corridor", "area", "trajectory" };
		for (String res : resources) {
			testPointsForCollections.add(new TestPoint(rootUri.toString(), "/" + res, null));
		}

		List<Object[]> collectionsData = new ArrayList<>();
		for (TestPoint testPointForCollections : testPointsForCollections) {

			collectionsData.add(new Object[] { testPointForCollections });
		}

		return collectionsData.iterator();
	}

	@DataProvider(name = "locationsCollectionPaths")
	public Iterator<Object[]> locationsCollectionPaths(ITestContext testContext) {

		List<TestPoint> testPointsForCollections = new ArrayList<TestPoint>();

		testPointsForCollections.add(new TestPoint(rootUri.toString(), "/locations", null));

		List<Object[]> collectionsData = new ArrayList<>();
		if (apiDef.contains("/locations")) {
			for (TestPoint testPointForCollections : testPointsForCollections) {

				collectionsData.add(new Object[] { testPointForCollections });
			}
		}

		return collectionsData.iterator();
	}

	@DataProvider(name = "positionCollectionPaths")
	public Iterator<Object[]> positionCollectionPaths(ITestContext testContext) {

		List<TestPoint> testPointsForCollections = new ArrayList<TestPoint>();

		testPointsForCollections.add(new TestPoint(rootUri.toString(), "/position", null));

		List<Object[]> collectionsData = new ArrayList<>();
		if (apiDef.contains("/position")) {
			for (TestPoint testPointForCollections : testPointsForCollections) {

				collectionsData.add(new Object[] { testPointForCollections });
			}
		}

		return collectionsData.iterator();
	}

	@DataProvider(name = "areaCollectionPaths")
	public Iterator<Object[]> areaCollectionPaths(ITestContext testContext) {

		List<TestPoint> testPointsForCollections = new ArrayList<TestPoint>();

		testPointsForCollections.add(new TestPoint(rootUri.toString(), "/area", null));

		List<Object[]> collectionsData = new ArrayList<>();
		if (apiDef.contains("/area")) {
			for (TestPoint testPointForCollections : testPointsForCollections) {

				collectionsData.add(new Object[] { testPointForCollections });
			}
		}

		return collectionsData.iterator();
	}

	@DataProvider(name = "trajectoryCollectionPaths")
	public Iterator<Object[]> trajectoryCollectionPaths(ITestContext testContext) {

		List<TestPoint> testPointsForCollections = new ArrayList<TestPoint>();

		testPointsForCollections.add(new TestPoint(rootUri.toString(), "/trajectory", null));

		List<Object[]> collectionsData = new ArrayList<>();
		if (apiDef.contains("/trajectory")) {
			for (TestPoint testPointForCollections : testPointsForCollections) {

				collectionsData.add(new Object[] { testPointForCollections });
			}
		}

		return collectionsData.iterator();
	}

	@DataProvider(name = "cubeCollectionPaths")
	public Iterator<Object[]> cubeCollectionPaths(ITestContext testContext) {

		List<TestPoint> testPointsForCollections = new ArrayList<TestPoint>();

		testPointsForCollections.add(new TestPoint(rootUri.toString(), "/cube", null));

		List<Object[]> collectionsData = new ArrayList<>();
		if (apiDef.contains("/cube")) {
			for (TestPoint testPointForCollections : testPointsForCollections) {

				collectionsData.add(new Object[] { testPointForCollections });
			}
		}

		return collectionsData.iterator();
	}

	@DataProvider(name = "corridorCollectionPaths")
	public Iterator<Object[]> corridorCollectionPaths(ITestContext testContext) {

		List<TestPoint> testPointsForCollections = new ArrayList<TestPoint>();

		testPointsForCollections.add(new TestPoint(rootUri.toString(), "/corridor", null));

		List<Object[]> collectionsData = new ArrayList<>();
		if (apiDef.contains("/corridor")) {
			for (TestPoint testPointForCollections : testPointsForCollections) {

				collectionsData.add(new Object[] { testPointForCollections });
			}
		}

		return collectionsData.iterator();
	}

        @BeforeClass
        public void retrieveRequiredInformationFromTestContext(ITestContext testContext) {
            OpenApi3 openApiDef = (OpenApi3) testContext.getSuite().getAttribute(SuiteAttribute.API_MODEL.getName());
            apiDef = openApiDef.getOpenApi();
        }

	/**
	 * Abstract Test 22, Test Method 1
	 *
	 * <pre>
	 * Abstract Test 22: /ats/core/fc-response
	 * Test Purpose: Validate that the Feature Collections complies with the require structure and contents.
	 * Requirement: /req/core/fc-response
	 *
	 * Test Method
	 *   1. Validate that the type property is present and has a value of FeatureCollection
	 * </pre>
	 *
	 * @param collection the collection under test, never <code>null</code>
	 */
	public void validateTypeProperty(CollectionResponseKey collection) {
		ResponseData response = collectionIdAndResponse.get(collection);
		if (response == null)
			throw new SkipException("Could not find a response for collection with id " + collection.id);

		JsonPath jsonPath = response.jsonPath();
		String type = jsonPath.get("type");
		assertNotNull(type, "type property is missing");
		assertEquals(type, "FeatureCollection", "Expected type property value of FeatureCollection but was " + type);
	}

	/**
	 * Abstract Test 22, Test Method 2
	 *
	 * <pre>
	 * Abstract Test 22: /ats/core/fc-response
	 * Test Purpose: Validate that the Feature Collections complies with the require structure and contents.
	 * Requirement: /req/core/fc-response
	 *
	 * Test Method
	 *   2. Validate the features property is present and that it is populated with an array of feature items.
	 * </pre>
	 *
	 * @param collection the collection under test, never <code>null</code>
	 */
	void validateFeaturesProperty(CollectionResponseKey collection) {
		ResponseData response = collectionIdAndResponse.get(collection);
		if (response == null)
			throw new SkipException("Could not find a response for collection with id " + collection.id);

		JsonPath jsonPath = response.jsonPath();
		List<Object> type = jsonPath.get("features");
		assertNotNull(type, "features property is missing");
	}

	/**
	 * Abstract Test 22, Test Method 4 (Abstract Test 23)
	 *
	 * <pre>
	 * Abstract Test 22: /ats/core/fc-response
	 * Test Purpose: Validate that the Feature Collections complies with the require structure and contents.
	 * Requirement: /req/core/fc-response
	 *
	 * Test Method
	 *   4. If the links property is present, validate that all entries comply with /ats/core/fc-links
	 * </pre>
	 *
	 * <pre>
	 * Abstract Test 23: /ats/core/fc-links
	 * Test Purpose: Validate that the required links are included in the Collections document.
	 * Requirement: /req/core/fc-links, /req/core/fc-rel-type
	 *
	 * Test Method:
	 * Verify that the response document includes:
	 *   1. a link to this response document (relation: self),
	 *   2. a link to the response document in every other media type supported by the server (relation: alternate).
	 *
	 * Verify that all links include the rel and type link parameters.
	 * </pre>
	 *
	 * @param collection the collection under test, never <code>null</code>
	 */
	void validateLinks(CollectionResponseKey collection) {
		ResponseData response = collectionIdAndResponse.get(collection);
		if (response == null)
			throw new SkipException("Could not find a response for collection with id " + collection.id);

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> links = jsonPath.getList("links");

		// 1. a link to this response document (relation: self)
		Map<String, Object> linkToSelf = findLinkByRel(links, "self");
		assertNotNull(linkToSelf, "Feature Collection Metadata document must include a link for itself");

		// 2. a link to the response document in every other media type supported by the
		// server (relation: alternate)
		// Dev: Supported media type are identified by the compliance classes for this
		// server
		List<String> mediaTypesToSupport = createListOfMediaTypesToSupportForFeatureCollectionsAndFeatures(linkToSelf);
		List<Map<String, Object>> alternateLinks = findLinksWithSupportedMediaTypeByRel(links, mediaTypesToSupport,
				"alternate");
		List<String> typesWithoutLink = findUnsupportedTypes(alternateLinks, mediaTypesToSupport);
		assertTrue(typesWithoutLink.isEmpty(),
				"Feature Collection Metadata document must include links for alternate encodings. Missing links for types "
						+ typesWithoutLink);

		// Validate that each "self"/"alternate" link includes a rel and type parameter.
		Set<String> rels = new HashSet<>();
		rels.add("self");
		rels.add("alternate");
		List<String> linksWithoutRelOrType = findLinksWithoutRelOrType(links, rels);
		assertTrue(linksWithoutRelOrType.isEmpty(),
				"Links for alternate encodings must include a rel and type parameter. Missing for links "
						+ linksWithoutRelOrType);
	}

	/**
	 * Abstract Test 22, Test Method 5 (Abstract Test 24)
	 *
	 * <pre>
	 * Abstract Test 22: /ats/core/fc-response
	 * Test Purpose: Validate that the Feature Collections complies with the require structure and contents.
	 * Requirement: /req/core/fc-response
	 *
	 * Test Method
	 *   5. If the timeStamp property is present, validate that it complies with /ats/core/fc-timeStamp
	 * </pre>
	 *
	 * <pre>
	 * Abstract Test 24: /ats/core/fc-timeStamp
	 * Test Purpose: Validate the timeStamp parameter returned with a Features response
	 * Requirement: /req/core/fc-timeStamp
	 *
	 * Test Method: Validate that the timeStamp value is set to the time when the response was generated.
	 * </pre>
	 *
	 * @param collection the collection under test, never <code>null</code>
	 */
	public void validateTimeStamp(CollectionResponseKey collection) {
		ResponseData response = collectionIdAndResponse.get(collection);
		if (response == null)
			throw new SkipException("Could not find a response for collection with id " + collection.id);

		JsonPath jsonPath = response.jsonPath();

		assertTimeStamp(collection.id, jsonPath, response.timeStampBeforeResponse, response.timeStampAfterResponse,
				true);
	}

	/**
	 * Abstract Test 22, Test Method 7 (Abstract Test 26)
	 *
	 * <pre>
	 * Abstract Test 22: /ats/core/fc-response
	 * Test Purpose: Validate that the Feature Collections complies with the require structure and contents.
	 * Requirement: /req/core/fc-response
	 *
	 * Test Method
	 *   7. If the numberReturned property is present, validate that it complies with /ats/core/fc-numberReturned
	 * </pre>
	 *
	 * <pre>
	 * Abstract Test 26: /ats/core/fc-numberReturned
	 * Test Purpose: Validate the numberReturned parameter returned with a Features response
	 * Requirement: /req/core/fc-numberReturned
	 *
	 * Test Method: Validate that the numberReturned value is identical to the number of features in the response.
	 * </pre>
	 *
	 * @param collection the collection under test, never <code>null</code>
	 */
	void validateNumberReturned(CollectionResponseKey collection) {
		ResponseData response = collectionIdAndResponse.get(collection);
		if (response == null)
			throw new SkipException("Could not find a response for collection with id " + collection.id);

		JsonPath jsonPath = response.jsonPath();

		assertNumberReturned(collection.id, jsonPath, true);
	}

	protected String findFeaturesUrlForGeoJson(Map<String, Object> collection) {
		List<Object> links = (List<Object>) collection.get("links");
		for (Object linkObject : links) {
			Map<String, Object> link = (Map<String, Object>) linkObject;
			Object rel = link.get("rel");
			Object type = link.get("type");
			if ("items".equals(rel) && GEOJSON_MIME_TYPE.equals(type))
				return (String) link.get("href");
		}
		return null;
	}

	protected boolean isRequired(Parameter param) {
		return param.getRequired() != null && param.getRequired();
	}

	protected Boolean isExplode(Parameter param) {
		return param.getExplode() != null && param.getExplode();
	}

	protected class ResponseData {

		private final Response response;

		protected final ZonedDateTime timeStampBeforeResponse;

		protected final ZonedDateTime timeStampAfterResponse;

		public ResponseData(Response response, ZonedDateTime timeStampBeforeResponse,
				ZonedDateTime timeStampAfterResponse) {
			this.response = response;
			this.timeStampBeforeResponse = timeStampBeforeResponse;
			this.timeStampAfterResponse = timeStampAfterResponse;
		}

		public JsonPath jsonPath() {
			return response.jsonPath();
		}
	}

	protected class CollectionResponseKey {

		private final String id;

		protected CollectionResponseKey(String id) {
			this.id = id;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			CollectionResponseKey that = (CollectionResponseKey) o;
			return Objects.equals(id, that.id);
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}
	}

}
