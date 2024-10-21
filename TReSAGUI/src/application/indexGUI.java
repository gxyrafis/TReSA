package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class indexGUI implements EventHandler<ActionEvent>{
	
	private Stage stage;
	private Scene scene;
	
	public indexGUI(Stage stage, Scene scene) 
	{
		this.stage = stage;
		this.scene = scene;
	}

	@Override
	public void handle(ActionEvent arg0) {
		
		Label indexed = new Label("Indexed Articles");
		Label nonindexed = new Label("Non-Indexed Articles");
		VBox main = new VBox();
		HBox labels = new HBox();
		HBox middle = new HBox();
		VBox addrem = new VBox();
		Button back = new Button("Back");
		Button add = new Button("Add");
		Button remove = new Button("Remove");
		Button change = new Button("Change Directory");
		Button refresh = new Button("Refresh");
		ObservableList<String> nonindexedL = FXCollections.observableArrayList();
		ObservableList<String> indexedL = FXCollections.observableArrayList();
		ListView<String> nonindexedLV = new ListView<>(nonindexedL);
		ListView<String> indexedLV = new ListView<>(indexedL);
        var scene2 = new Scene(main, 700, 400);
        
        //main setup
        main.getChildren().addAll(labels, middle, back);
        main.setAlignment(Pos.CENTER);
        main.setSpacing(30);
        
        //middle pane setup
        middle.getChildren().addAll(nonindexedLV, addrem, indexedLV);
        middle.setAlignment(Pos.CENTER);
        
        //add remove buttons pane
        addrem.getChildren().addAll(add, remove, refresh, change);
        addrem.setAlignment(Pos.CENTER);
        addrem.setPadding(new Insets(0, 30, 0, 30));
        addrem.setSpacing(25);
      
        
        //labels pane setup
        labels.getChildren().addAll(nonindexed, indexed);
        labels.setSpacing(140);
        labels.setAlignment(Pos.CENTER);
        
        //button setup
        back.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
        back.setMinSize(100, 40);
        back.setOnAction((e)->{
        	stage.setScene(scene);
        	stage.setTitle("TReSA Main");
        });
        
        //Change button
        change.setStyle("-fx-font-size: 10px;");
        change.setMinSize(80, 25);
        change.setOnAction((e)->{
            Dialog<String> dialog = new Dialog<>();
            dialog.initOwner(stage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Change Directory");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            VBox mainpane = new VBox();
            
            TextField dir = new TextField();
            dir.setPromptText(Global.dirart.getText().replace("\\\\", "\\"));
            
            mainpane.getChildren().add(dir);
            
            dialog.setResultConverter((button) ->{
                if(button == ButtonType.OK)
                {
                	return dir.getText();
                }
                else
                {
                    return null;
                }
            });
            dialog.getDialogPane().setContent(mainpane);
            Optional<String> result = dialog.showAndWait();
            
            if(result.isPresent() && !result.get().equals(""))
            {
                Path path = Paths.get(result.get());
                if(!Files.exists(path)) 
                {
                	Alert error = new Alert(AlertType.ERROR, "Invalid Directory Path. Please try again.");
                	error.showAndWait();
                }
                else 
                {
            		ArrayList<String> tmp = new ArrayList<>();
            		nonindexedL.clear();
            		tmp = functionality.getFileNames(result.get());
            		for(String str : tmp) 
            		{
            			nonindexedL.add(str);
            		}
            		Global.dirart.setText(result.get().replace("\\", "\\\\"));
            		if(!Global.dirart.getText().endsWith("\\")) 
            		{
            			Global.dirart.setText(Global.dirart.getText() + "\\");
            		}
            		
                }
            }
            
        });
        
        //Refresh button
        refresh.setStyle("-fx-font-size: 10px;");
        refresh.setMinSize(80, 25);
        refresh.setOnAction((e)->{
        	ArrayList<String> tmp = new ArrayList<>();
    		
    		tmp = functionality.getFileNames(Global.dirart.getText().replace("\\\\", "\\"));
    		nonindexedL.clear();
    		for(String str : tmp) 
    		{
    			nonindexedL.add(str);
    		}
        });
        
        add.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
        add.setMinSize(100, 30);
        add.setDisable(true);
        add.setOnAction((e) -> {
        	ObservableList<String> tmplist = nonindexedLV.getSelectionModel().getSelectedItems();
            ArrayList<String> toindex = new ArrayList<>();

	            for(String s : tmplist) 
	            {
	            	toindex.add(s.replace("[", "").replace("]", "").trim());
	            }
	            
	            nonindexedL.removeAll(tmplist);
	            
	            List<Document> docs = new ArrayList<>();
	            try {
	            	IndexWriter writer = null;
	    			try {
	    				writer = invertedIndex.createWriter();
	    			} catch (IOException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    			}
					docs = PreProcessing.articlesEditor(toindex, Global.dirart.getText());
					if(docs != null && writer != null) 
					{
						
						writer.addDocuments(docs);
						writer.commit();
						
						ArrayList<String> tmp = new ArrayList<>();
						
						 writer.close();
						ArrayList<String> tmp2 = new ArrayList<>();
						indexedL.clear();
						tmp2 = functionality.getIndexedNames();
						if(tmp2 != null) 
						{
							for(String str : tmp2) 
							{
								indexedL.add(str);
							}
						}
						 
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            

            
        });
		
        remove.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
        remove.setMinSize(100, 30);
        remove.setDisable(true);
        remove.setOnAction((e)->{
        	ObservableList<String> tmplist = indexedLV.getSelectionModel().getSelectedItems();
        	
        	for(String str : tmplist) 
        	{
        		if(!nonindexedL.contains(str.replace("[", "").replace("]", "").trim())) 
        		{
        			nonindexedL.add(str.replace("[", "").replace("]", "").trim());
        		}
        	}
        	ArrayList<String> tmp = new ArrayList<>();
			for(String s : nonindexedL) 
			{
				tmp.add(s);
			}
			
			Collections.sort(tmp, new StringComparator());
			nonindexedL.clear();
			for(String str : tmp) 
			{
				nonindexedL.add(str);
			}
        	IndexWriter writer = null;
			try {
				writer = invertedIndex.createWriter();
				Term term;
				
				for(String str : tmplist) 
	        	{
					System.out.print(str);
					term = new Term("File Name", str.replace("[", "").replace("]", "").trim());
					writer.deleteDocuments(term);
					writer.forceMergeDeletes();
					writer.commit();
	        	}
				writer.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ArrayList<String> tmp2 = new ArrayList<>();
			
			indexedL.clear();
			tmp2 = functionality.getIndexedNames();
			if(tmp2 != null) 
			{
				for(String str : tmp2) 
				{
					indexedL.add(str);
				}
			}
        });
        
        //label setup
		indexed.setStyle("-fx-text-alignment: center;-fx-font-size: 15px;-fx-font-weight: bold;");
		nonindexed.setStyle("-fx-text-alignment: center;-fx-font-size: 15px;-fx-font-weight: bold;");
		
		//Observable List setup nonindexed
		ArrayList<String> tmp = new ArrayList<>();
		
		tmp = functionality.getFileNames(Global.dirart.getText().replace("\\\\", "\\"));
		for(String str : tmp) 
		{
			nonindexedL.add(str);
		}
		
		//Observable List setup indexed
		ArrayList<String> tmp2 = new ArrayList<>();
		
		tmp2 = functionality.getIndexedNames();
		if(tmp2 != null) 
		{
			for(String str : tmp2) 
			{
				indexedL.add(str);
			}
		}
		//ListView setup
		nonindexedLV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		nonindexedLV.setPrefHeight(200);
		nonindexedLV.setPrefWidth(120);
		indexedLV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		indexedLV.setPrefHeight(200);
		indexedLV.setPrefWidth(120);
		
        //Buttons Disabled and Enabled
        nonindexedLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null)
                {
                    add.setDisable(true);
                }
                else
                {
                    add.setDisable(false);
                }
            }
            
        });
        
        indexedLV.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null)
                {
                    remove.setDisable(true);
                }
                else
                {
                    remove.setDisable(false);
                }
            }
            
        });
		
		stage.setScene(scene2);
		stage.setTitle("TReSA Indexing");
		stage.setResizable(false);
	}

}
