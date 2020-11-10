package org.opengis.cite.ogcapiedr10.collections;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertFalse;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.GEOJSON_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.openapi3.OpenApiUtils.retrieveParameterByName;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDate;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRange;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRangeWithDuration;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.parseTemporalExtent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.opengis.cite.ogcapiedr10.util.TemporalExtent;
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

/**
 * A.2.7. Features {root}/collections/{collectionId}/items - Datetime
 * 
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class CollectionsTime extends AbstractFeatures {




	/**
	 * <pre>
	 * Requirement 3: /req/edr/coords-definition Parameter coords definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 3: /req/edr/coords-definition Parameter coords definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void coordsParameterDefinition(TestPoint testPoint) {

		// Based on
		// https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

		Parameter coords = null;
		String paramName = "coords";

		OpenApi3 model = getApiModel();

		boolean hasCoordsParameter = false;

		for (Path path : model.getPaths().values()) {
			
			
			
			if (testPoint.getPath().equals(path.getPathString())) {
		
				for (Operation op : path.getOperations().values()) {

					for (Parameter param : op.getParameters()) {
						if (param.getName().equals(paramName))
							coords = param;
					}
				}
			}
		}

		// ----------------

		assertNotNull(coords, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
				+ "'  in OpenAPI document is missing");

		String msg = "Expected property '%s' with value '%s' but was '%s'";

		assertEquals(coords.getName(), paramName, String.format(msg, "name", "datetime", coords.getName()));
		assertEquals(coords.getIn(), "query", String.format(msg, "in", "query", coords.getIn()));
		// assertFalse( isRequired( coords ), String.format( msg, "required", "false",
		// coords.getRequired() ) );
		// assertEquals( coords.getStyle(), "form", String.format( msg, "style", "form",
		// coords.getStyle() ) );
		assertFalse(isExplode(coords), String.format(msg, "explode", "false", coords.getExplode()));

		Schema schema = coords.getSchema();
		assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));

	}

	/**
	 * <pre>
	 * Requirement 5: /req/core/rc-datetime-parameter Datetime parameter
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 5: /req/core/rc-datetime-parameter Datetime parameter", dataProvider = "collectionPaths", alwaysRun = true)
	public void dateTimeParameterDefinition(TestPoint testPoint) {

		Parameter datetime = null;
		String paramName = "datetime";

		OpenApi3 model = getApiModel();

		boolean hasCoordsParameter = false;

		for (Path path : model.getPaths().values()) {

			if (testPoint.getPath().equals(path.getPathString())) {

				for (Operation op : path.getOperations().values()) {

					for (Parameter param : op.getParameters()) {
						if (param.getName().equals(paramName))
							datetime = param;
					}
				}
			}
		}

		assertNotNull(datetime, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
				+ "'  in OpenAPI document is missing");

		String msg = "Expected property '%s' with value '%s' but was '%s'";

		assertEquals(datetime.getName(), paramName, String.format(msg, "name", paramName, datetime.getName()));
		assertEquals(datetime.getIn(), "query", String.format(msg, "in", "query", datetime.getIn()));
		assertFalse(isRequired(datetime), String.format(msg, "required", "false", datetime.getRequired()));
		// assertEquals( datetime.getStyle(), "form", String.format( msg, "style",
		// "form", datetime.getStyle() ) );
		assertFalse(isExplode(datetime), String.format(msg, "explode", "false", datetime.getExplode()));

		Schema schema = datetime.getSchema();
		assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));
	}



}
