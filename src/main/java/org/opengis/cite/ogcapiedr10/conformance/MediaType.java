package org.opengis.cite.ogcapiedr10.conformance;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.IUT;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class MediaType extends CommonFixture {

	protected URI iut;
	private String testingWktPOINT = "POINT(-1.054687%2052.498649)";
	private String geoJSONTestingCollection = "gfs-surface-precip";

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
	 * <pre>
	 * Abstract Test 18: Verify support for JSON
	 * Abstract Test 19: Verify the content of a JSON document given an input document and schema.
	 * </pre>
	 */
	@Test(description = "Implements Abstract Test 18 (/conf/json/definition), Abstract Test 19 (/conf/json/content)")
	public void validateResponseForJSON() {

		Response response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET);
		assertTrue(response.getStatusCode() == 200,
				"Fails Abstract Test 18, JSON response not supported for landing page");
		assertTrue(isJSONValidPerSchema(response.asString(), "/"),
				"Fails Abstract Test 19, landing page response not valid JSON");

		response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET, "/api");
		assertTrue(response.getStatusCode() == 200,
				"Fails Abstract Test 18, JSON response not supported for api description");

		response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET,
				"/conformance");
		assertTrue(response.getStatusCode() == 200,
				"Fails Abstract Test 18, JSON response not supported for conformance declaration");
		assertTrue(isJSONValidPerSchema(response.asString(), "/conformance"),
				"Fails Abstract Test 19, conformance declaration response not valid JSON");

		response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET,
				"/collections");
		assertTrue(response.getStatusCode() == 200,
				"Fails Abstract Test 18, JSON response not supported for collections metadata");
		// TODO add JSON Schema Validation for /collections

	}

	private boolean isJSONValidPerSchema(String doc, String path) {

		boolean valid = false;

		if (path.equals("/")) {
			try (InputStream inputStream = getClass()
					.getResourceAsStream("/org/opengis/cite/ogcapiedr10/jsonschema/landingPage.json")) {
				JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
				Schema schema = SchemaLoader.load(rawSchema);
				schema.validate(new JSONObject(doc)); // throws a ValidationException if this object is invalid
				valid = true;
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		} else if (path.equals("/conformance")) {
			try (InputStream inputStream = getClass()
					.getResourceAsStream("/org/opengis/cite/ogcapiedr10/jsonschema/confClasses.json")) {
				JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
				Schema schema = SchemaLoader.load(rawSchema);
				schema.validate(new JSONObject(doc)); // throws a ValidationException if this object is invalid
				valid = true;
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		} else {
			valid = true;
		}

		return valid;

	}

	/**
	 * <pre>
	 * Abstract Test 24: Verify support for CoverageJSON
	 * Abstract Test 25: Verify the content of a CoverageJSON document given an input document and schema.
	 * </pre>
	 */
	@Test(description = "Implements Abstract Test 24 (/conf/covjson/definition), Abstract Test 25 (/conf/covjson/content)", dataProvider = "collectionIDs", alwaysRun = true)
	public void validateResponseForCoverageJSON(Object collectionIdentifiers) {

		boolean supportsCoverageJSON = false;
	
		
		Set<String> collectionTypes = (Set<String>) collectionIdentifiers;

		String firstCollection = null;
		String firstParameterName = null;
		int i = 0;

	

		for (String collection : collectionTypes) {

		
			boolean returnsValidCoverageJSON = false;
	

				JsonPath response;
				Response request = init().baseUri(rootUri.toString()).accept(JSON).when().request(GET,
						"/collections/" + collection);

				request.then().statusCode(200);
				response = request.jsonPath();
				firstCollection = response.get("id").toString();

	

				ArrayList bbox = null;
				HashMap extentMap = null;
				try {
					extentMap = (HashMap) response.get("extent");
					HashMap spatialExtentMap = (HashMap) extentMap.get("spatial");
					bbox = (ArrayList) spatialExtentMap.get("bbox");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

	

				ArrayList interval = null;
				HashMap temporalExtentMap = null;
				try {
					if (extentMap.containsKey("temporal")) {
						temporalExtentMap = (HashMap) extentMap.get("temporal");

						if (temporalExtentMap.containsKey("interval")) {
							interval = (ArrayList) temporalExtentMap.get("interval");
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

		

				ArrayList<Object> keys = null;
				try {
					String parameterNameText = response.get("parameter_names").toString();
					HashMap parameterNameMap = (HashMap) response.get("parameter_names");
					keys = new ArrayList<>(parameterNameMap.keySet());
					firstParameterName = keys.get(0).toString();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

	

				if (interval != null) {
					String positionRequest = null;

					try {
						positionRequest = iut + "/collections/" + URLEncoder.encode(firstCollection, StandardCharsets.UTF_8.toString()) + "/position?coords="
								+ testingWktPOINT + "&parameter-name=" + URLEncoder.encode(firstParameterName, StandardCharsets.UTF_8.toString()) + "&datetime="
								+ constructDateTimeValue(interval.get(0).toString()) + "&crs=CRS84&f=CoverageJSON";
					} catch (Exception et) {
						et.printStackTrace();
					}

				

					try {
						if (positionRequest != null) {
							StringBuffer sb = new StringBuffer();

							URL url = new URL(positionRequest);

							BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
							String line;
							while ((line = in.readLine()) != null) {
								sb.append(line + "\n");
							}
							in.close();
					
							returnsValidCoverageJSON = validateCoverageJSON(sb.toString());
						}
					} catch (Exception et) {
						et.printStackTrace();
					}

				}

			
				if(returnsValidCoverageJSON) supportsCoverageJSON = true;				
			
			i++;

		}
		
		assertTrue(supportsCoverageJSON, "Fails Abstract Test 24, none of the collections appears to support valid CoverageJSON");

	}

	private boolean validateGeoJSON(String jsonObject) {
		boolean isValid = false;
		JsonPath geoJsonObject = new JsonPath(jsonObject);
		isValid = geoJsonObject.get("type").toString().equals("Feature");
		if (isValid == false) {
			isValid = geoJsonObject.get("type").toString().equals("FeatureCollection");
		}
		return isValid;
	}

	private boolean validateCoverageJSON(String jsonObject) {
		boolean isValid = false;
		JsonPath covJsonObject = new JsonPath(jsonObject);
		isValid = covJsonObject.get("type").toString().equals("Coverage");

		return isValid;
	}

	private String constructDateTimeValue(String input) throws Exception {
		String startDateOfInterval = null;
		String endDateOfInterval = null;
		// String test = "R36/2021-10-05T03:00:00Z/PT3H";
		
		if(!input.contains("00:00Z")) input = input.replace(":00Z", ":00:00Z");  //TODO For testing. REMOVE when done.

		String[] token = input.split("/");

		for (int i = 0; i < token.length; i++) {
			if (token[i].split("-").length == 3) {
				startDateOfInterval = token[i];
				i = token.length; // we found a valid token so we break the loop
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar c = Calendar.getInstance();

		c.setTime(sdf.parse(startDateOfInterval));

		c.add(Calendar.HOUR, 3);
		endDateOfInterval = sdf.format(c.getTime());

		return startDateOfInterval + "/" + endDateOfInterval;
	}

	/**
	 * <pre>
	 * Abstract Test 26 : Verify support for HTML
	 * </pre>
	 */
	@Test(description = "Implements Abstract Test 26 (/conf/html/definition)")
	public void validateResponseForHTML() {
		Response response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET);
		assertTrue(response.getStatusCode() == 200, "HTML response not supported for landing page");

		response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET, "/api");
		assertTrue(response.getStatusCode() == 200, "HTML response not supported for api description");

		response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET,
				"/conformance");
		assertTrue(response.getStatusCode() == 200, "HTML response not supported for conformance declaration");

		response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET,
				"/collections");
		assertTrue(response.getStatusCode() == 200, "HTML response not supported for collections metadata");
	}
	
	/**
	 * <pre>
	 * Abstract Test 20: Verify support for JSON and GeoJSON
	 * Abstract Test 21: Verify the content of a GeoJSON document given an input document and schema.
	 * </pre>
	 */
	@Test(description = "Implements Abstract Test 20 (/conf/geojson/definition), Abstract Test 21 (/conf/geojson/content)", dataProvider = "collectionIDs", alwaysRun = true)
	public void validateResponseForGeoJSON(Object collectionIdentifiers) {

	  // http://localhost/edr/collections/metar_demo/position?coords=POINT(-1.054687%2052.498649)&parameter-name=Metar%20observation&datetime=2021-10-05T01:00Z/2021-10-05T05:00Z&crs=CRS84&f=GeoJSON


	  boolean supportsGeoJSON = false;
	  
	  Set<String> collectionTypes = (Set<String>) collectionIdentifiers;

	  String firstCollection = null;
	  String firstParameterName = null;
	  int i = 0;



	  for (String collection : collectionTypes) {

	    boolean returnsValidGeoJSON = false;



	      JsonPath response;
	      Response request = init().baseUri(rootUri.toString()).accept(JSON).when().request(GET,
	          "/collections/" + collection);

	      request.then().statusCode(200);
	      response = request.jsonPath();
	      firstCollection = response.get("id").toString();



	      ArrayList bbox = null;
	      HashMap extentMap = null;
	      try {
	        extentMap = (HashMap) response.get("extent");
	        HashMap spatialExtentMap = (HashMap) extentMap.get("spatial");
	        bbox = (ArrayList) spatialExtentMap.get("bbox");
	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }



	      ArrayList interval = null;
	      HashMap temporalExtentMap = null;
	      try {
	        if (extentMap.containsKey("temporal")) {
	          temporalExtentMap = (HashMap) extentMap.get("temporal");

	          if (temporalExtentMap.containsKey("interval")) {
	            interval = (ArrayList) temporalExtentMap.get("interval");
	          }
	        }

	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }

	  

	      ArrayList<Object> keys = null;
	      try {
	        String parameterNameText = response.get("parameter_names").toString();
	        HashMap parameterNameMap = (HashMap) response.get("parameter_names");
	        keys = new ArrayList<>(parameterNameMap.keySet());
	        firstParameterName = keys.get(0).toString();
	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }



	      if (interval != null) {
	        String positionRequest = null;

	        try {
	        	
	        	
	          positionRequest = iut + "/collections/" + URLEncoder.encode(firstCollection, StandardCharsets.UTF_8.toString()) + "/position?coords="
	              + testingWktPOINT + "&parameter-name=" + URLEncoder.encode(firstParameterName, StandardCharsets.UTF_8.toString()) + "&datetime="
	              + constructDateTimeValue(interval.get(0).toString()) + "&crs=CRS84&f=GeoJSON";
	          
	          
	          
	        } catch (Exception et) {
	          et.printStackTrace();
	        }

	      

	        try {
	          if (positionRequest != null) {
	            StringBuffer sb = new StringBuffer();

	            URL url = new URL(positionRequest);

	            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	              sb.append(line + "\n");
	            }
	            in.close();
	            returnsValidGeoJSON = validateGeoJSON(sb.toString());
	          
	          }
	        } catch (Exception et) {
	          et.printStackTrace();
	        }

	      }

	      if(returnsValidGeoJSON) supportsGeoJSON = true;
	  	
	    
	    i++;

	  }
	  
	  assertTrue(supportsGeoJSON, "Fails Abstract Test 20 and 21, none of the collections appears to support valid GeoJSON");

	}	
}
