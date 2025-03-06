package GUI;

import DAO.ClientiDAO;
import DAO.ComenziDAO;
import DAO.LogDAO;
import DAO.ProduseDAO;
import model.Bill;
import model.Clienti;
import model.Comenzi;
import model.Produse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clasa GUIManager reprezintă interfața grafică a sistemului de gestionare a magazinului online.
 * Această clasă conține panouri pentru gestionarea clienților, produselor, comenzilor și a facturilor.
 */
public class GUIManager extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(GUIManager.class.getName());

    private DefaultTableModel clientTableModel;
    private DefaultTableModel productTableModel;
    private DefaultTableModel comenziTableModel;
    private DefaultTableModel logTableModel;

    private JTable clientTable;
    private JTable productTable;
    private JTable comenziTable;
    private JTable logTable;

    private ClientiDAO clientiDAO;
    private ProduseDAO produseDAO;
    private ComenziDAO comenziDAO;
    private LogDAO logDAO;
    /**
     * Constructorul clasei GUIManager.
     * Inițializează interfața grafică și managerii de acces la date.
     */
    public GUIManager() {
        clientiDAO = new ClientiDAO();
        produseDAO = new ProduseDAO();
        comenziDAO = new ComenziDAO();
        logDAO = new LogDAO();

        setTitle("Online Store Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel clientPanel = createClientPanel();
        JPanel productPanel = createProductPanel();
        JPanel comenziPanel = createComenziPanel();
        JPanel logPanel = LogPanel();

        tabbedPane.addTab("Clients", clientPanel);
        tabbedPane.addTab("Products", productPanel);
        tabbedPane.addTab("Comenzi", comenziPanel);
        tabbedPane.addTab("Logs", logPanel);

        getContentPane().add(tabbedPane);
    }
    /**
     * Creează panoul pentru gestionarea clienților.
     *
     * @return panoul pentru gestionarea clienților
     */
    private JPanel createClientPanel() {
        JPanel clientPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        clientPanel.add(buttonPanel, BorderLayout.NORTH);

        JButton addClientButton = new JButton("Add New Client");
        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nume = JOptionPane.showInputDialog(GUIManager.this, "Enter client name:");
                String email = JOptionPane.showInputDialog(GUIManager.this, "Enter client email:");
                String adresa = JOptionPane.showInputDialog(GUIManager.this, "Enter client address:");

                if (nume != null && email != null && adresa != null) {
                    Clienti client = new Clienti(nume, email, adresa);
                    try {
                        clientiDAO.insert(client);
                        JOptionPane.showMessageDialog(GUIManager.this, "Client added successfully!");
                        refreshClientTable();
                    } catch (IllegalAccessException ex) {
                        LOGGER.log(Level.SEVERE, "Error adding client", ex);
                        JOptionPane.showMessageDialog(GUIManager.this, "Failed to add client: " + ex.getMessage());
                    }
                }
            }
        });
        buttonPanel.add(addClientButton);

        JButton deleteClientButton = new JButton("Delete Client");
        deleteClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) clientTable.getValueAt(selectedRow, 0);
                    try {
                        clientiDAO.delete(id);
                        JOptionPane.showMessageDialog(GUIManager.this, "Client deleted successfully!");
                        refreshClientTable();
                    } catch (IllegalAccessException ex) {
                        LOGGER.log(Level.SEVERE, "Error deleting client", ex);
                        JOptionPane.showMessageDialog(GUIManager.this, "Failed to delete client: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(GUIManager.this, "Please select a client to delete.");
                }
            }
        });
        buttonPanel.add(deleteClientButton);

        JButton updateClientButton = new JButton("Update Client");
        updateClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = clientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) clientTable.getValueAt(selectedRow, 0);
                    String nume = (String) clientTable.getValueAt(selectedRow, 1);
                    String email = (String) clientTable.getValueAt(selectedRow, 2);
                    String adresa = (String) clientTable.getValueAt(selectedRow, 3);

                    nume = JOptionPane.showInputDialog(GUIManager.this, "Update client name:", nume);
                    email = JOptionPane.showInputDialog(GUIManager.this, "Update client email:", email);
                    adresa = JOptionPane.showInputDialog(GUIManager.this, "Update client address:", adresa);

                    if (nume != null && email != null && adresa != null) {
                        Clienti client = new Clienti(id, nume, email, adresa);
                        try {
                            clientiDAO.update(client);
                            JOptionPane.showMessageDialog(GUIManager.this, "Client updated successfully!");
                            refreshClientTable();
                        } catch (IllegalAccessException ex) {
                            LOGGER.log(Level.SEVERE, "Error updating client", ex);
                            JOptionPane.showMessageDialog(GUIManager.this, "Failed to update client: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(GUIManager.this, "Please select a client to update.");
                }
            }
        });
        buttonPanel.add(updateClientButton);

        clientTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Address"}, 0);
        clientTable = new JTable(clientTableModel);
        JScrollPane tableScrollPane = new JScrollPane(clientTable);
        clientPanel.add(tableScrollPane, BorderLayout.CENTER);

        refreshClientTable();

        return clientPanel;
    }
    /**
     * Creează panoul pentru gestionarea produselor.
     *
     * @return panoul pentru gestionarea produselor
     */
    private JPanel createProductPanel() {
        JPanel productPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        productPanel.add(buttonPanel, BorderLayout.NORTH);

        JButton addProductButton = new JButton("Add New Product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String denumire = JOptionPane.showInputDialog(GUIManager.this, "Enter product name:");
                String cantitateString = JOptionPane.showInputDialog(GUIManager.this, "Enter product quantity:");
                String pretString = JOptionPane.showInputDialog(GUIManager.this, "Enter product price:");

                if (denumire != null && cantitateString != null && pretString != null) {
                    int cantitate = Integer.parseInt(cantitateString);
                    int pret = Integer.parseInt(pretString);

                    Produse produs = new Produse(denumire, cantitate, pret);
                    try {
                        produseDAO.insert(produs);
                        JOptionPane.showMessageDialog(GUIManager.this, "Product added successfully!");
                        refreshProductTable();
                    } catch (IllegalAccessException ex) {
                        LOGGER.log(Level.SEVERE, "Error adding product", ex);
                        JOptionPane.showMessageDialog(GUIManager.this, "Failed to add product: " + ex.getMessage());
                    }
                }
            }
        });
        buttonPanel.add(addProductButton);

        JButton deleteProductButton = new JButton("Delete Product");
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) productTable.getValueAt(selectedRow, 0);
                    try {
                        produseDAO.delete(id);
                        JOptionPane.showMessageDialog(GUIManager.this, "Product deleted successfully!");
                        refreshProductTable();
                    } catch (IllegalAccessException ex) {
                        LOGGER.log(Level.SEVERE, "Error deleting product", ex);
                        JOptionPane.showMessageDialog(GUIManager.this, "Failed to delete product: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(GUIManager.this, "Please select a product to delete.");
                }
            }
        });
        buttonPanel.add(deleteProductButton);

        JButton updateProductButton = new JButton("Update Product");
        updateProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) productTable.getValueAt(selectedRow, 0);
                    String denumire = (String) productTable.getValueAt(selectedRow, 1);
                    int cantitate = (int) productTable.getValueAt(selectedRow, 2);
                    int pret = (int) productTable.getValueAt(selectedRow, 3);

                    denumire = JOptionPane.showInputDialog(GUIManager.this, "Update product name:", denumire);
                    String cantitateString = JOptionPane.showInputDialog(GUIManager.this, "Update product quantity:", cantitate);
                    String pretString = JOptionPane.showInputDialog(GUIManager.this, "Update product price:", pret);

                    if (denumire != null && cantitateString != null && pretString != null) {
                        cantitate = Integer.parseInt(cantitateString);
                        pret = Integer.parseInt(pretString);

                        Produse produs = new Produse(id, denumire, cantitate, pret);
                        try {
                            produseDAO.update(produs);
                            JOptionPane.showMessageDialog(GUIManager.this, "Product updated successfully!");
                            refreshProductTable();
                        } catch (IllegalAccessException ex) {
                            LOGGER.log(Level.SEVERE, "Error updating product", ex);
                            JOptionPane.showMessageDialog(GUIManager.this, "Failed to update product: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(GUIManager.this, "Please select a product to update.");
                }
            }
        });
        buttonPanel.add(updateProductButton);

        productTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Quantity", "Price"}, 0);
        productTable = new JTable(productTableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        productPanel.add(tableScrollPane, BorderLayout.CENTER);

        refreshProductTable();

        return productPanel;
    }
    /**
     * Creează panoul pentru gestionarea comenzilor.
     *
     * @return panoul pentru gestionarea comenzilor
     */
    private JPanel createComenziPanel() {
        JPanel comenziPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comenziPanel.add(buttonPanel, BorderLayout.NORTH);

        JButton addComandaButton = new JButton("Add New Comanda");
        addComandaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> clientComboBox = new JComboBox<>();
                JComboBox<String> productComboBox = new JComboBox<>();
                JTextField cantitateTextField = new JTextField();

                List<Clienti> listaClienti = null;
                try {
                    ClientiDAO clientiDAO = new ClientiDAO();
                    listaClienti = clientiDAO.getAllClients();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error fetching client names", ex);
                }
                if (listaClienti != null) {
                    for (Clienti client : listaClienti) {
                        clientComboBox.addItem(client.getNume());
                    }
                }

                // Fetch product names from the database and populate the productComboBox
                List<Produse> listaProduse = null;
                try {
                    ProduseDAO produseDAO = new ProduseDAO();
                    listaProduse = produseDAO.getAllProducts();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error fetching product names", ex);
                }
                if (listaProduse != null) {
                    for (Produse produs : listaProduse) {
                        productComboBox.addItem(produs.getDenumire());
                    }
                }




                JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 5));
                inputPanel.add(new JLabel("Client:"));
                inputPanel.add(clientComboBox);
                inputPanel.add(new JLabel("Product:"));
                inputPanel.add(productComboBox);
                inputPanel.add(new JLabel("Quantity:"));
                inputPanel.add(cantitateTextField);

                int result = JOptionPane.showConfirmDialog(GUIManager.this, inputPanel, "Add New Comanda", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String numeClient = (String) clientComboBox.getSelectedItem();
                    String denumireProdus = (String) productComboBox.getSelectedItem();
                    String cantitateString = cantitateTextField.getText();
                    Produse produsAles = new Produse("NIMIC", 0,0);
                    if (!cantitateString.isEmpty()) {
                        int cantitate = Integer.parseInt(cantitateString);
                        Comenzi comanda = new Comenzi(numeClient, denumireProdus, cantitate);
                        try {

                            for (Produse produs : listaProduse) {
                                if(produs.getDenumire().equals(denumireProdus))
                                    produsAles = produs;
                            }
                            if(cantitate <= produsAles.getCantitate()){
                                comenziDAO.insert(comanda);
                                produsAles.setCantitate(produsAles.getCantitate() - cantitate);
                                comenziDAO.update(comanda);
                                produseDAO.update(produsAles);
                                Bill bill = new Bill(comanda.getIdComanda(), comanda.getNumeClient(), comanda.getNumeProdus(), comanda.getCantitate(), comanda.getCantitate() * produsAles.getPret());
                                // Insert Bill object into Log table
                                LogDAO logDAO = new LogDAO();
                                logDAO.insert(bill);
                                JOptionPane.showMessageDialog(GUIManager.this, "Comanda added successfully!");
                                refreshComenziTable();
                                refreshProductTable();
                                refreshBillTable();
                            }
                            else{
                                JOptionPane.showMessageDialog(GUIManager.this, "Not enough in stock!");
                            }

                        } catch (IllegalAccessException ex) {
                            LOGGER.log(Level.SEVERE, "Error adding comanda", ex);
                            JOptionPane.showMessageDialog(GUIManager.this, "Failed to add comanda: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(GUIManager.this, "Please enter quantity.");
                    }
                }
            }
        });
        buttonPanel.add(addComandaButton);

        JButton deleteComandaButton = new JButton("Delete Comanda");
        deleteComandaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = comenziTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) comenziTable.getValueAt(selectedRow, 0);
                    try {
                        comenziDAO.delete(id);
                        JOptionPane.showMessageDialog(GUIManager.this, "Comanda deleted successfully!");
                        refreshComenziTable();
                    } catch (IllegalAccessException ex) {
                        LOGGER.log(Level.SEVERE, "Error deleting comanda", ex);
                        JOptionPane.showMessageDialog(GUIManager.this, "Failed to delete comanda: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(GUIManager.this, "Please select a comanda to delete.");
                }
            }
        });
        buttonPanel.add(deleteComandaButton);

        JButton viewAllComenziButton = new JButton("View All Comenzi");
        viewAllComenziButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshComenziTable();
            }
        });
        buttonPanel.add(viewAllComenziButton);

        comenziTableModel = new DefaultTableModel(new String[]{"ID", "Client Name", "Product Name", "Quantity"}, 0);
        comenziTable = new JTable(comenziTableModel);
        JScrollPane tableScrollPane = new JScrollPane(comenziTable);
        comenziPanel.add(tableScrollPane, BorderLayout.CENTER);

        refreshComenziTable();

        return comenziPanel;
    }
    /**
     * Creează panoul pentru afișarea facturilor.
     *
     * @return panoul pentru afișarea facturilor
     */
    private JPanel LogPanel() {
        JPanel billPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        billPanel.add(buttonPanel, BorderLayout.NORTH);

        JButton refreshBillButton = new JButton("Refresh Bills");
        refreshBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBillTable();
            }
        });
        buttonPanel.add(refreshBillButton);

        logTableModel = new DefaultTableModel(new String[]{"ID", "Client Name", "Product Name", "Quantity", "Price"}, 0);
        logTable = new JTable(logTableModel);
        JScrollPane tableScrollPane = new JScrollPane(logTable);
        billPanel.add(tableScrollPane, BorderLayout.CENTER);

        refreshBillTable();

        return billPanel;
    }
    /**
     * Reîmprospătează tabelul de facturi cu cele mai recente date.
     */
    private void refreshBillTable() {
        logTableModel.setRowCount(0); // Clear the table before repopulating

        // Retrieve the bill information from the LogDAO
        ResultSet resultSet = logDAO.report();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    Object[] row = {
                            resultSet.getInt("id"),
                            resultSet.getString("numeClient"),
                            resultSet.getString("numeProdus"),
                            resultSet.getInt("cantitate"),
                            resultSet.getInt("pret")
                    };
                    // Add the row to the logTableModel
                    logTableModel.addRow(row);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error while reading bills from database: " + e.getMessage());
            }
        }
    }

    /**
     * Reîmprospătează tabelul de clienți cu cele mai recente date.
     */
    private void refreshClientTable() {
        clientTableModel.setRowCount(0);
        ResultSet resultSet = clientiDAO.report();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    Object[] row = {
                            resultSet.getInt("id"),
                            resultSet.getString("nume"),
                            resultSet.getString("email"),
                            resultSet.getString("adresa")
                    };
                    clientTableModel.addRow(row);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error while reading clients from database: " + e.getMessage());
            }
        }
    }
    /**
     * Reîmprospătează tabelul de produse cu cele mai recente date.
     */
    private void refreshProductTable() {
        productTableModel.setRowCount(0);
        ResultSet resultSet = produseDAO.report();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    Object[] row = {
                            resultSet.getInt("id"),
                            resultSet.getString("denumire"),
                            resultSet.getInt("cantitate"),
                            resultSet.getInt("pret")
                    };
                    productTableModel.addRow(row);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error while reading products from database: " + e.getMessage());
            }
        }
    }
    /**
     * Reîmprospătează tabelul de comenzi cu cele mai recente date.
     */
    private void refreshComenziTable() {
        comenziTableModel.setRowCount(0);
        ResultSet resultSet = comenziDAO.report();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    Object[] row = {
                            resultSet.getInt("id"),
                            resultSet.getString("numeClient"),
                            resultSet.getString("numeProdus"),
                            resultSet.getInt("cantitate")
                    };
                    comenziTableModel.addRow(row);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error while reading comenzi from database: " + e.getMessage());
            }
        }
    }

    /**
     * Metoda principală pentru pornirea aplicației.
     *
     * @param args argumentele liniei de comandă (nu sunt utilizate)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUIManager guiManager = new GUIManager();
                guiManager.setVisible(true);
            }
        });
    }
}
