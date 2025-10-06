package org.opengis.cite.ogcapiedr10.collections;

import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.testng.annotations.Test;

/**
 * /collections/{collectionId}/
 *
 */
public class LocationsCollections extends AbstractFeatures {

	CollectionsTime ct = new CollectionsTime();

	/**
	 * <pre>
	 * Abstract Test 139 (v1.0.0),
     * Abstract Test 146 (v1.0.1): Validate that the datetime query parameters are constructed correctly.
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 139/146 (/conf/core/datetime-definition)",
			dataProvider = "locationsCollectionPaths", alwaysRun = true)
	public void locationsDateTimeParameterDefinition(TestPoint testPoint) {

		ct.dateTimeParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 141 (v1.0.0),
     * Abstract Test 148 (v1.0.1): Validate that the parameter-name query parameters are processed
	 * correctly. (locations)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 141/148 (/conf/collections/REQ_rc-parameter-name-definition)",
			dataProvider = "locationsCollectionPaths", alwaysRun = true)
	public void locationsParameternameParameterDefinition(TestPoint testPoint) {

		ct.parameternameParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 143 (v1.0.0),
     * Abstract Test 150 (v1.0.1): Validate that the crs query parameters are constructed
	 * correctly. (locations)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 143/150 (/conf/edr/REQ_rc-crs-definition)",
			dataProvider = "locationsCollectionPaths", alwaysRun = true)
	public void locationsCrsParameterDefinition(TestPoint testPoint) {

		ct.crsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 145 (v1.0.0),
     * Abstract Test 152 (v1.0.1): Validate that the f query parameter is constructed correctly.
	 * (locations)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 145/152 (/conf/edr/rc-f-definition)", dataProvider = "locationsCollectionPaths",
			alwaysRun = true)
	public void locationsFParameterDefinition(TestPoint testPoint) {

		ct.fParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

}
