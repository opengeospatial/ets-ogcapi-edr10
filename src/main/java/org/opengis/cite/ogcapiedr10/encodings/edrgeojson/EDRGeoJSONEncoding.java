package org.opengis.cite.ogcapiedr10.encodings.edrgeojson;

import static org.opengis.cite.ogcapiedr10.SuiteAttribute.REQUIREMENTCLASSES;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.EtsAssert;
import org.opengis.cite.ogcapiedr10.conformance.RequirementClass;
import org.opengis.cite.ogcapiedr10.encodings.geojson.GeoJSONValidator;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class EDRGeoJSONEncoding extends CommonFixture {

	private String schemaToApply = "/org/opengis/cite/ogcapiedr10/jsonschema/edrgeojson.json";

	protected URI iut;

	/**
	 * <pre>
	 * Abstract Test 22: Verify support for the EDR GeoJSON Schema
	 * Abstract Test 23: Verify the content of an EDR GeoJSON document given an input document and schema.
	 * Note that the first positions resource that supports GeoJSON is tested.
	 * </pre>
	 */
	@Test(description = "Implements Abstract Test 22 (/conf/edr-geojson/definition), Abstract Test 23 (/conf/edr-geojson/content)")
	public void validateResponseForEDRGeoJSON(ITestContext testContext) {

		List<?> requirementClasses = (List<?>) testContext.getSuite().getAttribute(REQUIREMENTCLASSES.getName());

		if (!requirementClasses.contains(RequirementClass.EDRGEOJSON)) {
			throw new SkipException(String.format("Requirements class %s not implemented.",
					RequirementClass.EDRGEOJSON.getConformanceClass()));
		}

		StringBuffer sb = new StringBuffer();
		boolean atLeastOneCollectionTested = false; // we test the first locations
													// resource we find
		Response response = getCollectionResponse(null);
		JsonPath jsonResponse = response.jsonPath();

		ArrayList collectionsList = (ArrayList) jsonResponse.getList("collections");

		for (int i = 0; (i < collectionsList.size()) && (atLeastOneCollectionTested == false); i++) {
			HashMap collectionItem = (HashMap) collectionsList.get(i);

			String collectionId = collectionItem.get("id").toString();

			if (!collectionItem.containsKey("data_queries")) {
				continue; // we loop until we find a collection that supports data_queries
			}

			HashMap dataQueries = (HashMap) collectionItem.get("data_queries");
			boolean supportsPositionQuery = dataQueries.containsKey("position");

			if (supportsPositionQuery) {
				HashMap positionQuery = (HashMap) dataQueries.get("position");
				HashMap link = (HashMap) positionQuery.get("link");
				HashMap variables = (HashMap) link.get("variables");

				ArrayList<String> outputFormatList = (ArrayList<String>) variables.get("output_formats");
				String supportedFormat = null;

				for (int f = 0; f < outputFormatList.size(); f++) {
					if (outputFormatList.get(f).equals("GeoJSON")) {
						supportedFormat = outputFormatList.get(f);
					}
				}

				ArrayList<String> crsList = (ArrayList<String>) collectionItem.get("crs");

				String supportedCRS = null;
				for (int f = 0; f < crsList.size(); f++) {
					if (crsList.get(f).equals("CRS84") || crsList.get(f).equals("CRS:84")
							|| crsList.get(f).equals("WGS84") || crsList.get(f).equals("EPSG:4326")
							|| crsList.get(f).contains("www.opengis.net/def/crs/OGC/1.3/CRS84")) {
						supportedCRS = crsList.get(f);
					}
				}

				double medianx = 0d;
				double mediany = 0d;

				HashMap extent = (HashMap) collectionItem.get("extent");
				if (extent.containsKey("spatial")) {

					HashMap spatial = (HashMap) extent.get("spatial");

					if (!spatial.containsKey("bbox")) {

						sb.append("spatial extent of collection " + collectionId + " missing bbox\n");
						continue;
					}

					ArrayList bboxEnv = (ArrayList) spatial.get("bbox"); // for some
																			// unknown
																			// reason the
																			// library
																			// returns
																			// JSON types
																			// as Integers
																			// only

					ArrayList bbox = null;

					if (bboxEnv.get(0).getClass().toString().contains("java.lang.Integer")
							|| bboxEnv.get(0).getClass().toString().contains("java.lang.Double")
							|| bboxEnv.get(0).getClass().toString().contains("java.lang.Float")) { // for
																									// EDR
																									// API
																									// V1.0.0
						bbox = bboxEnv;

					}
					else if (bboxEnv.get(0).getClass().toString().contains("java.util.ArrayList")) { // for
																										// EDR
																										// API
																										// V1.0.1
						bbox = (ArrayList) bboxEnv.get(0);
					}

					if (bbox.size() > 3) {

						if (bbox.get(0).getClass().toString().contains("Integer")
								|| bbox.get(0).getClass().toString().contains("Double")
								|| bbox.get(0).getClass().toString().contains("Float")) {
							double minx = Double.parseDouble(bbox.get(0).toString());
							double miny = Double.parseDouble(bbox.get(1).toString());
							double maxx = Double.parseDouble(bbox.get(2).toString());
							double maxy = Double.parseDouble(bbox.get(3).toString());

							medianx = minx + ((maxx - minx) / 2d);
							mediany = miny + ((maxy - miny) / 2d);

						}

					}
					else {
						sb.append("bbox of spatial extent of collection" + collectionId
								+ " has fewer than four coordinates\n");
					}

				}

				HashMap parameterNames = (HashMap) collectionItem.get("parameter_names");
				Set parameterNamesSet = parameterNames.keySet();
				Iterator<String> parameterNamesIterator = parameterNamesSet.iterator();

				parameterNamesIterator.hasNext();
				String sampleParamaterName = parameterNamesIterator.next();
				String sampleParamaterNameSafe = null;
				try {

					sampleParamaterNameSafe = URLEncoder.encode(sampleParamaterName, "UTF8");

				}
				catch (Exception ex) {
					ex.printStackTrace();
				}

				String sampleDateTime = null;
				if (extent.containsKey("temporal")) {

					HashMap temporal = (HashMap) extent.get("temporal");

					if (!temporal.containsKey("interval")) {

						sb.append("temporal extent of collection " + collectionId + " missing interval\n");
						continue;
					}

					ArrayList intervalEnv = (ArrayList) temporal.get("interval"); // for
																					// some
																					// unknown
																					// reason
																					// the
																					// library
																					// returns

					ArrayList interval = null;

					if (intervalEnv.get(0).getClass().toString().contains("java.lang.String")) { // EDR
																									// API
																									// v1.0.0
						interval = intervalEnv;
					}
					else { // EDR API v1.0.1
						interval = (ArrayList) intervalEnv.get(0);
					}

					if (interval.size() > 1) {

						sampleDateTime = interval.get(0) + "/" + interval.get(1);

					}

				}

				try {
					if (supportedFormat != null && supportedCRS != null) {
						if (supportedFormat.equals("GeoJSON") &&

								(supportedCRS.equals("CRS84") || supportedCRS.equals("CRS:84")
										|| supportedCRS.equals("WGS84") || supportedCRS.equals("EPSG:4326")
										|| supportedCRS.contains("www.opengis.net/def/crs/OGC/1.3/CRS84"))

						) {

							String locationsURL = null;

							if (supportedCRS.equals("EPSG:4326")) {
								locationsURL = link.get("href").toString() + "?f=" + supportedFormat + "&crs=EPSG:4326"
										+ "&coords=POINT(" + mediany + "+" + medianx + ")" + "&parameter-name="
										+ sampleParamaterNameSafe + "&datetime=" + sampleDateTime;
							}
							else {
								locationsURL = link.get("href").toString() + "?f=" + supportedFormat + "&crs=CRS84"
										+ "&coords=POINT(" + medianx + "+" + mediany + ")" + "&parameter-name="
										+ sampleParamaterNameSafe + "&datetime=" + sampleDateTime;
							}

							GeoJSONValidator validator = new GeoJSONValidator();
							boolean result = validator.isGeoJSONValidPerSchema(locationsURL,
									GeoJSONValidator.EDRGeoJSON);

							atLeastOneCollectionTested = true;
							if (result == false) {
								String msg = " GeoJSON returned by Collection " + collectionId
										+ " failed the schema validation test.\n";

								sb.append(msg);
							}
						}
						else {
							continue; // loop until we find a collection that supports
										// GeoJSON encoded responses that are referenced
										// to CRS84.
						}
					}
					else {
						continue; // loop until we find a collection that supports GeoJSON
									// encoded responses that are referenced to CRS84.
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
					sb.append(" " + ex.getMessage() + "\n");
				}

			}
			else {
				continue; // we loop until we find a collection that supports position
							// data queries
			}

		}

		// String resultMessage = sb.toString(); //verbose error message
		// EtsAssert.assertTrue(atLeastOneCollectionTested, "Fails Abstract Test 23. " +
		// resultMessage);

		EtsAssert.assertTrue(atLeastOneCollectionTested,
				"Fails Abstract Test 23. None of the collections were found to offer Position resources that return GeoJSON conforming to EDR GeoJSON.\n");

	}

}