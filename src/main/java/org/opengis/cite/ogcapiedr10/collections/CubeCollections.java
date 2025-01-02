package org.opengis.cite.ogcapiedr10.collections;

import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.testng.annotations.Test;

/**
 * /collections/{collectionId}/
 *
 */
public class CubeCollections extends AbstractFeatures {

	CollectionsTime ct= new CollectionsTime();
	/**
	 * <pre>
	 * Abstract Test 70: Validate that the coords query parameters are constructed correctly. (cube)
	 * Note that the test actually tests for the bbox parameter (see https://github.com/opengeospatial/ogcapi-environmental-data-retrieval/issues/423)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 70 (/conf/edr/rc-coords-definition)", dataProvider = "cubeCollectionPaths", alwaysRun = true)
	public void cubeBboxParameterDefinition(TestPoint testPoint) {

		
		
		ct.bboxParameterDefinition(testPoint,this.getModel()); System.gc();
	

	}

	/**
	 * <pre>
	 * Abstract Test 74: Validate that the dateTime query parameters are constructed correctly. (cube)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 74 (/conf/core/datetime-definition)", dataProvider = "cubeCollectionPaths", alwaysRun = true)
	public void cubeDateTimeParameterDefinition(TestPoint testPoint) {

		
		
		ct.dateTimeParameterDefinition(testPoint,this.getModel()); System.gc();
		

	}

	/**
	 * Abstract Test 76: Validate that the parameter-name query parameters are processed correctly. (cube)
	 * 
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements 76 (/conf/collections/REQ_rc-parameter-name-definition)", dataProvider = "cubeCollectionPaths", alwaysRun = true)
	public void cubeParameternameParameterDefinition(TestPoint testPoint) {

		
		
		ct.parameternameParameterDefinition(testPoint,this.getModel()); System.gc();	
	}

	/**
	 * Abstract Test 78: Validate that the crs query parameters are constructed correctly. (cube)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 78 (/conf/edr/REQ_rc-crs-definition)", dataProvider = "cubeCollectionPaths", alwaysRun = true)
	public void cubeCrsParameterDefinition(TestPoint testPoint) {

		
		
		ct.crsParameterDefinition(testPoint,this.getModel()); System.gc();	

	}
	
	

	/**
	 * Abstract Test 80: Validate that the f query parameter is constructed correctly. (cube)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 80 (/conf/edr/rc-f-definition)", dataProvider = "cubeCollectionPaths", alwaysRun = true)
	public void cubeFParameterDefinition(TestPoint testPoint) {


		
		
		ct.fParameterDefinition(testPoint,this.getModel()); System.gc();
	}	

	/**
	 * Abstract Test 69: Validate that resources can be identified and extracted from a Collection with a Cube query using query parameters.
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements VerticalLevel check of Abstract Test 69 (/conf/cube)", dataProvider = "cubeCollectionPaths", alwaysRun = true)
	public void cubeZParameterDefinition(TestPoint testPoint) {

		
		
		ct.zParameterDefinition(testPoint,this.getModel()); System.gc();

	}	


	
	
}

