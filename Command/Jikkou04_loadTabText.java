import java.io.*;
import java.util.*;

public class Jikkou04_loadTabText{
	public static void main(String args[]) throws IOException,FileNotFoundException{
		String srcFile=args[0];
		
		//dbNameéÊìæ
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
		
		//É\Å[ÉXì«Ç›çûÇ›
		LinkedList<String> srcList=new LinkedList<String>();
		{
			BufferedReader br = new BufferedReader(new FileReader(srcFile));
			String line;
			LinkedList<String> envList=new LinkedList<String>();
			while ((line = br.readLine()) != null) {
	//			System.out.println(line);
				srcList.add(line);
			}
			br.close();
		}
		
		DBManager dbm=new DBManager();
		if(dbm.existsDB(dbName))dbm.loadDB(dbName);
		NodeLoader nl=new NodeLoader(dbm);
		nl.loadTabList(srcList);
		dbm.saveDB(dbName);
	}
}