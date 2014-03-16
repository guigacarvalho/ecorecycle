package ecorecycle;
import java.io.Serializable;
/**
 * Class that represent the items dropped in the Transactions Class
 * and the item types available in the RMOS and RCM
 * @author guilherme
 *
 */
public class Item implements Serializable {
		public String itemType;
		public Double weight;
		public Double price;
		public int id;
		/**
		 * Class contructor with 3 parameters (including PRICE)
		 * @param itemType
		 * @param weight
		 * @param price
		 */
		public Item (String itemType, Double weight, Double  price) {
			this.itemType = itemType;
			this.weight = weight;
			this.price = price;
		}
		/**
		 * Class constructor with 3 parameters (including ID)
		 * @param itemType
		 * @param price
		 * @param i
		 */
		public Item (String itemType, Double price, int i) {
			this.itemType = itemType;
			this.price = price;
			this.id = i;
			
		}
		/**
		 * Class constructor with 2 parameters 
		 * @param itemType2
		 * @param d
		 */
		public Item(String itemType2, double d) {
			this.itemType = itemType2;
			this.weight = d;
		}
		/**
		 * Setter method for the weight property
		 * @param w
		 */
		public void setWeight(Double w) {
			this.weight = w;
		}
		/**
		 * Getter method for the Id property
		 * @return
		 */
		public int getId(){
			return this.id;
		}
}
