package application;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class searchByArticle implements EventHandler<MouseEvent>{

	private Stage stage;
	private Scene scene;
	
	public searchByArticle(Stage stage, Scene scene)
	{
		this.scene = scene;
		this.stage = stage;
	}
	@Override
	public void handle(MouseEvent arg0) {
		ObservableList<String> indexedL = FXCollections.observableArrayList();
		ListView<String> indexedLV = new ListView<>(indexedL);
		Button back = new Button("Back");
		Button preview = new Button("Preview");
		Button search = new Button("Find Similar");
		VBox backwrapper = new VBox();
		VBox wrapper = new VBox();
		HBox buttons = new HBox();
		VBox main = new VBox();
		Label explain = new Label("Archived Articles");
		Label explain2 = new Label("To add, remove or edit articles click on the \"Edit Article List\" button on the main screen");
		VBox labels = new VBox();
		HBox resultnum = new HBox();
		TextField resultnumtext = new TextField("10");
		Text resultnumexp = new Text("Desired number of results:");
		var scene2 = new Scene(wrapper, 700, 400);
		
		resultnumtext.setMaxWidth(60);
        resultnumtext.setAlignment(Pos.CENTER);
        resultnum.setAlignment(Pos.CENTER);
        resultnum.getChildren().addAll(resultnumexp, resultnumtext);
        resultnum.setSpacing(10);
		
        resultnumexp.setStyle("-fx-text-alignment: center;-fx-font-size: 13px;");
		explain.setStyle("-fx-text-alignment: center;-fx-font-size: 15px;-fx-font-weight: bold;");
		explain2.setStyle("-fx-text-alignment: center;-fx-font-size: 12px;-fx-font-weight: bold;");
		labels.getChildren().addAll(explain,explain2);
		labels.setAlignment(Pos.CENTER);
		
		//Buttons
		search.setMaxSize(100, 40);
		preview.setMaxSize(100, 40);
		preview.setPrefSize(100, 40);
		search.setPrefSize(100, 40);
		search.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;");
		preview.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
		buttons.getChildren().addAll(preview, search);
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);
		
		//Fill indexed list
		ArrayList<String> tmp2 = new ArrayList<>();
		
		tmp2 = functionality.getIndexedNames();
		if(tmp2 != null) 
		{
			for(String str : tmp2) 
			{
				indexedL.add(str);
			}
		}
		
		//Style indexed list
		indexedLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		indexedLV.setPrefHeight(200);
		indexedLV.setPrefWidth(120);
		indexedLV.setMaxHeight(200);
		indexedLV.setMaxWidth(120);
		
		//Back button
		backwrapper.getChildren().add(back);
		backwrapper.setAlignment(Pos.TOP_LEFT);
		back.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
		backwrapper.setPadding(new Insets(7,0,0,7));
        back.setMinSize(60, 30);
        back.setOnAction((e)->{
        	stage.setScene(scene);
        	stage.setTitle("TReSA Search");
        });
        
        search.setOnAction((e)->{
        	if((functionality.isNumber(resultnumtext.getText(), false))&&(Integer.parseInt(resultnumtext.getText()) > 0)) 
        	{
	        	String filename = indexedLV.getSelectionModel().getSelectedItem();
	        	IndexSearcher searcher;
	        	if(filename!=null) 
	        	{
	        		try {
		        		String filepath = "";
						searcher = invertedIndex.createSearcher();
						TopDocs hits = invertedIndex.searchByName(filename, searcher);
						
						for(ScoreDoc sd : hits.scoreDocs) 
						{
							Document d = searcher.doc(sd.doc);
							
							String fn = String.format(d.get("File Name"));
							String query = String.format(d.get("Body"));
							String query2 = String.format(d.get("Title"));
							
							
							TopDocs similar = invertedIndex.findSimilar(searcher, query, query2, Integer.parseInt(resultnumtext.getText()));
							
							searchResults.present(stage, scene2, similar, query);
						}
	        		} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
	        		} catch (Exception e1) {
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
        preview.setOnAction((e)->{
        	String filename = indexedLV.getSelectionModel().getSelectedItem();
        	IndexSearcher searcher;
        	if(filename!=null) 
        	{
	        	try {
	        		String filepath = "";
					searcher = invertedIndex.createSearcher();
					TopDocs hits = invertedIndex.searchByName(filename, searcher);
					
					for(ScoreDoc sd : hits.scoreDocs) 
					{
						Document d = searcher.doc(sd.doc);
						
						filepath = String.format(d.get("File Directory"));
						
						String title = functionality.getFieldContent(filepath+filename, "Title");
						String people = functionality.getFieldContent(filepath+filename, "People");
						String places = functionality.getFieldContent(filepath+filename, "Places");
						String body = functionality.getFieldContent(filepath+filename, "Body");
						
						articleDisplay.article(title, people, places, body, stage, scene2);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	else 
        	{
        		Alert error = new Alert(AlertType.ERROR, "Invalid Input. Select a document to use as the basis of the search");
            	error.showAndWait();
        	}
			
        });
        
        main.setAlignment(Pos.CENTER);
        main.getChildren().addAll(indexedLV, resultnum, buttons);
        main.setSpacing(20);
        main.setPadding(new Insets(0,0,10,0));
        wrapper.getChildren().addAll(backwrapper, labels, main);
        wrapper.setSpacing(10);
		
        stage.setScene(scene2);
        stage.setTitle("Article Search");
	}
}
