package pizza;

import java.util.ArrayList;

public class Toppings
{
	int numOfOrders;
	ArrayList<String> toppingsArrayList;
	
	public int getNumOfOrders() 
	{
		return numOfOrders;
	}
	public ArrayList<String> getToppingsArrayList() 
	{
		return toppingsArrayList;
	}
	public void setNumOfOrders(int numOfOrders) 
	{
		this.numOfOrders = numOfOrders;
	}
	public void setToppingsArrayList(ArrayList<String> toppingsArrayList) 
	{
		this.toppingsArrayList = toppingsArrayList;
	}
}
