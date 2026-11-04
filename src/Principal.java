import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Principal {

    // ⭐ XP progressivo: 20, 30, 40, 50...
    static final int XP_INICIAL = 20;
    static final int AUMENTO_XP_POR_NIVEL = 10;

    // 🎲 Chance de encontrar um evento ao seguir para a próxima sala.
    static final int CHANCE_EVENTO = 50;
    static final Random SORTEIO = new Random();

    // 🚀 ===== INÍCIO DO JOGO =====
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("   🏰 A MASMORRA DOS TRÊS GUARDIÕES 🏰");
        System.out.println("=========================================");
        System.out.println("🐉 Derrote os três guardiões para voltar para casa!");
        System.out.println("🏪 A loja só abre quando você escolher.");
        System.out.println("🎲 Ao avançar, você pode encontrar eventos aleatórios.\n");

        System.out.print("🧙 Qual é o seu nome, aventureiro? ");
        String nome = leitor.hasNextLine() ? leitor.nextLine().trim() : "";
        if (nome.isEmpty()) {
            nome = "Aventureiro";
        }

        System.out.println("\n🎭 Escolha sua classe:");
        System.out.println("1 - ⚔️ Guerreiro (muita vida e defesa)");
        System.out.println("2 - 🔮 Mago (muito ataque, pouca vida)");
        System.out.println("3 - 🏹 Arqueiro (equilibrado)");
        int opcaoClasse = lerOpcao(leitor, "👉 Opção: ", 1, 3);

        Personagem heroi = criarHeroi(nome, opcaoClasse);
        ArrayList<Item> itensLoja = criarItensLoja();
        ArrayList<Inimigo> inimigos = criarInimigos();
        ArrayList<String> derrotados = new ArrayList<>();
        Item pocao = itensLoja.get(0);
        boolean fugiu = false;

        mostrarFicha(heroi);

        // 🗺️ A loja nunca abre automaticamente: a escolha fica no menu.
        for (int i = 0; i < inimigos.size(); i++) {
            Inimigo atual = inimigos.get(i);
            boolean avancar = menuEntreSalas(
                    heroi, itensLoja, leitor, i + 1, inimigos.size(), atual.nome);

            if (!avancar) {
                fugiu = true;
                break;
            }

            // 🎲 Um único sorteio por caminho, antes da batalha.
            // Visitar a loja ou consultar a ficha não gera eventos.
            System.out.println("\n🧭 Você segue em direção à próxima sala...");
            tentarEvento(heroi, pocao);

            System.out.println("\n--- 🚪 SALA " + (i + 1)
                    + " DE " + inimigos.size() + " ---");

            if (!batalhar(heroi, atual, leitor, pocao)) {
                // Usa um método do próprio objeto Personagem.
                fugiu = heroi.estaVivo();
                break;
            }

            derrotados.add(atual.nome);
            processarVitoria(heroi, atual);
        }

        mostrarResultado(heroi, derrotados, inimigos.size(), fugiu);
        leitor.close();
    }

    // 🧙 Cria o herói uma única vez, com os atributos da classe escolhida.
    public static Personagem criarHeroi(String nome, int opcaoClasse) {
        switch (opcaoClasse) {
            case 1:
                return new Personagem(nome, "Guerreiro", 100, 12, 5, 2);
            case 2:
                return new Personagem(nome, "Mago", 80, 16, 2, 3);
            case 3:
                return new Personagem(nome, "Arqueiro", 90, 14, 3, 3);
            default:
                return new Personagem(nome, "Aprendiz", 80, 10, 3, 1);
        }
    }

    // 👹 Os dados dos inimigos ficam reunidos em um único método.
    public static ArrayList<Inimigo> criarInimigos() {
        ArrayList<Inimigo> inimigos = new ArrayList<>();
        inimigos.add(new Inimigo("Goblin", 25, 8, 2, 15, 15));
        inimigos.add(new Inimigo("Esqueleto", 35, 10, 4, 20, 20));
        inimigos.add(new Inimigo("Dragão", 60, 16, 6, 35, 35));
        return inimigos;
    }

    // 🏪 O catálogo usa a mesma classe Item para poções e melhorias.
    public static ArrayList<Item> criarItensLoja() {
        ArrayList<Item> itens = new ArrayList<>();
        itens.add(new Item("Poção de vida", 20, 0, 0, 10, 0));
        itens.add(new Item("Melhoria de vida", 0, 10, 0, 20, 10));
        itens.add(new Item("Melhoria de ataque", 0, 0, 2, 25, 15));
        return itens;
    }

    // 🧭 Menu entre batalhas. A loja só abre quando o jogador escolhe 2.
    public static boolean menuEntreSalas(
            Personagem heroi, ArrayList<Item> itensLoja, Scanner leitor,
            int proximaSala, int totalSalas, String proximoInimigo) {

        int opcao;
        do {
            System.out.println("\n=========================================");
            System.out.println("🧭 CAMINHO PARA A SALA " + proximaSala
                    + "/" + totalSalas + " — 👹 " + proximoInimigo);
            System.out.println("❤️ Vida: " + heroi.vida + "/" + heroi.vidaMaxima
                    + " | 💰 Moedas: " + heroi.moedas);
            System.out.println("1 - 🚶 Avançar (pode acontecer um evento)");
            System.out.println("2 - 🏪 Visitar a loja");
            System.out.println("3 - 📋 Ver ficha do herói");
            System.out.println("0 - 🏃 Voltar para a vila e encerrar a jornada");

            opcao = lerOpcao(leitor, "👉 O que você quer fazer? ", 0, 3);
            switch (opcao) {
                case 2:
                    abrirLoja(heroi, itensLoja, leitor);
                    break;
                case 3:
                    mostrarFicha(heroi);
                    break;
            }
        } while (opcao != 1 && opcao != 0);

        return opcao == 1;
    }

    // 🏪 Catálogo único: adicionar outro Item não exige criar outro case.
    public static void abrirLoja(
            Personagem heroi, ArrayList<Item> itensLoja, Scanner leitor) {

        int opcao;
        do {
            System.out.println("\n=========================================");
            System.out.println("             🏪 LOJA DA VILA");
            System.out.println("=========================================");
            System.out.println("💰 Moedas: " + heroi.moedas
                    + " | 🧪 Poções: " + heroi.pocoes);
            System.out.println("❤️ Vida: " + heroi.vida + "/" + heroi.vidaMaxima
                    + " | ⚔️ Ataque: " + heroi.ataque);

            for (int i = 0; i < itensLoja.size(); i++) {
                Item item = itensLoja.get(i);
                System.out.println((i + 1) + " - " + item.nome + " | "
                        + descricaoItem(item) + " | 💰 " + item.preco + " moedas");
            }
            System.out.println("0 - 🚪 Sair da loja");

            opcao = lerOpcao(leitor, "👉 Opção: ", 0, itensLoja.size());
            if (opcao > 0) {
                comprarItem(heroi, itensLoja.get(opcao - 1));
            }
        } while (opcao != 0);

        System.out.println("👋 Volte quando quiser, " + heroi.nome + "!");
    }

    public static String descricaoItem(Item item) {
        if (item.cura > 0) {
            return "🧪 recupera até " + item.cura + " de vida ao usar";
        }
        if (item.bonusVida > 0) {
            return "💗 +" + item.bonusVida + " de vida máxima e atual (permanente)";
        }
        return "⚔️ +" + item.bonusAtaque + " de ataque (permanente)";
    }

    // 💰 Toda compra usa a mesma validação de saldo e atualização de preço.
    public static boolean comprarItem(Personagem heroi, Item item) {
        if (heroi.moedas < item.preco) {
            System.out.println("🚫 Moedas insuficientes! Faltam "
                    + (item.preco - heroi.moedas) + " moedas.");
            return false;
        }

        heroi.moedas -= item.preco;

        if (item.cura > 0) {
            heroi.pocoes++;
            System.out.println("✅ Você comprou 1 " + item.nome + "!");
        }
        if (item.bonusVida > 0) {
            heroi.vidaMaxima += item.bonusVida;
            curar(heroi, item.bonusVida);
            heroi.melhoriasVida++;
            System.out.println("💗 Vida máxima aumentada em " + item.bonusVida
                    + "! Agora: " + heroi.vidaMaxima + ".");
        }
        if (item.bonusAtaque > 0) {
            heroi.ataque += item.bonusAtaque;
            heroi.melhoriasAtaque++;
            System.out.println("⚔️ Ataque aumentado em " + item.bonusAtaque
                    + "! Agora: " + heroi.ataque + ".");
        }

        item.preco += item.aumentoPreco;
        System.out.println("💰 Saldo: " + heroi.moedas + " moedas.");
        if (item.aumentoPreco > 0) {
            System.out.println("📈 Próxima " + item.nome + ": "
                    + item.preco + " moedas.");
        }
        return true;
    }

    // 🎲 Sorteia um evento uma única vez por avanço, com 50% de chance.
    public static boolean tentarEvento(Personagem heroi, Item pocao) {
        if (sortear(1, 100) > CHANCE_EVENTO) {
            System.out.println("🌙 O caminho está tranquilo. Nenhum evento aconteceu.");
            return false;
        }

        executarEvento(heroi, pocao, sortear(1, 6));
        return true;
    }

    // 🎭 Três eventos positivos e três negativos, todos com a mesma chance.
    // Armadilhas machucam, mas não matam fora de combate.
    public static void executarEvento(Personagem heroi, Item pocao, int evento) {
        System.out.println("\n✨ ===== EVENTO ALEATÓRIO ===== ✨");

        switch (evento) {
            case 1: {
                int moedas = sortear(10, 20);
                heroi.moedas += moedas;
                System.out.println("💰 Você encontrou uma bolsa com "
                        + moedas + " moedas! Saldo: " + heroi.moedas + ".");
                break;
            }
            case 2:
                heroi.pocoes++;
                System.out.println("🎁 Um baú escondido tinha 1 " + pocao.nome
                        + "! Poções: " + heroi.pocoes + ".");
                break;
            case 3: {
                int vidaRecuperada = curar(heroi, sortear(15, 30));
                System.out.println("⛲ Uma fonte mágica recuperou "
                        + vidaRecuperada + " de vida! ❤️ " + heroi.vida
                        + "/" + heroi.vidaMaxima + ".");
                break;
            }
            case 4: {
                int dano = Math.min(heroi.vida - 1, sortear(8, 15));
                heroi.vida -= dano;
                System.out.println("🕳️ Uma armadilha causou " + dano
                        + " de dano! ❤️ Vida: " + heroi.vida
                        + "/" + heroi.vidaMaxima + ".");
                break;
            }
            case 5: {
                int perda = Math.min(heroi.moedas, sortear(5, 15));
                heroi.moedas -= perda;
                System.out.println("💸 Você deixou cair " + perda
                        + " moedas! Saldo: " + heroi.moedas + ".");
                break;
            }
            default: {
                int dano = Math.min(heroi.vida - 1, sortear(5, 10));
                heroi.vida -= dano;
                System.out.println("🪨 Pedras caíram do teto e causaram " + dano
                        + " de dano! ❤️ Vida: " + heroi.vida
                        + "/" + heroi.vidaMaxima + ".");
                break;
            }
        }
    }

    // ❤️ Reaproveitado pela fonte, pelas poções e pela melhoria de vida.
    public static int curar(Personagem heroi, int quantidade) {
        int vidaRecuperada = Math.min(quantidade, heroi.vidaMaxima - heroi.vida);
        heroi.vida += vidaRecuperada;
        return vidaRecuperada;
    }

    // 🏆 Recompensas e XP são processados somente após uma vitória.
    public static void processarVitoria(Personagem heroi, Inimigo inimigo) {
        System.out.println("\n🏆 " + inimigo.nome + " derrotado! (+"
                + inimigo.recompensa + " XP)");
        ganharXP(heroi, inimigo.recompensa);
        heroi.moedas += inimigo.recompensaMoedas;
        System.out.println("💰 Você ganhou " + inimigo.recompensaMoedas
                + " moedas! Total: " + heroi.moedas + ".");
        mostrarFicha(heroi);
    }

    // 📋 Ficha com XP em texto, sem barra gráfica.
    public static void mostrarFicha(Personagem heroi) {
        System.out.println("\n=== 📋 FICHA DO HERÓI ===");
        System.out.println("🧙 Nome: " + heroi.nome);
        System.out.println("🎭 Classe: " + heroi.classe);
        System.out.println("🌟 Nível: " + heroi.nivel);
        mostrarProgressoXP(heroi);
        System.out.println("❤️ Vida: " + heroi.vida + "/" + heroi.vidaMaxima);
        System.out.println("⚔️ Ataque: " + heroi.ataque);
        System.out.println("🛡️ Defesa: " + heroi.defesa);
        System.out.println("🧪 Poções: " + heroi.pocoes);
        System.out.println("💰 Moedas: " + heroi.moedas);
        System.out.println("🛠️ Melhorias compradas: 💗 vida "
                + heroi.melhoriasVida + " | ⚔️ ataque " + heroi.melhoriasAtaque);
        System.out.println("======================");
    }

    // 🎯 XP necessário para concluir o nível atual.
    public static int xpParaProximoNivel(int nivel) {
        return XP_INICIAL + (nivel - 1) * AUMENTO_XP_POR_NIVEL;
    }

    // ⭐ Preserva a sobra e permite subir vários níveis de uma vez.
    public static void ganharXP(Personagem heroi, int quantidade) {
        heroi.xp += quantidade;

        while (heroi.xp >= xpParaProximoNivel(heroi.nivel)) {
            heroi.xp -= xpParaProximoNivel(heroi.nivel);
            heroi.nivel++;
            heroi.vidaMaxima += 10;
            heroi.ataque += 2;
            heroi.vida = heroi.vidaMaxima;

            System.out.println("\n🎉✨ LEVEL UP! Você chegou ao nível "
                    + heroi.nivel + "! ✨🎉");
            System.out.println("💗 Vida máxima: " + heroi.vidaMaxima
                    + " | ⚔️ Ataque: " + heroi.ataque);
            System.out.println("❤️ Sua vida foi totalmente recuperada!");
        }
    }

    public static void mostrarProgressoXP(Personagem heroi) {
        int necessario = xpParaProximoNivel(heroi.nivel);
        System.out.println("⭐ XP: " + heroi.xp + "/" + necessario);
        System.out.println("🎯 Faltam " + (necessario - heroi.xp)
                + " XP para o nível " + (heroi.nivel + 1) + ".");
    }

    // 🎲 Um único gerador aleatório para dados, eventos e recompensas.
    public static int sortear(int minimo, int maximo) {
        return SORTEIO.nextInt(maximo - minimo + 1) + minimo;
    }

    public static int rolarDado() {
        return sortear(1, 6);
    }

    // ⚔️ Retorna true somente quando o herói derrota o inimigo.
    public static boolean batalhar(
            Personagem heroi, Inimigo inimigo, Scanner leitor, Item pocao) {

        System.out.println("\n⚠️ Um " + inimigo.nome + " apareceu! 👹 (❤️ vida "
                + inimigo.vida + ", ⚔️ ataque " + inimigo.ataque
                + ", 🛡️ defesa " + inimigo.defesa + ")");

        boolean fugiu = false;
        int opcao;

        do {
            System.out.println("\n--- 🧙 " + heroi.nome + ": ❤️ "
                    + heroi.vida + "/" + heroi.vidaMaxima + " | 👹 "
                    + inimigo.nome + ": ❤️ " + inimigo.vida + " ---");
            System.out.println("1 - ⚔️ Atacar (ataque atual: " + heroi.ataque + ")");
            System.out.println("2 - 🧪 Tomar " + pocao.nome + " (+ "
                    + pocao.cura + " de vida, restam " + heroi.pocoes + ")");
            System.out.println("3 - 🏃 Fugir");
            opcao = lerOpcao(leitor, "👉 O que você faz? ", 1, 3);

            switch (opcao) {
                case 1: {
                    int dado = rolarDado();
                    int dano = Math.max(1, heroi.ataque + dado - inimigo.defesa);
                    inimigo.vida = Math.max(0, inimigo.vida - dano);
                    System.out.println("⚔️ Você atacou! 🎲 Dado: " + dado
                            + " | 💥 Dano: " + dano + ".");
                    break;
                }
                case 2:
                    if (heroi.pocoes == 0) {
                        System.out.println("🚫 Sem poções! Você perdeu a vez.");
                    } else if (heroi.vida == heroi.vidaMaxima) {
                        // Não gasta uma poção nem perde o turno com a vida cheia.
                        System.out.println("❤️ Sua vida já está cheia. A poção foi guardada.");
                        continue;
                    } else {
                        heroi.pocoes--;
                        int vidaRecuperada = curar(heroi, pocao.cura);
                        System.out.println("🧪 Você recuperou " + vidaRecuperada
                                + " de vida. ❤️ " + heroi.vida + "/" + heroi.vidaMaxima
                                + " | 🧪 Poções: " + heroi.pocoes + ".");
                    }
                    break;
                case 3:
                    System.out.println("🏃 Você fugiu da batalha!");
                    fugiu = true;
                    break;
                case 0:
                    // A entrada terminou: encerra a batalha sem repetir ataques.
                    fugiu = true;
                    break;
            }

            // 💢 O contra-ataque mantém a regra de causar pelo menos 1 de dano.
            if (!fugiu && inimigo.vida > 0 && heroi.vida > 0) {
                int danoInimigo = Math.max(1, inimigo.ataque - heroi.defesa);
                heroi.vida = Math.max(0, heroi.vida - danoInimigo);
                System.out.println("💢 " + inimigo.nome + " causou "
                        + danoInimigo + " de dano.");
                if (heroi.vida == 0) {
                    System.out.println("💀 " + heroi.nome + " caiu...");
                }
            }
        } while (inimigo.vida > 0 && heroi.vida > 0 && !fugiu);

        return inimigo.vida == 0 && heroi.vida > 0;
    }

    // 🔢 Centraliza a leitura e evita erros com letras ou opções fora do menu.
    public static int lerOpcao(
            Scanner leitor, String mensagem, int minimo, int maximo) {
        while (true) {
            System.out.print(mensagem);
            if (!leitor.hasNextLine()) {
                return 0; // Encerra os menus com segurança se a entrada acabar.
            }
            try {
                int opcao = Integer.parseInt(leitor.nextLine().trim());
                if (opcao >= minimo && opcao <= maximo) {
                    return opcao;
                }
            } catch (NumberFormatException ignorada) {
                // Mostra a mensagem abaixo e pede a opção novamente.
            }
            System.out.println("⚠️ Digite um número entre "
                    + minimo + " e " + maximo + ".");
        }
    }

    // 🏁 Resumo final, incluindo as melhorias compradas.
    public static void mostrarResultado(
            Personagem heroi, ArrayList<String> derrotados,
            int totalInimigos, boolean fugiu) {

        System.out.println("\n=========================================");
        if (heroi.vida == 0) {
            System.out.println("💀 Você caiu na masmorra... FIM DE JOGO.");
        } else if (derrotados.size() == totalInimigos) {
            System.out.println("🎉 VOCÊ VENCEU! Os três guardiões foram derrotados! 🎉");
        } else if (fugiu) {
            System.out.println("🏃 Você voltou para a vila e encerrou a jornada.");
        }

        System.out.println("👹 Inimigos derrotados: " + derrotados.size()
                + " de " + totalInimigos);
        mostrarFicha(heroi);

        if (!derrotados.isEmpty()) {
            System.out.println("🏅 Troféus da jornada: "
                    + String.join(", ", derrotados));
        }
        System.out.println("=========================================");
    }
}
