package application;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class invertedIndex {
	public static IndexWriter createWriter() throws IOException 	//Create the index writer to create and manage the inverted index.
	{
		FSDirectory dir = FSDirectory.open(Paths.get("Index"));
	    IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
	    //config.setSimilarity(new ClassicSimilarity());	//VSM
	    IndexWriter writer = new IndexWriter(dir, config);
	    return writer;
	}
	
	public static Document documentCreator(String title, String people, String places, String body, String filename, String filedir)	//Create documents to insert to the index based on the provided article structure.
	{
		Document doc = new Document();
		
		doc.add(new StringField("File Name", filename, Field.Store.YES));
		doc.add(new StringField("File Directory", filedir, Field.Store.YES));
		doc.add(new TextField("Title", title, Field.Store.YES));
		doc.add(new TextField("People", people, Field.Store.YES));
		doc.add(new TextField("Places", places, Field.Store.YES));
		doc.add(new TextField("Body", body, Field.Store.YES));
		
		return doc;
	}
	
	public static IndexSearcher createSearcher() throws IOException 
	{
	    Directory dir = FSDirectory.open(Paths.get("Index"));
	    IndexReader reader = DirectoryReader.open(dir);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    //searcher.setSimilarity(new ClassicSimilarity());	//VSM
	    return searcher;
	}
	
	public static IndexReader createReader() throws IOException 
	{
		Directory dir = FSDirectory.open(Paths.get("Index"));
		IndexReader ir = DirectoryReader.open(dir);
		
		return ir;
	}
	
	public static TopDocs findSimilar(IndexSearcher searcher, String qb, String qt, int n) throws IOException, ParseException 
	{
		
		Directory dir = FSDirectory.open(Paths.get("Index"));
	    IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		
		qb = qb.trim();
		qt = qt.trim();
		
		QueryParser qp = new QueryParser("", new StandardAnalyzer());
		String query;
		
		if(!qb.equals("")) 
		{
			query = "(Body:" + functionality.seperateOR(PreProcessing.preprocessB(qb)) + " OR Title:" + functionality.seperateOR(PreProcessing.preprocessB(qt)) + 
					") OR (Body:" + functionality.seperateOR(PreProcessing.preprocessB(qb)) + " AND Title:" + functionality.seperateOR(PreProcessing.preprocessB(qt)) + ")^4";
		}
		else 
		{
			query = " ";
		}
		
		TopDocs similarDocs = indexSearcher.search(qp.parse(query), n);
		
		return similarDocs;
	}
	
	public static TopDocs searchByName(String filename, IndexSearcher searcher) throws Exception
	{
		PhraseQuery.Builder query = new PhraseQuery.Builder();
		query.add(new Term("File Name", filename));
		PhraseQuery pq = query.build();
	    TopDocs hits = searcher.search(pq, 1);
	    return hits;
	}
	
	public static TopDocs searchGeneral(String phrase, int resnum, IndexSearcher searcher) throws IOException, ParseException 	//https://stackoverflow.com/questions/2005084/how-to-specify-two-fields-in-lucene-queryparser
	{
		QueryParser qp = new QueryParser("", new StandardAnalyzer());
		String body = PreProcessing.preprocessB(phrase).trim();
		String query;
		
		if(!body.equals(""))
		{
			query = "(People:" + PreProcessing.preprocessP(phrase).trim() + " OR Places:" + PreProcessing.preprocessP(phrase).trim() + "*" +	//Check if terms exist
					" OR Title:" + functionality.seperateOR(PreProcessing.preprocessT(phrase)).trim() + //in the title, people or places section
					") AND ((Body:" + functionality.seperateOR(PreProcessing.preprocessB(phrase)).trim() +	//Check exclusively body or title
					" OR Title:" + functionality.seperateOR(PreProcessing.preprocessT(phrase)).trim() + ")^4" +	//to see if they contain any word
					" OR (Body:" + functionality.seperateAND(PreProcessing.preprocessB(phrase)).trim() +	" OR Title:" +	//Check exclusively body or title
					functionality.seperateAND(PreProcessing.preprocessB(phrase)).trim() + ")^6)";	//To see if they contain all the words.
		}
		else 
		{
			query = "People:" + PreProcessing.preprocessP(phrase).trim() + " OR Places:" + PreProcessing.preprocessP(phrase).trim() + "*" +	//Check if terms exist
					" OR Title:" + functionality.seperateOR(PreProcessing.preprocessT(phrase)).trim(); //in the title, people or places section
		}
		
		TopDocs hits = searcher.search(qp.parse(query), resnum);
		
		return hits;
	}
	
	public static TopDocs searchFields(javafx.scene.control.TextField[] searchf, int n) throws IOException, ParseException
	{
		QueryParser qp = new QueryParser("", new StandardAnalyzer());
		String query = "";
		
		if(!searchf[0].getText().equals("")) 
		{
			System.out.println(searchf[0].getText());
			query += "(Title:" + PreProcessing.preprocessT(searchf[0].getText()).trim() + ") AND ";
		}
		if(!searchf[1].getText().equals("")) 
		{
			System.out.println(searchf[1].getText());
			query += "(Body:" + PreProcessing.preprocessT(searchf[1].getText()).trim() + ") AND ";
		}
		if(!searchf[2].getText().equals("")) 
		{
			System.out.println(searchf[2].getText());
			query += "(People:" + PreProcessing.preprocessT(searchf[2].getText()).trim() + ") AND ";
		}
		if(!searchf[3].getText().equals("")) 
		{
			System.out.println(searchf[3].getText());
			query += "(Places:" + PreProcessing.preprocessT(searchf[3].getText()).trim() + "*)";
		}
		
		Directory dir = FSDirectory.open(Paths.get("Index"));
	    IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs hits = searcher.search(qp.parse(query), n);
		
		return hits;
	}
	
	public static TopDocs searchBoolean(String query, int n) throws IOException, ParseException 
	{
		IndexSearcher searcher = createSearcher();
		QueryParser qp = new QueryParser("", new StandardAnalyzer());
		String[] words = query.replace("(", "( ").replace(")", " )").split(" ");
		String title = "";
		String people = "";
		String places = "";
		String body = "";
		ArrayList<String> bodystop = new ArrayList<>();
		String q = "";
		boolean bdy = false;
		boolean ttl = false;
		boolean plcs = false;
		boolean ppl = false;
		
		for(int i = 0 ; i < words.length ; i++) 
		{
			if(words[i].equals("Body:")) 
			{
				bdy = true;
				i++;
				while((i < words.length )&&(!words[i].equals("Title:"))&&(!words[i].equals("People:"))&&(!words[i].equals("Places:"))) 
				{
					if(words[i].equals("OR")) 
					{
						body += "orororororororora ";
					}
					else if(words[i].equals("AND")) 
					{
						body += "andandandando ";
					}
					else if(words[i].equals("NOT")) 
					{
						body += "notnotnontnota ";
					}
					else if(words[i].equals("(")) 
					{
						body += "popenpopenpopen ";
					}
					else if(words[i].equals(")")) 
					{
						body += "pclopclopclo ";
					}
					else 
					{
						body += words[i] + " ";
					}
					i++;
				}
				i--;
			}
			else if(words[i].equals("Title:")) 
			{
				ttl = true;
				i++;
				while((i < words.length )&&(!words[i].equals("Body:"))&&(!words[i].equals("People:"))&&(!words[i].equals("Places:"))) 
				{
					if(words[i].equals("(")) 
					{
						title += "popenpopenpopen ";
					}
					else if(words[i].equals(")")) 
					{
						title += "pclopclopclo ";
					}
					else 
					{
						title += words[i] + " ";
					}
					i++;
				}
				i--;
			}
			else if(words[i].equals("People:")) 
			{
				ppl = true;
				i++;
				while((i < words.length )&&(!words[i].equals("Title:"))&&(!words[i].equals("Body:"))&&(!words[i].equals("Places:"))) 
				{
					if(words[i].equals("(")) 
					{
						people += "popenpopenpopen ";
					}
					else if(words[i].equals(")")) 
					{
						people += "pclopclopclo ";
					}
					else 
					{
						people += words[i] + " ";
					}
					i++;
				}
				i--;
			}
			else if(words[i].equals("Places:")) 
			{
				plcs = true;
				i++;
				while((i < words.length )&&(!words[i].equals("Title:"))&&(!words[i].equals("People:"))&&(!words[i].equals("Body:"))) 
				{
					if(words[i].equals("(")) 
					{
						places += "popenpopenpopen ";
					}
					else if(words[i].equals(")")) 
					{
						places += "pclopclopclo ";
					}
					else 
					{
						places += words[i] + " ";
					}
					i++;
				}
				i--;
			}
			else 
			{
				q += words[i] + " ";
			}
		}
		if(ttl) 
		{
			q += "Title:" + PreProcessing.preprocessT(title);
			q = q.replace("and", "AND").replace("or", "OR").replace("not", "NOT").replace("popenpopenpopen", "(").replace("pclopclopclo", ")");
		}
		if(bdy) 
		{
			body = PreProcessing.preprocessB(body);
			body = body.replace("orororororororora", "OR").replace("andandandando", "AND").replace("notnotnontnota", "NOT").replace("popenpopenpopen", "(").replace("pclopclopclo", ")");
			q += "Body:" + body;
		}
		if(plcs) 
		{
			q += "Places:" + PreProcessing.preprocessP(places);
			q = q.trim();
			
			q = q.replace("and", "AND").replace("or", "OR").replace("not", "NOT").replace("popenpopenpopen", "(").replace("pclopclopclo", ")");
		}
		if(ppl) 
		{
			q += "People:" + PreProcessing.preprocessP(people);
			q = q.replace("and", "AND").replace("or", "OR").replace("not", "NOT").replace("popenpopenpopen", "(").replace("pclopclopclo", ")");
		}
		
		System.out.println(q);
		TopDocs hits = searcher.search(qp.parse(q), n);
		return hits;
	}
}
