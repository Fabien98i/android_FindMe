package com.ynov.findme.models;

import java.util.List;

public class ApiObject {
    private int nhits;
    private List<Records> records;
    private  Parameters parameters;


    public ApiObject (int nhits, List <Records> records, Parameters parameters) {
        this.nhits = nhits;
        this.records = records;
        this.parameters = parameters;
    }

    public int getNhits() {
        return nhits;
    }

    public List<Records> getRecords() {
        return records;
    }


    public Parameters getParameters() {
        return parameters;
    }
}
