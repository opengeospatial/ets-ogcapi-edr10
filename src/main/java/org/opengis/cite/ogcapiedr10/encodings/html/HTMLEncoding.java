package org.opengis.cite.ogcapiedr10.encodings.html;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.IUT;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.opengis.cite.ogcapiedr10.CommonFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class HTMLEncoding extends CommonFixture {

	protected URI iut;
	

	/**
	 * <pre>
	 * Abstract Test 26 : Verify support for HTML
	 * </pre>
	 */
	@Test(description = "Implements Abstract Test 26 (/conf/html/definition)")
	public void validateResponseForHTML() {
		Response response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET);
		assertTrue(response.getStatusCode() == 200, "HTML response not supported for Landing Page");

		response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET, "/api");
		assertTrue(response.getStatusCode() == 200, "HTML response not supported for API description");

		response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET,
				"/conformance");
		assertTrue(response.getStatusCode() == 200, "HTML response not supported for Conformance Declaration");

		response = init().baseUri(rootUri.toString()).accept(ContentType.HTML).when().request(Method.GET,
				"/collections");
		assertTrue(response.getStatusCode() == 200, "HTML response not supported for Collections metadata");
	}
	
}
