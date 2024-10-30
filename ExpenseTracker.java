import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ExpenseTracker extends JFrame {
    private DefaultTableModel tableModel;
    private JTable expenseTable;
    private JTextField expenseNameField;
    private JTextField amountField;
    private JTextField budgetField;
    private JLabel budgetLabel;
    private JLabel overspendLabel;
    private double budget = 0; // Default budget in Rs
    private double totalExpenses = 0.00;

    public ExpenseTracker() {
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Create the table model with Expense Name, Amount, and Delete columns
        tableModel = new DefaultTableModel(new Object[]{"Expense Name", "Amount (Rs)", "Delete"}, 0);
        expenseTable = new JTable(tableModel);

        // Add mouse listener to detect clicks on the Delete column
        expenseTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = expenseTable.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / expenseTable.getRowHeight();

                if (row < expenseTable.getRowCount() && row >= 0 && column == 2) {
                    removeExpense(row); // Remove the expense when "Delete" is clicked
                }
            }
        });

        // Input fields and add expense button
        JPanel inputPanel = new JPanel(new FlowLayout());
        expenseNameField = new JTextField(10);
        amountField = new JTextField(5);
        JButton addExpenseButton = new JButton("Add Expense");

        inputPanel.add(new JLabel("Expense Name:"));
        inputPanel.add(expenseNameField);
        inputPanel.add(new JLabel("Amount (Rs):"));
        inputPanel.add(amountField);
        inputPanel.add(addExpenseButton);

        // Budget setting panel
        JPanel budgetPanel = new JPanel(new FlowLayout());
        budgetField = new JTextField(5);
        JButton setBudgetButton = new JButton("Set Budget");
        budgetLabel = new JLabel("Total Expenses: Rs 0.00 / Budget: Rs " + budget);
        overspendLabel = new JLabel("");
        overspendLabel.setForeground(Color.RED);

        budgetPanel.add(new JLabel("Set Budget (Rs):"));
        budgetPanel.add(budgetField);
        budgetPanel.add(setBudgetButton);
        budgetPanel.add(budgetLabel);
        budgetPanel.add(overspendLabel);

        // Add action listener to "Add Expense" button
        addExpenseButton.addActionListener(e -> addExpense());

        // Add action listener to "Set Budget" button
        setBudgetButton.addActionListener(e -> setBudget());

        // Layout setup
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(expenseTable), BorderLayout.CENTER);
        add(budgetPanel, BorderLayout.SOUTH);
    }

    // Add a new expense to the table
    private void addExpense() {
        String expenseName = expenseNameField.getText();
        String amountText = amountField.getText();

        if (expenseName.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both expense name and amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            totalExpenses += amount;

            // Add a new row with "Delete" action as "X"
            tableModel.addRow(new Object[]{expenseName, amount, "X"});
            updateBudgetLabel();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Set a new budget based on user input
    private void setBudget() {
        String budgetText = budgetField.getText();

        if (budgetText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a budget.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            budget = Double.parseDouble(budgetText);
            updateBudgetLabel();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid budget amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remove an expense from the table and update total expenses
    private void removeExpense(int row) {
        double amount = (double) tableModel.getValueAt(row, 1);
        totalExpenses -= amount;
        tableModel.removeRow(row); // Remove the row from the table model
        updateBudgetLabel();
    }

    // Update the label that shows the current budget and total expenses
    private void updateBudgetLabel() {
        budgetLabel.setText("Total Expenses: Rs " + totalExpenses + " / Budget: Rs " + budget);

        if (totalExpenses > budget) {
            double overspend = totalExpenses - budget;
            overspendLabel.setText("Overspent by Rs " + overspend);
            JOptionPane.showMessageDialog(this, "Warning: You have exceeded your budget by Rs " + overspend + "!", "Budget Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            overspendLabel.setText(""); // Clear the overspend warning if under budget
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseTracker().setVisible(true));
    }
}
