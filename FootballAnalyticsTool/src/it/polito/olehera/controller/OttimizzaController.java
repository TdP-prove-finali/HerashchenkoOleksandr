package it.polito.olehera.controller;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import it.polito.olehera.model.Calciatore;
import it.polito.olehera.model.Model;
import it.polito.olehera.model.Rosa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class OttimizzaController {
	
	private Model model;
	private List<Calciatore> venduti;
	private TableViewSelectionModel<Calciatore> defaultSelectionModel;
	private NumberFormat nf;
	private ObservableList<Integer> righe;
	private String classe;
	
	public void setModel(Model model) {
		this.model = model;
		nf = NumberFormat.getInstance(Locale.ITALIAN);
		righe = FXCollections.observableArrayList();
		classe = "ceduto";
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
    	righe.clear();
    	lblCalciatori.setText(""+0);
    	tabella.setSelectionModel(defaultSelectionModel);
    }

	@FXML
    void doCalcolaRosaOttimizzata(ActionEvent event) {
		int budget = 0;
    	float b = 0;
    	String inserito = txtBudget.getText().trim();
    	try {
    	      b =  Float.parseFloat(inserito.substring(0, inserito.length()-1));
    	} catch(NumberFormatException nfe) {
    	      lblAvv.setText("Devi inserire un numero positivo seguito senza spazi da M o K");
    	      txtBudget.clear();
    	      return ;
    	}
    	
    	if ( b < 0 ) {
    		lblAvv.setText("Devi inserire un numero positivo seguito senza spazi da M o K");
  	        txtBudget.clear();
  	        return ;
    	}
    	
    	char ch = inserito.charAt(inserito.length()-1);
		if (ch == 'M')
			budget = (int)(b*1000000);
		else if (ch == 'K') 
			budget = (int)(b*1000);
		else {
			lblAvv.setText("Devi inserire un numero positivo seguito senza spazi da M o K");
  	        txtBudget.clear();
  	        return ;
		}
		
		int budgetMassimo = (int) (model.getSquadraAnalizza().valoreTot()*0.15);
		if ( budget > budgetMassimo ) {
			lblAvv.setText("Budget massimo: "+nf.format(budgetMassimo)+" €  (15% del valore totale della Rosa)");
			txtBudget.setText("0M");;
  	        return ;
		}
		
		int vendutiMax = (int) (model.getSquadraAnalizza().numCalciatori()/4);
		if ( venduti.size() > vendutiMax ) {
			lblAvv.setText("Puoi cedere al massimo "+vendutiMax+" giocatori  (1/4 del numero totale di giocatori della Rosa)");
  	        venduti.clear();
  	        righe.clear();
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
    	
    	if ( nuovi.size() == 0 ) {
    		lblAvv.setText("Con questi parametri la migliore Rosa è quella attuale!");
    	}
    	
    	lblNum.setText(""+ottimizzata.numCalciatori()+"  ("+nuovi.size()+")");
    	lblEta.setText(ottimizzata.etaMedia()+" anni");
    	lblValore.setText(nf.format(ottimizzata.valoreTot())+" €");
    	
    	righe.clear();
    	List<Integer> indici = new ArrayList<>();
    	ObservableList<Calciatore> values = FXCollections.observableArrayList();
    	for (int i=0; i<ottimizzata.getCalciatori().size(); i++) {
    		Calciatore c = ottimizzata.getCalciatori().get(i);
    		values.add(c);
    		if ( nuovi.contains(c) )
    			indici.add(i);
    	}
    	
    	tabella.setItems(values);
    	classe = "nuovo";
    	righe.setAll(indici);
    	
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
		if ( tabella.getSelectionModel() != null &&  tabella.getSelectionModel().getSelectedItem() != null ) {
			
		Calciatore selected = tabella.getSelectionModel().getSelectedItem();
		Integer index = tabella.getSelectionModel().getSelectedIndex();
		
		if ( !venduti.contains(selected) ) {
			venduti.add(selected);
			righe.add(index);
		} else {
			venduti.remove(selected);
			if ( righe.contains(index) )
				righe.remove(index);
		}
		
		tabella.getSelectionModel().clearSelection();
		lblCalciatori.setText(""+venduti.size());
		lblAvv.setText("");
		
		}
    }

    @FXML
    void doCancella(ActionEvent event) {
    	txtBudget.setText("0M");
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
    	righe.clear();
    	classe = "ceduto";
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
        
        colNome.setCellValueFactory(new PropertyValueFactory<Calciatore, String>("nome"));
        colAnni.setCellValueFactory(new PropertyValueFactory<Calciatore, Integer>("anni"));
        colNaz.setCellValueFactory(new PropertyValueFactory<Calciatore, String>("nazionalità"));
        colRuolo.setCellValueFactory(new PropertyValueFactory<Calciatore, String>("ruolo"));
        colVal.setCellValueFactory(new PropertyValueFactory<Calciatore, String>("valore"));
        
        defaultSelectionModel = tabella.getSelectionModel();
        
        tabella.setRowFactory(new Callback<TableView<Calciatore>, TableRow<Calciatore>>() {
            @Override
            public TableRow<Calciatore> call(TableView<Calciatore> tableView) {
                final TableRow<Calciatore> row = new TableRow<Calciatore>() {
                    @Override
                    protected void updateItem(Calciatore item, boolean empty){
                        super.updateItem(item, empty);
                        if (righe.contains(getIndex())) {
                            if ( !getStyleClass().contains(classe) ) {
                                getStyleClass().add(classe);
                            }
                        } else {
                            getStyleClass().removeAll(Collections.singleton(classe));
                        }
                    }
                };
                righe.addListener(new ListChangeListener<Integer>() {
                    @Override
                    public void onChanged(Change<? extends Integer> change) {
                        if (righe.contains(row.getIndex())) {
                            if ( !row.getStyleClass().contains(classe) ) {
                                row.getStyleClass().add(classe);
                            }
                        } else {
                            row.getStyleClass().removeAll(Collections.singleton(classe));
                        }
                    }
                });
                return row;
            }
        });
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