package pl.edu.pg;

import java.io.Serializable;

public class Query implements Serializable {
    private QueryType queryType;
    private Object data;
    public Query(QueryType queryType, Object data) {
        this.queryType = queryType;
        this.data = data;
    }
    public QueryType getQueryType() {
        return queryType;
    }
    public Object getData() {
        return data;
    }
    @Override
    public String toString() {
        return "Query{" +
                "queryType=" + queryType +
                ", data=" + data +
                '}';
    }
}
