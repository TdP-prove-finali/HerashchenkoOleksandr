package it.polito.olehera.controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import it.polito.olehera.model.Calciatore;
import it.polito.olehera.model.Model;
import it.polito.olehera.model.Rosa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class OttimizzaController {
	
	private Model model;
	
	public void setModel(Model model) {
		this.model = model;
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
    private BarChart<String, Double> grafico;

    @FXML
    private CategoryAxis asseX;

    @FXML
    private NumberAxis asseY;

    @FXML
    private TableView<Calciatore> tabella;

    @FXML
    private TableColumn<Calciatore, String> colNome;

    @FXML
    private TableColumn<Calciatore, Integer> colAnni;

    @FXML
    private TableColumn<Calciatore, String> colNaz;

    @FXML
    private TableColumn<Calciatore, String> colRuolo;

    @FXML
    private TableColumn<Calciatore, String> colVal;

    @FXML
    private Button btnCalcola;

    @FXML
    private Button btnCancella;

    @FXML
    private TextField txtBudget;

    @FXML
    private Label lblErr;

    @SuppressWarnings("unchecked")
	@FXML
    void doCalcolaRosaOttimizzata(ActionEvent event) {
    	
    	int budget = 0;
    	try {
    	      budget = Integer.parseInt(txtBudget.getText().trim());
    	} catch(NumberFormatException nfe) {
    	      lblErr.setText("Devi inserire un numero intero come Budget!");
    	      txtBudget.clear();
    	      return ;
    	}
    	
    	
    	
    	
    	lblErr.setText("");
    	
    	Rosa ottimizzata = model.calcolaRosaOttimizzata(budget);
    	
    	if (ottimizzata.getCalciatori().size() < 1) {
    		lblErr.setText("Non è possibile ottimizzare la Rosa con questi parametri");
    		return ;
    	}
    	
    	lblEta.setText(ottimizzata.etaMedia()+" anni");
    	
    	lblNum.setText(""+ottimizzata.numCalciatori());
    	
    	NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
    	lblValore.setText(nf.format(ottimizzata.valoreTot())+" €");
    	
    	ObservableList<Calciatore> values = FXCollections.observableArrayList();
    	for (Calciatore c : ottimizzata.getCalciatori())
    		values.add(c);
    	
    	tabella.setItems(values);
    	
    	XYChart.Series<String, Double> statistiche = new XYChart.Series<String, Double>();
    	
    	statistiche.getData().add(new XYChart.Data<String, Double>("Overall", ottimizzata.mediaOverall()));
    	statistiche.getData().add(new XYChart.Data<String, Double>("Potenziale", ottimizzata.mediaPotenziale()));
    	statistiche.getData().add(new XYChart.Data<String, Double>("Fisico", ottimizzata.mediaFisico()));
    	statistiche.getData().add(new XYChart.Data<String, Double>("Tecnica", ottimizzata.mediaTecnica()));
    	
    	grafico.getData().clear();
    	grafico.getData().addAll(statistiche);
    }

    @FXML
    void doCancella(ActionEvent event) {
    	txtBudget.clear();
    	lblErr.setText("");
    }

    @FXML
    void initialize() {
        assert lblNum != null : "fx:id=\"lblNum\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblEta != null : "fx:id=\"lblEta\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblValore != null : "fx:id=\"lblValore\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert grafico != null : "fx:id=\"grafico\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert asseX != null : "fx:id=\"asseX\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert asseY != null : "fx:id=\"asseY\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert tabella != null : "fx:id=\"tabella\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert colNome != null : "fx:id=\"colNome\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert colAnni != null : "fx:id=\"colAnni\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert colNaz != null : "fx:id=\"colNaz\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert colRuolo != null : "fx:id=\"colRuolo\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert colVal != null : "fx:id=\"colVal\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert btnCancella != null : "fx:id=\"btnCancella\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert txtBudget != null : "fx:id=\"txtBudget\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblErr != null : "fx:id=\"lblErr\" was not injected: check your FXML file 'Ottimizza.fxml'.";

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAnni.setCellValueFactory(new PropertyValueFactory<>("anni"));
        colNaz.setCellValueFactory(new PropertyValueFactory<>("nazionalità"));
        colRuolo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
        colVal.setCellValueFactory(new PropertyValueFactory<>("valore"));
    }
}