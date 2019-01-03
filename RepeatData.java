import java.io.*;

public abstract class RepeatData{
	protected String replaceStr;
	
	public String getReplaceStr(){
		return replaceStr;
	}
	
	public void setReplaceStr(String replaceStr){
		this.replaceStr=replaceStr;
	}
	
	public abstract void init();
	public abstract String getType();
	public abstract String get();
	public abstract void next();
	public abstract String getStrExp();
}
