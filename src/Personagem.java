
//======================Felipe===============================
// 🧙 ============================================================
// 📋 CLASSE PERSONAGEM — guarda o estado do aventureiro.
// As melhorias compradas permanecem quando o herói sobe de nível.
// =============================================================
public class Personagem {

    String nome;
    String classe;
    int nivel;
    int vida;
    int vidaMaxima;
    int ataque;
    int defesa;
    int pocoes;
    int moedas;
    int xp;                 // ⭐ XP do nível atual, com a sobra preservada.
    int melhoriasVida;      // 💗 Quantidade de melhorias de vida compradas.
    int melhoriasAtaque;    // ⚔️ Quantidade de melhorias de ataque compradas.

    public Personagem(String nome, String classe, int vidaMaxima,
                      int ataque, int defesa, int pocoes) {
        this.nome = nome;
        this.classe = classe;
        this.nivel = 1;
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;
        this.ataque = ataque;
        this.defesa = defesa;
        this.pocoes = pocoes;
        this.moedas = 30;
        this.xp = 0;
        this.melhoriasVida = 0;
        this.melhoriasAtaque = 0;
    }

    // ❤️ Método da classe: informa se o personagem ainda está vivo.
    // É usado pela Main para distinguir derrota de fuga.
    public boolean estaVivo() {
        return vida > 0;
    }
}
