import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class Jikkou04_ShowDefalutDB{
	public static void main(String args[]){
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		ReplaceModifier tm=new ReplaceModifier();
		tm.loadDB();
		String clipBoardStr="###DefaltDB Parameter###\n";
		clipBoardStr+=tm.getTreeStr();
		StringSelection selection = new StringSelection(clipBoardStr);
		clipboard.setContents(selection, null);
	}
}