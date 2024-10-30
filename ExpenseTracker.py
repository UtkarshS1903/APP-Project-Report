import tkinter as tk
from tkinter import ttk, messagebox

class ExpenseTracker:
    def __init__(self, root):
        self.root = root
        self.root.title("Expense Tracker")
        self.root.geometry("600x400")

        self.budget = 0
        self.total_expenses = 0

        self.table_model = ttk.Treeview(self.root, columns=("Expense Name", "Amount (Rs)", "Delete"), show="headings")
        self.table_model.heading("Expense Name", text="Expense Name")
        self.table_model.heading("Amount (Rs)", text="Amount (Rs)")
        self.table_model.heading("Delete", text="Delete")
        self.table_model.pack(fill="both", expand=True)

        # Input fields and add expense button
        input_panel = tk.Frame(self.root)
        input_panel.pack(fill="x")
        tk.Label(input_panel, text="Expense Name:").pack(side="left")
        self.expense_name_field = tk.Entry(input_panel)
        self.expense_name_field.pack(side="left")
        tk.Label(input_panel, text="Amount (Rs):").pack(side="left")
        self.amount_field = tk.Entry(input_panel)
        self.amount_field.pack(side="left")
        add_expense_button = tk.Button(input_panel, text="Add Expense", command=self.add_expense)
        add_expense_button.pack(side="left")

        # Budget setting panel
        budget_panel = tk.Frame(self.root)
        budget_panel.pack(fill="x")
        tk.Label(budget_panel, text="Set Budget (Rs):").pack(side="left")
        self.budget_field = tk.Entry(budget_panel)
        self.budget_field.pack(side="left")
        set_budget_button = tk.Button(budget_panel, text="Set Budget", command=self.set_budget)
        set_budget_button.pack(side="left")
        self.budget_label = tk.Label(budget_panel, text="Total Expenses: Rs 0.00 / Budget: Rs 0")
        self.budget_label.pack(side="left")
        self.overspend_label = tk.Label(budget_panel, text="", fg="red")
        self.overspend_label.pack(side="left")

        delete_button = tk.Button(self.root, text="Delete Expense", command=self.delete_expense)
        delete_button.pack()

        self.root.mainloop()

    def add_expense(self):
        expense_name = self.expense_name_field.get()
        amount_text = self.amount_field.get()

        if not expense_name or not amount_text:
            messagebox.showerror("Error", "Please enter both expense name and amount.")
            return

        try:
            amount = float(amount_text)
            self.total_expenses += amount

            self.table_model.insert("", "end", values=(expense_name, amount, "Delete"))
            self.expense_name_field.delete(0, "end")
            self.amount_field.delete(0, "end")
            self.update_budget_label()
        except ValueError:
            messagebox.showerror("Error", "Please enter a valid amount.")

    def set_budget(self):
        budget_text = self.budget_field.get()

        if not budget_text:
            messagebox.showerror("Error", "Please enter a budget.")
            return

        try:
            self.budget = float(budget_text)
            self.update_budget_label()
        except ValueError:
            messagebox.showerror("Error", "Please enter a valid budget amount.")

    def update_budget_label(self):
        self.budget_label.config(text=f"Total Expenses: Rs {self.total_expenses:.2f} / Budget: Rs {self.budget:.2f}")

        if self.total_expenses > self.budget:
            overspend = self.total_expenses - self.budget
            self.overspend_label.config(text=f"Overspent by Rs {overspend:.2f}")
            messagebox.showwarning("Budget Warning", f"Warning: You have exceeded your budget by Rs {overspend:.2f}!")
        else:
            self.overspend_label.config(text="")

    def delete_expense(self):
        selected_item = self.table_model.focus()
        if selected_item:
            amount = self.table_model.item(selected_item, "values")[1]
            self.total_expenses -= float(amount)
            self.table_model.delete(selected_item)
            self.update_budget_label()
        else:
            messagebox.showerror("Error", "Please select an expense to delete.")

if __name__ == "__main__":
    root = tk.Tk()
    app = ExpenseTracker(root)
