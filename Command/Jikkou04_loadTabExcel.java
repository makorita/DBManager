import java.io.*;
import java.util.*;

public class Jikkou04_loadTabExcel{
	public static void main(String args[]) throws IOException,FileNotFoundException{
		String srcFile=args[0];
		String sheetName=args[1];
		
		BufferedReader br = new BufferedReader(new FileReader("env.txt"));
		String dbName=br.readLine();
		//System.out.println(dbName);
		dbName=dbName.replace("dbName:","");
		br.close();
		
		DBManager dbm=new DBManager();
		if(dbm.existsDB(dbName))dbm.loadDB(dbName);
		NodeLoader nl=new NodeLoader(dbm);
		nl.loadTabExcel(srcFile,sheetName);
		dbm.saveDB(dbName);
	}
}