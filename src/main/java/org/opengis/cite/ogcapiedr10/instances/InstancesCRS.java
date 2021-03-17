package org.opengis.cite.ogcapiedr10.instances;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.sis.referencing.CRS;
import org.apache.sis.referencing.crs.DefaultGeographicCRS;
import org.opengis.cite.ogcapiedr10.CommonDataFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.FactoryException;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class InstancesCRS extends CommonDataFixture{
	
	
	
	
	/**
	 * <pre>
	 * Abstract Test 93: Validate that the instances of the Collection content complies with the required structure and contents.
	 * </pre>
	 *
	 */
	@Test(description = "Implements Abstract Test 93  Requirement 33 /req/core/crs84",alwaysRun = true)
	public void instancesCRS84() {

		boolean compliesWithCRS84Requirement = false;
		     StringBuffer resultMessage = new StringBuffer();
		     
	    	 TestPoint testPoint = new TestPoint(rootUri.toString(),"/collections",null);
	         String testPointUri = new UriBuilder( testPoint ).buildUrl();
	         Response response = init().baseUri( testPointUri ).accept( JSON ).when().request( GET );
	         JsonPath jsonPath = response.jsonPath();
	
	         List<Object> collectionsList = jsonPath.getList( "collections" );
	
	         for(int t = 0 ; t < collectionsList.size(); t++)
	         {
	        	 
	        	 //TODO Add a request for each instances resource e.g. https://ogcie.iblsoft.com/edr/collections/GFS_0-isoterm/instances?f=JSON
	        	 
	        	 // TODO Loops through checking the crs of each instances resource.
	        	 
	        	 boolean supportsCRS84 = false;
	        	 HashMap collectionMap = (HashMap) collectionsList.get(t);
	        	 
	        	 String crsText = collectionMap.get("crs").toString();
	        	 ArrayList crsList = (ArrayList) collectionMap.get("crs");	        	 	        	 
	        	 HashMap crsMap = (HashMap) crsList.get(0);
	        	 
	        	 try {
	        		 

					CoordinateReferenceSystem source = CRS.fromWKT(crsMap.get("wkt").toString());
					
					
					DefaultGeographicCRS crs = (DefaultGeographicCRS) source;
			
					 if(crs.getDatum().getEllipsoid().getName().toString().equals("WGS 84") || crs.getDatum().getEllipsoid().getName().toString().equals("WGS_1984") || crs.getDatum().getEllipsoid().getName().toString().equals("WGS84")) {
						
	        		 if(source.getCoordinateSystem().getAxis(0).toString().toLowerCase().contains("longitude") && source.getCoordinateSystem().getAxis(1).toString().toLowerCase().contains("latitude")) {
	        			
	        				 supportsCRS84 = true;	  
	        			 
	        		 }
	        	 }
	        	 if(supportsCRS84==false) {
	        		 compliesWithCRS84Requirement = false;
	        		 resultMessage.append("Collection "+collectionMap.get("id").toString()+" fails. ");
	        	 }
					
				} catch (NoSuchAuthorityCodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FactoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 
	       
	        	 
	         }	            
	         
	 
	         org.testng.Assert.assertTrue( compliesWithCRS84Requirement,   "Fails Abstract Test 93 because "+resultMessage.toString() );


	}	

}
