import java.util.*;
import java.text.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


class ProjectMain{
    static Scanner scanner = new Scanner(System.in);
    static String[] sections = {"ARTS", "SCIENCES", "NEWSPAPERS", "LAW"};
    static String[] collections = {};
    public static void mainMenu(){
        System.out.println("University of Java Library System");
        System.out.println("Menu Options");
        System.out.println("1. New Membership");
        System.out.println("2. New Collection");
        System.out.println("3. Remove Membership");
        System.out.println("4. Remove Item from Collection");
        System.out.println("5. New Employee");
        System.out.println("6. Borrow Item");
        System.out.println("7. Return Item");
        System.out.println("8. Check overdues");
        System.out.println("9. Quit");
        System.out.println("");
    }
    //You can either implement your events in these functions, or you can write an Event class and call a static function here.

    public static void newMemberEvent(String name, String address, Date dob, String email, String stringSSN, String memtype) throws Exception{
        SSN ssn = new SSN(stringSSN);
        switch(memtype){
          case "Professor":
            Professor professor = new Professor(name, address, dob, email, ssn);
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = dateFormat.format(dob);
            System.out.println("Professor Successfully Created.");
            System.out.println("\nNew Professor Information: \n-----------------------\nMember ID: " + professor.getMemberId() + "\nName: "+ professor.getName() + "\nMember Type: Professor\nAddress: " + professor.getAddress() + "\nDate of Birth: " + formattedDate + "\nEmail: " + professor.getEmail() + "\n-----------------------");
            professor.saveTo("membershipdatabasefile.txt");
            break;
          case "Student":
            Student student = new Student(name, address, dob, email, ssn);
            DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate2 = dateFormat2.format(dob);
            System.out.println("Student Successfully Created.");
            System.out.println("\nNew Student Information: \n-----------------------\nMember ID: " + student.getMemberId() + "\nName: "+ student.getName() + "\nMember Type: Student\nAddress: " + student.getAddress() + "\nDate of Birth: " + formattedDate2 + "\nEmail: " + student.getEmail() + "\n-----------------------");     
            student.saveTo("membershipdatabasefile.txt");
            break;
        case "External":
            External external = new External(name, address, dob, email, ssn);
            DateFormat dateFormat3 = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate3 = dateFormat3.format(dob);
            System.out.println("External Successfully Created.");
            System.out.println("\nNew External Information: \n-----------------------\nMember ID: " + external.getMemberId() + "\nName: "+ external.getName() + "\nMember Type: External\nAddress: " + external.getAddress() + "\nDate of Birth: " + formattedDate3 + "\nEmail: " + external.getEmail() + "\n-----------------------"); 
            external.saveTo("membershipdatabasefile.txt");
            break;

        }


    };
    public static void newCollectionEvent(String newCollection) throws Exception {
        boolean alreadyExists = false;
        for (String collection : collections) {
            if (collection.equalsIgnoreCase(newCollection)) {
                alreadyExists = true;
                break;
            }
        }
        if (alreadyExists) 
        {
            System.out.println("Collection already exists in array.");
        } else 
        {
            // Open the file for writing
            FileWriter fileWriter = new FileWriter("collections.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Write the new collection to the file
            bufferedWriter.write(newCollection.toUpperCase());
            bufferedWriter.newLine();

            // Close the BufferedWriter object to flush the data to the file and release resources
            bufferedWriter.close();
            String[] newCollections = new String[collections.length + 1];
            System.arraycopy(collections, 0, newCollections, 0, collections.length);
            newCollections[collections.length] = newCollection.toUpperCase();
            collections = newCollections;
            System.out.println("Collection added to array.");
            for (String collection : collections) {
                System.out.print(collection + " ");
            }
            System.exit(0);
        }
    }

    public static void newRemoveMemberEvent(String ssnStr) throws IOException {
        ArrayList<String> members = readFileToList("membershipdatabasefile.txt");
        ArrayList<String> updatedMembers = new ArrayList<>();
    
        for (String item : members) {
            String[] words = item.split(", ");
            String name = words[0];
            String address = words[1];
            String email = words[3];
            String ssn = words[4];
            String memID = words[5];
    
            if (ssn.equals(ssnStr)) {
                // Skip this item since it matches the input SSN
                continue;
            }
    
            updatedMembers.add(item);
        }
    
        writeListToFile(updatedMembers, "membershipdatabasefile.txt");
    }
    public static void writeListToFile(ArrayList<String> list, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (String line : list) {
            writer.write(line + "\n");
        }
        writer.close();
    }


    public static ArrayList<String> readFileToList(String fileName) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            list.add(line);
        }
        reader.close();
        return list;
    }

    public static void removesCollectionEvent(String collectionToRemove) {
        try {
            // Read the contents of the file into a StringBuilder object
            File file = new File("collections.txt");
            Scanner fileScanner = new Scanner(file);
            StringBuilder fileContents = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().toUpperCase();
                if (!line.equals(collectionToRemove)) {
                    fileContents.append(line).append(System.lineSeparator());
                }
            }
            fileScanner.close();
    
            // Write the updated contents back to the file
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileContents.toString());
            fileWriter.close();
    
            // Remove the collection from the collections array
            String[] newCollections = new String[collections.length - 1];
            int j = 0;
            for (int i = 0; i < collections.length; i++) {
                if (!collections[i].equalsIgnoreCase(collectionToRemove)) {
                    newCollections[j] = collections[i];
                    j++;
                }
            }
            collections = newCollections;
    
            System.out.println("Collection removed from file and array.");
            for (String collection : collections) {
                System.out.print(collection + " ");
            }
        } catch (IOException e) {
            System.out.println("Error removing collection: " + e.getMessage());
        }
    }
    public static void newEmployeeEvent(String name, String address, String dobString, String email, String stringSSN) throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date dob = formatter.parse(dobString);
        SSN ssn = new SSN(stringSSN);
        Employee emp = new Employee(name, address, dob, email, ssn);
        System.out.println("\nCreating new employee...");
        emp.saveTo("employeedatabasefile.txt");
        System.out.println("\nEmployee added to database.\n");
    };
    public static void newBorrowsEvent() throws Exception
    {
        Librarian.newBorrowsEvent();
    };
    public static void newReturnEvent()
    {
        Librarian.newReturnsEvent(null, null);
    };

    public static void newCheckOverdues() throws Exception{
        Librarian.newCheckOverdues();
    }
    //You are free to implememnt other events that you see needs to be implemented
    // DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    // String formattedDate = dateFormat.format(dob);
    public static void main(String [] args)throws Exception{
        // ProjectMain.mainMenu();
        // System.out.print("Enter your option number: ");
        // int option = scanner.nextInt();
        // // while(true){
        //     switch (option) {
        //         case 1:
        //         try {
        //             ProjectMain.newMemberEvent();
        //             break;
        //         } catch (Exception e) {
        //             // TODO: handle exception
        //         }
        //         case 2:
        //             try {
        //                 ProjectMain.newCollectionEvent();
        //                 break;
        //             } catch (Exception e) {
        //                 // TODO: handle exception
        //             }
        //         case 3:
        //             ProjectMain.newRemoveMemberEvent();
        //             break;
        //         case 4:
        //         try {
        //             ProjectMain.removesCollectionEvent();
        //             break;
        //         } catch (Exception e) {
        //             // TODO: handle exception
        //         }
        //         case 5:
        //             ProjectMain.newEmployeeEvent();
        //             break;
        //         case 6:
        //         try{
        //             ProjectMain.newBorrowsEvent();
        //             break;
        //         } catch (Exception e) {
        //             // TODO: handle exception
        //         }
        //         case 7:
        //             ProjectMain.newReturnEvent();
        //             break;
        //         case 8:
        //             ProjectMain.newCheckOverdues();
        //             break;
        //         case 9:
        //             System.exit(0);
        //             break;
        //         default:
        //             System.out.println("Invalid operator.");
        //     //         continue;
        //     // }
        // }
    }
}
