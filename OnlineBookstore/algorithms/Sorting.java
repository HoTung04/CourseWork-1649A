package algorithms;
import java.util.List;
import model.Order;

public class Sorting {
    public static void insertionSort(List<Order> orders) {
        for (int i = 1; i < orders.size(); i++) {
            Order current = orders.get(i);
            String currentTitle = getPrimaryBookTitle(current);
            int j = i - 1;
            while (j >= 0 && getPrimaryBookTitle(orders.get(j)).compareTo(currentTitle) > 0) {
                orders.set(j + 1, orders.get(j));
                j--;
            }
            orders.set(j + 1, current);
        }
    }

    private static String getPrimaryBookTitle(Order order) {
        if (order == null || order.getBooks() == null || order.getBooks().isEmpty()) {
            return "";
        }
        String title = order.getBooks().get(0).getTitle();
        return title == null ? "" : title;
    }
}
