
package nbp.main;

import nbp.downloader.StringDownloader;
import nbp.extraction.xml.specialized.SelectiveSaxDataReader;

public abstract class AccessXMLInformation {

	protected StringDownloader info;
	protected SelectiveSaxDataReader resp;

	public abstract <T> T getXMLData();

	public abstract String getXMLDataString();
}
