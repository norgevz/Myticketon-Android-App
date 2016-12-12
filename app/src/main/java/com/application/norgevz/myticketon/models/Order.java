package com.application.norgevz.myticketon.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by norgevz on 12/11/2016.
 */

public class Order {

    public String Id;
    public String Theater ;
    public String Auditorium;
    public Date StartTime;
    public String Tickets;
    public double Total;
    public String Show;
    public boolean Status;
    public boolean Printed;
    public String ShowTimeInstanceId;
    public double Fee;
    public int Quantity;

    public OrderHeader OrderHeader;
    public ArrayList<OrderSeat> OrderSeats;
    public ArrayList<OrderFee> OrderFees;

}
