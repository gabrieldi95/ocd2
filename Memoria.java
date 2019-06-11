import java.util.LinkedList;

public class Memoria extends LinkedList<Bloco> {

    void insere(int valor, int pos){

        for(Bloco e : this){
            if(e.pos == pos) {
                set(indexOf(e), new Bloco(valor, pos));
            }
        }
        add(new Bloco(valor, pos));

    }

    int getMem(int pos){

        for (Bloco e : this)
            if(e.pos == pos) return e.valor;
        try {
            throw new InvalidIndexException("Posicao nao encontrada");
        } catch (InvalidIndexException e) {
            e.printStackTrace();
        }
        return 0;
    }

    void print() {
        for (Bloco e : this)
            System.out.println(e);
    }

}
