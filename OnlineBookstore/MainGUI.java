import model.*;
import datastructure.*;
import algorithms.*;
import utils.FileUtil;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.LinkedList;

public class MainGUI extends JFrame {
    private OrderQueue orderQueue;
    private StackHistory history;
    private int nextOrderNumber = 1;

    private JTextArea output;
    private JTextField orderNumberField, customerField, titleField, authorField;
    private JTextField addressField, quantityField;

    private static final String DATA_FILE = "orders.dat";
    private static final String HISTORY_FILE = "history.dat";

    public MainGUI() {
        orderQueue = (OrderQueue) FileUtil.load(DATA_FILE);
        if (orderQueue == null) orderQueue = new OrderQueue();
        
        history = (StackHistory) FileUtil.load(HISTORY_FILE);
        if (history == null) history = new StackHistory();

        for (Order o : orderQueue.getAllOrders()) {
            if (o.getOrderNumber() >= nextOrderNumber) {
                nextOrderNumber = o.getOrderNumber() + 1;
            }
        }
        
        for (Order o : history.getAllOrders()) {
            if (o.getOrderNumber() >= nextOrderNumber) {
                nextOrderNumber = o.getOrderNumber() + 1;
            }
        }

        setTitle("Online Bookstore System");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        output = new JTextArea();
        output.setEditable(false);
        add(new JScrollPane(output), BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("Order Number:"));
        orderNumberField = new JTextField(String.valueOf(nextOrderNumber));
        orderNumberField.setEditable(false);
        panel.add(orderNumberField);

        panel.add(new JLabel("Customer Name:"));
        customerField = new JTextField();
        panel.add(customerField);

        panel.add(new JLabel("Delivery Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Book Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Book Author:"));
        authorField = new JTextField();
        panel.add(authorField);

        panel.add(new JLabel("Book Quantity:"));
        quantityField = new JTextField("1");
        quantityField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != java.awt.event.KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });
        panel.add(quantityField);

        JButton addBtn = new JButton("Add Order");
        addBtn.addActionListener(e -> addOrder());
        panel.add(addBtn);

        JButton processBtn = new JButton("Process Order");
        processBtn.addActionListener(e -> processOrder());
        panel.add(processBtn);

        JButton searchBtn = new JButton("Search Order");
        searchBtn.addActionListener(e -> openSearchWindow());
        panel.add(searchBtn);

        add(panel, BorderLayout.NORTH);

        JButton saveBtn = new JButton("Save Data");
        saveBtn.addActionListener(e -> saveData());
        add(saveBtn, BorderLayout.SOUTH);

        refreshDisplay();
    }

    private void addOrder() {
        String customer = customerField.getText().trim();
        String address = addressField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String quantityText = quantityField.getText().trim();
        
        if (customer.isEmpty() || address.isEmpty() || title.isEmpty() || author.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled in completely!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Number of books must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int orderNum = nextOrderNumber;
            Book book = new Book(title, author);
            List<Book> books = new LinkedList<>();
            books.add(book);
            Order order = new Order(orderNum, customer, address, books, quantity);
            orderQueue.enqueue(order);
            
            nextOrderNumber++;
            orderNumberField.setText(String.valueOf(nextOrderNumber));
            
            customerField.setText("");
            addressField.setText("");
            titleField.setText("");
            authorField.setText("");
            quantityField.setText("1");
            
            refreshDisplay();
            JOptionPane.showMessageDialog(this, "Order added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid book quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processOrder() {
        Order order = orderQueue.dequeue();
        if (order != null) {
            history.push(order);
            FileUtil.save(history, HISTORY_FILE);
            JOptionPane.showMessageDialog(this, "Processed: " + order);
        } else {
            JOptionPane.showMessageDialog(this, "No orders to process.");
        }
        refreshDisplay();
    }

    private void saveData() {
        FileUtil.save(orderQueue, DATA_FILE);
        FileUtil.save(history, HISTORY_FILE);
        JOptionPane.showMessageDialog(this, "Data saved successfully!");
    }

    private void openSearchWindow() {
        new SearchWindow(this, orderQueue, history);
    }

    private void refreshDisplay() {
        StringBuilder sb = new StringBuilder("Pending Orders:\n");
        List<Order> pendingOrders = orderQueue.getAllOrders();
        Sorting.insertionSort(pendingOrders);
        for (Order o : pendingOrders) {
            sb.append(o).append("\n");
        }
        
        sb.append("\nHistory (All Orders):\n");
        List<Order> allHistory = history.getAllOrders();
        if (allHistory.isEmpty()) {
            sb.append("No processed orders yet.");
        } else {
            for (Order o : allHistory) {
                sb.append(o).append("\n");
            }
        }
        
        output.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
