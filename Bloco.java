public class Bloco {
    /*
        Representa um pedaço de memória
     */

    Bloco(int valor, int pos){
        this.valor = valor;
        this.pos = pos;
    }

    int valor;

    int pos;

    public String toString() {
        return "Valor: " + valor + " Posicao: " + pos;
    }

}
