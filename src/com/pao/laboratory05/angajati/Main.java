package com.pao.laboratory05.angajati;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();

        while (true) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune){
                case 1:
                    System.out.print("Nume: ");
                    String nume = scanner.nextLine();
                    System.out.print("Departament (nume): ");
                    String numeDept = scanner.nextLine();
                    System.out.print("Departament (Locatie): ");
                    String locatieDept = scanner.nextLine();
                    System.out.print("Salariu: ");
                    double salariu = scanner.nextDouble();
                    scanner.nextLine();

                    Departament dept = new Departament(numeDept,locatieDept);
                    Angajat angajat = new Angajat(nume, dept, salariu);
                    service.addAngajat(angajat);
                    break;

                case 2:
                    service.listBySalary();
                    break;

                case 3:
                    System.out.print("Departament: ");
                    String cautareDept = scanner.nextLine();
                    service.findByDepartament(cautareDept);
                    break;

                case 0:
                    System.out.println("La revedere!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Optiune invalida!");
            }

        }
    }
}
