package com.photoserviceproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.time.LocalDate;
import javax.swing.DefaultComboBoxModel;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kenneth Mallabo & Zinah Al-Baghdadi
 */
public class PhotoServiceApp1 extends javax.swing.JFrame {

    /**
     * Creates new form PhotoServiceApp
     */
    private String[] rentPriceRanges = {null, "rentPrice > 0 AND rentPrice <= 30", "rentPrice > 30 AND rentPrice <= 70", "rentPrice > 70"};
    private String[] sellPriceRanges = {null, "sellPrice > 0 AND sellPrice <= 100", "sellPrice > 100 AND sellPrice <= 500", "sellPrice > 500 AND sellPrice <= 1000", "sellPrice > 1000"};
    private boolean userLoggedIn;

    public PhotoServiceApp1() {
        // initComponents();
        // variables
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSet resultUsers;
        ResultSet resultStocks;
        ResultSet resultOverdue;
        String msAccDB = "photo_serviceDB.accdb"; // path to the DB file
        String dbURL = "jdbc:ucanaccess://" + msAccDB;
    
//        String msAccDB = "C:\\Users\\kenne\\OneDrive\\Documents\\Photo_DB\\photo_serviceDB.accdb"; // path to the DB file
//        String dbURL = "jdbc:ucanaccess://" + msAccDB;

        // Step 1: Loading or registering JDBC driver class
        try {
            // Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException cnfex) {
            System.out.println("Problem in loading or "
                    + "registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }
        // Step 2: Opening database connection
        try {
            // initialise SQL query strings
            String sqlStr = "SELECT * FROM Admin";
            String sqlStr2 = "SELECT * FROM Client";
            String sqlStr3 = "SELECT brandName, stockName, sellPrice, stockPhoto FROM StockDetails";
            String sqlStr4 = "SELECT * FROM Rent";
            // Step 2.A: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL);

            // Step 2.B: Creating JDBC Statement
            statement = connection.createStatement();

            // Step 2.C: Executing SQL &amp; retrieve data into ResultSet
            resultSet = statement.executeQuery(sqlStr);
            resultUsers = statement.executeQuery(sqlStr2);
            resultStocks = statement.executeQuery(sqlStr3);
            resultOverdue = statement.executeQuery(sqlStr4);
            
            // retrieve users' data by using metadata
            ResultSetMetaData metaData2 = resultUsers.getMetaData(); 
            // fetch how many columns in meta data
            int numberOfColumns1 = metaData2.getColumnCount();
            // using StringBuilder to append each data read from database
            StringBuilder sb = new StringBuilder(numberOfColumns1);
            for (int i = 1; i <= numberOfColumns1; i++) {
                // append each name of the columns
                sb.append(metaData2.getColumnName(i) + "    ");
            }
            // check if there are data from the database
            while (resultUsers.next()) {
                sb.append("\n");
                for (int i = 1; i <= numberOfColumns1; i++) {
                    // append each data
                    sb.append( resultUsers.getString(i) + "   ");
                }
            }
            // set private StringBuilder variable from local method
            this.sbUsers = sb;
            
            // retrieve stock data by using metadata
            ResultSetMetaData metaData3 = resultStocks.getMetaData(); 
            // fetch how many columns in meta data
            int numberOfColumns3 = metaData3.getColumnCount();
            // using StringBuilder to append each data read from database
            StringBuilder sb2 = new StringBuilder(numberOfColumns3);
            for (int i = 1; i <= numberOfColumns3; i++) {
                // append each name of the columns
                sb2.append(metaData3.getColumnName(i) + "    ");
            }
            // check if there are data from the database
            while (resultStocks.next()) {
                sb2.append("\n");
                for (int i = 1; i <= numberOfColumns3; i++) {
                    // append each data
                    sb2.append(resultStocks.getString(i) + "   ");
                }
            }
            // set private StringBuilder variable from local method
            this.sbStocks = sb2;
            
            // retrieve overdue rent data by using metadata
            ResultSetMetaData metaData4 = resultOverdue.getMetaData(); 
            // fetch how many columns in meta data
            int numberOfColumns4 = metaData4.getColumnCount();
            // using StringBuilder to append each data read from database
            StringBuilder sb3 = new StringBuilder(numberOfColumns4);
            for (int i = 1; i <= numberOfColumns4; i++) {
                // append each name of the columns
                sb3.append(metaData4.getColumnName(i) + "    ");
            }
            // check if there are data from the database
            while (resultOverdue.next()) {
                System.out.println("");
                sb3.append("\n");
                for (int i = 1; i <= numberOfColumns4; i++) {
                    // append each data
                    sb3.append(resultOverdue.getString(i) + "   ");
                }
            }
            // set private StringBuilder variable from local method
            this.sbOverdue = sb3;

        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } finally {

            // Step 3: Closing database connection
            try {
                if (null != resultSet) {
                    // cleanup resources, once after processing
                    resultSet.close();
                }
                if (null != statement) {
                    // cleanup resources, once after processing
                    statement.close();
                }
                if (null != connection) {
                    // and then finally close connection
                    connection.close();
                }
            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }
        // initialise GUI
        initComponents();
        siteTabs.remove(shoppingCart);
        sellPriceBox.setEditable(false);
        rentPriceBox.setEditable(false);
    }
    
    // method to get connection to the database (Reference: https://1bestcsharp.blogspot.com/2018/05/java-login-and-register-form-with-mysql-database.html )
     public static Connection getConnection(){ 
        // variables
        Connection connection = null;
        
        String msAccDB = "photo_serviceDB.accdb"; // path to the DB file
        String dbURL = "jdbc:ucanaccess://" + msAccDB;
//        String msAccDB = "C:\\Users\\kenne\\OneDrive\\Documents\\Photo_DB\\photo_serviceDB.accdb"; // path to the DB file
//        String dbURL = "jdbc:ucanaccess://" + msAccDB;
    
        // Step 1: Loading or registering JDBC driver class
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException cnfex) {
            System.out.println("Problem in loading or "
                    + "registering MS Access JDBC driver");
            cnfex.printStackTrace();
        }
        try {
            // Step 2: Create and get connection using DriverManager class
            connection = DriverManager.getConnection(dbURL);
            
        } catch (SQLException sqlex) {
            System.err.println("SQL statement issue " + sqlex.getMessage());
        } 
        // return variable
        return connection;
    }
    
    // checks if user already exists in the database for registration (Reference: https://1bestcsharp.blogspot.com/2018/05/java-login-and-register-form-with-mysql-database.html )
    public boolean checkUsername(String username)
    {
        // variables
        PreparedStatement pStatement;
        ResultSet rs;
        boolean checkUser = false;
        // initialise SQL query strings
        String query = "SELECT * FROM Client WHERE username = ?";
        String query2 = "SELECT * FROM Admin WHERE username = ?";
        
        try {
            //Creating JDBC prepare Statement
            pStatement = PhotoServiceApp1.getConnection().prepareStatement(query);
            // set parameters
            pStatement.setString(1, username);
            // execute SQL query
            rs = pStatement.executeQuery();
            // check if there are data from the database
            if(rs.next())
            {
                checkUser = true;
            }
            else{
                //Creating JDBC prepare Statement
                pStatement = PhotoServiceApp1.getConnection().prepareStatement(query2);
                // set parameters
                pStatement.setString(1, username);
                // execute SQL query
                rs = pStatement.executeQuery();
                // check if there are data from the database
                if(rs.next())
                {
                    checkUser = true;
                }
            }
        } catch (SQLException sqlex) {
            System.err.println(sqlex.getMessage());
        }
         return checkUser;
    }

    public StringBuilder getSbUsers() {
        return sbUsers;
    }

    public StringBuilder getSbStocks() {
        return sbStocks;
    }

    public StringBuilder getSbOverdue() {
        return sbOverdue;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        popupMenu1 = new java.awt.PopupMenu();
        popupMenu2 = new java.awt.PopupMenu();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        jPopupMenu4 = new javax.swing.JPopupMenu();
        jMenu1 = new javax.swing.JMenu();
        jPopupMenu5 = new javax.swing.JPopupMenu();
        popupMenu3 = new java.awt.PopupMenu();
        jPopupMenu6 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jDialog3 = new javax.swing.JDialog();
        jTextField3 = new javax.swing.JTextField();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jTextField4 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPasswordField4 = new javax.swing.JPasswordField();
        jLabel39 = new javax.swing.JLabel();
        jPopupMenu7 = new javax.swing.JPopupMenu();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        siteTabs = new javax.swing.JTabbedPane();
        jTabbedPaneLoginRegister = new javax.swing.JTabbedPane();
        jPanelLogin = new javax.swing.JPanel();
        jButtonLogin = new javax.swing.JButton();
        jPasswordFieldLogin = new javax.swing.JPasswordField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jTextFieldLoginUsername = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jPanelRegister = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldUserRegister = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPasswordFieldRegister1 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jPasswordFieldRegister2 = new javax.swing.JPasswordField();
        jButtonRegister = new javax.swing.JButton();
        jPanelSettings = new javax.swing.JPanel();
        jTextFieldRemoveUsername = new javax.swing.JTextField();
        jButtonRemoveAccount = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jPanelReturnRent = new javax.swing.JPanel();
        jTextFieldReturnRentedUsername = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jButtonReturnRent = new javax.swing.JButton();
        jLabel81 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jTextFieldReturnRentedID = new javax.swing.JTextField();
        jPanelReturnProduct = new javax.swing.JPanel();
        jButtonReturnProduct = new javax.swing.JButton();
        jLabel89 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jTextFieldReturnProductUsername = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jTextFieldReturnProductItemID = new javax.swing.JTextField();
        jPanelListUsers = new javax.swing.JPanel();
        jLabel93 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanelListStock = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel94 = new javax.swing.JLabel();
        jPanelOverdue = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jLabel95 = new javax.swing.JLabel();
        jPanelLogout = new javax.swing.JPanel();
        jButtonLogout = new javax.swing.JButton();
        jLabel96 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        sellPage = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        sellPriceRangeComboBox = new javax.swing.JComboBox<>();
        sellCompanyComboBox = new javax.swing.JComboBox<>();
        sellItemComboBox = new javax.swing.JComboBox<>();
        addToCartBuyButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        sellItemDescriptionBox = new javax.swing.JTextArea();
        sellPriceBox = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        quantity = new javax.swing.JTextField();
        rentPage = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        rentPriceComboBox = new javax.swing.JComboBox<>();
        rentCompanyComboBox = new javax.swing.JComboBox<>();
        rentItemComboBox = new javax.swing.JComboBox<>();
        addToCartRentButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        itemDescriptionBox = new javax.swing.JTextArea();
        rentPriceBox = new javax.swing.JTextField();
        shoppingCart = new javax.swing.JPanel();
        cartContainer = new javax.swing.JPanel();
        cartBuy = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        buyItemButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        cartBuyTable = new javax.swing.JTable();
        cartRent = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        rentItemButton = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        cartRentTable = new javax.swing.JTable();

        jLabel1.setText("jLabel1");

        popupMenu1.setLabel("popupMenu1");

        popupMenu2.setLabel("popupMenu2");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jMenu1.setText("jMenu1");

        popupMenu3.setLabel("popupMenu3");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jTextField3.setText("jTextField3");

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTextField4.setText("jTextField1");

        jLabel38.setText("Password");

        jButton3.setText("Login");

        jPasswordField4.setText("jPasswordField1");

        jLabel39.setText("Username");

        jLabel14.setText("jLabel14");

        jLabel15.setText("jLabel15");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 700));
        setSize(new java.awt.Dimension(1000, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        siteTabs.setPreferredSize(new java.awt.Dimension(1000, 570));

        jButtonLogin.setText("Login");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel34.setText("Login Page");

        jLabel35.setText("Username");

        jTextFieldLoginUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLoginUsernameActionPerformed(evt);
            }
        });

        jLabel36.setText("Password");

        javax.swing.GroupLayout jPanelLoginLayout = new javax.swing.GroupLayout(jPanelLogin);
        jPanelLogin.setLayout(jPanelLoginLayout);
        jPanelLoginLayout.setHorizontalGroup(
            jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginLayout.createSequentialGroup()
                .addGap(463, 463, 463)
                .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPasswordFieldLogin, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldLoginUsername, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(456, Short.MAX_VALUE))
        );
        jPanelLoginLayout.setVerticalGroup(
            jPanelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldLoginUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonLogin)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("Login", jPanelLogin);

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel37.setText("Register Page");

        jLabel3.setText("Username");

        jLabel4.setText("Password");

        jLabel5.setText("Password Again");

        jButtonRegister.setText("Register");
        jButtonRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegisterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelRegisterLayout = new javax.swing.GroupLayout(jPanelRegister);
        jPanelRegister.setLayout(jPanelRegisterLayout);
        jPanelRegisterLayout.setHorizontalGroup(
            jPanelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegisterLayout.createSequentialGroup()
                .addGap(471, 471, 471)
                .addGroup(jPanelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelRegisterLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(jPasswordFieldRegister2)
                    .addComponent(jPasswordFieldRegister1)
                    .addComponent(jTextFieldUserRegister)
                    .addComponent(jButtonRegister, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(438, Short.MAX_VALUE))
        );
        jPanelRegisterLayout.setVerticalGroup(
            jPanelRegisterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegisterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldUserRegister, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordFieldRegister1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordFieldRegister2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonRegister)
                .addContainerGap(253, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("Register", jPanelRegister);

        jButtonRemoveAccount.setText("Remove Account");
        jButtonRemoveAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveAccountActionPerformed(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel46.setText("Settings");

        jLabel55.setText("Username");

        javax.swing.GroupLayout jPanelSettingsLayout = new javax.swing.GroupLayout(jPanelSettings);
        jPanelSettings.setLayout(jPanelSettingsLayout);
        jPanelSettingsLayout.setHorizontalGroup(
            jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSettingsLayout.createSequentialGroup()
                        .addGap(462, 462, 462)
                        .addGroup(jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldRemoveUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRemoveAccount)
                            .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSettingsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(417, Short.MAX_VALUE))
        );
        jPanelSettingsLayout.setVerticalGroup(
            jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldRemoveUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonRemoveAccount)
                .addContainerGap(344, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("Settings", jPanelSettings);
        jTabbedPaneLoginRegister.remove(jPanelSettings);

        jLabel40.setText("Item ID");

        jButtonReturnRent.setText("Return");
        jButtonReturnRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReturnRentActionPerformed(evt);
            }
        });

        jLabel81.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel81.setText("Return Rented Item");

        jLabel85.setText("Username");

        jTextFieldReturnRentedID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldReturnRentedIDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelReturnRentLayout = new javax.swing.GroupLayout(jPanelReturnRent);
        jPanelReturnRent.setLayout(jPanelReturnRentLayout);
        jPanelReturnRentLayout.setHorizontalGroup(
            jPanelReturnRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReturnRentLayout.createSequentialGroup()
                .addGroup(jPanelReturnRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelReturnRentLayout.createSequentialGroup()
                        .addGap(462, 462, 462)
                        .addGroup(jPanelReturnRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldReturnRentedID, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelReturnRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextFieldReturnRentedUsername, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                            .addComponent(jButtonReturnRent, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelReturnRentLayout.createSequentialGroup()
                        .addGap(438, 438, 438)
                        .addComponent(jLabel81)))
                .addContainerGap(442, Short.MAX_VALUE))
        );
        jPanelReturnRentLayout.setVerticalGroup(
            jPanelReturnRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReturnRentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel85)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldReturnRentedUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel40)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldReturnRentedID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonReturnRent)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("Return Rented Item", jPanelReturnRent);

        jButtonReturnProduct.setText("Return");
        jButtonReturnProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReturnProductActionPerformed(evt);
            }
        });

        jLabel89.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel89.setText("Return Product");

        jLabel91.setText("Username");

        jLabel92.setText("Item ID");

        javax.swing.GroupLayout jPanelReturnProductLayout = new javax.swing.GroupLayout(jPanelReturnProduct);
        jPanelReturnProduct.setLayout(jPanelReturnProductLayout);
        jPanelReturnProductLayout.setHorizontalGroup(
            jPanelReturnProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReturnProductLayout.createSequentialGroup()
                .addGap(455, 455, 455)
                .addComponent(jLabel89)
                .addGap(0, 452, Short.MAX_VALUE))
            .addGroup(jPanelReturnProductLayout.createSequentialGroup()
                .addGroup(jPanelReturnProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelReturnProductLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextFieldReturnProductUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelReturnProductLayout.createSequentialGroup()
                        .addGap(462, 462, 462)
                        .addGroup(jPanelReturnProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelReturnProductLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanelReturnProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jTextFieldReturnProductItemID, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonReturnProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelReturnProductLayout.setVerticalGroup(
            jPanelReturnProductLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReturnProductLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel91)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldReturnProductUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel92)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldReturnProductItemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonReturnProduct)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("Return Product", jPanelReturnProduct);

        jLabel93.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel93.setText("List of Users");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        StringBuilder tempSb = new StringBuilder();
        tempSb.append(getSbUsers());
        String usersString = tempSb.toString();
        jTextArea1.setText(usersString);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanelListUsersLayout = new javax.swing.GroupLayout(jPanelListUsers);
        jPanelListUsers.setLayout(jPanelListUsersLayout);
        jPanelListUsersLayout.setHorizontalGroup(
            jPanelListUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListUsersLayout.createSequentialGroup()
                .addGap(449, 449, 449)
                .addComponent(jLabel93)
                .addContainerGap(476, Short.MAX_VALUE))
            .addGroup(jPanelListUsersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanelListUsersLayout.setVerticalGroup(
            jPanelListUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListUsersLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel93)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("List of Users", jPanelListUsers);
        jTabbedPaneLoginRegister.remove(jPanelListUsers);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        StringBuilder tempSb2 = new StringBuilder();
        tempSb2.append(getSbStocks());
        String stockString = tempSb2.toString();
        jTextArea2.setText(stockString);
        jScrollPane3.setViewportView(jTextArea2);

        jLabel94.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel94.setText("List of Stock");

        javax.swing.GroupLayout jPanelListStockLayout = new javax.swing.GroupLayout(jPanelListStock);
        jPanelListStock.setLayout(jPanelListStockLayout);
        jPanelListStockLayout.setHorizontalGroup(
            jPanelListStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListStockLayout.createSequentialGroup()
                .addGap(449, 449, 449)
                .addComponent(jLabel94)
                .addContainerGap(477, Short.MAX_VALUE))
            .addComponent(jScrollPane3)
        );
        jPanelListStockLayout.setVerticalGroup(
            jPanelListStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelListStockLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel94)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("List of Stock", jPanelListStock);
        jTabbedPaneLoginRegister.remove(jPanelListStock);

        jTextArea4.setEditable(false);
        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        StringBuilder tempSb3 = new StringBuilder();
        tempSb3.append(getSbOverdue());
        String overdueString = tempSb3.toString();
        jTextArea4.setText(overdueString);
        jScrollPane5.setViewportView(jTextArea4);

        jLabel95.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel95.setText("Overdue Rentals");

        javax.swing.GroupLayout jPanelOverdueLayout = new javax.swing.GroupLayout(jPanelOverdue);
        jPanelOverdue.setLayout(jPanelOverdueLayout);
        jPanelOverdueLayout.setHorizontalGroup(
            jPanelOverdueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOverdueLayout.createSequentialGroup()
                .addGap(434, 434, 434)
                .addComponent(jLabel95)
                .addContainerGap(465, Short.MAX_VALUE))
            .addGroup(jPanelOverdueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        jPanelOverdueLayout.setVerticalGroup(
            jPanelOverdueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOverdueLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel95)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("Overdue Rentals", jPanelOverdue);
        jTabbedPaneLoginRegister.remove(jPanelOverdue);

        jButtonLogout.setText("Logout");
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });

        jLabel96.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel96.setText("Logout Page");

        jLabel6.setText("Are you sure you want to logout?");

        javax.swing.GroupLayout jPanelLogoutLayout = new javax.swing.GroupLayout(jPanelLogout);
        jPanelLogout.setLayout(jPanelLogoutLayout);
        jPanelLogoutLayout.setHorizontalGroup(
            jPanelLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogoutLayout.createSequentialGroup()
                .addGroup(jPanelLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLogoutLayout.createSequentialGroup()
                        .addGap(463, 463, 463)
                        .addGroup(jPanelLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonLogout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelLogoutLayout.createSequentialGroup()
                        .addGap(418, 418, 418)
                        .addComponent(jLabel6)))
                .addContainerGap(407, Short.MAX_VALUE))
        );
        jPanelLogoutLayout.setVerticalGroup(
            jPanelLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogoutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(38, 38, 38)
                .addComponent(jButtonLogout)
                .addContainerGap(352, Short.MAX_VALUE))
        );

        jTabbedPaneLoginRegister.addTab("Logout", jPanelLogout);
        jTabbedPaneLoginRegister.remove(jPanelLogout);

        siteTabs.addTab("Login/Register", jTabbedPaneLoginRegister);

        sellPage.setMaximumSize(new java.awt.Dimension(3276, 3276));
        sellPage.setPreferredSize(new java.awt.Dimension(900, 900));

        jLabel11.setText("1 PRICE RANGE");

        sellPriceRangeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "0 - 100", "100 - 500", "500 - 1000", "Above 1000" }));
        sellPriceRangeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellPriceRangeComboBoxActionPerformed(evt);
            }
        });

        sellCompanyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellCompanyComboBoxActionPerformed(evt);
            }
        });

        sellItemComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellItemComboBoxActionPerformed(evt);
            }
        });

        addToCartBuyButton.setText("BUY");
        addToCartBuyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToCartBuyButtonActionPerformed(evt);
            }
        });

        jLabel12.setText("2 COMPANY");

        jLabel13.setText("3 ITEM NAME");

        jLabel16.setText("About Item:");

        jLabel18.setText("Sell Price € :");

        sellItemDescriptionBox.setColumns(20);
        sellItemDescriptionBox.setRows(5);
        jScrollPane7.setViewportView(sellItemDescriptionBox);

        jLabel2.setText("4 QUANTITY");

        javax.swing.GroupLayout sellPageLayout = new javax.swing.GroupLayout(sellPage);
        sellPage.setLayout(sellPageLayout);
        sellPageLayout.setHorizontalGroup(
            sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellPageLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sellPageLayout.createSequentialGroup()
                        .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sellPageLayout.createSequentialGroup()
                                .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(sellPageLayout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(422, 422, 422))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sellPageLayout.createSequentialGroup()
                                        .addComponent(jScrollPane7)
                                        .addGap(80, 80, 80))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sellPageLayout.createSequentialGroup()
                                        .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(quantity))
                                        .addGap(74, 74, 74)))
                                .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sellItemComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel18)
                                    .addComponent(sellPriceBox, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addToCartBuyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(sellCompanyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(59, Short.MAX_VALUE))
                    .addGroup(sellPageLayout.createSequentialGroup()
                        .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel11)
                            .addComponent(sellPriceRangeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        sellPageLayout.setVerticalGroup(
            sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sellPageLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(sellPriceRangeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sellCompanyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sellItemComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sellPriceBox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(sellPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(quantity)
                    .addComponent(addToCartBuyButton, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        siteTabs.addTab("Products", sellPage);

        rentPage.setMaximumSize(new java.awt.Dimension(3276, 3276));
        rentPage.setPreferredSize(new java.awt.Dimension(1000, 570));

        jLabel8.setText("1 PRICE RANGE");

        rentPriceComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "0 - 30", "30 - 70", "Above 70" }));
        rentPriceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rentPriceComboBoxActionPerformed(evt);
            }
        });

        rentCompanyComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rentCompanyComboBoxActionPerformed(evt);
            }
        });

        rentItemComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rentItemComboBoxActionPerformed(evt);
            }
        });

        addToCartRentButton.setText("RENT");
        addToCartRentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToCartRentButtonActionPerformed(evt);
            }
        });

        jLabel9.setText("2 COMPANY");

        jLabel10.setText("3 ITEM NAME");

        jLabel19.setText("About Item:");

        jLabel20.setText("Rent Price € :");

        itemDescriptionBox.setColumns(20);
        itemDescriptionBox.setRows(5);
        jScrollPane1.setViewportView(itemDescriptionBox);

        javax.swing.GroupLayout rentPageLayout = new javax.swing.GroupLayout(rentPage);
        rentPage.setLayout(rentPageLayout);
        rentPageLayout.setHorizontalGroup(
            rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rentPageLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rentPageLayout.createSequentialGroup()
                        .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(rentPageLayout.createSequentialGroup()
                                .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(rentPageLayout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(422, 422, 422))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rentPageLayout.createSequentialGroup()
                                        .addComponent(jScrollPane1)
                                        .addGap(80, 80, 80)))
                                .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rentItemComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel20)
                                    .addComponent(rentPriceBox, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(rentCompanyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(59, Short.MAX_VALUE))
                    .addGroup(rentPageLayout.createSequentialGroup()
                        .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel8)
                            .addComponent(rentPriceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(rentPageLayout.createSequentialGroup()
                .addGap(434, 434, 434)
                .addComponent(addToCartRentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        rentPageLayout.setVerticalGroup(
            rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rentPageLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(rentPriceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rentCompanyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rentItemComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(rentPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rentPriceBox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(addToCartRentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        siteTabs.addTab("Rentals", rentPage);

        shoppingCart.setLayout(new javax.swing.BoxLayout(shoppingCart, javax.swing.BoxLayout.LINE_AXIS));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("ITEMS TO BUY");

        buyItemButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        buyItemButton.setText("BUY ITEM(S)");
        buyItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyItemButtonActionPerformed(evt);
            }
        });

        cartBuyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NAME", "QUANTITY", "TOTAL PRICE"
            }
        ));
        jScrollPane4.setViewportView(cartBuyTable);

        javax.swing.GroupLayout cartBuyLayout = new javax.swing.GroupLayout(cartBuy);
        cartBuy.setLayout(cartBuyLayout);
        cartBuyLayout.setHorizontalGroup(
            cartBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cartBuyLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(cartBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(cartBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(buyItemButton)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        cartBuyLayout.setVerticalGroup(
            cartBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cartBuyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buyItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("ITEMS TO RENT");

        rentItemButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        rentItemButton.setText("RENT ITEM(S)");
        rentItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rentItemButtonActionPerformed(evt);
            }
        });

        cartRentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NAME", "ITEM CODE", "PRICE"
            }
        ));
        jScrollPane6.setViewportView(cartRentTable);

        javax.swing.GroupLayout cartRentLayout = new javax.swing.GroupLayout(cartRent);
        cartRent.setLayout(cartRentLayout);
        cartRentLayout.setHorizontalGroup(
            cartRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cartRentLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(cartRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rentItemButton)
                    .addGroup(cartRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel17)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 811, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        cartRentLayout.setVerticalGroup(
            cartRentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cartRentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rentItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cartContainerLayout = new javax.swing.GroupLayout(cartContainer);
        cartContainer.setLayout(cartContainerLayout);
        cartContainerLayout.setHorizontalGroup(
            cartContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cartContainerLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(cartContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cartBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cartRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        cartContainerLayout.setVerticalGroup(
            cartContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cartContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cartRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cartBuy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        shoppingCart.add(cartContainer);

        siteTabs.addTab("My Cart", shoppingCart);

        getContentPane().add(siteTabs, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonReturnRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReturnRentActionPerformed
        // TODO add your handling code here:
        // variables
        String returnRentUsername = jTextFieldReturnRentedUsername.getText();
        String returnRentID = jTextFieldReturnRentedID.getText();
        PreparedStatement pStatement;
        // initialise SQL query string
        String query = "DELETE FROM Rent WHERE EXISTS(SELECT clientID, itemID "
                        + "FROM Client C, ItemsToRent IR WHERE C.clientID = Rent.clientID "
                        + "AND C.username = ? AND Rent.itemID = IR.itemID AND IR.itemID = ?)";
        
        // use popup to tell user if they are sure of their action
        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to return your rented item?", "Return rental",JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE);
        // if user selects yes
        if(result == JOptionPane.YES_OPTION)
        {
            // if username field is empty, tell user to insert it
            if(returnRentUsername.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Add A Username");
            }
            // if return field is empty, tell user to insert it
            else if(returnRentID.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Add an ItemID");
            }
            else{
                try {
                    //Creating JDBC prepare Statement
                    pStatement = PhotoServiceApp1.getConnection().prepareStatement(query);
                    // set parameters
                    pStatement.setString(1, returnRentUsername);
                    pStatement.setString(2, returnRentID);
                    // execute SQL query & check if the database has updated, inform user of success or failure
                    if(pStatement.executeUpdate() > 0)
                    {
                        JOptionPane.showMessageDialog(null, "Rented item returned!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Rented item does not exist!");
                    }

                } catch (SQLException sqlex) {
                    System.err.println(sqlex.getMessage());
                }
            }
        }
    }//GEN-LAST:event_jButtonReturnRentActionPerformed

    private void jTextFieldReturnRentedIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldReturnRentedIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldReturnRentedIDActionPerformed

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        // TODO add your handling code here:
        //(Reference: https://1bestcsharp.blogspot.com/2018/05/java-login-and-register-form-with-mysql-database.html )
        // variables
        String loginUsername = jTextFieldLoginUsername.getText();
        String loginPassword = String.valueOf(jPasswordFieldLogin.getPassword());
        ResultSet rs;
        PreparedStatement pStatement;
        // initialise SQL query string
        String query = "SELECT * FROM Client WHERE username = ? AND password = ?"; 
        String query2 = "SELECT * FROM Admin WHERE username = ? AND password = ?"; 
        
        try {
            //Creating JDBC prepare Statement
            pStatement = PhotoServiceApp1.getConnection().prepareStatement(query);
            // set parameters
            pStatement.setString(1, loginUsername);
            pStatement.setString(2, loginPassword);
            // execute SQL query
            rs = pStatement.executeQuery();
            
            // if username field is empty, tell user to insert it
            if(loginUsername.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Add a Username");
            }
            // if password field is empty, tell user to insert it
            else if(loginPassword.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Add a password");
            }
            else
            {
                // check if there are data from the Client table
                if(rs.next()) {
                    JOptionPane.showMessageDialog(null, "User Login successful");  
                    jTabbedPaneLoginRegister.remove(jPanelLogin);
                    jTabbedPaneLoginRegister.remove(jPanelRegister);
                    jTabbedPaneLoginRegister.addTab("Logout", jPanelLogout);
                    siteTabs.addTab("My Cart", shoppingCart);
                    userLoggedIn = true;
                }          
                else {
                    //Creating JDBC prepare Statement
                    pStatement = PhotoServiceApp1.getConnection().prepareStatement(query2);
                    // set parameters
                    pStatement.setString(1, loginUsername);
                    pStatement.setString(2, loginPassword);
                    // execute SQL query
                    rs = pStatement.executeQuery();
                    // check if there are data from the Admin table
                    if(rs.next()) {
                        JOptionPane.showMessageDialog(null, "Admin login successful");
                        jTabbedPaneLoginRegister.addTab("Settings", jPanelSettings);
                        jTabbedPaneLoginRegister.addTab("List of Users", jPanelListUsers);
                        jTabbedPaneLoginRegister.addTab("List of Stock", jPanelListStock);
                        jTabbedPaneLoginRegister.addTab("Overdue Rentals", jPanelOverdue);
                        jTabbedPaneLoginRegister.addTab("Logout", jPanelLogout);
                        siteTabs.addTab("My Cart", shoppingCart);
                        jTabbedPaneLoginRegister.remove(jPanelLogin);
                        jTabbedPaneLoginRegister.remove(jPanelRegister); 
                        jTabbedPaneLoginRegister.remove(jPanelReturnRent);
                        jTabbedPaneLoginRegister.remove(jPanelReturnProduct);
                        userLoggedIn = true;
                    } 
                    // inform user that the inputed data has not been found
                    else {
                        JOptionPane.showMessageDialog(null, "Incorrect username or password");
                    }
                }
            }
        } catch (SQLException sqlex) {
             System.err.println(sqlex.getMessage());
        }
    }//GEN-LAST:event_jButtonLoginActionPerformed

    private void jButtonRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegisterActionPerformed
        // TODO add your handling code here:
        //(Reference: https://1bestcsharp.blogspot.com/2018/05/java-login-and-register-form-with-mysql-database.html )
        // variables
        String registerUsername = jTextFieldUserRegister.getText();
        String registerPassword = String.valueOf(jPasswordFieldRegister1.getPassword());
        String registerRe_Password = String.valueOf(jPasswordFieldRegister2.getPassword());
        PreparedStatement pStatement;
        // initialise SQL query string
        String query = "INSERT INTO Client(username, password) VALUES(?, ?)";
        // check if inputted username is empty, inform user        
        if(registerUsername.equals(""))
        {
            JOptionPane.showMessageDialog(null, "Add A Username");
        }
        // check if inputted password is empty, inform user 
        else if(registerPassword.equals(""))
        {
            JOptionPane.showMessageDialog(null, "Add A Password");
        }
        // check if inputted password is not the same as the other, inform user 
        else if(!registerPassword.equals(registerRe_Password))
        {
            JOptionPane.showMessageDialog(null, "Retype The Password Again");
        }
        // check if inputted username already exists, inform user 
        else if(checkUsername(registerUsername) == true)
        {
            JOptionPane.showMessageDialog(null, "This Username Already Exist");
        }
        
        else{
            try {
                //Creating JDBC prepare Statement
                pStatement = PhotoServiceApp1.getConnection().prepareStatement(query);
                // set parameters
                pStatement.setString(1, registerUsername);
                pStatement.setString(2, registerPassword);
                // execute SQL query & check if the database has updated, inform user of success
                if(pStatement.executeUpdate() > 0)
                {
                    JOptionPane.showMessageDialog(null, "New User Added");
                }

            } catch (SQLException sqlex) {
                System.err.println(sqlex.getMessage());
            }
        }            
    }//GEN-LAST:event_jButtonRegisterActionPerformed

    private void jButtonRemoveAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveAccountActionPerformed
        // TODO add your handling code here:
        // variables
        String removeUserName = jTextFieldRemoveUsername.getText();
        PreparedStatement pStatement;
        // initialise SQL query string
        String query = "DELETE FROM Client WHERE username = ?";
        // use popup to tell user if they are sure of their action
        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this user?", "Delete User",JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE);
        // if user selects yes
        if(result == JOptionPane.YES_OPTION)
        {
            // check if inputted username is empty, inform user 
            if(removeUserName.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Add A Username");
            }
            else{
                try {
                    //Creating JDBC prepare Statement
                    pStatement = PhotoServiceApp1.getConnection().prepareStatement(query);
                    // set parameters
                    pStatement.setString(1, removeUserName);
                    // execute SQL query & check if the database has updated, inform user of success or failure
                    if(pStatement.executeUpdate() > 0)
                    {
                        JOptionPane.showMessageDialog(null, "User Deleted!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "User does not exist!");
                    }

                } catch (SQLException sqlex) {
                    System.err.println(sqlex.getMessage());
                }
            }
        }
    }//GEN-LAST:event_jButtonRemoveAccountActionPerformed

    private void jTextFieldLoginUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLoginUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldLoginUsernameActionPerformed

    private void jButtonReturnProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReturnProductActionPerformed
        // TODO add your handling code here:
        // variables
        String returnProducttUsername = jTextFieldReturnProductUsername.getText();
        String returnProductID = jTextFieldReturnProductItemID.getText();
        PreparedStatement pStatement;
        // initialise SQL query string
        String query = "DELETE FROM ItemsToSell WHERE EXISTS(SELECT itemID, clientID, stockName "
                        + "FROM Client C, StockDetails SD WHERE C.clientID = ItemsToSell.clientID "
                        + "AND C.username = ? AND ItemsToSell.itemID = ? "
                        + "AND ItemsToSell.stockName = SD.stockName)";
        // use popup to tell user if they are sure of their action
        int result = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to return your product?", "Return Product",JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE);
        // if user selects yes
        if(result == JOptionPane.YES_OPTION)
        {
            // check if inputted username is empty, inform user 
            if(returnProducttUsername.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Add A Username");
            }
            // check if inputted productID is empty, inform user 
            else if(returnProductID.equals(""))
            {
                JOptionPane.showMessageDialog(null, "Add an ItemID");
            }
            else{
                try {
                    //Creating JDBC prepare Statement
                    pStatement = PhotoServiceApp1.getConnection().prepareStatement(query);
                    // set parameters
                    pStatement.setString(1, returnProducttUsername);
                    pStatement.setString(2, returnProductID);
                    // execute SQL query & check if the database has updated, inform user of success or failure
                    if(pStatement.executeUpdate() > 0)
                    {
                        JOptionPane.showMessageDialog(null, "Product item returned!");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Product does not exist!");
                    }

                } catch (SQLException sqlex) {
                    System.err.println(sqlex.getMessage());
                }
            }
        }
    }//GEN-LAST:event_jButtonReturnProductActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        // TODO add your handling code here:
        // inform user that they have logged out of account
        JOptionPane.showMessageDialog(null, "Logged out of account");
        jTabbedPaneLoginRegister.remove(jPanelLogout);
        jTabbedPaneLoginRegister.remove(jPanelSettings);
        jTabbedPaneLoginRegister.remove(jPanelListUsers);
        jTabbedPaneLoginRegister.remove(jPanelListStock);
        jTabbedPaneLoginRegister.remove(jPanelOverdue);
        jTabbedPaneLoginRegister.remove(jPanelRegister);
        jTabbedPaneLoginRegister.remove(jPanelReturnRent);
        jTabbedPaneLoginRegister.remove(jPanelReturnProduct);
        siteTabs.remove(shoppingCart);
        
        jTabbedPaneLoginRegister.addTab("Login", jPanelLogin);
        jTabbedPaneLoginRegister.addTab("Register", jPanelRegister);
        jTabbedPaneLoginRegister.addTab("Return Rented Item", jPanelReturnRent);
        jTabbedPaneLoginRegister.addTab("Return Product", jPanelReturnProduct);
        userLoggedIn = false;
        
    }//GEN-LAST:event_jButtonLogoutActionPerformed

    private void rentPriceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rentPriceComboBoxActionPerformed

        // options: select, 0 - 30, 30 - 70, above 70
        int selectedItemIdx = rentPriceComboBox.getSelectedIndex();
        if(selectedItemIdx != 0){
            String condition = "WHERE " + rentPriceRanges[selectedItemIdx] + " GROUP BY brandName";
            Object[][] queryResult = DataHandler.getRows("ItemsToRent", "brandName", condition);
            rentCompanyComboBox.setModel(new DefaultComboBoxModel(DataHandler.convertDoubleArrayToSingle(queryResult)));
        }
    }//GEN-LAST:event_rentPriceComboBoxActionPerformed

    private void rentCompanyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rentCompanyComboBoxActionPerformed
        // TODO add your handling code here:
        int selectedPriceRangeIdx = rentPriceComboBox.getSelectedIndex();
        if(selectedPriceRangeIdx != 0){
            String condition = "WHERE brandName = " + '"' + (String) rentCompanyComboBox.getSelectedItem() + '"' + " AND " + rentPriceRanges[selectedPriceRangeIdx];
            Object[][] queryResult = DataHandler.getRows("ItemsToRent", "itemName", condition);
            rentItemComboBox.setModel(new DefaultComboBoxModel(DataHandler.convertDoubleArrayToSingle(queryResult)));
        }
    }//GEN-LAST:event_rentCompanyComboBoxActionPerformed

    private void rentItemComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rentItemComboBoxActionPerformed
        // TODO add your handling code here:
        String condition = "WHERE itemName = " + '"' + (String) rentItemComboBox.getSelectedItem() + '"';
        Object[][] queryResult = DataHandler.getRows("ItemsToRent", "description, rentPrice", condition);
        itemDescriptionBox.setText((String)queryResult[0][0]);
        itemDescriptionBox.setEditable(false);
        itemDescriptionBox.setLineWrap(true);
        rentPriceBox.setText(queryResult[0][1].toString());

    }//GEN-LAST:event_rentItemComboBoxActionPerformed

    private void addToCartRentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToCartRentButtonActionPerformed
        // TODO add your handling code here:
        if(userLoggedIn) {
            if(rentItemComboBox.getSelectedIndex() >= 0){
                Object[][] itemID = DataHandler.getRows("ItemsToRent", "itemID", "WHERE itemName = " + '"' + (String) rentItemComboBox.getSelectedItem() + '"');
                DefaultTableModel model = (DefaultTableModel) cartRentTable.getModel();
                model.addRow(new Object[]{rentItemComboBox.getSelectedItem(), itemID[0][0], rentPriceBox.getText()});
                JOptionPane.showMessageDialog(this, "Added to Cart", "RENT", JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Please Log In to Rent", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_addToCartRentButtonActionPerformed

    private void sellPriceRangeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellPriceRangeComboBoxActionPerformed
        // TODO add your handling code here:
        // options: Select, 0 - 100, 100 - 500, 500 - 1000, Above 1000
        int selectedItemIdx = sellPriceRangeComboBox.getSelectedIndex();
        if(selectedItemIdx != 0){
            String condition = "WHERE " + sellPriceRanges[selectedItemIdx] + " GROUP BY brandName";
            Object[][] queryResult = DataHandler.getRows("StockDetails", "brandName", condition);
            sellCompanyComboBox.setModel(new DefaultComboBoxModel(DataHandler.convertDoubleArrayToSingle(queryResult)));
        }
    }//GEN-LAST:event_sellPriceRangeComboBoxActionPerformed

    private void sellCompanyComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellCompanyComboBoxActionPerformed
        // TODO add your handling code here:
        int selectedPriceRangeIdx = sellPriceRangeComboBox.getSelectedIndex();
        if(selectedPriceRangeIdx != 0){
            String condition = "WHERE brandName = " + '"' + (String) sellCompanyComboBox.getSelectedItem() + '"' + " AND " + sellPriceRanges[selectedPriceRangeIdx];
            Object[][] queryResult = DataHandler.getRows("StockDetails", "stockName", condition);
            sellItemComboBox.setModel(new DefaultComboBoxModel(DataHandler.convertDoubleArrayToSingle(queryResult)));
        }
    }//GEN-LAST:event_sellCompanyComboBoxActionPerformed

    private void sellItemComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellItemComboBoxActionPerformed
        // TODO add your handling code here:
        String condition = "WHERE stockName = " + '"' + (String) sellItemComboBox.getSelectedItem() + '"';
        Object[][] queryResult = DataHandler.getRows("StockDetails", "description, sellPrice", condition);
        sellItemDescriptionBox.setText((String)queryResult[0][0]);
        sellItemDescriptionBox.setEditable(false);
        sellItemDescriptionBox.setLineWrap(true);
        sellPriceBox.setText(queryResult[0][1].toString());
    }//GEN-LAST:event_sellItemComboBoxActionPerformed

    private void addToCartBuyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToCartBuyButtonActionPerformed
        // TODO add your handling code here:
        if(userLoggedIn){
            if(sellItemComboBox.getSelectedIndex() >= 0){
                if(quantity.getText().matches("[a-zA-Z]*")){
                    JOptionPane.showMessageDialog(this, "Please Insert Quantity", "BUY", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    DefaultTableModel model = (DefaultTableModel) cartBuyTable.getModel();
                    int totalPrice = Integer.parseInt(quantity.getText()) * Integer.parseInt(sellPriceBox.getText());
                    model.addRow(new Object[]{sellItemComboBox.getSelectedItem(), quantity.getText(), Integer.toString(totalPrice)});
                    JOptionPane.showMessageDialog(this, "Added to Cart", "BUY", JOptionPane.INFORMATION_MESSAGE);
                }
            }    
        }else{
            JOptionPane.showMessageDialog(this, "Please Log In to Buy", "", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_addToCartBuyButtonActionPerformed

    private void buyItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyItemButtonActionPerformed
        // TODO add your handling code here:
        if(cartBuyTable.getRowCount() == 0){
            JOptionPane.showMessageDialog(this, "Please Specify Products to Buy", "", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object[][] clientID = DataHandler.getRows("Client", "clientID", "WHERE username = " + '"' + jTextFieldLoginUsername.getText() + '"' + " AND password = " + '"' + new String(jPasswordFieldLogin.getPassword()) + '"');
        System.out.println("clientid !!!! " + clientID[0][0]);
        Object[] itemNames = new Object[cartBuyTable.getRowCount()];
        for(int r = 0; r < cartBuyTable.getRowCount(); r++){
            itemNames[r] = cartBuyTable.getValueAt(r, 0);
            System.out.println("ITEM ID!!!!" + itemNames[r]);
        }

        int successfulInsert = 1;
        for(int r = 0; r < cartBuyTable.getRowCount(); r++){
            successfulInsert = DataHandler.insertData("ItemsToSell", "stockName, clientID", '"' + (String) itemNames[r] + '"' + ", " + clientID[0][0]);
            if(successfulInsert == -1){
                break;
            }
        }
        if (successfulInsert != -1){
            JOptionPane.showMessageDialog(this, "Your Buy Request was Successful", "", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this, "Please Try Again", "", JOptionPane.ERROR_MESSAGE);
        }
       
    }//GEN-LAST:event_buyItemButtonActionPerformed

    private void rentItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rentItemButtonActionPerformed

        if(cartRentTable.getRowCount() == 0){
            JOptionPane.showMessageDialog(this, "Please Specify Products to Rent", "", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.now(); // Gets the current date
        Object[][] clientID = DataHandler.getRows("Client", "clientID", "WHERE username = " + '"' + jTextFieldLoginUsername.getText() + '"' + " AND password = " + '"' + new String(jPasswordFieldLogin.getPassword()) + '"');
        System.out.println("clientid !!!! " + clientID[0][0]);
        Object[] itemIDs = new Object[cartRentTable.getRowCount()];
        for(int r = 0; r < cartRentTable.getRowCount(); r++){
            itemIDs[r] = cartRentTable.getValueAt(r, 1);
            System.out.println("ITEM ID!!!!" + itemIDs[r].toString());
        }
        
        int successfulInsert = 1;
        for(int r = 0; r < cartRentTable.getRowCount(); r++){
            successfulInsert = DataHandler.insertData("Rent", "clientID, itemID, rentedDate", clientID[0][0] + ", " + itemIDs[r] + ", " + '"' + date.format(formatter) + '"');
            if(successfulInsert == -1){
                break;
            }
        }
        if (successfulInsert != -1){
            JOptionPane.showMessageDialog(this, "Your Rent Request was Successful", "", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(this, "Please Try Again", "", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_rentItemButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PhotoServiceApp1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PhotoServiceApp1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PhotoServiceApp1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PhotoServiceApp1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PhotoServiceApp1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addToCartBuyButton;
    private javax.swing.JButton addToCartRentButton;
    private javax.swing.JButton buyItemButton;
    private javax.swing.JPanel cartBuy;
    private javax.swing.JTable cartBuyTable;
    private javax.swing.JPanel cartContainer;
    private javax.swing.JPanel cartRent;
    private javax.swing.JTable cartRentTable;
    private javax.swing.JTextArea itemDescriptionBox;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonRegister;
    private javax.swing.JButton jButtonRemoveAccount;
    private javax.swing.JButton jButtonReturnProduct;
    private javax.swing.JButton jButtonReturnRent;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelListStock;
    private javax.swing.JPanel jPanelListUsers;
    private javax.swing.JPanel jPanelLogin;
    private javax.swing.JPanel jPanelLogout;
    private javax.swing.JPanel jPanelOverdue;
    private javax.swing.JPanel jPanelRegister;
    private javax.swing.JPanel jPanelReturnProduct;
    private javax.swing.JPanel jPanelReturnRent;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JPasswordField jPasswordField4;
    private javax.swing.JPasswordField jPasswordFieldLogin;
    private javax.swing.JPasswordField jPasswordFieldRegister1;
    private javax.swing.JPasswordField jPasswordFieldRegister2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JPopupMenu jPopupMenu4;
    private javax.swing.JPopupMenu jPopupMenu5;
    private javax.swing.JPopupMenu jPopupMenu6;
    private javax.swing.JPopupMenu jPopupMenu7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPaneLoginRegister;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextFieldLoginUsername;
    private javax.swing.JTextField jTextFieldRemoveUsername;
    private javax.swing.JTextField jTextFieldReturnProductItemID;
    private javax.swing.JTextField jTextFieldReturnProductUsername;
    private javax.swing.JTextField jTextFieldReturnRentedID;
    private javax.swing.JTextField jTextFieldReturnRentedUsername;
    private javax.swing.JTextField jTextFieldUserRegister;
    private java.awt.PopupMenu popupMenu1;
    private java.awt.PopupMenu popupMenu2;
    private java.awt.PopupMenu popupMenu3;
    private javax.swing.JTextField quantity;
    private javax.swing.JComboBox<String> rentCompanyComboBox;
    private javax.swing.JButton rentItemButton;
    private javax.swing.JComboBox<String> rentItemComboBox;
    private javax.swing.JPanel rentPage;
    private javax.swing.JTextField rentPriceBox;
    private javax.swing.JComboBox<String> rentPriceComboBox;
    private javax.swing.JComboBox<String> sellCompanyComboBox;
    private javax.swing.JComboBox<String> sellItemComboBox;
    private javax.swing.JTextArea sellItemDescriptionBox;
    private javax.swing.JPanel sellPage;
    private javax.swing.JTextField sellPriceBox;
    private javax.swing.JComboBox<String> sellPriceRangeComboBox;
    private javax.swing.JPanel shoppingCart;
    private javax.swing.JTabbedPane siteTabs;
    // End of variables declaration//GEN-END:variables
    private StringBuilder sbUsers;
    private StringBuilder sbStocks;
    private StringBuilder sbOverdue;
}