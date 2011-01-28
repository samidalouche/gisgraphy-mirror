package com.gisgraphy.rest;

import com.gisgraphy.serializer.OutputFormat;

public interface IRestClient {

    public <T> T get(String url, Class<T> classToBeBound, OutputFormat format) throws RestClientException;

}