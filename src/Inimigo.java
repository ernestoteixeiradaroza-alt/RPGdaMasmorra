public class Inimigo {


    String nome;
    int vida;
    int vidaMaxima;
    int ataque;
    int defesa;
    int recompensa;


    int especial;      // dano do ataque especial (0 = nao tem)
    int cura;          // quanto ele se cura (0 = nao se cura)
    boolean venenoso;  // o ataque especial envenena?
    boolean fugitivo;  // foge quando a vida esta baixa?


    // estado dentro da batalha
    boolean jaCurou;
    boolean fugiu;


    String aviso;      // frase antes do ataque especial
}