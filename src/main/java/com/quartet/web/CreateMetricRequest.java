package com.quartet.web;

import java.io.Serializable;

public class CreateMetricRequest implements Serializable {

    private static final long serialVersionUID = 3096257963076226830L;

    private String name;
    private long value;

    //TODO clean up default constructor; in for json serialization
    public CreateMetricRequest() {

    }

    public CreateMetricRequest(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }
}
