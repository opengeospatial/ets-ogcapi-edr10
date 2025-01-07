package org.opengis.cite.ogcapiedr10.collections;

import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.testng.annotations.Test;

/**
 * /collections/{collectionId}/
 *
 */
public class AreaCollections extends AbstractFeatures {

	CollectionsTime ct = new CollectionsTime();

	/**
	 * <pre>
	 * Abstract Test 54: Validate that the coords query parameters are constructed correctly. (area)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 54 (/conf/edr/rc-coords-definition)",
			dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaCoordsParameterDefinition(TestPoint testPoint) {

		ct.coordsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 58: Validate that the dateTime query parameters are constructed correctly. (area)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 58 (/conf/core/datetime-definition)",
			dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaDateTimeParameterDefinition(TestPoint testPoint) {

		ct.dateTimeParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 60: Validate that the parameter-name query parameters are processed
	 * correctly. (area)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 60 (/conf/collections/REQ_rc-parameter-name-definition)",
			dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaParameternameParameterDefinition(TestPoint testPoint) {

		ct.parameternameParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 62: Validate that the crs query parameters are constructed correctly.
	 * (area)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 62 (/conf/edr/REQ_rc-crs-definition)",
			dataProvider = "areaCollectionPaths", alwaysRun = true)
	public void areaCrsParameterDefinition(TestPoint testPoint) {

		ct.crsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 64: Validate that the f query parameter is constructed correctly.
	 * (area)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 64 (/conf/edr/rc-f-definition)", dataProvider = "areaCollectionPaths",
			alwaysRun = true)
	public void areaFParameterDefinition(TestPoint testPoint) {

		ct.fParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 56: Validate that the vertical level query parameters are constructed
	 * correctly. (area)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 56 (/conf/edr/rc-z-definition)", dataProvider = "areaCollectionPaths",
			alwaysRun = true)
	public void areaZParameterDefinition(TestPoint testPoint) {

		ct.zParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

}
