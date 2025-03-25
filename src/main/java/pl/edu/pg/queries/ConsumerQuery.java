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

    @Override
    public String toString() {
        return queryType + " " + arguments[0] + " " + arguments[1] + " " + arguments[2];
    }

    public static ConsumerQuery fromString(String query) {
        String[] parts = query.split(" ");
        String[] arguments = new String[parts.length - 1];
        System.arraycopy(parts, 1, arguments, 0, parts.length - 1);
        return new ConsumerQuery(QueryType.valueOf(parts[0]), arguments);
    }
}
