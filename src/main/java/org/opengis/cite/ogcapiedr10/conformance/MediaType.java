package org.opengis.cite.ogcapiedr10.conformance;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;


public class MediaType extends CommonFixture {

    /**
     * <pre>
     * Abstract Test 18: Verify support for JSON
     * Abstract Test 19: Verify the content of a JSON document given an input document and schema.
     * </pre>
     */
    @Test(description = "Implements Abstract Test 18 (/conf/json/definition), Abstract Test 19 (/conf/json/content)")
    public void validateResponseForJSON() {
    	
    	
        Response response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET);
        assertTrue(response.getStatusCode() == 200, "Fails Abstract Test 18, JSON response not supported for landing page");
        assertTrue(isJSONValidPerSchema(response.asString(),"/"), "Fails Abstract Test 19, landing page response not valid JSON");
        
        response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET, "/api");
        assertTrue(response.getStatusCode() == 200, "Fails Abstract Test 18, JSON response not supported for api description");
 
        response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET, "/conformance");
        assertTrue(response.getStatusCode() == 200, "Fails Abstract Test 18, JSON response not supported for conformance declaration");
        assertTrue(isJSONValidPerSchema(response.asString(),"/conformance"), "Fails Abstract Test 19, conformance declaration response not valid JSON");
        
        response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET, "/collections");
        assertTrue(response.getStatusCode() == 200, "Fails Abstract Test 18, JSON response not supported for collections metadata");
        //TODO add JSON Schema Validation for /collections
        
        
        
    }
    
    // http://localhost/edr/collections/metar_demo/position?coords=POINT(-1.054687%2052.498649)&parameter-name=Metar%20observation&datetime=2021-09-19T01:00Z/2021-09-19T02:00Z&crs=CRS84&f=GeoJSON
    
   
    
    private boolean isJSONValidPerSchema(String doc, String path) {
    	   
 	   boolean valid = false;
 	   
 	  if(path.equals("/")) {
 	   try (InputStream inputStream = getClass().getResourceAsStream("/org/opengis/cite/ogcapiedr10/jsonschema/landingPage.json")) {
 		   JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
 		   Schema schema = SchemaLoader.load(rawSchema);
 		   schema.validate(new JSONObject(doc)); // throws a ValidationException if this object is invalid
 		   valid = true;
 		 }  
 	   catch(Exception ee)
 	   {
 		   ee.printStackTrace();
 	   }
 	  }
 	  else if (path.equals("/conformance")) {
 	 	   try (InputStream inputStream = getClass().getResourceAsStream("/org/opengis/cite/ogcapiedr10/jsonschema/confClasses.json")) {
 	 		   JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
 	 		   Schema schema = SchemaLoader.load(rawSchema);
 	 		   schema.validate(new JSONObject(doc)); // throws a ValidationException if this object is invalid
 	 		   valid = true;
 	 		 }  
 	 	   catch(Exception ee)
 	 	   {
 	 		   ee.printStackTrace();
 	 	   }		  
 	  }
 	  else {
 		  valid = true;
 	  }
 	   
 	   
 	  return valid;
 	   
 }   
    
    /**
     * <pre>
     * Abstract Test 24: Verify support for CoverageJSON
     * </pre>
     */
    @Test(description = "Implements Abstract Test 24 (/conf/covjson/definition)")
    public void validateResponseForCoverageJSON() {
    	
    	// Example query http://localhost/edr/collections/metar_demo/area?coords=POLYGON((-15.117187%2047.986193,-15.117187%2060.412144,23.90625%2059.710282,16.875%2046.554886,16.875%2046.554886,16.875%2046.554886,-15.117187%2047.986193))&parameter-name=Metar%20observation&datetime=2021-09-16T07:00Z/2021-09-16T10:00Z&crs=CRS84&f=application/prs.coverage+json
    	
    	throw new SkipException("CoverageJSON validation not implemented");
    }
    
    /**
     * <pre>
     * Abstract Test 26 : Verify support for HTML
     * </pre>
     */
    @Test(description = "Implements Abstract Test 26 (/conf/html/definition)")
    public void validateResponseForHTML() {
        Response response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET);
        assertTrue(response.getStatusCode() == 200, "HTML response not supported for landing page");
        
        response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET, "/api");
        assertTrue(response.getStatusCode() == 200, "HTML response not supported for api description");
        
        response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET, "/conformance");
        assertTrue(response.getStatusCode() == 200, "HTML response not supported for conformance declaration");
        
        response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET, "/collections");
        assertTrue(response.getStatusCode() == 200, "HTML response not supported for collections metadata");
    }
}
