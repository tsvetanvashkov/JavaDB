import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Engine  implements Runnable{

    private final EntityManager entityManager;
    private final BufferedReader bufferedReader;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Enter ex number:");
        try {
            int exNumber = Integer.parseInt(bufferedReader.readLine());
            switch (exNumber){
                case 2 -> changeCasing();
                case 3 -> containsEmployee();
                case 4 -> employeesWithSalaryOver();
                case 5 -> employeesFromDepartment();
                case 6 -> addingANewAddressAndUpdatingEmployee();
                case 7 -> addressesWithEmployeeCount();
                case 8 -> getEmployeeWithProject();
                case 9 -> findLatestProjects(10);
                case 10 -> increaseSalaries();
                case 11 -> findEmployeesByFirstName();
                case 12 -> employeesMaximumSalaries();
                case 13 -> removeTowns();


            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void removeTowns() throws IOException {
        System.out.println("Enter town name:");
        String townName = bufferedReader.readLine();

        Town town = entityManager.createQuery("SELECT t FROM Town t " +
                "WHERE t.name = :t_name", Town.class)
                .setParameter("t_name", townName)
                .getSingleResult();
        int affectedRows = removeAddressesByTownId(town.getId());
        entityManager.getTransaction().begin();
        entityManager.remove(town);
        entityManager.getTransaction().commit();

        System.out.printf("%d address in %s is deleted", affectedRows, townName);
    }

    private int removeAddressesByTownId(Integer id) {
        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a " +
                "WHERE a.town.id = :p_id", Address.class)
                .setParameter("p_id", id)
                .getResultList();
        entityManager.getTransaction().begin();
        addresses.forEach(entityManager::remove);
        entityManager.getTransaction().commit();
        return addresses.size();
    }

    @SuppressWarnings("unchecked")
    private void employeesMaximumSalaries() {
        List<Object[]> rows = entityManager.createNativeQuery("SELECT d.name, MAX(e.salary) AS m_salary " +
                "FROM departments AS d " +
                "JOIN employees AS e ON d.department_id = e.department_id " +
                "GROUP BY d.name " +
                "HAVING m_salary NOT BETWEEN 30000 AND 70000;")
                .getResultList();
        rows.forEach(r ->{
            System.out.printf("%s %s\n", r[0], r[1]);
        });
    }

    private void findEmployeesByFirstName() throws IOException {
        System.out.println("Enter pattern for search:");
        String pattern = bufferedReader.readLine();
        List<Employee> employees = entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.firstName LIKE :pat", Employee.class)
                .setParameter("pat", pattern + "%")
                .getResultList();
        employees.forEach(employee -> {
            System.out.printf("%s %s - %s - ($%.2f)\n",
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getJobTitle(),
                    employee.getSalary());
        });
    }

    private void increaseSalaries() {
        entityManager.getTransaction().begin();
        int affectedRows = entityManager.createQuery("UPDATE Employee e " +
                "SET e.salary = e.salary * 1.12 " +
                "WHERE e.department.id IN :ids")
                .setParameter("ids", Set.of(1, 2, 4, 11))
                .executeUpdate();
        entityManager.getTransaction().commit();
        System.out.println(affectedRows);
    }

    private void findLatestProjects(int i) {
        List<Project> projects = entityManager.createQuery("SELECT p FROM Project p " +
                "GROUP BY p.startDate " +
                "ORDER BY p.startDate DESC ", Project.class)
                .setMaxResults(i)
                .getResultList();

        projects.forEach(project -> {
            System.out.printf("Project name: %s\n" +
                    "\tProject Description: %s\n" +
                    "\tProject Start Date: %s\n" +
                    "\tProject End Date: %s\n",
                    project.getName(),
                    project.getDescription(),
                    project.getStartDate(),
                    project.getEndDate() == null ? "null" : project.getEndDate());
        });

    }

    private void getEmployeeWithProject() throws IOException {
        System.out.println("Enter employee id:");
        int employeeId = Integer.parseInt(bufferedReader.readLine());
        Employee employee = entityManager.find(Employee.class, employeeId);
        List<Project> projects = projectsByEmployeeId(employeeId);
        System.out.printf("%s %s - %s\n",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getJobTitle());
                employee.getProjects().toArray();
        projects.forEach(project -> {
            System.out.println(project.getName());
        });
    }

    private List<Project> projectsByEmployeeId(int employeeId) {
        List<Project> projects = entityManager.createQuery("SELECT p FROM Project p " +
                //"WHERE p.employees.id = :e_id " +
                "ORDER BY p.name ASC", Project.class)
                //.setParameter("e_id", employeeId)
                .getResultList();

        return projects;
    }


    private void addressesWithEmployeeCount() {
        List<Address> addresses = entityManager.createQuery("SELECT a FROM Address a " +
                "ORDER BY a.employees.size DESC", Address.class)
                .setMaxResults(10)
                .getResultList();
        addresses.forEach(address -> {
            System.out.printf("%s, %s - %d employees\n",
                    address.getText(),
                    address.getTown() == null
                            ? "Unknown" : address.getTown().getName(),
                    address.getEmployees().size());
        });

    }

    private void addingANewAddressAndUpdatingEmployee() throws IOException {
        System.out.println("Enter employee last name:");
        String lastName = bufferedReader.readLine();

        Employee employee = entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.lastName = :l_name", Employee.class)
                .setParameter("l_name", lastName)
                .getSingleResult();

        Address address = createAddress("Vitoshka 15");
        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();
    }

    private Address createAddress(String addresText) {
        Address address = new Address();
        address.setText(addresText);
        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();
        return address;
    }

    private void employeesFromDepartment() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.department.name = :d_name " +
                "ORDER BY e.salary, e.id", Employee.class)
                .setParameter("d_name", "Research and Development")
                .getResultStream()
                .forEach(employee -> {
                    System.out.printf("%s %s from %s - $%.2f\n",
                            employee.getFirstName(),
                            employee.getLastName(),
                            employee.getDepartment().getName(),
                            employee.getSalary());
                });

    }

    private void employeesWithSalaryOver() {
        entityManager.createQuery("SELECT e FROM Employee e " +
                "WHERE e.salary > :min_salary", Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void containsEmployee() throws IOException {
        System.out.println("Enter employee full name:");
        String[] fullName = bufferedReader.readLine().split("\\s+");
        String firstName = fullName[0];
        String lastName = fullName[1];

        Long singleResult = entityManager.createQuery("SELECT COUNT(e) FROM Employee e " +
                "WHERE e.firstName = :f_name and e.lastName = :l_name", Long.class)
                .setParameter("f_name", firstName)
                .setParameter("l_name", lastName)
                .getSingleResult();

        System.out.println(singleResult == 0
                ? "No" : "Yes");
    }

    private void changeCasing() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("UPDATE Town t " +
                "SET t.name = upper(t.name) " +
                "WHERE length(t.name) <= 5");
        System.out.println("Affected rows: " + query.executeUpdate());
        entityManager.getTransaction().commit();
    }
}
