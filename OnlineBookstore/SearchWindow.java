import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import model.Order;
import datastructure.OrderQueue;
import datastructure.StackHistory;
import algorithms.Searching;

public class SearchWindow extends JFrame {
    private JTextField searchField;
    private JTextArea resultArea;
    private OrderQueue orderQueue;
    private StackHistory history;

    public SearchWindow(MainGUI mainGUI, OrderQueue orderQueue, StackHistory history) {
        this.orderQueue = orderQueue;
        this.history = history;
        
        setTitle("Search Orders");
        setLocationRelativeTo(mainGUI);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter Order Number:"));
        searchField = new JTextField(15);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
        searchPanel.add(searchField);
        
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn);
        
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel countPanel = new JPanel(new FlowLayout());
        countPanel.setBorder(BorderFactory.createTitledBorder("Order Statistics"));
        
        int pendingCount = orderQueue.getAllOrders().size();
        int historyCount = history.getAllOrders().size();
        int totalCount = pendingCount + historyCount;
        
        countPanel.add(new JLabel("Pending Orders: " + pendingCount));
        countPanel.add(new JLabel(" | "));
        countPanel.add(new JLabel("Processed History: " + historyCount));
        countPanel.add(new JLabel(" | "));
        countPanel.add(new JLabel("Total Orders: " + totalCount));
        
        add(countPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        resultArea = new JTextArea(13, 0);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        bottomPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        
        JLabel instructionLabel = new JLabel("Enter an order number and press Search or Enter key");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(instructionLabel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        setSize(600, 500);
        
        setVisible(true);
        
        searchField.requestFocus();
    }
    
    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            resultArea.setText("Please enter an order number to search.");
            return;
        }
        
        try {
            int searchOrderNumber = Integer.parseInt(searchText);
            
            Order foundOrder = Searching.linearSearch(orderQueue.getAllOrders(), searchOrderNumber);
            
            if (foundOrder != null) {
                displaySearchResult(foundOrder, "PENDING ORDERS");
            } else {
                List<Order> allHistory = history.getAllOrders();
                foundOrder = Searching.linearSearch(allHistory, searchOrderNumber);
                
                if (foundOrder != null) {
                    displaySearchResult(foundOrder, "PROCESSED HISTORY");
                } else {
                    resultArea.setText("Order #" + searchOrderNumber + " not found in the system.\n\n" +
                                    "Searched in:\n" +
                                    "- Pending Orders\n" +
                                    "- Processed History");
                }
            }
        } catch (NumberFormatException ex) {
            resultArea.setText("Please enter a valid order number (numbers only).");
        }
    }
    
    private void displaySearchResult(Order order, String location) {
        StringBuilder sb = new StringBuilder();
        sb.append("ORDER FOUND IN: ").append(location).append("\n");
        sb.append("=====================================\n\n");
        sb.append("Order Number: ").append(order.getOrderNumber()).append("\n");
        sb.append("Customer: ").append(order.getCustomer()).append("\n");
        sb.append("Delivery Address: ").append(order.getDeliveryAddress()).append("\n");
        sb.append("Book Quantity: ").append(order.getBookQuantity()).append("\n");
        sb.append("Books:\n");
        
        for (int i = 0; i < order.getBooks().size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(order.getBooks().get(i)).append("\n");
        }
        
        sb.append("\n=====================================\n");
        sb.append("Status: ").append(location.equals("PENDING ORDERS") ? "Waiting to be processed" : "Already processed");
        
        resultArea.setText(sb.toString());
    }
}
