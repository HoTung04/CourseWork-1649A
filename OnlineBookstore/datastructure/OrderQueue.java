package datastructure;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import model.Order;

public class OrderQueue implements Serializable {
    private Queue<Order> queue = new LinkedList<>();

    public void enqueue(Order order) { queue.add(order); }
    public Order dequeue() { return queue.poll(); }
    public boolean isEmpty() { return queue.isEmpty(); }
    public List<Order> getAllOrders() { return new LinkedList<>(queue); }
}
