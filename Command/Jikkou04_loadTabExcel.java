import java.io.*;
import java.util.*;

public class Jikkou04_loadTabExcel{
	public static void main(String args[]) throws IOException,FileNotFoundException{
		String srcFile=args[0];
		String sheetName=args[1];
		
		//dbNameŽæ“¾
		String dbName=null;
		{
			BufferedReader br = new BufferedReader(new FileReader("env.txt"));
			String line;
			LinkedList<String> envList=new LinkedList<String>();
			while ((line = br.readLine()) != null) {
	//			System.out.println(line);
				if(line.matches("dbName:.*"))dbName=line.replace("dbName:","");
			}
			br.close();
		}
		
		DBManager dbm=new DBManager();
		if(dbm.existsDB(dbName))dbm.loadDB(dbName);
		NodeLoader nl=new NodeLoader(dbm);
		nl.loadTabExcel(srcFile,sheetName);
		dbm.saveDB(dbName);
	}
}