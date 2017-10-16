package exchangeInformer;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import downloader.xml.XMLStringNBPDownloader;
import downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import extraction.xml.specialized.SelectiveSaxDataReader;
import valueReading.xml.CurrMarkSpecSAXNumericReader;
import valueReading.xml.SAXNumericReader;

public class _SmokeTest {
	XMLStringNBPDownloader specInfo;
	XMLStringNBPDownloader genInfo;
	XMLStringNBPDownloader timedGenInfo;
	SelectiveSaxDataReader resp;
SelectiveSaxDataReader respExt;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		specInfo = HttpXmlExchangeDownloaderFactory.exchangeRateA("usd");
		genInfo=HttpXmlExchangeDownloaderFactory.exchangeTable("a");
		Calendar date=Calendar.getInstance();
		date.setTimeInMillis(date.getTimeInMillis()-1000*3600*24*7);
		timedGenInfo=HttpXmlExchangeDownloaderFactory.exchangeTableOnDay("a", date.getTime());
		resp = new SelectiveSaxDataReader(new SAXNumericReader("Mid"));
		respExt= new SelectiveSaxDataReader(new CurrMarkSpecSAXNumericReader("usd", "Mid"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testActualExchangeForUSD() {
		String r = specInfo.download();
		System.out.print(r + "\n");
		String readed = resp.read(r);
		System.out.println(readed);
		assertNotEquals(-1, Float.parseFloat(readed), 1);
	}
	
	@Test
	public void testGeneralExchangeForUSD() {
		String r = genInfo.download();
		System.out.print(r + "\n");
		String readed = respExt.read(r);
		System.out.println(readed);
		assertNotEquals(-1, Float.parseFloat(readed), 1);
	}
	@Test
	public void testGeneralTimedExchangeForUSDWeekAgo() {
		String r = timedGenInfo.download();
		System.out.print(r + "\n");
		String readed = respExt.read(r);
		System.out.println(readed);
		assertNotEquals(-1, Float.parseFloat(readed), 1);
	}
}
