package saude;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class KanbanPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private List<Paciente> pacientes;
    private Map<Paciente.Etapa, ColunaKanban> colunas;
    private Map<Integer, CardPaciente> cards;
    private int proximoId = 1;

    public KanbanPanel() {
        pacientes = GerenciadorArquivos.carregar();
        colunas   = new LinkedHashMap<>();
        cards     = new HashMap<>();

        setLayout(new GridLayout(1, Paciente.Etapa.values().length, 0, 0));
        setBackground(new Color(225, 235, 248));

        for (Paciente p : pacientes)
            if (p.getId() >= proximoId) proximoId = p.getId() + 1;

        for (Paciente.Etapa etapa : Paciente.Etapa.values()) {
            ColunaKanban coluna = new ColunaKanban(etapa, this);
            colunas.put(etapa, coluna);
            add(coluna);
        }

        for (Paciente p : pacientes) {
            CardPaciente card = new CardPaciente(p, this);
            cards.put(p.getId(), card);
            colunas.get(p.getEtapa()).adicionarCard(card);
        }
    }

    public void adicionarPaciente(String nome, String endereco, String telefone, String cpf) {
        Paciente p = new Paciente(proximoId++, nome, endereco, telefone, cpf);
        pacientes.add(p);
        CardPaciente card = new CardPaciente(p, this);
        cards.put(p.getId(), card);
        colunas.get(Paciente.Etapa.PACIENTE).adicionarCard(card);
        salvarDados();
    }

    public void moverPaciente(int id, Paciente.Etapa novaEtapa) {
        Paciente paciente = buscarPorId(id);
        if (paciente == null) return;

        CardPaciente card = cards.get(id);
        colunas.get(paciente.getEtapa()).removerCard(card);
        paciente.setEtapa(novaEtapa);

        if (novaEtapa == Paciente.Etapa.VISITA_REALIZADA) {
            solicitarRelato(paciente, card, novaEtapa);
        } else {
            colunas.get(novaEtapa).adicionarCard(card);
            card.atualizar();
            salvarDados();
        }
    }

    private void solicitarRelato(Paciente paciente, CardPaciente card, Paciente.Etapa novaEtapa) {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Relato da Visita Realizada", true);
        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel conteudo = new JPanel(new BorderLayout(8, 8));
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        conteudo.setBackground(new Color(248, 252, 255));

        JLabel titulo = new JLabel("✅ Visita realizada para: " + paciente.getNome());
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titulo.setForeground(new Color(50, 160, 90));

        JLabel instrucao = new JLabel("Adicione o relato da visita (opcional):");
        instrucao.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JTextArea txtRelato = new JTextArea(paciente.getRelato(), 6, 30);
        txtRelato.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtRelato.setLineWrap(true);
        txtRelato.setWrapStyleWord(true);
        txtRelato.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240)),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));

        JPanel topo = new JPanel(new GridLayout(2, 1, 4, 4));
        topo.setOpaque(false);
        topo.add(titulo);
        topo.add(instrucao);

        conteudo.add(topo, BorderLayout.NORTH);
        conteudo.add(new JScrollPane(txtRelato), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botoes.setBackground(new Color(248, 252, 255));

        JButton btnConfirmar = new JButton("Confirmar Visita");
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConfirmar.setBackground(new Color(50, 160, 90));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirmar.addActionListener(e -> {
            paciente.setRelato(txtRelato.getText().trim());
            colunas.get(novaEtapa).adicionarCard(card);
            card.atualizar();
            salvarDados();
            dialog.dispose();
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCancelar.addActionListener(e -> {
            paciente.setEtapa(Paciente.Etapa.PACIENTE);
            colunas.get(Paciente.Etapa.PACIENTE).adicionarCard(card);
            card.atualizar();
            dialog.dispose();
        });

        botoes.add(btnCancelar);
        botoes.add(btnConfirmar);

        dialog.add(conteudo, BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private Paciente buscarPorId(int id) {
        for (Paciente p : pacientes)
            if (p.getId() == id) return p;
        return null;
    }

    public List<Paciente> getPacientes() { return pacientes; }

    public void salvarDados() { GerenciadorArquivos.salvar(pacientes); }
}