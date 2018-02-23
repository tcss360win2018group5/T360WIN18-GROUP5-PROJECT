package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class CalenderAppController implements Initializable {

    public CalenderAppController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void exitClick() {
        System.exit(0);
    }
}
