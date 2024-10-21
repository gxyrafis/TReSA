package application;

import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.TopDocs;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class searchField implements EventHandler<MouseEvent>{


	private Stage stage;
	private Scene scene;
	
	public searchField(Stage stage, Scene scene)
	{
		this.scene = scene;
		this.stage = stage;
	}
	@Override
	public void handle(MouseEvent arg0) {
		Button back = new Button("Back");
		VBox backwrapper = new VBox();
		VBox wrapper = new VBox();
		VBox main = new VBox();
		HBox resultnum = new HBox();
		Text[] fields = new Text[4];
		TextField[] searchf = new TextField[4];
		HBox[] searchbox = new HBox[4];
		
		Text nrresult = new Text("Desired number of results: ");
		TextField resultnumtext = new TextField("10");
		Button searchb = new Button("Search");
		var scene2 = new Scene(wrapper, 700, 400);
		
		resultnum.setAlignment(Pos.CENTER);
		resultnum.getChildren().addAll(nrresult, resultnumtext);
		resultnumtext.setMaxWidth(60);
		resultnumtext.setAlignment(Pos.TOP_RIGHT);
		
		for(int i = 0 ; i < 4 ; i++) 
		{
			searchbox[i] = new HBox();
			fields[i] = new Text();
			searchf[i] = new TextField();
			searchbox[i].getChildren().addAll(fields[i], searchf[i]);
			searchbox[i].setAlignment(Pos.CENTER);
			main.getChildren().add(searchbox[i]);
		}
		
		fields[0].setText("Title: ");
		fields[1].setText("Body: ");
		fields[2].setText("People: ");
		fields[3].setText("Places: ");
		
		//Back button
		backwrapper.getChildren().add(back);
		backwrapper.setAlignment(Pos.TOP_LEFT);
		back.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;-fx-text-alignment: center;");
		backwrapper.setPadding(new Insets(7,0,0,7));
		back.setMinSize(60, 30);
		back.setOnAction((e)->{
		stage.setScene(scene);
		stage.setTitle("TReSA Search");
		});
        searchb.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
        searchb.setMinSize(50, 20);
        searchb.setOnAction((e) -> {
        	if((functionality.isNumber(resultnumtext.getText(), false))&&(Integer.parseInt(resultnumtext.getText()) > 0)) 
        	{
        		if(searchf[0].getText().equals("")&&searchf[1].getText().equals("")&&searchf[2].getText().equals("")&&searchf[3].getText().equals("")) 
        		{
        		}
        		else 
        		{
        			try {
        				TopDocs hits = invertedIndex.searchFields(searchf, Integer.parseInt(resultnumtext.getText()));
        				searchResults.present(stage, scene2, hits, searchf[1].getText());
						
					} catch (NumberFormatException | IOException e1) {
						Alert error = new Alert(AlertType.ERROR, "Invalid Input. Please correct your syntax.");
		            	error.showAndWait();
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
		
		searchf[1].setPromptText("Search...");
		searchf[2].setPromptText("Search...");
		searchf[3].setPromptText("Search...");
		searchf[0].setPromptText("Search...");
		
		main.setAlignment(Pos.CENTER);
		main.getChildren().add(resultnum);
		main.getChildren().add(searchb);
		main.setSpacing(30);
		wrapper.setAlignment(Pos.CENTER);
		wrapper.getChildren().addAll(backwrapper, main);
		wrapper.setPadding(new Insets(0,0,10,0));
		
		stage.setScene(scene2);
		
	}

}
