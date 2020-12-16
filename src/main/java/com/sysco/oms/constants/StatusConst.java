package com.sysco.oms.constants;

public class StatusConst {

    public enum OrderStatus {
        placed,approved,processing,fail,cancel;
    }

    public enum OrderRequestStatus {
        placed,cancel
    }
}
