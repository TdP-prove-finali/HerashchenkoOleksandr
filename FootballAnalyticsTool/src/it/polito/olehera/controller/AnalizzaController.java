package it.polito.olehera.controller;

import java.net.URL;
import java.text.NumberFormat;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private BarChart<String, Double> grafico;
    
    @FXML
    private CategoryAxis asseX;

    @FXML
    private NumberAxis asseY;

    @FXML
    private Label lblSquadra;

    @FXML
    private Button btnCambia;

    @FXML
    private Button btnOttimizza;
    
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
    
    
	private void setup() {
    	Rosa scelta = model.getSquadraAnalizza();
    	
    	lblSquadra.setText(scelta.getNome());
    	
    	lblEta.setText(scelta.etaMedia()+" anni");
    	
    	lblNum.setText(""+scelta.numCalciatori());
    	
    	NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
    	lblValore.setText(nf.format(scelta.valoreTot())+" €");
    	
    	ObservableList<Calciatore> values = FXCollections.observableArrayList();
    	for (Calciatore c : scelta.getCalciatori())
    		values.add(c);
    	
    	tabella.setItems(values);
    	
    	XYChart.Series<String, Double> statistiche = new XYChart.Series<String, Double>();
    	
    	statistiche.getData().add(createData("Overall", scelta.mediaOverall()));
    	statistiche.getData().add(createData("Potenziale", scelta.mediaPotenziale()));
    	statistiche.getData().add(createData("Fisico", scelta.mediaFisico()));
    	statistiche.getData().add(createData("Tecnica", scelta.mediaTecnica()));
    	
    	grafico.getData().add(statistiche);
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
			
			stage.setTitle("Football Analytics Tool");
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
			
			OttimizzaController controller = loader.getController();
			Stage s = new Stage();
			controller.setModel(model);
			
			s.setAlwaysOnTop(true);
			s.setTitle("Football Analytics Tool");
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
        assert asseX != null : "fx:id=\"asseX\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert asseY != null : "fx:id=\"asseY\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert lblSquadra != null : "fx:id=\"lblSquadra\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert btnCambia != null : "fx:id=\"btnCambia\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert btnOttimizza != null : "fx:id=\"btnOttimizza\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert tabella != null : "fx:id=\"tabella\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert colNome != null : "fx:id=\"colNome\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert colAnni != null : "fx:id=\"colAnni\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert colNaz != null : "fx:id=\"colNaz\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert colRuolo != null : "fx:id=\"colRuolo\" was not injected: check your FXML file 'Analizza.fxml'.";
        assert colVal != null : "fx:id=\"colVal\" was not injected: check your FXML file 'Analizza.fxml'.";
        
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAnni.setCellValueFactory(new PropertyValueFactory<>("anni"));
        colNaz.setCellValueFactory(new PropertyValueFactory<>("nazionalità"));
        colRuolo.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
        colVal.setCellValueFactory(new PropertyValueFactory<>("valore"));
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
      final Text dataText = new Text(data.getYValue() + "");
      node.parentProperty().addListener(new ChangeListener<Parent>() {
        @Override public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
          Group parentGroup = (Group) parent;
          parentGroup.getChildren().add(dataText);
        }
      });

      node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
        @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
          dataText.setLayoutX(Math.round(bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2));
          dataText.setLayoutY(Math.round(bounds.getMinY() - dataText.prefHeight(-1) * 0.5));
        }
      });
    }

}