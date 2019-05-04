import java.awt.*;

public class ScreenHandler {
    /* Class */
    private static ScreenHandler screenHandler;
    /* Get screen dimensions */
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Rectangle taskSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    private int taskBarHeight = screenSize.height - taskSize.height;

    /* Constructor */
    private ScreenHandler() {
    }

    /* Singleton */
    public static ScreenHandler getScreenHandler(){
        if (screenHandler==null){
            screenHandler = new ScreenHandler();
        }
        return screenHandler;
    }

    /* Get values */
    public Dimension getScreenSize() {
        return screenSize;
    }

    public int getTaskBarHeight() {
        return taskBarHeight;
    }
}
