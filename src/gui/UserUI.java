package gui;
import ecorecycle.*;
import javafx.application.Platform;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

/*import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
*/

@SuppressWarnings("serial")
public class UserUI extends JPanel implements  ItemListener, ActionListener {

	private final String IMG_PATHS[] = {
			"./img/cuiHeader.png",
			"./img/buttons/cuiRewardButton.png",
			"cuiUnitKgButton",
			"cuiCashButton",
			"./img/cuiDropItemsBar.png",
			"./img/buttons/Slice-"};

	private final String switchableButtons[] = {
			"cuiCashButton","cuiCouponButton","cuiUnitKgButton","cuiUnitLbButton"};
	
	JFrame frame;
	
	
	JToggleButton unit, cash;
	JButton rewardBtn;
	JButton [] item = new JButton[9];
	JTextArea transactionItemsTextArea;
	JLabel totalAmount;

	private int uiloaded=0;
	static int weight=0;
	public static int coupon=0;
	private String [] units = {"Lb", "Kg"}; 
	public RCM rcmObj;
	
	
	public UserUI (RCM rcmObj) {
		super(new FlowLayout());
		this.setBackground(Color.white);
		this.rcmObj = rcmObj; 
		
		// get content pane
		// create  labels
		JLabel header = loadImage(IMG_PATHS[0]);
		rewardBtn = loadImageBtn(IMG_PATHS[1], 0);
		rewardBtn.addActionListener(this);
		Container centerContainer = new Container();
		centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.LINE_AXIS));
		Container centerLeftContainer = new Container();
		centerLeftContainer.setLayout(new BoxLayout(centerLeftContainer, BoxLayout.PAGE_AXIS));
		unit = loadToggleBtn(IMG_PATHS[2]);
		unit.addItemListener(this);
		cash = loadToggleBtn(IMG_PATHS[3]);
		cash.addItemListener(this);
		centerLeftContainer.add(cash);
		centerLeftContainer.add(unit);
		
		transactionItemsTextArea = new JTextArea(5,40);
        JScrollPane transactionLogScrollPane = new JScrollPane(transactionItemsTextArea);
        transactionLogScrollPane.setBorder(null);

		transactionItemsTextArea.setFont(new Font("Letter Gothic Std", Font.BOLD, 14));
		transactionItemsTextArea.setForeground(new Color(166,170,169));
		transactionItemsTextArea.setSize(400, 200);
        transactionLogScrollPane.setPreferredSize(transactionItemsTextArea.getSize());;
		transactionItemsTextArea.setEditable(false);
		displayRates();

		JLabel dolarSign = new JLabel ("$");
		dolarSign.setFont(new Font("Lobster 1.4", Font.BOLD, 36));
		dolarSign.setForeground(new Color(42,195,207));

		JLabel machineLocation = new JLabel (rcmObj.location);
		machineLocation.setFont(new Font("Lobster 1.4", Font.BOLD, 36));
		machineLocation.setForeground(new Color(42,195,207));

		totalAmount = new JLabel ("0.00");
		totalAmount.setFont(new Font("Lobster 1.4", Font.BOLD, 36));
		totalAmount.setForeground(new Color(83,88,95));		
		totalAmount.setPreferredSize(new Dimension(100,50));
		
		centerContainer.add(centerLeftContainer);
		centerContainer.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
		centerContainer.add(transactionLogScrollPane);
		centerContainer.add(dolarSign);
		centerContainer.add(totalAmount);
		//System.out.printf("%.2f",centerContainer.getAlignmentX());
		JLabel dropItemsBar = loadImage(IMG_PATHS[4]);

		Container recyclableItems = new Container();
		recyclableItems.setLayout(new BoxLayout(recyclableItems, BoxLayout.X_AXIS));
		
		for (int i=1;i<10;i++) {
			item[i-1] = loadImageBtn(IMG_PATHS[5]+i+".png",i);
			recyclableItems.add(item[i-1]);
			item[i-1].addActionListener(this);
		}
		add(header);
		add(rewardBtn);
		add(centerContainer);
		add(dropItemsBar);
		add(recyclableItems);
		add(machineLocation);
		
		
	}
    public void displayRates () {
		transactionItemsTextArea.setText("-----------------------------------------------\n");
		for(int i=0;i<rcmObj.listOfItems.size();i++) {
	    	if(weight==0) //display in lb
	    		transactionItemsTextArea.setText(transactionItemsTextArea.getText()+String.format("$%.2f/Lb of %s\n", rcmObj.listOfItems.get(i).price*0.45,rcmObj.listOfItems.get(i).itemType ));
			else //display in kg
				transactionItemsTextArea.setText(transactionItemsTextArea.getText()+String.format("$%.2f/Kg of %s\n", rcmObj.listOfItems.get(i).price,rcmObj.listOfItems.get(i).itemType ));
		}
		transactionItemsTextArea.setText(transactionItemsTextArea.getText()+"----------------------------------------------\n");
    }
    
		private JToggleButton loadToggleBtn(String name) {
       try {
          String enabledPath = "./img/buttons/"+ name +".png";
          String pressedPath = "./img/buttons/click/"+ name +".png";
          String disabledPath = "./img/buttons/disabled/"+ name +".png";

          BufferedImage enabled = ImageIO.read(new File(enabledPath));
          BufferedImage pressed = ImageIO.read(new File(pressedPath));
          BufferedImage disabled = ImageIO.read(new File(disabledPath));
          
          ImageIcon enabledIcon = new ImageIcon(enabled);
          ImageIcon pressedIcon = new ImageIcon(pressed);
          ImageIcon disabledIcon = new ImageIcon(disabled);


          JToggleButton btn = new JToggleButton(enabledIcon, false);
          btn.setPressedIcon(pressedIcon);
          btn.setSelectedIcon(enabledIcon);
          btn.setDisabledIcon(disabledIcon);
          btn.setBorder(null);          
          return btn;
 
       } catch (IOException e) {
          e.printStackTrace();
          return null;
  		}
   }

		public void itemStateChanged(ItemEvent e) {
     //  if(e.getStateChange() == ItemEvent.SELECTED)
	   if(e.getSource() == cash)
       {
   		if (coupon==0) {
   			coupon=1;
   			System.out.printf("coupon=%d\n", coupon);
   		try {
	          String alternativePath = "./img/buttons/"+ switchableButtons[1] +".png";
	          BufferedImage alternate = ImageIO.read(new File(alternativePath));
	          ImageIcon alternativeIcon = new ImageIcon(alternate);
	          cash.setSelectedIcon(alternativeIcon);
	        } catch (IOException e1) {
	            e1.printStackTrace();
	    		}
   		}
   		else if (coupon==1) { 
   			coupon=0;
   			System.out.printf("coupon=%d\n", coupon);
	   		try {
		          String alternativePath = "./img/buttons/"+ switchableButtons[0] +".png";
		          BufferedImage alternate = ImageIO.read(new File(alternativePath));
		          ImageIcon alternativeIcon = new ImageIcon(alternate);
		          cash.setSelectedIcon(alternativeIcon);
		        } catch (IOException e1) {
		            e1.printStackTrace();
		    		}

   			}
   			
	   }
	   else if(e.getSource() == unit) {
	   		if (weight==0) {//kg to lb
	   			weight=1;
	   			System.out.printf("weight=%d\n", weight);
		   		try {
			          String alternativePath = "./img/buttons/"+ switchableButtons[3] +".png";
			          BufferedImage alternate = ImageIO.read(new File(alternativePath));
			          ImageIcon alternativeIcon = new ImageIcon(alternate);
			          unit.setSelectedIcon(alternativeIcon);
			        } catch (IOException e1) {
			            e1.printStackTrace();
		    		}
		   		displayRates();
	   		}
	   		else if (weight==1) {//lb to kg 
	   			weight=0;
	   			System.out.printf("weight=%d\n", weight);
		   		try {
			          String alternativePath = "./img/buttons/"+ switchableButtons[2] +".png";
			          BufferedImage alternate = ImageIO.read(new File(alternativePath));
			          ImageIcon alternativeIcon = new ImageIcon(alternate);
			          unit.setDisabledIcon(alternativeIcon);
			        } catch (IOException e1) {
			            e1.printStackTrace();
		    		}
	   			displayRates();
			}
	   }
   }
		public void actionPerformed(ActionEvent e) {
			for(int i=0;i<rcmObj.listOfItems.size();i++)
		       if(e.getSource() == item[rcmObj.listOfItems.get(i).getId()]) {
		    	   double weightLb = new Random().nextDouble()*10;
		    	   double itemPrice = (weightLb * rcmObj.listOfItems.get(i).price);
		    	   transactionItemsTextArea.setText(transactionItemsTextArea.getText()+
    			   String.format("%.2f "+ units[weight] + " * $ %.3f  = $ %.2f | "+rcmObj.listOfItems.get(i).itemType+"\n",
    					   weightLb, 
    					   rcmObj.listOfItems.get(i).price, itemPrice ));
		    	   
		    	   rcmObj.recyclableQtd[i] +=weightLb;
		    	   rcmObj.recyclableAmount[i] +=itemPrice;
		    	   
		    	   totalAmount.setText(String.format("%.2f",Float.parseFloat(totalAmount.getText()) + itemPrice));
		    	   Item dropedItem = rcmObj.listOfItems.get(i);
		    	   dropedItem.weight=weightLb;
		    	   rcmObj.dropRecyclableItem(dropedItem);
		    	   AdminUI.refreshTable();
		       }
		       if(e.getSource() == rewardBtn) {
		    	   if(Float.parseFloat(totalAmount.getText())>0.0) {
			           //Updating text box
		    		   transactionItemsTextArea.setText("-----------------------------------------------\n");
			           if(coupon==1) transactionItemsTextArea.setText(String.format(transactionItemsTextArea.getText()+"Your $ %s  coupon is being printed!\n", totalAmount.getText()));
			           else  transactionItemsTextArea.setText(String.format(transactionItemsTextArea.getText()+"Your cash is being delivered!\nYou earned $ %s. \n",totalAmount.getText()));
		        	   transactionItemsTextArea.setText(transactionItemsTextArea.getText()+"-----------------------------------------------\n");

		        	   //Displaying the animation
			    	   Icon icon = new ImageIcon("./img/animation2.gif");
			           rewardBtn.setIcon(icon);
			           rewardBtn.setBorder(null);
		        	   final Timer timer = new Timer();
		        	   class RemindTask extends TimerTask {
			               public void run() {
					           Icon icon = new ImageIcon(IMG_PATHS[1]);
					           rewardBtn.setIcon(icon);
					           rewardBtn.setBorder(null);
					           displayRates();
					           totalAmount.setText("0.00");
			                   timer.cancel(); //Terminate the timer thread
			               }

			           }
			           timer.schedule(new RemindTask(), 2000);
		               this.rcmObj.finishTransaction();
	
		               
		               AdminUI.refreshGraphic();
		    	   }
		       }

   }
		
		public void createAndShowGUI() {

        //Create and set up the window.
        frame = new JFrame("RCM");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//Add content to the window.
        frame.add(new UserUI(rcmObj));

        //Display the window.
        frame.setBounds(100, 100, 800, 600);
        frame.setVisible(true);

    }
		public JButton loadImageBtn(String path, int i) {
        try {
            String pressedPath = "./img/buttons/click/"+ path.substring(14,path.length());
            String disabledPath = "./img/buttons/disabled/"+ path.substring(14,path.length());
           BufferedImage enabled = ImageIO.read(new File(path));
           BufferedImage pressed = ImageIO.read(new File(pressedPath));
           BufferedImage disabled = ImageIO.read(new File(disabledPath));
           ImageIcon enabledIcon = new ImageIcon(enabled);
           ImageIcon pressedIcon = new ImageIcon(pressed);
           ImageIcon disabledIcon = new ImageIcon(disabled);
           JButton btn = new JButton(enabledIcon);
           if(!validateButton(i))
        	   btn.setEnabled(false);
           btn.setPressedIcon(pressedIcon);
           btn.setDisabledIcon(disabledIcon);
           btn.setBorder(null);
           
//         JOptionPane.showMessageDialog(null, label);
           return btn;
        } catch (IOException e) {
           e.printStackTrace();
           return null;
   		}
     }
		private boolean validateButton(int z) {
			if (z>0 && z<9) 
				for (int j = 0; j < rcmObj.listOfItems.size(); j++)
						if (rcmObj.listOfItems.get(j).itemType.equals(RMOS.getAvailableItemTypes()[z-1].itemType))
								return true;
				if(z==0) return true;
				return false;
		}
		public JLabel loadImage(String path) {
        try {
           BufferedImage img = ImageIO.read(new File(path));
           ImageIcon icon = new ImageIcon(img);
           JLabel imgLabel = new JLabel(icon);
           return imgLabel;
        } catch (IOException e) {
           e.printStackTrace();
           return null;
   		}
     }
		public void load(){
			if (uiloaded==0) {
				this.createAndShowGUI();
				uiloaded++;
			}
			else
			{
				this.frame.setVisible(true);
			}
		}

}