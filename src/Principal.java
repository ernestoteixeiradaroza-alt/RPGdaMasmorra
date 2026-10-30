import java.util.ArrayList;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) { // <-- ABRE O MAIN

        Scanner leitor = new Scanner(System.in);

        // ===== 1. APRESENTAÇÃO =====
        System.out.println("=========================================");
        System.out.println("   A MASMORRA DOS TRÊS GUARDIÕES");
        System.out.println("=========================================");
        System.out.println("Três guardiões bloqueiam a saída da masmorra.");
        System.out.println("Derrote todos para voltar para casa!\n");

        System.out.print("Qual é o seu nome, aventureiro? ");
        String nomeDigitado = leitor.nextLine();

        // ===== 2. ESCOLHA DA CLASSE (switch) =====
        System.out.println("\nEscolha sua classe:");
        System.out.println("1 - Guerreiro (muita vida e defesa)");
        System.out.println("2 - Mago      (muito ataque, pouca vida)");
        System.out.println("3 - Arqueiro  (equilibrado)");
        System.out.print("Opção: ");
        int opcaoClasse = leitor.nextInt();

        Personagem heroi = new Personagem();
        heroi.nome = nomeDigitado;
        heroi.nivel = 1;
        heroi.xp = 0;

        switch (opcaoClasse) {
            case 1:
                heroi.classe = "Guerreiro";
                heroi.vidaMaxima = 100;
                heroi.ataque = 12;
                heroi.defesa = 5;
                heroi.pocoes = 2;
                break;
            case 2:
                heroi.classe = "Mago";
                heroi.vidaMaxima = 80;
                heroi.ataque = 16;
                heroi.defesa = 2;
                heroi.pocoes = 3;
                break;
            case 3:
                heroi.classe = "Arqueiro";
                heroi.vidaMaxima = 90;
                heroi.ataque = 14;
                heroi.defesa = 3;
                heroi.pocoes = 3;
                break;
            default:
                heroi.classe = "Aprendiz";
                heroi.vidaMaxima = 80;
                heroi.ataque = 10;
                heroi.defesa = 3;
                heroi.pocoes = 1;
                System.out.println("Opção inválida... você entra como Aprendiz!");
        } // fecha o switch (opcaoClasse)

        heroi.vida = heroi.vidaMaxima;
        mostrarFicha(heroi);

        // ===== 3. OS INIMIGOS (ArrayList) =====
        ArrayList<Inimigo> inimigos = new ArrayList<>();

        Inimigo goblin = new Inimigo();
        goblin.nome = "Goblin";
        goblin.vida = 25;
        goblin.ataque = 8;
        goblin.defesa = 2;
        goblin.recompensa = 10;
        inimigos.add(goblin);

        Inimigo esqueleto = new Inimigo();
        esqueleto.nome = "Esqueleto";
        esqueleto.vida = 35;
        esqueleto.ataque = 10;
        esqueleto.defesa = 4;
        esqueleto.recompensa = 15;
        inimigos.add(esqueleto);

        Inimigo dragao = new Inimigo();
        dragao.nome = "Dragão";
        dragao.vida = 60;
        dragao.ataque = 16;
        dragao.defesa = 6;
        dragao.recompensa = 30;
        inimigos.add(dragao);

        // ===== 4. A JORNADA (for + batalhas) =====
        ArrayList<String> derrotados = new ArrayList<>();
        int sala = 0;

        for (int i = 0; i < inimigos.size(); i++) {
            sala++;
            Inimigo atual = inimigos.get(i);
            System.out.println("\n--- SALA " + sala + " DE " + inimigos.size() + " ---");

            boolean venceu = batalhar(heroi, atual, leitor);

            if (venceu) {
                derrotados.add(atual.nome);
                System.out.println("\n" + atual.nome + " derrotado! (+" + atual.recompensa + " XP)");
                heroi.xp += atual.recompensa;

                if (heroi.xp >= heroi.nivel * 20) {
                    heroi.nivel++;
                    heroi.vidaMaxima += 10;
                    heroi.ataque += 2;
                    heroi.vida = heroi.vidaMaxima;
                    System.out.println("*** Você subiu para o nível " + heroi.nivel + "! ***");
                    System.out.println("Vida máxima: " + heroi.vidaMaxima + " | Ataque: " + heroi.ataque);
                } // fecha o if de subir de nivel
            } else {
                break;
            }
        } // fecha o for das salas

        // ===== 5. RESULTADO FINAL (if/else) =====
        System.out.println("\n=========================================");
        if (heroi.vida <= 0) {
            System.out.println("Você caiu na masmorra... FIM DE JOGO.");
            System.out.println("Inimigos derrotados: " + derrotados.size() + " de " + inimigos.size());
        } else if (derrotados.size() == inimigos.size()) {
            System.out.println("VOCÊ VENCEU! Os três guardiões foram derrotados!");
            System.out.println("Nível final: " + heroi.nivel + " | XP: " + heroi.xp);
            System.out.println("Vida restante: " + heroi.vida + "/" + heroi.vidaMaxima);
        } else {
            System.out.println("Você fugiu da masmorra e voltou para a vila.");
            System.out.println("Inimigos derrotados: " + derrotados.size() + " de " + inimigos.size());
        } // fecha o if / else if / else do resultado

        if (derrotados.size() > 0) {
            System.out.print("Troféus da jornada: ");
            for (int i = 0; i < derrotados.size(); i++) {
                System.out.print(derrotados.get(i));
                if (i < derrotados.size() - 1) {
                    System.out.print(", ");
                }
            } // fecha o for dos trofeus
            System.out.println();
        } // fecha o if dos trofeus
        System.out.println("=========================================");

        leitor.close();
    } // === FECHA O MAIN ===

    // ===== MÉTODOS "RECEITA" =====

    // Mostra a ficha do herói na tela.
    public static void mostrarFicha(Personagem p) { // <-- ABRE o metodo mostrarFicha
        System.out.println("\n=== FICHA DO HERÓI ===");
        System.out.println("Nome: " + p.nome);
        System.out.println("Classe: " + p.classe);
        System.out.println("Nível: " + p.nivel);
        System.out.println("Vida: " + p.vida + "/" + p.vidaMaxima);
        System.out.println("Ataque: " + p.ataque);
        System.out.println("Defesa: " + p.defesa);
        System.out.println("Poções: " + p.pocoes);
        System.out.println("======================");
    } // fecha o metodo mostrarFicha

    // Devolve um número de 1 a 6, como um dado de RPG.
    public static int rolarDado() { // <-- ABRE o metodo rolarDado
        return (int) (Math.random() * 6) + 1;
    } // fecha o metodo rolarDado

    // Batalha contra um inimigo. Devolve true se o herói venceu.
    public static boolean batalhar(Personagem heroi, Inimigo inimigo, Scanner leitor) { // <-- ABRE o metodo batalhar
        System.out.println("\n>>> Um " + inimigo.nome + " apareceu! (vida " + inimigo.vida
                + ", ataque " + inimigo.ataque + ", defesa " + inimigo.defesa + ")");

        boolean fugiu = false;
        int opcao;

        do {
            System.out.println("\n--- " + heroi.nome + ": " + heroi.vida + " de vida | "
                    + inimigo.nome + ": " + inimigo.vida + " de vida ---");
            System.out.println("1 - Atacar");
            System.out.println("2 - Tomar poção (+20 de vida, restam " + heroi.pocoes + ")");
            System.out.println("3 - Fugir");
            System.out.print("O que você faz? ");
            opcao = leitor.nextInt();

            int dado = 0;
            int dano = 0;

            switch (opcao) {
                case 1:
                    dado = rolarDado();
                    dano = heroi.ataque + dado - inimigo.defesa;
                    if (dano < 1) {
                        dano = 1;
                    }
                    inimigo.vida -= dano;
                    System.out.println("Você atacou! Dado: " + dado + " | Dano causado: " + dano);
                    if (inimigo.vida <= 0) {
                        inimigo.vida = 0;
                    }
                    break;

                case 2:
                    if (heroi.pocoes > 0) {
                        heroi.pocoes--;
                        heroi.vida += 20;
                        if (heroi.vida > heroi.vidaMaxima) {
                            heroi.vida = heroi.vidaMaxima;
                        }
                        System.out.println("Você tomou uma poção. Vida: " + heroi.vida
                                + "/" + heroi.vidaMaxima + " | Poções restantes: " + heroi.pocoes);
                    } else {
                        System.out.println("Sem poções! Você perdeu a vez.");
                    }
                    break;

                case 3:
                    System.out.println("Você fugiu da batalha!");
                    fugiu = true;
                    break;

                default:
                    System.out.println("Opção inválida! Você perdeu a vez.");
            } // fecha o switch das acoes

            // Contra-ataque do inimigo (se ainda estiver de pé e você não fugiu)
            if (!fugiu && inimigo.vida > 0 && heroi.vida > 0) {
                int danoInimigo = inimigo.ataque - heroi.defesa;
                if (danoInimigo < 1) {
                    danoInimigo = 1;
                }
                heroi.vida -= danoInimigo;
                System.out.println(inimigo.nome + " atacou e causou " + danoInimigo + " de dano.");
                if (heroi.vida <= 0) {
                    heroi.vida = 0;
                    System.out.println(heroi.nome + " caiu...");
                }
            }

        } while (inimigo.vida > 0 && heroi.vida > 0 && !fugiu); // fecha o do-while da batalha

        return inimigo.vida <= 0 && heroi.vida > 0;
    } // fecha o metodo batalhar
} // === FECHA A CLASSE Principal ===