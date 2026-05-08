package saude;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

public class ColunaKanban extends JPanel implements DropTargetListener {
    private static final long serialVersionUID = 1L;

    private Paciente.Etapa etapa;
    private KanbanPanel kanbanPanel;
    private JPanel areaCards;
    private JLabel lblContador;

    private static final Color[] CORES_CABECALHO = {
            new Color(52, 120, 210),
            new Color(230, 140, 30),
            new Color(200, 90, 30),
            new Color(50, 160, 90),
            new Color(190, 50, 50)
    };

    private static final String[] ICONES = {"🏠", "🚶", "🚶", "✅", "❌"};

    public ColunaKanban(Paciente.Etapa etapa, KanbanPanel kanbanPanel) {
        this.etapa = etapa;
        this.kanbanPanel = kanbanPanel;

        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(230, Integer.MAX_VALUE));
        setBackground(new Color(240, 246, 255));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 225, 245), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        construirCabecalho();
        construirAreaCards();

        new DropTarget(this, this);
        new DropTarget(areaCards, this);
    }

    private void construirCabecalho() {
        int idx = etapa.ordinal();
        Color cor = CORES_CABECALHO[idx];

        JPanel cab = new JPanel(new BorderLayout(6, 0));
        cab.setBackground(cor);
        cab.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel lblTitulo = new JLabel(ICONES[idx] + "  " + etapa.getLabel());
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitulo.setForeground(Color.WHITE);

        lblContador = new JLabel("0");
        lblContador.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblContador.setForeground(new Color(255, 255, 255, 200));
        lblContador.setHorizontalAlignment(SwingConstants.RIGHT);

        cab.add(lblTitulo, BorderLayout.CENTER);
        cab.add(lblContador, BorderLayout.EAST);
        add(cab, BorderLayout.NORTH);
    }

    private void construirAreaCards() {
        areaCards = new JPanel();
        areaCards.setLayout(new BoxLayout(areaCards, BoxLayout.Y_AXIS));
        areaCards.setBackground(new Color(240, 246, 255));
        areaCards.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        JScrollPane scroll = new JScrollPane(areaCards);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(new Color(240, 246, 255));
        add(scroll, BorderLayout.CENTER);
    }

    public void adicionarCard(CardPaciente card) {
        areaCards.add(card);
        areaCards.add(Box.createVerticalStrut(8));
        atualizarContador();
        revalidate();
        repaint();
    }

    public void removerCard(CardPaciente card) {
        Component[] comps = areaCards.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] == card) {
                areaCards.remove(i);
                if (i < areaCards.getComponentCount()) {
                    Component prox = areaCards.getComponent(i);
                    if (prox instanceof Box.Filler) areaCards.remove(i);
                }
                break;
            }
        }
        atualizarContador();
        revalidate();
        repaint();
    }

    private void atualizarContador() {
        int count = 0;
        for (Component c : areaCards.getComponents()) {
            if (c instanceof CardPaciente) count++;
        }
        lblContador.setText(count + " paciente" + (count != 1 ? "s" : ""));
    }

    public Paciente.Etapa getEtapa() { return etapa; }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        setBackground(new Color(240, 246, 255));
        areaCards.setBackground(new Color(240, 246, 255));
        try {
            dtde.acceptDrop(DnDConstants.ACTION_MOVE);
            String idStr = (String) dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);
            int id = Integer.parseInt(idStr);
            kanbanPanel.moverPaciente(id, etapa);
            dtde.dropComplete(true);
        } catch (Exception ex) {
            dtde.dropComplete(false);
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        setBackground(new Color(220, 235, 255));
        areaCards.setBackground(new Color(220, 235, 255));
        repaint();
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        setBackground(new Color(240, 246, 255));
        areaCards.setBackground(new Color(240, 246, 255));
        repaint();
    }

    @Override public void dragOver(DropTargetDragEvent dtde) {}
    @Override public void dropActionChanged(DropTargetDragEvent dtde) {}
}