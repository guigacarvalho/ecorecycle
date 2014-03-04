package ecorecycle;


	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.ObjectOutputStream;
	
	 public class SerializeMain {

	 /**
	  * @author Arpit Mandliya
	  */
	 public static void main(String[] args) {

	RMOS station = new RMOS();
 	station.addRCM(new RCM("SCU",100.0,100.0));
 	station.getMachines().get(0).addRecyclableItem(station.getRandomType());
 	station.getMachines().get(0).addRecyclableItem(station.getRandomType());
 	station.getMachines().get(0).addRecyclableItem(station.getRandomType());
 	
	  try
	  {
	   FileOutputStream fileOut = new FileOutputStream("employee.ser");
	   ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
	   outStream.writeObject(station);
	   outStream.close();
	   fileOut.close();
	  }catch(IOException i)
	  {
	   i.printStackTrace();
	  }
	 }
	}