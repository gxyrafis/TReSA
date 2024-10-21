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

public class searchBoolean implements EventHandler<MouseEvent>{

	private Stage stage;
	private Scene scene;
	
	public searchBoolean(Stage stage, Scene scene)
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
		HBox searchbox = new HBox();
		HBox resultnum = new HBox();
		Text title = new Text("TReSA Boolean Search");
		Text instructions = new Text("Instructions:\nThere are 2 ways to use this search feature.\n1) You can search for terms or sentences separated by the Boolean operatos(OR, AND, NOT) and can also use parenthesis."
				+ "\nExample: (USA OR BRITAIN) AND OIL PRICES\n2)Same as the above, but you can also specify specific fields of the article in your query."
				+ "\nExample: (Title: USA AND Oil OR Places: USA AND Body: oil) NOT (People: George)"
				+ "\nValid fields are \"Title\", \"Body\", \"Places\", \"People\" and MUST be written in that order. If any are omitted, the order remains the same without them.");
		TextField searchf = new TextField();
		Text nrresult = new Text("Desired number of results: ");
		TextField resultnumtext = new TextField("10");
		Button searchb = new Button("Search");
		var scene2 = new Scene(wrapper, 700, 400);
		
        
        searchf.setMaxWidth(400);
        searchf.setMinWidth(400);
        searchf.setMinHeight(25);
        searchf.setPromptText("Search, but Boolean...");
		
		resultnum.setAlignment(Pos.CENTER);
		resultnum.getChildren().addAll(nrresult, resultnumtext);
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
		searchbox.setAlignment(Pos.CENTER);
		searchbox.getChildren().addAll(searchf, searchb);
		searchbox.setSpacing(5);
		title.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;-fx-text-alignment: center;");
		title.setWrappingWidth(400);
		instructions.setWrappingWidth(500);
		instructions.setStyle("-fx-font-size: 12px;-fx-text-alignment: center;");
		resultnumtext.setMaxWidth(60);
		resultnumtext.setAlignment(Pos.TOP_RIGHT);
		
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
        			try {
        				TopDocs hits = invertedIndex.searchBoolean(searchf.getText(), Integer.parseInt(resultnumtext.getText()));
        				searchResults.present(stage, scene2, hits, searchf.getText());
						
					} catch (NumberFormatException | IOException | ParseException e1) {
						Alert error = new Alert(AlertType.ERROR, "Invalid Input. Please correct your syntax.");
		            	error.showAndWait();
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
		
		resultnum.setSpacing(10);
		main.getChildren().addAll(title, instructions, searchbox, resultnum);
		main.setAlignment(Pos.CENTER);
		main.setSpacing(20);
		wrapper.getChildren().addAll(backwrapper, main);
		stage.setScene(scene2);
		stage.setTitle("Boolean Search");
	}

}
