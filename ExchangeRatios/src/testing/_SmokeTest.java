package testing;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import persistence.db.table.currency.CurrencyPrice;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import nbp.downloader.xml.XMLStringNBPDownloader;
import nbp.downloader.xml.factory.HttpXmlExchangeDownloaderFactory;
import nbp.extraction.xml.specialized.SelectiveSaxDataReader;
import valueReading.xml.CurrMarkSpecSAXNumericReader;
import valueReading.xml.SAXCurrencyPricesDataReader;
import valueReading.xml.SAXNumericReader;
import valueReading.xml.tableService.TablesAB;

public class _SmokeTest {
	XMLStringNBPDownloader specInfo;
	XMLStringNBPDownloader genInfo;
	XMLStringNBPDownloader timedGenInfo;
	XMLStringNBPDownloader timedPeriodGenInfo;
	SelectiveSaxDataReader resp;
	SelectiveSaxDataReader respExt;
	SelectiveSaxDataReader respExtTable;

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
		dateConst.set(2015, 5, 5);
		Calendar dateConst2 = Calendar.getInstance();
		dateConst2.set(2015, 5, 8);
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
	public void testActualExchangeForUSD() {
		String r = specInfo.download();
		System.out.print(r + "\n");
		String readed = resp.read(r);
		System.out.println(readed);
		assertNotNull(resp.readExt(r));
	}

	@Test
	public void testGeneralExchangeForUSD() {
		String r = genInfo.download();
		System.out.print(r + "\n");
		String readed = respExt.read(r);
		System.out.println(readed);
		assertNotNull(respExt.readExt(r));
	}

	@Test
	public void testGeneralTimedExchangeForUSDWeekAgo() {
		String r = timedGenInfo.download();
		System.out.print(r + "\n");
		String readed = respExt.read(r);
		System.out.println(readed);
		assertNotNull(respExt.readExt(r));
	}
	@Test
	public void testGeneralPeriodTimedExchangeWeekAgo() {
		String r = timedPeriodGenInfo.download();
		System.out.print(r + "\n");
		List<CurrencyPrice> list = (List<CurrencyPrice>) respExtTable.readExt(r);
		for(CurrencyPrice cp:list) {
		System.out.println("loaded: "+cp.getEffectiveDate()+", "+cp.getCurrencySign()+", "+cp.getAvgCurrencyPrice());
		}
	}
}
