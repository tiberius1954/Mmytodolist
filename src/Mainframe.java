import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class Mainframe extends JFrame{	
   
	Hhelper hh = new Hhelper();
	Calhelper cc = new Calhelper();
	Mainframe(){
		cp = getContentPane();
		cp.setBackground(cc.vszold);
		initcomponent();		
	}
	private void initcomponent() {
		
		setSize(1090, 620);
		setLayout(null);
		setLocationRelativeTo(null);
		hh.iconhere(this);
		setTitle("Our tasks	");
		fejlec = new JLabel("OUR TASKS");
		fejlec.setBounds(460,70,250,35);
		fejlec.setFont(new java.awt.Font("Times New Roman", 1, 28));
		add(fejlec);	
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				vegso();
		}
		});		
		
		lbajto1 = new JLabel();
		lbajto1.setBounds(150, 160, 145, 295);
    	lbajto1.setBorder(hh.borderf);
		lbajto1.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/ajto6.png")));
		lbajto1.setName("1");
		add(lbajto1);
		lbajto1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onMouseClicked(e);
			}
		});
		
		lbajto2 = new JLabel();
		lbajto2.setBounds(310, 160, 145, 295);
	 	lbajto2.setBorder(hh.borderf);
		lbajto2.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/ajto4.png")));
		lbajto2.setName("2");
		add(lbajto2);
		lbajto2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onMouseClicked(e);
			}
		});
		
		lbajto3 = new JLabel();
		lbajto3.setBounds(470, 160, 145, 295);
	    lbajto3.setBorder(hh.borderf);
		lbajto3.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/ajto2.png")));
		lbajto3.setName("3");
		add(lbajto3);
		lbajto3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onMouseClicked(e);
			}
		});
		
		lbajto4 = new JLabel();
		lbajto4.setBounds(630, 160, 145, 295);
     	lbajto4.setBorder(hh.borderf);
		lbajto4.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/ajto7.png")));
		lbajto4.setName("4");
		add(lbajto4);
		lbajto4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onMouseClicked(e);
			}
		});
		
		lbajto5 = new JLabel();
		lbajto5.setBounds(790, 160, 145, 295);
	   lbajto5.setBorder(hh.borderf);
		lbajto5.setIcon(new ImageIcon(ClassLoader.getSystemResource("Images/ajto8.png")));		
		lbajto5.setName("5");
		add(lbajto5);
		lbajto5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onMouseClicked(e);
			}
		});		
		
	}
	private void onMouseClicked(MouseEvent e) {
		JLabel target = (JLabel) e.getSource();
		String name = target.getName();
		switch (name) {
		case "1":
            Mycalendar c1 =  new Mycalendar();     
            c1.setVisible(true);     
			break;			
		case "2":
			Teljesevzt c2 = new Teljesevzt();
			 c2.setVisible(true);
			break;
		case "3":
			Tasklist c3 = new Tasklist();
			c3.setVisible(true);
			break;
		case "4":
			Moneylist c4 = new Moneylist();
			c4.setVisible(true);
			break;
		case "5":	
			vegso();
			break;	     	
		}	
		dispose();
	}	
	
	void vegso() {
	int x, y, d;
	x = 1000;
	y = 600;
	d = 10;
	while (x > 0 && y > 0) {
		setSize(x, y);
		x = x - 2 * d;
		y = y - d;
		setVisible(true);
		try {
			Thread.sleep(10);
		} catch (Exception e) {
			System.out.println("This is error:" + e);
		}
	}
	dispose();
}
	
	public static void main(String args[]) {
		Mainframe ts = new Mainframe();	
		ts.setVisible(true);
	}
	
	Container cp;
	JLabel lbajto1, lbajto2, lbajto3, lbajto4, lbajto5;
	JLabel fejlec;
}
