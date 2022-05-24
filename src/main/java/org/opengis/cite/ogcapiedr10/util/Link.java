package org.opengis.cite.ogcapiedr10.util;

public class Link {
    private String  href = null;
    private String  rel  = null;
    private String  type  = null;
    
    public Link(String href, String rel, String type) {
    	this.href = href;
    	this.rel = rel;
    	this.type = type;
    }
    
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
	
}
