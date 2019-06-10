public class Memoria {

    Bloco cabeca;

    void insere(int valor, int pos){

        Bloco novo = new Bloco(valor, pos);

        if(cabeca == null){
            cabeca = novo;
            cabeca.prox = null;
        }
        else{
            Bloco atual = cabeca;

            // Percorre até o ultimo elemento da lista ou o elemento na posição enviada
            while(atual.prox != null && atual.pos != pos){
                atual = atual.prox;
            }
            atual.prox = novo;
        }

    }

    int getMem(int pos){
        Bloco atual = cabeca;
        while(atual.pos != pos && atual.prox != null){
            atual = atual.prox;
        }
        if(atual.pos == pos){
            return atual.pos;
        }else try {
            throw new InvalidIndexException("Posicao nao encontrada");
        } catch (InvalidIndexException e) {
            e.printStackTrace();
        }
        return 0;
    }

    void print(){
        Bloco atual = cabeca;
        while(atual != null){
            System.out.println("Valor: " + atual.valor + " Posicao: " + atual.pos);
            atual = atual.prox;
        }
    }

}
