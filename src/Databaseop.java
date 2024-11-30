

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.proteanit.sql.DbUtils;

public class Databaseop {
	Connection con;
	Statement stmt;
	PreparedStatement pst;
	ResultSet rs;	
	DatabaseHelper dh = new DatabaseHelper();
	Hhelper hh = new Hhelper();

	public int data_delete(JTable dtable, String sql) {
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		int sIndex = dtable.getSelectedRow();
		if (sIndex < 0) {
			return flag;
		}
		String iid = d1.getValueAt(sIndex, 0).toString();
		if (iid.equals("")) {
			return flag;
		}	
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
			String vsql = sql + iid;
			flag = dh.Insupdel(vsql);
			if (flag > 0) {
				d1.removeRow(sIndex);
			}
		}
		return flag;
	}
	
	public void mtable_update(JTable dtable, String what) {
		String Sql;	
		if (what == "") {
			Sql = "select bid, pdate, name , income, outcome  from budget order by pdate" ;
		} else {
		    Sql = "select bid, pdate, name , income, outcome  from budget where " + what + " order by pdate";	
		}	
		ResultSet res = dh.GetData(Sql);
		dtable.setModel(DbUtils.resultSetToTableModel(res));
		dh.CloseConnection();
		String[] fej = { "Bid", "Date", "Movement", "Income", "Outcome" };
		((DefaultTableModel) dtable.getModel()).setColumnIdentifiers(fej);
		hh.setJTableColumnsWidth(dtable, 420, 0,0, 280, 70, 70);
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) dtable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment(SwingConstants.LEFT);	
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();	
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		dtable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		dtable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);	
	}
	
	public int table_maxid(String sql) {
		int myid = 0;
		try {
			con = dh.getConnection();
			rs = dh.GetData(sql);
			if (!rs.next()) {
				System.out.println("Error.");
			} else {
				myid = rs.getInt("max_id");
			}
			dh.CloseConnection();
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			ex.printStackTrace();
		}
		return myid;
	}
	
	public Boolean cannotdelete(String sql) {
		Boolean found = false;
		rs = dh.GetData(sql);
		try {
			if (rs.next()) {
				found = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dh.CloseConnection();
		return found;
	}	
	
	public void dtable_update(JTable dtable, String what) {
		String Sql;
		if (what == "") {
			Sql = "select did, mdate, start, end, note from todolist order by  mdate, start";
		} else {
			      Sql = "select did, mdate,  start, end, note from todolist where " + what + " order by mdate, start";			
		}
		ResultSet res = dh.GetData(Sql);
		dtable.setModel(DbUtils.resultSetToTableModel(res));
		dh.CloseConnection();	
		String[] fej = { "did","Date", "Start", "End", "Task" };
		((DefaultTableModel) dtable.getModel()).setColumnIdentifiers(fej);	
		hh.setJTableColumnsWidth(dtable, 420, 0,0, 50, 50, 320);
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) dtable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment(SwingConstants.LEFT);		
		dtable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				dtable.scrollRectToVisible(dtable.getCellRect(dtable.getRowCount() - 1, 0, true));
			}
		});
		if (dtable.getRowCount() > 0) {
			int row = dtable.getRowCount() - 1;
			dtable.setRowSelectionInterval(row, row);
		}
	}
	
	public boolean datefound(String Query) {
		boolean found = false;
		int count = 0;
		try {			
			con = dh.getConnection();
			rs = dh.GetData(Query);
			while (rs.next()) {	
			count = rs.getInt("count(*)");
	       	}
			dh.CloseConnection();	
		}
		catch (SQLException SQLe) {
			System.out.println("Count(*) error " + SQLe.toString());
		}
		if (count >0) {
	        found= true;
		}
		return found;
	}	
}
