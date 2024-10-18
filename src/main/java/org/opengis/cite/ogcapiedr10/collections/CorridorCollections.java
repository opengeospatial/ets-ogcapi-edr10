package org.opengis.cite.ogcapiedr10.collections;

import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.testng.annotations.Test;

/**
 * /collections/{collectionId}/
 *
 */
public class CorridorCollections extends AbstractFeatures {

	CollectionsTime ct = new CollectionsTime();

	/**
	 * <pre>
	 * Abstract Test 38: Validate that the coords query parameters are constructed correctly. (position)
	 * Abstract Test 54: Validate that the coords query parameters are constructed correctly. (area)
	 * Abstract Test 70: Validate that the coords query parameters are constructed correctly. (cube)
	 * Abstract Test 92: Validate that the coords query parameters are constructed correctly. (trajectory)
	 * Abstract Test 116: Validate that the coords query parameters are constructed correctly. (corridor)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 38 (/conf/edr/rc-coords-definition), Abstract Test 54, Abstract Test 70, Abstract Test 92, Abstract Test 116",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCoordsParameterDefinition(TestPoint testPoint) {

		ct.coordsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 42: Validate that the dateTime query parameters are constructed correctly. (position)
	 * Abstract Test 58: Validate that the dateTime query parameters are constructed correctly. (area)
	 * Abstract Test 74: Validate that the dateTime query parameters are constructed correctly. (cube)
	 * Abstract Test 91: Validate that resources can be identified and extracted from a Collection with a Trajectory query using query parameters. (trajectory)
	 * Abstract Test 115: Validate that resources can be identified and extracted from a Collection with a corridor query using query parameters. (corridor)
	 * Abstract Test 139: Validate that the dateTime query parameters are constructed correctly. (locations)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 42 (/conf/core/datetime-definition), Abstract Test 58 (/conf/core/datetime-definition), and Abstract Test 74 (/conf/core/datetime-definition), Abstract Test 115 (/conf/corridor), and Abstract Test 139 (/conf/core/datetime-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorDateTimeParameterDefinition(TestPoint testPoint) {

		ct.dateTimeParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 44: Validate that the parameter-name query parameters are processed
	 * correctly. (position) Abstract Test 60: Validate that the parameter-name query
	 * parameters are processed correctly. (area) Abstract Test 76: Validate that the
	 * parameter-name query parameters are processed correctly. (cube) Abstract Test 94:
	 * Validate that the parameter-name query parameters are processed correctly.
	 * (trajectory) Abstract Test 126: Validate that the parameter-name query parameters
	 * are processed correctly. (corridor) Abstract Test 141: Validate that the
	 * parameter-name query parameters are processed correctly. (locations)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 44 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 60 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 76 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 94 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 126 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 141 (/conf/collections/REQ_rc-parameter-name-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorParameternameParameterDefinition(TestPoint testPoint) {

		ct.parameternameParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 46: Validate that the crs query parameters are constructed correctly.
	 * (position) Abstract Test 62: Validate that the crs query parameters are constructed
	 * correctly. (area) Abstract Test 78: Validate that the crs query parameters are
	 * constructed correctly. (cube) Abstract Test 96: Validate that the crs query
	 * parameters are constructed correctly. (trajectory) Abstract Test 128: Validate that
	 * the crs query parameters are constructed correctly. (corridor) Abstract Test 143:
	 * Validate that the crs query parameters are constructed correctly. (locations)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 46 (/conf/edr/REQ_rc-crs-definition), Abstract Test 62 (/conf/edr/REQ_rc-crs-definition), Abstract Test 78 (/conf/edr/REQ_rc-crs-definition), Abstract Test 96 (/conf/edr/REQ_rc-crs-definition), Abstract Test 128 (/conf/edr/REQ_rc-crs-definition), Abstract Test 143 (/conf/edr/REQ_rc-crs-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCrsParameterDefinition(TestPoint testPoint) {

		ct.crsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * Abstract Test 48: Validate that the f query parameter is constructed correctly.
	 * (position) Abstract Test 64: Validate that the f query parameter is constructed
	 * correctly. (area) Abstract Test 80: Validate that the f query parameter is
	 * constructed correctly. (cube) Abstract Test 98: Validate that the f query parameter
	 * is constructed correctly. (trajectory) Abstract Test 130: Validate that the f query
	 * parameter is constructed correctly. (corridor) Abstract Test 145: Validate that the
	 * f query parameter is constructed correctly. (locations)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 48 (/conf/edr/rc-f-definition),Abstract Test 64 (/conf/edr/rc-f-definition), Abstract Test 80 (/conf/edr/rc-f-definition), Abstract Test 98 (/conf/edr/rc-f-definition), Abstract Test 130 (/conf/edr/rc-f-definition), Abstract Test 145 (/conf/edr/rc-f-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorFParameterDefinition(TestPoint testPoint) {

		ct.fParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * Abstract Test 40: Validate that the vertical level query parameters are constructed
	 * correctly. (position) Abstract Test 56: Validate that the vertical level query
	 * parameters are constructed correctly. (area)
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 40 (/conf/edr/rc-z-definition), Abstract Test 56 (/conf/edr/rc-z-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorZParameterDefinition(TestPoint testPoint) {

		ct.zParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Requirement A.21: /req/edr/within-definition Parameter within definition
	 * </pre> NOTE: Not referenced by ATS
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Requirement A.21: /req/edr/within-definition Parameter within definition",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorWithinParameterDefinition(TestPoint testPoint) {

		ct.withinParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 124: Validate that the vertical level query parameters are constructed correctly. (corridor)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 124: (/conf/edr/REQ_rc-height-units-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorWithinUnitsParameterDefinition(TestPoint testPoint) {

		ct.withinUnitsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 120: Validate that the corridor-height query parameter is constructed correctly.
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 120: (/conf/edr/REQ_rc-corridor-height-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCorridorHeightParameterDefinition(TestPoint testPoint) {

		ct.corridorHeightParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * <pre>
	 * Abstract Test 118: Validate that the corridor-width query parameter is constructed correctly. (corridor)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 118 (/conf/edr/REQ_rc-corridor-width-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCorridorWidthParameterDefinition(TestPoint testPoint) {

		ct.corridorWidthParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

}
