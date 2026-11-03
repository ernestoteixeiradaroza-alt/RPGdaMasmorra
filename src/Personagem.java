import java.util.ArrayList;


public class Personagem {


    String nome;
    String classe;
    int nivel;
    int vida;
    int vidaMaxima;
    int ataque;
    int defesa;
    int xp;
    int moedas;
    int veneno;                 // turnos de veneno restantes (0 = sem veneno)
    ArrayList<Item> mochila;    // precisa de new antes de usar
}