import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.lang.Throwable;
import java.io.IOException;


class DeanException extends Exception {
	public DeanException(){}
	
}


class EmptyLineException extends Exception {
	public void EmptyLineException(){}	
}

class Student {
    private String name;
    private double money;
	
	
	public Student(String name, double money){
		this.name = name;
		this.money = money;
	}
	
	public String getName(){
		//Returns student name
		return name; 
	}
	
	public void updateMoney(double money){
		//Update Student's money
		this.money = money;
	}
	
	public String toString(){
		//Print student information
		return getName()+": "+money; 
	}
	
	public Student clone(){
		//Create a new Student
		Student tmp = new Student(name,money);
		return tmp;
	}
	
	
	public boolean equals(Student check){
		//returns if two students are the same
		return check.toString().equals(toString());
	}
}

class StudentLine{
	private Student [] students;
	private int studentCount;
	final int CAPACITY = 3;
	
	
	public StudentLine(){
		this.studentCount = 0;
		this.students = new Student[CAPACITY+1];
		this.students[0] = null;
	}
	
	public int numStudents(){
		//return the number of students on the line
		return studentCount;
	}
	
	public Student getStudent(int index){
		//return the student at index provided
		if(index > studentCount || index < 1){
			throw new ArrayIndexOutOfBoundsException();
		}
		return students[index];
	}
	
	public Student removeStudent(int index) throws EmptyLineException{
		//Remove the student from the line at given index, and fill in the gap
		
		
		if(studentCount == 0){
			throw new EmptyLineException();
		}
		
		
		if(index > studentCount || index < 1){
			throw new ArrayIndexOutOfBoundsException();
		}
		
		
		for( int i = index; i<studentCount; i++){
			swapStudents(i,i+1);
		}
		Student tmp = getStudent(studentCount);
		studentCount -= 1;
		
		return tmp;
	}

	public Student addStudent(int index, Student student) throws DeanException, EmptyLineException{
		//Insert student at the specified index, and push those after the index back
		if(studentCount == CAPACITY){
			throw new DeanException();
		}
		
		if((index -1) > studentCount){
			throw new EmptyLineException();
		}
		
		studentCount+=1;
		students[studentCount] = student;
		for(int i = studentCount; i>index; i--){
				swapStudents(i,i-1);
		}
		
		return student;
		
	}
	
	public void swapStudents(int index1, int index2){
		//Have two students trade places, based on index
		if(index1 >studentCount || index1<=0 ||index2 >studentCount || index2<=0){
			throw new ArrayIndexOutOfBoundsException();
		}
		
		Student tempStudent = students[index1];
		students[index1] = students[index2];
		students[index2] = tempStudent;
	}
	
	public StudentLine clone(){
		//Create a copy of the student line
		StudentLine tmp = new StudentLine();
		for(int i= 1; i<studentCount+1;i++){
			try{
				tmp.addStudent(i,students[i].clone());
			}catch(Exception e){
				System.out.println("Can't be cloned");
			}
		}
		return tmp;
	}

	
	public boolean equals(StudentLine check){
		//Check if two lines are equal
		return check.toString().equals(toString());
	}

	public String toString(){
		//Print a student line
		String info = "";
		for(int i = 1; i<studentCount+1; i++){
				info += i +") "+students[i].toString()+"\n";
		}
		return info;
	}

}



public class LunchLineSimulator{
	private static StudentLine realityA;
	private static StudentLine realityB;
	
	private void printIntro(){
			System.out.println("Welcome to the Middle School where you are the master of the lunch line, and you may subject your angsty kids to whatever form of culinary torture best fits your mood. You are in Reality A.");
	}
	
	private static void A(StudentLine reality){
		//Add a student
		System.out.print("Please enter student name: ");
		String name = System.console().readLine();
		
		System.out.print("Please enter money amount: ");
		double money = Double.parseDouble(System.console().readLine());
		if(money >= 0){
			Student create = new Student(name,money);
			try{
				reality.addStudent(reality.numStudents()+1,create);
				System.out.println("Student Entered ");
			}catch(DeanException e){
				System.out.println(e);
				System.out.println("Student could not be added");
				
			}catch(EmptyLineException  e){
				System.out.println(e);
				System.out.println("Student could not be added");
				
			}
		}else{
			System.out.println("Invalid Money amt");
		}
		
	}
	
	private static void B(StudentLine reality){
		//Bully a student out of the line
		System.out.print("Please enter student index: ");
		int index = Integer.parseInt(System.console().readLine());
		try{
			Student tmp = reality.removeStudent(index);
			System.out.println("The bully has stolen "+tmp.getName()+"'s lunch money, and " +tmp.getName()+" has left, feeling hangry.");
			tmp = null;
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println(e);
			System.out.println("Bully missed");
		}catch(EmptyLineException e){
			System.out.println(e);
			System.out.println("Bully missed");
		}
		
	}
	
	private static void C(StudentLine reality){
		//Cut a student
		System.out.print("Please enter student name: ");
		String name = System.console().readLine();
		
		System.out.print("Please enter money amount: ");
		double money = Double.parseDouble(System.console().readLine());
		
		System.out.print("Please enter position: ");
		int index = Integer.parseInt(System.console().readLine());
		
		Student create = new Student(name,money);
		try{
			realityA.addStudent(index,create);
		}catch(DeanException e){
				System.out.println(e);
				System.out.println("You can't cut");
				
		}catch(EmptyLineException  e){
				System.out.println(e);
				System.out.println("You can't cut");
				
		}
		
		
		String skipped = realityA.getStudent(index+1).getName();
		System.out.println(name+" has cutted "+skipped+" and is now in position "+ index + ". "+name+" has $"+money+"." );
	}
	
	private static void S(StudentLine reality){
		//Serve the student at index 1
		
		try{
			System.out.println(reality.getStudent(1).getName()+" has been served today's special: Bouncy \"Chicken?\" Nuggets. We hope (s)he lives to see another day!");
			Student tmp = reality.removeStudent(1);
			tmp = null;
		}catch(ArrayIndexOutOfBoundsException  e){
			System.out.println(e);
			System.out.println("Nobody to serve");
		}catch(EmptyLineException e){
			System.out.println(e);
			System.out.println("Nobody to serve");
		}
		
		
	}
	
	private static void T(StudentLine reality){
		//Swap places
		System.out.print("Please enter student1 index: ");
		int index1 = Integer.parseInt(System.console().readLine());
		
		System.out.print("Please enter student2 index: ");
		int index2 = Integer.parseInt(System.console().readLine());
		
		try{
			reality.swapStudents(index1,index2);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Invalid Swap");
		}
		
		System.out.println(reality.getStudent(index2).getName()+" has traded places with "+reality.getStudent(index1).getName());
	
	}
	
	private static void P(StudentLine reality){
		//Print out the line
		System.out.println("Printing Lunch Line ");
		System.out.print(reality.toString());
	}
	
	private static void U(StudentLine reality){
		//Update money in student's account
		System.out.print("Please enter student index: ");
		int index = Integer.parseInt(System.console().readLine());
		
		System.out.print("Please enter new money amount: ");
		double money = Double.parseDouble(System.console().readLine());
			
		if(money >= 0){
			reality.getStudent(index).updateMoney(money);
			System.out.println("As a result of a shady transaction with the floor, "+reality.getStudent(index).getName()+" now has $"+money+".");
		}else{
			System.out.println("Invalid amount");
		}
		
		
	}
		
	private static void Q(){
		//Quit
		System.out.print("You are now leaving the Middle School Lunch Line Simulator. We congratulate you on your decision to do something more productive with your time.");
		System.exit(0);
	}
	
	private static void E(StudentLine active, StudentLine inactive){
		//Check if realitites are equal
		if(active.equals(inactive))
			System.out.println("The realities are equal.");
		else
			System.out.println("The realities are not equal.");
	}
	
	private static void printMenu(){
		System.out.println("Menu:\n\n     A) Add a student to the line at the end\n\n     C) Have a new student cut a friend\n\n     T) Have two students trade places\n\n     B) Have the bully remove a student\n\n     U) Update a student's money amount\n\n     S) Serve a student\n\n     P) Print the current reality's lunch line\n\n     O) Switch to the other reality\n\n     E) Check if the realities are equal\n\n     D) Duplicate this reality into the other reality\n\n     Q) Quit middle school and move on to real life.");
	}
	
	public static void main(String[] args){
		realityA = new StudentLine();
		realityB = new StudentLine();
		StudentLine activeReality = realityA;
		StudentLine inactiveReality = realityB;
		boolean real = true;
		printMenu();
		while(true){
			System.out.print("Please select an option: ");
			String ans = System.console().readLine();
			char option = ans.charAt(0);
			if (option == 'A' || option == 'a'){
				A(activeReality);
			}else if (option == 'B' || option == 'b'){
				B(activeReality);
			}else if (option == 'C' || option == 'c'){
				C(activeReality);
			}else if (option == 'D' || option == 'd'){
				System.out.println("Duplicating Realities");
				inactiveReality = activeReality.clone();
			}else if (option == 'E' || option == 'e'){
				E(activeReality,inactiveReality);
			}else if (option == 'O' || option == 'o'){
				real = !real;
				if(real)
					System.out.println("In reality A");
				else
					System.out.println("In reality B");
				StudentLine tmp = activeReality;
				activeReality = inactiveReality;
				inactiveReality = tmp;
				
			}else if (option == 'P' || option == 'p'){
				P(activeReality);
			}else if (option == 'Q' || option == 'q'){
				Q();
			}else if (option == 'S' || option == 's'){
				S(activeReality);
			}else if (option == 'T' || option == 't'){
				T(activeReality);
			}else if (option == 'U' || option == 'u'){
				U(activeReality);
			}else{
				System.out.println("Invalid Choice");
			}
			System.out.println("");
		}
	}

}