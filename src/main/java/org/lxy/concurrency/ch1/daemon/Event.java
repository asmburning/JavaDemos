package org.lxy.concurrency.ch1.daemon;

import lombok.Data;

import java.util.Date;

@Data
public class Event {

    private Date date;
    private String name;
}
