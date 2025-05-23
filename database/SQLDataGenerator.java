import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SQLDataGenerator {

    private static final Random rand = new Random();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static int randBetween(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    private static String sqlTimestamp(LocalDateTime dt) {
        return "'" + dt.format(dtf) + "'";
    }

    private static String randomName() {
        String[] firstNames = { "Rajesh", "Sneha", "Amit", "Anil", "Neha", "Vikas", "Priya", "Rohit", "Pooja",
                "Sanjay" };
        String[] lastNames = { "Kumar", "Sharma", "Joshi", "Singh", "Verma", "Gupta", "Patel", "Mehta", "Malhotra",
                "Kapoor" };
        return firstNames[rand.nextInt(firstNames.length)] + " " + lastNames[rand.nextInt(lastNames.length)];
    }

    private static String randomCity() {
        String[] cities = { "Mumbai", "Delhi", "Bangalore", "Pune", "Chennai", "Hyderabad", "Ahmedabad", "Kolkata" };
        return cities[rand.nextInt(cities.length)];
    }

    private static String randomStatus() {
        String[] statuses = { "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED" };
        return statuses[rand.nextInt(statuses.length)];
    }

    public static void main(String[] args) {
        int deptCount = randBetween(5, 7);
        int departmentId = 1, courseId = 100, userId = 1, leadId = 1;
        int followupId = 1, nodeId = 1;

        String[] departmentNames = {
                "Computer Science", "Mechanical Engineering", "Electrical Engineering",
                "Civil Engineering", "Electronics", "IT", "Biotech"
        };

        String[] courseNames = {
                "Software Engineering", "Data Structures", "OS", "DBMS", "Networks",
                "Thermodynamics", "Heat Transfer", "Machine Design", "Manufacturing", "Control Systems"
        };

        System.out.println("-- DEPARTMENTS");
        for (int i = 0; i < deptCount; i++) {
            String deptName = departmentNames[i % departmentNames.length];
            LocalDateTime createdAt = LocalDateTime.now();
            System.out.printf(
                    "INSERT INTO department (id, name, created_at, updated_at, is_deleted) VALUES (%d, '%s', %s, %s, FALSE);%n",
                    departmentId, deptName, sqlTimestamp(createdAt), sqlTimestamp(createdAt));
            departmentId++;
        }
        departmentId = 1;

        System.out.println("\n-- COURSES");
        for (int d = 0; d < deptCount; d++) {
            int coursePerDept = randBetween(3, 5);
            for (int c = 0; c < coursePerDept; c++) {
                String courseName = courseNames[(d * 5 + c) % courseNames.length];
                LocalDateTime createdAt = LocalDateTime.now();
                System.out.printf(
                        "INSERT INTO course (code, name, department_id, created_at, updated_at, is_deleted) VALUES (%d, '%s', %d, %s, %s, FALSE);%n",
                        courseId, courseName, departmentId, sqlTimestamp(createdAt), sqlTimestamp(createdAt));
                courseId++;
            }
            departmentId++;
        }
        departmentId = 1;

        System.out.println("\n-- USERS");
        for (int d = 0; d < deptCount; d++) {
            int userPerDept = randBetween(3, 5);
            for (int e = 0; e < userPerDept; e++) {
                String name = randomName();
                String email = name.toLowerCase().replaceAll(" ", ".") + userId + "@example.com";
                String phone = "9000000" + String.format("%03d", userId);
                String address = randomCity() + ", State";
                String role = "FOLLOWUP_EMPLOYEE";
                LocalDateTime createdAt = LocalDateTime.now();
                System.out.printf(
                        "INSERT INTO app_user (id, name, email, password, phone_number, address, created_at, updated_at, is_deleted, role, is_super, department_id) "
                                +
                                "VALUES (%d, '%s', '%s', 'pass123', '%s', '%s', %s, %s, FALSE, '%s', FALSE, %d);%n",
                        userId, name, email, phone, address, sqlTimestamp(createdAt), sqlTimestamp(createdAt), role,
                        departmentId);
                userId++;
            }
            departmentId++;
        }

        System.out.println("\n-- LEADS");
        for (int i = 1; i <= 20; i++) {
            String name = randomName();
            String email = name.toLowerCase().replaceAll(" ", ".") + leadId + "@leadmail.com";
            String phone = "9876543" + String.format("%04d", i);
            String address = randomCity() + ", State";
            LocalDateTime createdAt = LocalDateTime.now();
            System.out.printf(
                    "INSERT INTO lead (id, name, email, phone_number, address, created_at, updated_at, is_deleted) " +
                            "VALUES (%d, '%s', '%s', '%s', '%s', %s, %s, FALSE);%n",
                    leadId++, name, email, phone, address, sqlTimestamp(createdAt), sqlTimestamp(createdAt));
        }

        System.out.println("\n-- FOLLOWUPS AND NODES");

        int totalUsers = userId - 1;
        int totalLeads = leadId - 1;
        int totalCourses = courseId - 100;
        Set<Integer> usedLeads = new HashSet<>();

        for (int u = 1; u <= totalUsers; u++) {
            if (usedLeads.size() >= totalLeads)
                break;

            int followupsPerUser = randBetween(1, 2); // Less followups to not exceed unique leads
            for (int f = 0; f < followupsPerUser; f++) {
                // Get a unique unused lead ID
                int randomLead;
                int attempts = 0;
                do {
                    randomLead = randBetween(1, totalLeads);
                    attempts++;
                    if (attempts > totalLeads)
                        break;
                } while (usedLeads.contains(randomLead));

                if (usedLeads.contains(randomLead))
                    continue;
                usedLeads.add(randomLead);

                String desc = "Followup for user " + u;
                String status = randomStatus();
                LocalDateTime dueDate = LocalDateTime.now().plusDays(randBetween(1, 10));
                int courseAssigned = 100 + randBetween(0, totalCourses - 1);
                LocalDateTime createdAt = LocalDateTime.now();

                System.out.printf(
                        "INSERT INTO followup (id, lead_id, description, status, due_date, created_at, updated_at, is_deleted, course_id, employee_id) "
                                +
                                "VALUES (%d, %d, '%s', '%s', %s, %s, %s, FALSE, %d, %d);%n",
                        followupId, randomLead, desc, status, sqlTimestamp(dueDate), sqlTimestamp(createdAt),
                        sqlTimestamp(createdAt), courseAssigned, u);

                int nodes = randBetween(3, 5);
                for (int n = 0; n < nodes; n++) {
                    String title = "Node " + (n + 1);
                    String body = "Details of node " + (n + 1) + " for followup " + followupId;
                    System.out.printf(
                            "INSERT INTO followup_node (id, title, body, date, done_by, created_at, is_deleted, followup_id) "
                                    +
                                    "VALUES (%d, '%s', '%s', %s, %d, %s, FALSE, %d);%n",
                            nodeId++, title, body, sqlTimestamp(createdAt), u, sqlTimestamp(createdAt), followupId);
                }

                followupId++;
            }
        }

        System.out.println("\n-- DONE.");

        System.out.println("\n-- SYNC SEQUENCES");
        System.out.println("SELECT setval('department_id_seq', (SELECT MAX(id) FROM department));");
        System.out.println("SELECT setval('app_user_id_seq', (SELECT MAX(id) FROM app_user));");
        System.out.println("SELECT setval('lead_id_seq', (SELECT MAX(id) FROM lead));");
        System.out.println("SELECT setval('followup_id_seq', (SELECT MAX(id) FROM followup));");
        System.out.println("SELECT setval('followup_node_id_seq', (SELECT MAX(id) FROM followup_node));");

    }
}
