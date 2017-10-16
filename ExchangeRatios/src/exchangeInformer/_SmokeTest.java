package exchangeInformer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dataReading.xml.SAXNumericReader;
import downloader.xml.nbp.GeneralActualExchangeDownloader;
import downloader.xml.nbp.SpecifiedActualExchangeDownloader;
import extraction.xml.specialized.SAXDataReader;
import parser.xml.XMLInputSourceParser;

public class _SmokeTest {
	SpecifiedActualExchangeDownloader specInfo;
	GeneralActualExchangeDownloader genInfo;
	SAXDataReader resp;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		specInfo = new SpecifiedActualExchangeDownloader("usd");
		genInfo=new GeneralActualExchangeDownloader();
		resp = new SAXDataReader(new SAXNumericReader("Mid"));
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
	}

}
