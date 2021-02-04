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
public class CollectionsTime{

    protected boolean isRequired( Parameter param ) {
        return param.getRequired() != null && param.getRequired();
    }

    protected Boolean isExplode( Parameter param ) {
        return param.getExplode() != null && param.getExplode();
    }	
	
	/**
	 * <pre>
	 * Abstract Test 36: Validate that the coords query parameters are constructed correctly. (position)
	 * Abstract Test 52: Validate that the coords query parameters are constructed correctly. (area)
	 * Abstract Test 74: Validate that the coords query parameters are constructed correctly. (trajectory)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */

	public void coordsParameterDefinition(TestPoint testPoint,OpenApi3 model) {

		Parameter coords = null;
		String paramName = "coords";


		boolean hasCoordsParameter = false;

		for (Path path : model.getPaths().values()) {

			if (path.getPathString().endsWith(testPoint.getPath())) {

				for (Operation op : path.getOperations().values()) {
					for (Parameter param : op.getParameters()) {
						if (param.getName().equals(paramName)) {
							coords = param;
						}
					}
				}
			}
		}

		// ----------------

		if (!testPoint.getPath().endsWith("/locations")) {
			assertNotNull(coords, "Required " + paramName + " parameter for collections with path '"
					+ testPoint.getPath() + "'  in OpenAPI document is missing");

			if (coords != null) {
				String msg = "Expected property '%s' with value '%s' but was '%s'";
				assertEquals(coords.getName(), paramName, String.format(msg, "name", paramName, coords.getName()));
				assertEquals(coords.getIn(), "query", String.format(msg, "in", "query", coords.getIn()));
				assertTrue(isRequired(coords), String.format(msg, "required", "true", coords.getRequired()));
				// assertEquals( coords.getStyle(), "form", String.format( msg, "style","form",
				// coords.getStyle() ) ); //SHOULD BE Enabled
				assertFalse(isExplode(coords), String.format(msg, "explode", "false", coords.getExplode()));
				Schema schema = coords.getSchema();
				assertEquals(schema.getType(), "string",
						String.format(msg, "schema -> type", "string", schema.getType()));
			}

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
	public void dateTimeParameterDefinition(TestPoint testPoint,OpenApi3 model) {

		Parameter datetime = null;
		String paramName = "datetime";

	

		for (Path path : model.getPaths().values()) {

			if (path.getPathString().endsWith(testPoint.getPath())) {

				for (Operation op : path.getOperations().values()) {

					for (Parameter param : op.getParameters()) {
						if (param.getName().equals(paramName))
							datetime = param;
					}
				}
			}
		}

		if (datetime != null) {

			String msg = "Expected property '%s' with value '%s' but was '%s'";

			assertEquals(datetime.getName(), paramName, String.format(msg, "name", paramName, datetime.getName()));
			assertEquals(datetime.getIn(), "query", String.format(msg, "in", "query", datetime.getIn()));
			assertFalse(isRequired(datetime), String.format(msg, "required", "false", datetime.getRequired()));
			assertEquals(datetime.getStyle(), "form", String.format(msg, "style", "form", datetime.getStyle()));
			assertFalse(isExplode(datetime), String.format(msg, "explode", "false", datetime.getExplode()));

		}

	}

	/**
	 * Abstract Test 43: Validate that the parameter-name query parameters are
	 * processed correctly. (position) Abstract Test 59: Validate that the
	 * parameter-name query parameters are processed correctly. (area) Abstract Test
	 * 77: Validate that the parameter-name query parameters are processed
	 * correctly. (trajectory) Abstract Test 104: Validate that the parameter-name
	 * query parameters are processed correctly. (locations)
	 * 
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */

	public void parameternameParameterDefinition(TestPoint testPoint, OpenApi3 model) {

		Parameter parametername = null;
		String paramName = "parameter-name";


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

		if (parametername != null) {

			String msg = "Expected property '%s' with value '%s' but was '%s'";

			assertNotNull(parametername, "Required " + paramName + " parameter for collections with path '"
					+ testPoint.getPath() + "'  in OpenAPI document is missing");
			assertEquals(parametername.getName(), paramName,
					String.format(msg, "name", paramName, parametername.getName()));
			assertEquals(parametername.getIn(), "query", String.format(msg, "in", "query", parametername.getIn()));
			assertFalse(isRequired(parametername),
					String.format(msg, "required", "false", parametername.getRequired()));

		}
	}

	/**
	 * Abstract Test 44: Validate that the crs query parameters are constructed
	 * correctly. Abstract Test 60: Validate that the crs query parameters are
	 * constructed correctly. Abstract Test 78: Validate that the crs query
	 * parameters are constructed correctly. Abstract Test 105: Validate that the
	 * crs query parameters are constructed correctly.
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */

	public void crsParameterDefinition(TestPoint testPoint, OpenApi3 model) {

		Parameter crs = null;
		String paramName = "crs";


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

		if (crs != null) {

			String msg = "Expected property '%s' with value '%s' but was '%s'";
			assertEquals(crs.getName(), paramName, String.format(msg, "name", paramName, crs.getName()));
			assertEquals(crs.getIn(), "query", String.format(msg, "in", "query", crs.getIn()));
			assertFalse(isRequired(crs), String.format(msg, "required", "false", crs.getRequired()));
			assertEquals(crs.getStyle(), "form", String.format(msg, "style", "form", crs.getStyle()));
			assertFalse(isExplode(crs), String.format(msg, "explode", "false", crs.getExplode()));
		}

	}
	
	

	/**
	 * Abstract Test 46: Validate that the f query parameter is constructed correctly. (position)
	 * Abstract Test 62: Validate that the f query parameter is constructed correctly. (area)
	 * Abstract Test 80: Validate that the f query parameter is constructed correctly. (trajectory)
	 * Abstract Test 107: Validate that the f query parameter is constructed correctly. (locations)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	public void fParameterDefinition(TestPoint testPoint, OpenApi3 model) {


	  Parameter f = null;
	  String paramName = "f";

		
	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            f = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  if (f != null) {

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(f.getName(), paramName, String.format(msg, "name", paramName, f.getName()));
	  assertEquals(f.getIn(), "query", String.format(msg, "in", "query", f.getIn()));
	  assertFalse(isRequired(f), String.format(msg, "required", "false", f.getRequired()));
	  assertEquals( f.getStyle(), "form", String.format( msg, "style","form", f.getStyle() ) );
	  assertFalse(isExplode(f), String.format(msg, "explode", "false", f.getExplode()));		
	  }	
	}	

	/**
	 * Abstract Test 38: Validate that the vertical level query parameters are constructed correctly. (position)
	 * Abstract Test 54: Validate that the vertical level query parameters are constructed correctly. (area)
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	public void zParameterDefinition(TestPoint testPoint, OpenApi3 model) {

	  Parameter z = null;
	  String paramName = "z";


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


	  if (z != null) {
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(z.getName(), paramName, String.format(msg, "name", paramName, z.getName()));
	  assertEquals(z.getIn(), "query", String.format(msg, "in", "query", z.getIn()));
	  assertTrue(isRequired(z), String.format(msg, "required", "true", z.getRequired()));
	  assertEquals( z.getStyle(), "form", String.format( msg, "style","form", z.getStyle() ) );
	  assertFalse(isExplode(z), String.format(msg, "explode", "false", z.getExplode()));		
	  }	

	}	


	/**
	 * <pre>
	 * Requirement 14: /req/edr/within-definition Parameter within definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	public void withinParameterDefinition(TestPoint testPoint, OpenApi3 model) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter within = null;
	  String paramName = "within";


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


	  if (within != null) {
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(within.getName(), paramName, String.format(msg, "name", paramName, within.getName()));
	  assertEquals(within.getIn(), "query", String.format(msg, "in", "query", within.getIn()));
	  assertFalse(isRequired(within), String.format(msg, "required", "false", within.getRequired()));
	  assertEquals( within.getStyle(), "form", String.format( msg, "style","form", within.getStyle() ) );
	  assertFalse(isExplode(within), String.format(msg, "explode", "false", within.getExplode()));		
	  }	

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
	public void withinUnitsParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter withinUnits = null;
	  String paramName = "within-units";

	  

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


	  if (withinUnits != null) {
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(withinUnits.getName(), paramName, String.format(msg, "name", paramName, withinUnits.getName()));
	  assertEquals(withinUnits.getIn(), "query", String.format(msg, "in", "query", withinUnits.getIn()));
	  assertFalse(isRequired(withinUnits), String.format(msg, "required", "false", withinUnits.getRequired()));
	  assertEquals( withinUnits.getStyle(), "form", String.format( msg, "style","form", withinUnits.getStyle() ) );
	  assertFalse(isExplode(withinUnits), String.format(msg, "explode", "false", withinUnits.getExplode()));		
	  }	

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
	public void minzParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter minz = null;
	  String paramName = "min-z";

	  

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


	  if (minz != null) {

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(minz.getName(), paramName, String.format(msg, "name", paramName, minz.getName()));
	  assertEquals(minz.getIn(), "query", String.format(msg, "in", "query", minz.getIn()));
	  assertTrue(isRequired(minz), String.format(msg, "required", "true", minz.getRequired()));
	  assertEquals( minz.getStyle(), "form", String.format( msg, "style","form", minz.getStyle() ) );
	  assertFalse(isExplode(minz), String.format(msg, "explode", "false", minz.getExplode()));		
	  }		
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
	public void maxzParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter maxz = null;
	  String paramName = "max-z";

	  

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


	  if (maxz != null) {

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(maxz.getName(), paramName, String.format(msg, "name", paramName, maxz.getName()));
	  assertEquals(maxz.getIn(), "query", String.format(msg, "in", "query", maxz.getIn()));
	  assertTrue(isRequired(maxz), String.format(msg, "required", "true", maxz.getRequired()));
	  assertEquals( maxz.getStyle(), "form", String.format( msg, "style","form", maxz.getStyle() ) );
	  assertFalse(isExplode(maxz), String.format(msg, "explode", "false", maxz.getExplode()));		
	  }	
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
	public void resolutionxParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter resolutionx = null;
	  String paramName = "resolution-x";

	  

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


	  if (resolutionx != null) {
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(resolutionx.getName(), paramName, String.format(msg, "name", paramName, resolutionx.getName()));
	  assertEquals(resolutionx.getIn(), "query", String.format(msg, "in", "query", resolutionx.getIn()));
	  assertFalse(isRequired(resolutionx), String.format(msg, "required", "false", resolutionx.getRequired()));
	  assertEquals( resolutionx.getStyle(), "form", String.format( msg, "style","form", resolutionx.getStyle() ) );
	  assertFalse(isExplode(resolutionx), String.format(msg, "explode", "false", resolutionx.getExplode()));		
	  }

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
	public void resolutionyParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter resolutiony = null;
	  String paramName = "resolution-y";

	  

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


	  if (resolutiony != null) {
	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(resolutiony.getName(), paramName, String.format(msg, "name", paramName, resolutiony.getName()));
	  assertEquals(resolutiony.getIn(), "query", String.format(msg, "in", "query", resolutiony.getIn()));
	  assertFalse(isRequired(resolutiony), String.format(msg, "required", "false", resolutiony.getRequired()));
	  assertEquals( resolutiony.getStyle(), "form", String.format( msg, "style","form", resolutiony.getStyle() ) );
	  assertFalse(isExplode(resolutiony), String.format(msg, "explode", "false", resolutiony.getExplode()));		
	  }

	}

	/**
	 * <pre>
	 * Requirement 26: /req/edr/resolution-z-definition Parameter resolution-z definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	public void resolutionzParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter resolutionz = null;
	  String paramName = "resolution-z";

	  

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


	  if (resolutionz != null) {

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(resolutionz.getName(), paramName, String.format(msg, "name", paramName, resolutionz.getName()));
	  assertEquals(resolutionz.getIn(), "query", String.format(msg, "in", "query", resolutionz.getIn()));
	  assertFalse(isRequired(resolutionz), String.format(msg, "required", "false", resolutionz.getRequired()));
	  assertEquals( resolutionz.getStyle(), "form", String.format( msg, "style","form", resolutionz.getStyle() ) );
	  assertFalse(isExplode(resolutionz), String.format(msg, "explode", "false", resolutionz.getExplode()));		
	  }
	}

	/**
	 * <pre>
	 * Requirement 28: /req/edr/corridor-height-definition Parameter corridor-height definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	public void corridorHeightParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter corridorHeight = null;
	  String paramName = "corridor-height";

	  

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


	  if (corridorHeight != null) {

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(corridorHeight.getName(), paramName, String.format(msg, "name", paramName, corridorHeight.getName()));
	  assertEquals(corridorHeight.getIn(), "query", String.format(msg, "in", "query", corridorHeight.getIn()));
	  assertTrue(isRequired(corridorHeight), String.format(msg, "required", "true", corridorHeight.getRequired()));
	  assertEquals( corridorHeight.getStyle(), "form", String.format( msg, "style","form", corridorHeight.getStyle() ) );
	  assertFalse(isExplode(corridorHeight), String.format(msg, "explode", "false", corridorHeight.getExplode()));		
	  }	
	}

	/**
	 * <pre>
	 * Requirement 30: /req/edr/corridor-width-definition Parameter corridor-width definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	public void corridorWidthParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter corridorWidth = null;
	  String paramName = "corridor-width";

	  

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


	  if (corridorWidth != null) {

	  String msg = "Expected property '%s' with value '%s' but was '%s'";
	  assertEquals(corridorWidth.getName(), paramName, String.format(msg, "name", paramName, corridorWidth.getName()));
	  assertEquals(corridorWidth.getIn(), "query", String.format(msg, "in", "query", corridorWidth.getIn()));
	  assertTrue(isRequired(corridorWidth), String.format(msg, "required", "true", corridorWidth.getRequired()));
	  assertEquals( corridorWidth.getStyle(), "form", String.format( msg, "style","form", corridorWidth.getStyle() ) );
	  assertFalse(isExplode(corridorWidth), String.format(msg, "explode", "false", corridorWidth.getExplode()));		
	  }	
	}
	
	
}
