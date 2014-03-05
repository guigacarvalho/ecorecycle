package ecorecycle;
import gui.AdminUI;
import gui.UserUI;

import java.util.*;

import javax.swing.JOptionPane;

public class RCM {

	public int machineId;
	public String location;
	public ArrayList<Item> listOfItems = new ArrayList<Item>();
	public Double capacity;
	public Double presentCapacity;
	public Date lastEmptied;
	public String Status;
	public Double money;
	private Double totalWt=0.0;


	public ArrayList<Transaction> listOfTransaction = new ArrayList<Transaction>();
	public Transaction currentTransaction;
	private int coupons=0;
	private Double moneyForSession;
	public static double recyclableQtd[] = {		0.0, 		0.0,		0.0,		0.0,		0.0,		0.0,	0.0,		0.0,		0.0 };
	public static double recyclableAmount[] = {		0.0, 		0.0,		0.0,		0.0,		0.0,		0.0,	0.0,		0.0,		0.0 };


	
	class Transaction{
		private Date transactionDate;
		private int flagCoupon;
		public ArrayList<Item> transactionItems = new ArrayList<Item>();
		public Double totalWeight = 0.0;
		Transaction(){
			this.setTransactionDate(new Date());
			this.setFlagCoupon(UserUI.coupon);
		}
		public Double getTotalAmount() {
			Double totalAmount = 0.0;
			for(int i=0; i < transactionItems.size(); i++)
				totalAmount += transactionItems.get(i).price*transactionItems.get(i).weight;
			return totalAmount;
		}
		public int getTotalQtd() {
			int totalQtd = transactionItems.size();
			return totalQtd;
		}
		public Date getTransactionDate() {
			return transactionDate;
		}
		public void setTransactionDate(Date transactionDate) {
			this.transactionDate = transactionDate;
		}
		public int getFlagCoupon() {
			return flagCoupon;
		}
		public void setFlagCoupon(int flagCoupon) {
			this.flagCoupon = flagCoupon;
		}
	}
	
	public RCM(String location, Double  capacity, Double  money){
		System.out.print("= Generating new RCM at "+location+" with $"+money+" and capacity to support "+capacity+"lbs.\n");
		this.capacity = capacity;
		this.Status = "Disabled";
		this.presentCapacity = capacity;
		currentTransaction = new Transaction();
		this.lastEmptied = new Date();
		this.money = money;
		this.location = location;
		this.setCoupons(0);
	}
	
	public Date getLastEmptied() {
		return lastEmptied;
	}

	public void setLastEmptied(Date lastEmptied) {
		this.lastEmptied = lastEmptied;
		this.presentCapacity = capacity;
	}

	public ArrayList<Item> showRecyclableItemList(){
		return listOfItems;
	}	
	
	public void dropRecyclableItem(Item recyclableItem){
		 if(validateItem(recyclableItem)) {
			 this.currentTransaction.transactionItems.add(recyclableItem);
			 this.currentTransaction.totalWeight+=recyclableItem.weight;
			 //decrement current capacity
			 this.presentCapacity -= recyclableItem.weight;
			 System.out.print("= Dropping "+recyclableItem.weight+" of "+ recyclableItem.itemType +" in RCM "+this.location+"\n");
		 }
		 
	 }
	
	public void dropRandomRecyclableItem(){
		Item recyclableItem = listOfItems.get(new Random().nextInt(listOfItems.size()));
		recyclableItem.setWeight(new Random().nextDouble()*10);
		dropRecyclableItem(recyclableItem); 
	 }
	 public void addRecyclableItem(Item recyclableItem){
		//checks if the itemType don't already exist 
		 if(this.listOfItems.indexOf(recyclableItem)==-1) {
			 this.listOfItems.add(recyclableItem);
			   System.out.print("= Adding item type: "+recyclableItem.itemType+"\n");
		 }
	 }
	 
	 public Boolean validateItem(Item recyclableItem){
		 //Test Machine Status
		 if(Status != "Enabled"){
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
	 public String checkStatus(){
		 return Status;
	 }
	 
	 public Transaction getLastTransaction(){
		 return this.listOfTransaction.get(listOfTransaction.size()-1);
	 }
	 public Double  SessionMoney(){
		 return this.moneyForSession;
	 }

	public void activate() {
			this.Status = "Enabled";
			System.out.print("= Activating "+this.location+" machine!\n");
	}
	
	
	public int getCoupons() {
		return coupons;
	}
	public Double getTotalWeightOfMachine(){
		totalWt = 0.0;
		for(int i=0;i < this.listOfTransaction.size();i++){
			 totalWt = 		totalWt+ this.listOfTransaction.get(i).totalWeight;
		}
//		System.out.print("= Computing total weight.. This transaction had "+			 System.out.print("= Computing total weight.. This transaction had "+ +". \n"); +". \n");
		return totalWt;
	}
	public void finishTransaction () {
		this.listOfTransaction.add(currentTransaction);
		this.money-=currentTransaction.getTotalAmount();
		this.currentTransaction = new Transaction();
	}

	public void setCoupons(int coupons) {
		this.coupons = coupons;
	}
	public double getTotalValueOfMachinePerDay(){
		double totalValue=0;
		for(int i=0;i <listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate == new Date()){
				totalValue =+ this.listOfTransaction.get(i).getTotalAmount();
			}
			 
		}
		return totalValue;
	}
	
	public double getTotalValueOfMachinePerWeek(){
		double totalValue=0;
		Date myDate = null;
		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date newDate = calendar.getTime();
		//System.out.println(newDate);
		for(int i=0;i <listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate) && this.listOfTransaction.get(i).transactionDate.before(new Date())){
				totalValue =+ this.listOfTransaction.get(i).getTotalAmount();
			}
			 
		}
		return totalValue;
	}
	public int getNumberOfTransaction(int days){
		Date myDate = null;
		int n=0;
		Calendar calendar = Calendar.getInstance();
		//calendar.setTime(myDate);
		calendar.add(Calendar.DAY_OF_YEAR, -days);
		Date newDate = calendar.getTime();
		for(int i=0;i <listOfTransaction.size();i++){
			if(this.listOfTransaction.get(i).transactionDate.after(newDate)){
				n++;
			}
		}
		return n;
	}
}


