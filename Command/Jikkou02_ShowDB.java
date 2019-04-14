import java.io.*;
import java.util.*;

public class Jikkou02_ShowDB{
	public static void main(String args[]) throws IOException,FileNotFoundException{
		String currentPath="_";
		String dbName=null;
		BufferedReader br = new BufferedReader(new FileReader("env.txt"));
		String line;
		while ((line = br.readLine()) != null) {
//			System.out.println(line);
			if(line.matches("currentPath:.*"))currentPath=line.replace("currentPath:","");
			if(line.matches("dbName:.*"))dbName=line.replace("dbName:","");
		}
		br.close();
		
		//currentPathÇÃêÆå`
		if(currentPath.equals("_"))currentPath=null;
		else currentPath=currentPath.replaceFirst("_","");

		DBManager dbm=new DBManager();
		dbm.loadDB(dbName);
		//System.out.println(dbm.getTreeStr());
		System.out.println(dbm.getTreeStr(currentPath));
		//System.out.println(rootPath);
		
	}
}