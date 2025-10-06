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
	 * Abstract Test 38 (v1.0.0),
     * Abstract Test 45 (v1.0.1): Validate that the coords query parameters are constructed correctly. (position)
	 * 
	 * Abstract Test 54 (v1.0.0),
     * Abstract Test 61 (v1.0.1): Validate that the coords query parameters are constructed correctly. (area)
	 * 
	 * Abstract Test 70 (v1.0.0),
     * Abstract Test 77 (v1.0.1): Validate that the coords query parameters are constructed correctly. (cube)
	 * 
	 * Abstract Test 92 (v1.0.0),
     * Abstract Test 99 (v1.0.1): Validate that the coords query parameters are constructed correctly. (trajectory)
	 * 
	 * Abstract Test 116 (v1.0.0),
     * Abstract Test 123 (v1.0.1): Validate that the coords query parameters are constructed correctly. (corridor)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 38/45 (/conf/edr/rc-coords-definition), Abstract Test 54/61, Abstract Test 70/77, Abstract Test 92/99, Abstract Test 116/123",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCoordsParameterDefinition(TestPoint testPoint) {

		ct.coordsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 42 (v1.0.0),
     * Abstract Test 49 (v1.0.1): Validate that the dateTime query parameters are constructed correctly. (position)
	 * 
	 * Abstract Test 58 (v1.0.0),
     * Abstract Test 65 (v1.0.1): Validate that the dateTime query parameters are constructed correctly. (area)
	 * 
	 * Abstract Test 74 (v1.0.0),
     * Abstract Test 81 (v1.0.1): Validate that the dateTime query parameters are constructed correctly. (cube)
	 * 
	 * Abstract Test 91 (v1.0.0),
     * Abstract Test 98 (v1.0.1): Validate that resources can be identified and extracted from a Collection with a Trajectory query using query parameters. (trajectory)
	 * 
	 * Abstract Test 115 (v1.0.0),
     * Abstract Test 122 (v1.0.1): Validate that resources can be identified and extracted from a Collection with a corridor query using query parameters. (corridor)
	 * 
	 * Abstract Test 139 (v1.0.0),
     * Abstract Test 146 (v1.0.1): Validate that the dateTime query parameters are constructed correctly. (locations)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 42/49 (/conf/core/datetime-definition), Abstract Test 58/65 (/conf/core/datetime-definition), and Abstract Test 74/81 (/conf/core/datetime-definition), Abstract Test 115/122 (/conf/corridor), and Abstract Test 139/146 (/conf/core/datetime-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorDateTimeParameterDefinition(TestPoint testPoint) {

		ct.dateTimeParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 44 (v1.0.0),
     * Abstract Test 51 (v1.0.1): Validate that the parameter-name query parameters are processed
	 * correctly. (position) 
	 * 
	 * Abstract Test 60 (v1.0.0),
     * Abstract Test 67 (v1.0.1): Validate that the parameter-name query
	 * parameters are processed correctly. (area) 
	 * 
	 * Abstract Test 76 (v1.0.0),
     * Abstract Test 83 (v1.0.1): Validate that the
	 * parameter-name query parameters are processed correctly. (cube) 
	 * 
	 * Abstract Test 94 (v1.0.0),
     * Abstract Test 101 (v1.0.1): Validate that the parameter-name query parameters are processed correctly.
	 * (trajectory) 
	 * 
	 * Abstract Test 126 (v1.0.0),
     * Abstract Test 133 (v1.0.1): Validate that the parameter-name query parameters
	 * are processed correctly. (corridor) 
	 * 
	 * Abstract Test 141 (v1.0.0),
     * Abstract Test 148 (v1.0.1): Validate that the
	 * parameter-name query parameters are processed correctly. (locations)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 44/51 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 60/67 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 76/83 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 94/101 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 126/133 (/conf/collections/REQ_rc-parameter-name-definition), Abstract Test 141/148 (/conf/collections/REQ_rc-parameter-name-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorParameternameParameterDefinition(TestPoint testPoint) {

		ct.parameternameParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * <pre>
	 * Abstract Test 46 (v1.0.0),
     * Abstract Test 53 (v1.0.1): Validate that the crs query parameters are constructed correctly.
	 * (position) 
	 * 
	 * Abstract Test 62 (v1.0.0),
     * Abstract Test 69 (v1.0.1): Validate that the crs query parameters are constructed
	 * correctly. (area) 
	 * 
	 * Abstract Test 78 (v1.0.0),
     * Abstract Test 85 (v1.0.1): Validate that the crs query parameters are
	 * constructed correctly. (cube) 
	 * 
	 * Abstract Test 96 (v1.0.0),
     * Abstract Test 103 (v1.0.1): Validate that the crs query
	 * parameters are constructed correctly. (trajectory) 
	 * 
	 * Abstract Test 128 (v1.0.0),
     * Abstract Test 135 (v1.0.1): Validate that
	 * the crs query parameters are constructed correctly. (corridor) 
	 * 
	 * Abstract Test 143 (v1.0.0),
     * Abstract Test 150 (v1.0.1): Validate that the crs query parameters are constructed correctly. (locations)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 46/53 (/conf/edr/REQ_rc-crs-definition), Abstract Test 62/69 (/conf/edr/REQ_rc-crs-definition), Abstract Test 78/85 (/conf/edr/REQ_rc-crs-definition), Abstract Test 96/103 (/conf/edr/REQ_rc-crs-definition), Abstract Test 128/135 (/conf/edr/REQ_rc-crs-definition), Abstract Test 143/150 (/conf/edr/REQ_rc-crs-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCrsParameterDefinition(TestPoint testPoint) {

		ct.crsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 48 (v1.0.0),
     * Abstract Test 55 (v1.0.1): Validate that the f query parameter is constructed correctly.
	 * (position) 
	 * 
	 * Abstract Test 64 (v1.0.0),
     * Abstract Test 71 (v1.0.1): Validate that the f query parameter is constructed
	 * correctly. (area) 
	 * 
	 * Abstract Test 80 (v1.0.0),
     * Abstract Test 87 (v1.0.1): Validate that the f query parameter is
	 * constructed correctly. (cube) 
	 * 
	 * Abstract Test 98 (v1.0.0),
     * Abstract Test 105 (v1.0.1): Validate that the f query parameter
	 * is constructed correctly. (trajectory) 
	 * 
	 * Abstract Test 130 (v1.0.0),
     * Abstract Test 137 (v1.0.1): Validate that the f query
	 * parameter is constructed correctly. (corridor) 
	 * 
	 * Abstract Test 145 (v1.0.0),
     * Abstract Test 152 (v1.0.1): Validate that the
	 * f query parameter is constructed correctly. (locations)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 48/55 (/conf/edr/rc-f-definition),Abstract Test 64/71 (/conf/edr/rc-f-definition), Abstract Test 80/87 (/conf/edr/rc-f-definition), Abstract Test 98/105 (/conf/edr/rc-f-definition), Abstract Test 130/137 (/conf/edr/rc-f-definition), Abstract Test 145/152 (/conf/edr/rc-f-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorFParameterDefinition(TestPoint testPoint) {

		ct.fParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * <pre>
	 * Abstract Test 40 (v1.0.0),
     * Abstract Test 47 (v1.0.1): Validate that the vertical level query parameters are constructed
	 * correctly. (position) 
	 * 
	 * Abstract Test 56 (v1.0.0),
     * Abstract Test 63 (v1.0.1): Validate that the vertical level query
	 * parameters are constructed correctly. (area)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 40/47 (/conf/edr/rc-z-definition), Abstract Test 56/63 (/conf/edr/rc-z-definition)",
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
	 * Abstract Test 124 (v1.0.0),
     * Abstract Test 131 (v1.0.1): Validate that the vertical level query parameters are constructed correctly. (corridor)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 124/131: (/conf/edr/REQ_rc-height-units-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorWithinUnitsParameterDefinition(TestPoint testPoint) {

		ct.withinUnitsParameterDefinition(testPoint, this.getModel());
		System.gc();

	}

	/**
	 * <pre>
	 * Abstract Test 120 (v1.0.0),
     * Abstract Test 127 (v1.0.1): Validate that the corridor-height query parameter is constructed correctly.
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 120/127: (/conf/edr/REQ_rc-corridor-height-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCorridorHeightParameterDefinition(TestPoint testPoint) {

		ct.corridorHeightParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

	/**
	 * <pre>
	 * Abstract Test 118 (v1.0.0),
     * Abstract Test 125 (v1.0.1): Validate that the corridor-width query parameter is constructed correctly. (corridor)
	 * </pre>
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 118/125 (/conf/edr/REQ_rc-corridor-width-definition)",
			dataProvider = "corridorCollectionPaths", alwaysRun = true)
	public void corridorCorridorWidthParameterDefinition(TestPoint testPoint) {

		ct.corridorWidthParameterDefinition(testPoint, this.getModel());
		System.gc();
	}

}
