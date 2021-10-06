package org.opengis.cite.ogcapiedr10.encodings.coveragejson;

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

public class CoverageJSONEncoding extends CommonFixture {

	protected URI iut;


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

	private boolean isCoverageJSONValidPerSchema(String doc, String path) {

		boolean valid = false;

	
			try (InputStream inputStream = getClass()
					.getResourceAsStream("/org/opengis/cite/ogcapiedr10/jsonschema/landingPage.json")) {
				JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
				Schema schema = SchemaLoader.load(rawSchema);
				schema.validate(new JSONObject(doc)); // throws a ValidationException if this object is invalid
				valid = true;
			} catch (Exception ee) {
				ee.printStackTrace();
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



	private boolean validateCoverageJSON(String jsonObject) {
		boolean isValid = false;
		JsonPath covJsonObject = new JsonPath(jsonObject);
		isValid = covJsonObject.get("type").toString().equals("Coverage");

		return isValid;
	}

	

	
}
