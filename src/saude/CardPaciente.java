package saude;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;

public class CardPaciente extends JPanel implements DragGestureListener, DragSourceListener {
    private static final long serialVersionUID = 1L;
    private Paciente paciente;
    private KanbanPanel kanbanPanel;
    private DragSource dragSource;

    private static final Color COR_CARD   = new Color(255, 255, 255);
    private static final Color COR_BORDA  = new Color(200, 220, 240);
    private static final Color COR_HOVER  = new Color(235, 245, 255);
    private static final Color COR_NOME   = new Color(30, 60, 100);
    private static final Color COR_INFO   = new Color(90, 110, 140);

    public CardPaciente(Paciente paciente, KanbanPanel kanbanPanel) {
        this.paciente = paciente;
        this.kanbanPanel = kanbanPanel;

        setLayout(new BorderLayout(4, 4));
        setBackground(COR_CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        construirConteudo();
        configurarDrag();
        configurarHover();
        configurarClique();
    }

    private void construirConteudo() {
        removeAll();

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);

        JLabel lblNome = new JLabel("👤 " + paciente.getNome());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblNome.setForeground(COR_NOME);

        JLabel lblId = new JLabel("#" + paciente.getId());
        lblId.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblId.setForeground(COR_INFO);

        topo.add(lblNome, BorderLayout.CENTER);
        topo.add(lblId, BorderLayout.EAST);

        JPanel info = new JPanel(new GridLayout(2, 1, 0, 1));
        info.setOpaque(false);

        JLabel lblEnd = new JLabel("📍 " + paciente.getEndereco());
        lblEnd.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEnd.setForeground(COR_INFO);

        JLabel lblTel = new JLabel("📞 " + paciente.getTelefone());
        lblTel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTel.setForeground(COR_INFO);

        info.add(lblEnd);
        info.add(lblTel);

        add(topo, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);

        if (paciente.getEtapa() == Paciente.Etapa.VISITA_REALIZADA && !paciente.getRelato().isEmpty()) {
            JLabel lblRelato = new JLabel("📝 Relato registrado");
            lblRelato.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            lblRelato.setForeground(new Color(60, 160, 90));
            add(lblRelato, BorderLayout.SOUTH);
        }

        revalidate();
        repaint();
    }

    private void configurarDrag() {
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
    }

    private void configurarHover() {
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { setBackground(COR_HOVER); repaint(); }
            @Override public void mouseExited(MouseEvent e)  { setBackground(COR_CARD);  repaint(); }
        });
    }

    private void configurarClique() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) abrirDetalhes();
            }
        });
    }

    private void abrirDetalhes() {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Detalhes do Paciente", true);
        dialog.setSize(420, paciente.getEtapa() == Paciente.Etapa.VISITA_REALIZADA ? 420 : 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel conteudo = new JPanel(new GridBagLayout());
        conteudo.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        conteudo.setBackground(new Color(248, 252, 255));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 12);

        String[][] campos = {
                {"Nome:",     paciente.getNome()},
                {"CPF:",      paciente.getCpf()},
                {"Endereço:", paciente.getEndereco()},
                {"Telefone:", paciente.getTelefone()},
                {"Etapa:",    paciente.getEtapa().getLabel()}
        };

        for (int i = 0; i < campos.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0.3;
            JLabel lbl = new JLabel(campos[i][0]);
            lbl.setFont(labelFont);
            lbl.setForeground(COR_NOME);
            conteudo.add(lbl, g);
            g.gridx = 1; g.weightx = 0.7;
            JLabel val = new JLabel(campos[i][1]);
            val.setFont(valueFont);
            conteudo.add(val, g);
        }

        JTextArea txtRelato = null;
        if (paciente.getEtapa() == Paciente.Etapa.VISITA_REALIZADA) {
            g.gridx = 0; g.gridy = campos.length; g.weightx = 0.3;
            JLabel lblRelato = new JLabel("Relato:");
            lblRelato.setFont(labelFont);
            lblRelato.setForeground(COR_NOME);
            conteudo.add(lblRelato, g);

            g.gridx = 0; g.gridy = campos.length + 1;
            g.gridwidth = 2; g.weightx = 1.0;
            g.weighty = 1.0; g.fill = GridBagConstraints.BOTH;
            txtRelato = new JTextArea(paciente.getRelato(), 5, 30);
            txtRelato.setFont(valueFont);
            txtRelato.setLineWrap(true);
            txtRelato.setWrapStyleWord(true);
            txtRelato.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COR_BORDA),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
            ));
            conteudo.add(new JScrollPane(txtRelato), g);
        }

        final JTextArea relatoFinal = txtRelato;
        dialog.add(conteudo, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botoes.setBackground(new Color(248, 252, 255));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.setBackground(new Color(40, 130, 200));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSalvar.addActionListener(e -> {
            if (relatoFinal != null) {
                paciente.setRelato(relatoFinal.getText());
                construirConteudo();
                kanbanPanel.salvarDados();
            }
            dialog.dispose();
        });

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnFechar.addActionListener(e -> dialog.dispose());

        botoes.add(btnFechar);
        botoes.add(btnSalvar);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public Paciente getPaciente() { return paciente; }
    public void atualizar() { construirConteudo(); }

    @Override
    public void dragGestureRecognized(DragGestureEvent dge) {
        Transferable t = new StringSelection(String.valueOf(paciente.getId()));
        dragSource.startDrag(dge, DragSource.DefaultMoveDrop, t, this);
    }

    @Override public void dragEnter(DragSourceDragEvent e) {}
    @Override public void dragOver(DragSourceDragEvent e) {}
    @Override public void dropActionChanged(DragSourceDragEvent e) {}
    @Override public void dragExit(DragSourceEvent e) {}
    @Override public void dragDropEnd(DragSourceDropEvent e) {}
}

