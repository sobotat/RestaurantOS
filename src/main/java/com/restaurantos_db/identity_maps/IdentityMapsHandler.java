package com.restaurantos_db.identity_maps;

public class IdentityMapsHandler {

    private static final OrderIdentityMap orderIdentityMap = new OrderIdentityMap();
    private static final OrderItemIdentityMap orderItemIdentityMap = new OrderItemIdentityMap();
    private static final UserIdentityMap userIdentityMap = new UserIdentityMap();

    public static void refresh(){
        orderIdentityMap.clear();
        orderItemIdentityMap.clear();
        userIdentityMap.clear();
    }
}
