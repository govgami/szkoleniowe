package parser;

import java.text.SimpleDateFormat;

import logging.Log;

public class Str2SqlDate implements InnerDataParser{
String input;
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");


public Str2SqlDate(String textDate) {
	input=textDate;
}

@Override
public java.sql.Date parse() {
	try {
	java.util.Date date = sdf1.parse(input);
	java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	return sqlDate;
}catch(Exception e) {
Log.exception("attempted to parse: "+input+" with format "+sdf1.toPattern(), e);	
throw new RuntimeException(e);
}
}

}
