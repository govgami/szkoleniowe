package exchangeInformer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import downloader.xml.nbp.ActualExchangeDownloader;
import exchangeInformer.responseInterpreter.SAXDataReader;

public class _SmokeTest {
	ActualExchangeDownloader info;
	SAXDataReader resp;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		info = new ActualExchangeDownloader("usd");
		resp = new SAXDataReader();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String r = info.download();
		System.out.print(r + "\n");
		String readed = resp.read(r);
		System.out.println(readed);
		assertNotEquals(-1, Float.parseFloat(readed), 1);
	}

}
