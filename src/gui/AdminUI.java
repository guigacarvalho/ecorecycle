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
import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


@SuppressWarnings("serial")
public class AdminUI extends JPanel implements  ActionListener, 
												TableModelListener, 
												WindowListener{
	//UI Variables Declaration
	JToggleButton unit, cash;
	JButton newMachine, removeMachine, changeItem, addItem,activateRCM,loadMachine, EmptyMachine,submitBt ;
	JTextArea transactionItemsTextArea;
	JTextField loginTF, passwdTF;
	JLabel totalAmount,machinesLabel, statsLabel, itemTypes, loginL, passwdL, loginStatusL;
    JLabel [] separator = new JLabel[10];
    final static JFXPanel fxPanel = new JFXPanel();
    static DefaultTableModel defTableModel;
	private JTable table;
	Container topMenuContainer, loginContainer, statsContainer;
	
    String[] columnNames = {"ID",
            "Location",
            "Capacity",
            "$ Available",
            "Status",
            "Item Types",
            "Last Emptied"};
	private final String IMG_PATHS[] = {
			"./img/auiHeader.png",
			"./img/RecyclingMachines.png",
			"cuiUnitKgButton",
			"./img/newMachine.png",
			"./img/ItemTypes.png",
			"./img/buttons/Slice-",
			"./img/Stats.png",
			"./img/logo.png"};
	static JFrame frame;

	//Model variables
	public static RMOS station = new RMOS();
	private int selectedRcm;
	private  String [] usr= {"Guilherme", "Ankit"};
	private String passwd = "admin";
    private static final Logger fLogger = Logger.getLogger(EcoReSystem.class.getPackage().getName());

	
	
	//Class constructor
	public AdminUI (RMOS stationRecovered) {
		//Basic Layout Definitions
		super(new FlowLayout());
		AdminUI.station=stationRecovered;
		this.setBackground(Color.WHITE);
		loadLoginScreen();
		
	}
	private boolean authenticate() {
		if ((loginTF.getText().contentEquals(usr[1]) ||
				loginTF.getText().contentEquals(usr[0]) ) 
				&& passwdTF.getText().contentEquals(passwd)  ) {
			System.out.print("Login OK!\n");			
			return true;
		}
		else {
			System.out.print("= Login not OK!\n");
			return false;
		}
	}
	private void loadAdminPanel() {
	loadTable();

	loadMenuBar();

	//Creating the header
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
	
	Container statsContainer = new Container();
	statsContainer .setLayout(new GridLayout(2,2));
	
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("General", null, fxPanel, "Presents the general statistics");
    tabbedPane.addTab("RCM", null, statsContainer, "Presents the statistics about a specific RCM");
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
	private void loadLoginScreen(){
		
		JLabel logo = loadImage(IMG_PATHS[7]);
		
		loginL = new JLabel("Login");
		loginL.setFont(new Font("Lobster 1.4", Font.BOLD, 16));
		loginL.setForeground(new Color(83,88,95));
		loginL.setPreferredSize(new Dimension(50,20));
		
		loginTF = new JTextField();
		loginTF.setPreferredSize(new Dimension(100,20));
		
		passwdL = new JLabel("Password");
		passwdL.setBorder(null);
		passwdL.setFont(new Font("Lobster 1.4", Font.BOLD, 16));
		passwdL.setForeground(new Color(83,88,95));
		passwdL.setPreferredSize(new Dimension(70,20));

		passwdTF = new JPasswordField();
		passwdTF.setPreferredSize(new Dimension(100,20));
		passwdTF.setSize(100,20);
	    submitBt = new JButton("Submit");
	    submitBt.setBorder(null);
	    submitBt.setFont(new Font("Lobster 1.4", Font.BOLD, 16));
	    submitBt.setForeground(new Color(42,195,207));
	    submitBt.addActionListener(this);

		loginStatusL = new JLabel("");
		loginStatusL.setFont(new Font("Lobster 1.4", Font.BOLD, 16));
		loginStatusL.setForeground(new Color(83,88,95));
		loginStatusL.setPreferredSize(new Dimension(240,20));


		loginContainer = new Container();
		loginContainer.setLayout(new FlowLayout());
		loginContainer.add(logo);
		loginContainer.add(loginL);
		loginContainer.add(loginTF);
		loginContainer.add(passwdL);
		loginContainer.add(passwdTF);
	    loginContainer.add(submitBt);
		loginContainer.add(loginStatusL);
		add(loginContainer);
	}
		private void loadMenuBar() {
		    //Creating the menu bar
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
		    
		    for (int i =0; i<separator.length;i++) {
			    separator[i]= new JLabel(" = ");
			    separator[i].setBorder(null);
			    separator[i].setFont(new Font("Letter Gothic Std", Font.BOLD, 12));
			    separator[i].setForeground(new Color(166,170,169));
		    }
			topMenuContainer = new Container();
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
	}

		private void loadTable() {
			//JTable initialization
			Object[] newData = {0,"","","","","",""};
		    Object[][] data = {newData};

		    // Preventing the user input
		    defTableModel = new DefaultTableModel(data,columnNames) {
		    	  public boolean isCellEditable(int row, int column) {
		    	    	   return false;
		    	    }
		    };
		    
		    //Instantiating the JTable
			table = new JTable(defTableModel);
			
			//Adding the selection handler
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
	        ListSelectionModel rowSM = table.getSelectionModel();
	        rowSM.addListSelectionListener(new ListSelectionListener() {
	            public void valueChanged(ListSelectionEvent e) {
	                if (e.getValueIsAdjusting()) return;
	                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	                if (lsm.isSelectionEmpty()) {
	                	selectedRcm =-1;
	                } else {
	                    selectedRcm = lsm.getMinSelectionIndex();
	                }
	            }
	        });
		    defTableModel.addTableModelListener(this);	    
		    defTableModel.removeRow(0);
	        //Populating the table
		    for (int i =0; i< station.getMachines().size(); i++) {
		    	Object[] newData1 = {i,
		    			station.getMachine(i).location,
		    			station.getMachine(i).presentCapacity, 
		    			station.getMachine(i).money,
		    			station.getMachine(i).Status,
		    			station.getMachine(i).listOfItems.size(),
		    			station.getMachine(i).getLastEmptied()};
	    			defTableModel.addRow(newData1);	    	
		    }		
	}

		private void loadStats() {

//			mostUsedL = new JLabel("Most Used machine in the last _ days");
//			mostUsedL.setFont(new Font("Lobster 1.4", Font.BOLD, 16));
//			mostUsedL.setForeground(new Color(83,88,95));
//			mostUsedL.setPreferredSize(new Dimension(50,20));
//			
//			mostUsedTF = new JTextField();
//			mostUsedTF.setPreferredSize(new Dimension(100,20));
//			
//			passwdL = new JLabel("Password");
//			passwdL.setBorder(null);
//			passwdL.setFont(new Font("Lobster 1.4", Font.BOLD, 16));
//			passwdL.setForeground(new Color(83,88,95));
//			passwdL.setPreferredSize(new Dimension(70,20));

			passwdTF = new JPasswordField();
			passwdTF.setPreferredSize(new Dimension(100,20));
			passwdTF.setSize(100,20);
		    submitBt = new JButton("Submit");
		    submitBt.setBorder(null);
		    submitBt.setFont(new Font("Lobster 1.4", Font.BOLD, 16));
		    submitBt.setForeground(new Color(42,195,207));
		    submitBt.addActionListener(this);

			loginStatusL = new JLabel("");
			loginStatusL.setFont(new Font("Lobster 1.4", Font.BOLD, 16));
			loginStatusL.setForeground(new Color(83,88,95));
			loginStatusL.setPreferredSize(new Dimension(240,20));


			statsContainer = new Container();
			statsContainer .setLayout(new GridLayout(2,2));
			statsContainer .add(loginL);
			statsContainer .add(loginTF);
			statsContainer .add(passwdL);
			statsContainer .add(passwdTF);
			statsContainer .add(submitBt);
			statsContainer .add(loginStatusL);
			add(statsContainer );
			
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
						 else if(e.getSource() == submitBt){
							 if(authenticate()==true) {
								 hideLoginPanel();
								 loadAdminPanel();
							 } else {
								 loginStatusL.setText("Try a valid user/passwd combination.");
							 }
						 }
		       }
   
		
		private void hideLoginPanel() {
			loginContainer.setVisible(false);
			
		}
		public void createAndShowGUI() {

        //Create and set up the window.
        frame = new JFrame("RMOS");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Add content to the window.
        frame.add(new AdminUI(station));

        //Display the window.
        frame.setBounds(200, 200, 800, 600);
        frame.setVisible(true);
        frame.addWindowListener(this);
        
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
		public static void refreshStatistics () {
       		System.out.print(station.getMachine(0).getNumberOfTransaction(7)+""+
       		station.getMachine(0).getTotalValueOfMachinePerDay()+
       		station.getMachine(0).getTotalValueOfMachinePerWeek()+
       		station.getMaxTransactionMachine(7)+
       		station.returnMostUsedMachine().location);

		}
		
		@Override
		public void windowClosing(WindowEvent e) {
			System.out.print("Closing Window.\n");
		        ActionListener task = new ActionListener() {
		            boolean alreadyDisposed = false;
		            public void actionPerformed(ActionEvent e) {
		                if (frame.isDisplayable()) {
		                    alreadyDisposed = true;
		                    frame.dispose();
		                }
		            }
		        };
		        Timer timer = new Timer(500, task); //fire every half second
		        timer.setRepeats(false);
		        timer.start();
		      //serialize the List
		        try (
		          OutputStream file = new FileOutputStream("data.ser");
		          OutputStream buffer = new BufferedOutputStream(file);
		          ObjectOutput output = new ObjectOutputStream(buffer);
		        ){
		          output.writeObject(station);
		        }  
		        catch(IOException ex){
		          fLogger.log(Level.SEVERE, "Cannot perform output.", ex);
		        }
		
		}
		@Override
		public void windowOpened(WindowEvent e) {}
		@Override
		public void windowClosed(WindowEvent e) {}
		@Override
		public void windowIconified(WindowEvent e) {}
		@Override
		public void windowDeiconified(WindowEvent e) {}
		@Override
		public void windowActivated(WindowEvent e) {}
		@Override
		public void windowDeactivated(WindowEvent e) {}		
}