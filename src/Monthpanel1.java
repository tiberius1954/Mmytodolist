import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.util.Arrays;

public class Monthpanel1 extends JPanel {
	protected int month;
	protected int year;
	JTable dtable, mtable;
	Hhelper hh = new Hhelper();
	Calhelper cc = new Calhelper();
	Databaseop dd = new Databaseop();
	protected String[] monthNames = cc.monthnames();
	protected String[] dayNames = cc.daynames();
	JLabel[] dayLabel = new JLabel[42];
	int tDay = 0;
	private String sfrom = "";
	private JFrame pframe;
	
	public Monthpanel1(int year, int month, JTable dtable, JTable mtable, JFrame parent) {
		this.month = month;
		this.year = year;
		this.dtable = dtable;
		this.mtable = mtable;
		sfrom = "MyCalendar";
		pframe = parent;		
		this.add(createGUI());
	}

	protected JPanel createGUI() {
		JPanel monthPanel = new JPanel(true);	
		monthPanel.setBorder(BorderFactory.createLineBorder(SystemColor.activeCaption));
		monthPanel.setLayout(new BorderLayout());
		monthPanel.setBackground(Color.WHITE);
		monthPanel.setForeground(Color.BLACK);
		monthPanel.add(createTitleGUI(), BorderLayout.NORTH);
		monthPanel.add(createDaysGUI(), BorderLayout.SOUTH);
		return monthPanel;
	}

	protected JPanel createTitleGUI() {
		JPanel titlePanel = new JPanel(true);	
		titlePanel.setBorder(BorderFactory.createLineBorder(SystemColor.BLACK));
		titlePanel.setLayout(new FlowLayout());
		titlePanel.setBackground(Color.WHITE);
		JLabel label = new JLabel(year + " " + monthNames[month]);	
		label.setForeground(SystemColor.BLACK);
		titlePanel.add(label, BorderLayout.CENTER);
		return titlePanel;
	}

	protected JPanel createDaysGUI() {
		JPanel dayPanel = new JPanel(true);
		dayPanel.setLayout(new GridLayout(0, dayNames.length));

		Calendar today = Calendar.getInstance();
		int tMonth = today.get(Calendar.MONTH);
		int tYear = today.get(Calendar.YEAR);
		tDay = today.get(Calendar.DAY_OF_MONTH);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		Calendar iterator = (Calendar) calendar.clone();
		iterator.add(Calendar.DAY_OF_MONTH, -(iterator.get(Calendar.DAY_OF_WEEK) - 1));

		Calendar maximum = (Calendar) calendar.clone();
		maximum.add(Calendar.MONTH, +1);

		for (int i = 0; i < dayNames.length; i++) {
			JPanel dPanel = new JPanel(true);
			dPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			JLabel dLabel = new JLabel(dayNames[i], SwingConstants.CENTER);
			dPanel.add(dLabel);
			dayPanel.add(dPanel);
		}

		int cn = 0;
		int limit = dayNames.length * 6;

		for (int i = 0; i < 42; i++) {
			dayLabel[i] = new JLabel("", SwingConstants.CENTER);
			dayLabel[i].setPreferredSize(new Dimension(22, 22));
		}
		all_label_border();

		while (iterator.getTimeInMillis() < maximum.getTimeInMillis()) {
			int lMonth = iterator.get(Calendar.MONTH);
			int lYear = iterator.get(Calendar.YEAR);

			JPanel dPanel = new JPanel(true);
			dPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));		
			dayLabel[cn].setFont(new Font("Tahoma", Font.BOLD, 14));
			dayLabel[cn].setBorder(new EmptyBorder(2, 2, 2, 2));	
			if ((lMonth == month) && (lYear == year)) {
				int lDay = iterator.get(Calendar.DAY_OF_MONTH);
				dayLabel[cn].setText(Integer.toString(lDay));
				dayLabel[cn].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						onMouseClicked(e);
					}
				});
				
				 if  (lDay == hh.aktday) {
						dayLabel[cn].setBorder(new MatteBorder(2, 2, 2, 2, Color.RED));
						((Mycalendar) pframe).datumkiir();
				 }
				if ((tMonth == month) && (tYear == year) && (tDay == lDay)) {
					dPanel.setBackground(Color.ORANGE);
				
				} else {
					dPanel.setBackground(Color.WHITE);			
			} 				
			} else {
				dayLabel[cn].setText(" ");
				dPanel.setBackground(Color.WHITE);
			}
			dPanel.add(dayLabel[cn]);
			dayPanel.add(dPanel);
			iterator.add(Calendar.DAY_OF_YEAR, +1);
			cn++;
		}

		for (int i = cn; i < limit; i++) {
			JPanel dPanel = new JPanel(true);
			dPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			dayLabel[cn].setFont(new Font("Tahoma", Font.BOLD, 14));
			dayLabel[cn].setBorder(new EmptyBorder(2, 2, 2, 2));
			dayLabel[cn].setText(" ");
			dPanel.setBackground(Color.WHITE);
			dPanel.add(dayLabel[cn]);
			dayPanel.add(dPanel);
		}
		taskfound();
		return dayPanel;
	}

	private void onMouseClicked(MouseEvent e) {
		JLabel target = (JLabel) e.getSource();
		String ezaz = target.getText();
		String smonth = hh.itos(month + 1);
		String ssmonth = hh.padLeftZeros(smonth, 2);
		String mydate = hh.itos(year) + "-" + ssmonth + "-" + hh.padLeftZeros(ezaz, 2);
		String ztmydate = hh.itos(year) + "." + ssmonth + "." + hh.padLeftZeros(ezaz, 2);
		all_label_border();
		target.setBorder(new MatteBorder(2, 2, 2, 2, Color.RED));	
		target.setPreferredSize(new Dimension(22, 22));
		target.setHorizontalAlignment(SwingConstants.CENTER);
		dd.dtable_update(dtable, " mdate ='" + mydate + "'");
		dd.mtable_update(mtable, " pdate ='" + mydate + "'");
		hh.aktdate = mydate;
		hh.aktyear = year;
		hh.aktmonth = month;
		hh.aktday = hh.stoi(ezaz);
		((Mycalendar) pframe).datumkiir();
	}

	private void all_label_border() {
		for (int i = 0; i < 42; i++) {
			dayLabel[i].setBorder(new EmptyBorder(2, 2, 2, 2));
		}
	}

	private void taskfound() {
		Boolean found = false;
		Boolean pfound = false;
		String smonth = hh.itos(month + 1);
		String ssmonth = hh.padLeftZeros(smonth, 2);
		String mydate = "";
		String day = "";
		String Query = "";
		for (int i = 0; i < 42; i++) {
			day = dayLabel[i].getText();
			if (!hh.zempty(day)) {
				mydate = hh.itos(year) + "-" + ssmonth + "-" + hh.padLeftZeros(day, 2);
				Query = "select count(*) from todolist where mdate='" + mydate + "'";
				found = dd.datefound(Query);
				if (found == false) {
					Query = "select count(*) from budget where pdate='" + mydate + "'";
					pfound = dd.datefound(Query);
				}
				Component source = dayLabel[i].getParent();
				if ((found == true || pfound == true ) && hh.stoi(day) != tDay) {
					source.setBackground(hh.vvzold);
				} else {
					dayLabel[i].setBackground(Color.white);
					if (hh.stoi(day) != tDay) {
						source.setBackground(Color.white);				
					}
				}
			}
		}
	}
}
