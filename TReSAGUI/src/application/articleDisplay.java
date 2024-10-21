package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class articleDisplay{


	public static void article(String title, String people, String places, String body, Stage stage, Scene scene) {
		VBox wrapper = new VBox();
		VBox backwrapper = new VBox();
		Button back = new Button("Back");
		VBox main = new VBox();
		HBox p = new HBox();
		VBox articlewrap = new VBox();
		ScrollPane article = new ScrollPane();
		var scene2 = new Scene(wrapper, 700, 400);
		Label ttl = new Label(title);
		Label ppl = new Label();
		Label plcs = new Label();
		Text bdy = new Text(body);
		
		ttl.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
		ppl.setStyle("-fx-font-size: 14px;");
		bdy.setStyle("-fx-font-size: 16px;");
		bdy.wrappingWidthProperty().bind(scene.widthProperty().subtract(15));
		bdy.setTextAlignment(TextAlignment.CENTER);

		if(!people.equals("")) 
		{
			ppl.setText("People involved: " + people);
		}
		if(!places.equals("")) 
		{
			plcs.setText("Places involved: " + places);
		}
		
		articlewrap.getChildren().add(bdy);
		
		article.setContent(articlewrap);
		article.setFitToWidth(true);
		article.setPrefHeight(350);
		p.getChildren().addAll(ppl, plcs);
		p.setAlignment(Pos.CENTER);
		p.setSpacing(10);
		p.setPadding(new Insets(0,0,10,0));
		
		main.getChildren().addAll(ttl,p,article);
		main.setAlignment(Pos.CENTER);
		
		
		backwrapper.getChildren().add(back);
		backwrapper.setAlignment(Pos.TOP_LEFT);
		backwrapper.setPadding(new Insets(7,0,0,7));
        back.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
        back.setMinSize(60, 30);
        back.setOnAction((e)->{
        	stage.setScene(scene);
        });
        
        wrapper.getChildren().addAll(backwrapper, main);
        wrapper.setAlignment(Pos.CENTER);
        stage.setScene(scene2);
	}

}
