import javax.swing.*;

public class StockList extends JFrame {
    /* Classes */
    private static StockList stockList;
    /* Swing */
    private JPanel stockPanel;
    private JTextField stockQuantity;
    private JButton buyButton;
    private JList availableStock;

    private DefaultListModel stockEntity = new DefaultListModel();

    /* Constructor */
    private StockList() {
        ScreenHandler s = ScreenHandler.getScreenHandler();
        this.setContentPane(stockPanel);
        this.setTitle("ACMETrader: Stock Viewer");
        this.availableStock.setModel(stockEntity);
        this.setSize(s.getScreenSize().width/2,(s.getScreenSize().height-s.getTaskBarHeight()));
        this.setLocation(User.getUser().getWidth(),0);
    }

    /* Singleton */
    public static StockList getStockList(){
        if (stockList==null){
            stockList=new StockList();
        }
        return stockList;
    }

    /* Getters & setters */
    public Integer getStockQuantity() {
        return Integer.parseInt(stockQuantity.getText().toString());
    }

    public JButton getBuyButton() {
        return buyButton;
    }

    public DefaultListModel getStockEntity() {
        return stockEntity;
    }

    public JList getAvailableStock() {
        return availableStock;
    }
}
