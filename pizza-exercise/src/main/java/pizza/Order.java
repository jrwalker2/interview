package pizza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Order 
{
	private String responseString;
	private JsonArray jsonArray;
	private ArrayList<String> jsonArrayList;
	private ArrayList<Toppings> pizzaToppingsArrayList;
	private int numOfOccurrences;
	private static final String OLO_PIZZA_URL = "http://files.olo.com/pizzas.json";
	
	public static void main (String [] args)
	{
		Order order = new Order();
		order.processPizzaToppingCombos(OLO_PIZZA_URL);
	}

	
	public Order()
	{
		pizzaToppingsArrayList = new ArrayList<Toppings>();
	}
	
	public void processPizzaToppingCombos(String url)
	{
		sendRequest(url);
		for(int i=0; i<jsonArray.size(); i++)
		{
			JsonObject baseJsonArray = (JsonObject) jsonArray.get(i);
			JsonArray toppingsJsonArray = (JsonArray) baseJsonArray.get("toppings");
			
			jsonArrayList = convertJsonArrayToArrayList(toppingsJsonArray);
			Collections.sort(jsonArrayList); 
			
			boolean toppingsAreFound = false;
			for(Toppings toppings: pizzaToppingsArrayList)
			{
				if(toppings.getToppingsArrayList().equals(jsonArrayList))
				{
					numOfOccurrences = toppings.getNumOfOrders();
					numOfOccurrences++;
					toppings.setNumOfOrders(numOfOccurrences);
					
					toppingsAreFound = true;
					break;
				}
			}
			
			if(!toppingsAreFound)
			{
				Toppings toppings = new Toppings();
				toppings.setNumOfOrders(1);
				toppings.setToppingsArrayList(jsonArrayList);
				pizzaToppingsArrayList.add(toppings);
			}
		}
		
		pizzaToppingsArrayList.sort(Comparator.comparing(Toppings::getNumOfOrders).reversed());
		
		System.out.println("**********************");
		System.out.println("TOP 20 Pizza Toppings");
		System.out.println("**********************");
		for(int i=0; i<20; i++)	
		{
			Toppings toppings = pizzaToppingsArrayList.get(i);
			System.out.println("Rank [" + (i+1) + "] --> " + toppings.getToppingsArrayList() + ", Number of Orders: " +  toppings.getNumOfOrders());
		}
	}
	
	private ArrayList<String> convertJsonArrayToArrayList(JsonArray jsonArray)
	{
		ArrayList<String> arrayList = new ArrayList<String>();   
		if (jsonArray != null) 
		{ 
		   for (int i=0;i<jsonArray.size();i++)
		   { 
			   arrayList.add(jsonArray.get(i).getAsString());
		   } 
		}
		return arrayList;
	}
	
	
	private void sendRequest(String url)
	{
		try
		{
			HttpRequestBase httpRequest = new HttpGet(url);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse response = httpClient.execute(httpRequest);
			parseResponseBody(response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void parseResponseBody(HttpResponse response)
	{
		
		JsonParser parser = new JsonParser();
		
		try 
		{
			responseString = EntityUtils.toString(response.getEntity());
			jsonArray = (JsonArray)parser.parse(responseString);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}
}
