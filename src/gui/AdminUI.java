package gui;
import ecorecycle.Item;
//import ecorecycle.*;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import ecorecycle.RCM;
import ecorecycle.RMOS;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/*import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
*/

@SuppressWarnings("serial")
public class AdminUI extends JPanel implements  ActionListener, TableModelListener {

	private final String IMG_PATHS[] = {
			"./img/auiHeader.png",
			"./img/RecyclingMachines.png",
			"cuiUnitKgButton",
			"./img/newMachine.png",
			"./img/ItemTypes.png",
			"./img/buttons/Slice-",
			"./img/Stats.png"};

	public static RMOS station = new RMOS();
	
	JToggleButton unit, cash;
	JButton newMachine, removeMachine, changeItem, addItem,activateRCM,loadMachine, EmptyMachine ;
	JTextArea transactionItemsTextArea;
	JLabel totalAmount,machinesLabel, statsLabel, itemTypes;
	
//	private static int coupon=0;
//	private int kg=0;
//	private String [] units = {"Kg", "Lb"}; 
	
    final static JFXPanel fxPanel = new JFXPanel();
    static DefaultTableModel defTableModel;


    String[] columnNames = {"ID",
            "Location",
            "Capacity",
            "$ Available",
            "Status",
            "Item Types",
            "Last Emptied"};

	private JTable table;

	protected int selectedRcm;
	
	public AdminUI (final RMOS station) {
		super(new FlowLayout());
		this.setBackground(Color.WHITE);

		Object[] newData = {0,
    			station.getMachines().get(0).location,
    			station.getMachines().get(0).presentCapacity, 
    			station.getMachines().get(0).money,
    			station.getMachines().get(0).Status,
    			station.getMachines().get(0).listOfItems.size(),
    			station.getMachines().get(0).getLastEmptied(),
    			};
	    Object[][] data = {newData};
	    
	    defTableModel = new DefaultTableModel(data,columnNames) {
	    	  public boolean isCellEditable(int row, int column) {
//	    	       if(column > 3)
	    	    	   return false;
	//    	       return true;
	    	    }
	    };
	    
		table = new JTable(defTableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
            ListSelectionModel rowSM = table.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
//                        System.out.println("= No RCMs are selected.\n");
                    } else {
                        selectedRcm = lsm.getMinSelectionIndex();
  //                      System.out.println("= RCM " + station.getMachine(selectedRcm).location+ " is now selected.\n");
                    }
                }
            });
		
	    for (int i =1; i< station.getMachines().size(); i++) {
	    	Object[] newData1 = {i,
	    			station.getMachine(i).location,
	    			station.getMachine(i).presentCapacity, 
	    			station.getMachine(i).money,
	    			station.getMachine(i).Status,
	    			station.getMachine(i).listOfItems.size(),
	    			station.getMachine(i).getLastEmptied()};
    			defTableModel.addRow(newData1);	    	
	    }
		
	    defTableModel.addTableModelListener(this);	    
	    
	    newMachine = new JButton("New Machine");
	    newMachine.setBorder(null);
	    newMachine.setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
		newMachine.setForeground(new Color(42,195,207));
		newMachine.addActionListener(this);

	    removeMachine = new JButton("Remove Machine");
	    removeMachine .setBorder(null);
	    removeMachine .setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
	    removeMachine .setForeground(new Color(42,195,207));
	    removeMachine.addActionListener(this);
	    

	    addItem = new JButton("Add Item type");
	    addItem .setBorder(null);
	    addItem .setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
	    addItem .setForeground(new Color(42,195,207));
	    addItem .addActionListener(this);
	    
	    
	    changeItem = new JButton("Change Items");
	    changeItem.setBorder(null);
	    changeItem.setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
	    changeItem.setForeground(new Color(42,195,207));
	    changeItem.addActionListener(this);
	    
	    activateRCM = new JButton("Activate/Deactivate");
	    activateRCM.setBorder(null);
	    activateRCM.setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
	    activateRCM.setForeground(new Color(42,195,207));
	    activateRCM.addActionListener(this);
	    
	    loadMachine = new JButton("Load Machine");
	    loadMachine .setBorder(null);
	    loadMachine .setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
	    loadMachine .setForeground(new Color(42,195,207));
	    loadMachine .addActionListener(this);
	    
	    EmptyMachine = new JButton("Empty Machine");
	    EmptyMachine .setBorder(null);
	    EmptyMachine .setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
	    EmptyMachine .setForeground(new Color(42,195,207));
	    EmptyMachine .addActionListener(this);
	    
	    JLabel [] separator = new JLabel[10];
	    
	    for (int i =0; i<separator.length;i++) {
		    separator[i]= new JLabel(" = ");
		    separator[i].setBorder(null);
		    separator[i].setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
		    separator[i].setForeground(new Color(166,170,169));
	    }
	    
	    
		JLabel header = loadImage(IMG_PATHS[0]);
		machinesLabel = loadImage(IMG_PATHS[1]);
        
		itemTypes = loadImage(IMG_PATHS[4]);
		
		Container topLabels = new Container();
		topLabels.setLayout(new BoxLayout(topLabels, BoxLayout.LINE_AXIS));
		topLabels.add(machinesLabel);
		
		Container lowLabels = new Container();
		lowLabels.setLayout(new BoxLayout(lowLabels, BoxLayout.LINE_AXIS));
		lowLabels.add(itemTypes);
		
		Container centerContainer = new Container();
		centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.LINE_AXIS));
		
        JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane .setPreferredSize(new Dimension(460,100));

		centerContainer.add(new Box.Filler(new Dimension(20,20),new Dimension(20,20),new Dimension(20,20)));
		Container tableContainer = new Container();
		Container topMenuContainer = new Container();
		topMenuContainer.setLayout(new BoxLayout(topMenuContainer, BoxLayout.LINE_AXIS));
		topMenuContainer.add(newMachine);
		topMenuContainer.add(separator[0]);		
		topMenuContainer.add(removeMachine);
		topMenuContainer.add(separator[1]);		
		topMenuContainer.add(changeItem);
		topMenuContainer.add(separator[2]);		
		topMenuContainer.add(activateRCM);
		topMenuContainer.add(separator[3]);		
		topMenuContainer.add(loadMachine);
		topMenuContainer.add(separator[4]);
		topMenuContainer.add(EmptyMachine);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("General", null, fxPanel, "Presents the general statistics");
        tabbedPane.addTab("RCM", null, separator[5], "Presents the statistics about a specific RCM");
        tabbedPane.addTab("Money", null, separator[7], "Presents the statistics about money");
        tabbedPane.addTab("Recyclable Items", null, separator[8], "Presents the statistics about Recyclable Items");
        tabbedPane.addTab("Other", null, separator[9], "Presents other statistics");
        //tabbedPane.setBackground(Color.WHITE);

		tableContainer.add(topMenuContainer);

		tableContainer.setLayout(new BoxLayout(tableContainer, BoxLayout.PAGE_AXIS));
		tableContainer.add(table.getTableHeader(), BorderLayout.PAGE_START);
		tableContainer.add(tableScrollPane , BorderLayout.CENTER);
		centerContainer.add(tableContainer);
		
		add(header);
		add(topLabels);
		add(centerContainer);		
		add(lowLabels);
		add(tabbedPane);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX(fxPanel);
            }
       });

		
	}

		@SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) {
		       if(e.getSource() == newMachine) {
		    	   JTextField machineId,Location,capacity,Money;
					JLabel machineLabel ,LocationLabel , capacityLabel, MoneyLabel;
					//JButton Add, Cancel;
				    	   JPanel myPanel = new JPanel(new GridLayout(4,2));
				    	   machineLabel = new JLabel("Machine ID");
				    	   LocationLabel = new JLabel("Location");
				    	   capacityLabel = new JLabel("Capacity");
				    	   MoneyLabel = new JLabel("Money");
				    	   machineId = new JTextField();
				    	   Location = new JTextField();
				    	   capacity = new JTextField();
				    	   Money = new JTextField();
				    	   myPanel.add(machineLabel);
				    	   myPanel.add(machineId);
				    	   myPanel.add(LocationLabel);
				    	   myPanel.add(Location);
				    	   myPanel.add(capacityLabel);
				    	   myPanel.add(capacity);
				    	   myPanel.add(MoneyLabel);
				    	   myPanel.add(Money);
				    	   
				    	   int result = JOptionPane.showConfirmDialog(null, myPanel, 
					               "Create New RCM", JOptionPane.OK_CANCEL_OPTION);
				    	   if (result == JOptionPane.OK_OPTION) {
//				    		   String s1 = machineId.getText();
				    		   String s2 = Location.getText();
				    		   Double s3 = Double.valueOf(capacity.getText());
				    		   Double s4 = Double.valueOf(Money.getText());
					    	   station.addRCM(new RCM(s2, s3, s4));
					    	   int i =station.getMachines().size();
						    	Object[] newData1 = {i,
						    			s2,
						    			s3, 
						    			s4,
						    			"Disabled",
						    			0};
					    			defTableModel.addRow(newData1);
			    	   		}   
       					} else if(e.getSource() == removeMachine) {
				    	   station.removeRCM(station.getMachine(selectedRcm));
				    	   defTableModel.removeRow(selectedRcm);
       					}
						 else if(e.getSource() == changeItem) {
					    	   JPanel myPanel1 = new JPanel(new GridLayout(9,3));
					    	   JTextField [] listPrices= new JTextField[9];
					    	   JCheckBox [] checkItem = new JCheckBox[9]; 
					    	   int i=0,j =0;

					    	   for (i=0; i < station.getAvailableItemTypes().length; i++) {
					    		   for(j=0; j< station.getMachine(selectedRcm).listOfItems.size();j++) {
					    			   if(station.getMachine(selectedRcm).listOfItems.get(j).getId()==RMOS.getAvailableItemTypes()[i].getId()) {
//					    				   System.out.print("+ "+station.getMachine(selectedRcm).listOfItems.get(j).getId()+" = "+RMOS.getAvailableItemTypes()[i].getId()+" \n");
					    				   checkItem[i]= new JCheckBox(RMOS.getAvailableItemTypes()[i].itemType,true);
					    				   listPrices[i]=new JTextField(station.getMachine(selectedRcm).listOfItems.get(j).price.toString());
					    				   break;
					    			   }
					    			   else {
//				    				   System.out.print("- "+station.getMachine(selectedRcm).listOfItems.get(j).getId()+" = "+RMOS.getAvailableItemTypes()[i].getId()+" \n");
					    				   checkItem[i]= new JCheckBox(RMOS.getAvailableItemTypes()[i].itemType,false);
					    				   listPrices[i]=new JTextField(RMOS.getAvailableItemTypes()[i].price.toString());
					    			   }
					    		   }

					    		   
					    		   myPanel1.add(checkItem[i]);
					    		   myPanel1.add(listPrices[i]);
//						    	   System.out.printf("%d",i);
					    	   }
					    	   	   
					    	   int result1 = JOptionPane.showConfirmDialog(null, myPanel1, 
						               "Change RCM Items", JOptionPane.OK_CANCEL_OPTION);
					    	   if (result1 == JOptionPane.OK_OPTION) {
					    		   station.getMachine(selectedRcm).listOfItems.clear();
					    		   for (int i1=0; i1 < station.getAvailableItemTypes().length; i1++) {
						    			   if(checkItem[i1].isSelected()) {
						    				   station.getMachine(selectedRcm).addRecyclableItem(
						    						   new Item(checkItem[i1].getText(), 
						    						   Double.valueOf(listPrices[i1].getText()),i1));
						    			   }
					    		   		}
					    		   refreshTable();
					    	   }
					   }
						 else if(e.getSource() == activateRCM) {
							 if (station.getMachine(selectedRcm).Status=="Enabled")
								 station.getMachine(selectedRcm).Status="Disabled";
							 else
							 station.getMachine(selectedRcm).Status="Enabled";
				    		   refreshTable();

						 }
						 else if(e.getSource() == loadMachine) {
							 UserUI costumerUI = new UserUI(station.getMachine(selectedRcm));
							 costumerUI.load();
							 refreshGraphic();
						 }
						 else if(e.getSource() == EmptyMachine){
							 station.getMachine(selectedRcm).setLastEmptied(new Date());
							 station.getMachine(selectedRcm).presentCapacity=station.getMachine(selectedRcm).capacity;
							 refreshTable();
						 }
		       }
   
		
		public void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("RMOS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new AdminUI(station));

        //Display the window.

        frame.setBounds(200, 200, 800, 600);
        frame.setVisible(true);        
        
    }
    static void initFX(JFXPanel fxPanel) {
        // This method is invoked on the JavaFX thread
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static Scene createScene() {

    	final NumberAxis yAxis = new NumberAxis();
                
        final CategoryAxis xAxis = new CategoryAxis();

        final BarChart<String,Number> bc = 
            new BarChart<String,Number>(xAxis,yAxis);
        
        XYChart.Series series = new XYChart.Series();
	        for(int i=0; i<station.getMachines().size();i++) {
		        series.getData().add(new XYChart.Data(station.getMachine(i).location,station.getMachine(i).getTotalWeightOfMachine()));
	        }
        bc.getData().addAll(series);	
        Scene  scene  = new Scene(bc,750,150);
        scene.getStylesheets().add("chart.css");
        return (scene);
    }
		public JButton loadImageBtn(String path) {
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
           btn.setPressedIcon(pressedIcon);
           btn.setDisabledIcon(disabledIcon);
           btn.setBorder(null);
           return btn;
        } catch (IOException e) {
           e.printStackTrace();
           return null;
   		}
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
		public static void refreshTable(){
			//defTableModel.removeRow(selectedRcm);
		    for (int i =0; i< station.getMachines().size(); i++) {
		    	defTableModel.removeRow(defTableModel.getRowCount()-1);
		    }
		    for (int i =0; i< station.getMachines().size(); i++) {
		    	Object[] newData1 = {i,
		    			station.getMachine(i).location,
		    			station.getMachine(i).presentCapacity, 
		    			station.getMachine(i).money,
		    			station.getMachine(i).Status,
		    			station.getMachine(i).listOfItems.size(),
		    			station.getMachine(i).lastEmptied};
	    			defTableModel.addRow(newData1);	    	
		    }
		}

		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
	        int column = e.getColumn();
	        if(e.getType()==e.UPDATE){
		        String columnName = table.getColumnName(column);
		        Object data = table.getValueAt(row, column);
		        if (columnName =="Location") {
		        	station.getMachine(selectedRcm).location = (String) data;
		        	System.out.print("= Location changed to "+(String) data);
		        }
		        if (columnName == "Capacity") {
		        	station.getMachine(selectedRcm).capacity = Double.parseDouble((String) data);
		        	System.out.print("= Capacity changed to "+(String) data);
		        }
		        
	        } else if (e.getType()==e.DELETE) {
	        	
	        }
		
		}
		public static void refreshGraphic(){
			System.out.print("= Refreshing graphic\n");
			Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	                initFX(fxPanel);
	            }
	       });

		}
		
}
