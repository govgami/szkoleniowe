package testing;

import org.junit.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nbp.CurrencyPrice;
import nbp.downloader.xml.XMLStringNBPDownloader;
import nbp.downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import nbp.extraction.xml.specialized.SelectiveSaxDataReader;
import nbp.valueReading.xml.CurrMarkSpecSAXNumericReader;
import nbp.valueReading.xml.SAXCurrencyPricesDataReader;
import nbp.valueReading.xml.SAXNumericReader;
import nbp.valueReading.xml.table.TablesAB;

public class _SmokeTest {
	XMLStringNBPDownloader specInfo;
	XMLStringNBPDownloader genInfo;
	XMLStringNBPDownloader timedGenInfo;
	XMLStringNBPDownloader timedPeriodGenInfo;
	SelectiveSaxDataReader resp;
	SelectiveSaxDataReader respExt;
	SelectiveSaxDataReader respExtTable;
	
	Date date1,date2;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@BeforeMethod
	public void setUp() throws Exception {
		specInfo = HttpXmlExchangeDownloaderFactory.exchangeRateA("usd");
		genInfo = HttpXmlExchangeDownloaderFactory.exchangeTable("a");
		Calendar dateConst = Calendar.getInstance();
		dateConst.set(2015, 5, 1);
		date1=dateConst.getTime();
		Calendar dateConst2 = Calendar.getInstance();
		dateConst2.set(2015, 7, 30);//dateConst2.set(2015, 5, 8);
		date2=dateConst2.getTime();
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(date.getTimeInMillis() - 1000 * 3600 * 24 * 7);
		timedGenInfo = HttpXmlExchangeDownloaderFactory.exchangeTableOnDay("a", dateConst.getTime());
		timedPeriodGenInfo= HttpXmlExchangeDownloaderFactory.exchangeTableOnDayTo("a", dateConst.getTime(), dateConst2.getTime());
		resp = new SelectiveSaxDataReader(new SAXNumericReader("Mid"));
		respExt = new SelectiveSaxDataReader(new CurrMarkSpecSAXNumericReader("usd", "Mid"));
		respExtTable = new SelectiveSaxDataReader(new SAXCurrencyPricesDataReader(new TablesAB()));
	}

	@AfterMethod
	public void tearDown() throws Exception {
	}

	@Test
	public void shouldGetActualExchangeForUSD() {
		String r = specInfo.download();
		System.out.print(r + "\n");
		String readed = resp.read(r);
		System.out.println(readed);
		assertNotNull(resp.readExt(r));
		assertEquals(readed, "3.6006");
	}

	@Test
	public void shouldGetTableAWithExchangeForUSD() {
		String r = genInfo.download();
		System.out.print(r + "\n");
		String readed = respExt.read(r);
		System.out.println(readed);
		assertNotNull(respExt.readExt(r));
		assertEquals(readed, "3.6006");
	}

	@Test
	public void shouldGetTableAFromSpecifiedTimeForUSDWeekAgo() {
		String r = timedGenInfo.download();
		System.out.print(r + "\n");
		String readed = respExt.read(r);
		System.out.println(readed);
		assertNotNull(respExt.readExt(r));
	}
	@Test
	public void shouldGetTableFromSpecifiedPeriodWeekAgo() {
		String r = timedPeriodGenInfo.download();
		System.out.print(r + "\n");
		List<CurrencyPrice> list = (List<CurrencyPrice>) respExtTable.readExt(r);
		for(CurrencyPrice cp:list) {
		System.out.println("loaded: "+cp.getCurrencyName()+" "+cp.getEffectiveDate()+", "+cp.getCurrencySign()+", "+cp.getAvgCurrencyPrice());
		}
	}
	
//	@Test
//	public void testBlockGeneralPeriodTimedExchangeWeekAgo() {
//		List<CurrencyPrice> list = HttpXmlNbpPeriodTableCurrency.takeTable("a", date1, date2);
//		//for(CurrencyPrice cp:list) {
//		//System.out.println("loaded block: "+cp.getEffectiveDate()+", "+cp.getCurrencySign()+", "+cp.getAvgCurrencyPrice());
//		//}
//	}
}
