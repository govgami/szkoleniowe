package nbp.downloader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dates {
	public static List<Date> markValidDates(Date from, Date to){
		List<Date> list=new ArrayList<Date>();
		Date t=new Date(from.getTime());
		while(t.before(to)) {
			if(isDateValid(t)) {
				list.add(new Date(t.getTime()));
			}
			t.setTime(t.getTime()+1000*3600*24);
		}
		return list;
	}
	static boolean isDateValid(Date date) {
		//not Sunday or Saturday
		if(date.getDay()!=0&date.getDay()!=6) {
			return true;
		}
		return false;
	}
	
	public static Date getlastValidDate(Date date) {
		Date t=new Date(date.getTime());
		while(!isDateValid(t)) {
			t.setTime(t.getTime()-1000*3600*24);
		}
		return t;
	}
}
