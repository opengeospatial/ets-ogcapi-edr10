package org.opengis.cite.ogcapiedr10.collections;

import static io.restassured.http.ContentType.JSON;
import org.apache.sis.referencing.crs.DefaultGeographicCRS;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertFalse;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.GEOJSON_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.conformance.RequirementClass.CORE;
import static org.opengis.cite.ogcapiedr10.openapi3.OpenApiUtils.retrieveParameterByName;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDate;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRange;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRangeWithDuration;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.parseTemporalExtent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.sis.referencing.CRS;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.opengis.cite.ogcapiedr10.util.TemporalExtent;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.reprezen.kaizen.oasparser.model3.OpenApi3;
import com.reprezen.kaizen.oasparser.model3.Operation;
import com.reprezen.kaizen.oasparser.model3.Parameter;
import com.reprezen.kaizen.oasparser.model3.Path;
import com.reprezen.kaizen.oasparser.model3.Schema;

import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;

/**
 * /collections/{collectionId}/
 *
 */
public class CollectionsCRS extends CommonFixture {

	/**
	 * <pre>
	 * Abstract Test 8: Validate that all spatial geometries provided through the API are in the CRS84 spatial reference system unless otherwise requested by the client.
	 * </pre>
	 *
	 */
	@Test(description = "Implements Abstract Test 8  Requirement 33 /req/core/crs84")
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

}
