package it.polito.olehera.controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import it.polito.olehera.model.Calciatore;
import it.polito.olehera.model.Model;
import it.polito.olehera.model.Rosa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AnalizzaController {
	
	private Model model;
	private Stage stage;
	
	public void setModel(Model model, Stage stage) {
		this.model = model;
		this.stage = stage;
		setup();
	}

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lblNum;

    @FXML
    private Label lblEta;

    @FXML
    private Label lblValore;

    @FXML
    private BarChart<?, ?> grafico;

    @FXML
    private Label lblSquadra;

    @FXML
    private TextArea txtCalciatori;

    @FXML
    private Button btnCambia;

    @FXML
    private Button btnOttimizza;
    
    private void setup() {
    	Rosa scelta = model.getSquadraAnalizza();
    	
    	lblSquadra.setText(scelta.getNome());
    	
    	lblEta.setText(scelta.etaMedia()+" anni");
    	
    	lblNum.setText(""+scelta.numCalciatori());
    	
    	NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
    	lblValore.setText(nf.format(scelta.valoreTot())+" €");
    	
    	for (Calciatore c : scelta.getCalciatori())
    		txtCalciatori.appendText("\n"+c.toString());
    	
    	txtCalciatori.appendText("\nFisico: "+scelta.mediaFisico());
    	txtCalciatori.appendText("\nTecnica: "+scelta.mediaTecnica());
    	txtCalciatori.appendText("\nPotenziale: "+scelta.potenzialeTot());
    }

    @FXML
    void doCambiaSquadra(ActionEvent event) {
    	
    	try {
    		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root);
			
			HomeController controller = loader.getController();
			Model m = new Model();
 			controller.setModel(m, stage);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setTitle("Football Analytics PoliTo");
			stage.setScene(scene);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void doOttimizza(ActionEvent event) {
    	
    	try {
    		
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("Ottimizza.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root);
			
//			OttimizzaController controller = loader.getController();
			Stage s = new Stage();
// 			controller.setModel(model, s);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			s.setTitle("Football Analytics PoliTo");
			s.setScene(scene);
			s.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }

    @FXML
    void initialize() {
        assert lblNum != null : "fx:id=\"lblNum\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert lblEta != null : "fx:id=\"lblEta\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert lblValore != null : "fx:id=\"lblValore\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert grafico != null : "fx:id=\"grafico\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert lblSquadra != null : "fx:id=\"lblSquadra\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert txtCalciatori != null : "fx:id=\"txtCalciatori\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert btnCambia != null : "fx:id=\"btnCambia\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert btnOttimizza != null : "fx:id=\"btnOttimizza\" was not injected: check your FXML file 'Analizza.fxml'.";
    }
    
}