package application;

import java.util.Comparator;

public class StringComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		String tmp1, tmp2;
		Boolean comparable = true;
		
		tmp1 = o1.toLowerCase();
		tmp2 = o2.toLowerCase();
		
		if(tmp1.contains("article")&&tmp2.contains("article")) 
		{
			tmp1 = o1.substring(7, o1.indexOf('.'));
			tmp2 = o2.substring(7, o2.indexOf('.'));
			
			int n1 = Integer.parseInt(tmp1);
			int n2 = Integer.parseInt(tmp2);
			
			if(n1 > n2) 
			{
				return 1;
			}
			else if(n1 < n2) 
			{
				return -1;
			}
			else 
			{
				return 0;
			}
		}
		else 
		{
			return o1.compareTo(o2);
		}
	}

}
