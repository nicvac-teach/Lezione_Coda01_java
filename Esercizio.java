//LEGGERE LE ISTRUZIONI NEL FILE README.md

//Import di Classi Java necessarie al funzionamento del programma
import java.util.Scanner;

// Classe principale, con metodo main
class Esercizio {
    // Il programma parte con una chiamata a main().
    public static void main(String args[])
    {
        //Variabili del programma
        String nome;

        //Creo l'oggetto in per l'input da tastiera
        Scanner in = new Scanner( System.in );

        //Leggo l'input da tastiera
        System.out.print("Inserisci il tuo nome: ");
        nome = in.nextLine();

        //Output del nome acquisito da tastiera
        System.out.println("Ciao "+nome+"!");
    }
}

//LEGGERE LE ISTRUZIONI NEL FILE README.md