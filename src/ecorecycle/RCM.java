package ecorecycle;
import gui.AdminUI;
import gui.UserUI;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JOptionPane;
/**
 * Recycling Machine Class
 * @author guilherme and ankit
 *
 */
public class RCM implements Serializable {

	public int machineId;
	public String location;
	public ArrayList<Item> listOfItems = new ArrayList<Item>();
	public Double capacity;
	public Double presentCapacity;
	public ArrayList<Date> lastEmptied = new ArrayList<Date>();
	public String Status;
	public Double money;
	private Double totalWt=0.0;
	


	public ArrayList<Transaction> listOfTransaction = new ArrayList<Transaction>();
	public Transaction currentTransaction;
	public int coupons=0;
	public int weight=0;
	private Double moneyForSession;
	public double recyclableQtd[] = {		0.0, 		0.0,		0.0,		0.0,		0.0,		0.0,	0.0,		0.0,		0.0 };
	public double recyclableAmount[] = {		0.0, 		0.0,		0.0,		0.0,		0.0,		0.0,	0.0,		0.0,		0.0 };


	/**
	 * Transaction Inner Class
	 * Contains the items, dates and othe relevant data about the transaction
	 * @author guilherme
	 *
	 */
	public class Transaction implements Serializable {
		private Date transactionDate;
		private int flagCoupon;
		public ArrayList<Item> transactionItems = new ArrayList<Item>();
		public Double totalWeight = 0.0;
		public Double totalAmount;
		/**
		 * Transaction Class constructor
		 */
		Transaction(){
			this.setTransactionDate(new Date());
			this.setFlagCoupon(coupons);
		}
		/**
		 * Returns total amount of the transaction
		 * @return totalAmount
		 */
		public Double getTotalAmount() {
			Double totalAmount = 0.0;
			for(int i=0; i < transactionItems.size(); i++)
				totalAmount += transactionItems.get(i).price*transactionItems.get(i).weight;
			this.totalAmount=totalAmount;
			if(totalAmount > money){
				this.setFlagCoupon(1);
			}
			return totalAmount;
		}
		/**
		 * Returns the number of items of the transaction
		 * @return totalQtd
		 */
		public int getTotalQtd() {
			int totalQtd = transactionItems.size();
			return totalQtd;
		}
		/**
		 * Returns the transaction date
		 * @return transactionDate
		 */
		public Date getTransactionDate() {
			return transactionDate;
		}
		/**
		 * Sets the transaction date
		 * @param transactionDate
		 */
		public void setTransactionDate(Date transactionDate) {
			this.transactionDate = transactionDate;
		}
		/**
		 * Returns the coupon flag
		 * @return flag
		 */
		public int getFlagCoupon() {
			return flagCoupon;
		}
		/**
		 * Sets the coupon flag
		 * @param flagCoupon
		 */
		public void setFlagCoupon(int flagCoupon) {
			this.flagCoupon = flagCoupon;
		}
		
	}
	/**
	 * RCM Class constructor
	 * @param location
	 * @param capacity
	 * @param money
	 */
	public RCM(String location, Double  capacity, Double  money){
		System.out.print("= Generating new RCM at "+location+" with $"+money+" and capacity to support "+capacity+"lbs.\n");
		this.capacity = capacity;
		this.Status = "Disabled";
		this.presentCapacity = capacity;
		currentTransaction = new Transaction();
		this.lastEmptied.add(new Date());
		this.money = money;
		this.location = location;
		this.setCoupons(0);
	}
	/**
	 * Returns the last date the RCM was emptied 
	 * @return date
	 */
	public Date getLastEmptied() {
		return lastEmptied.get(lastEmptied.size()-1);
	}
	/**
	 * Returns the list of items available in the RCM 
	 * @returnlistOfItems
	 */
	public ArrayList<Item> showRecyclableItemList(){
		return listOfItems;
	}	
	/**
	 * Executes the "Drop Recyclable item" action
	 * @param recyclableItem
	 */
	public void dropRecyclableItem(Item recyclableItem){
			 this.currentTransaction.transactionItems.add(recyclableItem);
			 this.currentTransaction.totalWeight+=recyclableItem.weight;
			 //decrement current capacity
			 this.presentCapacity -= recyclableItem.weight;
			 System.out.print("= Dropping "+recyclableItem.weight+" of "+ recyclableItem.itemType +" in RCM "+this.location+"\n");		 
	 }
	/**
	 * Uitility method used for testing purposes
	 * Simulates the drop of a recyclable item
	 */
	public void dropRandomRecyclableItem(){
		Item recyclableItem = listOfItems.get(new Random().nextInt(listOfItems.size()));
		recyclableItem.setWeight(new Random().nextDouble()*10);
		dropRecyclableItem(recyclableItem); 
	 }
	/**
	 * Adds a recyclable item to the item list
	 * @param recyclableItem
	 */
	 public void addRecyclableItem(Item recyclableItem){
		//checks if the itemType don't already exist 
		 if(this.listOfItems.indexOf(recyclableItem)==-1) {
			 this.listOfItems.add(recyclableItem);
			   System.out.print("= Adding item type: "+recyclableItem.itemType+"\n");
		 }
	 }
	 /**
	  * Validates the item that is being dropped
	  * @param recyclableItem
	  * @return true or false
	  */
	 public Boolean validateItem(Item recyclableItem){
		 //Test Machine Status
		 if(!this.Status.equals("Enabled")){
//    	   JOptionPane.showInternalMessageDialog(null, " = Error: Can't drop item because the RCM is not enabled.\n", "Alert", JOptionPane.ERROR_MESSAGE); 
			 System.out.printf("= Error: Can't drop item because the RCM is not enabled.\n");
			 return false;
		 } 
		 //Test Capacity
		 if(this.presentCapacity < recyclableItem.weight){
			System.out.printf("= Error: That item is too heavy!\n");
			return false;
		 }
		 //Test ItemType
		for(int k=0; k < listOfItems.size(); k++){
			if(recyclableItem.itemType.equals(listOfItems.get(k).itemType))
				return true;
		} 	
		System.out.printf("= Error: This RCM does not accept this item type!\n");
		return false;

	 }	 
	 /**
	  * Getter method for the status property
	  * @return status
	  */
	 public String checkStatus(){
		 return Status;
	 }
	 /**
	  * Returns the last transaction
	  * @return lastTransaction
	  */
	 public Transaction getLastTransaction(){
		 return this.listOfTransaction.get(listOfTransaction.size()-1);
	 }
	 /**
	  * Activates the machine
	  */
	public void activate() {
			this.Status = "Enabled";
			System.out.print("= Activating "+this.location+" machine!\n");
	}
	/**
	 * Getter method for the coupon flag
	 * @return 0 for money or 1 for coupon
	 */
	public int getCoupons() {
		return coupons;
	}
	/**
	 * Calculates the total weight of a machine
	 * @return totalWeight
	 */
	public Double getTotalWeightOfMachine(){
		totalWt = 0.0;
		for(int i=0;i < this.listOfTransaction.size();i++){
			 totalWt = 		totalWt+ this.listOfTransaction.get(i).totalWeight;
		}
		return totalWt;
	}
	/**
	 * Calculates the items weight dropped in a machine x Days from the present date  
	 * @param days
	 * @return weightInXDays
	 */
	public double getWeightofMachine(int days){
		Date myDate = null;
		totalWt = 0.0;
		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i <listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate)){
				totalWt = totalWt+ this.listOfTransaction.get(i).totalWeight;;
			}
		}
		return totalWt;
	}

/**
 * Finishes the currentTransaction by
 * adding the currentTransaction in the RCMs' transaction list
 * and replacing it by a new empty currentTransaction Object
 */
public void finishTransaction () {
	this.listOfTransaction.add(currentTransaction);
	if(currentTransaction.getTotalAmount() <= money){
		this.money-=currentTransaction.getTotalAmount();
	}
	
	this.currentTransaction = new Transaction();
}
/**
 * Setter for the coupons flag
 * @param coupons
 */
	public void setCoupons(int coupons) {
		this.coupons = coupons;
	}
	/**
	 * Calculates the transactions number of a specific machine
	 * x Days from the present date
	 * @param days
	 * @return numberOfTransaction
	 */
	public int getNumberOfTransaction(int days){
		int n=0;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i <listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate)){
				n++;
			}
		}
		return n;
	}
	/**
	 * Calculates the money spent in Coupons
	 * x Days from the present date
	 * @param days
	 * @return moneyDispensedInCoupons
	 */
	public Double getTotalValueOfCoupons(int days){
		Double couponAmount = 0.0;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i < this.listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).flagCoupon == 1 && this.listOfTransaction.get(i).transactionDate.after(newDate)){
				couponAmount += this.listOfTransaction.get(i).totalAmount;
			}
		}
		return couponAmount;
	}
	/**
	 * Calculates the weight of a specific item type in the RCM 
	 * x Days from the present date
	 * @param days
	 * @return moneyDispensedInCoupons
	 */
	public Double getTotalWeightOfItem(String type, int days){
		Double totalWeight = 0.0;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i < this.listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate))
				for(int j=0;j < this.listOfTransaction.get(i).transactionItems.size();j++)
						if (this.listOfTransaction.get(i).transactionItems.get(j).itemType.equalsIgnoreCase(type)) 
							totalWeight += this.listOfTransaction.get(i).transactionItems.get(j).weight;
			}
		return totalWeight;
	}
	/**
	 * Calculates the money given for a specific item type in the RCM 
	 * x Days from the present date
	 * @param days
	 * @return moneyDispensedforItemType
	 */
	public Double getTotalMoneyOfItem(String type, int days){
		Double totalMoney = 0.0;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i < this.listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate))
				for(int j=0;j < this.listOfTransaction.get(i).transactionItems.size();j++)
						if (this.listOfTransaction.get(i).transactionItems.get(j).itemType.equalsIgnoreCase(type)) 
							totalMoney += (this.listOfTransaction.get(i).transactionItems.get(j).price * this.listOfTransaction.get(i).transactionItems.get(j).weight);
			}
		return totalMoney;
	}
	/**
	 * Calculates the number of items dropped in the RCM 
	 * x Days from the present date for a specific item type 
	 * @param days
	 * @return itemsDroppedforItemType
	 */

	public int getTotalItemsOfType(String type, int days){
		int n = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i < this.listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate))
				for(int j=0;j < this.listOfTransaction.get(i).transactionItems.size();j++)
						if (this.listOfTransaction.get(i).transactionItems.get(j).itemType.equalsIgnoreCase(type)) 
							n++;
			}
		return n;
	}
	/**
	 * Calculates the number of Transactions for a specific 
	 * type x Days from the present date
	 * @param type
	 * @param days
	 * @return numberOfTransactionsInXDays
	 */
	public int getTotalTransactionsWithType(String type, int days){
		int n = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i < this.listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate))
				for(int j=0;j < this.listOfTransaction.get(i).transactionItems.size();j++)
						if (this.listOfTransaction.get(i).transactionItems.get(j).itemType.equalsIgnoreCase(type)) { 
							n++;
							break;
						}
			}
		return n;
	}	
	/**
	 * Returns the total value of cash 
	 * dispensed in x Days from the present date 
	 * @param days
	 * @return cashAmount
	 */
	public Double getTotalValueOfCash(int days){
		Double cashAmount = 0.0;
		Date myDate = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
		//calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		System.out.println("\n= Start Date : " + sdf.format(calendar.getTime()));

		for(int i=0;i < this.listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).flagCoupon == 0 && this.listOfTransaction.get(i).transactionDate.after(newDate)){
				cashAmount += this.listOfTransaction.get(i).totalAmount;
				System.out.println("\n =Date : " + sdf.format(this.listOfTransaction.get(i).transactionDate)+" $"+this.listOfTransaction.get(i).totalAmount);
				
			}
		}
		return cashAmount;
	}
	/**
	 * Returns the number of times the RCM was emptied
	 * @param days
	 * @return numberOfTimesEmptied
	 */
	public int getNumberOfTimesMachineEmptied(int days){
		Date myDate = null;
		int n=0;
		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0; i< lastEmptied.size(); i++){
			if(lastEmptied.get(i).after(newDate)){
				n++;
			}
		}
		return n;
	}
	/**
	 * Returns the total number of items for an RCM 
	 * x Days from the present date
	 * @param days
	 * @return numberOfItems
	 */
	public int getNumberOfItems(int days){
		Date myDate = null;
		int totalItems =0;
		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i<this.listOfTransaction.size(); i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate)){
				totalItems += this.listOfTransaction.get(i).transactionItems.size();
			}
		}
		return totalItems;
	}
	/**
	 * Adds emptied dates into the dates array
	 * @param lastEmptied
	 */
	public void setLastEmptied(Date lastEmptied) {
		this.lastEmptied.add(lastEmptied);
		this.presentCapacity = capacity;
	}
	
}


