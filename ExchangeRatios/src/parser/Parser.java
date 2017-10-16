package parser;

public interface Parser {
	public <T, T2> T2 parse(T parsed) throws RuntimeException;
}
