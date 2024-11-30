import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;
import net.proteanit.sql.DbUtils;

public class Mycalendar extends JFrame {
	Hhelper hh = new Hhelper();
	Calhelper cc = new Calhelper();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	
	int iyear = 0;
	int imonth = 0;
	int iday = 0;
	private String rowid = "";
	private int myrow = 0;
	private String rowidm = "";
	private int myrowm = 0;
	JPanel Mhpanel;
	JFrame myframe = this;	

	Mycalendar() {
		hh.aktyear = cc.mynow("year");
		hh.aktmonth = cc.mynow("month");
		hh.aktday = cc.mynow("day");	
		setSize(1240, 670);	
		setLayout(null);
		setLocationRelativeTo(null);
		initcomponents();
		dd.dtable_update(dtable, " mdate ='" + hh.aktdate + "'");
		dd.mtable_update(mtable, " pdate ='" + hh.aktdate + "'");
     hh.iconhere(this);
	}
	Mycalendar(int pyear, int pmonth) {
		hh.aktyear = pyear;
		hh.aktmonth = pmonth;
		hh.aktday = 1;	
		setSize(1240, 670);	
		setLayout(null);
		setLocationRelativeTo(null);
		initcomponents();
		dd.dtable_update(dtable, " mdate ='" + hh.aktdate + "'");
		dd.mtable_update(mtable, " pdate ='" + hh.aktdate + "'");		
	}	

	private void initcomponents() {
		UIManager.put("ComboBox.selectionBackground", hh.lpiros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.GREEN, 1));
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				if (hh.whichpanel(cardPanel) == "mpanel") {
					Mainframe ts = new Mainframe();	
					ts.setVisible(true);
					dispose();			
				}
			}
		});
		setTitle("Month calendar");
		cp = getContentPane();
		cp.setBackground(cc.hatter);
		fejpanel = new JPanel(null);
		fejpanel.setBounds(0, 0, 1225, 50);
		fejpanel.setBackground(cc.rozsaszin);
		lbheader = hh.flabel("MONTH CALENDAR");
		lbheader.setBounds(530, 5, 300, 40);
		fejpanel.add(lbheader);
		add(fejpanel);
		cards = new CardLayout();
		cardPanel = new JPanel(null);
		cardPanel.setLayout(cards);
		cardPanel.setBounds(10, 50, 1205, 540);
		Mpanel = makempanel();
		Mpanel.setName("mpanel");
		ePanel = makeepanel();
		Mpanel.add(ePanel);
		hh.aktdate = hh.ldatefrom(hh.aktyear, hh.aktmonth + 1, hh.aktday);
		buttPanel = new JPanel(null);
		buttPanel.setBackground(new Color(238, 238, 238));
		buttPanel.setBounds(0, 20, 300, 60);		
		createBtnGUI();
		Mpanel.add(buttPanel);
	     lbdatumom = new JLabel("", SwingConstants.CENTER);
		 lbdatumom.setBackground(Color.white);
		 lbdatumom.setFont(new Font("Tahoma", Font.BOLD, 14));
		 lbdatumom.setOpaque(true);
		lbdatumom.setBounds(100, 440, 90, 30);
	    datumkiir();
		Mpanel.add(lbdatumom);		
		Mhpanel = new Monthpanel1(hh.aktyear, hh.aktmonth, dtable, mtable, myframe);
		Mhpanel.setBounds(0, 80, 300, 290);
		Mpanel.add(Mhpanel);
		yearPanel = new JPanel(null);
		yearPanel.setBackground(new Color(238, 238, 238));
		yearPanel.setBounds(0, 370, 300, 50);
		Mpanel.add(yearPanel);	
		createcmdyearGUI();
	     cmbYear.setSelectedItem(String.valueOf(hh.aktyear));		
	
		cardPanel.add(Mpanel, "mpanel");	
		add(cardPanel);
		cards.show(cardPanel, "mpanel");
		
	}
	
	private void createBtnGUI() {
		btnprev = hh.cbutton("<<");
		btnprev.setBounds(80, 10, 60, 30);
		btnprev.setBackground(hh.vpiros);
		buttPanel.add(btnprev);		
		btnprev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {	    
				hh.aktday= 1;
             if (hh.aktmonth == 0){ //Back one year
                 hh.aktmonth = 11;
                 hh.aktyear -= 1;
             } else{ //Back one month
                 hh.aktmonth -= 1;
             }
             tabletorl();         
             refreshCalendar();
             tabfresh();
			}
			});			
		
		btnnext = hh.cbutton(">>");
		btnnext.setBounds(160, 10, 60, 30);
		btnnext.setBackground(hh.vpiros);
		buttPanel.add(btnnext);		
		btnnext.addActionListener(new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent evt) {
		hh.aktday= 1;
		if (hh.aktmonth == 11) { // Foward one year
			hh.aktmonth = 0;
			hh.aktyear += 1;
		} else { // Foward one month
			hh.aktmonth += 1;
		}
		tabletorl();
		refreshCalendar();	
		tabfresh();
	}
	});
	}
	private void tabletorl() {
	DefaultTableModel dd = (DefaultTableModel) dtable.getModel();
	dd.setRowCount(0);
	DefaultTableModel mm = (DefaultTableModel) mtable.getModel();
	mm.setRowCount(0);
	}
	private void tabfresh() {
	String smonth = hh.itos(hh.aktmonth + 1);
	String ssmonth = hh.padLeftZeros(smonth, 2);
	String nap =  hh.itos(hh.aktday);
	String mydate = hh.itos(hh.aktyear) + "-" + ssmonth+ "-" + hh.padLeftZeros(nap, 2);
    dd.dtable_update(dtable, " mdate ='" + mydate + "'");
	dd.mtable_update(mtable, " pdate ='" + mydate + "'");
	}
	
	private void createcmdyearGUI() {
		  cmbYear = new JComboBox();
		  cmbYear.setFont(new Font("Tahoma", Font.BOLD, 14));
		  for (int i= 2022; i<=2050; i++){
	            cmbYear.addItem(String.valueOf(i));
	        }	
		   cmbYear.setBounds(110, 0, 80, 30);
		   yearPanel.add(cmbYear);
		  cmbYear.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {			  
		            if (cmbYear.getSelectedItem() != null){
		                String b = cmbYear.getSelectedItem().toString();
		                hh.aktyear= Integer.parseInt(b);
		                refreshCalendar();
		            }
		        }
		    });		
	}

	private void refreshCalendar() {		
		Mpanel.remove(Mhpanel);
		Mpanel.repaint();
		Mpanel.revalidate();
		Mhpanel.removeAll();
		Mhpanel = new Monthpanel1(hh.aktyear, hh.aktmonth,dtable, mtable, myframe);
		Mhpanel.setBounds(0, 80, 300, 290);	
		Mhpanel.repaint();
		Mhpanel.revalidate();
		Mpanel.add(Mhpanel);
	}

	private JPanel makempanel() {
		JPanel mpanel = new JPanel(null);
		mpanel.setBounds(0, 0, 310, 550);
		mpanel.setBackground(cc.hatter);
		return mpanel;
	}

	private JPanel makeepanel() {
		JPanel epanel = new JPanel(null);
		epanel.setBounds(302, 10, 900, 540);
		epanel.setBackground(cc.hatter);
		lPanel = new JPanel(null);
		lPanel.setBounds(0, 10, 450, 520);
		lPanel.setBackground(cc.psargaszold);
		lPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.GRAY));
		epanel.add(lPanel);
		lbeheader1 = hh.clabel("Tasks");
		lbeheader1.setBounds(130, 10, 130, 25);
		lPanel.add(lbeheader1);
		dtable = hh.ztable();
		dtable.setBackground(cc.psarga);
		dtable.setSelectionBackground(cc.vvzold);
		dtable.setSelectionForeground(Color.BLACK);
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) dtable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment(SwingConstants.LEFT);
		dtable.setTableHeader(new JTableHeader(dtable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});

		hh.madeheader(dtable);
		dtable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				dtable.scrollRectToVisible(dtable.getCellRect(dtable.getRowCount() - 1, 0, true));
			}
		});
		dtable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				dtableMouseClicked(evt);
			}
		});

		jScrollPane1 = new JScrollPane(dtable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		dtable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, { null, null, null, null, null } },
				new String[] { "did", "Date", "Start", "End", "Task" }));
		hh.setJTableColumnsWidth(dtable, 420, 0, 0, 50, 50, 320);
		jScrollPane1.setViewportView(dtable);
		jScrollPane1.setBounds(15, 50, 420, 230);
		lPanel.add(jScrollPane1);

		lbstart = hh.clabel("Start");
		lbstart.setBounds(60, 300, 100, 25);
		lPanel.add(lbstart);
		String rtime = "06:00";
		startmodel = new SpinnerDateModel();
		starttime = hh.cspinner(startmodel);
		starttime.setBounds(170, 300, 70, 25);
		hh.madexxx(starttime, "T");
		starttime.setName("starttime");
		lPanel.add(starttime);
		starttime.setValue(hh.stringtotime(rtime));
		((JSpinner.DefaultEditor) starttime.getEditor()).getTextField().addFocusListener(sFocusListener);		
		starttime.addChangeListener(new ChangeListener() {
		    @Override
		    public void stateChanged(ChangeEvent e) {		      
		        starttime.requestFocus();
		    }
		});
		  
		lbend = hh.clabel("End");
		lbend.setBounds(200, 300, 100, 25);
		lPanel.add(lbend);
		endmodel = new SpinnerDateModel();
		endtime = hh.cspinner(endmodel);
		endtime.setBounds(310, 300, 70, 25);
		hh.madexxx(endtime, "T");
		endtime.setName("endtime");
		lPanel.add(endtime);
		rtime = "22:00";
		endtime.setValue(hh.stringtotime(rtime));
		((JSpinner.DefaultEditor) endtime.getEditor()).getTextField().addFocusListener(sFocusListener);

		lbnote = hh.clabel("Task");
		lbnote.setBounds(10, 340, 70, 25);
		lPanel.add(lbnote);

		txanote = new JTextArea();
		txanote.setFont(hh.textf2);
		txanote.setBounds(90, 340, 340, 100);
		txanote.setCaretColor(Color.RED);
		txanote.putClientProperty("caretAspectRatio", 0.1);
		txanote.setLineWrap(true);
		jScrollPane2 = new JScrollPane(txanote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane2.setBounds(90, 340, 340, 100);
		jScrollPane2.setViewportView(txanote);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		txanote.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		lPanel.add(jScrollPane2);

		btnsave = hh.cbutton("Save");
		btnsave.setBounds(100, 460, 100, 25);
		btnsave.setBackground(cc.vred);
		lPanel.add(btnsave);

		btnsave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dtablesavebutt();
			}
		});

		btncancel = hh.cbutton("Cancel");
		btncancel.setBackground(cc.vzold);
		btncancel.setBounds(210, 460, 100, 25);
		lPanel.add(btncancel);
		btncancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dtclearFields();
			}
		});
		btndelete = hh.cbutton("Delete");
		btndelete.setBounds(310, 460, 100, 25);
		btndelete.setBackground(cc.ssarga);
		lPanel.add(btndelete);
		btndelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dtabledata_delete();
			}
		});

		rPanel = new JPanel(null);
		rPanel.setBounds(452, 10, 450, 520);
		rPanel.setBackground(cc.psargaszold);
		rPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, Color.GRAY));
		epanel.add(rPanel);
		lbeheader2 = hh.clabel("Money movements");
		lbeheader2.setBounds(130, 10, 180, 25);
		rPanel.add(lbeheader2);

		mtable = hh.ztable();
		mtable.setBackground(cc.psarga);
		mtable.setSelectionBackground(cc.vvzold);
		mtable.setSelectionForeground(Color.BLACK);
		DefaultTableCellRenderer mrenderer = (DefaultTableCellRenderer) mtable.getDefaultRenderer(Object.class);
		mrenderer.setHorizontalAlignment(SwingConstants.LEFT);
		mtable.setTableHeader(new JTableHeader(mtable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});

		hh.madeheader(mtable);
		mtable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				mtable.scrollRectToVisible(mtable.getCellRect(mtable.getRowCount() - 1, 0, true));
			}
		});
		mtable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				mtableMouseClicked(evt);
			}
		});

		jScrollPane3 = new JScrollPane(mtable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		mtable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, { null, null, null, null, null } },
				new String[] { "bid", "Date", "Money movement", "Income", "Outcome" }));
		hh.setJTableColumnsWidth(mtable, 420, 0, 0, 280, 70, 70);
		jScrollPane3.setViewportView(mtable);
		jScrollPane3.setBounds(15, 50, 420, 230);
		rPanel.add(jScrollPane3);

		lbname = hh.clabel("Movement");
		lbname.setBounds(10, 310, 120, 20);
		rPanel.add(lbname);
		txaname = new JTextArea();
		txaname.setFont(hh.textf2);
		txaname.setBounds(145, 340, 340, 100);
		txaname.setCaretColor(Color.RED);
		txaname.putClientProperty("caretAspectRatio", 0.1);
		txaname.setLineWrap(true);
		jScrollPane4 = new JScrollPane(txaname, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane4.setBounds(145, 290, 280, 60);
		jScrollPane4.setViewportView(txaname);
		border = BorderFactory.createLineBorder(Color.BLACK);
		txanote.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(2, 2, 2, 2)));
		rPanel.add(jScrollPane4);
		txaname.requestFocus();

		lbincome = hh.clabel("Income");
		lbincome.setBounds(10, 370, 120, 20);
		rPanel.add(lbincome);

		txincome = cTextField(25);
		txincome.setHorizontalAlignment(JTextField.RIGHT);
		txincome.setBounds(145, 370, 150, 25);
		rPanel.add(txincome);
		txincome.addKeyListener(hh.Onlynum());

		lboutcome = hh.clabel("Outcome");
		lboutcome.setBounds(10, 410, 120, 20);
		rPanel.add(lboutcome);

		txoutcome = cTextField(25);
		txoutcome.setHorizontalAlignment(JTextField.RIGHT);
		txoutcome.setBounds(145, 410, 150, 25);
		rPanel.add(txoutcome);
		txoutcome.addKeyListener(hh.Onlynum());

		btnmsave = hh.cbutton("Save");
		btnmsave.setBounds(80, 460, 100, 25);
		btnmsave.setBackground(cc.vred);
		rPanel.add(btnmsave);

		btnmsave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				mtablesavebutt();
			}
		});

		btnmcancel = hh.cbutton("Cancel");
		btnmcancel.setBackground(cc.vzold);
		btnmcancel.setBounds(190, 460, 100, 25);
		rPanel.add(btnmcancel);
		btnmcancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				mclearFields();
			}
		});
		btnmdelete = hh.cbutton("Delete");
		btnmdelete.setBounds(290, 460, 100, 25);
		btnmdelete.setBackground(cc.ssarga);
		rPanel.add(btnmdelete);
		btnmdelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				mtabledata_delete();
			}
		});
		return epanel;
	}

	private final FocusListener sFocusListener=new FocusListener(){
		@Override 
		public void focusGained(FocusEvent e){
			JComponent c=(JComponent)e.getSource();
			}
	@Override 
	public void focusLost(FocusEvent e){JComponent b=(JComponent)e.getSource();
	if(b.getParent().getParent().getName()=="starttime")
	{
		JTextField intf=((JSpinner.DefaultEditor)starttime.getEditor()).getTextField();
		      String intime=intf.getText();
	if(hh.correcttime(intime)==true){
		JOptionPane.showMessageDialog(null,"Correct time is 00:00 - 24:00 !");
		}
	}else{
    		JTextField outtf=((JSpinner.DefaultEditor)endtime.getEditor()).getTextField();
        	String outtime=outtf.getText();
    	if(hh.correcttime(outtime)==true)
    	{
	    	JOptionPane.showMessageDialog(null,"Correct time is 00:00 - 24:00 !");
    	}	
	}}};

	private void dtablesavebutt() {
		String sql = "";
		String jel = "";
		String note = txanote.getText();
		JTextField intf = ((JSpinner.DefaultEditor) starttime.getEditor()).getTextField();
		String sttime = intf.getText();
		intf = ((JSpinner.DefaultEditor) endtime.getEditor()).getTextField();
		String etime = intf.getText();
		hh.aktdate = hh.ldatefrom(hh.aktyear, hh.aktmonth + 1, hh.aktday);

		if (dtablevalidation(note, sttime, etime) == false) {	
			return;
		}
		if (rowid != "") {
			jel = "UP";
			sql = "update  todolist set mdate= '" + hh.aktdate + "', start= '" + sttime + "',end='" + etime + "',note='"
					+ note + "'  where tid = " + rowid;
		} else {
			sql = "insert into todolist (mdate, start, end, note) " + "values ('" + hh.aktdate + "','" + sttime + "'"
					+ ",'" + etime + "','" + note + "')";
		}
		try {
			int flag = dh.Insupdel(sql);
			if (flag == 1) {
				hh.ztmessage("Success", "Message");
				dd.dtable_update(dtable, "mdate ='" + hh.aktdate + "'");
			} else {
				JOptionPane.showMessageDialog(null, "sql error !");
			}
		} catch (Exception e) {
			System.err.println("SQLException: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "sql insert hiba");
		}
		dtclearFields();
		refreshCalendar();
	}

	private void dtclearFields() {
		txanote.setText("");
		String rtime = "06:00";
		starttime.setValue(hh.stringtotime(rtime));
		rtime = "22:00";
		endtime.setValue(hh.stringtotime(rtime));
		myrow = 0;
		rowid = "";
	}

	private void dtableMouseClicked(java.awt.event.MouseEvent evt) {
		int row = dtable.getSelectedRow();
		if (row >= 0) {
			txanote.setText(dtable.getValueAt(row, 4).toString());
			String arrtime = dtable.getValueAt(row, 2).toString();
			startmodel.setValue(hh.stringtotime(arrtime));
			String deptime = dtable.getValueAt(row, 3).toString();
			endmodel.setValue(hh.stringtotime(deptime));
			rowid = dtable.getValueAt(row, 0).toString();
			myrow = row;						
		}
	}

	private void mtablesavebutt() {
		String sql = "";
		String jel = "";
		String name = txaname.getText();
		String income = txincome.getText();
		String outcome = txoutcome.getText();
		hh.aktdate = hh.ldatefrom(hh.aktyear, hh.aktmonth + 1, hh.aktday);

		if (hh.zempty(name)) {
			JOptionPane.showMessageDialog(null, "Tölcsd ki  a megnevezés mezőt !");
			return;
		}
		if (rowidm != "") {
			jel = "UP";
			sql = "update  budget set pdate= '" + hh.aktdate + "', name = '" + name + "', income= '" + income
					+ "', outcome ='" + outcome + "'  where bid = " + rowidm;
		} else {
			sql = "insert into budget (pdate, name, income, outcome) values ('" + hh.aktdate + "','" + name + "','"
					+ income + "','" + outcome + "')";
		}
		try {
			int flag = dh.Insupdel(sql);
			if (flag == 1) {
				hh.ztmessage("Success", "Message");
				dd.mtable_update(mtable, "pdate ='" + hh.aktdate + "'");
			} else {
				JOptionPane.showMessageDialog(null, "sql error !");
			}
		} catch (Exception e) {
			System.err.println("SQLException: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "sql insert hiba");
		}
		mclearFields();
		refreshCalendar();
	}

	private void mclearFields() {
		txaname.setText("");
		txincome.setText("");
		txoutcome.setText("");
		myrowm = 0;
		rowidm = "";

	}

	private void mtableMouseClicked(java.awt.event.MouseEvent evt) {
		int row = mtable.getSelectedRow();
		if (row >= 0) {
			txaname.setText(mtable.getValueAt(row, 2).toString());
			txincome.setText(mtable.getValueAt(row, 3).toString());
			txoutcome.setText(mtable.getValueAt(row, 4).toString());
			rowidm = mtable.getValueAt(row, 0).toString();
			myrowm = row;
		}
	}

	private void dtabledata_delete() {
		int row = dtable.getSelectedRow();
		String sql = "delete from todolist where did=";
		if (row >= 0) {
			dd.data_delete(dtable, sql);
			dtclearFields();
			refreshCalendar();
		}
	}

	private void mtabledata_delete() {
		int row = mtable.getSelectedRow();
		String sql = "delete from  budget where bid=";
		if (row >= 0) {
			dd.data_delete(mtable, sql);
			mclearFields();
			refreshCalendar();
		}
	}
	public void datumkiir() {
		String sdate = hh.ldatefromzt(hh.aktyear, hh.aktmonth+1,hh.aktday);
		lbdatumom.setText(sdate);
	}
	Boolean dtablevalidation(String note, String sttime, String etime) {
		Boolean ret = true;
		ArrayList<String> err = new ArrayList<String>();

		if (hh.zempty(note)){
			err.add("A feladat üresen maradt !");
			ret = false;			
		}
		
	 	if (hh.twotime(sttime, etime) == false) {
	 		err.add("A vége idő kisebb mint a start !");
			ret = false;	
	 	}
	 	if (err.size() > 0) {
			JOptionPane.showMessageDialog(null, err.toArray(), "Hiba üzenet", 1);
		}			
		return ret;
	}

	
	public static void main(String args[]) {
		Mycalendar ts = new Mycalendar();
		ts.setSize(1240, 670);	
		ts.setVisible(true);
	}

	public JTextField cTextField(int hossz) {
		JTextField textField = new JTextField(hossz);
		textField.setFont(hh.textf2);
		textField.setBorder(hh.borderf);
		textField.setBackground(hh.feher);
		textField.setPreferredSize(new Dimension(250, 30));
		textField.setCaretColor(Color.RED);
		textField.putClientProperty("caretAspectRatio", 0.1);
		// textField.setHorizontalAlignment(JTextField.RIGHT)
		// textField.addFocusListener(dFocusListener);
		textField.setText("");
		textField.setDisabledTextColor(Color.magenta);
		return textField;
	}

	CardLayout cards;
	JPanel cardPanel, Mpanel, Monthpanel, ePanel, fejpanel, lPanel, rPanel, buttPanel, yearPanel;
	JLabel lbheader, lbeheader1, lbeheader2, lbdatumom;
	JLabel lbstart, lbend, lbnote;
	JTable dtable, mtable;
	JScrollPane jScrollPane1, jScrollPane2, jScrollPane3, jScrollPane4;
	JTextArea txanote, txaname;
	JTextField txincome, txoutcome;
	JLabel lbname, lbincome, lboutcome;
	Container cp;
	SpinnerDateModel startmodel, endmodel;
	JSpinner starttime, endtime;
	JButton btnsave, btncancel, btndelete, btnmsave, btnmcancel, btnmdelete;
	JButton btnnext, btnprev;
	 JComboBox cmbYear;
}
