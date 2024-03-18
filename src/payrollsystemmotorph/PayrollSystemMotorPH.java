/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package payrollsystemmotorph;

/**
 *
 * @author G3
 */
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PayrollSystemMotorPH {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Employee[] employees = initializeEmployees();

        System.out.println("Welcome to Motor PH Payroll System!");
        
        boolean exitProgram = false;
        
        while (!exitProgram) {
            System.out.println("Enter employee number (1-34) or 0 to exit:");
            int employeeNumber = scanner.nextInt();
            scanner.nextLine();

            if (employeeNumber == 0) {
                System.out.println("Exiting...Thank you for using Motor PH Payroll System!");
                break;
            } else if (employeeNumber < 1 || employeeNumber > 34) {
                System.out.println("Invalid employee number. Please enter a number between 1 and 34.");
                continue;
            }

            Employee employee = employees[employeeNumber - 1];
            displayInfo(employee);

            String[] workDate = getPaySlipDateRange(scanner);
            double[] hoursAndIncome = monthlyHWcalc(scanner, employee.hourlyRate);
            int totalRegularHours = (int) hoursAndIncome[0];
            int totalOvertimeHours = (int) hoursAndIncome[1];
            double grossIncome = hoursAndIncome[2];
            double overtimePay = hoursAndIncome[3];
            System.out.println("\nTotal Regular Hours: " + totalRegularHours);
            System.out.println("Total Overtime Hours: " + totalOvertimeHours);
            System.out.println("\nGross Income: " + grossIncome);

            double totalBenefits = totalBenefitsCalc(employee);
            double[] philHealthContribution = philHealthCalc(employee);
            double pagIbigContribution = pag_ibigCalc(employee);
            double sssContribution = sssCalc(employee, grossIncome);
            double totalContributions = govtContributions(sssContribution, philHealthContribution[1], pagIbigContribution);
            double taxableIncome = taxableIncomeCalc(grossIncome, totalContributions);
            double withholdingTax = withholdingTaxCalc(taxableIncome);
            double netIncome = netIncomeCalc(taxableIncome);

            while (true) {
    // Ask if the user wants to generate a payslip
            System.out.println("\nGenerate payslip (yes/no): ");
            String payslipOption = scanner.nextLine().trim().toLowerCase();

            if (payslipOption.equals("yes")) {
                // Generate the payslip
                generatePayslip(employee, grossIncome, overtimePay, totalBenefits, philHealthContribution, pagIbigContribution, sssContribution, totalContributions, taxableIncome, withholdingTax, netIncome,workDate);
            } else if (!payslipOption.equals("no")) {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                continue; // Restart the loop if input is invalid
            }

            // Ask if the user wants to search another employee
            System.out.println("\nSearch another employee? (yes/no): ");
            String option = scanner.nextLine().trim().toLowerCase();

            if (option.equals("no")) {
                System.out.println("Exiting... Thank you for using Motor PH Payroll System!");
                exitProgram = true; // Exit the loop if the user doesn't want to calculate another salary
                break;
            } else if (!option.equals("yes")) {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
            
            scanner.close();
    }
}

    static class Employee {
        private final int empId;
        private final String empName;
        private final String empBirthday;
        private final String empAddress;
        private final String phoneNum;
        private final String sssNum;
        private final String philHNum;
        private final String tinNum;
        private final String pagibigNum;
        private final String empStatus;
        private final String empPosition;
        private final String immSupervisor;
        private final double basicSal;
        private final double riceSub;
        private final double phoneAll;
        private final double clothingAll;
        private final double grossSMRate;
        private double hourlyRate;

        public Employee(int empId, String empName, String empBirthday, String empAddress, String phoneNum, String sssNum,
                        String philHNum, String tinNum, String pagibigNum, String empStatus, String empPosition,
                        String immSupervisor, double basicSal, double riceSub, double phoneAll, double clothingAll,
                        double grossSMRate, double hourlyRate) {
            this.empId = empId;
            this.empName = empName;
            this.empBirthday = empBirthday;
            this.empAddress = empAddress;
            this.phoneNum = phoneNum;
            this.sssNum = sssNum;
            this.philHNum = philHNum;
            this.tinNum = tinNum;
            this.pagibigNum = pagibigNum;
            this.empStatus = empStatus;
            this.empPosition = empPosition;
            this.immSupervisor = immSupervisor;
            this.basicSal = basicSal;
            this.riceSub = riceSub;
            this.phoneAll = phoneAll;
            this.clothingAll = clothingAll;
            this.grossSMRate = grossSMRate;
            this.hourlyRate = hourlyRate;
        }
    }

    // Initialize employees
    private static Employee[] initializeEmployees() {
        Employee[] employees = new Employee[34];
        employees[0] = new Employee(1, "Manuel Garcia III", "10/11/1983",
                "Valero Carpark Building Valero Street 1227, Makati City", "966-860-270", "44-4506057-3",
                "820126853951", "442-605-657-000", "691295330870", "Regular", "Chief Executive Officer", "N/A",
                90000, 1500, 2000, 1000, 45000, 535.71);
        employees[1] = new Employee(2, "Antonio Lim", "06/19/1988", 
                "San Antonio De Padua 2, Block 1 Lot 8 and 2, Dasmarinas, Cavite", "171-867-411", "52-2061274-9", 
                "331735646338", "683-102-776-000", "663904995411", "Regular", "Chief Operating Officer", 
                "Garcia, Manuel III", 60000, 1500, 2000, 1000, 30000, 357.14);
        employees[2] = new Employee(3, "Bianca Sofia Aquino", "08/04/1989", 
                "Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100, Quezon City", "966-889-370", "30-8870406-2", 
                "177451189665", "971-711-280-000", "171519773969", "Regular", "Chief Finance Officer", 
                "Garcia, Manuel III", 60000, 1500, 2000, 1000, 30000, 357.14);
        employees[3] = new Employee(4, "Isabella Reyes", "06/16/1994", 
                "460 Solanda Street Intramuros 1000, Manila", "786-868-477", "40-2511815-0", "341911411254", 
                "876-809-437-000", "416946776041", "Regular", "Chief Marketing Officer", 
                "Garcia, Manuel III", 60000, 1500, 2000, 1000, 30000, 357.14);
        employees[4] = new Employee(5, "Eduard Hernandez", "09/23/1989", 
                "National Highway, Gingoog, Misamis Occidental", "088-861-012", "50-5577638-1", 
                "957436191812", "031-702-374-000", "952347222457", "Regular", "IT Operations and Systems", 
                "Lim, Antonio", 52670, 1500, 1000, 1000, 26335, 313.51);
        employees[5] = new Employee(6, "Andrea Mae Villanueva", "02/14/1988", 
                "17/85 Stracke Via Suite 042, Poblacion, Las Pinas 4783 Dinagat Islands", "918-621-603", "49-1632020-8", 
                "382189453145", "317-674-022-000", "441093369646", "Regular", "HR Manager", 
                "Lim, Antonio", 52670, 1500, 1000, 1000, 26335, 313.51);
        employees[6] = new Employee(7, "Brad San Jose", "03/15/1996", 
                "99 Strosin Hills, Poblacion, Bislig 5340 Tawi-Tawi", "797-009-261", "40-2400714-1", "239192926939", 
                "672-474-690-000", "210850209964", "Regular", "HR Team Leader", 
                "Villanueva, Andrea Mae", 42975, 1500, 800, 800, 21488, 255.80);
        employees[7] = new Employee(8, "Alice Romualdez", "05/14/1992", 
                "12A/33 Upton Isle Apt. 420, Roxas City 1814 Surigao del Norte", "983-606-799", "55-4476527-2", 
                "545652640232", "888-572-294-000", "211385556888", "Regular", "HR Rank and File", 
                "San, Jose Brad", 22500, 1500, 500, 500, 11250, 133.93);
        employees[8] = new Employee(9, "Rosie Atienza", "09/24/1948", 
                "90A Dibbert Terrace Apt. 190, San Lorenzo 6056 Davao del Norte", "266-036-427", "41-0644692-3", 
                "708988234853", "604-997-793-000", "260107732354", "Regular", "HR Rank and File", 
                "San, Jose Brad", 22500, 1500, 500, 500, 11250, 133.93);
        employees[9] = new Employee(10, "Roderick Alvaro", "03/30/1988", 
                "#284 T. Morato corner, Scout Rallos Street, Quezon City", "053-381-386", "64-7605054-4", 
                "578114853194", "525-420-419-000", "799254095212", "Regular", "Accounting Head", 
                "Aquino, Bianca Sofia", 52670, 1500, 1000, 1000, 26335, 313.51);
        employees[10] = new Employee(11, "Anthony Salcedo", "09/14/1993", 
                "93/54 Shanahan Alley Apt. 183, Santo Tomas 1572 Masbate", "070-766-300", "26-9647608-3", 
                "126445315651", "210-805-911-000", "218002473454", "Regular", "Payroll Manager", 
                "Alvaro, Roderick", 50825, 1500, 1000, 1000, 25413, 302.53);
        employees[11] = new Employee(12, "Josie Lopez", "01/14/1987", 
                "49 Springs Apt. 266, Poblacion, Taguig 3200 Occidental Mindoro", "478-355-427", "44-8563448-3", 
                "431709011012", "218-489-737-000", "113071293354", "Regular", "Payroll Team Leader", 
                "Salcedo, Anthony", 38475, 1500, 800, 800, 19238, 229.02);
        employees[12] = new Employee(13, "Martha Farala", "01/11/1942", 
                "42/25 Sawayn Stream, Ubay 1208 Zamboanga del Norte", "329-034-366", "45-5656375-0", "233693897247", 
                "210-835-851-000", "631130283546", "Regular", "Payroll Rank and File", 
                "Salcedo, Anthony", 24000, 1500, 500, 500, 12000, 142.86);
        employees[13] = new Employee(14, "Leila Martinez", "07/11/1970", 
                "37/46 Kulas Roads, Maragondon 0962 Quirino", "877-110-749", "27-2090996-4", "515741057496", 
                "275-792-513-000", "101205445886", "Regular", "Payroll Rank and File", 
                "Salcedo, Anthony", 24000, 1500, 500, 500, 12000, 142.86);
        employees[14] = new Employee(15, "Fredrick Romualdez", "03/10/1985", 
                "22A/52 Lubowitz Meadows, Pililla 4895 Zambales", "023-079-009", "26-8768374-1", "308366860059", 
                "598-065-761-000", "223057707853", "Regular", "Account Manager", 
                "Lim, Antonio", 53500, 1500, 1000, 1000, 26750, 318.45);
        employees[15] = new Employee(16, "Christian Mata", "10/21/1987", 
                "90 O'Keefe Spur Apt. 379, Catigbian 2772 Sulu", "783-776-744", "49-2959312-6", 
                "824187961962", "103-100-522-000", "631052853464", "Regular", "Account Team Leader", 
                "Romualdez, Fredrick", 42975, 1500, 800, 800, 21488, 255.80);
        employees[16] = new Employee(17, "Selena De Leon", "02/20/1975", 
                "89A Armstrong Trace, Compostela 7874 Maguindanao", "975-432-139", "27-2090208-8", 
                "587272469938", "482-259-498-000", "719007608464", "Regular", "Account Team Leader", 
                "Romualdez, Fredrick", 41850, 1500, 800, 800, 20925, 249.11);
        employees[17] = new Employee(18, "Allison San Jose", "06/24/1986", 
                "08 Grant Drive Suite 406, Poblacion, Iloilo City 9186 La Union", "179-075-129", "45-3251383-0", 
                "745148459521", "121-203-336-000", "114901859343", "Regular", "Account Rank and File", 
                "Mata, Christian", 22500, 1500, 500, 500, 11250, 133.93);
        employees[18] = new Employee(19, "Cydney Rosario", "10/06/1996", 
                "93A/21 Berge Points, Tapaz 2180 Quezon", "868-819-912", "49-1629900-2", "579253435499", 
                "122-244-511-000", "265104358643", "Regular", "Account Rank and File", 
                "Mata, Christian", 22500, 1500, 500, 500, 11250, 133.93);
        employees[19] = new Employee(20, "Mark Bautista", "02/12/1991", 
                "65 Murphy Center Suite 094, Poblacion, Palayan 5636 Quirino", "683-725-348", "49-1647342-5", 
                "399665157135", "273-970-941-000", "260054585575", "Regular", "Account Rank and File", 
                "Mata, Christian", 23250, 1500, 500, 500, 11625, 138.39);
        employees[20] = new Employee(21, "Darlene Lazaro", "11/25/1985", 
                "47A/94 Larkin Plaza Apt. 179, Poblacion, Caloocan 2751 Quirino", "740-721-558", "45-5617168-2", 
                "606386917510", "354-650-951-000", "104907708845", "Probationary", "Account Rank and File", 
                "Mata, Christian", 23250, 1500, 500, 500, 11625, 138.39);
        employees[21] = new Employee(22, "Kolby Delos Santos", "02/26/1980",
                "06A Gulgowski Extensions, Bongabon 6085 Zamboanga del Sur", "739-443-033", "52-0109570-6", 
                "357451271274", "187-500-345-000", "113017988667", "Probationary", "Account Rank and File", 
                "Mata, Christian", 24000, 1500, 500, 500, 12000, 142.86);
        employees[22] = new Employee(23, "Vella Santos", "12/31/1983", 
                "99A Padberg Spring, Poblacion, Mabalacat 3959 Lanao del Sur", "955-879-269", "52-9883524-3", 
                "548670482885", "101-558-994-000", "360028104576", "Probationary", "Account Rank and File", 
                "Mata, Christian", 22500, 1500, 500, 500, 11250, 133.93);
        employees[23] = new Employee(24, "Tomas Del Rosario", "12/18/1978", 
                "80A/48 Ledner Ridges, Poblacion, Kabankalan 8870 Marinduque", "882-550-989", "45-5866331-6", 
                "953901539995", "560-735-732-000", "913108649964", "Probationary", "Account Rank and File",
                "Mata, Christian", 22500, 1500, 500, 500, 11250, 133.93);
        employees[24] = new Employee(25, "Jacklyn Tolentino", "05/19/1984", 
                "96/48 Watsica Flats Suite 734, Poblacion, Malolos 1844 Ifugao", "675-757-366", "47-1692793-0", 
                "753800654114", "841-177-857-000", "210546661243", "Probationary", "Account Rank and File", 
                "De Leon, Selena", 24000, 1500, 500, 500, 12000, 142.86);
        employees[25] = new Employee(26, "Percival Gutierrez", "12/18/1970", 
                "58A Wilderman Walks, Poblacion, Digos 5822 Davao del Sur", "512-899-876", "40-9504657-8", 
                "797639382265", "502-995-671-000", "210897095686", "Probationary", "Account Rank and File", 
                "De Leon, Selena", 24750, 1500, 500, 500, 12375, 147.32);
        employees[26] = new Employee(27, "Garfield Manalaysay", "08/28/1986", 
                "60 Goyette Valley Suite 219, Poblacion, Tabuk 3159 Lanao del Sur", "948-628-136", "45-3298166-4",
                "810909286264", "336-676-445-000", "211274476563", "Probationary", "Account Rank and File", 
                "De Leon, Selena", 24750, 1500, 500, 500, 12375, 147.32);
        employees[27] = new Employee(28, "Lizeth Villegas", "12/12/1981",
                "66/77 Mann Views, Luisiana 1263 Dinagat Islands", "332-372-215", "40-2400719-4", "934389652994", 
                "210-395-397-000", "122238077997", "Probationary", "Account Rank and File", 
                "De Leon, Selena", 24000, 1500, 500, 500, 12000, 142.86);
        employees[28] = new Employee(29, "Carol Ramos", "08/20/1978",
                "72/70 Stamm Spurs, Bustos 4550 Iloilo", "250-700-389", "60-1152206-4", "351830469744", 
                "395-032-717-000", "212141893454", "Probationary", "Account Rank and File", 
                "De Leon, Selena", 22500, 1500, 500, 500, 11250, 133.93);
        employees[29] = new Employee(30, "Emelia Maceda", 
                "04/14/1973", "50A/83 Bahringer Oval Suite 145, Kiamba 7688 Nueva Ecija", "973-358-041", "54-1331005-0", 
                "465087894112", "215-973-013-000", "515012579765", "Probationary", "Account Rank and File", "De Leon, Selena", 22500, 1500, 500, 500, 11250, 133.93);
        employees[30] = new Employee(31, "Delia Aguilar", "01/27/1989", 
                "95 Cremin Junction, Surallah 2809 Cotabato", "529-705-439", "52-1859253-1", "136451303068", 
                "599-312-588-000", "110018813465", "Probationary", "Account Rank and File", 
                "De Leon, Selena", 22500, 1500, 500, 500, 11250, 133.93);
        employees[31] = new Employee(32, "John Rafael Castro", "02/09/1992", 
                "Hi-way, Yati, Liloan Cebu", "332-424-955", "26-7145133-4", "601644902402", "404-768-309-000", 
                "697764069311", "Regular", "Sales & Marketing", 
                "Reyes, Isabella", 52670, 1500, 1000, 1000, 26335, 313.51);
        employees[32] = new Employee(33, "Carlos Ian Martinez", "11/16/1990",
                "Bulala, Camalaniugan", "078-854-208", "11-5062972-7", "380685387212", "256-436-296-000", 
                "993372963726", "Regular", "Supply Chain and Logistics", 
                "Reyes, Isabella", 52670, 1500, 1000, 1000, 26335, 313.51);
        employees[33] = new Employee(34, "Beatriz Santos", "08/07/1990", 
                "Agapita Building, Metro Manila", "526-639-511", "20-2987501-5", "918460050077", "911-529-713-000", 
                "874042259378", "Regular", "Customer Service and Relations", "Reyes, Isabella", 52670, 1500, 1000, 1000, 26335, 313.51);
        
        return employees;
    }

    // Display employee info
    private static void displayInfo(Employee employee) {
        System.out.println("-----------EMPLOYEE DETAILS-------------");
        System.out.println("Employee ID:                " + employee.empId);
        System.out.println("Name:                       " + employee.empName);
        System.out.println("Birthday:                   " + employee.empBirthday);
        System.out.println("Address:                    " + employee.empAddress);
        System.out.println("Phone Number:               " + employee.phoneNum);
        System.out.println("SSS:                        " + employee.sssNum);
        System.out.println("PhilHealth:                 " + employee.philHNum);
        System.out.println("Tin Number                  " + employee.tinNum);
        System.out.println("PAG-IBIG:                   " + employee.pagibigNum);
        System.out.println("Status:                     " + employee.empStatus);
        System.out.println("Position:                   " + employee.empPosition);
        System.out.println("Immediate Supervisor:       " + employee.immSupervisor);
        System.out.println("Basic Salary:               " + employee.basicSal);
        System.out.println("Rice Subsidy:               " + employee.riceSub);
        System.out.println("Phone Allowance:            " + employee.phoneAll);
        System.out.println("Clothing Allowance:         " + employee.clothingAll);
        System.out.println("Gross Semi Monthly Rate:    " + employee.grossSMRate);
        System.out.println("Hourly Rate:                " + employee.hourlyRate);
        System.out.println("                                                     ");
    }

    // Prompt user for work duration
    private static String[] getPaySlipDateRange(Scanner scanner) {
        String[] workDate = new String[2];

        System.out.println("---------MONTHLY ATTENDANCE RECORD---------");
        System.out.println("\nEnter Payslip Date Range (MM/DD/YYYY)");

        LocalDate startDate = getValidDate(scanner, "Start date: ");
        workDate[0] = startDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        LocalDate endDate = getValidDate(scanner, "End date: ");
        workDate[1] = endDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        return workDate;
    }

    // Validate date input
    private static LocalDate getValidDate(Scanner scanner, String prompt) {
        LocalDate date = null;
        boolean isValid = false;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        do {
            try {
                System.out.print(prompt);
                String userInput = scanner.nextLine();
                date = LocalDate.parse(userInput, dateFormat);
                isValid = true;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter a date in MM/DD/YYYY format.");
            }
        } while (!isValid);

        return date;
    }

    // Calculate monthly hours worked
    public static double[] monthlyHWcalc(Scanner scanner, double hourlyRate) {
        int totalRegHours = 0;
        int totalOvtHours = 0;
        double overtimePay = 0;

        System.out.print("Enter number of days worked in a month: ");
        int numberOfWorkDays = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfWorkDays; i++) {

            System.out.printf("\nDay %d:\n", i + 1);
            System.out.print("Clock-in time (HH:MM): ");
            String clockInTime = scanner.nextLine();
            System.out.print("Clock-out time (HH:MM): ");
            String clockOutTime = scanner.nextLine();

            int totalWorkedHours = hoursWorkedCalc(clockInTime, clockOutTime);

            if (totalWorkedHours > 8) {
                int regHours = 8;
                int overtimeHours = totalWorkedHours - 8;
                totalRegHours += regHours;
                totalOvtHours += overtimeHours;
                System.out.println("Regular Hours: " + regHours);
                System.out.println("Overtime Hours: " + overtimeHours);
                overtimePay += overtimeCalc(overtimeHours, hourlyRate);
            } else {
                totalRegHours += totalWorkedHours;
                System.out.println("Regular Hours: " + totalWorkedHours);
                System.out.println("Overtime Hours: 0");
            }
        }
            double grossIncome = grossIncomeCalc(hourlyRate, totalRegHours, overtimePay);
            
        return new double[] {totalRegHours, totalOvtHours, grossIncome, overtimePay};
    }

    // Calculate total hours worked in a day
    private static int hoursWorkedCalc(String clockInTime, String clockOutTime) {
        String[] clockInParts = clockInTime.split(":");
        String[] clockOutParts = clockOutTime.split(":");

        int clockInHour = Integer.parseInt(clockInParts[0]);
        int clockInMinute = Integer.parseInt(clockInParts[1]);

        int clockOutHour = Integer.parseInt(clockOutParts[0]);
        int clockOutMinute = Integer.parseInt(clockOutParts[1]);

        int totalMinutesWorked = (clockOutHour - clockInHour) * 60 + (clockOutMinute - clockInMinute);

        // Consider the grace period from 08:00 to 08:10
        if (clockInHour == 8 && clockInMinute <= 10) {
            if (clockOutHour == 8 && clockOutMinute <= 10) {
                // If both clock-in and clock-out are within the grace period, no additional hour is added
                return 0;
            } else {
                // If clock-out is after the grace period, deduct the grace period from the total minutes worked
                totalMinutesWorked -= Math.min(10, clockOutMinute);
            }
        }

        // Subtract lunch break time if applicable (12:00 to 13:00)
        if (clockInHour < 12 && clockOutHour >= 13) {
            totalMinutesWorked -= 60;
        }

        // Convert total minutes worked to hours (rounded down)
        int totalHoursWorked = totalMinutesWorked / 60;

        return totalHoursWorked;

    }

    // Calculate overtime pay
    public static double overtimeCalc(int totalOvertimeHoursWorkedInAMonth, double hourlyRate) {
        double overtimePay = 1.25 * hourlyRate * totalOvertimeHoursWorkedInAMonth;
        return overtimePay;
    }

    // Calculate gross income
    public static double grossIncomeCalc(double hourlyRate, int totalRegularHoursWorkedInAMonth,
            double overtimePay) {
        double grossIncome = (hourlyRate * totalRegularHoursWorkedInAMonth) + overtimePay;
        return grossIncome;
    }
    
    // Calculate total benefits including rice subsidy, phone allowance, and clothing allowance
    public static double totalBenefitsCalc(Employee employee) {
        double totalBenefits = employee.riceSub + employee.phoneAll + employee.clothingAll;
        return totalBenefits;
    }

    // Calculate SSS contribution
    public static double sssCalc(Employee employee, double grossIncome) {
        
        double[] BasicSalThresholds = { 3250, 3750, 4250, 4750, 5250, 5750, 6250, 6750, 7250, 7750, 8250, 8750,
                9250, 9750, 10250, 10750, 11250, 11750, 12250, 12750, 13250, 13750, 14250, 14750, 15250, 15750, 16250,
                16750, 17250, 17750, 18250, 18750, 19250, 19750, 20250, 20750, 21250, 21750, 22250, 22750, 23250, 23750,
                24250, 24750 };

        double[] SSS_MonthlyCont = { 135.00, 157.50, 180.00, 202.50, 225.00, 247.50, 270.00, 292.50, 315.00,
                337.50, 360.00, 382.50, 405.00, 427.50, 450.00, 472.50, 495.00, 517.50, 540.00, 562.50, 585.00, 607.50,
                630.00, 652.50, 675.00, 697.50, 720.00, 742.50, 765.00, 787.50, 810.00, 832.50, 855.00, 877.50, 900.00,
                922.50, 945.00, 967.50, 990.00, 1012.50, 1035.00, 1057.50, 1080.00, 1102.50 };
        
        double sssMonthlyContribution = 0;

        // Get the monthly SSS contribution based on employee's gross income
        for (int i = 0; i < BasicSalThresholds.length; i++) {
            if (grossIncome < BasicSalThresholds[i]) {
                sssMonthlyContribution = SSS_MonthlyCont[i];
                break;
            }
        }

        // For basic salary over 24,750
        if (grossIncome >= BasicSalThresholds[BasicSalThresholds.length - 1]) {
            sssMonthlyContribution = 1125.00;
        }

        return sssMonthlyContribution;
    }

    // Calculate PhilHealth contribution
    public static double[] philHealthCalc(Employee employee) {
        double premiumRate = 0.03;
        double monthlyPremium = 0;

        // Calculate 3% monthly premium rate based on employee's basic salary
        if (employee.basicSal <= 10000) {
            monthlyPremium = 300;
        } else if (employee.basicSal > 10000 && employee.basicSal < 60000) {
            monthlyPremium = employee.basicSal * premiumRate;
        } else if (employee.basicSal >= 60000) {
            monthlyPremium = 1800;
        }

        // Monthly premium contribution payments are equally shared between the employee and employer.
        double employeeShare = monthlyPremium * 0.5;

        double[] philHealthContribution = { monthlyPremium, employeeShare };
        return philHealthContribution;
    }

    // Calculate Pag-IBIG contribution
    public static double pag_ibigCalc(Employee employee) {
        double pagIbigMonthlyContribution = 0;

        if (employee.basicSal >= 1000 && employee.basicSal <= 1500) {
            pagIbigMonthlyContribution = employee.basicSal * 0.01;
        } else if (employee.basicSal > 1500) {
            pagIbigMonthlyContribution = employee.basicSal * 0.02;

            // The maximum Pag-Ibig contribution amount is 100
            if (pagIbigMonthlyContribution > 100) {
                pagIbigMonthlyContribution = 100;
            }
        }

        return pagIbigMonthlyContribution;
    }

    // Calculate total government contributions
    public static double govtContributions(double sssMonthlyContribution, double employeeShare,
            double pagIbigMonthlyContribution) {
        double totalMonthlyContribution = sssMonthlyContribution + employeeShare + pagIbigMonthlyContribution;
        return totalMonthlyContribution;
    }

    // Calculate taxable income
    public static double taxableIncomeCalc(double grossIncome, double totalMonthlyContribution) {
        double taxableIncome = grossIncome - totalMonthlyContribution;

        // Prevent a negative value for employee's income
        if (taxableIncome < 0) {
            taxableIncome = 0;
        }

        return taxableIncome;
    }

    // Calculate withholding tax
    public static double withholdingTaxCalc(double taxableIncome) {
        // Withholding tax is calculated after applying deductions
        double withholdingTax = 0;

        // Monthly Tax Rate
        if (taxableIncome <= 20832) {
            withholdingTax = 0;
        } else if (taxableIncome >= 20833 && taxableIncome < 33333) {
            withholdingTax = (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome >= 33333 && taxableIncome < 66667) {
            withholdingTax = 2500 + (taxableIncome - 33333) * 0.25;
        } else if (taxableIncome >= 66667 && taxableIncome < 166667) {
            withholdingTax = 10833 + (taxableIncome - 66667) * 0.30;
        } else if (taxableIncome >= 166667 && taxableIncome < 666667) {
            withholdingTax = 40833.33 + (taxableIncome - 166667) * 0.32;
        } else if (taxableIncome >= 666667) {
            withholdingTax = 200833.33 + (taxableIncome - 666667) * 0.35;
        }

        return withholdingTax;
    }

    // Calculate net income
    public static double netIncomeCalc(double taxableIncome) {
        double netIncome = taxableIncome - withholdingTaxCalc(taxableIncome);
        return netIncome;
    }
    
    private static void generatePayslip(Employee employee, double grossIncome, double overtimePay, double totalBenefits,
            double[] philHealthContribution, double pagIbigContribution, double sssContribution, double totalContributions,
            double taxableIncome, double withholdingTax, double netIncome, String[] paySlipDateRange) {
        System.out.println("\n-----------MONTHLY PAYSLIP-------------");
        System.out.println("Employee ID: " + employee.empId);
        System.out.println("Name: " + employee.empName);
        System.out.println("\nDate Generated:");
        System.out.println("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        System.out.println("------------");
        System.out.println("PAYSLIP DATE RANGE");
        System.out.println("\nStart Date: " + paySlipDateRange[0]);
        System.out.println("End Date: " + paySlipDateRange[1]);
        System.out.println("------------");
        System.out.println("EARNINGS");
        System.out.println("\nGross Income: PHP " + grossIncome);
        System.out.println("Overtime Pay: PHP " + overtimePay);
        System.out.println("Total Benefits: PHP " + totalBenefits);
        System.out.println("------------");
        System.out.println("DEDUCTIONS");
        System.out.println("\nSSS Contribution: PHP " + sssContribution);
        System.out.println("PhilHealth Contribution (Employee Share): PHP " + philHealthContribution[1]);
        System.out.println("Pag-IBIG Contribution: PHP " + pagIbigContribution);
        System.out.println("Total Contributions: PHP " + totalContributions);
        System.out.println("Taxable Income: PHP " + taxableIncome);
        System.out.println("Withholding Tax: PHP " + withholdingTax);
        System.out.println("------------");
        System.out.println("Net Income: PHP " + netIncome);
        System.out.println("--------------------------------");
    }
}


