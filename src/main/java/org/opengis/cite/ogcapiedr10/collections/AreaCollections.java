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
	 * Abstract Test 38: Validate that the coords query parameters are constructed correctly. (position)
	 * Abstract Test 54: Validate that the coords query parameters are constructed correctly. (area)
	 * Abstract Test 70: Validate that the coords query parameters are constructed correctly. (cube)
	 * Abstract Test 92: Validate that the coords query parameters are constructed correctly. (trajectory)
	 * Abstract Test 116: Validate that the coords query parameters are constructed correctly. (corridor)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 38 (/conf/edr/rc-coords-definition), Abstract Test 54, Abstract Test 70, Abstract Test 92, Abstract Test 116", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaCoordsParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.coordsParameterDefinition(testPoint,apiModel);
	

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
	 */
	@Test(description = "Implements Abstract Test 42 (/conf/core/datetime-definition), Abstract Test 58 (/conf/core/datetime-definition), and Abstract Test 74 (/conf/core/datetime-definition), and Abstract Test 139 (/conf/core/datetime-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaDateTimeParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.dateTimeParameterDefinition(testPoint,apiModel);
		

	}

	/**
	 * Abstract Test 44: Validate that the parameter-name query parameters are processed correctly. (position) 
	 * Abstract Test 60: Validate that the parameter-name query parameters are processed correctly. (area) 
	 * Abstract Test 76: Validate that the parameter-name query parameters are processed correctly. (trajectory) 
	 * Abstract Test 94: Validate that the parameter-name query parameters are processed correctly. (locations)
	 * Abstract Test 126: Validate that the parameter-name query parameters are processed correctly. (locations)
	 * Abstract Test 141: Validate that the parameter-name query parameters are processed correctly. (locations)
	 * 
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 44 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 60 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 76 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 94 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 126 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 141 (/conf/collections/REQ_rc-parameter-name-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaParameternameParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.parameternameParameterDefinition(testPoint,apiModel);	
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
	 */
	@Test(description = "Implements Abstract Test 46 (/conf/edr/REQ_rc-crs-definition), Abstract Test 62 (/conf/edr/REQ_rc-crs-definition), Abstract Test 78 (/conf/edr/REQ_rc-crs-definition), Abstract Test 96 (/conf/edr/REQ_rc-crs-definition), Abstract Test 128 (/conf/edr/REQ_rc-crs-definition), Abstract Test 143 (/conf/edr/REQ_rc-crs-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaCrsParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.crsParameterDefinition(testPoint,apiModel);	

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
	 */
	@Test(description = "Implements Abstract Test 48 (/conf/edr/rc-f-definition),Abstract Test 64 (/conf/edr/rc-f-definition), Abstract Test 80 (/conf/edr/rc-f-definition), Abstract Test 98 (/conf/edr/rc-f-definition), Abstract Test 130 (/conf/edr/rc-f-definition), Abstract Test 145 (/conf/edr/rc-f-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaFParameterDefinition(TestPoint testPoint) {


		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.fParameterDefinition(testPoint,apiModel);
	}	

	/**
	 * Abstract Test 40: Validate that the vertical level query parameters are constructed correctly. (position)
	 * Abstract Test 56: Validate that the vertical level query parameters are constructed correctly. (area)
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 40 (/conf/edr/rc-z-definition), Abstract Test 56 (/conf/edr/rc-z-definition)", dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaZParameterDefinition(TestPoint testPoint) {

		OpenApi3 model = apiModel;
		CollectionsTime ct= new CollectionsTime();
		ct.zParameterDefinition(testPoint,apiModel);

	}	


	
	



	



	
	
}
