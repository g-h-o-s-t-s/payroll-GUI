import javafx.application.Application;
/**
 * Driver class, calls Client JavaFX class, Controller().
 * @author Sagnik Mukherjee, Michael Choe
 */
@SuppressWarnings("WeakerAccess")
public class Main
{
    /**
     * Main method to create instance of client class.
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        Application.launch(Controller.class, args);
    }
}