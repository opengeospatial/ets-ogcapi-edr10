package org.opengis.cite.ogcapiedr10.corecollections;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertFalse;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.OgcApiEdr10.GEOJSON_MIME_TYPE;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.IUT;
import static org.opengis.cite.ogcapiedr10.openapi3.OpenApiUtils.retrieveParameterByName;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDate;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRange;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.formatDateRangeWithDuration;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.parseTemporalExtent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.geotoolkit.referencing.CRS;
import org.geotoolkit.referencing.crs.DefaultGeographicCRS;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.opengis.cite.ogcapiedr10.util.TemporalExtent;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.reprezen.kaizen.oasparser.model3.OpenApi3;
import com.reprezen.kaizen.oasparser.model3.Operation;
import com.reprezen.kaizen.oasparser.model3.Parameter;
import com.reprezen.kaizen.oasparser.model3.Path;
import com.reprezen.kaizen.oasparser.model3.Schema;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * /collections/{collectionId}/
 *
 */
public class CollectionsResponse extends CommonFixture {

	protected URI iut;
    
    @DataProvider(name = "collectionIDs")
    public Iterator<Object[]> collectionIDs( ITestContext testContext ) {

    
        this.iut = (URI) testContext.getSuite().getAttribute( IUT.getName() );
        
        
        Set<String> collectionTypes = extractCollectionIDs(this.iut);
        List<Object[]> collectionsData = new ArrayList<>();
       
        collectionsData.add(new Object[] {collectionTypes});
        
        return collectionsData.iterator();
        
    }    
	
    private Set<String> extractCollectionIDs(URI rootUri)
    {
    	JsonPath response;
        Response request = init().baseUri( rootUri.toString() ).accept( JSON ).when().request( GET, "/collections" );
        
        request.then().statusCode( 200 );
        response = request.jsonPath();
        List<Object> collections = response.getList( "collections" );
        Set<String> collectionTypes = new HashSet<>();
        for ( Object collection : collections ) {
            Map<String, Object> collectionMap = (Map<String, Object>) collection;
            Object collectionType = collectionMap.get( "id" );
            collectionTypes.add( (String) collectionType );
        }
        
        return collectionTypes;
    	
    }
    
    
    /*
     * Abstract Test 9 /conf/collections/rc-md-op 
     * Abstract Test 10 /conf/rc-md-success
     * Validate that information about the Collections can be retrieved from the expected location.
     * Validate that the Collections content complies with the required structure and contents.
     */
    
	@Test(description = "Implements Abstract Test 9 (/conf/collections/rc-md-op) and Abstract Test 10 (/conf/rc-md-success)", dataProvider = "collectionIDs", alwaysRun = true)
	public void validateRetrievalOfCollections(Object collectionIdentifiers) {

		Set<String> collectionTypes = (Set<String>) collectionIdentifiers;
	
		//The dataProvider validated the response code was 200
		//Therefore if the size of the collection is greater than zero, we know the iut passed the test
		
		assertTrue(collectionTypes.size()>0, "Was not able to validate that information about the Collections was retrieved correctly");

	}    
	
    /*
     * Abstract Test 11 /conf/collections/src-md-op 
     * Abstract Test 12 /conf/collections/src-md-success
     * Validate that the Collection content can be retrieved from the expected location.
     * Validate that the Collections content complies with the required structure and contents.
     */
    
	@Test(description = "Implements Abstract Test 11 (/conf/collections/src-md-op) and Abstract Test 12 (/conf/collections/src-md-success)", dataProvider = "collectionIDs", alwaysRun = true)
	public void validateRetrievalOfEnvironmentalDataCollections(Object collectionIdentifiers) {

		Set<String> collectionTypes = (Set<String>) collectionIdentifiers;
			
		boolean identicalID = false;
		int i=0;
		for(String collection:collectionTypes)
		{
		
			if(i==0)  //We choose to test one
			{
				System.out.println(collection);
		    	JsonPath response;
		        Response request = init().baseUri( rootUri.toString() ).accept( JSON ).when().request( GET, "/collections/"+collection );
		        
		        request.then().statusCode( 200 );
		        response = request.jsonPath();
	    
		        if(response.getString("id").equals(collection)) identicalID = true;
	
				
			}
			i++;
		}
		
		assertTrue(identicalID, "The id of the collection returned by the response did not match the collectionID in the URL");

	} 	
	
   


    	/**
    	 * <pre>
    	 * Abstract Test 8: Validate that all spatial geometries provided through the API are in the CRS84 spatial reference system unless otherwise requested by the client.
    	 * 
    	 * Requirement A.46 G, which is in the Collections Requirements Class, states that "Every Collection within a collections array MUST have a 'crs' parameter which must comply with the requirement '/req/edr/rc-crs'."
    	 * Therefore we can expect that there be a declaration of support for CRS84
    	 * </pre>
    	 *
    	 */

    	@Test(description = "Implements Abstract Test 8 (/conf/core/crs84)")
    	public void collectionsCRS84() {
    		
    		boolean compliesWithCRS84Requirement = true;
    		StringBuffer resultMessage = new StringBuffer();

    		TestPoint testPoint = new TestPoint(rootUri.toString(), "/collections", null);
    		String testPointUri = new UriBuilder(testPoint).buildUrl();
    		Response response = init().baseUri(testPointUri).accept(JSON).when().request(GET);
    		JsonPath jsonPath = response.jsonPath();

    		List<Object> collectionsList = jsonPath.getList("collections");
    		
    		
    		for (int t = 0; t < collectionsList.size(); t++) {
    			boolean supportsCRS84 = false;
    			
    			HashMap collectionMap = (HashMap) collectionsList.get(t);

    			String crsText = null;
    			
    			if(collectionMap.containsKey("crs")) {
    			  crsText = collectionMap.get("crs").toString();
    			}
    			else {
    				
    				if(collectionMap.containsKey("extent")) {
    	  
	    			    HashMap extentMap = (HashMap) collectionMap.get("extent");

    				    HashMap spatialMap = (HashMap) extentMap.get("spatial");

	            		crsText = spatialMap.get("crs").toString();
	            		
	            	
    				}
    		
    			}

    			
    			if (crsText.contains("CRS:84") || crsText.contains("CRS84") || crsText.contains("EPSG:4326") || crsText.contains("WGS84") || crsText.contains("www.opengis.net/def/crs/OGC/1.3/CRS84")){ 
    				compliesWithCRS84Requirement = true;
    			} 
    			else{
    				compliesWithCRS84Requirement = false;
    				resultMessage.append("Collection " + collectionMap.get("id").toString() + " fails. ");
    			}

    		}
   
  

    		org.testng.Assert.assertTrue(compliesWithCRS84Requirement,
    				"Fails Abstract Test 8 because " + resultMessage.toString());

    	}
    	
    	/*
    	 * We keep code here for future use
    	 * 
    	 */
    	
    	private void crsChecking_NotUsed(ArrayList crsList)
    	{
    		boolean compliesWithCRS84Requirement = true;
    		StringBuffer resultMessage = new StringBuffer();
    		boolean supportsCRS84 = false;
    		HashMap collectionMap = null; //(HashMap) collectionsList.get(t);
    		
			//ArrayList crsList = (ArrayList) collectionMap.get("crs");
			
			HashMap crsMap = (HashMap) crsList.get(0);

			CoordinateReferenceSystem source = null;

			try {

				source = CRS.parseWKT(crsMap.get("wkt").toString());
			} catch (Exception e) {

				e.printStackTrace();
			}

			DefaultGeographicCRS crs = (DefaultGeographicCRS) source;

			if (crs.getDatum().getEllipsoid().getName().toString().equals("WGS 84")
					|| crs.getDatum().getEllipsoid().getName().toString().equals("WGS_1984")
					|| crs.getDatum().getEllipsoid().getName().toString().equals("WGS84")) {

				if (source.getCoordinateSystem().getAxis(0).toString().toLowerCase().contains("longitude")
						&& source.getCoordinateSystem().getAxis(1).toString().toLowerCase().contains("latitude")) {

					supportsCRS84 = true;

				}
			}
			if (supportsCRS84 == false) {
				compliesWithCRS84Requirement = false;
				resultMessage.append("Collection " + collectionMap.get("id").toString() + " fails. ");
			}
    	}

    	/**
    	 * <pre>
    	 * Abstract Test 17: Validate that each parameter in a collection is correctly defined.
    	 * </pre>
    	 *
    	 */
    	@Test(description = "Implements Abstract Test 17 (/conf/edr/rc-parameters)")
    	public void collectionsParameters() {
    		boolean compliesWithCollectionParametersRequirement = true;
    		StringBuffer resultMessage = new StringBuffer();

    		TestPoint testPoint = new TestPoint(rootUri.toString(), "/collections", null);
    		String testPointUri = new UriBuilder(testPoint).buildUrl();
    		Response response = init().baseUri(testPointUri).accept(JSON).when().request(GET);
    		JsonPath jsonPath = response.jsonPath();
    		

    		List<Object> collectionsList = jsonPath.getList("collections");
    		
    		boolean atLeastOneCollectionIsEDR = false;

    		for (int t = 0; t < collectionsList.size(); t++) {
    		
    			HashMap collectionMap = (HashMap) collectionsList.get(t);

    			if(collectionMap.containsKey("itemType")) continue; //skip if this is an OGC API Features or OGC API Records collection
    			
    			if(!collectionMap.containsKey("parameter_names")) continue; //skip if this is not an EDR collection
    			else atLeastOneCollectionIsEDR = true;
    			
    			String parameterNameText = collectionMap.get("parameter_names").toString();
    			HashMap parameterNameMap = (HashMap) collectionMap.get("parameter_names");
    			
    			
    			ArrayList<String> parameterNameList = new ArrayList<String>();
    			
    			//Implements Test Method 2: Verify that each parameter property has a unique name (in the collection).
    			for (Object k: parameterNameMap.keySet())
    			{
    				if(parameterNameList.contains(k.toString())) {
    					org.testng.Assert.fail(	"Fails Abstract Test 17 because the parameter " + k.toString()+" is duplicated in collection "+collectionMap.get("id"));		
    				}
    				else {
    					parameterNameList.add(k.toString());
    				}
    			}
    			
    			//Implements Test Method 1: Verify that all parameters listed in a collection have the required properties.
    			//Implements Test Method 3: Verify that each parameter property has a type property.
    			//Implements Test Method 4: Verify that each parameter property has a observedProperty property.
    			for(int i=0; i < parameterNameList.size(); i++)
    			{
    				String parameterName = parameterNameList.get(i);
    	    				
    				HashMap parameterValueMap = (HashMap) parameterNameMap.get(parameterName);
    				boolean hasType = parameterValueMap.containsKey("type"); 
    				boolean hasObservedProperty = parameterValueMap.containsKey("observedProperty"); 
       				
    				org.testng.Assert.assertTrue(hasType,	"Fails Abstract Test 17 because parameter " + parameterName+" in collection "+collectionMap.get("id") + " is missing a 'type' property");
    				org.testng.Assert.assertTrue(hasObservedProperty,	"Fails Abstract Test 17 because parameter " + parameterName+" in collection "+collectionMap.get("id") + " is missing a 'observedProperty' property");				
    			}
    			

    			org.testng.Assert.assertTrue(compliesWithCollectionParametersRequirement,	"Fails Abstract Test 17 because " + resultMessage.toString());
    		}
    	}
        
    	/**
    	 * <pre>
    	 * Abstract Test 13: Validate the extent property if it is present
    	 * Abstract Test 14: Validate that each collection provided by the server is described in the Collections Metadata.
    	 * Abstract Test 15: Validate that each Collection metadata entry in the Collections Metadata document includes all required links (data or collection).
    	 * Abstract Test 16: Validate that the required links are included in the Collections Metadata document (self and alternate).
    	 * </pre>
    	 *
    	 */
    	@Test(description = "Implements Abstract Test 13 (/conf/core/rc-extent), Abstract Test 14 (/conf/edr/rc-collection-info), Abstract Test 15 (/conf/edr/rc-md-query-links), Abstract Test 16 (/conf/core/rc-collection-info-links)")
    	public void verifyCollectionsMetadata() {

    	  StringBuffer resultMessageForSelfAndAlternateLinks = new StringBuffer();
    	  StringBuffer resultMessageForDataOrCollectionLinks = new StringBuffer();
    	  StringBuffer resultMessageForCollectionId = new StringBuffer();
    	  StringBuffer resultMessageForCollectionExtent = new StringBuffer();

    	  TestPoint testPoint = new TestPoint(rootUri.toString(), "/collections", null);
    	  String testPointUri = new UriBuilder(testPoint).buildUrl();
    	  Response response = init().baseUri(testPointUri).accept(JSON).when().request(GET);
    	  JsonPath jsonPath = response.jsonPath();

    	  List<Object> collectionsList = jsonPath.getList("collections");
    	  
    	  

    	  for (int t = 0; t < collectionsList.size(); t++) {
        	boolean collectionHasSelfAndAlternateLinks = false;
        	boolean collectionHasDataOrCollectionLinks = false;
        	boolean collectionHasID = false;
        	boolean collectionHasValidExtent = false;
    	
    	    HashMap collectionMap = (HashMap) collectionsList.get(t);
    	    
    	    if(collectionMap.containsKey("itemType")) continue;

    	    //Test Method 2 of Abstract Test 14: Verify that each collection entry includes an identifier.
    	    if(collectionMap.containsKey("id")==false) resultMessageForCollectionId.append(collectionMap.get("id").toString()+" , ");
    	    
    	    //Test Method 1 of Abstract Test 14: Verify that all collections listed in the collections array of the Collections Metadata exist.
    	    
    	    JsonPath jsonPathCol = getCollectionMetadata(collectionMap.get("id").toString());
        
    	    //Abstract Test 13
    	    if(checkExtentInCollection(jsonPathCol)==false) resultMessageForCollectionExtent.append(collectionMap.get("id").toString()+" , ");
    	    
    	  
    	    //Abstract Test 15
    	    List<Object> linksList1 = jsonPathCol.getList("links"); 
    	    linksList1.addAll((ArrayList) collectionMap.get("links"));  //in some cases, the links shown for the collection at /collections are a subset of those shown in /collections/collectionid
    	    
    	    collectionHasDataOrCollectionLinks = checkDataOrCollectionLinksArePresentInCollectionMetadata(linksList1);
    	    
    
    	    
    	    
    	    //Abstract Test 16    	    
    	    collectionHasSelfAndAlternateLinks = checkSelfAndAlternateLinksArePresentInCollectionMetadata(linksList1);
    	    
    	    
    	    if(collectionHasSelfAndAlternateLinks==false) resultMessageForSelfAndAlternateLinks.append(collectionMap.get("id").toString()+" , ");
    	    if(collectionHasDataOrCollectionLinks==false) resultMessageForDataOrCollectionLinks.append(collectionMap.get("id").toString()+" , ");
    	    
    	  }
    	  
    	  StringBuffer resultMessage = new StringBuffer();
    	  
    	  if(!resultMessageForCollectionExtent.toString().isEmpty())
    	  resultMessage.append("Fails Abstract Test 13 because these collections have invalid or missing extent elements: " + resultMessageForCollectionExtent.toString()+". ");    	  

    	  if(!resultMessageForCollectionId.toString().isEmpty())
    	  resultMessage.append("Fails Abstract Test 14 because these collections are missing 'id' properties: " + resultMessageForCollectionId.toString()+". ");
    	  
    	  if(!resultMessageForDataOrCollectionLinks.toString().isEmpty())
    	  resultMessage.append("Fails Abstract Test 15 because these collections are missing 'data' or 'collection' rel links: " + resultMessageForDataOrCollectionLinks.toString()+". ");
    	  
    	  if(!resultMessageForSelfAndAlternateLinks.toString().isEmpty())
    	  resultMessage.append("Fails Abstract Test 16 because these collections are missing 'self' or 'alternate' rel links: " + resultMessageForSelfAndAlternateLinks.toString()+". ");

    	  
    	  org.testng.Assert.assertTrue(resultMessage.toString().isEmpty(),	resultMessage.toString());
    	}  
    	/*
    	 * check that the collection extent contains bbox
    	 */
    	private boolean checkExtentInCollection(JsonPath jsonPathCol)
    	{
    
    	    HashMap extentMap = (HashMap) jsonPathCol.get("extent");

    	    return extentMap.containsKey("spatial");
    	    
    	    // Deactivated because of changes between v1.0.0 and v1.0.1               	  
    	    //HashMap spatialMap = (HashMap) extentMap.get("spatial"); 	  
    	    //return spatialMap.containsKey("bbox");
       	
    	}    	
    	/*
    	 * check that the collection exists and get links from it
    	 */
    	private JsonPath getCollectionMetadata(String collectionId)
    	{
    		
      	  TestPoint testPoint = new TestPoint(rootUri.toString(), "/collections/"+collectionId, null);
      	  String testPointUri = new UriBuilder(testPoint).buildUrl();
      	  Response response = init().baseUri(testPointUri).accept(JSON).when().request(GET);
      	  JsonPath jsonPath = response.jsonPath();    	
      
       	  return jsonPath;
    	}
    	private boolean checkSelfAndAlternateLinksArePresentInCollectionMetadata(List<Object> linksList)
    	{
    		
    

    		
       	  boolean hasSelfRel = false;
       	  boolean hasAlternateRel = false;
       	    
    	    for(int w = 0; w < linksList.size(); w++)
    	    {
    			HashMap linksMap = (HashMap) linksList.get(w);
    	   
    	        if (linksMap.get("rel").toString().equals("self")) hasSelfRel = true;
    	        if (linksMap.get("rel").toString().equals("alternate")) hasAlternateRel = true;
    	    }
    	    
    	    if (hasSelfRel || hasAlternateRel) return true;
    	    return false;
    	    
    	}
    	private boolean checkDataOrCollectionLinksArePresentInCollectionMetadata(List<Object> linksList)
    	{
    		
          //check for data link deactivated because of https://github.com/opengeospatial/ogcapi-environmental-data-retrieval/issues/371
    		
       	  boolean hasDataRel = false;
       	  boolean hasCollectionRel = false;
       	  

       	    
    	    for(int w = 0; w < linksList.size(); w++)
    	    {
    			HashMap linksMap = (HashMap) linksList.get(w);


    	        if (linksMap.get("rel").toString().equals("data")) hasDataRel = true;
    	        if (linksMap.get("rel").toString().equals("collection")) hasCollectionRel = true;
    	    }
        	    

			
    	    
    	    if (hasDataRel || hasCollectionRel) return true;
    	    return false;
    	    
    	}   	
    	
        //http://localhost/edr/collections/metar_demo/position?coords=POINT(-1.054687%2052.498649)&parameter-name=Metar%20observation&datetime=2021-09-19T01:00Z/2021-09-19T02:00Z&crs=CRS84&f=GeoJSON
}
