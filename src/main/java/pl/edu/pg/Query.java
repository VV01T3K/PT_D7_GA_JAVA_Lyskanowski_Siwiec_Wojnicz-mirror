package pl.edu.pg;

public class Query {
    public enum QueryType {
        ENCRYPT, DECRYPT
    }

    public final String pathFrom;
    public final String pathTo;
    public final QueryType queryType;
    public final String key;

    public Query(QueryType queryType, String pathFrom, String pathTo) {
        this.pathFrom = pathFrom;
        this.pathTo = pathTo;
        this.queryType = queryType;
        this.key = "";
    }

    public Query(String pathFrom, String pathTo, QueryType queryType, String key) {
        this.pathFrom = pathFrom;
        this.pathTo = pathTo;
        this.queryType = queryType;
        this.key = key;
    }

    @Override
    public String toString() {
        return queryType + " " + pathFrom + " " + pathTo + " " + key;
    }

    public static Query fromString(String query) {
        String[] parts = query.split(" ");
        if (parts.length == 3) {
            return new Query(QueryType.valueOf(parts[0]), parts[1], parts[2]);
        } else {
            return new Query(parts[0], parts[1], QueryType.valueOf(parts[2]), parts[3]);
        }
    }
}