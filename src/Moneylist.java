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
import java.util.Date;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.toedter.calendar.JDateChooser;

public class Moneylist extends JFrame {
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

	Moneylist() {
		initcomponents();
	}

	private void initcomponents() {
		setSize(800, 620);
		setLayout(null);
		setLocationRelativeTo(null);
		hh.iconhere(this);
		setTitle("Money movement list");
		this.getContentPane().setBackground(cc.vszold);

		fejpanel = new JPanel(null);
		fejpanel.setBounds(0, 0, 785, 50);
		fejpanel.setBackground(cc.rozsaszin);
		lbheader = hh.flabel("Money movement list");
		lbheader.setBounds(230, 5, 400, 40);
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
		lbidoszak.setBounds(125, 70, 70, 25);
		add(lbidoszak);
		sdate.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		sdate.setDateFormatString("yyyy-MM-dd");
		sdate.setFont(new Font("Arial", Font.BOLD, 16));
		sdate.setBounds(205, 70, 120, 25);
		add(sdate);
		lbjel = hh.clabel(" - ");
		lbjel.setBounds(322, 70, 20, 25);
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

		montable = hh.ztable();
		DefaultTableCellRenderer birenderer = (DefaultTableCellRenderer) montable.getDefaultRenderer(Object.class);
		birenderer.setHorizontalAlignment(SwingConstants.LEFT);
		montable.setTableHeader(new JTableHeader(montable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});

		hh.madeheader(montable);
		montable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				montable.scrollRectToVisible(montable.getCellRect(montable.getRowCount() - 1, 0, true));
			}
		});
		monPane = new JScrollPane(montable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		montable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
				new String[] { "bid", "Date", "Money movement", "Income", "Outcime" }));
		hh.setJTableColumnsWidth(montable, 755, 0, 13, 57, 15, 15);
		DefaultTableCellRenderer rrenderer = (DefaultTableCellRenderer) montable.getDefaultRenderer(Object.class);

		monPane.setViewportView(montable);
		monPane.setBounds(15, 125, 755, 320);
		add(monPane);

		btnprint = hh.cbutton("Print");
		btnprint.setBounds(300, 480, 130, 30);
		btnprint.setBackground(hh.lpiros);
		add(btnprint);
		btnprint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					   DefaultTableModel m1 = (DefaultTableModel) montable.getModel();	
						if (m1.getRowCount() <= 0) {
							return;
						}			
					PrinterJob job = PrinterJob.getPrinterJob();
					job.setJobName("moneylist");
					HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
					attr.add(new MediaPrintableArea(10, 10, 190, 275, MediaPrintableArea.MM));
					job.print(attr);

					MessageFormat[] header = new MessageFormat[4];
					header[0] = new MessageFormat(hss.center("MONEY MOVEMENT LIST", 170));
					header[1] = new MessageFormat("");
					header[2] = new MessageFormat("Term: " + ssdate + " - " + vvdate);
					header[3] = new MessageFormat("");

					MessageFormat[] footer = new MessageFormat[1];
					footer[0] = new MessageFormat(hss.center("Page {0,number,integer}", 170));
					job.setPrintable(hh.new MyTablePrintable(montable, JTable.PrintMode.FIT_WIDTH, header, footer));
					job.printDialog();
					job.print();			
				//	dispose();

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
		String swhere = "pdate >= date('" + ssdate + "') and pdate <=" + " date('" + vvdate + "')";
		table_update(swhere);
		return;
	}

	void table_update(String swhere) {
		DefaultTableModel m1 = (DefaultTableModel) montable.getModel();
		m1.setRowCount(0);
		double intotal = 0;
		double outtotal= 0;
		double iintotal = 0;
		double ioutotal  = 0;
		String Sql = "select bid,  pdate, name, income,  outcome  from budget" + " where " + swhere;
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String bid = rs.getString("bid");
				String pdate = rs.getString("pdate");
				String name = rs.getString("name");
				String income = rs.getString("income");
				String outcome = rs.getString("outcome");
				iintotal =  iintotal  +  hh.stodd(income);
				ioutotal =  ioutotal + hh.stodd(outcome);
				m1.addRow(new Object[] { bid, pdate, name, income, outcome });
			}
			String sss = hh.ddtos(iintotal);
			String oss = hh.ddtos(ioutotal);
			String isumtotal = hh.bsf(sss);
			String osumtotal = hh.bsf(oss);			
			m1.addRow(new Object[] { "", "", "Total", isumtotal, osumtotal });			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}

		String[] fej = { "bid", "Date", "Money movement", "Income", "Outcome" };
		((DefaultTableModel) montable.getModel()).setColumnIdentifiers(fej);
		hh.setJTableColumnsWidth(montable, 755, 0, 13, 57, 15, 15);
		montable.setShowGrid(true);
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		montable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		montable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);		
		JTableHeader header = montable.getTableHeader();
	}

	public static void main(String args[]) {
		Moneylist ts = new Moneylist();
		ts.setVisible(true);
	}

	JLabel lbidoszak, lbheader, lbjel;
	JPanel fejpanel;
	JTable montable;
	JScrollPane monPane;
	JButton btnprint, btnsearch;

}
