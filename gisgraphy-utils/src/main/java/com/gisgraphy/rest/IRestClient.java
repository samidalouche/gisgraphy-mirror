package com.gisgraphy.rest;

import java.io.OutputStream;

import com.gisgraphy.serializer.OutputFormat;

public interface IRestClient {

    public <T> T get(String url, Class<T> classToBeBound, OutputFormat format) throws RestClientException;
    public void get(String url, OutputStream outputStream, OutputFormat format) throws RestClientException;

}