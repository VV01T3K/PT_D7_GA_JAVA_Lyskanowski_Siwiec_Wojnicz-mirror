package pl.edu.pg.queries;

public class ConsumerQuery {
    public enum QueryType {
        DECRYPT,
        ENCRYPT
    };
    public final QueryType queryType;
    public final String[] arguments;

    public ConsumerQuery(QueryType queryType, String[] arguments) {
        this.queryType = queryType;
        this.arguments = arguments;
    }
}
