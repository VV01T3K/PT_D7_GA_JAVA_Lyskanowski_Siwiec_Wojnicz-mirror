package pl.edu.pg.lab6.controller;

public enum ControllerResponses {
    DONE("done"),
    BAD_REQUEST("bad request"),
    NOT_FOUND("not found");

    private final String response;

    ControllerResponses(String response) {
        this.response = response;
    }
    @Override
    public String toString() {
        return response;
    }
}
