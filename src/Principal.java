
import java.util.ArrayList;
import java.util.Scanner;


public class Principal {


    // ==========================================================
    // NUMEROS DO JOGO: mexa aqui e o jogo inteiro muda.
    // (substituem os "numeros magicos" espalhados pelo codigo)
    // ==========================================================
    static final int CURA_POCAO = 30;
    static final int CURA_POCAO_GRANDE = 50;
    static final int PRECO_POCAO = 10;
    static final int PRECO_POCAO_GRANDE = 20;
    static final int PRECO_TREINO = 25;
    static final int PONTOS_TREINO = 1;
    static final int MOEDAS_INICIAIS = 20;


    static final int XP_POR_NIVEL = 20;
    static final int VIDA_POR_NIVEL = 10;
    static final int ATAQUE_POR_NIVEL = 2;


    static final int ESPECIAL_A_CADA = 3;   // turnos entre ataques especiais
    static final int VIDA_PARA_CURAR = 40;  // % de vida em que o inimigo cura
    static final int VIDA_PARA_FUGIR = 40;  // % de vida em que o inimigo foge
    static final int DANO_VENENO = 3;
    static final int TURNOS_VENENO = 3;


    static final int BONUS_BAU = 15;
    static final int DANO_ARMADILHA = 8;
    static final int CURA_FONTE = 25;


    static final int PONTOS_POR_INIMIGO = 50;


    // ==========================================================
    // MAIN: so controla o "jogar de novo"
    // ==========================================================
    public static void main(String[] args) {


        Scanner leitor = new Scanner(System.in);
        boolean jogarDeNovo;


        do {
            jogarDeNovo = jogarPartida(leitor);


            if (jogarDeNovo) {
                System.out.println();
                System.out.println("==================================================");
                System.out.println("   Uma nova masmorra se abre...");
                System.out.println("==================================================");
            }


        } while (jogarDeNovo);


        System.out.println();
        System.out.println("Obrigado por jogar!");
        leitor.close();
    }


    // ==========================================================
    // UMA PARTIDA INTEIRA
    // ==========================================================
    public static boolean jogarPartida(Scanner leitor) {


        abertura();


        String nome = lerTexto(leitor, "Qual e o seu nome, aventureiro? ");
        int classe = escolherClasse(leitor);


        Personagem heroi = criarHeroi(nome, classe);
        mostrarFicha(heroi);


        ArrayList<Inimigo> inimigos = criarInimigos();
        ArrayList<String> derrotados = new ArrayList<>();


        for (int i = 0; i < inimigos.size(); i++) {
            Inimigo atual = inimigos.get(i);


            // a partir da segunda sala, o jogador escolhe o caminho
            if (i > 0 && escolherPortaElite(leitor, atual.nome)) {
                tornarElite(atual);
            }


            cabecalhoSala(i + 1, inimigos.size(), atual);


            boolean salaResolvida = batalhar(heroi, atual, leitor);


            if (!salaResolvida) {
                break;                                  // morreu ou fugiu
            }


            derrotados.add(atual.nome);
            int xpGanho = atual.fugiu ? atual.recompensa / 2 : atual.recompensa;
            heroi.xp += xpGanho;
            heroi.moedas += xpGanho;


            System.out.println();
            if (atual.fugiu) {
                System.out.println(atual.nome + " escapou! Voce ganha so " + xpGanho + " XP.");
            } else {
                System.out.println(atual.nome + " derrotado! +" + xpGanho
                        + " XP e +" + xpGanho + " moedas.");
            }


            subirDeNivel(heroi);


            if (i < inimigos.size() - 1 && heroi.vida > 0) {
                eventoDoCorredor(heroi);
                loja(heroi, leitor);
            }
        }


        mostrarResultadoFinal(heroi, inimigos.size(), derrotados);
        return perguntarJogarDeNovo(leitor);
    }


    // ==========================================================
    // CRIACAO DE OBJETOS (fabricas: uma linha por monstro/item)
    // ==========================================================
    public static Personagem criarHeroi(String nome, int classe) {


        Personagem p = new Personagem();
        p.nome = nome;
        p.nivel = 1;
        p.xp = 0;
        p.moedas = MOEDAS_INICIAIS;
        p.veneno = 0;
        p.mochila = new ArrayList<>();      // lista tambem precisa de new!


        int pocoes = 0;


        switch (classe) {
            case 1:
                p.classe = "Guerreiro";
                p.vidaMaxima = 100;
                p.ataque = 12;
                p.defesa = 5;
                pocoes = 2;
                break;
            case 2:
                p.classe = "Mago";
                p.vidaMaxima = 85;
                p.ataque = 16;
                p.defesa = 3;
                pocoes = 4;
                break;
            case 3:
                p.classe = "Arqueiro";
                p.vidaMaxima = 90;
                p.ataque = 15;
                p.defesa = 4;
                pocoes = 3;
                break;
            default:
                p.classe = "Aprendiz";
                p.vidaMaxima = 80;
                p.ataque = 10;
                p.defesa = 3;
                pocoes = 1;
                System.out.println("Opcao invalida... voce entra como Aprendiz!");
        }


        for (int i = 0; i < pocoes; i++) {
            p.mochila.add(criarItem("Pocao pequena", CURA_POCAO, PRECO_POCAO));
        }


        p.vida = p.vidaMaxima;
        return p;
    }


    public static Item criarItem(String nome, int cura, int preco) {
        Item item = new Item();
        item.nome = nome;
        item.cura = cura;
        item.preco = preco;
        return item;
    }


    public static Inimigo criarInimigo(String nome, int vida, int ataque, int defesa, int recompensa) {
        Inimigo i = new Inimigo();
        i.nome = nome;
        i.vida = vida;
        i.vidaMaxima = vida;
        i.ataque = ataque;
        i.defesa = defesa;
        i.recompensa = recompensa;
        return i;
    }


    public static ArrayList<Inimigo> criarInimigos() {


        ArrayList<Inimigo> lista = new ArrayList<>();


        Inimigo goblin = criarInimigo("Goblin", 25, 8, 2, 10);
        goblin.fugitivo = true;


        Inimigo esqueleto = criarInimigo("Esqueleto", 38, 10, 4, 15);
        esqueleto.cura = 14;


        Inimigo dragao = criarInimigo("Dragao", 66, 17, 6, 30);
        dragao.especial = 26;
        dragao.venenoso = true;
        dragao.aviso = "O Dragao enche o peito de ar...";


        lista.add(goblin);
        lista.add(esqueleto);
        lista.add(dragao);


        return lista;
    }


    // ==========================================================
    // BATALHA
    // ==========================================================
    public static boolean batalhar(Personagem heroi, Inimigo inimigo, Scanner leitor) {


        System.out.println();
        System.out.println(">>> " + inimigo.nome + "  (vida " + inimigo.vida
                + ", ataque " + inimigo.ataque + ", defesa " + inimigo.defesa + ")");


        int turno = 0;
        boolean heroiFugiu = false;


        do {
            turno++;


            if (heroi.veneno > 0) {
                aplicarVeneno(heroi);
            }


            if (heroi.vida > 0) {
                mostrarPlacar(heroi, inimigo, turno);


                int opcao = escolherAcao(leitor, heroi);


                if (opcao == 1) {
                    atacar(heroi, inimigo);
                } else if (opcao == 2) {
                    usarItem(heroi, leitor);
                } else if (opcao == 3) {
                    System.out.println("Voce fugiu da batalha!");
                    heroiFugiu = true;
                } else {
                    System.out.println("Opcao invalida! Voce perdeu a vez.");
                }
            }


            if (!heroiFugiu && inimigo.vida > 0 && heroi.vida > 0) {
                inimigo.fugiu = turnoDoInimigo(heroi, inimigo, turno);
            }


        } while (inimigo.vida > 0 && heroi.vida > 0 && !heroiFugiu && !inimigo.fugiu);


        if (heroi.vida <= 0) {
            System.out.println(heroi.nome + " caiu na masmorra...");
            return false;
        }
        if (heroiFugiu) {
            return false;
        }
        return true;                            // inimigo morreu ou fugiu
    }


    public static void atacar(Personagem heroi, Inimigo inimigo) {


        int dado = rolarDado();


        if (dado == 1) {
            System.out.println("Voce atacou... e o " + inimigo.nome + " DESVIOU! (dano 0)");
            return;
        }


        int dano = calcularDano(heroi.ataque, inimigo.defesa, dado);


        if (dado == 6) {
            dano = dano * 2;
            System.out.println("CRITICO! Dado 6 - dano dobrado!");
        }


        inimigo.vida -= dano;
        if (inimigo.vida < 0) {
            inimigo.vida = 0;
        }


        System.out.println("Voce atacou! Dado: " + dado + " | Dano: " + dano);
    }


    // Devolve true se o inimigo fugiu da batalha.
    public static boolean turnoDoInimigo(Personagem heroi, Inimigo inimigo, int turno) {


        // 1) inimigo medroso
        if (inimigo.fugitivo
                && vidaEmPorcento(inimigo.vida, inimigo.vidaMaxima) <= VIDA_PARA_FUGIR) {
            System.out.println("O " + inimigo.nome + " fugiu, com medo!");
            return true;
        }


        // 2) inimigo curandeiro (uma vez por batalha)
        if (inimigo.cura > 0 && !inimigo.jaCurou
                && vidaEmPorcento(inimigo.vida, inimigo.vidaMaxima) <= VIDA_PARA_CURAR) {
            inimigo.jaCurou = true;
            inimigo.vida += inimigo.cura;
            if (inimigo.vida > inimigo.vidaMaxima) {
                inimigo.vida = inimigo.vidaMaxima;
            }
            System.out.println("O " + inimigo.nome + " se cura! Vida: "
                    + inimigo.vida + "/" + inimigo.vidaMaxima);
            return false;
        }


        // 3) ataque especial de tempos em tempos
        if (inimigo.especial > 0 && turno % ESPECIAL_A_CADA == 0) {
            System.out.println("* " + inimigo.aviso);
            int dano = calcularDano(inimigo.especial, heroi.defesa, 0);
            heroi.vida -= dano;
            System.out.println("* ATAQUE ESPECIAL do " + inimigo.nome + "! Dano: " + dano);


            if (inimigo.venenoso) {
                heroi.veneno = TURNOS_VENENO;
                System.out.println("* Voce foi ENVENENADO (" + TURNOS_VENENO + " turnos).");
            }
        } else {
            int dano = calcularDano(inimigo.ataque, heroi.defesa, 0);
            heroi.vida -= dano;
            System.out.println(inimigo.nome + " atacou: " + dano + " de dano");
        }


        if (heroi.vida < 0) {
            heroi.vida = 0;
        }


        return false;
    }


    public static void aplicarVeneno(Personagem heroi) {
        heroi.vida -= DANO_VENENO;
        heroi.veneno--;
        System.out.println("Veneno: -" + DANO_VENENO + " de vida ("
                + heroi.veneno + " turnos restantes)");
        if (heroi.vida < 0) {
            heroi.vida = 0;
        }
    }


    public static void usarItem(Personagem heroi, Scanner leitor) {


        if (heroi.mochila.size() == 0) {
            System.out.println("Sua mochila esta vazia! Voce perdeu a vez.");
            return;
        }


        System.out.println("--- MOCHILA ---");
        for (int i = 0; i < heroi.mochila.size(); i++) {
            Item item = heroi.mochila.get(i);
            System.out.println((i + 1) + " - " + item.nome + " (cura " + item.cura + ")");
        }
        System.out.println("0 - Voltar (perde a vez)");
        System.out.print("Usar qual item? ");
        int escolha = lerNumero(leitor);


        if (escolha < 1 || escolha > heroi.mochila.size()) {
            System.out.println("Voce nao usou nada.");
            return;
        }


        Item usado = heroi.mochila.remove(escolha - 1);   // remove() devolve o item tirado
        heroi.vida += usado.cura;
        if (heroi.vida > heroi.vidaMaxima) {
            heroi.vida = heroi.vidaMaxima;
        }


        System.out.println("Voce usou " + usado.nome + ". Vida: "
                + heroi.vida + "/" + heroi.vidaMaxima);
    }


    // ==========================================================
    // ENTRE AS SALAS
    // ==========================================================
    public static void eventoDoCorredor(Personagem heroi) {


        int sorteio = rolarDado();              // reaproveita o dado de 1 a 6


        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("  CORREDOR ENTRE AS SALAS");
        System.out.println("--------------------------------------------------");


        switch (sorteio) {
            case 1:
            case 2:
                heroi.moedas += BONUS_BAU;
                System.out.println("Voce encontra um BAU: +" + BONUS_BAU + " moedas.");
                break;
            case 3:
            case 4:
                heroi.vida -= DANO_ARMADILHA;
                if (heroi.vida < 1) {
                    heroi.vida = 1;
                }
                System.out.println("ARMADILHA de flechas: -" + DANO_ARMADILHA + " de vida.");
                break;
            default:
                heroi.vida += CURA_FONTE;
                if (heroi.vida > heroi.vidaMaxima) {
                    heroi.vida = heroi.vidaMaxima;
                }
                System.out.println("Voce acha uma FONTE: +" + CURA_FONTE + " de vida.");
        }


        System.out.println("Vida: " + heroi.vida + "/" + heroi.vidaMaxima
                + "   Moedas: " + heroi.moedas);
    }


    public static void loja(Personagem heroi, Scanner leitor) {


        int opcao;


        do {
            System.out.println();
            System.out.println("--- LOJA  (voce tem " + heroi.moedas + " moedas) ---");
            System.out.println("1 - Pocao pequena (+" + CURA_POCAO + ") .... " + PRECO_POCAO);
            System.out.println("2 - Pocao grande (+" + CURA_POCAO_GRANDE + ") .... " + PRECO_POCAO_GRANDE);
            System.out.println("3 - Treinar ataque (+" + PONTOS_TREINO + ") ... " + PRECO_TREINO);
            System.out.println("4 - Treinar defesa (+" + PONTOS_TREINO + ") ... " + PRECO_TREINO);
            System.out.println("5 - Sair da loja");
            System.out.print("O que voce quer? ");
            opcao = lerNumero(leitor);


            if (opcao == 1) {
                if (pagar(heroi, PRECO_POCAO)) {
                    heroi.mochila.add(criarItem("Pocao pequena", CURA_POCAO, PRECO_POCAO));
                    System.out.println("Comprou uma pocao pequena.");
                }
            } else if (opcao == 2) {
                if (pagar(heroi, PRECO_POCAO_GRANDE)) {
                    heroi.mochila.add(criarItem("Pocao grande", CURA_POCAO_GRANDE, PRECO_POCAO_GRANDE));
                    System.out.println("Comprou uma pocao grande.");
                }
            } else if (opcao == 3) {
                if (pagar(heroi, PRECO_TREINO)) {
                    heroi.ataque += PONTOS_TREINO;
                    System.out.println("Ataque agora e " + heroi.ataque + ".");
                }
            } else if (opcao == 4) {
                if (pagar(heroi, PRECO_TREINO)) {
                    heroi.defesa += PONTOS_TREINO;
                    System.out.println("Defesa agora e " + heroi.defesa + ".");
                }
            } else if (opcao != 5) {
                System.out.println("Opcao invalida!");
            }


        } while (opcao != 5);
    }


    public static boolean pagar(Personagem heroi, int preco) {
        if (heroi.moedas < preco) {
            System.out.println("Moedas insuficientes!");
            return false;
        }
        heroi.moedas -= preco;
        return true;
    }


    public static boolean escolherPortaElite(Scanner leitor, String nomeInimigo) {
        System.out.println();
        System.out.println("Duas passagens levam adiante:");
        System.out.println("1 - Corredor estreito  -> " + nomeInimigo + " normal");
        System.out.println("2 - Porta de ferro     -> " + nomeInimigo + " ELITE (mais forte, recompensa dobrada)");
        System.out.print("Por onde voce vai? ");
        return lerNumero(leitor) == 2;
    }


    public static void tornarElite(Inimigo inimigo) {
        inimigo.nome = inimigo.nome + " ELITE";
        inimigo.vida = inimigo.vida + inimigo.vida * 25 / 100;
        inimigo.vidaMaxima = inimigo.vida;
        inimigo.ataque += 1;
        inimigo.especial += 3;
        inimigo.recompensa = inimigo.recompensa * 2;
        System.out.println("Voce sente um cheiro de ferro... o " + inimigo.nome
                + " esta mais forte!");
    }


    // ==========================================================
    // PROGRESSAO
    // ==========================================================
    public static void subirDeNivel(Personagem heroi) {
        while (heroi.xp >= heroi.nivel * XP_POR_NIVEL) {
            heroi.nivel++;
            heroi.vidaMaxima += VIDA_POR_NIVEL;
            heroi.ataque += ATAQUE_POR_NIVEL;
            heroi.vida += VIDA_POR_NIVEL;         // ganha o novo folego, nao cura tudo
            if (heroi.vida > heroi.vidaMaxima) {
                heroi.vida = heroi.vidaMaxima;
            }
            System.out.println("*** SUBIU PARA O NIVEL " + heroi.nivel + "! ***");
            System.out.println("Vida maxima: " + heroi.vidaMaxima
                    + "   Ataque: " + heroi.ataque);
        }
    }


    // ==========================================================
    // TELAS
    // ==========================================================
    public static void abertura() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("        A MASMORRA DOS TRES GUARDIOES");
        System.out.println("==================================================");
        System.out.println("Tres guardioes bloqueiam a saida. Derrote todos!");
        System.out.println();
    }


    public static int escolherClasse(Scanner leitor) {
        System.out.println();
        System.out.println("Escolha sua classe:");
        System.out.println("1 - Guerreiro (muita vida e defesa)");
        System.out.println("2 - Mago      (muito ataque, pouca vida)");
        System.out.println("3 - Arqueiro  (equilibrado)");
        System.out.print("Classe (1-3): ");
        return lerNumero(leitor);
    }


    public static void cabecalhoSala(int numero, int total, Inimigo inimigo) {
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println("  SALA " + numero + " DE " + total + "  -  " + inimigo.nome);
        System.out.println("--------------------------------------------------");
    }


    public static void mostrarFicha(Personagem p) {
        System.out.println();
        System.out.println("=== FICHA DO HEROI ===");
        System.out.println("Nome: " + p.nome + "  (" + p.classe + ")");
        System.out.println("Nivel: " + p.nivel);
        System.out.println("Vida: " + p.vida + "/" + p.vidaMaxima);
        System.out.println("Ataque: " + p.ataque + "   Defesa: " + p.defesa);
        System.out.println("Mochila: " + p.mochila.size() + " item(ns)   Moedas: " + p.moedas);
        System.out.println("======================");
    }


    public static void mostrarPlacar(Personagem heroi, Inimigo inimigo, int turno) {
        System.out.println();
        System.out.println("--- turno " + turno + " ---");
        System.out.println(heroi.nome + ": " + heroi.vida + "/" + heroi.vidaMaxima
                + "   |   " + inimigo.nome + ": " + inimigo.vida + "/" + inimigo.vidaMaxima);
        if (heroi.veneno > 0) {
            System.out.println("(envenenado: " + heroi.veneno + " turnos restantes)");
        }
    }


    public static int escolherAcao(Scanner leitor, Personagem heroi) {
        System.out.println("1 - Atacar");
        System.out.println("2 - Usar item da mochila (" + heroi.mochila.size() + " item(ns))");
        System.out.println("3 - Fugir");
        System.out.print("O que voce faz? ");
        return lerNumero(leitor);
    }


    public static void mostrarResultadoFinal(Personagem heroi, int totalInimigos,
                                             ArrayList<String> derrotados) {


        System.out.println();
        System.out.println("==================================================");


        if (heroi.vida <= 0) {
            System.out.println("   FIM DE JOGO - voce caiu na masmorra");
            System.out.println("==================================================");
            System.out.println("Inimigos enfrentados: " + derrotados.size() + " de " + totalInimigos);
        } else if (derrotados.size() == totalInimigos) {
            System.out.println("   VOCE VENCEU! A masmorra foi limpa!");
            System.out.println("==================================================");
        } else {
            System.out.println("   Voce saiu da masmorra e voltou para a vila.");
            System.out.println("==================================================");
            System.out.println("Inimigos enfrentados: " + derrotados.size() + " de " + totalInimigos);
        }


        if (derrotados.size() > 0) {
            System.out.print("Trofeus: ");
            for (int i = 0; i < derrotados.size(); i++) {
                System.out.print(derrotados.get(i));
                if (i < derrotados.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }


        int pontos = calcularPontuacao(heroi, derrotados.size());


        System.out.println();
        System.out.println("--- PONTUACAO ---");
        System.out.println("XP x 2 ............. " + (heroi.xp * 2));
        System.out.println("Vida restante ...... " + heroi.vida);
        System.out.println("Moedas ............. " + heroi.moedas);
        System.out.println("Inimigos x " + PONTOS_POR_INIMIGO + " ..... "
                + (derrotados.size() * PONTOS_POR_INIMIGO));
        System.out.println("----------------");
        System.out.println("TOTAL .............. " + pontos + "  (" + categoria(pontos) + ")");
    }


    // ==========================================================
    // CONTAS E UTILITARIOS
    // ==========================================================
    public static int calcularPontuacao(Personagem heroi, int inimigosDerrotados) {
        return heroi.xp * 2 + heroi.vida + heroi.moedas + inimigosDerrotados * PONTOS_POR_INIMIGO;
    }


    public static String categoria(int pontos) {
        if (pontos >= 300) {
            return "LENDARIO";
        }
        if (pontos >= 200) {
            return "VETERANO";
        }
        if (pontos >= 120) {
            return "APRENDIZ";
        }
        return "SOBREVIVENTE";
    }


    public static int rolarDado() {
        return (int) (Math.random() * 6) + 1;
    }


    // O calculo de dano existe em UM lugar so (antes estava repetido 3 vezes).
    public static int calcularDano(int ataque, int defesa, int bonus) {
        int dano = ataque + bonus - defesa;
        if (dano < 1) {
            dano = 1;
        }
        return dano;
    }


    // Serve para o heroi e para o inimigo.
    public static int vidaEmPorcento(int vida, int vidaMaxima) {
        if (vidaMaxima <= 0) {
            return 0;
        }
        return vida * 100 / vidaMaxima;
    }


    public static boolean perguntarJogarDeNovo(Scanner leitor) {
        System.out.println();
        System.out.print("Jogar de novo? (1 = sim / 2 = nao): ");
        return lerNumero(leitor) == 1;
    }


    // Le um numero sem quebrar se a pessoa digitar letra ou se a entrada acabar.
    public static int lerNumero(Scanner leitor) {
        while (true) {
            if (!leitor.hasNext()) {
                encerrar();                     // fim da entrada (Ctrl+D) ou piped
            }
            if (leitor.hasNextInt()) {
                int numero = leitor.nextInt();
                leitor.nextLine();
                return numero;
            }
            System.out.print("Digite um numero: ");
            leitor.next();
        }
    }


    // Le um texto nao vazio.
    public static String lerTexto(Scanner leitor, String pergunta) {
        System.out.print(pergunta);
        String texto = lerLinha(leitor);
        while (texto.length() == 0) {
            System.out.print("Digite alguma coisa: ");
            texto = lerLinha(leitor);
        }
        return texto;
    }


    // nextLine que nao estoura quando a entrada termina.
    public static String lerLinha(Scanner leitor) {
        if (!leitor.hasNextLine()) {
            encerrar();
        }
        return leitor.nextLine().trim();
    }


    // Sai do jogo com uma mensagem amigavel (sem stack trace).
    public static void encerrar() {
        System.out.println();
        System.out.println("Ate a proxima aventura!");
        System.exit(0);
    }
}