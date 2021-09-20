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
public class AreaCollections extends AbstractFeatures {

	/**
	 * <pre>
	 * Abstract Test 54: Validate that the coords query parameters are constructed correctly. (area)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 54 (/conf/edr/rc-coords-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaCoordsParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.coordsParameterDefinition(testPoint,apiModel);
	

	}

	/**
	 * <pre>
	 * Abstract Test 58: Validate that the dateTime query parameters are constructed correctly. (area)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 58 (/conf/core/datetime-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaDateTimeParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.dateTimeParameterDefinition(testPoint,apiModel);
		

	}

	/**
	 * Abstract Test 60: Validate that the parameter-name query parameters are processed correctly. (area) 
	 * 
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 60 (/conf/collections/REQ_rc-parameter-name-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaParameternameParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.parameternameParameterDefinition(testPoint,apiModel);	
	}

	/**
	 * Abstract Test 62: Validate that the crs query parameters are constructed correctly. (area)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 62 (/conf/edr/REQ_rc-crs-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaCrsParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.crsParameterDefinition(testPoint,apiModel);	

	}
	
	

	/**
	 * Abstract Test 64: Validate that the f query parameter is constructed correctly. (area)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 64 (/conf/edr/rc-f-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaFParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.fParameterDefinition(testPoint,apiModel);
	}	

	/**
	 * Abstract Test 56: Validate that the vertical level query parameters are constructed correctly. (area)
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 56 (/conf/edr/rc-z-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaZParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.zParameterDefinition(testPoint,apiModel);

	}	


	
	



	



	
	
}
