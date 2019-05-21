package it.polito.olehera.controller;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.olehera.model.Campionato;
import it.polito.olehera.model.Model;
import it.polito.olehera.model.Rosa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomeController {
	
	private Model model;
	private Stage stage;
	
	public void setModel(Model model, Stage stage) {
		this.model = model;
		this.stage = stage;
		cbxCampionato.getItems().addAll(model.getCampionati());
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Campionato> cbxCampionato;

    @FXML
    private ComboBox<Rosa> cbxClub;

    @FXML
    private Button btnAnalizza;

    @FXML
    void doAnalisi(ActionEvent event) {
    	model.setSquadraAnalizza(cbxClub.getValue());
    	
    	try {
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Analizza.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root);
			
			AnalizzaController controller = loader.getController();
 			controller.setModel(model, stage);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setTitle("Football Analytics PoliTo");
			stage.setScene(scene);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void doCaricaSquadre(ActionEvent event) {
    	Campionato scelto = cbxCampionato.getValue();
    	cbxClub.getItems().clear();
     	cbxClub.getItems().addAll(model.getSquadre(scelto));
    }

    @FXML
    void initialize() {
        assert cbxCampionato != null : "fx:id=\"cbxCampionato\" was not injected: check your FXML file 'Home.fxml'.";
        assert cbxClub != null : "fx:id=\"cbxClub\" was not injected: check your FXML file 'Home.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Home.fxml'.";
    }
    
}