import java.io.*;
import java.util.*;

public class RepeatModifier extends Modifier{
	private static final String REPEAT_PATH="repeat";
	
	private int globalRepeatNum;
	private TreeMap<String,RepeatData> repeatMap;
	
	public RepeatModifier(){
		globalRepeatNum=1;
		repeatMap=new TreeMap<String,RepeatData>();
	}
	
	public int getGlobalRepeatNum(){
		return globalRepeatNum;
	}
	
	public void setGlobalRepeatNum(int globalRepeatNum){
		this.globalRepeatNum=globalRepeatNum;
		put(REPEAT_PATH+Modifier.DIVIDE_STR+"globalRepeatNum",String.valueOf(globalRepeatNum));
	}
	
	public void clearRepeatMap(){
		repeatMap.clear();
		clear(REPEAT_PATH);
	}

	public void addRepeatData(RepeatData curRepeat){
		repeatMap.put(curRepeat.getReplaceStr(),curRepeat);
		//DB連携
		if(curRepeat.getType().equals("NUMERIC")){
			put(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr()+Modifier.DIVIDE_STR+"startValue",String.valueOf(((RepeatNumericData)curRepeat).getStartValue()));
			put(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr()+Modifier.DIVIDE_STR+"maxValue",String.valueOf(((RepeatNumericData)curRepeat).getMaxValue()));
			put(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr()+Modifier.DIVIDE_STR+"delta",String.valueOf(((RepeatNumericData)curRepeat).getDelta()));
			put(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr()+Modifier.DIVIDE_STR+"zeroPad",String.valueOf(((RepeatNumericData)curRepeat).getZeroPad()));
			put(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr()+Modifier.DIVIDE_STR+"type","NUMERIC");
		}else if(curRepeat.getType().equals("LIST")){
			if(existsKey(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr()))remove(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr());
			Iterator<String> it=((RepeatListData)curRepeat).iterator();
			int index=0;
			while(it.hasNext()){
				String curStr=it.next();
				put(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr()+Modifier.DIVIDE_STR+"リスト"+String.format("%02d",index++),curStr);
			}
			put(REPEAT_PATH+Modifier.DIVIDE_STR+curRepeat.getReplaceStr()+Modifier.DIVIDE_STR+"type","LIST");
		}
	}
	
	public void doAfterLoad(){
		if(!existsKey(REPEAT_PATH))return;
		Iterator<Node> it=iterator(REPEAT_PATH);
		while(it.hasNext()){
			Node tmpNode=it.next();
			if(tmpNode.getName().equals("globalRepeatNum"))globalRepeatNum=Integer.parseInt(tmpNode.getValue("globalRepeatNum"));
			else if(tmpNode.getValue(tmpNode.getName()+Modifier.DIVIDE_STR+"type").equals("NUMERIC")){
				String replaceStr=tmpNode.getName();
				int startValue=Integer.parseInt(tmpNode.getValue(tmpNode.getName()+Modifier.DIVIDE_STR+"startValue"));
				int maxValue=Integer.parseInt(tmpNode.getValue(tmpNode.getName()+Modifier.DIVIDE_STR+"maxValue"));
				int delta=Integer.parseInt(tmpNode.getValue(tmpNode.getName()+Modifier.DIVIDE_STR+"delta"));
				int zeroPad=Integer.parseInt(tmpNode.getValue(tmpNode.getName()+Modifier.DIVIDE_STR+"zeroPad"));
				RepeatNumericData tmpNumeric=new RepeatNumericData(replaceStr,startValue,maxValue,delta,zeroPad);
				repeatMap.put(tmpNumeric.getReplaceStr(),tmpNumeric);	//setするとDB連携される
			}else if(tmpNode.getValue(tmpNode.getName()+Modifier.DIVIDE_STR+"type").equals("LIST")){
				String replaceStr=tmpNode.getName();
				RepeatListData tmpListData=new RepeatListData(replaceStr);
				Iterator<Node> childIt=tmpNode.iterator(replaceStr);
				while(childIt.hasNext()){
					Node childNode=childIt.next();
					if(!childNode.getName().matches("リスト\\d{2}"))continue;
					tmpListData.add(childNode.getValue());
					repeatMap.put(tmpListData.getReplaceStr(),tmpListData);	//setするとDB連携される
				}
			}
		}
	}
	
	public void allRepeat(){
		LinkedList<String> aftList=getRepeated(getTargetList(),getGlobalRepeatNum());
		setTargetList(aftList);
	}
	
	public void partialRepeat(){
		LinkedList<String> aftList=new LinkedList<String>();	//編集＆戻り用リスト
		LinkedList<String> partialList=null;
		int repNum=0;
		String repeatStr=null;
		String mode="NONE";
		for(String curStr:getTargetList()){
			if(mode.equals("NONE") && curStr.matches("<repeat:\\d+>")){
				partialList=new LinkedList<String>();
				String tmpStr=curStr;
				tmpStr=tmpStr.replaceFirst("<repeat:","");
				tmpStr=tmpStr.replaceFirst(">","");
				repNum=Integer.parseInt(tmpStr);
				mode="REPEAT";
			}else if(mode.equals("REPEAT") && curStr.matches("<repeat:end>")){
				LinkedList<String> partialReplacedList=getRepeated(partialList,repNum);
				aftList.addAll(partialReplacedList);
				mode="NONE";
			}else if(mode.equals("REPEAT")){
				partialList.add(curStr);
			}else if(mode.equals("NONE") && curStr.matches("<while:.*>")){
				partialList=new LinkedList<String>();
				repeatStr=curStr;
				repeatStr=repeatStr.replaceFirst("<while:","");
				repeatStr=repeatStr.replaceFirst(">","");
				mode="WHILE";
			}else if(mode.equals("WHILE") && curStr.matches("<while:end>")){
				LinkedList<String> partialReplacedList=getWhileList(partialList,repeatStr);
				aftList.addAll(partialReplacedList);
				mode="NONE";
			}else if(mode.equals("WHILE")){
				partialList.add(curStr);
			}else if(mode.equals("NONE")){
				aftList.add(curStr);
			}
		}
		
		setTargetList(aftList);
	}
	
	private void initAllRepeatData(){
		for (String key : repeatMap.keySet()) {
			repeatMap.get(key).init();
		}
	}
	
	private LinkedList<String> getRepeated(LinkedList<String> srcList,int repNum){
		initAllRepeatData();
		LinkedList<String> returnList=new LinkedList<String>();
		for(int i=0;i<repNum;i++){
			LinkedList<String> aftList=replaceRepeat(srcList);
			returnList.addAll(aftList);
		}
		
		return returnList;
	}
	
	private LinkedList<String> getWhileList(LinkedList<String> srcList,String repeatStr){	//whileの連番は部分リストの中では使えない
		initAllRepeatData();
		LinkedList<String> returnList=new LinkedList<String>();
		while(true){
			String curStr=repeatStr;
			for (String key : repeatMap.keySet()) {
				RepeatData curRepeatData=repeatMap.get(key);
				if(curStr.matches(".*"+key+".*")){	//マッチした場合のみ置換してnext()実行
//					System.out.println("bef:"+curStr);
					curStr=curStr.replaceAll(curRepeatData.getReplaceStr(),curRepeatData.get());
//					System.out.println("aft:"+curStr);
					curRepeatData.next();
				}
			}
			if(!existsKey(curStr))break;	//終了条件
			
			LinkedList<String> aftList=replaceRepeat(srcList);
			returnList.addAll(aftList);
		}
		
		return returnList;
	}
	
	private LinkedList<String> replaceRepeat(LinkedList<String> srcList){
		//編集用リストへのコピー
		LinkedList<String> editList=new LinkedList<String>();
		for(String curStr:srcList){
			editList.add(curStr);
		}
		
		//連番・リスト置換処理
		for (String key : repeatMap.keySet()) {
			RepeatData curRepeat=repeatMap.get(key);
			boolean replaceCheck=false;	//これをチェックしないとwhile用の連番も進められてしまう
			for(int i=0;i<editList.size();i++){
				String curStr=editList.get(i);
//				System.out.println(curStr+","+curRepeat.getReplaceStr()+","+curRepeat.get());
				if(curStr.matches(".*"+curRepeat.getReplaceStr()+".*")){
					curStr=curStr.replaceAll(curRepeat.getReplaceStr(),curRepeat.get());
					replaceCheck=true;
				}
//				System.out.println("aft:"+curStr);
				editList.set(i,curStr);
			}
			if(replaceCheck)curRepeat.next();
		}
		
		return editList;
	}
}