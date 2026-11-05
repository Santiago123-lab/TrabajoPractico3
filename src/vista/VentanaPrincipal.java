package vista;

import presentador.IGrafoPresenter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class VentanaPrincipal extends JFrame implements IGrafoVista {

    private static final long serialVersionUID = 1L;

    // Inputs de vértice 
    private JTextField txtNombreVertice;

    // Inputs de arista 
    private JComboBox<String> comboOrigen;
    private JComboBox<String> comboDestino;

    // Botones 
    private JButton btnCargarJSON;
    private JButton btnGuardarJSON;
    private JButton btnAgregarVertice;
    private JButton btnAgregarArista;
    private JButton btnEjecutar;
    private JButton btnLimpiar;

    // Tablas de estado 
    private DefaultTableModel modeloVertices;
    private DefaultTableModel modeloAristas;

    // Panel de visualización
    private PanelGrafo panelGrafo;

    // Resultado y estado
    private JTextArea areaResultado;
    private JLabel    lblEstado;

    public VentanaPrincipal() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }

    // IGrafoVista: wiring 

    @Override
    public void mostrar() {
        setVisible(true);
    }

    @Override
    public void setPresenter(IGrafoPresenter presenter) {
        btnAgregarVertice.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                presenter.onAgregarVertice();
            }
        });
        btnAgregarArista.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                presenter.onAgregarArista();
            }
        });
        btnEjecutar.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                presenter.onEjecutarAlgoritmo();
            }
        });
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                presenter.onLimpiar();
            }
        });
        btnCargarJSON.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                presenter.onCargarJSON();
            }
        });

        btnGuardarJSON.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                presenter.onGuardarJSON();
            }
        });
    }

    // IGrafoVista: display 

    @Override
    public void mostrarGrafo(List<String> vertices, List<String[]> aristas) {
        // Actualizar combos de selección
        comboOrigen.removeAllItems();
        comboDestino.removeAllItems();
        for (String v : vertices) {
            comboOrigen.addItem(v);
            comboDestino.addItem(v);
        }
        // Actualizar tabla de vértices
        modeloVertices.setRowCount(0);
        for (String v : vertices) {
            modeloVertices.addRow(new Object[]{v});
        }
        // Actualizar tabla de aristas
        modeloAristas.setRowCount(0);
        for (String[] a : aristas) {
            modeloAristas.addRow(new Object[]{a[0], a[1]});
        }
        // Actualizar visualización gráfica
        panelGrafo.actualizarGrafo(vertices, aristas);
    }

    @Override
    public void resaltarDominante(List<String> dominante) {
        panelGrafo.resaltarDominante(new HashSet<>(dominante));
    }

    @Override
    public void mostrarResultado(String texto) {
        areaResultado.setText(texto);
    }

    @Override
    public void mostrarEstado(String mensaje, boolean esError) {
        lblEstado.setText(mensaje);
        lblEstado.setForeground(esError ? Color.RED : new Color(0, 130, 0));
    }

    @Override
    public void limpiarEstado() {
        lblEstado.setText(" ");
        lblEstado.setForeground(Color.BLACK);
    }

    // IGrafoVista: entrada de datos 

    @Override
    public String pedirNombreVertice() {
        return txtNombreVertice.getText().trim();
    }

    @Override
    public String[] pedirArista(List<String> verticesDisponibles) {
        String origen  = (String) comboOrigen.getSelectedItem();
        String destino = (String) comboDestino.getSelectedItem();
        if (origen == null || destino == null) return null;
        return new String[]{origen, destino};
    }

    @Override
    public boolean confirmarLimpiar() {
        return JOptionPane.showConfirmDialog(
                this,
                "¿Desea limpiar el grafo completamente?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        ) == JOptionPane.YES_OPTION;
    }

    /** El presenter lo llama para limpiar el campo después de agregar. */
    public void limpiarCampoVertice() {
        txtNombreVertice.setText("");
    }

    // Construcción de la UI 

    private void initialize() {
        setTitle("Conjunto Dominante Minimo - TP3");
        setBounds(100, 100, 1200, 710);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        // Panel: Gestion de Vertices 
        JPanel panelVertices = new JPanel();
        panelVertices.setBorder(new TitledBorder(
                new EtchedBorder(), "Gestion de Vertices"));
        panelVertices.setBounds(10, 35, 320, 170);
        panelVertices.setLayout(null);
        getContentPane().add(panelVertices);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 28, 60, 20);
        panelVertices.add(lblNombre);

        txtNombreVertice = new JTextField();
        txtNombreVertice.setBounds(72, 26, 110, 22);
        panelVertices.add(txtNombreVertice);

        btnAgregarVertice = new JButton("Agregar Vertice");
        btnAgregarVertice.setToolTipText("Agrega el vertice al grafo");
        btnAgregarVertice.setBounds(190, 25, 118, 24);
        panelVertices.add(btnAgregarVertice);

        modeloVertices = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        modeloVertices.addColumn("Vertices Ingresados");

        JTable tablaVertices = new JTable(modeloVertices);
        JScrollPane scrollVertices = new JScrollPane(tablaVertices);
        scrollVertices.setBounds(10, 56, 298, 95);
        panelVertices.add(scrollVertices);

        // Panel: Agregar Arista 
        JPanel panelAristas = new JPanel();
        panelAristas.setBorder(new TitledBorder(
                new EtchedBorder(), "Agregar Arista"));
        panelAristas.setBounds(10, 215, 320, 170);
        panelAristas.setLayout(null);
        getContentPane().add(panelAristas);

        JLabel lblOrigen = new JLabel("Origen:");
        lblOrigen.setBounds(10, 28, 55, 20);
        panelAristas.add(lblOrigen);

        comboOrigen = new JComboBox<>();
        comboOrigen.setBounds(68, 26, 110, 22);
        panelAristas.add(comboOrigen);

        JLabel lblDestino = new JLabel("Destino:");
        lblDestino.setBounds(10, 58, 55, 20);
        panelAristas.add(lblDestino);

        comboDestino = new JComboBox<>();
        comboDestino.setBounds(68, 56, 110, 22);
        panelAristas.add(comboDestino);

        btnAgregarArista = new JButton("Agregar Arista");
        btnAgregarArista.setToolTipText("Conecta los dos vertices seleccionados");
        btnAgregarArista.setBounds(188, 40, 118, 24);
        panelAristas.add(btnAgregarArista);

        modeloAristas = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        modeloAristas.addColumn("Origen");
        modeloAristas.addColumn("Destino");

        JTable tablaAristas = new JTable(modeloAristas);
        JScrollPane scrollAristas = new JScrollPane(tablaAristas);
        scrollAristas.setBounds(10, 88, 298, 70);
        panelAristas.add(scrollAristas);


        // ── Barra de Herramientas Superior 
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); 
        toolBar.setBounds(173, 6, 155, 21); 
        getContentPane().add(toolBar);

        btnCargarJSON = new JButton("Cargar JSON");
        btnCargarJSON.setBackground(new Color(240, 240, 240));
        btnCargarJSON.setToolTipText("Carga un grafo desde un archivo JSON"); 
        toolBar.add(btnCargarJSON);

        toolBar.addSeparator(new Dimension(10, 0)); 

        btnGuardarJSON = new JButton("Guardar JSON");
        btnGuardarJSON.setBackground(new Color(240, 240, 240));
        btnGuardarJSON.setToolTipText("Guarda el grafo actual en un archivo JSON"); 
        toolBar.add(btnGuardarJSON);
        
        // Panel: Algoritmo 

        JPanel panelAlgoritmo = new JPanel();
        panelAlgoritmo.setBorder(new TitledBorder(
                new EtchedBorder(), "Algoritmo Goloso"));
        panelAlgoritmo.setBounds(10, 395, 320, 60);
        panelAlgoritmo.setLayout(null);
        getContentPane().add(panelAlgoritmo);

        btnEjecutar = new JButton("Ejecutar Algoritmo");
        btnEjecutar.setToolTipText("Calcula el conjunto dominante minimo");
        btnEjecutar.setBounds(10, 22, 168, 24);
        panelAlgoritmo.add(btnEjecutar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setForeground(Color.RED);
        btnLimpiar.setToolTipText("Borra el grafo y reinicia la aplicacion");
        btnLimpiar.setBounds(186, 22, 120, 24);
        panelAlgoritmo.add(btnLimpiar);

        // Panel: Resultado 
        JPanel panelResultado = new JPanel();
        panelResultado.setBorder(new TitledBorder(
                new EtchedBorder(), "Resultado del Algoritmo"));
        panelResultado.setBounds(10, 465, 320, 196);
        panelResultado.setLayout(null);
        getContentPane().add(panelResultado);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollResultado = new JScrollPane(areaResultado);
        scrollResultado.setBounds(10, 20, 298, 165);
        panelResultado.add(scrollResultado);

        lblEstado = new JLabel(" ");
        lblEstado.setFont(new Font("Dialog", Font.ITALIC, 11));
        lblEstado.setBounds(10, 190, 298, 20);
        panelResultado.add(lblEstado);

        // Panel: Visualizacion del Grafo 
        JPanel panelVisualizacion = new JPanel();
        panelVisualizacion.setBorder(new TitledBorder(
                new EtchedBorder(), "Visualizacion del Grafo"));
        panelVisualizacion.setBounds(340, 10, 840, 655);
        panelVisualizacion.setLayout(new BorderLayout(0, 0));
        getContentPane().add(panelVisualizacion);

        panelGrafo = new PanelGrafo();
        panelVisualizacion.add(panelGrafo, BorderLayout.CENTER);

        // Leyenda
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        leyenda.setBorder(new EtchedBorder());

        JLabel iconoNormal = new JLabel("  ");
        iconoNormal.setOpaque(true);
        iconoNormal.setBackground(new Color(173, 216, 230));
        iconoNormal.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        leyenda.add(iconoNormal);
        leyenda.add(new JLabel("Vertice normal"));

        JLabel iconoDom = new JLabel("  ");
        iconoDom.setOpaque(true);
        iconoDom.setBackground(new Color(255, 200, 80));
        iconoDom.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        leyenda.add(iconoDom);
        leyenda.add(new JLabel("Vertice en conjunto dominante"));

        panelVisualizacion.add(leyenda, BorderLayout.SOUTH);
    }

	@Override
	public String pedirRutaCargarJSON() 
	{
		JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo de Grafo JSON");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON", "json"));
        
        int seleccion = chooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
	}

	@Override
	public String pedirRutaGuardarJSON() 
	{
		JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar Grafo como JSON");
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON", "json"));
        
        int seleccion = chooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            if (!ruta.toLowerCase().endsWith(".json")) {
                ruta += ".json";
            }
            return ruta;
        }
        return null;
	}
	
}
