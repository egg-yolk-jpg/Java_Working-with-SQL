/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sql_injection_assignment;

/**
 *
 * name: Yakimah Wiley 
 * assignment: M8 - SQL Injection
 * date: 4/7/2025 
 * class: CMPSC222 - Secure Coding
 *
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This program is an exploration for safe handling of SQL.
 * The primary focus of this application is one using prepared statements
 * to securely store user input into a relational database.
 * 
 * This application is based around storing student and course data into 
 * Microsoft Access using Java.
 * @author Apache_PHP
 */
public class SQL_Injection_Assignment {
    /**
     * The main function is in charge of determining what type of data the
     * user wants to input.
     * 
     * Users are first directed to the menu function to select the operation 
     * they want to complete. The value selected is then used within a switch 
     * statement to determine which values the user needs to supply to the application.
     * 
     * This process is continues for as long as the user doesn't request to 
     * exit the program
     * @param args 
     */
    public static void main(String[] args) {
        //used to determine when the loop is terminated, thereby exiting the program
        Boolean exit = false;
        
        //constant variable used to determine regex conditions
        final int SNAME_SIZE = 50;
        final int MAJOR_SIZE  = 30;
        final int EMAIL_SIZE  = 50;
        final int CID_SIZE  = 50;
        final int SEMESTER_SIZE  = 20;

        while(exit == false){
            //userMenu determines which operation the user wants to work on
            int selection = userMenu();
            switch (selection) {
                case 1 -> {
                    System.out.println("""
                                   You have selected to INSERT A COURSE.
                                       Math and Calculus will now be added to the database.""");
                    //The courses are predefined in this operation
                    insertCourse("Math", "3", "Fall");
                    insertCourse("Calculus", "4", "Fall");

                    break;
                }
                case 2 -> {
                    System.out.println("""
                                   You have selected to UPDATE COURSE CREDIT.
                                   Please follow the below instructions:""");

                    //The user determines which course is updated
                    System.out.println("Enter the name of the course:");
                    String course = checkString(CID_SIZE, "cid");
                    
                    //Database call
                    updateData(course);
                    break;
                }
                case 3 -> {
                    System.out.println("""
                                   You have selected to SELECT A COURSE.
                                   Please follow the below instructions:""");

                    //The user specifies which courses are returned based on semester and cedit hours
                    System.out.println("Enter the number of credit hours:");
                    String credits = checkInt();

                    System.out.println("Enter the semester:");
                    String semester = checkString(SEMESTER_SIZE, "general");
                    
                    //Database call
                    selectCourse(semester, credits);
                    break;
                }
                case 4 -> {
                    System.out.println("""
                                   You have selected to INSERT A STUDENT.
                                   Please follow the below instructions:""");

                    //The user provides all relevant details pertaining to the student
                    System.out.println("Enter the student's ID number:");
                    String student_id = checkInt();

                    System.out.println("Enter the name of the student:");
                    String student_name = checkString(SNAME_SIZE, "general");

                    System.out.println("Enter the selected major:");
                    String major = checkString(MAJOR_SIZE, "general");

                    System.out.println("Enter the student's email:");
                    String email = checkString(EMAIL_SIZE, "email");
                    
                    //Database call
                    insertStudent(student_id, student_name, major, email);
                    break;
                }
                case 5 -> {
                    System.out.println("""
                                   You have selected to DELETE A STUDENT.
                                   Please follow the below instructions:""");

                    //The user provides the name of the student to be deleted
                    System.out.println("Enter the name of the student:");
                    String student_name = checkString(SNAME_SIZE, "general");
                    
                    //Database call
                    deleteStudent(student_name);
                    break;
                }
                case 6 -> {
                    //The condition required to exit the program is triggered
                    System.out.println("Thank you and have a great day!");
                    exit = true;
                    break;
                }
            }
        }
    }
    
    /**
     * This function requests the user select an operation to be completed.
     * It then passes the provided value through a validation process before
     * returning it to the callinig function
     * @return 
     */
    private static int userMenu(){
        Scanner scan = new Scanner(System.in);
        System.out.println("""
                           
                           Select an option (1-6)
                           1. Insert a course
                           2. Update course credit
                           3. Select a course
                           4. Insert a student
                           5. Delete a student
                           6. Exit program
                           """);
        String response = scan.nextLine();
        //Verifies that the user selected a valid option and returns the associated index
        return validateMenuSelection(response);
    }
    
    /**
     * This function validates the provided input from the function above.
     * It relies on two processes. The first process assumes that the user
     * provided a numeric value. It this is true, the program verifies that 
     * the value is between 1 and 6 inclusive.
     * 
     * Otherwise, the program checks if the user provided a non numeric response 
     * that matches 1 of 2 potential entries.
     * @param selection
     * @return 
     */
    private static int validateMenuSelection(String selection){
        int select;
        //Entry type 1: the string matches the origianl options
        ArrayList<String> option_set1 = new ArrayList<>() {
            {
                add("Insert a course");
                add("Update course credit");
                add("Select a course");
                add("Insert a student");
                add("Delete a student");
                add("Exit program");
            }
        };
        
        //Entry type 2: the string is a short hand of the original list
        ArrayList<String> option_set2 = new ArrayList<>() {
            {
                add("Insert course");
                add("Update credit");
                add("Select course");
                add("Insert student");
                add("Delete student");
                add("Exit");
            }
        };
        
        try{
            //Checks if the user gave a numeric value and verifies that it is within range
            select = Integer.parseInt(selection);
            if(select > 0 && select <=6){
                return select;
            }else{
                //if the value is numeric but not within range, the user must provide new input
                System.out.println("\nInvalid Selection. Select a number between 1 and 5.");
                return userMenu();
            }
        }catch(Exception ex){
            //if the user-provided input is non-numeric, the input is comepared to the above two option sets
            for(String option: option_set1){
                if(option.toLowerCase().equals(selection.toLowerCase())){
                    return option_set1.indexOf(option) + 1;
                }            
            }
            for (String option : option_set2) {
                if (option.toLowerCase().equals(selection.toLowerCase())) {
                    return option_set2.indexOf(option) + 1;
                }
            }
            
            //If the user input doesn't match either option set, then they must provide a new input
            System.out.println("Invalid Selection. Select a number between 1 and 5.");
            return userMenu();
        }
    }
    
    /**
     * This function is in charge of completing the query to insert a new
     * course into the database
     * @param course_name
     * @param credits
     * @param semester 
     */
    private static void insertCourse(String course_name, String credits, String semester){
        try{
            Connection conn = getConnection();
            String query = "Insert into Course(cid, credits, semester) values(?, ?, ?)";
            PreparedStatement p_stmt = conn.prepareStatement(query);
            p_stmt.setString(1, course_name);
            p_stmt.setString(2,  credits);
            p_stmt.setString(3, semester);
            int updated = p_stmt.executeUpdate();
            System.out.printf("""
                               Insert Successful! (%d)
                              """, updated);
            p_stmt.close();
            conn.close();
        }catch(SQLException ex){
            System.out.println(ex);
        }
    }
    
    /**
     * This function is in charge of completing the query to insert a new student
     * into the database
     * @param student_id
     * @param student_name
     * @param major
     * @param email 
     */
    private static void insertStudent(String student_id, String student_name, String major, String email){
        try {
            Connection conn = getConnection();
            String query = "Insert into Student(sid, sname, major, email) values(?, ?, ?, ?)";
            PreparedStatement p_stmt = conn.prepareStatement(query);
            p_stmt.setString(1, student_id);
            p_stmt.setString(2, student_name);
            p_stmt.setString(3, major);
            p_stmt.setString(4, email);
            int updated = p_stmt.executeUpdate();
            System.out.printf("""
                               Insert Successful! (%d)
                              """, updated);
            p_stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }
    
    /**
     * This function is in charge of setting the number of credit hours for a 
     * course to 5
     * @param course_name 
     */
    private static void updateData(String course_name){
        try {
            Connection conn = getConnection();
            String query = "Update Course Set credits = 5 where cid = ?";
            PreparedStatement p_stmt = conn.prepareStatement(query);
            p_stmt.setString(1, course_name);
            int updated = p_stmt.executeUpdate();
            System.out.printf("""
                               Insert Successful! (%d)
                              """, updated);
            p_stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * This function is responsible for selecting the data from the Courses
     * table that match the conditionals provided by the user
     * @param semester
     * @param credits 
     */
    private static void selectCourse(String semester, String credits){
        ArrayList<String> courses = new ArrayList();
        try {
            Connection conn = getConnection();
            String query = "Select * from Course where semester = ? and credits = ?";
            PreparedStatement p_stmt = conn.prepareStatement(query);
            p_stmt.setString(1, semester);
            p_stmt.setString(2, credits);
            ResultSet rs = p_stmt.executeQuery();
            while (rs.next()) {
                courses.add(rs.getString("cid"));
            }
            rs.close();
            p_stmt.close();
            conn.close();
            System.out.printf("""
                              Courses during %s semester worth %s credit hours:
                              -----------------------------------------------\n""", semester, credits);
            for(String course: courses){
                System.out.println(course);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * This function deletes the student specified by the user
     * @param student_name 
     */
    private static void deleteStudent(String student_name){
        try {
            Connection conn = getConnection();
            String query = "Delete from Student where sname = ?";
            PreparedStatement p_stmt = conn.prepareStatement(query);
            p_stmt.setString(1, student_name);
            int updated = p_stmt.executeUpdate();
            System.out.printf("""
                               Delete Successful! (%d)
                              """, updated);
            p_stmt.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * This function sets up the connection for all functions that complete
     * a database call
     * @return 
     */
    private static Connection getConnection(){
        String database = "jdbc:ucanaccess://c://CMPSC222//SQL_Injection.accdb";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(database);
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(SQL_Injection_Assignment.class.getName()).log(Level.SEVERE, "Problem with DB connection", ex);
        }
        return conn;
    }
    
    /**
     * This function creates the scanner used by all functions that requires user
     * inputs. It is called within each of those functions
     * @return 
     */
    private static String getInput(){
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }
    
    /**
     * This function is in charge of input validation for strings.
     * It obtains the expected size and the string type from the calling function
     * then it obtains the user input and runs it through regex for validation.
     * The regex is formatted with the expected size, to ensure that each string
     * type can be no larger than the expectation of the respective column 
     * within the database
     * @param size
     * @param type
     * @return 
     */
    private static String checkString(int size, String type){
        String input = getInput();
        String regex = null;
        switch(type){
            case "general" ->{
                regex = String.format("^[a-z \\-'A-Z]{1,%d}$", size);
                break;
            }
            case "cid" ->{
                regex = String.format("^[a-z \\-A-Z0-9]{1,%d}$", size);
                break;
            }
            case "email" ->{
                regex = String.format("^[a-z \\-'A-Z@.0-9]{1,%d}$", size);
                break;
            }
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(input);
        
        if(match.find()){
            return input;
        }else{
            System.out.println("Invalid input");
            return checkString(size, type);
        }
    }
    
    /**
     * This function is in charge of the input validation for values that
     * require integers. All it does is verify that the value can be cast as an
     * integer and (if so) returns the value to the calling function as is.
     * @return 
     */
    private static String checkInt(){
        String input = getInput();
        try{
            Integer.parseInt(input);
            return input;
        }catch(Exception ex){
            System.out.println("Invalid input");
            return checkInt();
        }
    }
}
