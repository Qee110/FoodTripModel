import java.util.Scanner;

public class FoodTripModel {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("======================================================");
        System.out.println("   DISCRETE CHOICE MODEL: LIKERT-SCALE PIPELINE");
        System.out.println("======================================================");

        // 1. DEFINE BETA WEIGHTS
        System.out.println("\nDefine your theory-based Beta (B) values:");
        System.out.print("Enter Beta 1 (Driving Experience) : ");
        double b_DE = scanner.nextDouble();
        System.out.print("Enter Beta 2 (Journey Quality)    : ");
        double b_JQ = scanner.nextDouble();
        System.out.print("Enter Beta 3 (Food Availability)  : ");
        double b_FA = scanner.nextDouble();
        System.out.print("Enter Beta 4 (Cost/Affordability) : ");
        double b_Cost = scanner.nextDouble();

        System.out.print("\nEnter the number of respondents (students): ");
        int numStudents = scanner.nextInt();

        // Arrays to store ratings [Student Index][Transport Index]
        double[][] DE_ratings = new double[numStudents][4];
        double[][] JQ_ratings = new double[numStudents][4];
        double[][] FA_ratings = new double[numStudents][4];
        double[][] Cost_ratings = new double[numStudents][4];

        String[] transportNames = {"Drive Own Car", "Car Pooling", "E-hailing", "Public Transport"};

        // 2. DATA COLLECTION
        System.out.println("\n======================================================");
        System.out.println(" DATA COLLECTION (1-5 SCALE)");
        System.out.println("======================================================");
        
        for (int i = 0; i < numStudents; i++) {
            System.out.printf("\n--- RESPONDENT %d ---\n", (i + 1));
            
            for (int t = 0; t < 4; t++) {
                System.out.printf("\nRate your perception for: %s\n", transportNames[t]);
                
                System.out.print("  DE (Experience)   (1-5): ");
                DE_ratings[i][t] = scanner.nextDouble();
                System.out.print("  JQ (Comfort)      (1-5): ");
                JQ_ratings[i][t] = scanner.nextDouble();
                System.out.print("  FA (Food Access)  (1-5): ");
                FA_ratings[i][t] = scanner.nextDouble();
                System.out.print("  COST (Affordable) (1-5): ");
                Cost_ratings[i][t] = scanner.nextDouble();
            }
        }

        // 3. AGGREGATE PERCEPTIONS (Averages)
        double[] avg_DE = new double[4];
        double[] avg_JQ = new double[4];
        double[] avg_FA = new double[4];
        double[] avg_Cost = new double[4];

        for (int t = 0; t < 4; t++) {
            double sum_DE = 0, sum_JQ = 0, sum_FA = 0, sum_Cost = 0;
            for (int i = 0; i < numStudents; i++) {
                sum_DE += DE_ratings[i][t];
                sum_JQ += JQ_ratings[i][t];
                sum_FA += FA_ratings[i][t];
                sum_Cost += Cost_ratings[i][t];
            }
            avg_DE[t] = sum_DE / numStudents;
            avg_JQ[t] = sum_JQ / numStudents;
            avg_FA[t] = sum_FA / numStudents;
            avg_Cost[t] = sum_Cost / numStudents;
        }

        // 4. CALCULATE UTILITY
        double[] utility = new double[4];
        for (int t = 0; t < 4; t++) {
            utility[t] = (b_DE * avg_DE[t]) + (b_JQ * avg_JQ[t]) + 
                         (b_FA * avg_FA[t]) + (b_Cost * avg_Cost[t]);
        }

        // 5. CALCULATE PROBABILITIES (Softmax / MNL)
        double[] probabilities = new double[4];
        double sumExpU = 0;
        for (int t = 0; t < 4; t++) {
            sumExpU += Math.exp(utility[t]);
        }
        for (int t = 0; t < 4; t++) {
            probabilities[t] = Math.exp(utility[t]) / sumExpU;
        }

        // 6. TABULATE RESULTS
        System.out.println("\n======================================================");
        System.out.println("    AVERAGE CHOICE PROBABILITY");
        System.out.println("======================================================");
        System.out.printf("%-18s | %-10s | %-10s\n", "Transport Mode", "Utility", "Probability");
        System.out.println("------------------------------------------------------");
        for (int t = 0; t < 4; t++) {
            System.out.printf("%-18s | %-10.3f | %6.2f%%\n", 
                    transportNames[t], utility[t], (probabilities[t] * 100));
        }
        System.out.println("======================================================");

        scanner.close();
    }
}
