package datastructure;
import java.io.Serializable;
import java.util.Stack;
import java.util.List;
import java.util.LinkedList;
import model.Order;

public class StackHistory implements Serializable {
    private Stack<Order> stack = new Stack<>();

    public void push(Order order) { stack.push(order); }
    public Order pop() { return stack.isEmpty() ? null : stack.pop(); }
    public Order peek() { return stack.isEmpty() ? null : stack.peek(); }
    
    public List<Order> getAllOrders() {
        return new LinkedList<>(stack);
    }
}
