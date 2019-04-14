import java.io.*;
import java.util.*;

public class Jikkou01_cdb{
	public static void main(String args[]) throws IOException{
		String currentPath="_";
		BufferedReader br = new BufferedReader(new FileReader("env.txt"));
		String line;
		LinkedList<String> envList=new LinkedList<String>();
		while ((line = br.readLine()) != null) {
//			System.out.println(line);
			if(line.matches("currentPath:.*")){
				currentPath=line.replace("currentPath:","");
			}else envList.add(line);
		}
		br.close();

		//currentPath:ÇÃçXêV
		String path=null;
		if(args.length==0){
			System.out.println(currentPath);
			System.exit(0);
		}
		path=args[0];
//		currentPath="_test1";
		System.out.println(path);
		
		if(path.equals("..")){
			String[] word=currentPath.split("_");
			if(word.length<=2)currentPath="_";
			else{
				currentPath=null;
				for(int i=1;i<word.length-1;i++){
					if(currentPath==null)currentPath="_"+word[i];
					else currentPath+="_"+word[i];
				}
			}
		}else{
			if(currentPath.equals("_"))currentPath+=path;
			else currentPath+="_"+path;
		}
		//System.out.println(currentPath);
		
		PrintWriter wr=new PrintWriter(new FileWriter("env.txt"));
		for(String curStr:envList){
			wr.println(curStr);
		}
		wr.println("currentPath:"+currentPath);
		System.out.println(currentPath+":Ç…à⁄ìÆÇµÇ‹ÇµÇΩ");
		wr.close();
	}
}