import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jasypt.util.text.BasicTextEncryptor;

public class Starter extends Application {
    public static void main(String[] args) {

        /*String password = "Saman@123";
        String key = "#0w3bHa";
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);
        String encrypt = basicTextEncryptor.encrypt(password);
        String decrypt = basicTextEncryptor.decrypt(encrypt);*/

        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/LoginFormView.fxml"))));
        stage.show();
    }
}
