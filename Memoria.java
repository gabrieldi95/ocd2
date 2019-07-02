import java.util.LinkedList;

/*
    * Lista ligada com métodos personalizados para a memória
 */
public class Memoria extends LinkedList<Bloco> {

    void insere(int valor, int pos, String comando){

        // Busca primeiro se já existe um bloco nessa posição e coloca no lugar dele
        for(Bloco e : this){
            if(e.pos == pos) {
                set(indexOf(e), new Bloco(valor, pos, comando));
            }
        }
        // Se não tiver insere no final da lista
        add(new Bloco(valor, pos, comando));

    }

    void insere_semPos(int valor){
        // Busca uma posicao disponível na memória e aloca

        int pos = (int) (Math.random() * 101);
        boolean ocupado = true;

        while(ocupado){
            pos = (int) (Math.random() * 101);
            for(Bloco e : this){
                if(e.pos == pos) {
                    ocupado = true;
                    break;
                }
                else {
                    ocupado = false;
                }
            }
        }
        add(new Bloco(valor, pos));

    }

    Bloco getMem(int pos){
        for (Bloco e : this)
            if(e.pos == pos) return e;

        // Se não achar um bloco com essa posição joga uma exceção
        try {
            throw new InvalidPositionException("Posicao nao encontrada");
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
        return null;
    }

    void print() {
        for (Bloco e : this)
            System.out.println(e);
    }

}
