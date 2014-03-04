package ecorecycle;

public class Item {
		public String itemType;
		public Double weight;
		public Double price;
		public int id;
		
		Item (String itemType, Double weight, Double  price) {
			this.itemType = itemType;
			this.weight = weight;
			this.price = price;
		}

		public Item (String itemType, Double price, int i) {
			this.itemType = itemType;
			this.price = price;
			this.id = i;
			
		}
		public void setWeight(Double w) {
			this.weight = w;
		}
		public int getId(){
			return this.id;
		}
}
