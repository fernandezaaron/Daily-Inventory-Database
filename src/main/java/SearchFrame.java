import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SearchFrame extends JFrame {

    JTable Receipt;
    DefaultTableModel dtm;
    JLabel bg;
    JScrollPane searchscroll;

    public SearchFrame() throws IOException {
        super("Search");
        setLayout(new GridLayout());
        setSize(300,500);
        setVisible(true);

        BufferedImage a = ImageIO.read(new File("./Interface/bg2.png"));
        bg = new JLabel(new ImageIcon(a));


        dtm = new DefaultTableModel(new Object[]{"Product", "Price"},1);
        Receipt = new JTable(dtm);

        Receipt.setSize(300,500);
        Receipt.setBackground(Color.WHITE);
        Receipt.setOpaque(false);
        Receipt.setVisible(true);
        searchscroll = new JScrollPane(Receipt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        searchscroll.setSize(300,500);
        searchscroll.setVisible(true);

        //getContentPane().add(bg);
        add(searchscroll);


    }
}
