import java.io.*;
import java.util.*;
import java.util.regex.*;

public class IPLoader{
	private IPManager ipm;
	private LinkedList<File> configList;
	
	public IPLoader(IPManager ipm){
		this.ipm=ipm;
		configList=new LinkedList<File>();
	}
	
	public void recursiveCheck(File curDir){
		File[] childList=curDir.listFiles();
		for(File curFile:childList){
			if(curFile.isDirectory()){
				if(curFile.getName().matches(".*old.*"))continue;
				if(curFile.getName().matches(".*廃止.*"))continue;
				//System.out.println(curFile.getName());
				recursiveCheck(curFile);
			}else if(curFile.isFile()){
				if(!curFile.getName().matches(".*\\.txt") && !curFile.getName().matches(".*\\.log"))continue;
				//System.out.println(curFile.getName());
				configList.add(curFile);
			}
		}
	}
	
	public void loadRawIP(String patternStr) throws FileNotFoundException,IOException{	//patternStrにマッチするIPを取得する
		TreeMap<String,TreeSet<String>> ipMap=new TreeMap<String,TreeSet<String>>();
		Pattern p = Pattern.compile(patternStr);
		for(File curFile:configList){
			System.out.println(curFile.getName());
			BufferedReader br = new BufferedReader(new FileReader(curFile));
			String line=null;
			while ((line = br.readLine()) != null) {
				Matcher m = p.matcher(line);
				while (m.find()) {
					String curIP=m.group();
					if(ipMap.containsKey(curIP)){
						TreeSet<String> curFileNameSet=ipMap.get(curIP);
						curFileNameSet.add(curFile.getName());
					}else{
						TreeSet<String> curFileNameSet=new TreeSet<String>();
						curFileNameSet.add(curFile.getName());
						ipMap.put(curIP,curFileNameSet);
					}
				}
			}
			br.close();
			
			//break;
		}
		
		for (String curIP : ipMap.keySet()) {
			//System.out.println(curIP);
			TreeSet<String> curFileNameSet=ipMap.get(curIP);
			String curExplain=null;
			for(String curFileName:curFileNameSet){
				if(curExplain==null)curExplain=curFileName+"\n";
				else curExplain+=curFileName+"\n";
			}
			ipm.put(curIP+DBManager.DIVIDE_STR+"ip",curIP);
			ipm.put(curIP+DBManager.DIVIDE_STR+"explain",curExplain);
		}
	}
	
	public void checkIfIP(boolean updateFlag) throws FileNotFoundException,IOException{
		for(File curFile:configList){
			System.out.println(curFile.getName());
			BufferedReader br = new BufferedReader(new FileReader(curFile));
			String line=null;
			String hostname=null;
			String i_f=null;
			while ((line = br.readLine()) != null) {
				//ホスト名取得
				if(line.matches("hostname .*")){
					hostname=line;
					hostname=hostname.replace("hostname ","");
				}
				if(hostname==null)continue;
				
				//IF情報取得
				if(line.matches("interface .*")){
					i_f=line;
					i_f=i_f.replace("interface ","");
					if(i_f.matches("FastEthernet.*"))i_f=i_f.replace("FastEthernet","Fa");
					if(i_f.matches("GigabitEthernet.*"))i_f=i_f.replace("GigabitEthernet","Gi");
					if(i_f.matches("Tunnel.*"))i_f=i_f.replace("Tunnel","Tu");
				}else if(line.equals("!")){
					i_f=null;
				}else if(line.matches(" ip address \\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} \\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} standby .*")){
					String[] word=line.split(" ");
					Address tmpAddr=new Address(word[3],word[4]);
					String primaryIP=word[3];
					String mask=word[4];
					String secondaryIP=word[6];
					String primaryExplain=primaryIP+"/"+Address.getMaskLength(mask)+"\n";
					primaryExplain+=hostname+":primary "+i_f+"\n";
					String secondaryExplain=secondaryIP+"/"+Address.getMaskLength(mask)+"\n";
					secondaryExplain+=hostname+":secondary "+i_f+"\n";
					String networkIPExplain=Address.getStrExp(tmpAddr.getNetworkBit())+"/"+Address.getMaskLength(mask)+"\n";
					networkIPExplain+=hostname+":networkIP "+i_f+"\n";
					if(!updateFlag || ipm.existsKey(primaryIP)){
						ipm.put(primaryIP+DBManager.DIVIDE_STR+"ip",primaryIP);
						ipm.put(primaryIP+DBManager.DIVIDE_STR+"explain",primaryExplain);
						ipm.put(primaryIP+DBManager.DIVIDE_STR+"checked","primaryIP");
						//System.out.println(Address.getStrExp(tmpAddr.getNetworkBit()));
						ipm.put(Address.getStrExp(tmpAddr.getNetworkBit())+DBManager.DIVIDE_STR+"ip",Address.getStrExp(tmpAddr.getNetworkBit()));
						ipm.put(Address.getStrExp(tmpAddr.getNetworkBit())+DBManager.DIVIDE_STR+"explain",networkIPExplain);
						ipm.put(Address.getStrExp(tmpAddr.getNetworkBit())+DBManager.DIVIDE_STR+"checked","networkIP");
					}
					if(!updateFlag || ipm.existsKey(secondaryIP)){
						ipm.put(secondaryIP+DBManager.DIVIDE_STR+"ip",secondaryIP);
						ipm.put(secondaryIP+DBManager.DIVIDE_STR+"explain",secondaryExplain);
						ipm.put(secondaryIP+DBManager.DIVIDE_STR+"checked","secondaryIP");
					}
				}else if(line.matches(" ip address \\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} .*")){
					String[] word=line.split(" ");
					String address=word[3];
					String mask=word[4];
					Address tmpAddr=new Address(word[3],word[4]);
					String explain=null;
					if(line.matches(".* secondary"))explain=address+"/"+Address.getMaskLength(mask)+":secondary\n";
					else explain=address+"/"+Address.getMaskLength(mask)+"\n";
					explain+=hostname+" "+i_f+"\n";
					String networkIPExplain=Address.getStrExp(tmpAddr.getNetworkBit())+"/"+Address.getMaskLength(mask)+"\n";
					networkIPExplain+=hostname+" "+i_f+":networkIP\n";
					if(!updateFlag || ipm.existsKey(address)){
						ipm.put(address+DBManager.DIVIDE_STR+"ip",address);
						ipm.put(address+DBManager.DIVIDE_STR+"explain",explain);
						ipm.put(address+DBManager.DIVIDE_STR+"checked","interfaceIP");
						ipm.put(Address.getStrExp(tmpAddr.getNetworkBit())+DBManager.DIVIDE_STR+"ip",Address.getStrExp(tmpAddr.getNetworkBit()));
						ipm.put(Address.getStrExp(tmpAddr.getNetworkBit())+DBManager.DIVIDE_STR+"explain",networkIPExplain);
						ipm.put(Address.getStrExp(tmpAddr.getNetworkBit())+DBManager.DIVIDE_STR+"checked","networkIP");
					}
				}else if(line.matches(" standby \\d+ ip .*")){
					String vip=line;
					String[] word=vip.split(" ");
					//System.out.println(word[2]);
					//System.out.println(word[4]);
					String address=word[4];
					String explain=address+"\n";
					explain+=hostname+" "+i_f+"_vip\n";
					if(!updateFlag || ipm.existsKey(address)){
						ipm.put(address+DBManager.DIVIDE_STR+"ip",address);
						ipm.put(address+DBManager.DIVIDE_STR+"explain",explain);
						ipm.put(address+DBManager.DIVIDE_STR+"checked","standbyIP");
					}
				}

			}
			br.close();
		}
	}
	
	public void checkNatIP(boolean updateFlag,TreeMap<String,String> hostCenterMap) throws FileNotFoundException,IOException{
		for(File curFile:configList){
			if(!curFile.getName().matches(".*EJ..U.RT.*"))continue;
			System.out.println(curFile.getName());
			BufferedReader br = new BufferedReader(new FileReader(curFile));
			String line=null;
			String hostname=null;
			while ((line = br.readLine()) != null) {
				//ホスト名取得
				if(line.matches("hostname .*")){
					hostname=line;
					hostname=hostname.replace("hostname ","");
				}
				if(hostname==null)continue;
				
				//個社ホスト用NAT行
				if(line.matches("ip nat outside source static .*")){
					//System.out.println(line);
					String natLine=line;
					natLine=natLine.replace("ip nat outside source static ","");
					String[] word=natLine.split(" ");
					if(!updateFlag || ipm.existsKey(word[1])){
						ipm.put(word[1]+DBManager.DIVIDE_STR+"ip",word[1]);
						ipm.put(word[1]+DBManager.DIVIDE_STR+"explain",hostCenterMap.get(hostname)+" ホスト");
						ipm.put(word[1]+DBManager.DIVIDE_STR+"checked","NatIP");
					}
				}
			}
			br.close();
		}
	}
}