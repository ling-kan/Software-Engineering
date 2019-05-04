import javax.swing.*;
import java.awt.event.*;

public class ConfirmWindow extends JDialog {
    private JPanel contentPane;
    private JButton buyButton;
    private JButton cancelButton;

    public ConfirmWindow() {
        this.setContentPane(contentPane);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.pack();
        this.setTitle("Confirm action");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public JButton getBuyButton() {
        return buyButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

}
