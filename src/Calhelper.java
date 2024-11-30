
import java.util.Date;
import java.awt.Color;
import java.util.Calendar;

public class Calhelper {
	Color vzold = new Color(119, 191, 65);
	Color vred = new Color(246, 85, 31);
	Color ssarga = new Color(245, 212, 9);
	Color hatter = new Color(67, 62, 59);
	Color slilla = new Color(205, 134, 150);
	Color vlilla = new Color(170, 144, 189);
	Color skek1 = new Color(74, 108, 180);
	Color vvkek1 = new Color(13, 182, 213);
	Color vpiros = new Color(217, 58, 39);
	Color spiros = new Color(233, 82, 113);
	Color vrozsaszin = new Color(205, 134, 150);
	Color rrozsaszin = new Color(236, 80, 83);
	Color rozsaszin = new Color(253, 99, 97);
	Color prozsaszin = new Color(255, 82, 136);
	Color plilla = new Color(243, 109, 230);
	Color psargaszold = new Color(215, 238, 98);
	Color pvzold = new Color(213, 255, 118);
	Color vvzold = new Color(85, 240, 86);
	Color psarga = new Color(251, 240, 85);
	 Color vszold = new Color(186, 255, 113);
	
	public String[]          monthNames          = {"January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November", "December"};		
	
	 private String[]  dayNames   = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	 
	 private String[] napnevek = {"Su", "Mo", "The", "We", "Thu", "Fri", "Sa"}; 
	 
	 public String[] daynames() {
			return dayNames;
		}
	 public String[] napnevek() {
			return napnevek;
		}
	 
	 public String [] monthnames() {
		 return monthNames;
	 }
	 
	public int  mynow(String which) { 	
	 Calendar cal = Calendar.getInstance();	
	cal.setTime(new Date());	
	if(which =="year") {
	      int iyear = cal.get(Calendar.YEAR);
	      return iyear;
	}else  if (which=="month") {
	     int imonth = cal.get(Calendar.MONTH);
	    return imonth;
	} else  if (which=="day") {
      	 int iday  = cal.get(Calendar.DAY_OF_MONTH);
	     return iday;
	}else  if (which=="hour") {
    	 int ihour = cal.get(Calendar.HOUR_OF_DAY);  	
	     return ihour;
	} else  if (which=="minute") {
	     int iminute = cal.get(Calendar.MINUTE); 
	     return iminute;
	}	 
	return 0;
	}
	
	public int numofmonth(String monthname) {
		int ho = -1;
		switch (monthname) {
		case "January":
		ho = 0;
		break;
		case "February":
		ho=1;
		break;
		case "March":
		ho=2;
		break;
		case "Aprilis":
	      	ho=3;
		break;
		case "May":
		ho= 4;
		break;
		case "June":
		ho=5;
		break;
		case "July":
		ho=6;
		break;
		case "August":
			ho=7;
			break;
		case "September":
			ho=8;
			break;
		case "October":
			ho=9;
			break;
		case "November":
			ho=10;
			break;
		case "December":
			ho=11;
			break;
		default:			
	     	ho=-1;
		}
		return ho;
	}	
};
