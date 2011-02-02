package com.gisgraphy.addressparser;

import com.gisgraphy.serializer.OutputFormat;

/**
 * @author <a href="mailto:david.masclet@gisgraphy.com">David Masclet</a>
 *
 */
public class AddressQuery {
   
	public final static boolean DEFAULT_INDENTATION = false;

    private String address;
    private String country;
    private OutputFormat outputFormat = OutputFormat.getDefault();
    private String callback;
    private boolean indent = DEFAULT_INDENTATION; 
    
	public OutputFormat getOutputFormat() {
		return outputFormat;
	}
	public void setOutputFormat(OutputFormat outputFormat) {
		this.outputFormat = outputFormat;
	}
	public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getCallback() {
        return callback;
    }
    public void setCallback(String callback) {
        this.callback = callback;
    }
    public boolean isIndent() {
        return indent;
    }
    public void setIndent(boolean indent) {
        this.indent = indent;
    }
    
    @Override
    public String toString() {
	return "address query "+address+" for country "+country+" in "+outputFormat+" format , callback = "+callback+" and indentation="+indent;  
    }
	public AddressQuery(String address, String country) {
		if (address== null || address.trim().isEmpty()){
			throw new IllegalArgumentException("address can not be nul or empty");
		}
		if (country== null || country.trim().isEmpty()){
			throw new IllegalArgumentException("country can not be nul or empty");
		}
		this.address = address;
		this.country = country;
	}
	
	 
   /* public AddressQuery() {
	}*/
}
