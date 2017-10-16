package logging;

import java.util.logging.Logger;

import org.apache.log4j.BasicConfigurator;

public class Log {
static Logger log;
static {
	log=Logger.getLogger("");
	BasicConfigurator.configure();
}

public static void info(String logInformation) {
	log.info(logInformation);
}
public static void warn(String logInformation) {
	log.warning(logInformation);
}
public static void exception(String logInformation, Throwable thrown) {
	log.log(java.util.logging.Level.SEVERE, logInformation, thrown);
}
}
