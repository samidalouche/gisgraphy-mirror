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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public final class HttpClientT {

    static MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager;

    static HttpClient httpClient;

    static String url;

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception

    {
	multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager();
	multiThreadedHttpConnectionManager.setMaxConnectionsPerHost(30);
	multiThreadedHttpConnectionManager.setMaxTotalConnections(30);

	url = args[0];

	int counter = args.length > 1 ? Integer.parseInt(args[1]) : 100;


	    httpClient = new HttpClient(multiThreadedHttpConnectionManager);

	    Thread[] arrayThread = new Thread[counter + 1];
	    for (int j = 0; j < counter; j++) {
		arrayThread[counter] = new HttpClientThreadT(j + "");
		arrayThread[counter].start();
	    }
	    _waitForEver();

    }

    private static void _waitForEver() throws InterruptedException
    {
	while (true)
	{
	    Thread.sleep(10000);
	    HttpClientT.multiThreadedHttpConnectionManager.deleteClosedConnections();
	    HttpClientT.multiThreadedHttpConnectionManager.closeIdleConnections(5);
	    System.out.println("Still alive..");
	}

    }

}
