package de.unistuttgart.iste.pe2;

public class Main {

    static int[] arrayIndexes = {
            20, 44, 50, 13, 17, 33, 41,
            68, 77, 44, 29, 72, 48, 71,
            37, 48, 11, 69, 5, 65, 65
    };

    static char[] arrayLetters = {
           'V', 'b', 't'
    };

    public static void main(String[] args) {
        Connector connector = new Connector();
        // connect
        connector.connect();

        // Aufgabe 2.2.A
        connector.aufgabeA(arrayIndexes);
        System.out.println("-------------------------------------");

        // Aufgabe 2.2.B
        connector.aufgabeB(arrayLetters);
        System.out.println("-------------------------------------");

        // Aufgabe 2.2.C
        connector.aufgabeC();
        System.out.println("-------------------------------------");

        // close connection
        connector.disconnect();
    }
}