package com.ynov.findme.models;

public class Records {
    private String recordid;
    private Fields fields;

    public Records(String recordid, Fields fields) {
        this.recordid = recordid;
        this.fields = fields;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }
}
