/*
    add op(0)[4bits] r1[2bits] r2[2bits] na
    sub op(1)[4bits] r1[2bits] r2[2bits] na
    addi op(2)[4bits] r1[2bits] na num[8bits]
    lw op(3)[4bits] r1[2bits] na addr[8bits]
    sw op(4)[4bits] r1[2bits] na addr[8bits]
    mv op(5)[4bits] r1[2bits] r2[2bits] na
    jump op(6)[4bits] na na num[8bits]
    jge op(5)[4bits] r1[2bits] na num[8bits]
    jl op(5)[4bits] r1[2bits] na num[8bits]
        0000 00 00 00000000
     32.784 16392 8192 4096         2048 1024     512 256    128 64 32 16 8 4 2 1
 */

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class UC {

    static int[][] UC;

    static int AX;
    static int BX;
    static int CX;
    static int DX;
    static int MAR;
    static int MBR;

    static int PC = 1000;

    static int IR;
    static int op;
    static int p1;
    static int p2;

    static int ULA;
    static int x = 1;
    static int AC;

    static int barramento = 0;

    static int memoria;
    static int barr_mem;

    static int flag_mem;
    static int flag_ula;
    static boolean flag_const = false;

    static Memoria mem = new Memoria();

    public static void main(String[] args){

        /*
            REGISTRADORES
         */


//        mem.insere(30, 10);
//        mem.insere(99, 20);
//        mem.print();

        // mem.getMem(342); //lança exception por que nao existe essa posicao

//        le_codigo("/Users/gabriel.arruda@ibm.com/Desktop/Faculdade/OCD/EP2/src/codigo.txt");
//        le_comando(17586);
//
//        armazena_codigo("/Users/gabriel.arruda@ibm.com/Desktop/Faculdade/OCD/EP2/src/codigo.txt", mem);
//        mem.print();

        executa_codigo("/Users/gabriel.arruda@ibm.com/Desktop/Faculdade/OCD/EP2/src/codigo.txt");
    }






    static void executa_codigo(String codigo){
        ArrayList<String> programa = le_codigo(codigo);
        int pos = 1000;
        for (String comando : programa) {
            mem.insere(compila(comando), pos);
            pos++;
        }

        while(PC != pos){
            UC = ciclo_busca();
            executa_ciclo();
            le_comando(IR);
            UC = gera_ciclo_execucao();
            executa_ciclo();
            x=1;
        }
    }


    /**
     * Executa o ciclo que esta na UC, seja de busca ou execução
     */
    static void executa_ciclo(){
        /*
            LE A MATRIZ DA UC, COMPOSTA PELAS PORTAS A SEREM ABERTAS E AS ABRE UMA A UMA
         */
        flag_ula = 0;
        for(int i=0; i<UC.length; i++){
            for (int j = 0; j < 4; j++) {

                int flag = UC[i][4];
                flag_ula = (flag_ula > 0) ? flag_ula : UC[i][5];
                switch (UC[i][j]){
                    case 1:
                        PC = barramento;
                        break;
                    case 2:
                        barramento = PC;
                        break;
                    case 3:
                        MAR = barramento;
                        break;
                    case 4:
                        MBR = barramento;
                        break;
                    case 5:
                        barramento = MBR;
                        break;
                    case 6:
                        AX = barramento;
                        break;
                    case 7:
                        barramento = AX;
                        break;
                    case 8:
                        BX = barramento;
                        break;
                    case 9:
                        barramento = BX;
                        break;
                    case 10:
                        CX = barramento;
                        break;
                    case 11:
                        barramento = CX;
                        break;
                    case 12:
                        DX = barramento;
                        break;
                    case 13:
                        barramento = DX;
                        break;
                    case 14:
                        IR = barramento;
                        break;
                    case 15:
                        p2 = barramento;
                        break;
                    case 16:
                        barramento = p2;
                        break;
                    case 17:
                        p1 = barramento;
                        break;
                    case 18:
                        barramento = p1;
                        break;
                    case 19:
                        x = barramento;
                        break;
                    case 20:
                        ULA = barramento;
                        break;
                    case 21:
                        switch (flag_ula){
                            case 1:
                                AC = x+ULA;
                                break;
                            case 2:
                                AC = ULA-x;
                                break;
                            case 3:
                                AC = (ULA == 0) ? x : PC;
                        }

                        barramento = AC;
                        break;
                    case 22:
                        barr_mem = MAR;
                        break;
                    case 23:
                        barr_mem = MBR;
                        break;
                    case 24:
                        MBR = barr_mem;
                        break;
                    case 25:
                        switch (flag){
                            case 1:
                                memoria = barr_mem;
                                break;
                            case 3:
                                mem.insere(memoria, barr_mem);
                                break;
                        }
                        break;
                    case 26:
                        switch (flag){
                            case 2:
                                barr_mem = mem.getMem(memoria);
                        }
                        break;
                }
            }
            try {
                Thread.sleep(1000);
            }catch (Exception e){

            }
        }
    }


    /**
     *
     * @return Reotrna o ciclo de busca da instrução
     */
    static int[][] ciclo_busca(){
        // PORTAS: IGUAIS DIAGRAMAS, EXCETO AS DA IRP2 E IRP1 QUE ESTÃO INVERTIDAS.
        // COMO NO DIAGRAMA NAO TEM DX, DEPOIS DA PORTA 13 DEVE-SE SOMAR 2. POR EXEMPLO: A PORTA PARA A ULA NO DIAGRAMA
        // É 18, AQUI FICOU 20.
        // CADA LINHA DA MATRIZ REPRESENTA UM TEMPO
        // CADA NÚUMERO UMA PORTA A SER ABERTA
        // O PEULTIMO NÚMERO DE CADA LINHA REPRESENTA A FLAG DA MEMORIA
        // 1-AV, 2-READ, 3-WRITE
        // O ULTIMO NUMERO É A FLAG DA ULA
        int[][] busca = { {2, 3, 20, 0, 0, 0}, {22, 25, 21, 1, 1, 1}, {26, 24, 0, 0, 2, 0}, {5, 14, 0, 0, 0, 0} };
        return busca;
    }


    /**
     * Função que cria o ciclo de execução da próxima instrução
     * @return Uma matriz com n linhas e 6 colunas, onde cada número representa uma porta a ser aberta
     * no ciclo de execução de uma instrução
     */
    static int[][] gera_ciclo_execucao(){

        int[][] ciclos;
        int port1 = 0;
        int port2 = 0;
        switch (p1){
            case 0:
                port1 = 6;
                break;
            case 1:
                port1 = 8;
                break;
            case 2:
                port1 = 10;
                break;
            case 3:
                port1 = 12;
                break;
        }
        if(!flag_const) {
            switch (p2) {
                case 0:
                    port2 = 6;
                    break;
                case 1:
                    port2 = 8;
                    break;
                case 2:
                    port2 = 10;
                    break;
                case 3:
                    port2 = 12;
                    break;
            }
        }else port2 = 15;

        switch (op) {
            case 0:
                ciclos = new int[][] { {port1+1, 20, 0, 0, 0, 1}, {port2+1, 19, 0, 0, 0, 0}, {port1, 21, 0, 0, 0, 0} };
                return ciclos;

            case 1:
                ciclos = new int[][] { {port1+1, 20, 0, 0, 0, 2}, {port2+1, 19, 0, 0, 0, 0}, {port1, 21, 0, 0, 0, 0} };
                return ciclos;

            case 2:
                ciclos = new int[][] { {port1+1, 20, 0, 0, 0, 1}, {16, 19, 0, 0, 0, 0}, {21, port1, 0, 0, 0, 0} };
                return ciclos;

            case 3:
                ciclos = new int[][] { {16, 3, 0, 0, 0, 0}, {22, 25, 0, 0, 1, 0}, {26, 24, 0, 0, 2, 0}, {5, port1, 0, 0, 0, 0} };
                return ciclos;

            case 4:
                ciclos = new int[][] { {16, 3, 0, 0, 0, 0}, {22, 25, 0, 0, 1, 0}, {port1+1, 4, 0, 0, 3, 0}, {23, 25, 0, 0, 3, 0} };
                return ciclos;

            case 5:
                ciclos = new int[][] { {port2+1, port1, 0, 0, 0, 0}};
                return ciclos;
            case 6:
                ciclos = new int[][] { {16, 1, 0, 0, 0, 0}};
                return ciclos;
            case 7:
                ciclos = new int[][] { {port1+1, 20, 0, 0, 0, 3}, {16, 19, 0, 0, 0, 0}, {21, 1, 0, 0, 0, 0} };
                return ciclos;
        }
        ciclos = new int[0][0];
        return ciclos;
    }

    /**
     * Função que le um arquivo texto e carrega na memória do Java
     * @param arq Caminho absoluto até o arquivo
     * @return ArrayList contendo os comandos em string a serem executados
     */
    static ArrayList le_codigo(String arq){
        ArrayList<String> lines = new ArrayList<String>();

        try{
            FileReader codigo = new FileReader(arq);
            BufferedReader lerArq = new BufferedReader(codigo);

            String line = null;

            while ((line = lerArq.readLine()) != null) {
                lines.add(line);
            }
            codigo.close();
        }catch(IOException e){
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        return lines;
    }

    /**
     * le_comando Função que le um comando em decimal e retorna o IR, P1 e P2
     * @param comando int em decimal que representa uma linha de código Assembly
     */
    static void le_comando(int comando){
        int opc = (int) Math.floor(comando/4096);
        int reg1 = (int) Math.floor((comando-4096*opc)/1024);
        int reg2 = (int) Math.floor((comando-4096*opc-1024*reg1)/256);
        int constante = (int) Math.floor(comando-4096*opc-1024*reg1-256*reg2);

        op = opc;
        p1 = reg1;
        p2 = (flag_const) ? constante : reg2;

    }

    /**
     * Le uma string que representa uma linha de código e retorna um inteiro que representa esse comando
     * @param comando Uma linha de código em String
     * @return Retorna um inteiro que vai de 0 até 2^16, que representa um comando a ser executado
     */
    static int compila(String comando){

        int result = 0;
        int constante = 0;

        String[] comando_arr = comando.split(" ");

        /**************************************
         TO-DO:
         TRANSFORMAR DE HEXA PARA BINARIO
         VALIDAR TAMANHO DA CONSTANTE
         ************************************/
        try{
            constante += Integer.parseInt(comando_arr[2]);
            result += constante;
            flag_const = true;
        }catch (NumberFormatException e){
            flag_const = false;
        }

        switch (comando_arr[0]){
            case "ADD":
                switch (comando_arr[1]) {
                    case "BX":
                        result += 1024;
                        break;
                    case "CX":
                        result += 2048;
                        break;
                    case "DX":
                        result += 1024 + 2048;
                        break;
                }

                switch (comando_arr[2]){
                    case "BX":
                        result += 256;
                        break;
                    case "CX":
                        result += 512;
                        break;
                    case "DX":
                        result += 256 + 512;
                        break;
                }
                break;

            case "SUB":
                result += 4096;
                switch (comando_arr[1]) {
                    case "BX":
                        result += 1024;
                        break;
                    case "CX":
                        result += 2048;
                        break;
                    case "DX":
                        result += 1024 + 2048;
                        break;
                }

                switch (comando_arr[2]){
                    case "BX":
                        result += 256;
                        break;
                    case "CX":
                        result += 512;
                        break;
                    case "DX":
                        result += 256 + 512;
                        break;
                }
                break;

            case "ADDI":
                result += 4096*2;
                switch (comando_arr[1]) {
                    case "BX":
                        result += 1024;
                        break;
                    case "CX":
                        result += 2048;
                        break;
                    case "DX":
                        result += 1024 + 2048;
                        break;
                }
                break;

            case "LW":
                result += 4096*3;
                switch (comando_arr[1]) {
                    case "BX":
                        result += 1024;
                        break;
                    case "CX":
                        result += 2048;
                        break;
                    case "DX":
                        result += 1024 + 2048;
                        break;
                }
                break;

            case "SW":
                result += 4096*4;
                switch (comando_arr[1]) {
                    case "BX":
                        result += 1024;
                        break;
                    case "CX":
                        result += 2048;
                        break;
                    case "DX":
                        result += 1024 + 2048;
                        break;
                }
                break;

            case "MOV":
                result += 4096*5;
                switch (comando_arr[1]) {
                    case "BX":
                        result += 1024;
                        break;
                    case "CX":
                        result += 2048;
                        break;
                    case "DX":
                        result += 1024 + 2048;
                        break;
                }

                switch (comando_arr[2]){
                    case "BX":
                        result += 256;
                        break;
                    case "CX":
                        result += 512;
                        break;
                    case "DX":
                        result += 256 + 512;
                        break;
                }
                break;

            case "JP":
                result += 4095*6;
                break;

            case "JE":
                result += 4096*7;
                switch (comando_arr[1]) {
                    case "BX":
                        result += 1024;
                        break;
                    case "CX":
                        result += 2048;
                        break;
                    case "DX":
                        result += 1024 + 2048;
                        break;
                }
                break;

            default:
                throw new IllegalArgumentException("Comando assembly inválido");

        }
        return result;
    }



}
