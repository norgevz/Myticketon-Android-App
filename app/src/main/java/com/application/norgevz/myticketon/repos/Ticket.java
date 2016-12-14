package com.application.norgevz.myticketon.repos;

import java.util.Date;

/**
 * Created by norgevz on 12/13/2016.
 */

public class Ticket {

    public String theaterName;

    public String showName;

    public Date StartTime;

    public boolean reedemState;

    public Ticket(String theaterName, String showName, Date startTime, boolean reedemState) {
        this.reedemState = reedemState;
        this.showName = showName;
        StartTime = startTime;
        this.theaterName = theaterName;
    }
}
