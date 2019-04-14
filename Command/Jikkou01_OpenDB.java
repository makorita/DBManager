import java.io.*;
import java.util.*;

public class Jikkou01_OpenDB{
	public static void main(String args[]) throws IOException{
		String dbName=args[0];
		BufferedReader br = new BufferedReader(new FileReader(env.txt));
		String line;
		LinkedList<String> envList=new LinkedList<String>();
		while ((line = br.readLine()) != null) {
//			System.out.println(line);
			if(line.matches("dbName:.*"))continue;
			envList.add(line);
		}
		br.close();

		PrintWriter wr=new PrintWriter(new FileWriter("env.txt"));
		for(String curStr:envList){
			wr.println(curStr);
		}
		wr.println("dbName:"+dbName);
		System.out.println(dbName+":ÇÉIÅ[ÉvÉìÇµÇ‹ÇµÇΩ");
		wr.close();
	}
}