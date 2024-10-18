package org.opengis.cite.ogcapiedr10.collections;

import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.testng.annotations.Test;

/**
 * /collections/{collectionId}/
 *
 */
public class TrajectoryCollections extends AbstractFeatures {

	CollectionsTime ct = new CollectionsTime();

	/**
	 * <pre>
	 * Abstract Test 92: Validate that the coords query parameters are constructed correctly. (trajectory)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 92 (/conf/edr/rc-coords-definition)",
			dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryCoordsParameterDefinition(TestPoint testPoint) {

		ct.coordsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 91: Validate that resources can be identified and extracted from a Collection with a Trajectory query using query parameters. (trajectory)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements datetime parameter part of Abstract Test 91 (/conf/trajectory)",
			dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryDateTimeParameterDefinition(TestPoint testPoint) {

		ct.dateTimeParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 94: Validate that the parameter-name query parameters are processed
	 * correctly. (trajectory)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 94 (/conf/collections/REQ_rc-parameter-name-definition)",
			dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryParameternameParameterDefinition(TestPoint testPoint) {

		ct.parameternameParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 96: Validate that the crs query parameters are constructed correctly.
	 * (trajectory)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 96 (/conf/edr/REQ_rc-crs-definition)",
			dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryCrsParameterDefinition(TestPoint testPoint) {

		ct.crsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 98: Validate that the f query parameter is constructed correctly.
	 * (trajectory)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 98 (/conf/edr/rc-f-definition)",
			dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryFParameterDefinition(TestPoint testPoint) {

		ct.fParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 91: Validate that resources can be identified and extracted from a
	 * Collection with a Trajectory query using query parameters.
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements VerticalLevel part of Abstract Test 91 (/conf/trajectory)",
			dataProvider = "trajectoryCollectionPaths", alwaysRun = true)
	public void trajectoryZParameterDefinition(TestPoint testPoint) {

		ct.zParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

}
