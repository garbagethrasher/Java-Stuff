import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.lang.Throwable;
import java.io.IOException;

class FullStackException extends Exception{
	FullStackException(){}
}

class EmptyStackException extends Exception{
	EmptyStackException(){}
}

class Package{
	private String recipient;
	private int arrivalDate;
	private double weight;
	
	public Package(String recipient, int arrivalDate, double weight){
		this.recipient = recipient;
		this.arrivalDate = arrivalDate;
		this.weight = weight;
	}
	
	public String getRecipient(){
		return recipient;
	}
	
	public int getArrivalDate(){
		return arrivalDate;
	}
	
	public void setRecipient(String newRecipient){
		recipient = newRecipient;
	}
	
	public void setArrivalDate(int newArrivalDate){
		arrivalDate = newArrivalDate;
	}
	
	public String toString(){
		return "[" + recipient + " " + arrivalDate + "]" ;    
	}

}

class PackageStack{
	private Package [] packages;
	private final int CAPACITY = 7;
	private int currentAmt;
	
	public PackageStack(){
		currentAmt = 0;
		packages = new Package[CAPACITY];
	}
	
	public boolean isFull(){
		return currentAmt == CAPACITY;
	}
	
	public boolean isEmpty(){
		return currentAmt == 0;
	}
	
	
	public void push(Package x) throws FullStackException{
		//Add package to the top of stack
		if(isFull())
			throw new FullStackException();
		
		packages[currentAmt] = x;
		
		currentAmt+=1;
	}
	
	public Package pop() throws EmptyStackException{
		//Remove package from the top of stack
		if(isEmpty())
			throw new EmptyStackException();
		
		currentAmt-=1;
		Package tmp = packages[currentAmt];
		packages[currentAmt] = null;
		
		return tmp;
	}
	
	public Package peek()throws EmptyStackException{
		//return package from top of stack but don't remove it
		if(isEmpty())
			throw new EmptyStackException();
		
		Package tmp = packages[currentAmt-1];
		
		return tmp;
	}
	
	public String toString(){
		String tmp = "";
		for (int i = 0; i<currentAmt; i++)
			tmp += packages[i];
		
		return tmp;
	}

}

class MailroomManager{
	private static PackageStack agStack;
	private static PackageStack hjStack;
	private static PackageStack kmStack;
	private static PackageStack nrStack;
	private static PackageStack szStack;
	private static PackageStack floorStack;
	
	private static int date = 0;
	
	private static PackageStack selPackageStack(String name){
		int letter = name.charAt(0);
		if((letter >= 65 && letter <= 71)||(letter >= 97 && letter <= 103)) 
			return agStack;
		if((letter >= 72 && letter <= 74)||(letter >= 104 && letter <= 106)) 
			return hjStack;
		if((letter >= 75 && letter <= 77)||(letter >= 107 && letter <= 109)) 
			return kmStack;
		if((letter >= 78 && letter <= 82)||(letter >= 110 && letter <= 114)) 
			return nrStack;
		if((letter >= 83 && letter <= 90)||(letter >= 115 && letter <= 122)) 
			return szStack;
		
		return floorStack;
	}
	
	
	
	private static void  D(){
		System.out.print("Please enter the recipient name: ");
		String name = System.console().readLine();
		PackageStack curStack = selPackageStack(name);
		System.out.print("Please enter the weight(lbs): ");
		double weight = Double.parseDouble(System.console().readLine());
		Package tmp = new Package(name,date,weight);
		boolean atmpt = true;
		while(atmpt){
			try{
				curStack.push(tmp);
				System.out.println("Package added ");
				atmpt = false;
			}catch(FullStackException e){
				System.out.println("Stack full, moving to another stack");
				if(!(agStack.isFull())){
					curStack = agStack;
					System.out.println("Stack full, moving to stack 1");
				}else if(!(hjStack.isFull())){
					curStack = hjStack;
					System.out.println("Stack full, moving to stack 2");
				}else if(!(kmStack.isFull())){
					curStack = kmStack;
					System.out.println("Stack full, moving to stack 3");
				}else if(!(nrStack.isFull())){
					curStack = nrStack; 
					System.out.println("Stack full, moving to stack 4");
				}else if(!(szStack.isFull())){
					curStack = szStack;
					System.out.println("Stack full, moving to stack 5");
				}else if(!(floorStack.isFull())){
					curStack = floorStack;
					System.out.println("Stack full, moving to floor");
				}else{
					System.out.println("All stacks full");
					atmpt = false;
				}
			}
		}
	}
	
	
	private static void  P(){
		System.out.println("Printing");
		System.out.print("Stack 1 (A-G):|");
		System.out.println(agStack.toString());
		System.out.print("Stack 2 (H-J):|");
		System.out.println(hjStack.toString());
		System.out.print("Stack 3 (K-M):|");
		System.out.println(kmStack.toString());
		System.out.print("Stack 4 (N-R):|");
		System.out.println(nrStack.toString());
		System.out.print("Stack 5 (S-Z):|");
		System.out.println(szStack.toString());
		System.out.print("Stack 6 Floor:|");
		System.out.println(floorStack.toString());
	}
	
	
	private static void Q(){
		//Quit
		System.out.print("Quitting");
		System.exit(0);
	}
	
	
	private static void M(){
		System.out.print("Please enter the source stack (enter 0 for floor): ");
		int source = Integer.parseInt(System.console().readLine());	
		System.out.print("Please enter the destination stack: ");
		int dest = Integer.parseInt(System.console().readLine());
		PackageStack sourceStack = floorStack;
		PackageStack destStack = floorStack;
		
		if(source == 0){
			sourceStack = floorStack;
		}else if(source == 1){
			sourceStack = agStack;
		}else if(source == 2){
			sourceStack = hjStack;
		}else if(source == 3){
			sourceStack = kmStack;
		}else if(source == 4){
			sourceStack = nrStack;
		}else if(source == 5){
			sourceStack = szStack;
		}
		
		if(dest == 0){
			destStack = floorStack;
		}else if(dest == 1){
			destStack = agStack;
		}else if(dest == 2){
			destStack = hjStack;
		}else if(dest == 3){
			destStack = kmStack;
		}else if(dest == 4){
			destStack = nrStack;
		}else if(dest == 5){
			destStack = szStack;
		}
		
		
		
		if((source>=0&&source<=5)&&(dest>=0&&dest<=5)){
			try{
				Package get = sourceStack.pop();
				try{
					destStack.push(get);
					System.out.println("The package has been moved");
				}catch(FullStackException e){
					System.out.println("The stack you chose has enough");
				}
			
			}catch(EmptyStackException e){
				System.out.println("Nothing in the stack you chose");
			}
		}else{
			System.out.println("Invalid stack you chose");
		}
		
	}
	
	
	private static void  G(){
		System.out.print("Please enter the recipient name: ");
		String name = System.console().readLine();
		PackageStack curStack = selPackageStack(name);
		Package tmp;
		int source = 0;
		boolean atmpt = true;
		while(atmpt){
			int count = 0;
			try{
				while(!(curStack.isEmpty())){
					tmp = curStack.pop();
					if(tmp.getRecipient() == name){
						tmp = null;
						atmpt = false;
						break;
					}else{
						floorStack.push(tmp);
						count+=1;
					}	
				}
				
				for(int i=0; i<count; i++){
					tmp = floorStack.pop();
					curStack.push(tmp);
				}
				
				if(source == 1){
					curStack = agStack;
				}else if(source == 2){
					curStack = hjStack;
				}else if(source == 3){
					curStack = kmStack;
				}else if(source == 4){
					curStack = nrStack;
				}else if(source == 5){
					curStack = szStack;
				}else{
					break;
				}
				source +=1;
			
			}catch(EmptyStackException e){}catch(FullStackException e){}
		}
		
		if(atmpt){
			System.out.println("Package has been delivered");
		}else{
			System.out.println("No package for you");
		}
		
	}
	
	private static void E(){
		Package tmp;
		try{
			while(!(floorStack.isEmpty())){
				tmp = floorStack.pop();
				tmp = null;
			}
		}catch (Exception e){
			
		}
	}
	
	private static void  F(){
		Package tmp;
		PackageStack storage = new PackageStack();
		int source = 1;
		PackageStack curStack = agStack;
		while(source<=5){
			if(source == 1){
				curStack = agStack;
			}else if(source == 2){
				curStack = hjStack;
			}else if(source == 3){
				curStack = kmStack;
			}else if(source == 4){
				curStack = nrStack;
			}else if(source == 5){
				curStack = szStack;
			}
			
			int count = 0;
			try{
				while(!(curStack.isEmpty())){
					tmp = curStack.pop();
					floorStack.push(tmp);
					count+=1;
					}	
				
				
				for(int i=0; i<count; i++){
					tmp = floorStack.pop();
					if(curStack == selPackageStack(tmp.getRecipient()))
						curStack.push(tmp);
					else
						storage.push(tmp);
				}
				
				while(!storage.isEmpty()){
					floorStack.push(storage.pop());
				}
				
				source +=1;
			
			}catch(EmptyStackException e){}catch(FullStackException e){
				
				
			}
		}
		
		System.out.println("Order is restored");
		storage = null;
	}
	
	
	
	private static void  L(){
		System.out.print("Please enter the recipient name: ");
		String name = System.console().readLine();
		PackageStack curStack = agStack;
		Package tmp;
		int source = 1;
		while(source<=5){
			if(source == 1){
				curStack = agStack;
			}else if(source == 2){
				curStack = hjStack;
			}else if(source == 3){
				curStack = kmStack;
			}else if(source == 4){
				curStack = nrStack;
			}else if(source == 5){
				curStack = szStack;
			}
			
			int count = 0;
			try{
				while(!(curStack.isEmpty())){
					tmp = curStack.pop();
					floorStack.push(tmp);
					count+=1;
					}	
				
				
				for(int i=0; i<count; i++){
					tmp = floorStack.pop();
					if(tmp.getRecipient().equals(name)){
						System.out.println("Stack#"+source+": " +tmp.toString());
					}
					curStack.push(tmp);
				}
				
				source +=1;
			
			}catch(EmptyStackException e){}catch(FullStackException e){}
		}
		
		
	}
	
	public static void printMenu(){
		System.out.println("Welcome to the Irving Mailroom Manager. You can try to make it better, but the odds are stacked against you. It is day 0.\nMenu:\n     D) Deliver a package\n     G) Get someone's package\n     T) Make it tomorrow\n     P) Print the stacks\n     M) Move a package from one stack to another\n     F) Find packages in the wrong stack and move to floor\n     L) List all packages awaiting a user\n     E) Empty the floor.\n     Q) Quit");
	}
	
	
	public static void main(String[] args){
		agStack = new PackageStack();
		hjStack = new PackageStack();
		kmStack = new PackageStack();
		nrStack = new PackageStack();
		szStack = new PackageStack();
		floorStack = new PackageStack();
		
		
		Package one = new Package("alex",0,2.3);
		Package two = new Package("helix",0,2.3);
		Package three = new Package("billy",0,2.3);
		Package four = new Package("chris",0,2.3);
		Package five = new Package("damn",0,2.3);
		Package six = new Package("hose",0,2.3);
		Package seven = new Package("Ill",0,2.3);
		
		try{
		agStack.push(one);
		agStack.push(two);
		agStack.push(three);
		agStack.push(four);
		agStack.push(five);
		agStack.push(six);
		hjStack.push(seven);
		}
		catch(Exception e){}
		printMenu();
		while(true){
			System.out.print("Please select an option: ");
			String ans = System.console().readLine();
			char option = ans.charAt(0);
			if (option == 'D' || option == 'd'){
				D();
			}else if (option == 'G' || option == 'g'){
				G();
			}else if (option == 'T' || option == 't'){
				date+=1;
				System.out.print("Today is tomorrow");
			}else if (option == 'P' || option == 'p'){
				P();
			}else if (option == 'M' || option == 'm'){
				M();
			}else if (option == 'F' || option == 'f'){
				F();
			}else if (option == 'L' || option == 'l'){
				L();
			}else if (option == 'E' || option == 'e'){
				E();
			}else if (option == 'Q' || option == 'q'){
				Q();
			}else{
				System.out.println("Invalid Choice");
			}
			System.out.println("");
		}
		
		
	}
}