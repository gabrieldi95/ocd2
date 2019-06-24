/*
    add op(0)[4bits] r1[2bits] r2[2bits] na
    sub op(1)[4bits] r1[2bits] r2[2bits] na
    addi op(2)[4bits] r1[2bits] na num[8bits]
    lw op(3)[4bits] r1[2bits] na addr[8bits]
    sw op(4)[4bits] r1[2bits] na addr[8bits]
    mv op(5)[4bits] r1[2bits] r2[2bits] na
    jump op(6)[4bits] na na num[8bits]
        0000 00 00 00000000
   8192 4096 2048 1024         512 256        128 64      32 16 8 4 2 1
 */

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

        int barramento = 0;
        Memoria mem = new Memoria();
        mem.insere(30, 10);
        mem.insere(99, 20);
        mem.print();

        // mem.getMem(342); //lança exception por que nao existe essa posicao

        le_codigo("/Users/gabriel.arruda@ibm.com/Desktop/Faculdade/OCD/EP2/src/codigo.txt");
        le_comando(17586);

        armazena_codigo("/Users/gabriel.arruda@ibm.com/Desktop/Faculdade/OCD/EP2/src/codigo.txt", mem);
        mem.print();
    }

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

    static void le_comando(int comando){
        int op = (int) Math.floor(comando/1024);
        int reg1 = (int) Math.floor((comando-1024*op)/256);
        int reg2 = (int) Math.floor((comando-1024*op-256*reg1)/64);
        int addr = (int) Math.floor(comando-1024*op-256*reg1-64*reg2);
    }

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
        }catch (NumberFormatException e){

        }

        switch (comando_arr[0]){
            case "ADD":
                switch (comando_arr[1]) {
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

                switch (comando_arr[2]){
                    case "BX":
                        result += 64;
                        break;
                    case "CX":
                        result += 128;
                        break;
                    case "DX":
                        result += 128 + 64;
                        break;
                }
                break;

            case "SUB":
                result += 1024;
                switch (comando_arr[1]) {
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

                switch (comando_arr[2]){
                    case "BX":
                        result += 64;
                        break;
                    case "CX":
                        result += 128;
                        break;
                    case "DX":
                        result += 128 + 64;
                        break;
                }
                break;

            case "ADDI":
                result += 1024*2;
                switch (comando_arr[1]) {
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
                result += constante;
                break;

            case "LW":
                result += 1024*3;
                switch (comando_arr[1]) {
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
                result += constante;
                break;

            case "SW":
                result += 1024*4;
                switch (comando_arr[1]) {
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
                result += constante;
                break;

            case "MOV":
                result += 1024*5;
                switch (comando_arr[1]) {
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

                switch (comando_arr[2]){
                    case "BX":
                        result += 64;
                        break;
                    case "CX":
                        result += 128;
                        break;
                    case "DX":
                        result += 128 + 64;
                        break;
                }
                break;

            case "JP":
                result += 1024*6;
                result += constante;
                break;

            default:
                throw new IllegalArgumentException("Comando assembly inválido");

        }
        return result;
    }

    static void armazena_codigo(String path, Memoria mem){
        ArrayList<String> comandos = le_codigo("/Users/gabriel.arruda@ibm.com/Desktop/Faculdade/OCD/EP2/src/codigo.txt");
        for (String comando : comandos) {
            mem.insere_semPos(compila(comando));
        }
    }

}
