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
public class TrajectoryCollections extends AbstractFeatures {

	CollectionsTime ct= new CollectionsTime();
	/**
	 * <pre>
	 * Abstract Test 92: Validate that the coords query parameters are constructed correctly. (trajectory)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 92 (/conf/edr/rc-coords-definition)", dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryCoordsParameterDefinition(TestPoint testPoint) {

		
		
		ct.coordsParameterDefinition(testPoint,this.getModel()); System.gc();
	

	}

	/**
	 * <pre>
	 * Abstract Test 91: Validate that resources can be identified and extracted from a Collection with a Trajectory query using query parameters. (trajectory)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements datetime parameter part of Abstract Test 91 (/conf/trajectory)", dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryDateTimeParameterDefinition(TestPoint testPoint) {

		
		
		ct.dateTimeParameterDefinition(testPoint,this.getModel()); System.gc();
		

	}

	/**
	 * Abstract Test 94: Validate that the parameter-name query parameters are processed correctly. (trajectory) 
	 * 
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 94 (/conf/collections/REQ_rc-parameter-name-definition)", dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryParameternameParameterDefinition(TestPoint testPoint) {

		
		
		ct.parameternameParameterDefinition(testPoint,this.getModel()); System.gc();	
	}

	/**
	 * Abstract Test 96: Validate that the crs query parameters are constructed correctly. (trajectory)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 96 (/conf/edr/REQ_rc-crs-definition)", dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryCrsParameterDefinition(TestPoint testPoint) {

		
		
		ct.crsParameterDefinition(testPoint,this.getModel()); System.gc();	

	}
	
	

	/**
	 * Abstract Test 98: Validate that the f query parameter is constructed correctly. (trajectory)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 98 (/conf/edr/rc-f-definition)", dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryFParameterDefinition(TestPoint testPoint) {


		
		
		ct.fParameterDefinition(testPoint,this.getModel()); System.gc();
	}	

	/**
	 * Abstract Test 91: Validate that resources can be identified and extracted from a Collection with a Trajectory query using query parameters.
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements VerticalLevel part of Abstract Test 91 (/conf/trajectory)", dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryZParameterDefinition(TestPoint testPoint) {
		
		ct.zParameterDefinition(testPoint,this.getModel()); System.gc();

	}	



	
}

