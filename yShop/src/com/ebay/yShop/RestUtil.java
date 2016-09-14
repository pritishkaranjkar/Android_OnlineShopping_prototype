package com.ebay.yShop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class RestUtil {
	
	//testfdaf
    public static String getCategoryUri(String occasion, String keywords, String categoryId)
    {
    	try
    	{
    			String query = "(" + occasion + "," + keywords + ")";

	    		String response = callFindApi(query, categoryId, 1);
	    		
				JSONObject root = new JSONObject(response);
				
				
				JSONArray items = root.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
				
					JSONObject item = items.getJSONObject(0);
					String imageUri = item.getJSONArray("galleryURL").getString(0);
					
					System.out.println(imageUri);
					
					return imageUri;
	
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		
    	}
    	
    	return null;
    	
    }
    
	public static ArrayList<Item> getItemsForCategory(String occasion, String keywords, String categoryId)
	{
		try
		{
			ArrayList<Item> searchItems = new ArrayList<Item>();
			
			String query = "(" + occasion + "," + keywords + ")";
			
			String response = callFindApi(query, categoryId, 20);
					
			JSONObject root = new JSONObject(response);
			
			
			JSONArray items = root.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");
			
			for(int i = 0; i < items.length(); i ++)
			{
				Item searchItem = new Item();
				
				JSONObject item = items.getJSONObject(i);
				
				searchItem.title = item.getJSONArray("title").getString(0);
				searchItem.imageUri = item.getJSONArray("galleryURL").getString(0);
				searchItem.viewItemUri = item.getJSONArray("viewItemURL").getString(0);
				searchItem.price = item.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__");
						
				searchItems.add(searchItem);
			}
			
			return searchItems;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public  static String callFindApi(String keywords, String categoryId, int entries){
		
		try
		{
			
			String encodedKeywords = URLEncoder.encode(keywords, "UTF-8");
			
		// TODO Auto-generated method stub
		HttpClient client = new DefaultHttpClient();
		
		String getRequest = "http://svcs.ebay.com/services/search/FindingService/v1?" +
				"SECURITY-APPNAME=" + "DSSTeam9e-eb31-4c40-b638-4f36d25258f" +// add the security key
				"&OPERATION-NAME=findItemsAdvanced" +
				"&SERVICE-VERSION=1.0.0" +
				"&RESPONSE-DATA-FORMAT=JSON" +
				"&REST-PAYLOAD" +
				"&categoryId=" + categoryId +
				"&keywords=" + encodedKeywords + 
				"&itemFilter(0).name=Conditions" +
				"&itemFilter(0).value=New" +
				"&itemFilter(1).name=ListingType" +
				"&itemFilter(1).value=FixedPrice" +			
/*				"&itemFilter(2).name=MinQuantity" +
				"&itemFilter(2).value=2" +*/
				//"&sortOrder=PricePlusShippingHighest"+
				"&paginationInput.entriesPerPage=" + Integer.toString(entries);
		
		HttpGet request = new HttpGet(getRequest);

		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		String line = "";

		StringBuilder content = new StringBuilder();

		while (null != (line = rd.readLine()))
		{
			content.append(line);
		}
		
		return content.toString();
		

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;

	}

}


