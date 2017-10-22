package testing;


import static org.testng.Assert.*;

import org.testng.annotations.*;

public class MainTesting {
  @Test
  public void f() throws Exception {
	  _SmokeTest t=new _SmokeTest();
	  t.setUp();
	  t.testActualExchangeForUSD();
	  t.testGeneralExchangeForUSD();
	  t.testGeneralTimedExchangeForUSDWeekAgo();
  }
}
