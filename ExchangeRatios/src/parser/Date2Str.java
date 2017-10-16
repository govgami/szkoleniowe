package parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import logging.Log;

public class Date2Str implements InnerDataParser {

	Date date;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

	public Date2Str(long time) {
		date = new Date(time);
	}

	public Date2Str(Date time) {
		date = time;
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
