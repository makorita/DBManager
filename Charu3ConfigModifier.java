import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Charu3ConfigModifier extends Modifier{
	public static final String CHARU3_CONFIG_PATH="F:/application/[クリップボード拡張]c3030301U/charu3.charutxt";

	public Charu3ConfigModifier(){
		
	}

	public void doAfterLoad(){
	}
	
	public void writeCharu3Config(String key){
		LinkedList<String> charu3Config=new LinkedList<String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(CHARU3_CONFIG_PATH), "UTF-16"));
			String line;
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				if(line.equals("_|CHARUTXT-MAC|_HotKey=alt + m"))break;
				charu3Config.add(line);
			}
			br.close();
			
			charu3Config.add("_|CHARUTXT-MAC|_HotKey=alt + m");
			charu3Config.add(getChar3ConfigStr(key));
			
			PrintWriter wr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CHARU3_CONFIG_PATH),"UnicodeLittle")));
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