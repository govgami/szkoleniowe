package parser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import logging.Log;

public class Str2BigDecimal implements InnerDataParser {

	String state;

	public Str2BigDecimal(String numericString) {
		state = numericString;
	}

	@Override
	public BigDecimal parse() throws RuntimeException {
		try {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setGroupingSeparator(',');
			symbols.setDecimalSeparator('.');
			String pattern = "#,##0.0#";
			DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
			decimalFormat.setParseBigDecimal(true);

			// parse the string
			return (BigDecimal) decimalFormat.parse(state);
		} catch (Exception e) {
			Log.exception("failure:\n", e);
			throw new RuntimeException(e);
		}
	}

}
