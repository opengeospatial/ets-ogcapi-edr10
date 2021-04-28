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
     * Abstract Test 17/18 : Verify support for JSON
     * Abstract Test 20 : Verify support for the EDR GeoJSON Schema
     * </pre>
     */
    @Test(description = "Implements Abstract Test 17/18 + 20, Requirement /req/json/definition + /req/edr-geojson/definition")
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
     * Abstract Test 22 : Verify support for CoverageJSON
     * </pre>
     */
    @Test(description = "Implements Abstract Test 22, Requirement /req/covjson/definition")
    public void validateResponseForCoverageJSON() {
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
     * Abstract Test 24 : Verify support for HTML
     * </pre>
     */
    @Test(description = "Implements Abstract Test 24, Requirement /req/html/definition")
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
