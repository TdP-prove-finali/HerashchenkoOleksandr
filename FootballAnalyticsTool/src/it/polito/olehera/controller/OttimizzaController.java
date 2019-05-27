package it.polito.olehera.controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import it.polito.olehera.model.Calciatore;
import it.polito.olehera.model.Model;
import it.polito.olehera.model.Rosa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class OttimizzaController {
	
	private Model model;
	private List<Calciatore> venduti;
	private TableViewSelectionModel<Calciatore> defaultSelectionModel;
	
	public void setModel(Model model) {
		this.model = model;
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
    private Label lblBudgetRimasto;

    @FXML
    private Label lblRimasti;

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
    private ImageView ball;
    
    @FXML
    private Slider sldTempo;

    @FXML
    private Slider sldQualita;
    
    @FXML
    private Label lblCalciatori;

    @FXML
    private Label lblAvv;
    
    private void setup() {
    	Rosa scelta = model.getSquadraAnalizza();
    	
    	lblEta.setText(scelta.etaMedia()+" anni");
    	
    	lblNum.setText(""+scelta.numCalciatori());
    	
    	NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
    	lblValore.setText(nf.format(scelta.valoreTot())+" €");
    	
    	ObservableList<Calciatore> values = FXCollections.observableArrayList(scelta.getCalciatori());
    	tabella.setItems(values);
    	
    	pulisciGrafico(grafico);
    	
    	XYChart.Series<String, Double> statistiche = new XYChart.Series<String, Double>();
    	
    	statistiche.getData().add(createData("Overall", Math.floor(scelta.mediaOverall()*10.0)/10.0 ));
    	statistiche.getData().add(createData("Potenziale", Math.floor(scelta.mediaPotenziale()*10.0)/10.0 ));
    	statistiche.getData().add(createData("Fisico", Math.floor(scelta.mediaFisico()*10.0)/10.0 ));
    	statistiche.getData().add(createData("Tecnica", Math.floor(scelta.mediaTecnica()*10.0)/10.0 ));
    	
    	grafico.getData().add(statistiche);
    	lblAvv.setText("Click sulla riga del calciatore per selezionarlo, un'altro Click per deselezionarlo");
    	venduti = new ArrayList<>();
    	lblCalciatori.setText(""+0);
    	tabella.setSelectionModel(defaultSelectionModel);
    }

	@FXML
    void doCalcolaRosaOttimizzata(ActionEvent event) {
    	int budget = 0;
    	try {
    	      budget = Integer.parseInt(txtBudget.getText().trim());
    	} catch(NumberFormatException nfe) {
    	      lblAvv.setText("Devi inserire un numero intero come Budget!");
    	      txtBudget.clear();
    	      return ;
    	}
    	
    	double t = sldTempo.getValue();
    	double q = sldQualita.getValue();
    	
    	lblAvv.setText("");
    	
    	Rosa ottimizzata = model.calcolaRosaOttimizzata(venduti, budget, t, q);
    	
    	List<Calciatore> nuovi = new ArrayList<>();
    	for (Calciatore c : ottimizzata.getCalciatori())
    		if (!model.getSquadraAnalizza().getCalciatori().contains(c))
    			nuovi.add(c);
    	
    	// colora righe dei nuovi calciatori
    	
    	lblNum.setText(""+ottimizzata.numCalciatori());
    	
    	NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
    	lblValore.setText(nf.format(ottimizzata.valoreTot())+" €");
    	
    	ObservableList<Calciatore> values = FXCollections.observableArrayList();
    	for (Calciatore c : ottimizzata.getCalciatori())
    		values.add(c);
    	
    	tabella.setItems(values);
    	
    	XYChart.Series<String, Double> statistiche = new XYChart.Series<String, Double>();
    	
    	statistiche.getData().add(createData("Overall", Math.floor(ottimizzata.mediaOverall()*10.0)/10.0 ));
    	statistiche.getData().add(createData("Potenziale", Math.floor(ottimizzata.mediaPotenziale()*10.0)/10.0 ));
    	statistiche.getData().add(createData("Fisico", Math.floor(ottimizzata.mediaFisico()*10.0)/10.0 ));
    	statistiche.getData().add(createData("Tecnica", Math.floor(ottimizzata.mediaTecnica()*10.0)/10.0 ));
    	
    	grafico.getData().add(statistiche);
    	
    	lblBudgetRimasto.setText("Budget Rimasto:");
    	lblRimasti.setText(""+nf.format(model.getSquadraAnalizza().valoreTot()+budget-ottimizzata.valoreTot())+" €");
    	
    	btnCalcola.setDisable(true);
    	txtBudget.setEditable(false);
    	sldQualita.setDisable(true);
    	sldTempo.setDisable(true);
    	tabella.setSelectionModel(null);
    }
	
	@FXML
    void doAggiungi(MouseEvent event) {
		if ( tabella.getSelectionModel() != null ) {
		Calciatore selected = tabella.getSelectionModel().getSelectedItem();
		
		if ( !venduti.contains(selected) ) {
			venduti.add(selected);
			colNome.getCellData(selected);
//			cambia colore riga 
	        
		} else {
			venduti.remove(selected);
			
		}
		
		lblCalciatori.setText(""+venduti.size());
		lblAvv.setText("");
		}
    }

    @FXML
    void doCancella(ActionEvent event) {
    	txtBudget.setText("0");;
    	lblAvv.setText("");
    	sldQualita.setValue(0.5);
    	sldTempo.setValue(0.5);
    	lblBudgetRimasto.setText("");
    	lblRimasti.setText("");
    	setup();
    	btnCalcola.setDisable(false);
    	txtBudget.setEditable(true);
    	sldQualita.setDisable(false);
    	sldTempo.setDisable(false);
    }

    @FXML
    void initialize() {
        assert lblNum != null : "fx:id=\"lblNum\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblEta != null : "fx:id=\"lblEta\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblValore != null : "fx:id=\"lblValore\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblBudgetRimasto != null : "fx:id=\"lblBudgetRimasto\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblRimasti != null : "fx:id=\"lblRimasti\" was not injected: check your FXML file 'Ottimizza.fxml'.";
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
        assert sldTempo != null : "fx:id=\"sldTempo\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert sldQualita != null : "fx:id=\"sldQualita\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblCalciatori != null : "fx:id=\"lblCalciatori\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert lblAvv != null : "fx:id=\"lblErr\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        assert ball != null : "fx:id=\"ball\" was not injected: check your FXML file 'Ottimizza.fxml'.";
        
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAnni.setCellValueFactory(new PropertyValueFactory<>("anni"));
        colNaz.setCellValueFactory(new PropertyValueFactory<>("nazionalità"));
        colRuolo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
        colVal.setCellValueFactory(new PropertyValueFactory<>("valore"));
        
        defaultSelectionModel = tabella.getSelectionModel();
    }
    
    private XYChart.Data<String, Double> createData(String nome, double valore) {
    	XYChart.Data<String, Double> data = new XYChart.Data<String, Double>(nome, valore);
    	
    	data.nodeProperty().addListener(new ChangeListener<Node>() {
            @Override public void changed(ObservableValue<? extends Node> ov, Node oldNode, final Node node) {
              if (node != null)
                displayLabelForData(data);
            }
        });
    	
    	return data;
    }
    
    /** places a text label with a bar's value above a bar node for a given XYChart.Data */
    private void displayLabelForData(XYChart.Data<String, Double> data) {
      final Node node = data.getNode();
      final Text dataText = new Text(data.getYValue() + " ");
      node.parentProperty().addListener(new ChangeListener<Parent>() {
        @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
        	if (parent != null && parent instanceof Group) {
        		Group parentGroup = (Group) parent;
        		parentGroup.getChildren().add(dataText);
        	}}});

      node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
        @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
          dataText.setLayoutX(Math.round(bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));
          dataText.setLayoutY(Math.round(bounds.getMinY() - dataText.prefHeight(-1) * 0.5));
        }});
    }
    
    private void pulisciGrafico(BarChart<String, Double> grafico) {
    	ObservableList<Series<String, Double>> allSeries = grafico.getData();
        
            for (XYChart.Series<String, Double> series : allSeries) {
                for (XYChart.Data<String, Double> data : series.getData()) {
                    Node node = data.getNode();
                    Parent parent = node.parentProperty().get();
                    if (parent != null && parent instanceof Group) {
                        Group group = (Group) parent;
                        group.getChildren().clear();
                    }
                }
            }
            allSeries.clear();
    }
    
}