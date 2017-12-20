import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.lang.Throwable;
import java.io.IOException;

class EndOfListException extends Exception {
	public void EndOfListException(){}	
}



class Car{
	public enum Make{
		FORD,GMC,CHEVY,JEEP,DODGE,CHRYSLER,LINCOLN
	}
	
	private String owner;
	private Make make;
	
	public Car(Make make,String owner){
		this.make = make;
		this.owner = owner;
	}
	
	public String toString(){
		//Prints information of the car
		return make + " is owned by " + owner;
	}
	

	
}

class CarListNode{
	private Car data;
	private CarListNode next;
	private CarListNode prev;
	
	public CarListNode(Car initData){
		if(initData == null)
			throw new IllegalArgumentException();
		
		this.data = initData;
		this.next = null;
		this.prev = null;
	}
	
	public CarListNode getNextNode(){
		//returns the next node
		return next; 
	}
	
	public CarListNode getPrevNode(){
		//returns the previous node
		return prev; 
	}
	
	public void setNextNode(CarListNode nextCar){
		//set the node next value to another node
		next = nextCar; 
	}
	
	public void setPrevNode(CarListNode prevCar){
		//set the node prev value to nother node
		prev = prevCar; 
	}
	
	public Car getCar(){
		//returns information about the car/node
		return data;
	}
	
	public String toString(){
		//Used to print information
		return data.toString();
	}
}

class CarList{
	private CarListNode head;
	private CarListNode tail;
	private CarListNode cursor;
	private int carListNodeCount;
	private int carListCursorCount;
	
	public CarList(){
		this.head = null;
		this.tail = null;
		this.cursor = null;
		this.carListNodeCount = 0;
		this.carListCursorCount = 0;
	}
	
	public int numCars(){
		//returns amount cars in list
		return carListNodeCount;
	}
	
	public int indexOfCursor(){
		return carListCursorCount;
	}
	
	public Car getCursorCar(){
		return cursor.getCar();
	}
	
	public void resetCursorToHead(){
		//sets cursor to he head
		carListCursorCount = 0;
		cursor = head;
	}
	
	public void resetCursorToTail(){
		//sets cursor to tail
		cursor = tail;
		carListCursorCount = carListNodeCount;
	}
	
	public void cursorForward()throws EndOfListException{
		//Move the cursor by one node
		if(cursor == tail)
			throw new EndOfListException();
		
		carListCursorCount +=1;
		cursor = cursor.getNextNode();
	}
	
	public void cursorBackward()throws EndOfListException{
		//Decrement the cursor by one
		if(cursor == head)
			throw new EndOfListException();
		
		carListCursorCount -=1;
		cursor = cursor.getPrevNode();
	}
	
	public void insertBeforeCursor(Car newCar){
		//Insert car before the cursor
		if(newCar == null){
			throw new IllegalArgumentException();
		}
		
		CarListNode tmp = new CarListNode(newCar);
		tmp.setNextNode(cursor);

		if(cursor == null){
			head = tmp;
			cursor = tmp;
			tail = tmp;
		}else if(cursor == head){
			head = tmp;
		}else{
			tmp.setPrevNode(cursor.getPrevNode());
			(cursor.getPrevNode()).setNextNode(tmp);
			cursor.setPrevNode(tmp);
		}
		
		carListCursorCount +=1;
		carListNodeCount+=1;

	}

	
	
	public void appendToTail(Car newCar){
		//Add a car to the end of the list 
		if(newCar == null)
			throw new IllegalArgumentException();
		
		CarListNode tmp = new CarListNode(newCar);
		
		
		if(head == null){
			head = tmp;
			cursor = tmp;
		}else{
			tail.setNextNode(tmp);
			tmp.setPrevNode(tail);
		}
		tail = tmp;
		carListNodeCount+=1;
	}	
	
	public Car removeCursor(){
		//remove car at cursor
		if(cursor == null)
			throw new IllegalArgumentException();
		
		Car tmp = cursor.getCar();
		if(carListNodeCount  == 1){
			head = null;
			tail = null;
			cursor.setPrevNode(null);
			cursor.setNextNode(null);
			cursor = null;
			carListNodeCount -=1;
			return tmp;
		}
			
		
		if(cursor == head){
			head = cursor.getNextNode();
			head.setPrevNode(null);
			cursor.setPrevNode(null);
			cursor.setNextNode(null);
			cursor = head;
		}else if(cursor == tail){
			tail = cursor.getPrevNode();
			tail.setNextNode(null);
			cursor.setPrevNode(null);
			cursor.setNextNode(null);
			cursor = tail;
		}else{
			CarListNode newNext = cursor.getNextNode();
			CarListNode newCurrent = cursor.getPrevNode();
			newCurrent.setNextNode(newNext);
			newNext.setPrevNode(newCurrent);
			cursor = newCurrent;
		}
		carListCursorCount -=1;
		carListNodeCount -=1;
		
		return tmp;
	}


	public void merge(CarList other){
		//merge two list
		CarListNode tmp = head;
		CarListNode tmp2 = null;
		if(head != null){
			tmp2 = head.getNextNode();
		}
		other.resetCursorToHead();
		while(tmp2 != null){
			Car put = other.removeCursor();
			CarListNode get = new CarListNode(put);
			get.setPrevNode(tmp);
			get.setNextNode(tmp2);
			tmp.setNextNode(get);
			tmp2.setPrevNode(get);
		}
		
		while(other.numCars()>0){
			Car get = other.removeCursor();
			appendToTail(get);
		}
	}
	
	public String toString(){
		//print stuff
		String str = "";
		CarListNode tmp = head;
		while(tmp != null){
			if(tmp == cursor)
				str+=" CURSOR: ";
			if(tmp == head)
				str+=" Head: ";
			if(tmp == tail)
				str+=" Tail: ";
			str+=tmp.toString()+"->";
			tmp = tmp.getNextNode();
		}
	
		return str;
	}
}

public class OilChangeManager{
	
	private static void A(CarList current){
		System.out.print("Please enter vehicle make (Ford, GMC, Chevy, Jeep, Dodge, Chrysler, and Lincoln): ");
		String make = System.console().readLine();
		make = make.trim();
		
		System.out.print("Please enter owner's name: ");
		String name = System.console().readLine();
		
		if(make.equals("ford")){
			Car tmp = new Car(Car.Make.FORD,name);
			current.appendToTail(tmp);
			System.out.print("Car added");
		}else if(make.equals("gmc")){
			Car tmp = new Car(Car.Make.GMC,name);
			current.appendToTail(tmp);
			System.out.print("Car added");
		}else if(make.equals("chevy")){
			Car tmp = new Car(Car.Make.CHEVY,name);
			current.appendToTail(tmp);
			System.out.print("Car added");
		}else if(make.equals("jeep")){
			Car tmp = new Car(Car.Make.JEEP,name);
			current.appendToTail(tmp);
			System.out.print("Car added");
		}else if(make.equals("dodge")){
			Car tmp = new Car(Car.Make.DODGE,name);
			current.appendToTail(tmp);
			System.out.print("Car added");
		}else if(make.equals("chrysler")){
			Car tmp = new Car(Car.Make.CHRYSLER,name);
			current.appendToTail(tmp);
			System.out.print("Car added");
		}else if(make.equals("lincoln")){
			Car tmp = new Car(Car.Make.LINCOLN,name);
			current.appendToTail(tmp);
			System.out.print("Car added");
		}else{
			System.out.print("Invalid");
		}
	}
	
	
	private static void F2(CarList current){
		try{
			current.cursorForward();
			System.out.println("Cursor moved forward");
		}catch(EndOfListException e){
			System.out.println("Cursor can't move forward");
			System.out.println(e);
		}
	}
	
	private static void P(CarList current){
		System.out.println("Printing list");
		System.out.println(current.toString());
		
	}
	
	private static void H(CarList current){
		current.resetCursorToHead();
	}
	
	private static void T(CarList current){
		current.resetCursorToTail();
	}
	
	private static void B(CarList current){
		try{
			current.cursorBackward();
			System.out.println("Cursor moved back 1");
		}catch(EndOfListException e){
			System.out.println("Cursor can't move back");
		}
	}
	
	private static void R(CarList current){
		try{
			Car tmp = current.removeCursor();
			tmp = null;
			System.out.println("Car removed");
		}catch(IllegalArgumentException e){
			System.out.println("Car can't be removed");
		}
	}
	
	
	private static Car X(CarList current){
		try{
			Car tmp = current.removeCursor();
			System.out.println("Car cutted");
			return tmp;
		}catch(IllegalArgumentException e){
			System.out.println("Car can't be cut");
			return null;
		}
	}
	
	private static void V(CarList current, Car add){
		try{
			current.insertBeforeCursor(add);
			System.out.println("Car has been added");
		}catch(IllegalArgumentException e){
			System.out.println("Car can't be added");
		}
	}
	
	private static void I(CarList current){
		try{
			System.out.print("Please enter vehicle make (Ford, GMC, Chevy, Jeep, Dodge, Chrysler, and Lincoln): ");
			String make = System.console().readLine();
		
			System.out.print("Please enter owner's name: ");
			String name = System.console().readLine();
			
			Car tmp = new Car(Car.Make.FORD,name);
			current.insertBeforeCursor(tmp);
			
			System.out.println("Car inserted before cursor");
		}catch(IllegalArgumentException e){
			System.out.println("Car can't be inserted");
		}
	}
	
	
	
	private static void Q(){
		//Quit
		System.out.print("Get Out");
		System.exit(0);
	}
	
	private static void initMenu(){
		System.out.println("Menu: \n     L) Edit Job Lists for Joe and Donny \n     M) Merge Job Lists \n     P) Print Job Lists \n     F) Paste car to end of finished car list \n     S) Sort Job Lists //Extra Credit, you don't have to do this if you don't want to \n     Q) Quit.");	
	}
	
	private static void listMenu(){
		System.out.println("Options:\n     A) Add a car to the end of the list\n     F) Cursor Forward\n     H) Cursor to Head\n     T) Cursor to Tail\n     B) Cursor Backward\n     I) Insert car before cursor\n     X) Cut car at cursor \n     V) Paste before cursor \n     R) Remove cursor");
	}
	
	
	
	
	
	public static void main(String[] args){
		CarList Joe = new CarList();
		CarList Donny = new CarList();
		CarList Finish = new CarList();
		Car cut=null;
		CarList activeList;
		
		while(true){
			initMenu();
			System.out.print("Please select a option ");
			String ans = System.console().readLine();
			char option = ans.charAt(0);
			
			if(option == 'F' || option == 'f'){
				V(Finish,cut);
			}else if(option == 'L' || option == 'l'){
				System.out.print("Please select a list - Joe (J) or Donny (D): ");
				ans = System.console().readLine();
				option = ans.charAt(0);
				
				if (option == 'J' || option == 'j'){
					activeList = Joe;
				}else if (option == 'D' || option == 'd'){
					activeList = Donny;
				}else{
					System.out.print("Invalid option ");
					continue;
				}
				listMenu();
				System.out.print("Please select an option: ");
				 ans = System.console().readLine();
				 option = ans.charAt(0);
			
				if (option == 'A' || option == 'a'){
					A(activeList);
				}else if (option == 'B' || option == 'b'){
					B(activeList);
				}else if (option == 'F' || option == 'f'){
					F2(activeList);
				}else if (option == 'H' || option == 'h'){
					H(activeList);
				}else if (option == 'I' || option == 'i'){
					I(activeList);
				}else if (option == 'R' || option == 'r'){
					R(activeList);
				}else if (option == 'T' || option == 't'){
					T(activeList);
				}else if (option == 'V' || option == 'v'){
					V(activeList,cut);
				}else if (option == 'X' || option == 'x'){
					if( cut != null)
						cut = null;
					cut = X(activeList);
				}else{
					System.out.println("Invalid Choice");
				}
			}else if(option == 'M' || option == 'm'){
				System.out.print("Please select a main list - Joe (J) or Donny (D): ");
				ans = System.console().readLine();
				option = ans.charAt(0);
				
				if (option == 'J' || option == 'j'){
					Joe.merge(Donny);
				}else if (option == 'D' || option == 'd'){
					Donny.merge(Joe);
				}else{
					System.out.print("Invalid option ");
					continue;
				}
			}else if(option == 'P' || option == 'p'){
				System.out.println("Joe");
				P(Joe);
				System.out.println("Donny");
				P(Donny);
				System.out.println("Finish");
				P(Finish);
			}else if(option == 'Q' || option == 'q'){
				Q();
			}else{
				System.out.println("Invalid Choice");
			}
			
			System.out.println("");
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}