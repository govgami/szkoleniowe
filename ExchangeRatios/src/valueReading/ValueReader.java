package valueReading;

public interface ValueReader {
	public String getFoundValueString();

	public <T> T getFoundValue();
}
