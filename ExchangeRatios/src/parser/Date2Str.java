package parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import logging.Log;

public class Date2Str implements InnerDataParser {

	Date date;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public Date2Str(long time) {
		date = new Date(time);
	}

	public Date2Str(Date time) {
		if(time!=null)
		date = time;
		else
			throw new RuntimeException("assigned null");
	}

	protected String dateFormat() throws RuntimeException {
		try {
			return dateFormat.format(date);
		} catch (RuntimeException e) {
			Log.exception("DateFormat Exception:", e);
			throw new RuntimeException("DateFormat exception");
		}
	}

	@Override
	public String parse() {
		return dateFormat();
	}

}
