package application;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class searchResults {

	public static void present(Stage stage, Scene scene, TopDocs hits, String query) 
	{
		int i = 0;
		for (ScoreDoc sd : hits.scoreDocs) 
		{
			i++;
	    }
		articleDisplay disp;
		String[] files = new String[i];
		Label[] titles = new Label[i];
		Label[] matchingwords = new Label[i];
		String matchingwordstext = "";
		VBox main = new VBox();
		VBox[] article = new VBox[i];
		VBox articlewrapper = new VBox();
		Button back = new Button("Back");
		ScrollPane displayres = new ScrollPane();
		String[] qparts;
		VBox backwrapper = new VBox();
		ScoreDoc[] filterScoreDosArray = hits.scoreDocs;
		var scene2 = new Scene(main, 700, 400);
		
		try {
			IndexSearcher srch = invertedIndex.createSearcher();
			i = 0;
			
			for (ScoreDoc sd : hits.scoreDocs) 
	        {
				int matches = 0;
	            Document d = srch.doc(sd.doc);
	            files[i] = d.get("File Directory") + d.get("File Name");
	            titles[i] = new Label();
	            titles[i].setStyle("-fx-font-size: 16px;-fx-font-weight: bold;-fx-text-alignment: center;");
	            titles[i].setOnMouseEntered((e)->{
	            	Label hovered = (Label) e.getTarget();
	            	
	            	hovered.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;-fx-text-alignment: center;-fx-underline: true;");
	            });
	            
	            titles[i].setOnMouseExited((e)->{
	            	Label hovered = (Label) e.getTarget();
	            	
	            	hovered.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;-fx-text-alignment: center;");
	            });
	            
	            matchingwords[i] = new Label();
	            matchingwords[i].setStyle("-fx-font-size: 12px;-fx-text-alignment: center;");
	            article[i] = new VBox();
	            
	            titles[i].setText(functionality.getFieldContent(files[i], "Title"));
	            
	            String places = functionality.getFieldContent(files[i], "Places");
	            String people = functionality.getFieldContent(files[i], "People");
	            String body = functionality.getFieldContent(files[i], "Body");
	            String title = titles[i].getText();

	            titles[i].setOnMousePressed((e)->
	            {
	            	articleDisplay.article(title, people, places, body, stage, scene2);
	            });
	            
	            qparts = query.split(" ");
	            matchingwordstext = "Relevant words: ";
	            matches = 0;
	            for(int j = 0 ; j < qparts.length ; j++) 
	            {
	            	if((PreProcessing.preprocessB(body).contains(PreProcessing.preprocessB(qparts[j])))&&(matches < 6)) 
	            	{
	            		matchingwordstext += "..." + qparts[j] + "...";
	            		matches++;
	            	}
	            }
	            matchingwordstext += "\nMatching Score: " + filterScoreDosArray[i].score;
	            
	            matchingwords[i].setText(matchingwordstext);
	            
	            article[i].getChildren().addAll(titles[i], matchingwords[i]);
	            articlewrapper.getChildren().add(article[i]);
	           
				titles[i].setOnMouseClicked((e)->{
	            	
	            });
	            i++;
	        }
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		articlewrapper.setSpacing(20);
		//Back button setup
		backwrapper.getChildren().add(back);
		backwrapper.setAlignment(Pos.TOP_CENTER);
		backwrapper.setPadding(new Insets(7,0,0,7));
        back.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;");
        back.setMinSize(60, 30);
        back.setOnAction((e)->{
        	stage.setScene(scene);
        });
        
		
		main.getChildren().addAll(backwrapper, displayres);
		main.setAlignment(Pos.CENTER);
		main.setSpacing(10);
		main.setPadding(new Insets(5,0,0,0));

		
		displayres.setPrefSize(700, 350);
		displayres.setFitToWidth(true);
		displayres.setContent(articlewrapper);
		articlewrapper.setAlignment(Pos.CENTER);
		
		stage.setScene(scene2);
		if(hits.totalHits.toString().substring(0, 1).equals("0")) 
		{
			Alert error = new Alert(AlertType.INFORMATION, "No results were found for this search. Please go back and try again.");
        	error.showAndWait();
		}
		
	}
}
