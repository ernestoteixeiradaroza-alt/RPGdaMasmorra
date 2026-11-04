// 👹 ============================================================
// 📋 CLASSE INIMIGO — atributos e recompensas dos guardiões.
// =============================================================
public class Inimigo {

    String nome;
    int vida;
    int ataque;
    int defesa;
    int recompensa;       // ⭐ XP entregue ao ser derrotado.
    int recompensaMoedas; // 💰 Moedas entregues ao ser derrotado.

    public Inimigo(String nome, int vida, int ataque, int defesa,
                   int recompensa, int recompensaMoedas) {
        this.nome = nome;
        this.vida = vida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.recompensa = recompensa;
        this.recompensaMoedas = recompensaMoedas;
    }
}
