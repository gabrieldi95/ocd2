public class UC {

    public static void main(String[] args){
        Memoria mem = new Memoria();
        mem.insere(30, 10);
        mem.insere(30, 20);
        mem.print();
        System.out.println(mem.getMem(10));
        //mem.getMem(342); lança exception por que nao existe essa posicao
    }

}
