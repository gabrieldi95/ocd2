import java.util.LinkedList;

/*
    * Lista ligada com métodos personalizados para a memória
 */
public class Memoria extends LinkedList<Bloco> {

    void insere(int valor, int pos){

        // Busca primeiro se ejá existe um bloco nessa posição e coloca no lugar dele
        for(Bloco e : this){
            if(e.pos == pos) {
                set(indexOf(e), new Bloco(valor, pos));
            }
        }
        // Se não tiver insere no final da lista
        add(new Bloco(valor, pos));

    }

    int getMem(int pos){
        for (Bloco e : this)
            if(e.pos == pos) return e.valor;

        // Se não achar um bloco com essa posição joga uma exceção
        try {
            throw new InvalidPositionException("Posicao nao encontrada");
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        return 0;
    }

    void print() {
        for (Bloco e : this)
            System.out.println(e);
    }

}
