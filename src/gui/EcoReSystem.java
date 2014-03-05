package gui;

import ecorecycle.RCM;
import ecorecycle.RMOS;

public class EcoReSystem {
	public static void main(String[] args) {
		
        //Schedule a job for the event dispatch thread:
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	private RMOS station;
        	
        	public void run() {
                //Creating and showing the application's GUI.
            	station = new RMOS();
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
//            		station.getMachine(i).dropRandomRecyclableItem();
//            		station.getMachine(i).dropRandomRecyclableItem();
//            		station.getMachine(i).dropRandomRecyclableItem();
//            		station.getMachine(i).dropRandomRecyclableItem();
            		station.getMachine(i).finishTransaction();
            		
            		            		            		
            		station.getMachine(i).finishTransaction();
            		
            	}
//        		System.out.printf(station.toString());

            	System.out.print("= RCM "+station.getMachine(0).location+" has "+station.getMachine(0).getTotalWeightOfMachine()+"kg and "+station.getMachine(0).listOfTransaction.size()+" transactions.\n");
	            System.out.print("= RCM "+station.getMachine(1).location+" has "+station.getMachine(1).getTotalWeightOfMachine()+"kg and "+station.getMachine(1).listOfTransaction.size()+" transactions.\n");

            	AdminUI stationUI = new AdminUI(this.station);            	
            	stationUI.createAndShowGUI();
            }
        });
        
	}
}
