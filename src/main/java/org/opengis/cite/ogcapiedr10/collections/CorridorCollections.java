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
public class CorridorCollections extends AbstractFeatures {

	/**
	 * <pre>
	 * Abstract Test 36: Validate that the coords query parameters are constructed correctly. (position)
	 * Abstract Test 52: Validate that the coords query parameters are constructed correctly. (corridor)
	 * Abstract Test 74: Validate that the coords query parameters are constructed correctly. (trajectory)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 36, Abstract Test 52, and Abstract Test 74, meets Requirement 3 /req/edr/coords-definition Parameter coords definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCoordsParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.coordsParameterDefinition(testPoint,apiModel);
	

	}

	/**
	 * <pre>
	 * Abstract Test 40: Validate that the dateTime query parameters are constructed correctly. (position)
	 * Abstract Test 56: Validate that the dateTime query parameters are constructed correctly. (corridor)
	 * Abstract Test 89: Validate that the dateTime query parameters are constructed correctly. (items)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 40, Abstract Test 56, and Abstract Test 89, and meets Requirement 5: /req/core/datetime-parameter Datetime parameter", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorDateTimeParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.dateTimeParameterDefinition(testPoint,apiModel);
		

	}

	/**
	 * Abstract Test 43: Validate that the parameter-name query parameters are
	 * processed correctly. (position) Abstract Test 59: Validate that the
	 * parameter-name query parameters are processed correctly. (corridor) Abstract Test
	 * 77: Validate that the parameter-name query parameters are processed
	 * correctly. (trajectory) Abstract Test 104: Validate that the parameter-name
	 * query parameters are processed correctly. (locations)
	 * 
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 42, Abstract Test 58, Abstract Test 76, and Abstract Test 103 and meets Requirement 6: /req/edr/parameter-name-definition Parameter parametername definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorParameternameParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.parameternameParameterDefinition(testPoint,apiModel);	
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
	@Test(description = "Implements Abstract Test 44, Abstract Test 60, Abstract Test 78, and Abstract Test 105 and Requirement 8: /req/edr/crs-definition Parameter crs definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCrsParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.crsParameterDefinition(testPoint,apiModel);	

	}
	
	

	/**
	 * Abstract Test 46: Validate that the f query parameter is constructed correctly. (position)
	 * Abstract Test 62: Validate that the f query parameter is constructed correctly. (corridor)
	 * Abstract Test 80: Validate that the f query parameter is constructed correctly. (trajectory)
	 * Abstract Test 107: Validate that the f query parameter is constructed correctly. (locations)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 46, Abstract Test 62, Abstract Test 80, Abstract Test 107 and Requirement 10: /req/edr/f-definition Parameter f definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorFParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.fParameterDefinition(testPoint,apiModel);
	}	

	/**
	 * Abstract Test 38: Validate that the vertical level query parameters are constructed correctly. (position)
	 * Abstract Test 54: Validate that the vertical level query parameters are constructed correctly. (corridor)
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 38, Abstract Test 54 and Requirement 12: /req/edr/z-definition Parameter z definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorZParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.zParameterDefinition(testPoint,apiModel);

	}	


	/**
	 * <pre>
	 * Requirement 14: /req/edr/within-definition Parameter within definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 14: /req/edr/within-definition Parameter within definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorWithinParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.withinParameterDefinition(testPoint,apiModel);

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
	@Test(description = "Requirement 16: /req/edr/within-units-definition Parameter withinUnits definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorWithinUnitsParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.withinUnitsParameterDefinition(testPoint,apiModel);

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
	@Test(description = "Requirement 18: /req/edr/min-z-definition Parameter min-z definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorMinzParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.minzParameterDefinition(testPoint,apiModel);	
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
	@Test(description = "Requirement 20: /req/edr/max-z-definition Parameter max-z definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorMaxzParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.maxzParameterDefinition(testPoint,apiModel);
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
	@Test(description = "Requirement 22: /req/edr/resolution-x-definition Parameter resolution-x definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorResolutionxParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.resolutionxParameterDefinition(testPoint,apiModel);

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
	@Test(description = "Requirement 24: /req/edr/resolution-y-definition Parameter resolution-y definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorResolutionyParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.resolutionyParameterDefinition(testPoint,apiModel);

	}

	/**
	 * <pre>
	 * Requirement 26: /req/edr/resolution-z-definition Parameter resolution-z definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 26: /req/edr/resolution-z-definition Parameter resolution-z definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorResolutionzParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.resolutionzParameterDefinition(testPoint,apiModel);
	}

	/**
	 * <pre>
	 * Requirement 28: /req/edr/corridor-height-definition Parameter corridor-height definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 28: /req/edr/corridor-height-definition Parameter corridor-height definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCorridorHeightParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.corridorHeightParameterDefinition(testPoint,apiModel);
	}

	/**
	 * <pre>
	 * Requirement 30: /req/edr/corridor-width-definition Parameter corridor-width definition
	 * </pre>
	 * NOTE: Not referenced by ATS
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement 30: /req/edr/corridor-width-definition Parameter corridor-width definition", dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCorridorWidthParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.corridorWidthParameterDefinition(testPoint,apiModel);
	}
	
	
}

