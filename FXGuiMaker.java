import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.lang.Throwable;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;




class FXTreeNode{
	public enum ComponentType{
		Button, Label, TextArea, HBox, VBox,AnchorPane
	}
	
	private String text;
	private ComponentType type;
	private FXTreeNode parent;
	private FXTreeNode[] children;
	final int maxChildren=10;

	
	FXTreeNode(String text, ComponentType type){
		this.text = text;
		this.type = type;
		this.parent = null;
		this.children = new FXTreeNode[maxChildren];
	}
	
	public void setParent(FXTreeNode node){
		parent = node;
	}
	
	public FXTreeNode getParent(){
		return parent;
	}
	
	public FXTreeNode getChild(int index){
		return children[index];
	}
	
	public void setChild(int index, FXTreeNode node){
		children[index] = node;
	}


	public void setText(String newText){
		text = newText;
	}
	
	public String toString(){
		return type+ " " +text;
	}
}


class FXComponentTree{
	private FXTreeNode root;
	private FXTreeNode cursor;

	FXComponentTree(){
		this.root = null;
		this.cursor = null;
	}
	
	
	public void cursorToRoot(){
		cursor = root;
	}
	
	public void deleteChild(int index){
		index--;
		if(cursor.getChild(index) == null)
			System.out.println("Nothing to delete");
		else{
			FXTreeNode tmp;
			cursor.setChild(index,null);
			for(int i =index; i<cursor.maxChildren-1; i++){
				if(cursor.getChild(i+1) == null)
					break;
				tmp = cursor.getChild(i+1);
				cursor.setChild(i,tmp);
				cursor.setChild(i+1,null);
				
			}
			System.out.println("Deleted");
		}
			
	}
	
	
	
	public void addChild(int index, FXTreeNode node){
		index -= 1;
		
		if(index != 0){
			if(cursor.getChild(index-1) == null){
				System.out.println("INVALID INDEX");
				return;
			}
		}
		if(cursor.getChild(cursor.maxChildren-1) != null){
			System.out.println("TOO MANY CHILDREN");
			return;
		}
				
		
		FXTreeNode tmp;
		for(int i = cursor.maxChildren-1; i>index-1; i--){
			if(cursor.getChild(i) == null)
				continue;
			tmp = cursor.getChild(i);
			cursor.setChild(i+1,tmp);
		}
		cursor.setChild(index,node);
		node.setParent(cursor);
		System.out.println("Child Added");
	}
	
	public void setTextAtCursor(String text){
		if(cursor == null){
			System.out.println("NOTHING AT CURSOR");
			return;
		}
			
		cursor.setText(text);
	}
	
	public void cursorToChild(int index){
		
		if(cursor == null){
			System.out.println("NOTHING AT CURSOR");
			return;
		}
		index-=1;
		if(cursor.getChild(index) != null)
			cursor = cursor.getChild(index);
		else
			System.out.println("No Child at that index");
	}
	
	public void cursorToParent(){
		if(cursor == null){
			System.out.println("NOTHING AT CURSOR");
			return;
		}
		
		if(cursor != root)
			cursor = cursor.getParent();
		else
			System.out.println("Cursor is at the highest point");
	}
	
	private static void inTree(FXComponentTree tree, String depth, String type, String text){
		FXTreeNode tmp;
		if(type.equals("Button")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.Button);	
		}else if(type.equals("Label")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.Label);	
		}else if(type.equals("TextArea")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.TextArea);	
		}else if(type.equals("HBox")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.HBox);	
		}else if(type.equals("VBox")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.VBox);	
		}else {
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.AnchorPane);	
		}
		
		
		if(depth.equals("0")){
			tree.root = tmp;
			tree.cursor = tmp;
		}else {
			tree.cursorToRoot();
			for(int i =2; i<depth.length()-2;i++){
				if(depth.charAt(i) == '-')
					continue;
				tree.cursorToChild(Character.getNumericValue(depth.charAt(i))+1);
			}
			int tmp3 =Character.getNumericValue(depth.charAt(depth.length()-1));
			tmp3+=1;
			tree.addChild(tmp3,tmp);
			tree.cursorToRoot();
		}
			
		
	}
	
	
	public static FXComponentTree readFromFile(String fileName){
		FXComponentTree tree = new FXComponentTree();
		try {
            Scanner input = new Scanner(fileName);
            File file = new File(input.nextLine());
            input = new Scanner(file);


            while (input.hasNextLine()) {
                String line = input.nextLine();
				int ws = 0;
				String depth= "";
				String type = "";
				String text = "";
				for(int i =0; i<line.length();i++){
					if(line.charAt(i) == ' '){
						ws+=1;
						continue;
					}
					if(ws<1)
						depth += line.charAt(i);
					else if(ws<2)
						type += line.charAt(i);
					else
						text += line.charAt(i);
						
				}
                inTree(tree, depth,type,text);
			}
            input.close();

        }catch (Exception ex) {
			System.out.print(ex);
            System.out.println("False file name");
        }
		
		return tree;
	}
	
	public static void writeToFile(FXComponentTree tree, String filename)throws FileNotFoundException{
			
			 File doc = new File(filename+".txt");

             PrintWriter pw = new PrintWriter(doc);
			 pw.println(tree.toString());
			 pw.close();
	}

	
	public String toString(){
		String tmp = "";
		tmp = stringify(root,tmp,"",0);
		return tmp;
	}
	
	private String stringify(FXTreeNode parent,String tmp,String loc, int childNum){
		if(parent == null)
			return "";
		loc+=childNum+"-";
		tmp+=loc+" "+parent.toString()+"\n";
		for(int i=0; i<parent.maxChildren; i++){
			if(parent.getChild(i)==null)
				break;
			tmp = stringify(parent.getChild(i),tmp,loc,i);
		}
		
		return tmp;
	}
	
}



public class FXGuiMaker{
	private static FXComponentTree tree;
	
	private static void R(){
		tree.cursorToRoot();
	}

	private static void L(){
		System.out.print("Please enter filename: ");
		String fileName = System.console().readLine();
		tree = tree.readFromFile(fileName);
	}
	
	private static void P(){
		System.out.println(tree.toString());
	}
	
	private static void C(){
		System.out.print("Please enter Index: ");
		int index = Integer.parseInt(System.console().readLine());
		tree.cursorToChild(index);
		System.out.println("Cursor moved to child");
	}
	
	private static void D(){
		System.out.print("Please enter Index: ");
		int index = Integer.parseInt(System.console().readLine());
		tree.deleteChild(index);
		System.out.println("child removed");
	}
	
	private static void A(){
		System.out.print("Please enter Type: ");
		String type = System.console().readLine();
		
		System.out.print("Please enter text: ");
		String text = System.console().readLine();
		
		FXTreeNode tmp;
		if(type.equals("Button")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.Button);	
		}else if(type.equals("Label")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.Label);	
		}else if(type.equals("TextArea")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.TextArea);	
		}else if(type.equals("HBox")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.HBox);	
		}else if(type.equals("VBox")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.VBox);	
		}else if(type.equals("AnchorPane")){
			tmp = new FXTreeNode(text,FXTreeNode.ComponentType.AnchorPane);	
		}else{
			System.out.println("INVALID TYPE");
			return;
		}
		
		
		System.out.print("Please enter Index: ");
		int index = Integer.parseInt(System.console().readLine());
		tree.addChild(index,tmp);
		System.out.println("Child added");
	}
	
	private static void U(){
		tree.cursorToParent();
		System.out.println("Cursor moved to parent");
	}
	
	private static void E(){
		System.out.print("Please enter text: ");
		String text = System.console().readLine();
		tree.setTextAtCursor(text);
		System.out.println("Cursor has new text");
	}
	
	private static void S(){
		try{
			System.out.print("Name of file you want to save: ");
			String text = System.console().readLine();
			tree.writeToFile(tree,text);
		}catch(Exception e){
			System.out.print("ERROR");
		}
	}
	
	private static void Q(){
		//Quit
		System.out.print("We congratulate you on your decision to do something more productive with your time.");
		System.exit(0);
	}


	public static void main(String[] args){
		while(true){
			System.out.print("Please select an option: ");
			String ans = System.console().readLine();
			char option = ans.charAt(0);
			if (option == 'A' || option == 'a'){
				A();
			}else if (option == 'C' || option == 'c'){
				C();
			}else if (option == 'D' || option == 'd'){
				D();
			}else if (option == 'E' || option == 'e'){
				E();
			}else if (option == 'L' || option == 'l'){
				L();
			}else if (option == 'P' || option == 'p'){
				P();
			}else if (option == 'Q' || option == 'q'){
				Q();
			}else if (option == 'R' || option == 'r'){
				R();
			}else if (option == 'S' || option == 's'){
				S();
			}else if (option == 'U' || option == 'u'){
				U();
			}else{
				System.out.println("Invalid Choice");
			}
			System.out.println("");
		}
	}
}