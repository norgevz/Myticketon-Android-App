package com.application.norgevz.myticketon.models;

/**
 * Created by norgevz on 12/11/2016.
 */

public class OrderFee {
    public String Id;
    public String FeeCategoryId;
    public String OrderId;
    public double FeeValue;

    public FeeCategory FeeCategory;
    public Order Order;
}
