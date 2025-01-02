package org.opengis.cite.ogcapiedr10.conformance;

/**
 * Updated at the OGC API - Tiles Sprint 2020 by ghobona
 *
 * Encapsulates all known requirement classes.
 *
 * @author <a href="mailto:goltz@lat-lon.de">Lyn Goltz </a>
 */
public enum RequirementClass {

    CORE( "http://www.opengis.net/spec/ogcapi-edr-1/1.0/conf/core" ),
    GEOJSON("http://www.opengis.net/spec/ogcapi-edr-1/1.0/conf/geojson"),
    EDRGEOJSON("http://www.opengis.net/spec/ogcapi-edr-1/1.0/req/edr-geojson");


    private final String conformanceClass;

    private final String mediaTypeFeaturesAndCollections;

    private final String mediaTypeOtherResources;

    RequirementClass( String conformanceClass ) {
        this( conformanceClass, null, null );
    }

    RequirementClass( String conformanceClass, String mediaTypeFeaturesAndCollections, String mediaTypeOtherResources ) {
        this.conformanceClass = conformanceClass;
        this.mediaTypeFeaturesAndCollections = mediaTypeFeaturesAndCollections;
        this.mediaTypeOtherResources = mediaTypeOtherResources;
    }

    /**
     * @return <code>true</code> if the RequirementClass has a media type for features and collections,
     *         <code>true</code> otherwise
     */
    public boolean hasMediaTypeForFeaturesAndCollections() {
        return mediaTypeFeaturesAndCollections != null;
    }

    /**
     * @return media type for features and collections, <code>null</code> if not available
     */
    public String getMediaTypeFeaturesAndCollections() {
        return mediaTypeFeaturesAndCollections;
    }

    /**
     * @return <code>true</code> if the RequirementClass has a media type for other resources,
     *         <code>true</code> otherwise
     */
    public boolean hasMediaTypeForOtherResources() {
        return mediaTypeOtherResources != null;
    }

    /**
     * @return media type of other resources, <code>null</code> if not available
     */
    public String getMediaTypeOtherResources() {
        return mediaTypeOtherResources;
    }

    public String getConformanceClass() {
        return conformanceClass;
    }

    /**
     * @param conformanceClass
     *            the conformance class of the RequirementClass to return.
     * @return the RequirementClass with the passed conformance class, <code>null</code> if RequirementClass exists
     */
    public static RequirementClass byConformanceClass( String conformanceClass ) {
        for ( RequirementClass requirementClass : values() ) {
            if ( requirementClass.conformanceClass.equals( conformanceClass ) )
                return requirementClass;
        }
        return null;
    }

}
