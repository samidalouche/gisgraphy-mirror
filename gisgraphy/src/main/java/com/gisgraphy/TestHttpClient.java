package com.gisgraphy;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public final class TestHttpClient {

    static MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager;

    static HttpClient httpClient;

    static String url;

    public static void main(String[] args)

    {
	multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
	multiThreadedHttpConnectionManager.setMaxConnectionsPerHost(30);
	multiThreadedHttpConnectionManager.setMaxTotalConnections(30);

	url = args[0];

	int counter = args.length > 1 ? Integer.parseInt(args[1]) : 100;

	long waitTime = args.length > 2 ? Long.parseLong(args[2]) : 0;

	int batch = args.length > 3 ? Integer.parseInt(args[3]) : 1;

	try {
	    httpClient = new HttpClient(multiThreadedHttpConnectionManager);

	    Thread[] arrayThread = new Thread[counter + 1];
	    for (int j = 0; j < counter; j++) {
		arrayThread[counter] = new TestHttpClientthread(j + "");
		arrayThread[counter].start();
	    }
	    _waitForEver();
	} catch (Exception e)	{
	    e.printStackTrace();
	}

    }

    private static void _waitForEver() throws InterruptedException
    {
	while (true)
	{
	    Thread.sleep(10000);
	    TestHttpClient.multiThreadedHttpConnectionManager.deleteClosedConnections();
	    TestHttpClient.multiThreadedHttpConnectionManager.closeIdleConnections(5);
	    System.out.println("Still alive..");
	}

    }

}
