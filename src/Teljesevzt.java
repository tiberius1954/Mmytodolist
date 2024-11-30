import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.GregorianCalendar;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class Teljesevzt extends JFrame {
	Hhelper hh = new Hhelper();
	Calhelper cc = new Calhelper();	
	JLabel[] hoLabel = new JLabel[12];
	JPanel[] Panarr = new JPanel[12];	
	 protected String[] headers= cc.napnevek();
	 protected String[] months= cc.monthnames();	
	   int nowYear, nowMonth, nowDay, currentYear, currentMonth;	  
	   
	Teljesevzt(){
		setSize(1240, 690);
		setLayout(null);
		setLocationRelativeTo(null);
		 GregorianCalendar cal = new GregorianCalendar(); //Create calendar
	        nowDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
	        nowMonth = cal.get(GregorianCalendar.MONTH); //Get month
	        nowYear = cal.get(GregorianCalendar.YEAR); //Get year		 
	        currentYear = nowYear;
	        currentMonth = nowMonth; 	
	        hh.iconhere(this);
	     
		initcomponent();			
		}
	private void initcomponent(){
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {	
				Mainframe ts = new Mainframe();	
				ts.setVisible(true);			
				dispose();				
			}
		});
		setTitle("Whole year");
		cp = getContentPane();
		cp.setBackground(cc.hatter);
		fejPanel = new JPanel(null);
		fejPanel.setBounds(0, 0, 1225, 100);
		fejPanel.setBackground(cc.rozsaszin);
		lbheader = hh.flabel("WHOLE YEAR");
		lbheader.setBounds(510, 0, 300, 40);
		fejPanel.add(lbheader);
		add(fejPanel);
		aPanel = new JPanel(null);		
		aPanel.setBounds(0, 100, 1240, 570);
	 	aPanel.setBackground(cc.hatter);
	 	add(aPanel);
	 	createcmdyearGUI();
	 	cmbYear.setSelectedItem(String.valueOf(nowYear));
	 	
    hajra();
	}
	private void hajra() {
		for (int i = 0; i < 6; i++) {			  
	         Panarr[i] =  new Tombos(i).createpanel();	    
	         Panarr[i].setBounds(15 + i * 200, 20, 190, 230);	  
		      aPanel.add(Panarr[i]);	
		}
		for (int i = 6; i < 12; i++) {		
			 Panarr[i] =  new Tombos(i).createpanel();	
	        Panarr[i].setBounds(15 + (i -6) * 200, 290, 190, 230);		
		    aPanel.add(Panarr[i]);
		}			
	}
	private void createcmdyearGUI() {
		  lbevek = new JLabel ("Év");
		 lbevek.setFont(new Font("Tahoma", Font.BOLD, 14));			
		  lbevek.setBounds(569,50,30,30);
		  lbevek.setHorizontalAlignment( JLabel.CENTER );
		  lbevek.setOpaque(true);
		  fejPanel.add(lbevek);
		  cmbYear = new JComboBox();
		  cmbYear.setFont(new Font("Tahoma", Font.BOLD, 14));
		  for (int i= 2022; i<=2050; i++){
	            cmbYear.addItem(String.valueOf(i));
	        }	
		   cmbYear.setBounds(600, 50, 80, 30);
		   fejPanel.add(cmbYear);
		  cmbYear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {			  
		            if (cmbYear.getSelectedItem() != null){
		                String b = cmbYear.getSelectedItem().toString();
		                currentYear= Integer.parseInt(b);    
		       		   aPanel.removeAll();
		        		aPanel.repaint();
				        aPanel.revalidate();
		                hajra();
		            }
		        }
		    });		
	}	

	public static void main(String args[]) {
		Teljesevzt ts = new Teljesevzt();	
		ts.setVisible(true);
	}
	
	private class Tombos extends JPanel{	
		protected int zmonth;			
		public Tombos(int mymonth){	
			this.zmonth = mymonth;				
			add(createpanel());				
		}	
		
		JPanel createpanel() {
			JLabel lbMonth;
			JLabel lbYear;
	        lbMonth  = new JLabel();
	        lbYear = new JLabel();  
			JPanel myPanel = new JPanel(true);
			myPanel.setLayout(null);
	 		 myPanel.setBounds(0 , 0, 190, 240);			 	
			DefaultTableModel  tmodel =new DefaultTableModel(headers, 6){  //header names, rowcount
			     public boolean isCellEditable(int rowIndex, int mColIndex){
			    	 return false;
			    	 }
			     };			
			 	JTable tnaptar = new JTable(tmodel);		
			    tnaptar.setRowHeight(25);
			    tnaptar.setFont(new Font("Tahoma", Font.BOLD, 12));
			 	tnaptar.setTableHeader(new JTableHeader(tnaptar.getColumnModel()) {
					@Override
					public Dimension getPreferredSize() {
						Dimension d = super.getPreferredSize();
						d.height = 25;
						d.width = 25;
						return d;
					}
				});
			 	tnaptar.setDefaultRenderer(tnaptar.getColumnClass(0), new tblCalendarRenderer());
			 	 tnaptar.getTableHeader().setBackground(Color.orange);
			 	tnaptar.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
			 	JScrollPane jsp = new JScrollPane(tnaptar);
			 	jsp.setBounds(1, 30, 190, 210);			 
			   	myPanel.add(jsp);			        
		         lbMonth.setText(months[zmonth]);		
		         lbMonth.setBounds(80,5,100,20);
		         myPanel.add(lbMonth);	
		         lbMonth.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							onMouseClicked(e);
						}
					});		  	     
		
	       refsCalendar (zmonth, currentYear, tnaptar); 		
		    myPanel.add(jsp);
	      	return myPanel;
	}
		 public void refsCalendar(int month, int year, JTable tonaptar ){		      
		        int nod, som; //Number Of Days, Start Of Month	 		     
		        DefaultTableModel tamodel = (DefaultTableModel) tonaptar.getModel();
		        //Clear table
		        for (int i=0; i<6; i++){
		            for (int j=0; j<7; j++){
		                tamodel.setValueAt(null, i, j);
		            }
		        }
		        
		        //Get first day of month and number of days
		        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		        nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		        som = cal.get(GregorianCalendar.DAY_OF_WEEK);		        
		      
		        for (int i=1; i<=nod; i++){
		            int row = (int) ((i+som-2)/7);
		            int column  = (int) (i+som-2)%7;
		            tamodel.setValueAt(i, row, column);		     
		        }		     
		    }
	 
		 public	class tblCalendarRenderer extends DefaultTableCellRenderer{
		        public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
		            super.getTableCellRendererComponent(table, value, selected, focused, row, column);		       
		            if (column == 0 || column == 6){ //Week-end
		                setBackground(new Color(255, 220, 220));
		            }
		            else{ //Week
		                setBackground(new Color(255, 255, 255));
		            }
		            if (value != null){    
		             if (Integer.parseInt(value.toString()) == nowDay && zmonth == nowMonth && currentYear == nowYear){ //Today
		            	  setBackground(cc.rozsaszin);
		                }
		            }
		            setBorder(null);
		            setForeground(Color.black);		       
		            setHorizontalAlignment( JLabel.CENTER );		            
		            return this;
		        }
		    }
	}
	private void onMouseClicked(MouseEvent e) {
		JLabel target = (JLabel) e.getSource();
		String honap = target.getText();
	    System.out.println(honap);
	    int ho = cc.numofmonth(honap);
	    System.out.println(ho);
	    Mycalendar mc =  new Mycalendar(currentYear, ho); 
	    mc.setSize(1240, 670);
		mc.setLayout(null);
		mc.setLocationRelativeTo(null);
	    mc.setVisible(true);
	    dispose();
	}
	
	Container cp;
	JLabel lbheader, lbevek;
	JPanel fejPanel, aPanel; 
	JComboBox cmbYear;
}
