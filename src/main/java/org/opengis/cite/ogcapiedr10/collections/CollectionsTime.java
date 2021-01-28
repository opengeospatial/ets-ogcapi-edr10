package org.opengis.cite.ogcapiedr10.collections;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertFalse;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
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
 * /collections/{collectionId}/
 *
 */
public class CollectionsTime extends AbstractFeatures {

	/**
	 * <pre>
	 * Abstract Test 36: Validate that the coords query parameters are constructed correctly. (position)
	 * Abstract Test 52: Validate that the coords query parameters are constructed correctly. (area)
	 * Abstract Test 74: Validate that the coords query parameters are constructed correctly. (trajectory)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 36, Abstract Test 52, and Abstract Test 74, meets Requirement 3 /req/edr/coords-definition Parameter coords definition", dataProvider = "collectionPaths", alwaysRun = true)
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

		if(!testPoint.getPath().endsWith("/locations"))
		{
		assertNotNull(coords, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
				+ "'  in OpenAPI document is missing");
		}
		else{ 
			String msg = "Expected property '%s' with value '%s' but was '%s'";
		
		assertEquals(coords.getName(), paramName, String.format(msg, "name", paramName, coords.getName()));
		assertEquals(coords.getIn(), "query", String.format(msg, "in", "query", coords.getIn()));
		assertTrue(isRequired(coords), String.format(msg, "required", "true", coords.getRequired()));
		assertEquals( coords.getStyle(), "form", String.format( msg, "style","form", coords.getStyle() ) );
		assertFalse(isExplode(coords), String.format(msg, "explode", "false", coords.getExplode()));		
		Schema schema = coords.getSchema();
		assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));		
		}

	}

	/**
	 * <pre>
	 * Abstract Test 40: Validate that the dateTime query parameters are constructed correctly. (position)
	 * Abstract Test 56: Validate that the dateTime query parameters are constructed correctly. (area)
	 * Abstract Test 89: Validate that the dateTime query parameters are constructed correctly. (items)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 40, Abstract Test 56, and Abstract Test 89, and meets Requirement 5: /req/core/datetime-parameter Datetime parameter", dataProvider = "collectionPaths", alwaysRun = true)
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
		assertEquals( datetime.getStyle(), "form", String.format( msg, "style","form", datetime.getStyle() ) );
		assertFalse(isExplode(datetime), String.format(msg, "explode", "false", datetime.getExplode()));

		Schema schema = datetime.getSchema();
		assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));
	}

	/**
	 * Abstract Test 43: Validate that the parameter-name query parameters are processed correctly. (position)
	 * Abstract Test 59: Validate that the parameter-name query parameters are processed correctly. (area)
	 * Abstract Test 77: Validate that the parameter-name query parameters are processed correctly. (trajectory)
	 * Abstract Test 104: Validate that the parameter-name query parameters are processed correctly. (locations)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 43, Abstract Test 59, Abstract Test 77, and Abstract Test 104 and meets Requirement 6: /req/edr/parameter-name-definition Parameter parametername definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void parameternameParameterDefinition(TestPoint testPoint) {

		// Based on
		// https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

		Parameter parametername = null;
		String paramName = "parameter-name";

		OpenApi3 model = getApiModel();

		boolean hasParameternameParameter = false;

		for (Path path : model.getPaths().values()) {



			if (testPoint.getPath().equals(path.getPathString())) {

				for (Operation op : path.getOperations().values()) {

					for (Parameter param : op.getParameters()) {
						if (param.getName().equals(paramName))
							parametername = param;
					}
				}
			}
		}

		// ----------------

		String msg = "Expected property '%s' with value '%s' but was '%s'";

		assertNotNull(parametername, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
				+ "'  in OpenAPI document is missing");
		assertEquals(parametername.getName(), paramName, String.format(msg, "name", paramName, parametername.getName()));
		assertEquals(parametername.getIn(), "query", String.format(msg, "in", "query", parametername.getIn()));
		assertFalse(isRequired(parametername), String.format(msg, "required", "false", parametername.getRequired()));		


	}

	/**
	 * Abstract Test 44: Validate that the crs query parameters are constructed correctly.
	 * Abstract Test 60: Validate that the crs query parameters are constructed correctly.
	 * Abstract Test 78: Validate that the crs query parameters are constructed correctly.
	 * Abstract Test 105: Validate that the crs query parameters are constructed correctly.
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 44, Abstract Test 60, Abstract Test 78, and Abstract Test 105 and Requirement 8: /req/edr/crs-definition Parameter crs definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void crsParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter crs = null;
	  String paramName = "crs";

	  OpenApi3 model = getApiModel();

	  boolean hasCrsParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            crs = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(crs, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");

	   String msg = "Expected property '%s' with value '%s' but was '%s'";
		assertEquals(crs.getName(), paramName, String.format(msg, "name", paramName, crs.getName()));
		assertEquals(crs.getIn(), "query", String.format(msg, "in", "query", crs.getIn()));
		assertFalse(isRequired(crs), String.format(msg, "required", "false", crs.getRequired()));
		assertEquals( crs.getStyle(), "form", String.format( msg, "style","form", crs.getStyle() ) );
		assertFalse(isExplode(crs), String.format(msg, "explode", "false", crs.getExplode()));		
		Schema schema = crs.getSchema();
		assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));		  

	}


	/**
	 * Abstract Test 46: Validate that the f query parameter is constructed correctly. (position)
	 * Abstract Test 62: Validate that the f query parameter is constructed correctly. (area)
	 * Abstract Test 80: Validate that the f query parameter is constructed correctly. (trajectory)
	 * Abstract Test 107: Validate that the f query parameter is constructed correctly. (locations)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 46, Abstract Test 62, Abstract Test 80, Abstract Test 107 and Requirement 10: /req/edr/f-definition Parameter f definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void fParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter f = null;
	  String paramName = "f";

	  OpenApi3 model = getApiModel();

	  boolean hasfParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            f
 = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(f
, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(f.getName(), paramName, String.format(msg, "name", paramName, f.getName()));
	  assertEquals(f.getIn(), "query", String.format(msg, "in", "query", f.getIn()));
	  assertFalse(isRequired(f), String.format(msg, "required", "false", f.getRequired()));
	  assertEquals( f.getStyle(), "form", String.format( msg, "style","form", f.getStyle() ) );
	  assertFalse(isExplode(f), String.format(msg, "explode", "false", f.getExplode()));		
	  Schema schema = f.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));	
	}

	/**
	 * Abstract Test 38: Validate that the vertical level query parameters are constructed correctly. (position)
	 * Abstract Test 54: Validate that the vertical level query parameters are constructed correctly. (area)
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 38, Abstract Test 54 and Requirement 12: /req/edr/z-definition Parameter z definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void zParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter z = null;
	  String paramName = "z";

	  OpenApi3 model = getApiModel();

	  boolean haszParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            z = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(z, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(z.getName(), paramName, String.format(msg, "name", paramName, z.getName()));
	  assertEquals(z.getIn(), "query", String.format(msg, "in", "query", z.getIn()));
	  assertTrue(isRequired(z), String.format(msg, "required", "true", z.getRequired()));
	  assertEquals( z.getStyle(), "form", String.format( msg, "style","form", z.getStyle() ) );
	  assertFalse(isExplode(z), String.format(msg, "explode", "false", z.getExplode()));		
	  Schema schema = z.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));	

	}

	/**
	 * <pre>
	 * Requirement 14: /req/edr/within-definition Parameter within definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 14: /req/edr/within-definition Parameter within definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void withinParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter within = null;
	  String paramName = "within";

	  OpenApi3 model = getApiModel();

	  boolean haswithinParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            within = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(within, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(within.getName(), paramName, String.format(msg, "name", paramName, within.getName()));
	  assertEquals(within.getIn(), "query", String.format(msg, "in", "query", within.getIn()));
	  assertFalse(isRequired(within), String.format(msg, "required", "false", within.getRequired()));
	  assertEquals( within.getStyle(), "form", String.format( msg, "style","form", within.getStyle() ) );
	  assertFalse(isExplode(within), String.format(msg, "explode", "false", within.getExplode()));		
	  Schema schema = within.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));	

	}

	/**
	 * <pre>
	 * Requirement 16: /req/edr/within-units-definition Parameter withinUnits
 definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 16: /req/edr/within-units-definition Parameter withinUnits definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void withinUnitsParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter withinUnits = null;
	  String paramName = "within-units";

	  OpenApi3 model = getApiModel();

	  boolean haswithinUnitsParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            withinUnits = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(withinUnits, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(withinUnits.getName(), paramName, String.format(msg, "name", paramName, withinUnits.getName()));
	  assertEquals(withinUnits.getIn(), "query", String.format(msg, "in", "query", withinUnits.getIn()));
	  assertFalse(isRequired(withinUnits), String.format(msg, "required", "false", withinUnits.getRequired()));
	  assertEquals( withinUnits.getStyle(), "form", String.format( msg, "style","form", withinUnits.getStyle() ) );
	  assertFalse(isExplode(withinUnits), String.format(msg, "explode", "false", withinUnits.getExplode()));		
	  Schema schema = withinUnits.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));	

	}


	
	/**
	 * <pre>
	 * Requirement 18: /req/edr/min-z-definition Parameter min-z
 definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 18: /req/edr/min-z-definition Parameter min-z definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void minzParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter minz = null;
	  String paramName = "min-z";

	  OpenApi3 model = getApiModel();

	  boolean hasminzParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            minz = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(minz, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(minz.getName(), paramName, String.format(msg, "name", paramName, minz.getName()));
	  assertEquals(minz.getIn(), "query", String.format(msg, "in", "query", minz.getIn()));
	  assertTrue(isRequired(minz), String.format(msg, "required", "true", minz.getRequired()));
	  assertEquals( minz.getStyle(), "form", String.format( msg, "style","form", minz.getStyle() ) );
	  assertFalse(isExplode(minz), String.format(msg, "explode", "false", minz.getExplode()));		
	  Schema schema = minz.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));		
	}

	/**
	 * <pre>
	 * Requirement 20: /req/edr/max-z-definition Parameter max-z
 definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 20: /req/edr/max-z-definition Parameter max-z definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void maxzParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter maxz = null;
	  String paramName = "max-z";

	  OpenApi3 model = getApiModel();

	  boolean hasmaxzParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            maxz = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(maxz, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(maxz.getName(), paramName, String.format(msg, "name", paramName, maxz.getName()));
	  assertEquals(maxz.getIn(), "query", String.format(msg, "in", "query", maxz.getIn()));
	  assertTrue(isRequired(maxz), String.format(msg, "required", "true", maxz.getRequired()));
	  assertEquals( maxz.getStyle(), "form", String.format( msg, "style","form", maxz.getStyle() ) );
	  assertFalse(isExplode(maxz), String.format(msg, "explode", "false", maxz.getExplode()));		
	  Schema schema = maxz.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));	
	}

	/**
	 * <pre>
	 * Requirement 22: /req/edr/resolution-x-definition Parameter resolution-x
 definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 22: /req/edr/resolution-x-definition Parameter resolution-x definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void resolutionxParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter resolutionx = null;
	  String paramName = "resolution-x";

	  OpenApi3 model = getApiModel();

	  boolean hasresolutionxParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            resolutionx = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(resolutionx, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(resolutionx.getName(), paramName, String.format(msg, "name", paramName, resolutionx.getName()));
	  assertEquals(resolutionx.getIn(), "query", String.format(msg, "in", "query", resolutionx.getIn()));
	  assertFalse(isRequired(resolutionx), String.format(msg, "required", "false", resolutionx.getRequired()));
	  assertEquals( resolutionx.getStyle(), "form", String.format( msg, "style","form", resolutionx.getStyle() ) );
	  assertFalse(isExplode(resolutionx), String.format(msg, "explode", "false", resolutionx.getExplode()));		
	  Schema schema = resolutionx.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));

	}

	/**
	 * <pre>
	 * Requirement 24: /req/edr/resolution-y-definition Parameter resolution-y
 definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 24: /req/edr/resolution-y-definition Parameter resolution-y definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void resolutionyParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter resolutiony = null;
	  String paramName = "resolution-y";

	  OpenApi3 model = getApiModel();

	  boolean hasresolutionyParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            resolutiony
 = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(resolutiony, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(resolutiony.getName(), paramName, String.format(msg, "name", paramName, resolutiony.getName()));
	  assertEquals(resolutiony.getIn(), "query", String.format(msg, "in", "query", resolutiony.getIn()));
	  assertFalse(isRequired(resolutiony), String.format(msg, "required", "false", resolutiony.getRequired()));
	  assertEquals( resolutiony.getStyle(), "form", String.format( msg, "style","form", resolutiony.getStyle() ) );
	  assertFalse(isExplode(resolutiony), String.format(msg, "explode", "false", resolutiony.getExplode()));		
	  Schema schema = resolutiony.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));

	}

	/**
	 * <pre>
	 * Requirement 26: /req/edr/resolution-z-definition Parameter resolution-z definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 26: /req/edr/resolution-z-definition Parameter resolution-z definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void resolutionzParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter resolutionz = null;
	  String paramName = "resolution-z";

	  OpenApi3 model = getApiModel();

	  boolean hasresolutionzParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            resolutionz = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(resolutionz, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(resolutionz.getName(), paramName, String.format(msg, "name", paramName, resolutionz.getName()));
	  assertEquals(resolutionz.getIn(), "query", String.format(msg, "in", "query", resolutionz.getIn()));
	  assertFalse(isRequired(resolutionz), String.format(msg, "required", "false", resolutionz.getRequired()));
	  assertEquals( resolutionz.getStyle(), "form", String.format( msg, "style","form", resolutionz.getStyle() ) );
	  assertFalse(isExplode(resolutionz), String.format(msg, "explode", "false", resolutionz.getExplode()));		
	  Schema schema = resolutionz.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));
	}

	/**
	 * <pre>
	 * Requirement 28: /req/edr/corridor-height-definition Parameter corridor-height definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 28: /req/edr/corridor-height-definition Parameter corridor-height definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void corridorHeightParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter corridorHeight = null;
	  String paramName = "corridor-height";

	  OpenApi3 model = getApiModel();

	  boolean hascorridorHeightParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            corridorHeight = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(corridorHeight, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(corridorHeight.getName(), paramName, String.format(msg, "name", paramName, corridorHeight.getName()));
	  assertEquals(corridorHeight.getIn(), "query", String.format(msg, "in", "query", corridorHeight.getIn()));
	  assertTrue(isRequired(corridorHeight), String.format(msg, "required", "true", corridorHeight.getRequired()));
	  assertEquals( corridorHeight.getStyle(), "form", String.format( msg, "style","form", corridorHeight.getStyle() ) );
	  assertFalse(isExplode(corridorHeight), String.format(msg, "explode", "false", corridorHeight.getExplode()));		
	  Schema schema = corridorHeight.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));	
	}

	/**
	 * <pre>
	 * Requirement 30: /req/edr/corridor-width-definition Parameter corridor-width definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 30: /req/edr/corridor-width-definition Parameter corridor-width definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void corridorWidthParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter corridorWidth = null;
	  String paramName = "corridor-width";

	  OpenApi3 model = getApiModel();

	  boolean hascorridorWidthParameter = false;

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            corridorWidth = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(corridorWidth, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(corridorWidth.getName(), paramName, String.format(msg, "name", paramName, corridorWidth.getName()));
	  assertEquals(corridorWidth.getIn(), "query", String.format(msg, "in", "query", corridorWidth.getIn()));
	  assertTrue(isRequired(corridorWidth), String.format(msg, "required", "true", corridorWidth.getRequired()));
	  assertEquals( corridorWidth.getStyle(), "form", String.format( msg, "style","form", corridorWidth.getStyle() ) );
	  assertFalse(isExplode(corridorWidth), String.format(msg, "explode", "false", corridorWidth.getExplode()));		
	  Schema schema = corridorWidth.getSchema();
	  assertEquals(schema.getType(), "string", String.format(msg, "schema -> type", "string", schema.getType()));	
	}



}
