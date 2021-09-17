package org.opengis.cite.ogcapiedr10.conformance;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;

public class MediaType extends CommonFixture {

    /**
     * <pre>
     * Abstract Test 18: Verify support for JSON
     * </pre>
     */
    @Test(description = "Implements Abstract Test 18 (/conf/json/definition)")
    public void validateResponseForJSON() {
    	
    	
        Response response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET);
        assertTrue(response.getStatusCode() == 200, "JSON response not supported for landing page");
        
        response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET, "/api");
        assertTrue(response.getStatusCode() == 200, "JSON response not supported for api description");
        
        response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET, "/conformance");
        assertTrue(response.getStatusCode() == 200, "JSON response not supported for conformance declaration");
        
        response = init().baseUri(rootUri.toString()).accept(ContentType.JSON).when().request(Method.GET, "/collections");
        assertTrue(response.getStatusCode() == 200, "JSON response not supported for collections metadata");
    }
    
    /**
     * <pre>
     * Abstract Test 24: Verify support for CoverageJSON
     * </pre>
     */
    @Test(description = "Implements Abstract Test 24 (/conf/covjson/definition)")
    public void validateResponseForCoverageJSON() {
    	
    	// Example query http://localhost/edr/collections/metar_demo/area?coords=POLYGON((-15.117187%2047.986193,-15.117187%2060.412144,23.90625%2059.710282,16.875%2046.554886,16.875%2046.554886,16.875%2046.554886,-15.117187%2047.986193))&parameter-name=Metar%20observation&datetime=2021-09-16T07:00Z/2021-09-16T10:00Z&crs=CRS84&f=application/prs.coverage+json
    	
        Response response = init().baseUri(rootUri.toString()).accept("application/prs.coverage+json").when().request(Method.GET);
        assertTrue(response.getStatusCode() == 200, "CoverageJSON response not supported for landing page");
        
        response = init().baseUri(rootUri.toString()).accept("application/prs.coverage+json").when().request(Method.GET, "/api");
        assertTrue(response.getStatusCode() == 200, "CoverageJSON response not supported for api description");
        
        response = init().baseUri(rootUri.toString()).accept("application/prs.coverage+json").when().request(Method.GET, "/conformance");
        assertTrue(response.getStatusCode() == 200, "CoverageJSON response not supported for conformance declaration");
        
        response = init().baseUri(rootUri.toString()).accept("application/prs.coverage+json").when().request(Method.GET, "/collections");
        assertTrue(response.getStatusCode() == 200, "CoverageJSON response not supported for collections metadata");
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
