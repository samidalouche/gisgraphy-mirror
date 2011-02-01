package com.gisgraphy.addressparser;

import java.io.OutputStream;

import com.gisgraphy.addressparser.exception.AddressParserException;

public interface IAddressParserService {
    
    /**
     * Execute the query. It is thread safe
     * 
     * @param query
     *                The query to execute
     * @throws AddressParserException
     *                 If an error occurred
     */
    public AddressResultsDto execute(AddressQuery query)
	    throws AddressParserException;
    
    /**
     * Execute the query and serialize the results in an {@link OutputStream}.
     * It is thread safe and can be used in a servlet container
     * 
     * @param query
     *                the query to execute
     * @param outputStream
     *                the outputstream we want to serialize in
     * @throws AddressParserException
     *                 If an error occurred
     */
    public void executeAndSerialize(AddressQuery query, OutputStream outputStream)
	    throws AddressParserException;

    /**
     * Execute the query and returns the results as String. It is thread safe
     * 
     * @param query
     *                the query to execute
     * @throws AddressParserException
     *                 If an error occurred
     */
    public String executeQueryToString(AddressQuery query) throws AddressParserException;

}
