package gui;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import ecorecycle.RCM;
import ecorecycle.RMOS;

public class EcoReSystem {

	public static void main(String[] args) {
		RMOS station=new RMOS();
    	station.addRCM(new RCM("SCU",1000.0,100.0));
    	station.addRCM(new RCM("San Jose",1000.0,500.0));
//          	station.addRCM(new RCM("Santa Cruz",1000.0,5000.0));
    	for (int i =0; i<station.getMachines().size(); i++) {
    		station.getMachine(i).addRecyclableItem(RMOS.getRandomType());
    		station.getMachine(i).addRecyclableItem(RMOS.getRandomType());
    		station.getMachine(i).addRecyclableItem(RMOS.getRandomType());
    		station.getMachine(i).activate();
    		station.getMachine(i).dropRandomRecyclableItem();
    		station.getMachine(i).dropRandomRecyclableItem();
//    		station.getMachine(i).dropRandomRecyclableItem();
//    		station.getMachine(i).dropRandomRecyclableItem();
//    		station.getMachine(i).dropRandomRecyclableItem();
//    		station.getMachine(i).dropRandomRecyclableItem();
    		station.getMachine(i).finishTransaction();
    		
    		            		            		
    		station.getMachine(i).finishTransaction();
    		
    	}
//		System.out.printf(station.toString());

    	System.out.print("= RCM "+station.getMachine(0).location+" has "+station.getMachine(0).getTotalWeightOfMachine()+"kg and "+station.getMachine(0).listOfTransaction.size()+" transactions.\n");
        System.out.print("= RCM "+station.getMachine(1).location+" has "+station.getMachine(1).getTotalWeightOfMachine()+"kg and "+station.getMachine(1).listOfTransaction.size()+" transactions.\n");

    	AdminUI stationUI = new AdminUI(station);            	
    	stationUI.createAndShowGUI();
    
      //serialize the List
        try (
          OutputStream file = new FileOutputStream("quarks.ser");
          OutputStream buffer = new BufferedOutputStream(file);
          ObjectOutput output = new ObjectOutputStream(buffer);
        ){
          output.writeObject(station);
        }  
        catch(IOException ex){
          fLogger.log(Level.SEVERE, "Cannot perform output.", ex);
        }

        RMOS recoveredStation = null;
        //deserialize the quarks.ser file
        try(
          InputStream file = new FileInputStream("quarks.ser");
          InputStream buffer = new BufferedInputStream(file);
          ObjectInput input = new ObjectInputStream (buffer);
        ){
          //deserialize the List
          recoveredStation = (RMOS) input.readObject();
          //display its data
        }
        catch(ClassNotFoundException ex){
          fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
        }
        catch(IOException ex){
          fLogger.log(Level.SEVERE, "Cannot perform input.", ex);
        }
        System.out.print(recoveredStation.getMachines().size()+" imported");
        
	}

      // PRIVATE
      private static final Logger fLogger =
        Logger.getLogger(EcoReSystem.class.getPackage().getName())
      ;
    
}
