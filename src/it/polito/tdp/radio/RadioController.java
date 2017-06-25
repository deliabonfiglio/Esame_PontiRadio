/**
 * Sample Skeleton for 'Radio.fxml' Controller Class
 */

package it.polito.tdp.radio;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.radio.bean.Citta;
import it.polito.tdp.radio.bean.Model;
import it.polito.tdp.radio.bean.Ponte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class RadioController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="comboCity1"
    private ComboBox<Citta> comboCity1; // Value injected by FXMLLoader

    @FXML // fx:id="comboCity2"
    private ComboBox<Citta> comboCity2; // Value injected by FXMLLoader

    @FXML // fx:id="comboCity3"
    private ComboBox<Citta> comboCity3; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaPonti"
    private Button btnCercaPonti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCopertura"
    private Button btnCopertura; // Value injected by FXMLLoader
    @FXML
    private TextArea txtResult;

    @FXML
    void doCercaCoperturaOttima(ActionEvent event) {
    	Citta c1 = comboCity1.getValue();
    	Citta c2 = comboCity2.getValue();
    	Citta c3 = comboCity3.getValue();
    	
    	if(c1== null || c2== null || c3==null|| c1.equals(c2) || c2.equals(c3) || c3.equals(c1)){
    		txtResult.appendText("Errore: scegliere tre città, diverse tra loro\n");
    		return;
    	}
    	txtResult.clear();
    	/*Set<Ponte> max = model.createPath(c1, c2,c3);
    	txtResult.appendText("Massimo numero di ponti necessari: "+max.size()+"\n");
    	
    	for(Ponte p: max){
    		txtResult.appendText(p.toString()+"\n");
    	}*/
    	model.creaGrafo(c1, c2, c3);
    	Set<Ponte> ponti = model.getPath();
    	for(Ponte p: ponti)
    		txtResult.appendText(p.toString()+"\n");
    	
    	/*
    	Set<Ponte> ponti = model.interfacciaRicorsione(c1, c2, c3);
    	for(Ponte p: ponti)
    		txtResult.appendText(p.toString()+"\n");
    	*/
    }
    @FXML
    void doCercaPonti(ActionEvent event) {
    	Citta c1 = comboCity1.getValue();
    	Citta c2 = comboCity2.getValue();
    	
    	if(c1== null || c2== null || c1.equals(c2)){
    		txtResult.appendText("Errore: scegliere due città, diverse tra loro\n");
    		return;
    	}
    	txtResult.clear();
    	
    	List<Ponte>pontiDiEntrambe= model.getPontiCheCoprono(c1, c2);
    	if(pontiDiEntrambe.size()==0){
    		txtResult.appendText("Non esistono ponti che coprono entrambe le città selezionate.\n");
    	}
    	else {
    		txtResult.appendText("I ponti che coprono entrambe le città selezionate sono: \n");
    		for(Ponte p: pontiDiEntrambe){
    			txtResult.appendText(p.toString()+"\n");
    		}
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert comboCity1 != null : "fx:id=\"comboCity1\" was not injected: check your FXML file 'Radio.fxml'.";
        assert comboCity2 != null : "fx:id=\"comboCity2\" was not injected: check your FXML file 'Radio.fxml'.";
        assert comboCity3 != null : "fx:id=\"comboCity3\" was not injected: check your FXML file 'Radio.fxml'.";
        assert btnCercaPonti != null : "fx:id=\"btnCercaPonti\" was not injected: check your FXML file 'Radio.fxml'.";
        assert btnCopertura != null : "fx:id=\"btnCopertura\" was not injected: check your FXML file 'Radio.fxml'.";

    }
    
    public void setModel(Model m1){
    	this.model=m1;
    	this.comboCity1.getItems().clear();
    	this.comboCity2.getItems().clear();
    	this.comboCity3.getItems().clear();
    	
    	this.comboCity1.getItems().addAll(model.getCities());
    	this.comboCity2.getItems().addAll(model.getCities());
    	this.comboCity3.getItems().addAll(model.getCities());
    }
}
