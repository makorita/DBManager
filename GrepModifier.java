import java.io.*;
import java.util.*;
import java.util.regex.*;

public class GrepModifier extends Modifier{
	private LinkedList<String> grepList;
	private LinkedList<String> jogaiList;
	
	public GrepModifier(){
		grepList=new LinkedList<String>();
		jogaiList=new LinkedList<String>();
	}
	
	public void addGrepWord(String grepStr){
		grepList.add(grepStr);
	}
	
	public void addJogaiWord(String jogaiStr){
		jogaiList.add(jogaiStr);
	}
	
	public void grep(){
		LinkedList<String> aftList=new LinkedList<String>();
		for(String curStr:getTargetList()){
			for(String curGrepStr:grepList){
				if(curStr.matches(".*"+curGrepStr+".*")){
					aftList.add(curStr);
					break;
				}
			}
		}
		setTargetList(aftList);
	}
	
	public void matchGrep(){
		LinkedList<String> aftList=new LinkedList<String>();
		for(String curStr:getTargetList()){
			for(String curGrepStr:grepList){
				Pattern p=Pattern.compile(curGrepStr);
				Matcher m=p.matcher(curStr);
				while(m.find()){
					aftList.add(m.group());
				}
			}
		}
		setTargetList(aftList);
	}

	public void ungrep(){
		LinkedList<String> aftList=new LinkedList<String>();
		LABEL:for(String curStr:getTargetList()){
			for(String curJogaiStr:jogaiList){
				if(curStr.matches(".*"+curJogaiStr+".*"))continue LABEL;
			}
			aftList.add(curStr);
		}
		setTargetList(aftList);
	}
}