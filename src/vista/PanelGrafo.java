package vista;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Panel de visualización del grafo.
 * Estilo plano: sin degradados ni sombras.
 * No importa ninguna clase del paquete modelo.
 */
public class PanelGrafo extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int RADIO = 18;

    private static final Color COLOR_FONDO        = Color.WHITE;
    private static final Color COLOR_ARISTA        = new Color(100, 100, 100);
    private static final Color COLOR_VERTICE_NORMAL = new Color(173, 216, 230); // azul claro
    private static final Color COLOR_VERTICE_DOM    = new Color(255, 200, 80);  // amarillo
    private static final Color COLOR_BORDE          = new Color(50, 50, 50);
    private static final Color COLOR_TEXTO          = Color.BLACK;
    private static final Color COLOR_VACIO          = new Color(150, 150, 150);

    // Datos del grafo (solo strings, sin modelo.*)
    private List<String>   vertices  = new ArrayList<>();
    private List<String[]> aristas   = new ArrayList<>();
    private Set<String>    dominante = new HashSet<>();
    private Map<String, Point> posiciones = new LinkedHashMap<>();

    public PanelGrafo() {
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    }

    // ── API pública ────────────────────────────────────────────────────────

    public void actualizarGrafo(List<String> vertices, List<String[]> aristas) {
        this.vertices  = new ArrayList<>(vertices);
        this.aristas   = new ArrayList<>(aristas);
        this.dominante = new HashSet<>();
        calcularPosiciones();
        repaint();
    }

    public void resaltarDominante(Set<String> dominante) {
        this.dominante = dominante == null ? new HashSet<>() : new HashSet<>(dominante);
        repaint();
    }

    // ── Layout circular ────────────────────────────────────────────────────

    private void calcularPosiciones() {
        posiciones.clear();
        int n = vertices.size();
        if (n == 0) return;

        int cx = Math.max(getWidth()  / 2, 200);
        int cy = Math.max(getHeight() / 2, 200);
        int r  = Math.min(cx, cy) - RADIO - 30;
        if (r < 40) r = 40;

        for (int i = 0; i < n; i++) {
            double angulo = 2 * Math.PI * i / n - Math.PI / 2;
            posiciones.put(
                vertices.get(i),
                new Point((int)(cx + r * Math.cos(angulo)),
                          (int)(cy + r * Math.sin(angulo)))
            );
        }
    }

    // ── Painting ───────────────────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (vertices.isEmpty()) {
            g2.setColor(COLOR_VACIO);
            g2.setFont(new Font("Dialog", Font.ITALIC, 13));
            String msg = "Agregue vertices para visualizar el grafo";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
            return;
        }

        if (posiciones.size() != vertices.size()) calcularPosiciones();

        dibujarAristas(g2);
        dibujarVertices(g2);
    }

    private void dibujarAristas(Graphics2D g2) {
        g2.setColor(COLOR_ARISTA);
        g2.setStroke(new BasicStroke(1.5f));

        for (String[] arista : aristas) {
            Point p1 = posiciones.get(arista[0]);
            Point p2 = posiciones.get(arista[1]);
            if (p1 == null || p2 == null) continue;
            g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private void dibujarVertices(Graphics2D g2) {
        g2.setFont(new Font("Dialog", Font.BOLD, 11));

        for (String nombre : vertices) {
            Point p = posiciones.get(nombre);
            if (p == null) continue;

            boolean esDom = dominante.contains(nombre);

            // Relleno plano
            g2.setColor(esDom ? COLOR_VERTICE_DOM : COLOR_VERTICE_NORMAL);
            g2.fillOval(p.x - RADIO, p.y - RADIO, RADIO * 2, RADIO * 2);

            // Borde
            g2.setColor(COLOR_BORDE);
            g2.setStroke(new BasicStroke(esDom ? 2f : 1f));
            g2.drawOval(p.x - RADIO, p.y - RADIO, RADIO * 2, RADIO * 2);

            // Etiqueta
            g2.setColor(COLOR_TEXTO);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(nombre,
                p.x - fm.stringWidth(nombre) / 2,
                p.y + fm.getAscent() / 2 - 1);
        }
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        calcularPosiciones();
    }
}
