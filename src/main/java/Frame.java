import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Frame extends JFrame {

    JPanel main,container, ProductPanel, InventoryPanel;
    JButton Product, Inventory, AddProduct, GetSum, SearchButton, DeleteButton, EditButton;
    JTextField ProductName, Price, Date, Quantity, Search;
    JTable ProductTable, AllProductTable;
    JScrollPane ProductScroll, InventoryScroll;
    CardLayout card = new CardLayout();

    JLabel DateLabel, ProductLabel, PriceLabel, QuantityLabel;
    DefaultTableModel dtm, dtm2;
    JLabel bg;


    public Frame() throws IOException{
        super("Inventory");
        //setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(ImageIO.read(new File("./Interface/jedisaurbot.png")));

        BufferedImage a = ImageIO.read(new File("./Interface/bg.png"));
        bg = new JLabel(new ImageIcon(a));




        Product = new JButton(new AbstractAction("Product") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == Product) {
                    card.show(container, "Prod");
                    int rowcount = AllProductTable.getRowCount();
                    for(int i=rowcount-1; i>=0; i--){
                        dtm2.removeRow(i);
                    }

                }
            }
        });

        Inventory = new JButton(new AbstractAction("Inventory") {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(container, "Inventory");
                try{
                    Database db = Database.getInstance();
                    ResultSet rs = db.getResult("SELECT * FROM prods");
                    while(rs.next()){
                        String id = rs.getString("ID");
                        String d = rs.getString("Date");
                        String p = rs.getString("Product");
                        String pr = rs.getString("Price");
                        String qty = rs.getString("Quantity");
                        String fp = rs.getString("BasePrice");
                        String[] data = new String[]{id,d,p,qty,pr,fp};


                        for(int i=0; i<dtm2.getRowCount(); i++){
                            Object value = dtm2.getValueAt(i,0);
                            if(value.equals(id)){
                                dtm2.removeRow(i);
                            }
                        }
                        dtm2.addRow(data);

                    }
                    ResultSet res = db.getResult("SELECT SUM(Price) From prods");
                    String Total = "Total";
                    if(res.next()){
                        for(int i=0; i<dtm2.getRowCount(); i++){
                            Object value = dtm2.getValueAt(i,3);
                            if(value.equals(Total)){
                                dtm2.removeRow(i);
                            }
                        }
                        dtm2.addRow(new Object[]{"","","",Total,res.getString("SUM(Price)")});

                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }


            }
        });

        AddProduct = new JButton(new AbstractAction("Add Product") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == AddProduct) {
                    try {
                        Database db = Database.getInstance();


                        String date = Date.getText();
                        String prod = ProductName.getText();
                        double price = Double.parseDouble(String.valueOf(Price.getText()));
                        double quant = Double.parseDouble(String.valueOf(Quantity.getText()));
                        double finalprice = price*quant;

                        if (date.isBlank() && prod.isBlank() && String.valueOf(price).isBlank() && String.valueOf(quant).isBlank()) {
                            JOptionPane.showMessageDialog(null, "Empty input!!", "Error!!", JOptionPane.WARNING_MESSAGE);
                        } else {
                            Random rand = new Random();

                            ArrayList hs = new ArrayList<Integer>(1);
                            while (hs.size()<1){
                                while(hs.add(rand.nextInt(999999999)) != true);

                            }
                            assert hs.size()==100000000;

                            System.out.println(hs);
                            ResultSet rs = db.getResult("SELECT * FROM prods WHERE Date = '" + date + "'");
                            db.append_db(String.valueOf(hs),date, prod, String.valueOf(finalprice), String.valueOf(quant), String.valueOf(price));
                            dtm.addRow(new Object[]{hs,date, prod, quant, finalprice,price});
//                            ResultSet res = db.getResult("SELECT SUM(Price) FROM prods");
//                            dtm.addRow(new Object[]{"Total", res.getString("SUM(Price)")});
                            System.out.println("Successfully added to prods");
                        }
                        Date.setText("");
                        ProductName.setText("");
                        Price.setText("");
                        Quantity.setText("");

                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        GetSum = new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == GetSum){
                    try{
                        Database db = Database.getInstance();
                        int row = ProductTable.getSelectedRow();
                        Object getrow = dtm.getValueAt(row, 0);
                        ResultSet rs = db.getResult("SELECT * FROM prods WHERE ID='"+getrow+"'");
                        while (rs.next()){
                            ResultSet res = db.setResult("DELETE FROM prods WHERE ID='"+getrow+"'");
                            dtm.removeRow(row);
                        }




                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }

            }
        });

        DeleteButton = new JButton(new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == DeleteButton){
                    try{
                        Database db = Database.getInstance();
                        int row = AllProductTable.getSelectedRow();
                        Object getrow = dtm2.getValueAt(row, 0);
                        ResultSet rs = db.getResult("SELECT * FROM prods WHERE ID='"+getrow+"'");
                        while (rs.next()){
                            ResultSet res = db.setResult("DELETE FROM prods WHERE ID='"+getrow+"'");
                            dtm2.removeRow(row);
                        }




                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

            }
        });

        EditButton = new JButton(new AbstractAction("Edit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == EditButton){
                    try{
                        Database db = Database.getInstance();
                        int row = AllProductTable.getSelectedRow();
                        int col = AllProductTable.getSelectedColumn();
                        Object getrowcol = dtm2.getValueAt(row,col);
                        Object getrow = dtm2.getValueAt(row, 0);
                        if(col == 1){
                            ResultSet res = db.setResult("UPDATE prods SET Date ='"+getrowcol+"' WHERE ID='"+getrow+"'");
                            System.out.println("Updated Date");
                        }
                        if(col == 2){
                            ResultSet res = db.setResult("UPDATE prods SET Product ='"+getrowcol+"' WHERE ID ='"+getrow+"'");
                            System.out.println("Updated Product Name");
                        }
                        if(col == 3){
                            ResultSet res = db.setResult("UPDATE prods SET Price ='"+getrowcol+"' WHERE ID='"+getrow+"'");
                        }
                        if(col == 4){
                            ResultSet res = db.setResult("UPDATE prods SET Quantity = '"+getrowcol+"' WHERE ID='"+getrow+"'");
                        }
//                        ResultSet rs = db.getResult("SELECT * FROM prods WHERE ID='"+getrowcol+"'");
//                        if(rs.next()){
//                            ResultSet res = db.setResult("UPDATE prods SET "+String.valueOf(getrowcol)+ " WHERE ID='"+getrowcol+"'");
//                        }

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        SearchButton = new JButton(new AbstractAction("Search") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == SearchButton){
                    try {
                        String search = Search.getText();
                        Database db = Database.getInstance();
                        ResultSet rs;
                        rs = db.getResult("SELECT * FROM prods WHERE Date ='" + search + "'OR Product = '"+search+"'");


                        SearchFrame sf = new SearchFrame();
                        while (rs.next()) {
                            sf.dtm.addRow(new Object[]{rs.getString("Product"), rs.getString("Price")});
                        }
                        ResultSet res;
                        res = db.getResult("SELECT SUM(Price) FROM prods WHERE Date = '"+search+"' OR Product = '"+search+"'");
                        if(res.next()){
                            sf.dtm.addRow(new Object[]{"Total",res.getString("SUM(Price)")});
                        }
                        res.close();



                        Search.setText("");
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }


                }

            }
        });


        main = new JPanel();
        main.setLayout(null);
        main.setSize(1280,720);
        main.setBackground(Color.BLACK);
        main.setOpaque(false);


        container = new JPanel();
        container.setLayout(card);
        container.setBounds(280,70,1100,590);
        container.setBackground(Color.ORANGE);
        container.setOpaque(false);

        ProductPanel = new JPanel();
        ProductPanel.setLayout(null);
        ProductPanel.setBackground(Color.white);
        ProductPanel.setOpaque(false);

        InventoryPanel = new JPanel();
        InventoryPanel.setLayout(null);
        InventoryPanel.setBackground(Color.yellow);
        InventoryPanel.setOpaque(false);

        Search = new JTextField();
        Search.setLayout(null);
        Search.setBounds(0,250,218,48);
        //Search.setForeground(Color.white);
        Search.setBackground(Color.gray);
        //Search.setOpaque(false);


        Date = new JTextField();
        Date.setLayout(null);
        Date.setBounds(120,10,130,30);

        ProductName = new JTextField();
        ProductName.setLayout(null);
        ProductName.setBounds(120,50,130,30);

        Price =  new JTextField();
        Price.setLayout(null);
        Price.setBounds(120,90,130,30);

        Quantity = new JTextField();
        Quantity.setLayout(null);
        Quantity.setBounds(120,130,130,30);

        DateLabel = new JLabel("Date (dd/mm/yy): ");
        DateLabel.setBounds(10,10,100,30);
        DateLabel.setForeground(new Color(144,144,144));

        ProductLabel = new JLabel("Product Name: ");
        ProductLabel.setBounds(10,50,100,30);
        ProductLabel.setForeground(new Color(144,144,144));

        PriceLabel = new JLabel("Price: ");
        PriceLabel.setBounds(10,90,100,30);
        PriceLabel.setForeground(new Color(144,144,144));

        QuantityLabel = new JLabel("Quantity: ");
        QuantityLabel.setBounds(10,130,100,30);
        QuantityLabel.setForeground(new Color(144,144,144));



        dtm = new DefaultTableModel(new Object[]{"ID","Date","Product Name", "Quantity","Price","Base Price"},0);
        ProductTable = new JTable(dtm);
        ProductTable.setBounds(270,10,700,630);
        ProductTable.setBackground(Color.white);

        ProductScroll = new JScrollPane(ProductTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ProductScroll.setBounds(270,10,700,630);
        ProductScroll.setVisible(true);

        dtm2 = new DefaultTableModel(new Object[]{"ID","Date", "Product Name", "Quantity", "Price","Base Price"},0);

        AllProductTable = new JTable(dtm2);
        AllProductTable.setBounds(100,10,880,Integer.MAX_VALUE);
        AllProductTable.setBackground(Color.white);
        AllProductTable.setVisible(true);

        InventoryScroll = new JScrollPane(AllProductTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        InventoryScroll.setBounds(20,10,940,540);
        InventoryScroll.setVisible(true);



        Product.setBounds(0, 97, 218, 48);
        Product.setForeground(new Color(144,144,144));
        Product.setBackground(Color.BLACK);
        Product.setFont(new Font("Stencil",Font.PLAIN,23));
        Product.setHorizontalAlignment(SwingConstants.LEFT);
//        Product.setOpaque(false);
        Product.setFocusable(false);
        Product.setBorderPainted(false);

        Inventory.setBounds(0,173,218,48);
        Inventory.setForeground(new Color(144,144,144));
        Inventory.setBackground(Color.black);
        Inventory.setFont(new Font("Stencil",Font.PLAIN,23));
        Inventory.setHorizontalAlignment(SwingConstants.LEFT);
//        Inventory.setOpaque(false);
        Inventory.setFocusable(false);
        Inventory.setBorderPainted(false);

        AddProduct.setBounds(10,500,150,30);
        GetSum.setBounds(10,540,150,30);
        DeleteButton.setBounds(410,565,150,30);
        EditButton.setBounds(570,565,160,30);

        SearchButton.setBounds(45,316,120,35);
        SearchButton.setForeground(Color.white);
        SearchButton.setBackground(Color.black);
        SearchButton.setOpaque(false);
        SearchButton.setBorderPainted(false);
        //  SearchButton.setFocusable(false);




        main.add(container);
        container.add("Prod",ProductPanel);
        container.add("Inventory", InventoryPanel);

        ProductPanel.add(Quantity);
        ProductPanel.add(ProductScroll);
        ProductPanel.add(ProductName);
        ProductPanel.add(Price);
        ProductPanel.add(AddProduct);
        ProductPanel.add(GetSum);
        ProductPanel.add(Date);
        ProductPanel.add(DateLabel);
        ProductPanel.add(ProductLabel);
        ProductPanel.add(PriceLabel);
        ProductPanel.add(QuantityLabel);

        InventoryPanel.add(InventoryScroll);
        InventoryPanel.add(DeleteButton);
        InventoryPanel.add(EditButton);


        main.add(SearchButton);
        main.add(Search);
        main.add(Product);
        main.add(Inventory);
        main.add(bg);


        add(main);

        bg.setSize(1280,720);
        getContentPane().add(bg);


        setSize(1280, 720);
        setVisible(true);
        setResizable(false);

    }
}
