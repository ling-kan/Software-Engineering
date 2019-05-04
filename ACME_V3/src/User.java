import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class User extends JFrame {
    /* Classes */
    private static User user;
    /* Vars */
    private String userName;
    private Double userBalance;
    private Double userValue;
    private ArrayList<Position> positionArrayList = new ArrayList<>();
    /* Swing */
    private JPanel userWindow;
    private JButton closePositionButton;
    private JList userPositionList;
    private DefaultListModel userPositionEntity = new DefaultListModel();
    private JLabel accountBalanceLabel;
    private JLabel accountValueLabel;

    /* Constructor */
    private User() {
        this.setContentPane(userWindow);
        this.userPositionList.setModel(userPositionEntity);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) this.userPositionList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        userPositionList.setFont( new Font("Arial", Font.PLAIN, 12) );
        /* locate & size */
        ScreenHandler s = ScreenHandler.getScreenHandler();
        this.setLocation(0,0);
        this.setSize(s.getScreenSize().width/2,(s.getScreenSize().height-s.getTaskBarHeight()));
    }

    /* Singleton */
    public static User getUser(){
        if (user==null){
            user = new User();
        }
        return user;
    }

    /* Getters & setters */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public void setUserValue(Double userValue) {
        this.userValue = userValue;
    }

    @Override
    public JPanel getContentPane() {
        return userWindow;
    }

    public JButton getClosePositionButton() {
        return closePositionButton;
    }

    public JList getUserPositionList() {
        return userPositionList;
    }

    public DefaultListModel getUserPositionEntity() {
        return userPositionEntity;
    }

    public void setAccountBalanceLabel(String accountBalanceText) {
        this.accountBalanceLabel.setText(accountBalanceText);
    }

    public void setAccountValueLabel(String accountValueText) {
        this.accountValueLabel.setText(accountValueText);
    }

    public ArrayList<Position> getPositionArrayList() {
        return positionArrayList;
    }
}
