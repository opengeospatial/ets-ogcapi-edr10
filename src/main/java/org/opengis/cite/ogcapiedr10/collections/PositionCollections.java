package org.opengis.cite.ogcapiedr10.collections;

import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.testng.annotations.Test;

/**
 * /collections/{collectionId}/
 *
 */
public class PositionCollections extends AbstractFeatures {

	CollectionsTime ct = new CollectionsTime();

	/**
	 * <pre>
	 * Abstract Test 38 (v1.0.0),
     * Abstract Test 45 (v1.0.1): Validate that the coords query parameters are constructed correctly. (position)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 38/45 (/conf/edr/rc-coords-definition)",
			dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionCoordsParameterDefinition(TestPoint testPoint) {

		ct.coordsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 42 (v1.0.0),
     * Abstract Test 49 (v1.0.1): Validate that the dateTime query parameters are constructed correctly. (position)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 42/49 (/conf/core/datetime-definition)",
			dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionDateTimeParameterDefinition(TestPoint testPoint) {

		ct.dateTimeParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 44 (v1.0.0),
     * Abstract Test 51 (v1.0.1): Validate that the parameter-name query parameters are processed
	 * correctly. (position)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 44/51 (/conf/collections/REQ_rc-parameter-name-definition)",
			dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionParameternameParameterDefinition(TestPoint testPoint) {

		ct.parameternameParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 46 (v1.0.0),
     * Abstract Test 53 (v1.0.1): Validate that the crs query parameters are constructed correctly.
	 * (position)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 46/53 (/conf/edr/REQ_rc-crs-definition)",
			dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionCrsParameterDefinition(TestPoint testPoint) {

		ct.crsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 48 (v1.0.0),
     * Abstract Test 55 (v1.0.1): Validate that the f query parameter is constructed correctly.
	 * (position)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 48/55 (/conf/edr/rc-f-definition)",
			dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionFParameterDefinition(TestPoint testPoint) {

		ct.fParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 40 (v1.0.0),
     * Abstract Test 47 (v1.0.1): Validate that the vertical level query parameters are constructed
	 * correctly. (position)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 40/47 (/conf/edr/rc-z-definition)", dataProvider = "positionCollectionPaths",
			alwaysRun = true)
	public void positionZParameterDefinition(TestPoint testPoint) {

		ct.zParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

}
