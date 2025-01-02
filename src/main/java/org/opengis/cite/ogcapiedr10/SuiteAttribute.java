package org.opengis.cite.ogcapiedr10;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.reprezen.kaizen.oasparser.model3.OpenApi3;

import io.restassured.path.json.JsonPath;
import jakarta.ws.rs.client.Client;

/**
 * An enumerated type defining ISuite attributes that may be set to constitute a shared test fixture.
 */
@SuppressWarnings("rawtypes")
public enum SuiteAttribute {

    /**
     * A client component for interacting with HTTP endpoints.
     */
    CLIENT( "httpClient", Client.class ),

    /**
     * The root URL.
     */
    IUT( "instanceUnderTest", URI.class ),

    /**
     * A File containing the test subject or a description of it.
     */
    TEST_SUBJ_FILE( "testSubjectFile", File.class ),

    /**
     * The number of collections to test.
     */
    NO_OF_COLLECTIONS( "noOfCollections", Integer.class ),
    
    /**
     * Location of API Definition
     */
    API_DEFINITION( "apiDefinition", URI.class ),    

    /**
     * Parsed OpenApi3 document resource /api; Added during execution.
     */
    API_MODEL( "apiModel", OpenApi3.class ),

    /**
     * Requirement classes parsed from /conformance; Added during execution.
     */
    REQUIREMENTCLASSES( "requirementclasses", List.class ),

    /**
     * Parsed collections from resource /collections; Added during execution.
     */
    COLLECTIONS( "collections", List.class ),

    /**
     * LandingPage as JSONPath object
     */
    LANDINGPAGEJSONPATH( "landingpagejsonpath", JsonPath.class ),

    /**
     * Collection names assigned to a feature id parsed from resource /collections/{name}/items; Added during execution.
     */
    FEATUREIDS( "featureIds", Map.class );

    private final Class attrType;

    private final String attrName;

    SuiteAttribute( String attrName, Class attrType ) {
        this.attrName = attrName;
        this.attrType = attrType;
    }

    public Class getType() {
        return attrType;
    }

    public String getName() {
        return attrName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder( attrName );
        sb.append( '(' ).append( attrType.getName() ).append( ')' );
        return sb.toString();
    }
}
