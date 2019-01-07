import java.io.*;
import java.util.*;

public class Address implements Serializable{
	private long address;	//�r�b�g��
	private long mask;	//�r�b�g��
	
	public Address(String addressStr){	//x.x.x.x��/32�ō쐬�B
		if(addressStr.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
			address=getBit(addressStr);
			String bin = "11111111111111111111111111111111";
			mask = Long.parseLong(bin, 2);
		}else if(addressStr.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}/\\d{1,2}")){
			String[] word=addressStr.split("/");
			address=Address.getBit(word[0]);
			mask=Address.getBit(Integer.parseInt(word[1]));
		}else if(addressStr.matches("Any|any|ANY")){
			address=Address.getBit("0.0.0.0");
			mask=0;
		}else{
			System.out.println("Illegal AddrStr:"+addressStr);
			System.exit(0);
		}
	}
	
	public Address(String addressStr,int maskLength){
		address=Address.getBit(addressStr);
		mask=Address.getBit(maskLength);
	}
	
	public Address(String addressStr,String maskStr){
		if(!Address.isLegalMask(maskStr)){
			System.out.println("Illegal Mask:"+maskStr);
			System.exit(0);
		}
		address=getBit(addressStr);
		mask=getBit(maskStr);
	}
	
	public long getAddress(){
		return address;
	}
	
	public long getMask(){
		return mask;
	}
	
	public void setMask(int maskLength){
		mask=getBit(maskLength);
	}
	
	public int getMaskLength(){
		return Address.getMaskLength(getMask());
	}
	
	public long getNetworkBit(){	//�l�b�g���[�N�A�h���X
		long returnBit=address&mask;
		return returnBit;
	}
	
	public long getBroadcastBit(){	//�u���[�h�L���X�g�A�h���X
		long returnBit=address|(~mask&Long.parseLong("11111111111111111111111111111111",2));
		return returnBit;
	}
	
	public boolean exactlyEquals(Address taisyouAddr){
		if(getAddress()==taisyouAddr.getAddress() && getMask()==taisyouAddr.getMask())return true;
		else return false;
	}
	
	public boolean equals(Address taisyouAddr){
		if(getAddress()==taisyouAddr.getAddress())return true;
		else return false;
	}
	
	public boolean containsAddress(Address taisyouAddress){	//�ꕔ��邩�ǂ���
		long thisBegin=getNetworkBit();
		long thisEnd=getBroadcastBit();
		long taisyouBegin=taisyouAddress.getNetworkBit();
		long taisyouEnd=taisyouAddress.getBroadcastBit();
		
		if(thisBegin>taisyouEnd || thisEnd<taisyouBegin)return false;
		else return true;
	}
	
	public boolean includeAddress(Address taisyouAddress){	//�Ώۂ������̃����W�Ɋ܂܂�邩�ǂ���
		long thisBegin=getNetworkBit();
		long thisEnd=getBroadcastBit();
		long taisyouBegin=taisyouAddress.getNetworkBit();
		long taisyouEnd=taisyouAddress.getBroadcastBit();
		
		if(thisBegin<=taisyouBegin && thisEnd>=taisyouEnd)return true;
		else return false;
	}
	
	public void add1Bit(){
		address++;
	}
	
	public String getSimpleStrExp(){
		return getStrExp(getAddress());
	}
	
	public String getStrExp(){
		return toString();
	}
	
	public String toString(){	//Detail�\��
		return Address.getStrExp(getAddress(),getMask());
	}
	
	public void showAll(){
		System.out.println(getStrExp());
	}
	
	public static String getStrExp(long tmpAddress){	//�r�b�g�`�����A�h���X�`��������ɕϊ�
		String returnStr=null;
		for(int i=0;i<4;i++){
			long tmpOct=tmpAddress&Long.parseLong("11111111", 2);
			//System.out.println(tmpInt);
			if(returnStr==null)returnStr=String.valueOf(tmpOct);
			else returnStr=tmpOct+"."+returnStr;
			
			if(i!=3)tmpAddress=tmpAddress>>>8;
		}
		
		return returnStr;
	}
	
	public static String getStrExp(long tmpAddress,long tmpMask){
		String returnStr=getStrExp(tmpAddress);
		
		returnStr=returnStr+"/"+getMaskLength(tmpMask);
		
		return returnStr;
	}
	
	public static String getMaskStr(int maskLength){	//�}�X�N������255.255.255.0�`���̕������Ԃ�
		long maskLong=0L;
		for(int i=1;i<=32;i++){
			if(i<=maskLength)maskLong=maskLong|1;
			if(i!=32)maskLong=maskLong<<1;
		}
		
		//System.out.println(Long.toBinaryString(maskLong));
		return getStrExp(maskLong);
	}
	
	public static long getBit(String addressStr){	//"192.168.0.1"�����r�b�g�ŕԂ�
		//���[�K���`�F�b�N
		if(!addressStr.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
			System.out.println("Illegal Address:"+addressStr);
			System.exit(0);
		}

		long returnBit=0;
		
		String[] word=addressStr.split("\\.");
		for(int i=0;i<word.length;i++){
			//System.out.println(word[i]);
			returnBit=returnBit|Long.parseLong(word[i]);
			if(i<word.length-1)returnBit=returnBit<<8;
		}
		
		return returnBit;
	}
	
	public static long getBit(int maskLength){	//�}�X�N������r�b�g��𐶐�
		long returnBit=0;
		for(int i=0;i<maskLength;i++){
			returnBit=returnBit|1l;
			if(i!=maskLength-1)returnBit=returnBit<<1;
			//System.out.println(Integer.toBinaryString(returnBit));
		}
		for(int i=0;i<32-maskLength;i++){
			returnBit=returnBit<<1;
			//System.out.println(Integer.toBinaryString(returnBit));
		}
		
		return returnBit;
	}
	
	public static String reverseMaskWildcard(String addressStr){
		long tmpBit=getBit(addressStr);
		tmpBit=~tmpBit&Long.parseLong("11111111111111111111111111111111",2);
		return getStrExp(tmpBit);
	}
	
	public static int getMaskLength(String maskStr){	//�}�X�N����Ԃ�
		long maskBit=Address.getBit(maskStr);
		
		return getMaskLength(maskBit);
	}
	
	public static int getMaskLength(long maskBit){	//�}�X�N����Ԃ�
		int returnBit=32;
		if(!Address.isLegalMask(maskBit)){
			System.out.println("Illegal Mask:"+maskBit);
			System.exit(0);
		}
		
		for(int i=0;i<32;i++){
			if((maskBit&1)==0)returnBit--;
			else return returnBit;
			maskBit=maskBit>>>1;
		}
		
		return returnBit;
	}
	
	public static boolean isLegalMask(String maskStr){	//�ʏ�̃}�X�N�t�H�[�}�b�g���ۂ�
		long maskBit=getBit(maskStr);

		return isLegalMask(maskBit);
	}
	
	public static boolean isLegalMask(long maskBit){	//�ʏ�̃}�X�N�t�H�[�}�b�g���ۂ�
		boolean checkBool=true;
		for(int i=0;i<32;i++){	//�r�b�g��������m�F�B1�x�r�b�g����������A0��������false
			if(checkBool && (maskBit&1)==1){
				checkBool=false;
			}else if(!checkBool && (maskBit&1)==0)return false;
			maskBit=maskBit>>>1;
		}
		
		return true;
	}
	
	public static boolean isLegalWildcard(String wildStr){	//�ʏ�̃��C���h�J�[�h�t�H�[�}�b�g���ۂ�
		long wildBit=Address.getBit(wildStr);
		return isLegalWildcard(wildBit);
	}
	
	public static boolean isLegalWildcard(long wildBit){	//�ʏ�̃��C���h�J�[�h�t�H�[�}�b�g���ۂ�
		boolean checkBool=true;
		for(int i=0;i<32;i++){
			if(checkBool && (wildBit&1)==0){
				checkBool=false;
			}else if(!checkBool && (wildBit&1)==1)return false;
			wildBit=wildBit>>>1;
		}
		
		return true;
	}
}
