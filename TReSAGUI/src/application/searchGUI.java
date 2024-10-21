package application;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class searchGUI implements EventHandler<ActionEvent>{


	private Stage stage;
	private Scene scene;
	
	public searchGUI(Stage stage, Scene scene) 
	{
		this.stage = stage;
		this.scene = scene;
	}
	public void handle(ActionEvent arg0) {
		Label title = new Label("TReSA");
		Label resnumlabel = new Label("Number of results: ");
		VBox wrapper = new VBox();
		VBox main = new VBox();
		VBox backwrapper = new VBox();
		HBox searchwrapper = new HBox();
		HBox resultnum = new HBox();
		TextField searchf = new TextField();
		TextField resultnumtext = new TextField("10");
		var scene2 = new Scene(wrapper, 700, 400);
		Button back = new Button("Back");
		Button searchb = new Button("Search");
		TitledPane searchbadv = new TitledPane();
		Text booleansearch = new Text("Boolean Search");
		Text searcharticle = new Text("Search by Article");
		Text fieldsearch = new Text("Search by Field");
		VBox dropdown = new VBox();
		searchByArticle sba = new searchByArticle(stage, scene2);
		searchBoolean sb = new searchBoolean(stage, scene2);
		searchField sf = new searchField(stage, scene2);
		
		
		backwrapper.getChildren().add(back);
		backwrapper.setAlignment(Pos.TOP_LEFT);
		backwrapper.setPadding(new Insets(7,0,0,7));
        back.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
        back.setMinSize(60, 30);
        back.setOnAction((e)->{
        	stage.setScene(scene);
        	stage.setTitle("TReSA Main");
        });
        
        searchf.setMaxWidth(400);
        searchf.setMinWidth(400);
        searchf.setMinHeight(25);
        
        resultnum.getChildren().addAll(resnumlabel, resultnumtext);
        resultnum.setPadding(new Insets(0,0,20,0));
        resultnum.setSpacing(10);
        resultnum.setAlignment(Pos.CENTER);
        
        resultnumtext.setMaxWidth(60);
        resultnumtext.setAlignment(Pos.CENTER_RIGHT);
        
        searchb.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
        searchb.setMinSize(50, 20);
        searchb.setOnAction((e)->{
        	if((functionality.isNumber(resultnumtext.getText(), false))&&(Integer.parseInt(resultnumtext.getText()) > 0)) 
        	{
        		if(searchf.getText().equals("")) 
        		{
        		}
        		else 
        		{
	        		IndexSearcher searcher;
					try {
						searcher = invertedIndex.createSearcher();
						TopDocs hits = invertedIndex.searchGeneral(searchf.getText(), Integer.parseInt(resultnumtext.getText()), searcher);
						searchResults.present(stage, scene2, hits, searchf.getText());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		}
        	}
        	else 
        	{
        		Alert error = new Alert(AlertType.ERROR, "Invalid Input. Please insert a positive integer at the 'Number of results' field.");
            	error.showAndWait();
        	}
        });
        
        //Advanced search stuff
        searchbadv.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;");
        booleansearch.setStyle("-fx-font-size: 14px;-fx-text-alignment: center;");
        booleansearch.setOnMouseEntered((e)->{
        	Text hovered = (Text)e.getTarget();
        	
        	hovered.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-alignment: center;-fx-underline: true;");
        });
        booleansearch.setOnMouseExited((e)->
        {
        	Text hovered = (Text)e.getTarget();
        	
        	hovered.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-alignment: center;");
        });
        booleansearch.setOnMouseClicked(sb);
        fieldsearch.setOnMouseEntered((e)->{
        	Text hovered = (Text)e.getTarget();
        	
        	hovered.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-alignment: center;-fx-underline: true;");
        });
        fieldsearch.setOnMouseExited((e)->
        {
        	Text hovered = (Text)e.getTarget();
        	
        	hovered.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-alignment: center;");
        });
        fieldsearch.setOnMouseClicked(sf);
        searcharticle.setOnMouseEntered((e)->{
        	Text hovered = (Text)e.getTarget();
        	
        	hovered.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-alignment: center;-fx-underline: true;");
        });
        searcharticle.setOnMouseExited((e)->
        {
        	Text hovered = (Text)e.getTarget();
        	
        	hovered.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-alignment: center;");
        });
        searcharticle.setOnMouseClicked(sba);
        fieldsearch.setStyle("-fx-font-size: 14px;-fx-text-alignment: center;");
        dropdown.getChildren().addAll(booleansearch, searcharticle, fieldsearch);
        dropdown.setAlignment(Pos.CENTER);
        dropdown.setSpacing(3);
        searcharticle.setStyle("-fx-font-size: 14px;-fx-text-alignment: center;");
        searchbadv.setMaxSize(90, 40);
        searchbadv.setText("Advanced Search");
        searchbadv.setContent(dropdown);
        searchbadv.setExpanded(false);
        
        searchwrapper.getChildren().addAll(searchf, searchb);
        searchwrapper.setAlignment(Pos.CENTER);
        searchwrapper.setSpacing(10);
		
        wrapper.setSpacing(70);
       	wrapper.getChildren().addAll(backwrapper, main);
		main.getChildren().addAll(title, searchwrapper, resultnum, searchbadv);
		main.setSpacing(15);
		main.setAlignment(Pos.CENTER);
		main.setPadding(new Insets(0,0,10,0));
		
		searchf.setPromptText("Search...");
		
		resnumlabel.setStyle("-fx-text-alignment: center;-fx-font-size: 12px;");
		title.setStyle("-fx-text-alignment: center;-fx-font-size: 25px;-fx-font-weight: bold;");
		
		stage.setScene(scene2);
		stage.setTitle("TReSA Search");
		stage.setResizable(false);
	}

}
