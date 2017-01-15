package com.application.norgevz.myticketon.models;

import java.util.Date;

/**
 * Created by norgevz on 12/13/2016.
 */

public class Ticket {

    public String theaterName;

    public String showName;

    public Date StartTime;

    public Order order;

    public Ticket(String theaterName, String showName, Date startTime, Order _order) {
        this.showName = showName;
        StartTime = startTime;
        this.theaterName = theaterName;
        order = _order;
    }
}
