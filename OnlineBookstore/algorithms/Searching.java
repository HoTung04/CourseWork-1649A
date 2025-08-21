package algorithms;
import java.util.Collection;
import model.Order;

public class Searching {
    public static Order linearSearch(Collection<Order> orders, int orderNumber) {
        for (Order o : orders) {
            if (o.getOrderNumber() == orderNumber) {
                return o;
            }
        }
        return null;
    }
}
