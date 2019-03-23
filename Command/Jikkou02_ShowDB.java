import java.io.*;
import java.util.*;

public class Jikkou02_ShowDB{
	public static void main(String args[]) throws IOException,FileNotFoundException{
		String rootPath=null;
		if(args.length>0)rootPath=args[0];
		
		BufferedReader br = new BufferedReader(new FileReader("env.txt"));
		String dbName=br.readLine();
		dbName=dbName.replace("dbName:","");
		br.close();
		
		DBManager dbm=new DBManager();
		dbm.loadDB(dbName);
		System.out.println(dbm.getTreeStr(rootPath));
	}
}