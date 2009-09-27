/*******************************************************************************
 *   Gisgraphy Project 
 * 
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 * 
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *   Lesser General Public License for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA
 * 
 *  Copyright 2008  Gisgraphy project 
 *  David Masclet <davidmasclet@gisgraphy.com>
 *  
 *  
 *******************************************************************************/
package com.gisgraphy.test.standalone;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public final class HttpClientThreadT extends Thread {

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
	    HttpClient client = HttpClientT.httpClient;

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
    public HttpClientThreadT(String name) {
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
