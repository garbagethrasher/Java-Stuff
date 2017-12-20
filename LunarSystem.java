import java.io.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.lang.Throwable;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


class Student implements Serializable {
	private String webID;
	private List<Course> courses;
	
	//Constructor
	public Student(String webID){
		this.webID = webID;
		this.courses = new ArrayList<Course>();
	}
	
	public String getWebid(){
		return webID;
	}
	
	//Custom
	public void addCourse(Course newCourse){
		courses.add(newCourse);
	}
	
	public void dropCourse(Course newCourse){
		courses.remove(newCourse);
	}
	
	public void sortByName(){
		Collections.sort(courses, new CourseNameComparator());  
	}
	
	public void sortBySemester(){
		Collections.sort(courses, new CourseSemesterComparator());  
	}
	
	public String toString(){
		String tmp="";
		for(int i=0; i<courses.size(); i++){
			tmp+=courses.get(i).getDepartment() +" "+ courses.get(i).getNumber() +" "+courses.get(i).getSemester()+"\n";
		}
		return tmp;
	}
}

class Course implements Serializable{
	private String department;
	private int number;
	private String semester;
	private List<Student> students;
	
	//Constructor
	
	public Course(String department, int number, String semester){
		this.department = department;
		this.number = number;
		this.semester = semester;
		this.students = new ArrayList<Student>();
	}
	
	//Getters and Setters
	
	public void setDepartment(String newDepartment){
		department = newDepartment;
	}
	
	public String getDepartment(){
		return department;
	}
	
	public void setNumber(int newNum){
		number = newNum;
	}
	
	public int getNumber(){
		return number;
	}
	
	public void setSemester(String newSemester){
		semester = newSemester;
	}
	
	public String getSemester(){
		return semester;
	}
	
	//Custom
	public void addStudent(Student newStudent){
		students.add(newStudent);
	}
	
	public void dropStudent(Student newStudent){
		students.remove(newStudent);
	}
	
	public String toString(){
		String tmp="";
		int i=0;
		while(i<students.size()){
			if(students.get(i)== null){
				students.remove(i);
				continue;
			}
			tmp+=students.get(i).getWebid()+"\n";
			i++;
		}
		return tmp;
	}
	
}

class CourseNameComparator implements Comparator {
	public int compare (Object left2, Object right2){
		Course left = (Course) left2;
		Course right = (Course) right2;
		int comp = left.getDepartment().compareTo(right.getDepartment());
		if(comp ==0){
			if(left.getNumber()>right.getNumber())
				return 1;
			if(left.getNumber()<right.getNumber())
				return -1;
		}
		return 0;
	}
}


class CourseSemesterComparator implements Comparator {
	public int compare (Object left2, Object right2){
		Course left = (Course) left2;
		Course right = (Course) right2;
		int comp = left.getSemester().compareTo(right.getSemester());
		return comp;
	}
}

public class LunarSystem{
	private static HashMap<String, Student> database;
	private  static HashMap<String, Course> courses;
	
	private static void UImenu(){
		System.out.println("Menu:\n    L)Login\n    X)Save state and quit\n    Q)Quit without saving state.");
	}
	
	private static void studMenu(){
		System.out.println("Options:\n    A)Add a class\n    D)Drop a class\n    C)View your classes sorted by course name/department\n    S)View your courses sorted by semester\n    L) Logout");
	}
	
	private static void regMenu(){
		System.out.println("Options:\n     R) Register a student\n     D) De-register a student\n     E) View course enrollment\n     L) Logout");
	}
	
	private static Course validCourse(){
		System.out.print("Please enter course name: ");
		String courseName = System.console().readLine();
		System.out.print("Please select a semester:");
		String semesterName = System.console().readLine();
		
		String courseTitle = courseName +" "+semesterName;
		
		
		if(!(courses.containsKey(courseTitle))){
			String[] courseNames = courseName.split(" ");
			Course newCourse = new Course(courseNames[0],Integer.parseInt(courseNames[1]),semesterName);
			courses.put(courseName,newCourse);
			return newCourse;
		}
		return courses.get(courseTitle);
	}
	
	private static void studCycle(Student user){
		studMenu();
		while(true){
			System.out.print("Please select an option: ");
			String ans = System.console().readLine();
			char option = ans.charAt(0);
			if(option == 'A' || option == 'a'){ 
				Course tmp = validCourse();
				tmp.addStudent(user);
				user.addCourse(tmp);
			}else if(option == 'D' || option == 'd'){
				Course tmp = validCourse();
				tmp.dropStudent(user);
				user.dropCourse(tmp);
			}else if(option == 'C' || option == 'c'){
				user.sortByName();
				System.out.print(user.toString());
			}else if(option == 'S' || option == 's'){
				user.sortBySemester();
				System.out.print(user.toString());
			}else if(option == 'L' || option == 'l'){
				break;
			}else{
				System.out.println("Invalid Choice");
			}
		}	
	}
	
	private static void regCycle(){
		regMenu();
		while(true){
			System.out.print("Please select an option: ");
			String ans = System.console().readLine();
			char option = ans.charAt(0);
			if(option == 'R' || option == 'r'){ 
				System.out.print("Please enter a webid for the new student: ");
				String webID = System.console().readLine();
				Student tmp = new Student(webID);
				database.put(webID,tmp);
			}else if(option == 'D' || option == 'd'){
				System.out.print("Please enter a webid for the student to be de-registered: ");
				String webID = System.console().readLine();
				Student tmp = database.get(webID);
				tmp = null;
			}else if(option == 'E' || option == 'e'){
				System.out.print("Please enter course name: ");
				String courseName = System.console().readLine();
				Course tmp = courses.get(courseName);
				if(tmp != null)
					System.out.println(tmp.toString());
				else
					System.out.println("Invalid course name");
			}else if(option == 'L' || option == 'l'){
				break;
			}else{
				System.out.println("Invalid Choice");
			}
		}
	}
	
	private static void Q(){
		System.out.print("We congratulate you on your decision to do something more productive with your time.");
		System.exit(0);
	}
	
	private static void load(){
		try{
			FileInputStream file = new FileInputStream("storage.obj");
			ObjectInputStream inStream = new ObjectInputStream(file);
			database = (HashMap<String, Student>) inStream.readObject();
			courses = (HashMap<String, Course>) inStream.readObject();
			inStream.close();
		}catch(Exception e){
			System.out.println("Nothing to load");	
		}
	}
	
	
	public static void main(String[] args){
		courses = new HashMap<String, Course>();
		database = new HashMap<String, Student>();
		load();
		while(true){
			UImenu();
			System.out.print("Please select an option: ");
			String ans = System.console().readLine();
			char option = ans.charAt(0);
			if(option == 'L' || option == 'l'){ 
				System.out.print("Please enter webid: ");
				String webID = System.console().readLine();
				if(webID.equals("REGISTRAR")){
					regCycle();
				}else{
					Student tmp = database.get(webID);
					if(tmp != null)
						studCycle(tmp);
					else
						System.out.println("Invalid WebID");
				}
			}else if(option == 'X' || option == 'x'){ 
			try{
				FileOutputStream file = new FileOutputStream("storage.obj");
				ObjectOutputStream outStream = new ObjectOutputStream(file);
				outStream.writeObject(database);
				outStream.reset();
				outStream.writeObject(courses);
				outStream.close();
				System.out.println("Saved");
				Q();
			}catch(Exception e){
				
			}
			
			}else if(option == 'Q' || option == 'q'){ 
				Q();
			}
		}	
		
	}
}