package saude;
C:\Users\Pontosat\IdeaProjects\SistemaAgenteDeSaude
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JanelaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private KanbanPanel kanbanPanel;

    public JanelaPrincipal() {
        setTitle("Sistema de Visitas — Agente Comunitário de Saúde");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setMinimumSize(new Dimension(900, 550));
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setLayout(new BorderLayout());
        add(criarCabecalho(), BorderLayout.NORTH);

        kanbanPanel = new KanbanPanel();
        JScrollPane scrollKanban = new JScrollPane(kanbanPanel);
        scrollKanban.setBorder(BorderFactory.createEmptyBorder());
        scrollKanban.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollKanban.getHorizontalScrollBar().setUnitIncrement(20);
        add(scrollKanban, BorderLayout.CENTER);

        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel cab = new JPanel(new BorderLayout());
        cab.setBackground(new Color(25, 65, 125));
        cab.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        esquerda.setOpaque(false);

        JLabel lblIco = new JLabel("🏥");
        lblIco.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        JLabel lblTitulo = new JLabel("Sistema de Visitas Domiciliares");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        JLabel lblSub = new JLabel("Agente Comunitário de Saúde");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(180, 200, 230));
        textos.add(lblTitulo);
        textos.add(lblSub);

        esquerda.add(lblIco);
        esquerda.add(textos);

        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        direita.setOpaque(false);

        JButton btnAdicionar = criarBotao("+ Novo Paciente", new Color(52, 190, 100), Color.WHITE);
        btnAdicionar.addActionListener(e -> abrirDialogNovoPaciente());

        JButton btnSalvar = criarBotao("💾 Salvar", new Color(60, 100, 180), Color.WHITE);
        btnSalvar.addActionListener(e -> {
            kanbanPanel.salvarDados();
            mostrarToast("Dados salvos com sucesso!");
        });

        direita.add(btnSalvar);
        direita.add(btnAdicionar);

        cab.add(esquerda, BorderLayout.WEST);
        cab.add(direita, BorderLayout.EAST);
        return cab;
    }

    private JButton criarBotao(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private JPanel criarRodape() {
        JPanel rod = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rod.setBackground(new Color(230, 240, 252));
        rod.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 215, 235)));
        JLabel lbl = new JLabel("💡 Dica: arraste os cartões entre as colunas para atualizar o status. Duplo clique para ver detalhes.");
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lbl.setForeground(new Color(80, 110, 160));
        rod.add(lbl);
        return rod;
    }

    private void abrirDialogNovoPaciente() {
        JDialog dialog = new JDialog(this, "Cadastrar Novo Paciente", true);
        dialog.setSize(400, 310);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        form.setBackground(new Color(248, 252, 255));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Nome completo:", "CPF:", "Endereço:", "Telefone:"};
        JTextField[] campos = new JTextField[4];

        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0.35;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(new Color(30, 60, 100));
            form.add(lbl, g);
            g.gridx = 1; g.weightx = 0.65;
            campos[i] = new JTextField();
            campos[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            campos[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(190, 215, 240)),
                    BorderFactory.createEmptyBorder(5, 7, 5, 7)
            ));
            form.add(campos[i], g);
        }

        dialog.add(form, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoes.setBackground(new Color(248, 252, 255));

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCadastrar.setBackground(new Color(40, 130, 200));
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setBorderPainted(false);
        btnCadastrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCadastrar.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnCadastrar.addActionListener(e -> {
            String nome = campos[0].getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "O nome é obrigatório!", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            kanbanPanel.adicionarPaciente(nome, campos[2].getText().trim(),
                    campos[3].getText().trim(), campos[1].getText().trim());
            mostrarToast("Paciente '" + nome + "' cadastrado!");
            dialog.dispose();
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCancelar.addActionListener(e -> dialog.dispose());

        botoes.add(btnCancelar);
        botoes.add(btnCadastrar);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.getRootPane().setDefaultButton(btnCadastrar);
        dialog.setVisible(true);
    }

    private void mostrarToast(String mensagem) {
        JWindow toast = new JWindow(this);
        JLabel label = new JLabel("  " + mensagem + "  ");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(40, 40, 40));
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        toast.add(label);
        toast.pack();
        toast.setLocation(getX() + (getWidth() - toast.getWidth()) / 2, getY() + getHeight() - 80);
        toast.setVisible(true);
        Timer t = new Timer(2000, e -> toast.dispose());
        t.setRepeats(false);
        t.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JanelaPrincipal().setVisible(true));
    }
}
