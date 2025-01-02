package org.opengis.cite.ogcapiedr10.collections;

import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.testng.annotations.Test;

/**
 * /collections/{collectionId}/
 *
 */
public class PositionCollections extends AbstractFeatures {

	CollectionsTime ct= new CollectionsTime();
	/**
	 * <pre>
	 * Abstract Test 38: Validate that the coords query parameters are constructed correctly. (position)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 36 (/conf/edr/rc-coords-definition)", dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionCoordsParameterDefinition(TestPoint testPoint) {

		
		
		ct.coordsParameterDefinition(testPoint,this.getModel()); System.gc();
	

	}

	/**
	 * <pre>
	 * Abstract Test 42: Validate that the dateTime query parameters are constructed correctly. (position)
	 * </pre>
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 42 (/conf/core/datetime-definition)", dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionDateTimeParameterDefinition(TestPoint testPoint) {

		
		
		ct.dateTimeParameterDefinition(testPoint,this.getModel()); System.gc();
		

	}

	/**
	 * Abstract Test 44: Validate that the parameter-name query parameters are processed correctly. (position) 
	 * 
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 44 (/conf/collections/REQ_rc-parameter-name-definition)", dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionParameternameParameterDefinition(TestPoint testPoint) {

		
		
		ct.parameternameParameterDefinition(testPoint,this.getModel()); System.gc();	
	}

	/**
	 * Abstract Test 46: Validate that the crs query parameters are constructed correctly. (position)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 46 (/conf/edr/REQ_rc-crs-definition)", dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionCrsParameterDefinition(TestPoint testPoint) {

		
		
		ct.crsParameterDefinition(testPoint,this.getModel()); System.gc();	

	}
	
	

	/**
	 * Abstract Test 48: Validate that the f query parameter is constructed correctly. (position)
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Implements Abstract Test 48 (/conf/edr/rc-f-definition)", dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionFParameterDefinition(TestPoint testPoint) {


		
		
		ct.fParameterDefinition(testPoint,this.getModel()); System.gc();
	}	

	/**
	 * Abstract Test 40: Validate that the vertical level query parameters are constructed correctly. (position)
	 *
	 *
	 * @param testPoint the testPoint under test, never <code>null</code>
	 */
	@Test(description = "Abstract Test 40 (/conf/edr/rc-z-definition)", dataProvider = "positionCollectionPaths", alwaysRun = true)
	public void positionZParameterDefinition(TestPoint testPoint) {

		
		
		ct.zParameterDefinition(testPoint,this.getModel()); System.gc();

	}	



	
}

