package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;


public class PreProcessing {
	public static List<Document> articlesEditor(ArrayList<String> articles, String dir) throws IOException 
	{
		if(!articles.isEmpty()) 
		{
			List<Document> docs = new ArrayList<>();
			Scanner read;
			
			for(String str : articles) 
			{
				try 
				{
					read = openReadFile(dir + str);	//Open article reader
					String categoryPrev = ""; //Previous found category to switch to in case of random <Somethingsomething>
					String category = "";	//Places, people, title, body.
					String line = "";	//Line from txt file
					String content = "";	//Article content
					Boolean ready = false;	//Content is ready. Aka section closing condition has been met
					Boolean completed = true;	//Document is completed
					String[] doc = new String[4];	//Different sections
					
					for(int i = 0 ; i < 4 ; i++) 
					{
						doc[i] = "";
					}
					
					while(read.hasNext()) 	//Break down article into parts and edit accordingly
					{
						line = read.nextLine();
						if(!line.isEmpty()){
							if((line.charAt(0) == '<')&&(line.charAt(1) != '/')&&(line.contains(">"))) 
							{
								categoryPrev = category;
								category = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
								
								if((!category.equals("TITLE"))&&(!category.equals("PEOPLE"))&&(!category.equals("PLACES"))&&(!category.equals("BODY"))) 
								{
									category = categoryPrev;
								}
								
								if(line.contains("</" + category + ">"))
								{
									content = line.substring(line.indexOf(">") + 1, line.indexOf("</" + category + ">"));
									ready = true;
								}
								else 
								{
									content = line.substring(line.indexOf(">") + 1);
									ready = false;
								}
							}
							else if((!category.equals("")) && line.contains("</" + category + ">")) 
							{
								ready = true;
							}
							else 
							{
								content += "\n" + line;
							}
						
						}
						
						if(ready) 
						{
							switch(category) 
							{
								case "PLACES":
									doc[0] = PreProcessing.preprocessP(content);
									if(doc[0].equals("")) 
									{
										doc[0] += " ";
									}
									break;
								case "PEOPLE":
									doc[1] = PreProcessing.preprocessP(content);
									if(doc[1].equals("")) 
									{
										doc[1] += " ";
									}
									break;
								case "TITLE":
									doc[2] = PreProcessing.preprocessT(content);
									if(doc[2].equals("")) 
									{
										doc[2] += " ";
									}
									break;
								case "BODY":
									doc[3] = PreProcessing.preprocessB(content);
									if(doc[3].equals("")) 
									{
										doc[3] += " ";
									}
									break;
								default:	//Does nothing, means it found something irrelevant surrounded by <>
									break;
							}
							
							for(int i = 0 ; i < 4 ; i++) 
							{
								if(doc[i].equals(""))
								{
									completed = false;
								}
							}
							
							if(completed) 
							{
								docs.add(invertedIndex.documentCreator(doc[2], doc[1], doc[0], doc[3], str, dir));
								/*System.out.println(doc[2]);
								System.out.println(doc[1]);
								System.out.println(doc[0]);
								System.out.println(doc[3]);
								System.out.println(str);*/
							}
							else 
							{
								completed = true;
							}
							
							/*try 
							{
								PreProcessing.articleWriter(str, category, content, fexists);
								fexists = false;
							}
							catch(IOException e) 
							{
								System.out.println("Error while creating a file to store the article.");
							}*/
							
							
							ready = false;
						}
						
					}
				}
				catch(FileNotFoundException e) 
				{
					System.out.println(str + " was not found in our collection of articles.");
				}
			}
			
			return docs;
		}
		return null;
	}
	
	public static String preprocessP(String content) throws IOException	//People and Places. Only case folding and punctuation removal
	{
        Analyzer analyzer = CustomAnalyzer.builder().withTokenizer("standard").addTokenFilter("lowercase").build();
        String text = "";
        for (String str : analyze(content, analyzer)) 
        {
            text += str + " ";
        }
        
        String[] tmp = text.split(" ");
        text = "";
        
        for(String str : tmp) 
        {
        	if(functionality.isNumber(str, true)) 
        	{
        		str = str.replace(",", "");
        	}
        	else if(functionality.isNumberDobule(str, false)) 
        	{
        		
        	}
        	else 
        	{
        		str = str.replace(",", "").replace(".", "");
        	}
        	text += str + " ";
        }
		return text;
	}
	
	public static String preprocessT(String content) throws IOException 	//Titles. Case folding, punctuation removal and stemming
	{
        Analyzer analyzer = CustomAnalyzer.builder().withTokenizer("standard").addTokenFilter("lowercase").addTokenFilter("porterstem").build();
        String text = "";
        for (String str : analyze(content, analyzer)) 
        {
            text += str + " ";
        }
        
        String[] tmp = text.split(" ");
        text = "";
        
        for(String str : tmp) 
        {
        	if(functionality.isNumber(str, true)) 
        	{
        		str = str.replace(",", "");
        	}
        	else if(functionality.isNumberDobule(str, false)) 
        	{
        		
        	}
        	else 
        	{
        		str = str.replace(",", "").replace(".", "");
        	}
        	text += str + " ";
        }
		
		return text;
	}
	
	public static String preprocessB(String content) throws IOException	//Body. Case folding, punctuation removal, stemming and stop word removal
	{
		
        Analyzer analyzer = CustomAnalyzer.builder().withTokenizer("standard").addTokenFilter("lowercase").addTokenFilter("porterstem").addTokenFilter("stop").build();
        String text = "";
        for (String str : analyze(content, analyzer)) 
        {
            text += str + " ";
        }
        
        String[] tmp = text.split(" ");
        text = "";
        
        for(String str : tmp) 
        {
        	if(functionality.isNumber(str, true)) 
        	{
        		str = str.replace(",", "");
        	}
        	else if(functionality.isNumberDobule(str, false)) 
        	{
        		
        	}
        	else 
        	{
        		str = str.replaceAll("[.,()']", "").replaceAll("[-/_]", " ");
        	}
        	text += str + " ";
        }
		return text;
	}
	
	/*private static void articleWriter(String articleName, String category, String content, Boolean fexists) throws IOException
	{
		File article = new File("Articles_edited\\" + articleName);
		PrintWriter output;
		
		if(!article.exists()) 
		{
			article.createNewFile();
			output = new PrintWriter(article);
		}
		else if(!fexists)
		{
			output = new PrintWriter(new FileWriter(article, true));
		}
		else
		{
			output = new PrintWriter(new FileWriter(article, false));
		}
		
		if(category.equalsIgnoreCase("body")) 
		{
			output.print("<" + category + ">" + content + "</" + category + ">");
		}
		else 
		{
			output.println("<" + category + ">" + content + "</" + category + ">");
		}
		output.close();
	}*/
	
	public static Scanner openReadFile(String fileName) throws FileNotFoundException
	{
		Scanner input = new Scanner(new FileInputStream(fileName));
		return input;
	}
	
    public static List<String> analyze(String text, Analyzer analyzer) throws IOException	//Found on StackOverflow, can't remember exact post.
    {	
        List<String> result = new ArrayList<String>();
        TokenStream tokenStream = analyzer.tokenStream("FIELD_NAME", text);
        CharTermAttribute attr = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            result.add(attr.toString());
        }
        return result;
    }
}
