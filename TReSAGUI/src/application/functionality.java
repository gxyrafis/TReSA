package application;


//https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

public class functionality {
	
	public static ArrayList<String> getFileNames(String directory)
	{
		ArrayList<String> filenames = new ArrayList<>();
		
		File[] files = new File(directory).listFiles();
		
		for (File file : files) {
		    if (file.isFile()&&file.getAbsolutePath().endsWith(".txt")) {
		        filenames.add(file.getName());
		    }
		}
		
		Collections.sort(filenames, new StringComparator());

		return filenames;
	}
	
	public static ArrayList<String> getIndexedNames()		//https://stackoverflow.com/questions/2311845/is-it-possible-to-iterate-through-documents-stored-in-lucene-index
	{
		ArrayList<String> indexed = new ArrayList<>();
		IndexReader reader;
		try {
			reader = invertedIndex.createReader();
			for (int i=0; i< reader.maxDoc(); i++) {
			    Document doc = reader.document(i);
			    indexed.add(doc.get("File Name"));
			}
			reader.close();
		} catch (IOException e) {
			return null;
		}
		Collections.sort(indexed, new StringComparator());
		return indexed;
	}
	
	public static boolean isNumber(String str, boolean removecomma) 
	{
		if(removecomma) 
		{
			str = str.replace(",", "");
		}
		try {
			int test = Integer.parseInt(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	public static boolean isNumberDobule(String str, boolean removecomma) 
	{
		if(removecomma) 
		{
			str = str.replace(",", "");
		}
		try {
			double test = Double.parseDouble(str);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	public static String getFieldContent(String file, String field) throws FileNotFoundException
	{
		Scanner read = PreProcessing.openReadFile(file);
		String total = "";
		
		while(read.hasNext()) 
		{
			total += read.nextLine() + "\n";
		}
		
		switch(field) 
		{
			case "Title":
				return total.substring(total.indexOf("<TITLE>") + 7, total.indexOf("</TITLE>"));
			case "People":
				return total.substring(total.indexOf("<PEOPLE>") + 8, total.indexOf("</PEOPLE>"));
			case "Places":
				return total.substring(total.indexOf("<PLACES>") + 8, total.indexOf("</PLACES>"));
			case "Body":
				return total.substring(total.indexOf("<BODY>") + 6, total.indexOf("</BODY>"));
			default:
				return null;
		}	
	}
	
	public static String seperateOR(String text) 
	{
		String tmp = text;
		String[] split = text.split(" ");
		text = "(";
		
		if(split.length > 2)
		{
			text = "(" + split[0];
			for(int i = 1 ; i < split.length - 1 ; i++) 
			{
				text += " OR " + split[i] ;
			}
			text = text.trim();
			text += ")";
		}
		else if(split.length == 2) 
		{
			text += split[0] + " OR " + split[1] + ")";
		}
		else 
		{
			text = tmp;
		}
		
		return text;
	}
	
	public static String seperateAND(String text) 
	{
		String tmp = text;
		String[] split = text.split(" ");
		text = "(";
		
		if(split.length > 2)
		{
			text = "(" + split[0];
			for(int i = 1 ; i < split.length - 1 ; i++) 
			{
				text += " AND " + split[i] ;
			}
			text = text.trim();
			text += ")";
		}
		else if(split.length == 2) 
		{
			text += split[0] + " AND " + split[1] + ")";
		}
		else 
		{
			text = tmp;
		}
		
		return text;
	}

}
