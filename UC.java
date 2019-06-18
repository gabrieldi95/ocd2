import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class UC {

    public static void main(String[] args){

        /*
            REGISTRADORES
         */
        int AX;
        int BX;
        int CX;
        int DX;

        int registrador = 0;
        Memoria mem = new Memoria();
        mem.insere(30, 10);
        mem.insere(99, 20);
        mem.print();

        // mem.getMem(342); //lança exception por que nao existe essa posicao

        le_codigo("/Users/gabriel.arruda@ibm.com/Desktop/Faculdade/OCD/EP2/src/codigo.txt");

    }

    static ArrayList le_codigo(String arq){

        ArrayList<String> lines = new ArrayList<String>();

        try{
            FileReader codigo = new FileReader(arq);
            BufferedReader lerArq = new BufferedReader(codigo);

            String line = null;

            while ((line = lerArq.readLine()) != null) {
                System.out.printf("%s\n", line);

                lines.add(line);
            }
            codigo.close();
        }catch(IOException e){
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        return lines;
    }

}
