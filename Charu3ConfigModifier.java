import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Charu3ConfigModifier extends Modifier{
	private static final String CHARU3_DB_PATH="myData";
	private String charu3ConfigPath;

	public Charu3ConfigModifier(String charu3ConfigPath){
		this.charu3ConfigPath=charu3ConfigPath;
		setDbPath(CHARU3_DB_PATH);
	}

	public void doAfterLoad(){
	}
	
	public void writeCharu3Config(){
		LinkedList<String> charu3Config=new LinkedList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(charu3ConfigPath), "UTF-16"));
			String line;
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				if(line.equals("_|CHARUTXT-MAC|_HotKey=alt + m"))break;
				charu3Config.add(line);
			}
			br.close();
			
			charu3Config.add("_|CHARUTXT-MAC|_HotKey=alt + m");
			charu3Config.add(getChar3ConfigStr(getDbPath()));
			
			PrintWriter wr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(charu3ConfigPath),"UnicodeLittle")));
			for(String curStr:charu3Config){
				//System.out.println(curStr);
				wr.println(curStr);
			}
			wr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}