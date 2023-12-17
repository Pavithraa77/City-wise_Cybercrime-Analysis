import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

interface Authenticator {
    boolean authenticate(String username, String password);
}

class Account {
    String username;
    String password;

    Account(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

class User extends Account implements Authenticator {
    String email;
    String phoneNumber;
    String secretQuestion;
    String secretAnswer;
    boolean isAuthority;

    User(String username, String password, String email, String phoneNumber, String secretQuestion, String secretAnswer,
            boolean isAuthority) {
        super(username, password);
        this.isAuthority = isAuthority;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;

    }

    String toCSVString() {
        return String.join(",", username, password, email, phoneNumber, secretQuestion, secretAnswer,
                String.valueOf(isAuthority));
    }

    @Override
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

class Complaint {
    private int crimeId;
    private String crimeDate;
    private String crimeType;
    private String motive;
    private String suspect;
    private String victimDetails;
    private String perpetratorName;
    private int perpetratorAge;
    private double lossIncurred;
    private String complaintDescription;

    public Complaint(int crimeId, String crimeDate, String crimeType, String motive, String suspect,
            String victimDetails, String perpetratorName, int perpetratorAge, double lossIncurred,
            String complaintDescription) {
        this.crimeId = crimeId;
        this.crimeDate = crimeDate;
        this.crimeType = crimeType;
        this.motive = motive;
        this.suspect = suspect;
        this.victimDetails = victimDetails;
        this.perpetratorName = perpetratorName;
        this.perpetratorAge = perpetratorAge;
        this.lossIncurred = lossIncurred;
        this.complaintDescription = complaintDescription;
    }

    // Getters
    public int getCrimeId() {
        return crimeId;
    }

    public String getCrimeDate() {
        return crimeDate;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public String getMotive() {
        return motive;
    }

    public String getSuspect() {
        return suspect;
    }

    public String getVictimDetails() {
        return victimDetails;
    }

    public String getPerpetratorName() {
        return perpetratorName;
    }

    public int getPerpetratorAge() {
        return perpetratorAge;
    }

    public double getLossIncurred() {
        return lossIncurred;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    // Setters
    public void setCrimeDate(String crimeDate) {
        this.crimeDate = crimeDate;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    public void setVictimDetails(String victimDetails) {
        this.victimDetails = victimDetails;
    }

    public void setPerpetratorName(String perpetratorName) {
        this.perpetratorName = perpetratorName;
    }

    public void setPerpetratorAge(int perpetratorAge) {
        this.perpetratorAge = perpetratorAge;
    }

    public void setLossIncurred(double lossIncurred) {
        this.lossIncurred = lossIncurred;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }
}

public class UserAuthentication {
    // HashMap to store user
    static Map<Integer, User> users = new HashMap<>();
    static int userIdCounter = 1;

    // HashMap to store complaints
    static Map<Integer, Complaint> complaints = new HashMap<>();

    public static void main(String[] args) {
        loadUsersFromCSV();
        loadCrimeFromCSV();// Load existing users from CSV
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("Do you have an account? (yes/no)");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                loggedIn = login(scanner);
            } else if (choice.equalsIgnoreCase("no")) {
                register(scanner);
            } else {
                System.out.println("Invalid choice. Please enter yes or no.");
            }
        }
    }

    static void loadUsersFromCSV() {
        String csvFileName = "userdeets.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7) {
                    String username = data[0];
                    String password = data[1];
                    String email = data[2];
                    String phoneNumber = data[3];
                    String secretQuestion = data[4];
                    String secretAnswer = data[5];
                    boolean isAuthority = Boolean.parseBoolean(data[6]);

                    User user = new User(username, password, email, phoneNumber, secretQuestion, secretAnswer,
                            isAuthority);
                    users.put(userIdCounter++, user);
                } else {
                    System.out.println("Invalid data format in CSV file!");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users from CSV: " + e.getMessage());
        }
    }

    static void loadCrimeFromCSV() {
        String csvFileName = "crimedeets.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 10) {
                    int crimeId = Integer.parseInt(data[0]);
                    String crimeDate = data[1];
                    String crimeType = data[2];
                    String motive = data[3];
                    String suspect = data[4].equals("null") ? null : data[4];
                    String victimDetails = data[5].equals("null") ? null : data[5];
                    String perpetratorName = data[6].equals("null") ? null : data[6];
                    int perpetratorAge = Integer.parseInt(data[7]);
                    double lossIncurred = Double.parseDouble(data[8]);
                    String complaintDescription = data[9];

                    Complaint complaint = new Complaint(crimeId, crimeDate, crimeType, motive, suspect,
                            victimDetails, perpetratorName, perpetratorAge, lossIncurred, complaintDescription);
                    complaints.put(crimeId, complaint);
                } else {
                    System.out.println("Invalid data format in CSV file.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading complaints from CSV: " + e.getMessage());
        }
    }

    static boolean login(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            User user = entry.getValue();
            if (user.authenticate(username, password)) {
                if (user.isAuthority) {
                    System.out.println("Authority login successful! Welcome, " + username);
                } else {
                    System.out.println("User login successful! Welcome, " + username);
                }
                loggedInOptions(user, scanner);
                updateUserInDataStructures(user);
                return true;
            }
        }
        System.out.println("Invalid username or password. Please try again.");
        return false;
    }

    static void loggedInOptions(User user, Scanner scanner) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nChoose an option:");
            System.out.println("1. View Profile");
            System.out.println("2. Update Profile");
            System.out.println("3. View Password");
            System.out.println("4. Logout");
            System.out.println("5. File a Complaint");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    viewProfile(user);
                    break;
                case "2":
                    updateProfile(user, scanner);
                    break;
                case "3":
                    passwordRecovery(user, scanner);
                    break;
                case "4":
                    loggedIn = false;
                    break;
                case "5":
                    fileComplaint(user, scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }

    static void viewProfile(User user) {
        System.out.println("Username: " + user.username);
        System.out.println("Email: " + user.email);
        System.out.println("Phone Number: " + user.phoneNumber);
    }

    static void updateProfile(User user, Scanner scanner) {
        System.out.println("Enter new email (press enter to keep the current value):");
        String newEmail = scanner.nextLine();
        if (!newEmail.isEmpty()) {
            user.email = newEmail;
            updateUserInDataStructures(user);
        }

        System.out.println("Enter new phone number (press enter to keep the current value):");
        String newPhoneNumber = scanner.nextLine();
        if (!newPhoneNumber.isEmpty()) {
            user.phoneNumber = newPhoneNumber;
            updateUserInDataStructures(user);
        }

        System.out.println("Profile updated successfully!");
    }

    static void updateUserInDataStructures(User user) {
        // Update HashMap
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            if (entry.getValue().username.equals(user.username)) {
                entry.getValue().email = user.email;
                entry.getValue().phoneNumber = user.phoneNumber;
                break;
            }
        }

        // Update CSV
        String csvFileName = "userdeets.csv";
        try {
            List<String> fileContent = new ArrayList<>();
            Files.lines(Paths.get(csvFileName))
                    .forEach(line -> {
                        String[] data = line.split(",");
                        if (data[0].equals(user.username)) {
                            data[2] = user.email;
                            data[3] = user.phoneNumber;
                        }
                        fileContent.add(String.join(",", data));
                    });

            Files.write(Paths.get(csvFileName), fileContent);
        } catch (IOException e) {
            System.out.println("Error updating CSV file: " + e.getMessage());
        }
    }

    static void passwordRecovery(User user, Scanner scanner) {
        System.out.println("Answer the following security question to recover your password:");
        System.out.println(user.secretQuestion);
        String answer = scanner.nextLine();
        if (user.secretAnswer.equalsIgnoreCase(answer)) {
            System.out.println("Your password is: " + user.password);
        } else {
            System.out.println("Incorrect answer. Password recovery failed.");
        }
    }

    static boolean isValidDateFormat(String date) {
        try {
            java.time.LocalDate.parse(date, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (java.time.format.DateTimeParseException e) {
            return false;
        }
    }

    static void fileComplaint(User user, Scanner scanner) {
        System.out.println("Please provide details about the complaint:");

        boolean isValidDate = false;
        String crimeDate = "";
        while (!isValidDate) {
            System.out.println("Crime date (YYYY-MM-DD):");
            crimeDate = scanner.nextLine();
            isValidDate = isValidDateFormat(crimeDate);

            if (!isValidDate) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        System.out.println("Crime type:");
        System.out.println("1. Cyber Theft");
        System.out.println("2. Cyber Fraud");
        System.out.println("3. Cyber Assault");
        String crimeTypeChoice = scanner.nextLine();
        String crimeType = getCrimeTypeFromChoice(crimeTypeChoice);

        System.out.println("Suspected Motive:");
        String motive = scanner.nextLine();
        if (motive.trim().isEmpty()) {
            motive = null;
        }

        System.out.println("Enter suspect (if any,else click enter):");
        String suspect = scanner.nextLine();

        if (suspect.isEmpty()) {
            suspect = null;
        }

        boolean isVictim = askIfUserIsVictim(scanner);
        String victimDetails = isVictim ? getUserDetailsAsString(user, scanner) : askVictimDetails(scanner);

        System.out.println("Perpetrator name (if known):");
        String perpetratorName = scanner.nextLine();

        System.out.println("Perpetrator age (if known):");
        int perpetratorAge = Integer.parseInt(scanner.nextLine());

        System.out.println("Loss incurred (in Rs if any):");
        double lossIncurred = Double.parseDouble(scanner.nextLine());

        System.out.println("Please provide a Complaint Description");
        String complaintDescription = scanner.nextLine();

        int crimeId = assignCrimeId();

        // Create a new Complaint object with collected details
        Complaint newComplaint = new Complaint(crimeId, crimeDate, crimeType, motive, suspect,
                victimDetails, perpetratorName, perpetratorAge, lossIncurred, complaintDescription);

        // Store complaint in HashMap
        complaints.put(crimeId, newComplaint);
    }

    // Helper method to map crime type choices to actual crime types
    static String getCrimeTypeFromChoice(String choice) {
        switch (choice) {
            case "1":
                return "Theft";
            case "2":
                return "Fraud";
            case "3":
                return "Assault";
            default:
                return "Other";
        }
    }

    // Helper method to check if the user is the victim
    static boolean askIfUserIsVictim(Scanner scanner) {
        System.out.println("Are you the victim? (yes/no)");
        String victimChoice = scanner.nextLine();
        return victimChoice.equalsIgnoreCase("yes");
    }

    // Helper method to get victim details if user is not the victim
    static String askVictimDetails(Scanner scanner) {
        System.out.println("Enter victim's name:");
        String name = scanner.nextLine();

        System.out.println("Enter victim's age:");
        String age = scanner.nextLine();

        return name + "," + age;
    }

    // Helper method to get user details as a string
    static String getUserDetailsAsString(User user, Scanner scanner) {
        System.out.println("Enter your Age");
        String age = scanner.nextLine();
        return user.username + "," + age;
    }

    // Implement the logic to assign a unique crime ID
    static int lastAssignedCrimeID = 1000; // Initial starting ID for crimes

    // Method to assign a unique crime ID
    static int assignCrimeId() {
        return ++lastAssignedCrimeID;
    }

    static boolean isUsernameUnique(String username) {
        for (User user : users.values()) {
            if (user.username.equals(username)) {
                return false;
            }
        }
        return true;
    }

    static boolean isValidPassword(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        return password.matches(pattern);
    }

    static boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailPattern);
    }

    static boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^[0-9]{10}$";
        return phoneNumber.matches(phonePattern);
    }

    static void register(Scanner scanner) {
        boolean isValidUsername = false;
        boolean isValidPassword = false;
        boolean isValidEmail = false;
        boolean isValidPhoneNumber = false;
        boolean isAuthority = false;

        while (true) {
            System.out.println("Are you an authority? (yes/no)");
            String authorityChoice = scanner.nextLine();

            if (authorityChoice.equalsIgnoreCase("yes")) {
                isAuthority = true;
                break;
            } else if (authorityChoice.equalsIgnoreCase("no")) {
                break;
            } else {
                System.out.println("Please enter a valid option (yes/no).");
            }
        }

        String username = "";
        while (true) {
            System.out.println("Create a unique username:");
            username = scanner.nextLine();
            if (isUsernameUnique(username)) {
                isValidUsername = true;
                break;
            } else {
                System.out.println("Username already exists. Please choose a different username.");
            }
        }

        String password = "";
        while (true) {
            System.out.println(
                    "Create a password (at least 6 characters with uppercase, lowercase, number, and special character):");
            password = scanner.nextLine();
            if (isValidPassword(password)) {
                isValidPassword = true;
                break;
            } else {
                System.out.println(
                        "Invalid password format. Ensure it contains at least 6 characters with uppercase, lowercase, number, and special character.");
            }
        }

        String email = "";
        while (true) {
            System.out.println("Enter your email:");
            email = scanner.nextLine();
            if (isValidEmail(email)) {
                isValidEmail = true;
                break;
            } else {
                System.out.println("Invalid email format. Please enter a valid email address.");
            }
        }

        String phoneNumber = "";
        while (true) {
            System.out.println("Enter phone number:");
            phoneNumber = scanner.nextLine();
            if (isValidPhoneNumber(phoneNumber)) {
                isValidPhoneNumber = true;

                break;
            } else {
                System.out.println("Invalid phone number format. Please enter a valid phone number.");
            }
        }

        String secretQuestion = "";
        System.out.println("Enter secret question:");
        secretQuestion = scanner.nextLine();

        String secretAnswer = "";
        System.out.println("Enter secret answer:");
        secretAnswer = scanner.nextLine();

        if (isValidUsername && isValidPassword && isValidEmail && isValidPhoneNumber) {
            User newUser = new User(username, password, email, phoneNumber, secretQuestion, secretAnswer, isAuthority);
            users.put(userIdCounter++, newUser);
            writeUserDataToCSV(newUser);
            System.out.println("Registration successful! Your user ID is: " + (userIdCounter - 1));
        }
    }

    static void writeUserDataToCSV(User user) {
        String csvFileName = "userdeets.csv";
        try (FileWriter writer = new FileWriter(csvFileName, true)) {
            writer.append(user.toCSVString()).append("\n");
            System.out.println("wrote ");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
