import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.lang.Throwable;
import java.io.IOException;

class EndOfListException extends Exception {
	public void EndOfListException(){}	
}



class Person{
	public enum Status{
		Available,Holding,OnRide
	}
	
	private int number;
	private int curr;
	private int maxLines;
	private Status stat;
	
	private String mem;
	
	private Ride[] lines;
	
	public Person(int number, int maxLines){
		this.number = number;
		this.maxLines = maxLines;
		this.lines = new Ride[maxLines];
		this.stat = Status.Available;
		this.curr = 0;
		
		if(maxLines == 2)
			mem = "Silver";
		else if(maxLines == 3)
			mem = "Gold";
		else
			mem = "Reg";
		
		
		
	}
	public int getNumber(){
		return number;
	}
	
	public void setStatus(Status set){
		stat = set;
	}
	
	public Status getStatus(){
		return stat;
	}
	
	public void setLines(Ride rides){
		curr %= maxLines;
		lines[curr] = rides;
		curr++;
	}
	
	
	
	public String toString(){
		//Prints information of the car
		return mem+" " +number;
	}
	
}

class VirtualLine extends LinkedList <Person>{
	public void enqueue(Person p){
		add(p);
	}
	
	public Person dequeue(){
		try{
			return removeFirst();
		}catch(Exception e){
			return null;
		}
	}
	
	public Person peek(){
		try{
			return getFirst();
		}catch(Exception e){
			return null;
		}
	}
	
	public String toString(){
		Person start = peek();
		if(start == null)
			return "Empty";
		
		String tmp = start.toString()+ " ";
		enqueue(dequeue());
		
		while(peek() != start){
			tmp += peek().toString()+ " ";
			enqueue(dequeue());
		}
		return tmp;
	}
}


class HoldingQueue extends VirtualLine{
	private int maxSize;
	private int size = 0;
	public int getMaxSize(){
		return maxSize;
	}
	
	public int getSize(){
		return size;
	}
	
	
	public void setMaxSize(int size){
		maxSize = size;
	}
	
	public void enqueue(Person p){
		if(size<maxSize){
			super.enqueue(p);
			size++;
		}else{
			System.out.println("We full");
		}
	}
	public Person dequeue(){	
		size--;
		return super.dequeue();
	}
	
	
}






class Ride{

	private int duration;
	private int timeLeft;
	private int capacity;
	private String Name;
	
	private int holdingCap;
	
	public void setCapacity(int newCapacity){
		capacity = newCapacity;
		peopleOnRide = new Person[capacity];
	}
	
	public void setDuration(int newDuration){
		duration = newDuration;
	}
	
	
	public void setHoldingCap(int newCapacity){
		holdingCap = newCapacity;
		holdingQueue.setMaxSize(newCapacity);
	}
	
	
	
	
	
	public Ride(String Name){
		this.Name = Name;
	}
	
	private VirtualLine virtualLine = new VirtualLine();
	private HoldingQueue holdingQueue = new HoldingQueue();
	private Person[] peopleOnRide;

	
	private int currOnRide = 0;
	
	
	private void clearRide(){
		for(int i=0; i<peopleOnRide.length; i++){
			if(peopleOnRide[i] == null)
				break;
			peopleOnRide[i].setStatus(Person.Status.Available);
			peopleOnRide[i] = null;
		}
	}
	
	private void moveToRide(){
		for(int i=0; i<capacity; i++){
			if(holdingQueue.peek() == null){
				break;
			}
			holdingQueue.peek().setStatus(Person.Status.OnRide);
			peopleOnRide[i] = holdingQueue.dequeue();
		}
	}
	
	
	private void moveToHolding(){
		//Move riders to holding
		int i = holdingQueue.getSize();
		while(i<holdingCap){
			if(virtualLine.peek() == null)
				break;
			if(virtualLine.peek().getStatus() == Person.Status.Available){
				virtualLine.peek().setStatus(Person.Status.Holding);
				holdingQueue.enqueue(virtualLine.dequeue());
			}else{
				virtualLine.enqueue(virtualLine.dequeue());	
			}
			i++;
		}
	}

	
	public void simulate(Person p){
		
		virtualLine.enqueue(p);
		 if(timeLeft == 0){
			clearRide();
			moveToRide();
			moveToHolding();
			timeLeft = duration;
		}else
			timeLeft-=1;
	}
	
	private int onRide = 0;
	public void init(Person p){
		if(onRide < capacity){
			peopleOnRide[onRide] = p;
			onRide+=1;
		}else if(holdingQueue.getSize() != holdingCap)
			holdingQueue.enqueue(p);
		else
			virtualLine.enqueue(p);
	}
	
	

	
	private String onRide(){
		if(peopleOnRide[0] == null)
			return "Empty";
		String tmp="";
		int i = 0;
		while(i<capacity){
			if(peopleOnRide[i] == null)
				return tmp;
			tmp+=peopleOnRide[i].toString()+ " ";
			i++;
		}
		return tmp;
	}
	
	public String toString(){
		String tmp ="";
		tmp+= Name +"\n";
		tmp+= "On Ride: " + onRide() +"\n";
		tmp+= "Holding Queue: " + holdingQueue.toString() +"\n";
		tmp+= "VirtualLine: " + virtualLine.toString();
		
		return tmp;
	}
	

}

class RandomGenerator{
	public static Ride selectRide(Ride[] rides){
		Random rand = new Random();
		int index = rand.nextInt(rides.length);
		return rides[index];
	}
}






public class SevenFlags{
	
	
	
	public static void main(String[] args){
		RandomGenerator rideRand = new RandomGenerator();
		
		Ride BSOD = new Ride("Blue Scream of Death");
		Ride TOT = new Ride("i386 Tower of Terror");
		Ride GF = new Ride("GeForce");
		Ride KK = new Ride("Kingda Knuth");
		
		///////////////////////REMOVE THIS STUFF AND MAKE PROPER MENU
		System.out.print("How many gold members: ");
		int goldCap = Integer.parseInt(System.console().readLine());
		
		System.out.print("How many silver members: ");
		int silverCap = Integer.parseInt(System.console().readLine());
		
		System.out.print("How many reg members: ");
		int regCap = Integer.parseInt(System.console().readLine());
		
		
		
		///////////////////////REMOVE THIS STUFF AND MAKE PROPER MENU
		

		
		Person [] gold = new Person[goldCap];
		Person [] silver = new Person[silverCap];
		Person [] reg = new Person[regCap];
		
		for(int i=0; i<goldCap; i++){
			Person tmp = new Person(i+1,3);
			gold[i] = tmp;
		}
		
		for(int i=0; i<silverCap; i++){
			Person tmp = new Person(i+1,2);
			silver[i] = tmp;
		}
		
		for(int i=0; i<regCap; i++){
			Person tmp = new Person(i+1,1);
			reg[i] = tmp;
		}
		
		///////////////////////REMOVE THIS STUFF AND MAKE PROPER MENU
		
		System.out.print("How many simulations: ");
		int simTime = Integer.parseInt(System.console().readLine());
		
		
		System.out.print("Capacity at BSOD: ");
		BSOD.setCapacity(Integer.parseInt(System.console().readLine()));
		System.out.print("Holding Cap at BSOD: ");
		BSOD.setHoldingCap(Integer.parseInt(System.console().readLine()));
		System.out.print("How long is BSOD: ");
		BSOD.setDuration(Integer.parseInt(System.console().readLine()));
		
		System.out.print("Capacity at TOT: ");
		TOT.setCapacity(Integer.parseInt(System.console().readLine()));
		System.out.print("Holding Cap at TOT: ");
		TOT.setHoldingCap(Integer.parseInt(System.console().readLine()));
		System.out.print("How long is TOT: ");
		TOT.setDuration(Integer.parseInt(System.console().readLine()));
		
		
		

		System.out.print("Capacity at GF: ");
		GF.setCapacity(Integer.parseInt(System.console().readLine()));
		System.out.print("Holding Cap at GF: ");
		GF.setHoldingCap(Integer.parseInt(System.console().readLine()));
		System.out.print("How long is GF: ");
		GF.setDuration(Integer.parseInt(System.console().readLine()));
		
		System.out.print("Capacity at KK: ");
		KK.setCapacity(Integer.parseInt(System.console().readLine()));
		System.out.print("Holding Cap at KK: ");
		KK.setHoldingCap(Integer.parseInt(System.console().readLine()));
		System.out.print("How long is KK: ");
		KK.setDuration(Integer.parseInt(System.console().readLine()));
		
		///////////////////////REMOVE THIS STUFF AND MAKE PROPER MENU
		//gold silver reg gold silver gold
		System.out.println("------------------------------------------------------------------------------------------");
		Ride [] ride = {BSOD,TOT,GF,KK};
		Ride rideTmp;
		//Initialize
		for(int i =0; i<1; i++){
			rideTmp = rideRand.selectRide(ride);
			rideTmp.init(gold[i%goldCap]);
			gold[i%goldCap].setLines(rideTmp);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.init(silver[i%silverCap]);
			silver[i%silverCap].setLines(rideTmp);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.init(reg[i%regCap]);
			reg[i%regCap].setLines(rideTmp);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.init(gold[(i+1)%goldCap]);
			gold[(i+1)%goldCap].setLines(rideTmp);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.init(silver[(i+1)%silverCap]);
			silver[(i+1)%silverCap].setLines(rideTmp);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.init(gold[(i+2)%goldCap]);
			gold[(i+2)%goldCap].setLines(rideTmp);
			System.out.println("At Time: "+i);
			System.out.println(BSOD.toString());			
			System.out.println("----------------");
			System.out.println(TOT.toString());
			System.out.println("----------------");
			System.out.println(GF.toString());
			System.out.println("----------------");
			System.out.println(KK.toString());
			System.out.println("----------------");
			
		}
		
		
		
		//Simulate
		for(int i =1; i<simTime; i++){
			rideTmp = rideRand.selectRide(ride);
			rideTmp.simulate(gold[i%goldCap]);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.simulate(silver[i%silverCap]);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.simulate(reg[i%regCap]);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.simulate(gold[(i+1)%goldCap]);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.simulate(silver[(i+1)%silverCap]);
			rideTmp = rideRand.selectRide(ride);
			rideTmp.simulate(gold[(i+2)%goldCap]);
			System.out.println("At Time: "+i);
			System.out.println(BSOD.toString());			
			System.out.println("----------------");
			System.out.println(TOT.toString());
			System.out.println("----------------");
			System.out.println(GF.toString());
			System.out.println("----------------");
			System.out.println(KK.toString());
			System.out.println("----------------");
			
		}
		
		
		int numOfRides = 4;
		
		System.out.print("Number gold rode: " +((numOfRides/simTime)*goldCap)+3);
		System.out.print("Number gold rode: " +((numOfRides/simTime)*silverCap)+2);
		System.out.print("Number gold rode: " +((numOfRides/simTime)*regCap)+1);
		
		
		
		
		

	}
		
}