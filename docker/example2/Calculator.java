import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double num1, num2;
        String operator;

        System.out.println("Welcome to Dockerized Calculator!");

        while (true) {
            System.out.print("\nEnter first number (or type 'exit'): ");
            if (scanner.hasNext("exit")) break;
            num1 = scanner.nextDouble();

            System.out.print("Enter operator (+, -, *, /): ");
            operator = scanner.next();

            System.out.print("Enter second number: ");
            num2 = scanner.nextDouble();

            switch (operator) {
                case "+" -> System.out.println("Result: " + (num1 + num2));
                case "-" -> System.out.println("Result: " + (num1 - num2));
                case "*" -> System.out.println("Result: " + (num1 * num2));
                case "/" -> {
                    if (num2 != 0) System.out.println("Result: " + (num1 / num2));
                    else System.out.println("Cannot divide by zero!");
                }
                default -> System.out.println("Invalid operator!");
            }
        }

        System.out.println("Calculator closed.");
        scanner.close();
    }
}
