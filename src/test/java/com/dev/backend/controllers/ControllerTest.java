package com.dev.backend.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;

@SuppressWarnings("deprecation")
public class ControllerTest {
	private static String HOST = "http://localhost:8080/mono/";
	private static String USERNAME="";
	private static String PASSWORD="";
	
	
	DefaultHttpClient httpClient;
	HttpUriRequest request;
	HttpResponse response;
	
	protected void createGET(String URL){
		request = new HttpGet(URL);
	}
	
	protected void testPost(boolean expected, Object expectedContents, String json, String service){
		initHttpClient();
		createPOST(HOST+service, json);
		response = getResponse();
		Assert.assertEquals(response.getStatusLine().getStatusCode() == 200, expected);
		String out = printResponse();
		if(expectedContents instanceof Object[]){
			for(String expectedContent : (String[])expectedContents)
				Assert.assertTrue("Output["+out+"] \n does not contain expected msg :"+expectedContent, out.contains(expectedContent));
		}else{
			Assert.assertTrue("Output["+out+"] \n does not contain expected msg :"+expectedContents, out.contains((String)expectedContents));
		}
		closeHttpClient();
	}
	
	protected void testGet(boolean expected, Object expectedContents, String service){
		initHttpClient();
		createGET(HOST+service);
		response = getResponse();
		String out = printResponse();
		Assert.assertEquals("Respond status code is NOT 200.", response.getStatusLine().getStatusCode() == 200, expected);
		if(expectedContents instanceof Object[]){
			for(String expectedContent : (String[])expectedContents)
				Assert.assertTrue("Output["+out+"] \n does not contain expected msg :"+expectedContent, out.contains(expectedContent));
		}else{
			Assert.assertTrue("Output["+out+"] \n does not contain expected msg :"+expectedContents, out.contains((String)expectedContents));
		}
		closeHttpClient();
	}
	
	protected void createPOST(String URL, String content, String id, String password){
		request = new HttpPost(URL+"?loginId="+id+"&password="+password);
		StringEntity input;
		
		try {
			input = new StringEntity(content);
			input.setContentType("application/json");
			((HttpPost)request).setEntity(input);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	protected void createPOST(String URL, String content){
		createPOST(URL, content, USERNAME, PASSWORD);
	}
	
	protected String printResponse(){
		String output = "", returnStr = "";
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			while ((output = br.readLine()) != null)
				returnStr = returnStr.concat(output);
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnStr;
	}
	
	protected void initHttpClient(){
		httpClient = new DefaultHttpClient();
	}
	
	protected void closeHttpClient(){
		httpClient.getConnectionManager().shutdown();
	}

	protected DefaultHttpClient getHttpClient() {
		return httpClient;
	}

	protected void setHttpClient(DefaultHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	protected HttpUriRequest getRequest() {
		return request;
	}

	protected void setRequest(HttpUriRequest request) {
		this.request = request;
	}

	protected HttpResponse getResponse() {
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	protected void setResponse(HttpResponse response) {
		this.response = response;
	}
}
