package org.opengis.cite.ogcapiedr10.corecollections;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.Method.GET;
import static org.opengis.cite.ogcapiedr10.EtsAssert.assertTrue;
import static org.opengis.cite.ogcapiedr10.SuiteAttribute.IUT;
import static org.opengis.cite.ogcapiedr10.openapi3.OpenApiUtils.retrieveTestPointsForCollectionsMetadata;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.findLinkByRel;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.findLinksWithSupportedMediaTypeByRel;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.findLinksWithoutRelOrType;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.findUnsupportedTypes;
import static org.opengis.cite.ogcapiedr10.util.JsonUtils.linkIncludesRelAndType;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opengis.cite.ogcapiedr10.CommonDataFixture;
import org.opengis.cite.ogcapiedr10.openapi3.TestPoint;
import org.opengis.cite.ogcapiedr10.openapi3.UriBuilder;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 *
 */
public class Collections extends CommonDataFixture {
    private final Map<TestPoint, Response> testPointAndResponses = new HashMap<>();

    private Object[][] testPointsData;

    @DataProvider(name = "collectionsUris")
    public Object[][] collectionsUris(ITestContext testContext) {
        if (this.testPointsData == null) {
            URI iut = (URI) testContext.getSuite().getAttribute(IUT.getName());
            List<TestPoint> testPoints = retrieveTestPointsForCollectionsMetadata(this.getModel(), iut);
            this.testPointsData = new Object[testPoints.size()][];
            int i = 0;
            for (TestPoint testPoint : testPoints) {
                this.testPointsData[i++] = new Object[] { testPoint };
                System.out.println("CHKDA1 "+testPoint.getServerUrl()+ " | "+testPoint.getPath());
            }
        }
        return testPointsData;
    }
    
    /**
     * <pre>
     * Abstract Test 82: Validate that information about the Collections can be retrieved from the expected location.
     * </pre>
     * 
     * @param testPoint the test point to test, never <code>null</code>
     */
    @Test(description = "Implements Abstract Test 82, meets Requirement /req/collections/rc-md-op", dataProvider = "collectionsUris", dependsOnGroups = "conformance")
    public void validateCollectionsMetadataOperation(TestPoint testPoint) {
    	
    	System.out.println("CHKDA2 "+testPoint.getServerUrl()+ " | "+testPoint.getPath());
    	
        String testPointUri = new UriBuilder(testPoint).buildUrl();
        Response response = init().baseUri(testPointUri).accept(JSON).when().request(GET);
        response.then().statusCode(200);
        this.testPointAndResponses.put(testPoint, response);
    }
    
    /**
     * <pre>
     * Abstract Test 15: Validate that each Collection metadata entry in the Collections Metadata document includes all required links.
     * </pre>
     * 
     * @param testPoint
     *            the test point to test, never <code>null</code>
     */
    @Test(description = "Implements Abstract Test 15, meets Requirement /req/edr/rc-md-query-links", dataProvider = "collectionsUris", dependsOnMethods = "validateCollectionsMetadataOperation")
    public void validateCollectionMetadata_Links(TestPoint testPoint) {
        Response response = testPointAndResponses.get(testPoint);
        if (response == null)
            throw new SkipException("Could not find a response for test point " + testPoint);

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> collections = jsonPath.getList("collections");

        for (Map<String, Object> collection : collections) {
            Map<String, Object> map = (Map<String, Object>) collection;
            Object links = map.get("links");

            List<Map<String, Object>> collectionLinks = (List<Map<String, Object>>) links;
            Boolean relationIsDataOrCollection = false;
            Boolean isValidCollection = false;

            for (Map<String, Object> link : collectionLinks) {
                Object rel = link.get("rel");
                if (rel.equals("data") || rel.equals("collection")) {
                    relationIsDataOrCollection = true;
                    if (linkIncludesRelAndType(link)) {
                        isValidCollection = true;
                        break;
                    }
                }
            }
            assertTrue(relationIsDataOrCollection,
                    "Collection must include links for data or collection encodings. Missing links for collection " + map.get("id"));
            assertTrue(isValidCollection,
                    "Links for data or collection encodings must include a rel and type parameter. Missing for collection " + map.get("id"));
        }
    }

    /**
     * <pre>
     * Abstract Test 16: Validate that the required links are included in the Collections Metadata document.
     * </pre>
     * 
     * @param testPoint the test point to test, never <code>null</code>
     */
    @Test(description = "Implements Abstract Test 16, meets Requirement /req/core/rc-collection-info-links", dataProvider = "collectionsUris", dependsOnMethods = "validateCollectionsMetadataOperation")
    public void validateCollectionsMetadata_Links(TestPoint testPoint) {
        Response response = testPointAndResponses.get(testPoint);
        if (response == null)
            throw new SkipException("Could not find a response for test point " + testPoint);

        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> links = jsonPath.getList("links");

        Map<String, Object> linkToSelf = findLinkByRel(links, "self");
        assertNotNull(linkToSelf, "Collections Metadata document must include a link for itself");
        assertTrue(linkIncludesRelAndType(linkToSelf), "Link to itself must include a rel and type parameter");

        List<String> mediaTypesToSupport = createListOfMediaTypesToSupportForOtherResources(linkToSelf);
        List<Map<String, Object>> alternateLinks = findLinksWithSupportedMediaTypeByRel(links, mediaTypesToSupport, "alternate");
        List<String> typesWithoutLink = findUnsupportedTypes(alternateLinks, mediaTypesToSupport);
        
        assertTrue(typesWithoutLink.isEmpty(),
                "Collections Metadata document must include links for alternate encodings. Missing links for types " + typesWithoutLink);

        Set<String> rels = new HashSet<>();
        rels.add("self");
        rels.add("alternate");
        List<String> linksWithoutRelOrType = findLinksWithoutRelOrType(alternateLinks, rels);
        assertTrue(linksWithoutRelOrType.isEmpty(),
                "Links for alternate encodings must include a rel and type parameter. Missing for links " + linksWithoutRelOrType);
    }
}
