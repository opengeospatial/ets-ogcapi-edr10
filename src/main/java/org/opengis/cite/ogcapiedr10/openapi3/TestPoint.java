package org.opengis.cite.ogcapiedr10.openapi3;

import com.reprezen.kaizen.oasparser.model3.MediaType;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Encapsulates a Test Point with the UriTemplate and predefined replacements.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public class TestPoint {

    private final String serverUrl;

    private final String path;

    private Map<String, String> predefinedTemplateReplacement;

    private Map<String, MediaType> contentMediaTypes;

    /**
     * Instantiates a TestPoint with UriTemplate but without predefined replacements.
     *
     * @param serverUrl
     *            the serverUrl, never <code>null</code>
     * @param path
     *            the path never, <code>null</code>
     * @param contentMediaTypes
     *            the content media types for the GET operation with response "200", may be <code>null</code>
     */
    public TestPoint( String serverUrl, String path, Map<String, MediaType> contentMediaTypes ) {
        this( serverUrl, path, Collections.emptyMap(), contentMediaTypes );
    }

    /**
     * Instantiates a TestPoint with UriTemplate and predefined replacements.
     *
     * @param serverUrl
     *            the serverUrl, never <code>null</code>
     * @param path
     *            the path, never <code>null</code>
     * @param predefinedTemplateReplacement
     *            a list of predefined replacements never <code>null</code>
     * @param contentMediaTypes
     *            the content media types for the GET operation with response "200", may be <code>null</code>
     */
    public TestPoint( String serverUrl, String path, Map<String, String> predefinedTemplateReplacement,
                      Map<String, MediaType> contentMediaTypes ) {
        this.serverUrl = serverUrl;
        this.path = path;
        this.predefinedTemplateReplacement = Collections.unmodifiableMap( predefinedTemplateReplacement );
        this.contentMediaTypes = contentMediaTypes;
    }

    /**
     *
     * @return the serverUrl never <code>null</code>
     */
    public String getServerUrl() {
        return serverUrl;
    }

    /**
     * @return the path never, <code>null</code>
     */
    public String getPath() {
        return path;
    }

    /**
     * @return an unmodifiable mao with predefined replacements, may be empty but never <code>null</code>
     */
    public Map<String, String> getPredefinedTemplateReplacement() {
        return predefinedTemplateReplacement;
    }

    /**
     * @return the content media types for the GET operation with response "200", may be <code>null</code>
     */
    public Map<String, MediaType> getContentMediaTypes() {
        return contentMediaTypes;
    }

    @Override
    public String toString() {
        return "Server URL: " + serverUrl + " , Path: " + path + ", Replacements: " + predefinedTemplateReplacement;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        TestPoint testPoint = (TestPoint) o;
        return Objects.equals( serverUrl, testPoint.serverUrl )
               && Objects.equals( path, testPoint.predefinedTemplateReplacement )
               && Objects.equals( predefinedTemplateReplacement, testPoint.path )
               && Objects.equals( contentMediaTypes, testPoint.contentMediaTypes );
    }

    @Override
    public int hashCode() {
        return Objects.hash( serverUrl, path, predefinedTemplateReplacement, contentMediaTypes );
    }

}
