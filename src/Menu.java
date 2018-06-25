/**
 * Created by xavierromacastells on 17/12/16.
 */

import java.util.Scanner;

public class Menu {

    private int opcio;  // Variable que guarda l'opcio introduida per l'usuari
    /**
     * Constructor de la classe
     * Gestiona tota la classe amb una crida
     */
    public Menu() {
        opcio = 0;
        procMenu();
        while (opcio < 1 || opcio > 9) {
            procMenu();
        }
    }

    public int getOpcio() {
        return opcio;
    }

    /**
     * Procediment que recolleix el procediment necessari per imprimir, llegir
     * i gestiona l'opcio
     */
    private void procMenu() {

        printMenu();
        llegeixTeclat();
    }

    /**
     * Procediment que imprimeix el menu per consola
     */
    private void printMenu() {

        StringBuilder sb = new StringBuilder();     //Variable per concatenar el menu i imprimil

        sb.append(System.getProperty("line.separator"));
        sb.append("************ LSBicing menu ************");
        sb.append(System.getProperty("line.separator"));
        sb.append("1. Geolocalització");
        sb.append(System.getProperty("line.separator"));
        sb.append("2. Buscar estació de Bicing més propera");
        sb.append(System.getProperty("line.separator"));
        sb.append("3. Creació d'una ruta");
        sb.append(System.getProperty("line.separator"));
        sb.append("4. Visualització d'estacions de Bicing");
        sb.append(System.getProperty("line.separator"));
        sb.append("5. Cerca d'ubicacions");
        sb.append(System.getProperty("line.separator"));
        sb.append("6. Visualitzar ubicació guardada");
        sb.append(System.getProperty("line.separator"));
        sb.append("7. Estacions més llunyanes");
        sb.append(System.getProperty("line.separator"));
        sb.append("8. Rànquing");
        sb.append(System.getProperty("line.separator"));
        sb.append("9. Sortir");
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        sb.append("Introdueix una opció:");

        System.out.println(sb.toString());
    }

    /**
     * Procdiment que llegeix l'entrada del teclat per consola
     */
    private void llegeixTeclat() {

        Scanner     sc = new Scanner(System.in);    // Variable scanner


        /* Controla que l'entrada sigui numerica */
        try {
            /* Llegim el teclat */
            opcio = sc.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.err.println("Error, opcio no vàlida");

        }
            /* Opcio dins del rang [1..9]? */
            if (opcio < 1 || opcio > 9){

                System.out.println("ERROR: Opcio no valida");
                System.out.println("");
            /* Es forca valor per defecte [0] */
                opcio = 0;
            }




    }


}



