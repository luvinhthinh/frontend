package com.dev.frontend.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("deprecation")
public class Utils
{
	private static String baseURL = "http://localhost:8080/mono";
	public static final String ERROR = "ERROR";
	
	public static Double parseDouble(String value)
	{
		if(value == null||value.isEmpty())
			return 0D;
		try
		{
			return Double.parseDouble(value);
		}
		catch(Exception e)
		{
			return 0D;
		}
	}
	
	@SuppressWarnings({ "resource" })
	public static String httpGet(String url){
		String line = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet( baseURL + url);
			HttpResponse response = client.execute(request);
			int status = response.getStatusLine().getStatusCode(); 
			
			if (200 <= status && status < 300 ){
				BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
				line = rd.readLine();
			}else{
				line = "{error_code: "+status+"}";
			}
		} catch (IOException e) {
			System.out.println("No Record Found !");
		}
		return line;
	}
	
	@SuppressWarnings({ "resource" })
	public static boolean httpDelete(String url){
		boolean status = false;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpDelete request = new HttpDelete( baseURL + url);
			client.execute(request);
			status = true;
		} catch (IOException e) {
			System.out.println("Can't delete record !");
		}
		return status;
	}
	
	@SuppressWarnings({ "resource" })
	public static String httpPost(String url, String body){
		String line = "";
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost( baseURL + url);
			request.setEntity(new StringEntity(body));
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			HttpResponse response = client.execute(request);
			int status = response.getStatusLine().getStatusCode(); 
			
			if (200 <= status && status < 300 ){
				BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
				line = rd.readLine();
			}else{
				line = ERROR;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object convertToObject(String json, Class valueType){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static List<Object> convertToList(String json, Class valueType){
		ObjectMapper mapper = new ObjectMapper();
		try {
			if("".equals(json)){
				return new ArrayList<Object>();
			}
			return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, valueType));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
