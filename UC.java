import java.util.LinkedList;
import java.util.ListIterator;

public class UC {

    public static void main(String[] args){
        int registrador = 0;
        Memoria mem = new Memoria();
        mem.insere(30, 10);
        mem.insere(99, 20);
        mem.print();

        // mem.getMem(342); //lança exception por que nao existe essa posicao
    }

}
