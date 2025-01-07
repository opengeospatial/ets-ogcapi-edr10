package org.opengis.cite.ogcapiedr10.queries;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opengis.cite.ogcapiedr10.util.JsonUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CorridorQueryProcessor extends AbstractProcessor {

	double sizeOfLensSide = 1d; // in degrees

	double corridorWidth = 2;

	String corridorWidthUnit = "m";

	public final String queryTypeNotSupported = "None of the collections support this query type. Increase the number of collections to parse.";

	public String validateCorridorQueryUsingParameters(Set<String> collectionIds, String rootUri, int noOfCollections,
			RequestSpecification ini) {
		StringBuffer sb = new StringBuffer();

		ArrayList<String> collectionsList = new ArrayList<String>();
		collectionsList.addAll(collectionIds);

		int numberOfCollectionsWithCorridorSupport = 0;

		// fix setting of maximum, see
		// https://github.com/opengeospatial/ets-ogcapi-edr10/issues/133
		int maximum = getMaximum(noOfCollections, collectionsList.size());

		for (int c = 0; c < maximum; c++) {

			String collectionId = collectionsList.get(c);

			boolean supportsCorridorQuery = false;

			String url = JsonUtils.getCollectionURL(rootUri, collectionId);

			Response response = JsonUtils.getCollectionResponse(rootUri, collectionId, ini);
			JsonPath jsonResponse = response.jsonPath();

			HashMap<?, ?> dataQueries = jsonResponse.getJsonObject("data_queries");

			if (dataQueries == null) { // Avoids Nullpointer Exception
				sb.append(" The data_queries element is missing from the collection " + collectionId + " .");
			}
			supportsCorridorQuery = dataQueries != null && dataQueries.containsKey("corridor");

			if (supportsCorridorQuery == false) { // Avoids Nullpointer Exception
				sb.append(" The corridor element is missing from the data_queries element of the collection "
						+ collectionId + " .");
			}

			if (supportsCorridorQuery) {

				numberOfCollectionsWithCorridorSupport++;

				if (jsonResponse.getJsonObject("parameter_names") == null) { // Avoids
																				// Nullpointer
																				// Exception
					sb.append(" The parameter_names element is missing from the collection " + collectionId + " .");
				}

				HashMap parameterNames = jsonResponse.getJsonObject("parameter_names");
				Set parameterNamesSet = parameterNames.keySet();
				Iterator<String> parameterNamesIterator = parameterNamesSet.iterator();

				if (!parameterNamesIterator.hasNext()) {
					continue;
				}
				String sampleParamaterName = parameterNamesIterator.next();

				if (jsonResponse.getList("crs") == null) { // Avoids Nullpointer Exception
					sb.append(" The crs list is missing from the collection " + collectionId + " .");
				}

				List<String> crsList = jsonResponse.getList("crs");

				String supportedCRS = null;
				for (int q = 0; q < crsList.size(); q++) {
					if (crsList.get(q).equals("CRS:84") || crsList.get(q).equals("CRS84")
							|| crsList.get(q).equals("EPSG:4326")
							|| crsList.get(q).contains("www.opengis.net/def/crs/OGC/1.3/CRS84")) {
						supportedCRS = crsList.get(q);
					}
				}
				if (supportedCRS == null) {
					sb.append(collectionId + " does not support CRS84 CRS. \n");

				}

				HashMap corridorQuery = (HashMap) dataQueries.get("corridor");
				HashMap link = (HashMap) corridorQuery.get("link");
				HashMap variables = (HashMap) link.get("variables");
				ArrayList<String> outputFormatList = (ArrayList<String>) variables.get("output_formats");
				String supportedFormat = getSupportedFormat(outputFormatList);

				double medianx = 0d;
				double mediany = 0d;
				double lminx = 0d; // lens
				double lminy = 0d; // lens
				double lmaxx = 0d; // lens
				double lmaxy = 0d; // lens

				HashMap extent = jsonResponse.getJsonObject("extent");
				if (extent.containsKey("spatial")) {

					HashMap spatial = (HashMap) extent.get("spatial");

					if (!spatial.containsKey("bbox")) {
						sb.append("spatial extent of collection " + collectionId + " missing bbox. \n");
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

							lminx = medianx - sizeOfLensSide;
							lminy = mediany - sizeOfLensSide;
							lmaxx = medianx + sizeOfLensSide;
							lmaxy = mediany + sizeOfLensSide;

						}

					}
					else {
						sb.append("bbox of spatial extent of collection" + collectionId
								+ " has fewer than four coordinates. \n");
					}

				}
				else { // if spatial extent is missing
					sb.append(" The spatial extent element is missing from the collection " + collectionId + " .");
				}

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

						sb.append("Temporal extent of collection " + collectionId + " missing interval. \n");
						continue;
					}

					ArrayList intervalEnv = (ArrayList) temporal.get("interval");

					ArrayList interval = null;

					if (intervalEnv.get(0).getClass().toString().contains("java.lang.String")) {
						interval = intervalEnv;
					}
					else if (intervalEnv.get(0).getClass().toString().contains("java.util.ArrayList")) {

						interval = (ArrayList) intervalEnv.get(0);
					}

					if (interval.size() > 1) {

						sampleDateTime = interval.get(0) + "/" + interval.get(1);

					}

				}
				else { // if temporal extent is missing
					sb.append(" The temporal extent element is missing from the collection " + collectionId + " .");
				}

				String constructedURL = url + "/corridor?parameter-name=" + sampleParamaterNameSafe + "&coords="
						+ "LINESTRING(" + lminx + "+" + lminy + "," + medianx + "+" + mediany + "," + lmaxx + "+"
						+ lmaxy + ")" + "&corridor-width=" + corridorWidth + "&width-units=" + corridorWidthUnit + "&f="
						+ supportedFormat + "&datetime=" + sampleDateTime;
				System.out.println("C " + constructedURL);

				String pageContent = null;
				try {
					pageContent = readStringFromURL(constructedURL, 10); // you can use
																			// Integer.MAX_VALUE
																			// for no
																			// limit

				}
				catch (Exception ex) {
					ex.printStackTrace();
					sb.append(ex.getMessage() + " \n");
				}

				if (pageContent != null) {

					if (pageContent.contains("Coverage") || pageContent.contains("Feature")) {
						// do nothing
					}
					else {
						sb.append("Response of Corridor Query on collection " + collectionId
								+ " did not contain a recognised encoding. \n");
					}

				}
				else {
					sb.append("Response of Corridor Query on collection " + collectionId + " was null. \n");
				}

			}
			else { // does not support Corridor queries
				continue;
			}

		}
		if (numberOfCollectionsWithCorridorSupport == 0) {
			sb.append(queryTypeNotSupported + "\n");
		}

		return sb.toString();
	}

}
