package com.restaurantos_domain;

import com.restaurantos_db.OrderGateway;
import com.restaurantos_db.unit_of_works.OrderItemUnitOfWork;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Payment {
    private static final Logger logger = LogManager.getLogger(Payment.class.getName());

    Order order;
    LinkedList<OrderItem> selected, all;

    public Payment(Order order, LinkedList<OrderItem> selected, LinkedList<OrderItem> all){
        this.order = order;
        this.selected = selected;
        this.all = all;
    }

    public void pay(){
        for(OrderItem orderItem : selected){
            orderItem.setState("Paid");
        }

        boolean allPayed = true;
        for(OrderItem orderItem : all){
            if(!orderItem.getState().equals("Paid"))
                allPayed = false;
        }

        OrderItemUnitOfWork orderItemUnitOfWork = new OrderItemUnitOfWork();
        for (OrderItem orderItem : selected) {
            orderItemUnitOfWork.addToUpdate(orderItem);
        }
        orderItemUnitOfWork.forceCommit();

        if(allPayed) {
            order.setPaid(true);
            OrderGateway orderGateway = new OrderGateway();
            orderGateway.update(order);

            logger.log(Level.INFO, "Order was Paid");
        }else
            logger.log(Level.INFO, "In Order was Paid " + selected.size());
    }

    public void exportPayment(){
        String out =    "Restaurant OS - ORDER " + order.getOrderId() + "\n" +
                        "Date " + order.getCreatedDate() + "\n" +
                        "Employee " + order.getCreatedBy().getFirstName() + " " + order.getCreatedBy().getLastName() + "\n" +
                        "<--------------------------------------->\n\n";

        double totalCost = 0;
        for (OrderItem orderItem : selected){
            double cost = (orderItem.getCount() * orderItem.getMenuItem().getCost());
            totalCost += cost;

            String name = orderItem.getMenuItem().getFood().getName();
            for(int s = name.length(); s < 20; s++ )
                name += " ";
            out += "> " + name + "\t" + String.format("%2d", orderItem.getCount()) + "ks " +
                   "\t" + String.format(" %4.2f", cost) + " kc\n";
        }

        out +=  "\n<--------------------------------------->\n" +
                "Total Cost: " +  totalCost + "\n";

        String dateTime = DateTimeFormatter.ofPattern("dd_MM_yyyy").format(LocalDate.now()) + "-" +
                          DateTimeFormatter.ofPattern("hh_mm_ss").format(LocalTime.now());
        Files.writeString("/Payments/order" + order.getOrderId() + "-" + dateTime + ".txt", out);
    }
}
