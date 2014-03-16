package ecorecycle;

import java.io.*;
import java.util.*;
/**
 * Monitoring station class
 * @author guilherme
 *
 */
public class RMOS implements Serializable {	
	
	private ArrayList<RCM> allRCMs = new ArrayList<RCM>();
	
	private static Item availableItemTypes[]=new Item[9];
	/**
	 * RMOS Class constructor
	 */
	public RMOS() {
		availableItemTypes[0] = new Item ("Mixed",0.0051,0);
		availableItemTypes[1] = new Item ("Cartons",0.05,1);
		availableItemTypes[2] = new Item ("Landfill",0.02,2);
		availableItemTypes[3] = new Item ("Glass",0.009,3);
		availableItemTypes[4] = new Item ("Cans",0.7,4);
		availableItemTypes[5] = new Item ("Cardboard",0.35,5);
		availableItemTypes[6] = new Item ("Plastics",0.17,6);
		availableItemTypes[7] = new Item ("Paper",0.1,7);
		availableItemTypes[8] = new Item ("Food Waste",0.009,8);
	}
	/**
	 * Returns the type ID for a specific recyclable item name
	 * @param s
	 * @return typeID
	 */
	public int getAvailableTypeId(String s) {
		for (int i = 0; i < availableItemTypes.length; i++)
			if (availableItemTypes[i].itemType.equals(s)) return i;
		return -1;
	}
	/**
	 * Returns the list of RCM Objects
	 * @return RCMList
	 */
	public ArrayList<RCM> getMachines(){
		return allRCMs;
	}
	/**
	 * Returns a machine given its id
	 * @param i
	 * @return RCMID
	 */
	public RCM getMachine(int i){
		return allRCMs.get(i);
	}
	/**
	 * Adds a RCM object to the list
	 * @param obj
	 */	
	public void addRCM(RCM rcmObj){
		allRCMs.add(rcmObj);		
	}
	/**
	 * Removes a RCM object from the list
	 * @param obj
	 */
	public void removeRCM(RCM obj){
		allRCMs.remove(obj);
	}
/**
 * Method that returns the operational status
 * of a specific RCM		
 * @param obj
 * @return status
 */
	public String checkOperationalStatus(RCM obj){
		int j;
		for(j=0;j<allRCMs.size();j++){
			if(obj.equals(allRCMs.get(j))){
				return allRCMs.get(j).Status;
			}
		}
		return null;
	}
	/**
	 * Changes the price of an specific item
	 * in a RCM
	 * @param obj
	 * @param item
	 * @param price
	 */
	public void changePrice(RCM obj,Item item, Double price){
		int k = obj.listOfItems.indexOf(item);
		obj.listOfItems.get(k).price = price;
	}
	/**
	 * Returns the available money in a specific RCM
	 * @param obj
	 * @return money
	 */
	public Double  checkMoney(RCM obj){
		return obj.money;
	}
	/**
	 * Returns the capacity of an specific RCM
	 * @param obj
	 * @return capacity
	 */
	public Double  checkCapacity(RCM obj){  // Return the capacity 
		
		return (obj.presentCapacity)/(obj.capacity)*100;
	}
	/**
	 * Returns the last emptied date for an specific RCM
	 * @param obj
	 * @return date
	 */
	public Date getLastDateEmptied(RCM obj){
		return obj.lastEmptied.get(obj.listOfTransaction.size()-1);
	}
	/**
	 * Returns the most used RCM in all times
	 * @return rcm
	 */
	public RCM returnMostUsedMachine(){
		int k, max;
		RCM maxObj;
		max = allRCMs.get(0).listOfTransaction.size();
		maxObj = allRCMs.get(0);
		for(k=1;k< allRCMs.size();k++){
			if(allRCMs.get(k).listOfTransaction.size() > max){
				max = allRCMs.get(k).listOfTransaction.size();
				maxObj = allRCMs.get(k);
			}
		}
		return maxObj;
	}
	/**
	 * Utility method used for testing purposes
	 * that displays all RCMs basic information
	 */
	public void display(){
		int k;
		for(k=0;k <allRCMs.size(); k++){
			System.out.println(allRCMs.get(k).machineId);
			System.out.println(allRCMs.get(k).location);
			System.out.println(allRCMs.get(k).Status);
		}
	}
	/**
	 * Getter method for available item types
	 * @return availableItemTypes
	 */
	public static Item[] getAvailableItemTypes() {
		return availableItemTypes;
	}
	/**
	 * Utility method used for testing purposes
	 * that displays the list of items of all RCMs
	 */
	public String toString(){
		String test="";
		for(int i=0; i<allRCMs.size();i++) {
			test+=(allRCMs.get(i).location+"\n");
			for(int j=0; j< allRCMs.get(i).listOfItems.size();j++)
				test+=(allRCMs.get(i).listOfItems.get(j).getId()+"\n");
		}
		return test;
		
	}
	/**
	 * Setter method for the available items list
	 * @param availableItemTypes
	 */
	public static void setAvailableItemTypes(Item availableItemTypes[]) {
		RMOS.availableItemTypes = availableItemTypes;
	}
	/**
	 * Method that returns the Machine Location with maximum transactions in the last
	 * "x" days 
	 * @param days
	 * @return RCMlocation
	 */
	public String getMaxTransactionMachine(int days){
		int max;
		max = allRCMs.get(0).getNumberOfTransaction(days);
		String Location = allRCMs.get(0).location;
		for(int i=1;i<allRCMs.size();i++){
			if(max <  allRCMs.get(i).getNumberOfTransaction(days)){
				max = allRCMs.get(i).getNumberOfTransaction(days);
				Location = allRCMs.get(i).location;
			}
		}
		return Location;
	}
	/**
	 * Utility method used to test the UI in early stages
	 * @return item
	 */
	public static Item getRandomType() {
		int i = new Random().nextInt(8);
		return availableItemTypes[i];
	}

}
