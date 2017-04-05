package com.quartet.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "Metrics")
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column(nullable = false)
    private String name;

    //assume value is a long
    @Column(nullable = false)
    private long value;

    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime createdDateTime;

    //needed by hibernate
    protected Metric() {

    }

    public Metric(String name, long value) {
        this.name = name;
        this.value = value;
        this.createdDateTime = DateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    @Override
    public String toString() {
        return "Metric{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
