package model;
import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private int orderNumber;
    private String customer;
    private String deliveryAddress;
    private List<Book> books;
    private int bookQuantity;

    public Order(int orderNumber, String customer, String deliveryAddress, List<Book> books, int bookQuantity) {
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.books = books;
        this.bookQuantity = bookQuantity;
    }

    public int getOrderNumber() { return orderNumber; }
    public String getCustomer() { return customer; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public List<Book> getBooks() { return books; }
    public int getBookQuantity() { return bookQuantity; }

    @Override
    public String toString() {
        return "Order#" + orderNumber + " - " + customer + " - Address: " + deliveryAddress + 
               " - Books: " + books + " - Quantity: " + bookQuantity;
    }
}
