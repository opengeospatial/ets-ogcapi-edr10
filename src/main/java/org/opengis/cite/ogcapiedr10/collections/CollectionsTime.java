package org.opengis.cite.ogcapiedr10.collections;

import com.reprezen.kaizen.oasparser.model3.*;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;

import static org.opengis.cite.ogcapiedr10.EtsAssert.assertFalse;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

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
	 * Abstract Test 38: Validate that the coords query parameters are constructed correctly. (position)
	 * Abstract Test 54: Validate that the coords query parameters are constructed correctly. (area)
	 * Abstract Test 92: Validate that the coords query parameters are constructed correctly. (trajectory)
	 * Abstract Test 116: Validate that the coords query parameters are constructed correctly. (corridor)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */

	public void coordsParameterDefinition(TestPoint testPoint,OpenApi3 model) {
	
 
		Parameter coords = null;
		String paramName = "coords";


		boolean hasCoordsParameter = false;

		for (Path path : model.getPaths().values()) {

			if (path.getPathString().endsWith(testPoint.getPath())) {

				for (Operation op : path.getOperations().values()) {
					for (Parameter param : op.getParameters()) {
						
						if(hasName(param)) {	
							if (param.getName().equals(paramName)) {
								coords = param;
							}
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
				// coords.getStyle() ) ); //TODO SHOULD BE Enabled
				assertFalse(isExplode(coords), String.format(msg, "explode", "false", coords.getExplode()));
				Schema schema = coords.getSchema();
				assertEquals(schema.getType(), "string",
						String.format(msg, "schema -> type", "string", schema.getType()));
			}

		} 

	}      
        
        /**
         * <pre>
         * Abstract Test 70: Validate that the bbox query parameters are constructed correctly. (cube)
         * </pre>
         *
         * @param testPoint
         *            the testPoint under test, never <code>null</code>
         * @param model
         *            api definition, never <code>null</code>
         */

        public void bboxParameterDefinition(TestPoint testPoint,
                OpenApi3 model) {

            Parameter bbox = null;
            String paramName = "bbox";

            for (Path path : model.getPaths().values()) {

                if (path.getPathString().endsWith(testPoint.getPath())) {

                    for (Operation op : path.getOperations().values()) {
                        for (Parameter param : op.getParameters()) {

                            if (hasName(param)) {
                                if (param.getName().equals(paramName)) {
                                    bbox = param;
                                }
                            }
                        }
                    }
                }
            }

            // ----------------

                assertNotNull(bbox, "Required " + paramName + " parameter for collections with path '"
                        + testPoint.getPath() + "'  in OpenAPI document is missing");

                if (bbox != null) {
                    String msg = "Expected property '%s' with value '%s' but was '%s'";
                    assertEquals(bbox.getName(), paramName, String.format(msg, "name", paramName, bbox.getName()));
                    assertEquals(bbox.getIn(), "query", String.format(msg, "in", "query", bbox.getIn()));
                    assertTrue(isRequired(bbox), String.format(msg, "required", "true", bbox.getRequired()));
                    assertFalse(isExplode(bbox), String.format(msg, "explode", "false", bbox.getExplode()));
                    Schema schema = bbox.getSchema();
                    if(schema.hasOneOfSchemas()) {
                        List<Schema> oneOfSchemas = schema.getOneOfSchemas();
                        for (Schema oneOfschema : oneOfSchemas) {
                            assertEquals(oneOfschema.getType(), "array",
                                    String.format(msg, "schema -> type", "array", schema.getType()));
                        }
                    }else {
                        assertEquals(schema.getType(), "array",
                                String.format(msg, "schema -> type", "array", schema.getType()));
                    }
                }

        }

	/**
	 * <pre>
	 * Abstract Test 42: Validate that the dateTime query parameters are constructed correctly. (position)
	 * Abstract Test 58: Validate that the dateTime query parameters are constructed correctly. (area)
	 * Abstract Test 74: Validate that the dateTime query parameters are constructed correctly. (cube)
	 * Abstract Test 139: Validate that the dateTime query parameters are constructed correctly. (instances)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */
	public void dateTimeParameterDefinition(TestPoint testPoint,OpenApi3 model) {
		

		Parameter datetime = null;
		String paramName = "datetime";

	

		for (Path path : model.getPaths().values()) {

			if (path.getPathString().endsWith(testPoint.getPath())) {

				for (Operation op : path.getOperations().values()) {
					
					for (Parameter param : op.getParameters()) {
						
						if(hasName(param)) {
							if (param.getName().equals(paramName))
							{
								  datetime = param;
							}
						}
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
	
	
	public boolean hasName(Parameter parameter)
	{
		boolean result = true;
		
		  try {
			  parameter.getName();  //we do this to check whether there is a name
		  }
		  catch(Exception ee) {
		      result = false;
		  }
		
		return result;
		
	}

	/**
	 * Abstract Test 44: Validate that the parameter-name query parameters are processed correctly. (position) 
	 * Abstract Test 60: Validate that the parameter-name query parameters are processed correctly. (area) 
	 * Abstract Test 76: Validate that the parameter-name query parameters are processed correctly. (cube) 
	 * Abstract Test 94: Validate that the parameter-name query parameters are processed correctly. (trajectory)
	 * Abstract Test 126: Validate that the parameter-name query parameters are processed correctly. (corridor)
	 * Abstract Test 141: Validate that the parameter-name query parameters are processed correctly. (locations)
	 * 
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */

	public void parameternameParameterDefinition(TestPoint testPoint, OpenApi3 model) {
	

		Parameter parametername = null;
		String paramName = "parameter-name";


		for (Path path : model.getPaths().values()) {

			if (testPoint.getPath().equals(path.getPathString())) {

				for (Operation op : path.getOperations().values()) {

					for (Parameter param : op.getParameters()) {
						
						if(hasName(param)) {
						   if (param.getName().equals(paramName))
							{
							  parametername = param;
							}
						}
						
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
	 * Abstract Test 46: Validate that the crs query parameters are constructed correctly. (position)
	 * Abstract Test 62: Validate that the crs query parameters are constructed correctly. (area)
	 * Abstract Test 78: Validate that the crs query parameters are constructed correctly. (cube)
	 * Abstract Test 96: Validate that the crs query parameters are constructed correctly. (trajectory)
	 * Abstract Test 128: Validate that the crs query parameters are constructed correctly. (corridor)
	 * Abstract Test 143: Validate that the crs query parameters are constructed correctly.	(locations) 
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */

	public void crsParameterDefinition(TestPoint testPoint, OpenApi3 model) {
	

		Parameter crs = null;
		String paramName = "crs";


		for (Path path : model.getPaths().values()) {

			if (testPoint.getPath().equals(path.getPathString())) {

				for (Operation op : path.getOperations().values()) {

					for (Parameter param : op.getParameters()) {
						
						if(hasName(param)) {
						
							if (param.getName().equals(paramName))
							{
								crs = param;
							}
						}
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
	 * Abstract Test 48: Validate that the f query parameter is constructed correctly. (position)
	 * Abstract Test 64: Validate that the f query parameter is constructed correctly. (area)
	 * Abstract Test 80: Validate that the f query parameter is constructed correctly. (cube)
	 * Abstract Test 98: Validate that the f query parameter is constructed correctly. (trajectory)
	 * Abstract Test 130: Validate that the f query parameter is constructed correctly. (corridor)
	 * Abstract Test 145: Validate that the f query parameter is constructed correctly. (locations)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */
	public void fParameterDefinition(TestPoint testPoint, OpenApi3 model) {



	  Parameter f = null;
	  String paramName = "f";

		
	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	        	
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  f = param;
		            }
	            }
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
	 * Abstract Test 40 (/conf/edr/rc-z-definition): Validate that the vertical level query parameters are constructed correctly. (position)
	 * Abstract Test 56 (/conf/edr/rc-z-definition): Validate that the vertical level query parameters are constructed correctly. (area)
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */
	public void zParameterDefinition(TestPoint testPoint, OpenApi3 model) {
	

	  Parameter z = null;
	  String paramName = "z";


	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	        	
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  z = param;
		            }
	            }
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
	 * Requirement A.21: /req/edr/within-definition Parameter within definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
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
	        	
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  within = param;
		            }
	            }
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
	 * Requirement A.23: /req/edr/within-units-definition Parameter withinUnits
 definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code> 
	 */
	public void withinUnitsParameterDefinition(TestPoint testPoint, OpenApi3 model) {
	



	  Parameter withinUnits = null;
	  String paramName = "within-units";

	  

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  withinUnits = param;
		            }
	            }
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
	 * Requirement A.25: /req/edr/resolution-x-definition Parameter resolution-x
 definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */
	public void resolutionxParameterDefinition(TestPoint testPoint, OpenApi3 model) {
	



	  Parameter resolutionx = null;
	  String paramName = "resolution-x";

	  

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	        	
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  resolutionx = param;
		            }
	            }
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
	 * Requirement A.28: /req/edr/resolution-y-definition Parameter resolution-y
 definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */
	public void resolutionyParameterDefinition(TestPoint testPoint, OpenApi3 model) {
	



	  Parameter resolutiony = null;
	  String paramName = "resolution-y";

	  

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	        	
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  resolutiony = param;
		            }
	           }
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
	 * Requirement A.30: /req/edr/resolution-z-definition Parameter resolution-z definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */
	public void resolutionzParameterDefinition(TestPoint testPoint, OpenApi3 model) {
	



	  Parameter resolutionz = null;
	  String paramName = "resolution-z";

	  

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  resolutionz = param;
		            }
	        	}
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
	 * Abstract Test 120: Validate that the corridor-height query parameter is constructed correctly.
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */
	public void corridorHeightParameterDefinition(TestPoint testPoint, OpenApi3 model) {




	  Parameter corridorHeight = null;
	  String paramName = "corridor-height";

	  

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  corridorHeight = param;
		            }
	          }
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
	 * Abstract Test 118: Validate that the corridor-width query parameter is constructed correctly. (corridor)
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 * @param model api definition, never <code>null</code>
	 */
	public void corridorWidthParameterDefinition(TestPoint testPoint, OpenApi3 model) {


	  Parameter corridorWidth = null;
	  String paramName = "corridor-width";

	  

	  for (Path path : model.getPaths().values()) {



	    if (testPoint.getPath().equals(path.getPathString())) {

	      for (Operation op : path.getOperations().values()) {

	        for (Parameter param : op.getParameters()) {
	        	if(hasName(param)) {
		            if (param.getName().equals(paramName))
		            {
		        	  corridorWidth = param;
		            }
	           }
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
