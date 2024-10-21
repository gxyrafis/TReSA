package application;
	


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			mainPage.create(primaryStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		/*ArrayList<String> arr= new ArrayList<String>();
		 for(int i = 0 ; i < 101 ; i++) 
		 {
			 arr.add("Article" + i +".txt");
		 }
		 
		 List<Document> docs = new ArrayList<>();
		 IndexWriter iw = null;
		 
		try 
		{
			docs = PreProcessing.articlesEditor(arr);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if(docs != null && iw != null) 
		{
			try {
				iw.deleteAll();
				iw.addDocuments(docs);
				iw.commit();
				iw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			IndexSearcher srch = invertedIndex.createSearcher();
			TopDocs topdocs = invertedIndex.searchByTitle(PreProcessing.preprocessT("NATIONAL AVERAGE PRICES FOR FARMER-OWNED RESERVE"), srch);
			
			System.out.println("Total hits: " + topdocs.totalHits);
			for (ScoreDoc sd : topdocs.scoreDocs) 
	        {
	            Document d = srch.doc(sd.doc);
	            System.out.println(String.format(d.get("File Name")));
	            System.out.println(String.format(d.get("Title")));
	        }
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		launch(args);
	}
}
