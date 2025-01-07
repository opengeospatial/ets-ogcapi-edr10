package org.opengis.cite.ogcapiedr10.encodings.geojson;

import static org.opengis.cite.ogcapiedr10.SuiteAttribute.REQUIREMENTCLASSES;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.EtsAssert;
import org.opengis.cite.ogcapiedr10.conformance.RequirementClass;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class GeoJSONEncoding extends CommonFixture {

	protected URI iut;

	/**
	 * <pre>
	 * Abstract Test 20: Verify support for JSON and GeoJSON
	 * Abstract Test 21: Verify the content of a JSON document given an input document and schema.
	 * Note that the first locations resource that supports GeoJSON is tested.
	 * </pre>
	 */
	@Test(description = "Implements Abstract Test 20 (/conf/geojson/definition), Abstract Test 21 (/conf/geojson/content)")
	public void validateResponseForGeoJSON(ITestContext testContext) {

		List<?> requirementClasses = (List<?>) testContext.getSuite().getAttribute(REQUIREMENTCLASSES.getName());

		if (!requirementClasses.contains(RequirementClass.GEOJSON)) {
			throw new SkipException(String.format("Requirements class %s not implemented.",
					RequirementClass.GEOJSON.getConformanceClass()));
		}

		StringBuffer sb = new StringBuffer();
		boolean atLeastOneCollectionTested = false; // we test the first locations
													// resource we find
		Response response = getCollectionResponse(null);
		JsonPath jsonResponse = response.jsonPath();
		ArrayList collectionsList = (ArrayList) jsonResponse.getList("collections");

		for (int i = 0; (i < collectionsList.size()) && (atLeastOneCollectionTested == false); i++) {
			HashMap collectionItem = (HashMap) collectionsList.get(i);

			if (!collectionItem.containsKey("data_queries")) {
				continue; // we loop until we find a collection that supports data_queries
			}

			HashMap dataQueries = (HashMap) collectionItem.get("data_queries");
			boolean supportsLocationsQuery = dataQueries.containsKey("locations");

			if (supportsLocationsQuery) {
				HashMap locationsQuery = (HashMap) dataQueries.get("locations");
				HashMap link = (HashMap) locationsQuery.get("link");
				HashMap variables = (HashMap) link.get("variables");
				ArrayList<String> outputFormatList = (ArrayList<String>) variables.get("output_formats");
				String supportedFormat = null;
				for (int f = 0; f < outputFormatList.size(); f++) {
					if (outputFormatList.get(f).equalsIgnoreCase("GeoJSON")) {
						supportedFormat = outputFormatList.get(f);
					}
				}

				try {
					if (supportedFormat != null) {
						String locationsURL = link.get("href").toString() + "?f=" + supportedFormat;

						GeoJSONValidator validator = new GeoJSONValidator();
						boolean result = validator.isGeoJSONValidPerSchema(locationsURL, GeoJSONValidator.GeoJSON);
						atLeastOneCollectionTested = true;
						if (result == false) {
							sb.append(
									" None of the collections with locations resources were found to offer GeoJSON encoded responses.\n");
						}
					}
					else {
						continue; // We loop until we find a collection that supports
									// GeoJSON.
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
					sb.append(" " + ex.getMessage() + "\n");
				}

			}
			else {
				continue; // we loop until we find a collection that supports the
							// Locations query
			}

		}

		// String resultMessage = sb.toString(); //verbose error message
		// EtsAssert.assertTrue(resultMessage.length()==0, "Fails Abstract Test 21. " +
		// resultMessage);

		EtsAssert.assertTrue(atLeastOneCollectionTested,
				"Fails Abstract Test 21. None of the collections were found to offer Locations resources that return valid GeoJSON\n");

	}

}