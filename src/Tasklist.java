import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class Tasklist extends JFrame{
	Hhelper hh = new Hhelper();
	 Hhelper.StringUtils hss =   new Hhelper.StringUtils();
	 Calhelper cc = new Calhelper();		
	JDateChooser sdate = new JDateChooser(new Date());
	JDateChooser vdate = new JDateChooser(new Date());
	ResultSet rs;
	Connection con;
	Statement stmt;
	DatabaseHelper dh = new DatabaseHelper();
	String ssdate, vvdate;
	
Tasklist(){
	initcomponents();		
	}

private 	void initcomponents() {
	setSize(800, 620);
	setLayout(null);
	setLocationRelativeTo(null);
	hh.iconhere(this);
	setTitle("Task list");
	this.getContentPane().setBackground(cc.vszold);	
	fejpanel = new JPanel(null);
	fejpanel.setBounds(0, 0, 785, 50);
	fejpanel.setBackground(cc.rozsaszin);
	lbheader = hh.flabel("TASK LIST");
	lbheader.setBounds(280, 5, 180, 40);
	fejpanel.add(lbheader);
	add(fejpanel);
	setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent windowEvent) {		
		    	Mainframe ts = new Mainframe();	
				ts.setVisible(true);
				dispose();	   		
		}
	});
	 lbidoszak = hh.clabel("Term");
	lbidoszak.setBounds(120, 70, 70,25);
 	add(lbidoszak);
	sdate.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
	sdate.setDateFormatString("yyyy-MM-dd");
	sdate.setFont(new Font("Arial", Font.BOLD, 16));
	sdate.setBounds(205, 70, 120, 25);
	add(sdate);
	lbjel = hh.clabel(" - ");
	lbjel.setBounds(322,70,20,25);
	add(lbjel);
	
	vdate.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
	vdate.setDateFormatString("yyyy-MM-dd");
	vdate.setFont(new Font("Arial", Font.BOLD, 16));
	vdate.setBounds(346, 70, 120, 25);
	add(vdate);
	
	btnsearch = hh.cbutton("Filter");
	btnsearch.setForeground(hh.skek);
	btnsearch.setBackground(Color.ORANGE);
	btnsearch.setBounds(476, 70, 90, 25);
	btnsearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
   add(btnsearch);
	btnsearch.addActionListener((ActionListener) new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			sqlgyart();
		}
	});	
	
	tasktable = hh.ztable();
	DefaultTableCellRenderer birenderer = (DefaultTableCellRenderer) tasktable.getDefaultRenderer(Object.class);
	birenderer.setHorizontalAlignment(SwingConstants.LEFT);
	tasktable.setTableHeader(new JTableHeader(tasktable.getColumnModel()) {
		@Override
		public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			d.height = 25;
			return d;
		}
	});
	
	hh.madeheader(tasktable);
	tasktable.addComponentListener(new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			tasktable.scrollRectToVisible(tasktable.getCellRect(tasktable.getRowCount() - 1, 0, true));
		}
	});
	taskPane = new JScrollPane(tasktable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	tasktable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
			new String[] { "did",  "Date", "Task","Start", "End" }));
	hh.setJTableColumnsWidth(tasktable, 755, 0, 13, 67, 10, 10);
	DefaultTableCellRenderer rrenderer = (DefaultTableCellRenderer) tasktable.getDefaultRenderer(Object.class);
   taskPane.setViewportView(tasktable);
	taskPane.setBounds(15, 125, 755, 320);	
	add(taskPane);
	
	btnprint = hh.cbutton("Print");
	btnprint.setBounds(300, 480, 130, 30);
	btnprint.setBackground(hh.lpiros);
	add(btnprint);
	btnprint.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent evt) {
			try {				
		     DefaultTableModel m1 = (DefaultTableModel) tasktable.getModel();	
			if (m1.getRowCount() <= 0) {
				return;
			}				
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setJobName("tasklist");
			HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();		
			attr.add(new MediaPrintableArea(10, 10, 190, 275, MediaPrintableArea.MM));
			job.print(attr);
	
			MessageFormat[] header = new MessageFormat[4];
			header[0] = new MessageFormat(hss.center("TASK LISTA", 170));
			header[1] = new MessageFormat("");
			header[2] = new MessageFormat("Term: "+ ssdate +" - " + vvdate);
			header[3] = new MessageFormat("");				
			
			MessageFormat[] footer = new MessageFormat[1];		
			footer[0] = new MessageFormat(hss.center("Page {0,number,integer}", 170));
			job.setPrintable(hh.new MyTablePrintable(tasktable, JTable.PrintMode.FIT_WIDTH, header, footer));
			job.printDialog();
			job.print();		
			
			} catch (java.awt.print.PrinterAbortException e) {
			} catch (PrinterException ex) {
				System.err.println("Error printing: " + ex.getMessage());
				ex.printStackTrace();
			}		
		}
	});	
}

private void sqlgyart() {	
	 ssdate = ((JTextField) sdate.getDateEditor().getUiComponent()).getText();
	 vvdate = ((JTextField) vdate.getDateEditor().getUiComponent()).getText();
	String swhere = "mdate >= date('" + ssdate +"') and mdate <="
			+ " date('" + vvdate +"')";		
		table_update(swhere);
return;
}

void table_update(String swhere) {
	DefaultTableModel m1 = (DefaultTableModel) tasktable.getModel();	
  m1.setRowCount(0);
	String Sql = "select did,  mdate, note, start,  end  from todolist"
			+ " where " + swhere;	
	try {
		rs = dh.GetData(Sql);
		while (rs.next()) {					
			String did = rs.getString("did");				
			String mdate = rs.getString("mdate");
			String note= rs.getString("note");
			String start = rs.getString("start");
			String end = rs.getString("end");					
			m1.addRow(new Object[] { did, mdate, note,start, end });		
		}
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		dh.CloseConnection();		
	}


	String[] fej ={ "did",  "Date", "Task","Start", "End" };
	((DefaultTableModel) tasktable.getModel()).setColumnIdentifiers(fej);	
	hh.setJTableColumnsWidth(tasktable, 755, 0, 13, 67, 10, 10);	
	   tasktable.setShowGrid(true);
	JTableHeader header = tasktable.getTableHeader();  
}	

	public static void main(String args[]) {
		Tasklist ts = new Tasklist();	
		ts.setVisible(true);
	}
JLabel lbidoszak, lbheader, lbjel;
JPanel fejpanel;
JTable tasktable;
JScrollPane taskPane;
JButton 	btnprint, btnsearch;


}
