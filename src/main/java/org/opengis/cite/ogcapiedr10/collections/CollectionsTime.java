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

		if(!testPoint.getPath().endsWith("/locations"))
		{
		assertNotNull(coords, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
				+ "'  in OpenAPI document is missing");
		}

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

	/**
	 * <pre>
	 * Requirement 6: /req/edr/parameters-definition Parameter parametername definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 6: /req/edr/parameters-definition Parameter parametername definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void parameternameParameterDefinition(TestPoint testPoint) {

		// Based on
		// https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

		Parameter parametername = null;
		String paramName = "parametername";

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

	
		assertNotNull(parametername, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
				+ "'  in OpenAPI document is missing");
		

	}
	
	/**
	 * <pre>
	 * Requirement 8: /req/edr/outputCRS-definition Parameter crs definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 8: /req/edr/parameters-definition Parameter crs definition", dataProvider = "collectionPaths", alwaysRun = true)
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
	  

	}	
	
	/*
	 * 
	 *  START OF GEHGEN
	 * 
	 */
	
	/**
	 * <pre>
	 * Requirement 10: /req/edr/outputFormat-definition Parameter f definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 10: /req/edr/outputFormat-definition Parameter f definition", dataProvider = "collectionPaths", alwaysRun = true)
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
	  

	}	

	/**
	 * <pre>
	 * Requirement 12: /req/edr/z-definition Parameter z
 definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 12: /req/edr/z-definition Parameter z definition", dataProvider = "collectionPaths", alwaysRun = true)
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
	  

	}	

	/**
	 * <pre>
	 * Requirement 14: /req/edr/within-definition Parameter within
 definition
	 * </pre>
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
	  

	}	

	/**
	 * <pre>
	 * Requirement 16: /req/edr/withinUnits-definition Parameter withinUnits
 definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 16: /req/edr/withinUnits-definition Parameter withinUnits definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void withinUnitsParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter withinUnits = null;
	  String paramName = "withinUnits";

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
	  

	}	

	/**
	 * <pre>
	 * Requirement 18: /req/edr/minz-definition Parameter minx
 definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 18: /req/edr/minz-definition Parameter minx definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void minxParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter minx = null;
	  String paramName = "minx";

	  OpenApi3 model = getApiModel();

	  boolean hasminxParameter = false;

	  for (Path path : model.getPaths().values()) {
	    
	    
	    
	    if (testPoint.getPath().equals(path.getPathString())) {
	  
	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	          if (param.getName().equals(paramName))
	            minx = param;
	        }
	      }
	    }
	  }

	  // ----------------


	  assertNotNull(minx, "Required " + paramName + " parameter for collections with path '" + testPoint.getPath()
	      + "'  in OpenAPI document is missing");
	  

	}	

	/**
	 * <pre>
	 * Requirement 20: /req/edr/maxz-definition Parameter maxz
 definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 20: /req/edr/maxz-definition Parameter maxz definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void maxzParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter maxz = null;
	  String paramName = "maxz";

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
	  

	}	

	/**
	 * <pre>
	 * Requirement 22: /req/edr/resolutionx-definition Parameter resolutionx
 definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 22: /req/edr/resolutionx-definition Parameter resolutionx definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void resolutionxParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter resolutionx = null;
	  String paramName = "resolutionx";

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
	  

	}	

	/**
	 * <pre>
	 * Requirement 24: /req/edr/resolutiony-definition Parameter resolutiony
 definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 24: /req/edr/resolutiony-definition Parameter resolutiony definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void resolutionyParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter resolutiony = null;
	  String paramName = "resolutiony";

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
	  

	}	

	/**
	 * <pre>
	 * Requirement 26: /req/edr/resolutionz-definition Parameter resolutionz definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 26: /req/edr/resolutionz-definition Parameter resolutionz definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void resolutionzParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter resolutionz = null;
	  String paramName = "resolutionz";

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
	  

	}	

	/**
	 * <pre>
	 * Requirement 28: /req/edr/corridorHeight-definition Parameter corridorHeight definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 28: /req/edr/corridorHeight-definition Parameter corridorHeight definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void corridorHeightParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter corridorHeight = null;
	  String paramName = "corridorHeight";

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
	  

	}	

	/**
	 * <pre>
	 * Requirement 30: /req/edr/corridorWidth-definition Parameter corridorWidth definition
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 30: /req/edr/corridorWidth-definition Parameter corridorWidth definition", dataProvider = "collectionPaths", alwaysRun = true)
	public void corridorWidthParameterDefinition(TestPoint testPoint) {

	  // Based on
	  // https://github.com/RepreZen/KaiZen-OpenApi-Parser/blob/master/GettingStarted.md

	  Parameter corridorWidth = null;
	  String paramName = "corridorWidth";

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
	  

	}	

	

}
