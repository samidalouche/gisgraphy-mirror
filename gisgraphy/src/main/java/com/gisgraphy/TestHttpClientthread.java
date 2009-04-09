package com.gisgraphy;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public final class TestHttpClientthread extends Thread {

    static String url;
    private String name;

    @Override
    public void run()

    {
	url = "http://localhost/";

	int counter = 100;
	int batch = 1;

	try {
	    // 1
	    // HttpClient client = new HttpClient();

	    // 2
	    // HttpClient client = new
	    // HttpClient(TestHttpClient.multiThreadedHttpConnectionManager);

	    // 3
	    HttpClient client = TestHttpClient.httpClient;

	    long start = System.currentTimeMillis();
	    for (int x = 0; x < batch; x++) {
		for (int j = 0; j < counter; j++) {
		    doGet(x, j, client);
		}
	    }
	    System.out.println("thread " + this.name + " took " + (System.currentTimeMillis() - start));

	} catch (Exception e) {
	    System.err.println("exception in thread" + this.name + " : " + e);
	    System.exit(-1);
	}
    }

    /**
     * @param name
     */
    public TestHttpClientthread(String name) {
	super(name);
	this.name = name;
    }

    private void doGet(int batch, int counter, HttpClient client) throws IOException {

	GetMethod postMethod = new GetMethod(url);
	try {
	    client.executeMethod(postMethod);
	} finally {
	    postMethod.releaseConnection();
	    // System.out.println("thread "+this.name+" batch " + batch +
	    // " iteration " + counter + " done");
	}
    }

}
