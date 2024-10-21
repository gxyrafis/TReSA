package application;

import org.apache.lucene.index.IndexWriter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class mainPage {
	
	public static void create(Stage stage) 
	{
		Label title = new Label("T.Re.S.A.\nThe Reuters Search Assistant");
		Label prompt = new Label("Welcome\nWhat would you like to do?");
		VBox mainwrapper = new VBox();
		Button index = new Button("Edit article list");
		Button search = new Button("Search");
		HBox buttonwrapper = new HBox();
        var scene = new Scene(mainwrapper, 700, 400);
        
        searchGUI handlerSearch = new searchGUI(stage, scene);
        indexGUI handlerIndex = new indexGUI(stage, scene);
        
        //Button Setup
        index.setMinSize(120, 40);
        search.setMinSize(120, 40);
        index.setStyle("-fx-font-size: 13px;-fx-font-weight: bold;");
        search.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
        buttonwrapper.setSpacing(30);
        buttonwrapper.getChildren().addAll(index,search);
        buttonwrapper.setAlignment(Pos.CENTER);
        
        index.setOnAction(handlerIndex);
        search.setOnAction(handlerSearch);
        
        
        //Label setup
        title.setStyle("-fx-text-alignment: center;-fx-font-size: 25px;-fx-font-weight: bold;");
        prompt.setStyle("-fx-text-alignment: center;-fx-font-size: 15px;-fx-font-weight: bold;");
        prompt.setPadding(new Insets(50,0,40,0));
        
        //Mainwrapper setup
        mainwrapper.getChildren().addAll(title, prompt, buttonwrapper);
        mainwrapper.setAlignment(Pos.CENTER);
        mainwrapper.setPadding(new Insets(30,0,100,0));
        
        stage.setScene(scene);
        stage.setMinHeight(400);
        stage.setMinWidth(700);
        stage.setMaxHeight(1080);
        stage.setMaxWidth(1920);
        stage.setTitle("TReSA Main");
        stage.show();
	}
}
