// 🧪 ============================================================
// 📋 CLASSE ITEM — representa poções e melhorias da loja.
// Cada efeito só é aplicado quando seu valor é maior que zero.
// =============================================================
public class Item {

    String nome;
    int cura;           // ❤️ Cura de uma poção consumida na batalha.
    int bonusVida;      // 💗 Aumento permanente de vida máxima.
    int bonusAtaque;    // ⚔️ Aumento permanente de ataque.
    int preco;          // 💰 Preço atual da compra.
    int aumentoPreco;   // 📈 Quanto o preço sobe após cada compra.

    public Item(String nome, int cura, int bonusVida, int bonusAtaque,
                int preco, int aumentoPreco) {
        this.nome = nome;
        this.cura = cura;
        this.bonusVida = bonusVida;
        this.bonusAtaque = bonusAtaque;
        this.preco = preco;
        this.aumentoPreco = aumentoPreco;
    }
}
