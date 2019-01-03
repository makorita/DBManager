import java.io.*;
import java.util.*;

public class RepeatNumericData extends RepeatData{
	private int startValue;
	private int maxValue;
	private int delta;
	private int zeroPad;
	
	private int curValue;
	
	public RepeatNumericData(String replaceStr,int startValue,int maxValue,int delta,int zeroPad){
		setReplaceStr(replaceStr);
		this.startValue=startValue;
		this.maxValue=maxValue;
		this.delta=delta;
		this.zeroPad=zeroPad;
		
		init();
	}
	
	public void init(){
		curValue=startValue;
	}
	
	public String getType(){
		return "NUMERIC";
	}
	
	public int getStartValue(){
		return startValue;
	}
	
	public int getMaxValue(){
		return maxValue;
	}
	
	public int getDelta(){
		return delta;
	}
	
	public int getZeroPad(){
		return zeroPad;
	}
	
	public String get(){
		return String.format("%0"+zeroPad+"d",curValue);
	}
	
	public void next(){
		if(curValue>=maxValue)init();
		else curValue+=delta;
	}
	
	public String getStrExp(){
		String returnStr=getReplaceStr()+","+String.format("%0"+zeroPad+"d",startValue)+","+maxValue+","+delta;
		
		return returnStr;
	}
}
