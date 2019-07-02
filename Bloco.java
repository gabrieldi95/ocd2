public class Bloco {
    /*
        Representa um pedaço de memória
     */
    
    Bloco(int valor, int pos)
    {
        this.valor = valor;
        this.pos = pos;
    }
    
    Bloco(int valor, int pos, String asm){
        this.valor = valor;
        this.pos = pos;
        this.asm = asm;
    }

    int valor;
    String asm;
    int pos;

    public String toString() {
        return "Valor: " + valor + " Posicao: " + pos;
    }

}
