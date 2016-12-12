package com.application.norgevz.myticketon.models;

import java.util.ArrayList;

/**
 * Created by norgevz on 12/11/2016.
 */

public class OrderHeader {

    public String Id ;
    public double Total;
    public String UserId;
    public boolean Status;
    public String PaymentType;
    public double Fee;

    public ArrayList<Order> Orders;
}
