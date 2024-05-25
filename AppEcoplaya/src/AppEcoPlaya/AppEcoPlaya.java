package AppEcoPlaya;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class AppEcoPlaya {

    // Variables estáticas para almacenar datos
    private static Map<String, Usuario> usuariosRegistrados = new HashMap<>();
    private static Usuario usuarioActual;
    private static ArrayList<RegistroResiduo> reportesPendientes = new ArrayList<>();
    private static ArrayList<RegistroResiduo> reportesAtendidos = new ArrayList<>();
    private static ArrayList<EventoLimpieza> eventosLimpieza = new ArrayList<>(); // Lista de eventos de limpieza
    private static ArrayList<String> problemasReportados = new ArrayList<>(); // Lista de problemas reportados

    // Método principal
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppEcoPlaya::Login);
    }

    // Pantalla de inicio de sesión
    private static void Login() {

        // Configuración del frame
        JFrame frame = new JFrame("EcoPlaya - Iniciar Sesión");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        // Componentes del panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(173, 216, 230)); //Azul claro
        frame.getContentPane().add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);

        //Acciones de los botones
        JLabel title = new JLabel("Iniciar Sesión");
        title.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente más grande y en negrita
        panel.add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel usuarioLabel = new JLabel("Usuario:");
        panel.add(usuarioLabel, gbc);

        gbc.gridx++;
        JTextField usuarioField = new JTextField(20);
        panel.add(usuarioField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel contraseñaLabel = new JLabel("Contraseña:");
        panel.add(contraseñaLabel, gbc);

        gbc.gridx++;
        JPasswordField contraseñaField = new JPasswordField(20);
        panel.add(contraseñaField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton iniciarSesionButton = new JButton("Iniciar Sesión");
        panel.add(iniciarSesionButton, gbc);

        gbc.gridy++;
        JButton registrarUsuarioButton = new JButton("Registrar Nuevo Usuario");
        panel.add(registrarUsuarioButton, gbc);

        // Acción para iniciar sesión
        iniciarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String contraseña = new String(contraseñaField.getPassword());
                if (usuariosRegistrados.containsKey(usuario)) {
                    Usuario usuarioRegistrado = usuariosRegistrados.get(usuario);
                    if (usuarioRegistrado.getContraseña().equals(contraseña)) {
                        usuarioActual = usuarioRegistrado;
                        frame.dispose();
                        RolUsuarios();
                    } else {
                        JOptionPane.showMessageDialog(null, "Contraseña incorrecta.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario no registrado.", "Error de Autenticación", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Acción para registrar nuevo usuario
        registrarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserRegistration();
            }
        });

        frame.setVisible(true);
    }

    // Registro de nuevo usuario
    private static void UserRegistration() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Registrar Nuevo Usuario");
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBackground(new Color(173, 216, 230)); //Azul claro

        JLabel usuarioLabel = new JLabel("Usuario:");
        JTextField usuarioField = new JTextField(20);
        JLabel contraseñaLabel = new JLabel("Contraseña:");
        JPasswordField contraseñaField = new JPasswordField(20);
        JLabel tipoLabel = new JLabel("Tipo de Usuario:");
        String[] tipos = {"Normal", "Administrador"};
        JComboBox<String> tipoComboBox = new JComboBox<>(tipos);
        JButton registrarButton = new JButton("Registrar");

        panel.add(usuarioLabel);
        panel.add(usuarioField);
        panel.add(contraseñaLabel);
        panel.add(contraseñaField);
        panel.add(tipoLabel);
        panel.add(tipoComboBox);
        panel.add(registrarButton);

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioField.getText();
                String contraseña = new String(contraseñaField.getPassword());
                String tipoSeleccionado = (String) tipoComboBox.getSelectedItem();
                String tipo = tipoSeleccionado.equalsIgnoreCase("Administrador") ? "admin" : "normal";
                if (!usuario.isEmpty() && !contraseña.isEmpty()) {
                    Usuario nuevoUsuario = new Usuario(usuario, contraseña, tipo);
                    usuariosRegistrados.put(usuario, nuevoUsuario);
                    JOptionPane.showMessageDialog(null, "Usuario registrado exitosamente.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // Pantalla de rol de usuarios
    private static void RolUsuarios() {

        // Configuración de la ventana
        JFrame frame = new JFrame("EcoPlaya - Registro de Residuos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        panel.setBackground(new Color(173, 216, 230)); // Color verde claro
        frame.getContentPane().add(panel);

        // Botones y acciones
        JButton registrarResiduoButton = new JButton("Registrar Nuevo Residuo");
        JButton reportarProblemaButton = new JButton("Reportar Problema");
        JButton verMapaButton = new JButton("Ver Mapa Interactivo");
        JButton eventosButton = new JButton("Eventos de Limpieza");
        JButton cerrarSesionButton = new JButton("Cerrar Sesión");
        JButton verResiduosButton = new JButton("Ver Residuos");
        JButton verUsuariosButton = new JButton("Ver Usuarios Registrados"); // Nuevo botón para ver usuarios
        JButton verProblemasButton = new JButton("Ver Problemas Reportados"); // Nuevo botón para ver problemas

        panel.add(registrarResiduoButton);
        panel.add(reportarProblemaButton);
        panel.add(verMapaButton);
        panel.add(eventosButton);
        panel.add(cerrarSesionButton);
        panel.add(verResiduosButton);
        panel.add(verUsuariosButton); // Agregar el botón para ver usuarios
        panel.add(verProblemasButton); // Agregar el botón para ver problemas

        // Verificar el tipo de usuario para habilitar o deshabilitar botones
        if (usuarioActual != null && usuarioActual.getTipo().equals("admin")) {
            registrarResiduoButton.setEnabled(true);
            reportarProblemaButton.setEnabled(true);
            verMapaButton.setEnabled(true);
            eventosButton.setEnabled(true);
            cerrarSesionButton.setEnabled(true);
            verResiduosButton.setEnabled(true);
            verUsuariosButton.setEnabled(true); // Habilitar el botón para ver usuarios para los administradores
            verProblemasButton.setEnabled(true); // Habilitar el botón para ver problemas para los administradores
        } else {
            registrarResiduoButton.setEnabled(true);
            reportarProblemaButton.setEnabled(true);
            verMapaButton.setEnabled(false);
            eventosButton.setEnabled(true); // Permitir a los usuarios normales ver los eventos de limpieza
            cerrarSesionButton.setEnabled(true);
            verResiduosButton.setEnabled(true); // Habilitar la opción de ver residuos para todos los usuarios
            verUsuariosButton.setEnabled(false); // Deshabilitar el botón para ver usuarios para usuarios normales
            verProblemasButton.setEnabled(false); // Deshabilitar el botón para ver problemas para usuarios normales
        }

        registrarResiduoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistroDeResiduos();
            }
        });

        reportarProblemaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reportarProblema();
            }
        });

        verMapaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verMapaInteractivo();
            }
        });

        eventosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventosDeLimpieza(); // Mostrar la lista de eventos de limpieza
            }
        });

        cerrarSesionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usuarioActual = null;
                frame.dispose();
                Login();
            }
        });

        verResiduosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usuarioActual != null && usuarioActual.getTipo().equals("admin")) {
                    // Para los usuarios administradores, mostrar todos los residuos
                    ListaDeResiduos(reportesPendientes);
                } else {
                    // Para usuarios normales, mostrar solo sus propios residuos
                    ListaDeResiduos(getUserResidueList(usuarioActual.getNombre()));
                }
            }
        });

        verUsuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListaDeUsuarios(); // Mostrar la lista de usuarios registrados
            }
        });

        verProblemasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verProblemasReportados(); // Mostrar la lista de problemas reportados
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    // Método para obtener la lista de residuos de un usuario
    private static ArrayList<RegistroResiduo> getUserResidueList(String userName) {
        ArrayList<RegistroResiduo> userResidueList = new ArrayList<>();
        for (RegistroResiduo registro : reportesPendientes) {
            if (registro.getNombreUsuario().equals(userName)) {
                userResidueList.add(registro);
            }
        }
        return userResidueList;
    }

    // Pantalla para ver la lista de residuos
    private static void ListaDeResiduos(ArrayList<RegistroResiduo> residueList) {

        // Configuración de la ventana y tabla
        JFrame frame = new JFrame("Residuos Registrados");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());
        frame.getContentPane().add(panel);

        // Llenado de la tabla con los datos
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Usuario");
        model.addColumn("Tipo");
        model.addColumn("Ubicación");
        model.addColumn("Estado"); // Columna para el estado pendiente o atendido
        model.addColumn("Acciones"); // Columna para los botones de acción

        JTable table = new JTable(model);

        for (RegistroResiduo registro : residueList) {
            Object[] rowData = new Object[5];
            rowData[0] = registro.getNombreUsuario();
            rowData[1] = registro.getTipoResiduo();
            rowData[2] = registro.getUbicacion();
            rowData[3] = registro.isPendiente() ? "Pendiente" : "Atendido";

            JButton cambiarEstadoButton = new JButton("Cambiar Estado");
            cambiarEstadoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registro.setPendiente(!registro.isPendiente());
                    model.setValueAt(registro.isPendiente() ? "Pendiente" : "Atendido", table.getSelectedRow(), 3);
                }
            });
            rowData[4] = cambiarEstadoButton;

            model.addRow(rowData);
        }

        JScrollPane tableScrollPane = new JScrollPane(table);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // Pantalla de eventos de limpieza
    private static void EventosDeLimpieza() {

        // Configuración de la ventana y tabla
        JFrame frame = new JFrame("Eventos de Limpieza");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(173, 216, 230)); // Color verde claro
        frame.getContentPane().add(panel);

        // Llenado de la tabla con los eventos
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Fecha");
        model.addColumn("Ubicación");
        model.addColumn("Descripción");
        JTable table = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(table);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Agregar eventos de limpieza a la tabla
        for (EventoLimpieza evento : eventosLimpieza) {
            model.addRow(new Object[]{evento.getFecha(), evento.getUbicacion(), evento.getDescripcion()});
        }

        // Agregar evento de limpieza solo para usuarios administradores
        if (usuarioActual != null && usuarioActual.getTipo().equals("admin")) {
            JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            panel.add(buttonPanel, BorderLayout.SOUTH);

            JButton agregarEventoButton = new JButton("Agregar Evento");
            buttonPanel.add(agregarEventoButton);

            JButton eliminarEventoButton = new JButton("Eliminar Evento");
            buttonPanel.add(eliminarEventoButton);

            agregarEventoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    agregarEventoLimpieza();
                    // Actualizar la tabla después de agregar un nuevo evento
                    actualizarTablaEventos(model);
                }
            });

            eliminarEventoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        usuarioActual.eliminarEventoLimpieza(selectedRow);
                        model.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor selecciona un evento para eliminar.");
                    }
                }
            });
        }

        frame.setVisible(true);
    }

    private static void actualizarTablaEventos(DefaultTableModel model) {
        // Limpiar la tabla
        model.setRowCount(0);

        // Agregar eventos de limpieza actualizados a la tabla
        for (EventoLimpieza evento : eventosLimpieza) {
            model.addRow(new Object[]{evento.getFecha(), evento.getUbicacion(), evento.getDescripcion()});
        }
    }

    //Pantalla de registros de Residuos
    private static void RegistroDeResiduos() {

        // Configuración de la ventana y tabla
        JFrame frame = new JFrame("Registrar Nuevo Residuo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBackground(new Color(173, 216, 230)); // Color verde claro
        frame.getContentPane().add(panel);

        JTextField nombreField = new JTextField(20);
        JTextField tipoField = new JTextField(20);
        JTextField ubicacionField = new JTextField(20);
        
        //Llenado de la tabla con registros de residuos
        JLabel nombreLabel = new JLabel("Nombre de Usuario:");
        JLabel tipoLabel = new JLabel("Tipo de Residuo:");
        JLabel ubicacionLabel = new JLabel("Ubicación:");

        JButton registrarButton = new JButton("Registrar");

        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(tipoLabel);
        panel.add(tipoField);
        panel.add(ubicacionLabel);
        panel.add(ubicacionField);
        panel.add(registrarButton);

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String tipo = tipoField.getText();
                String ubicacion = ubicacionField.getText();
                if (!nombre.isEmpty() && !tipo.isEmpty() && !ubicacion.isEmpty()) {
                    registrarResiduo(nombre, tipo, ubicacion);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }

            private void registrarResiduo(String nombre, String tipo, String ubicacion) {
                RegistroResiduo nuevoRegistro = new RegistroResiduo(nombre, tipo, ubicacion, true);
                reportesPendientes.add(nuevoRegistro);
                JOptionPane.showMessageDialog(null, "Residuo registrado exitosamente.");
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    private static void reportarProblema() {
        String problema = JOptionPane.showInputDialog(null, "Por favor describa el problema:");
        if (problema != null && !problema.isEmpty()) {
            problemasReportados.add(usuarioActual.getNombre() + ": " + problema); // Agregar el nombre de usuario al problema reportado
            JOptionPane.showMessageDialog(null, "Problema reportado exitosamente.");
        }
    }

    private static void verMapaInteractivo() {
        JOptionPane.showMessageDialog(null, "Mostrando mapa interactivo.");
    }

    private static void agregarEventoLimpieza() {
        JFrame frame = new JFrame("Agregar Evento de Limpieza");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        frame.getContentPane().add(panel);

        JTextField fechaField = new JTextField(20);
        JTextField ubicacionField = new JTextField(20);
        JTextField descripcionField = new JTextField(20); // Campo de descripción del evento

        JLabel fechaLabel = new JLabel("Fecha:");
        JLabel ubicacionLabel = new JLabel("Ubicación:");
        JLabel descripcionLabel = new JLabel("Descripción:"); // Etiqueta para la descripción

        JButton agregarButton = new JButton("Agregar");

        panel.add(fechaLabel);
        panel.add(fechaField);
        panel.add(ubicacionLabel);
        panel.add(ubicacionField);
        panel.add(descripcionLabel);
        panel.add(descripcionField);
        panel.add(agregarButton);

        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fecha = fechaField.getText();
                String ubicacion = ubicacionField.getText();
                String descripcion = descripcionField.getText(); // Obtener la descripción del campo de texto
                if (!fecha.isEmpty() && !ubicacion.isEmpty() && !descripcion.isEmpty()) {
                    EventoLimpieza nuevoEvento = new EventoLimpieza(fecha, ubicacion, descripcion);
                    eventosLimpieza.add(nuevoEvento);
                    JOptionPane.showMessageDialog(null, "Evento de limpieza agregado exitosamente.");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    private static class RegistroResiduo {

        private String nombreUsuario;
        private String tipoResiduo;
        private String ubicacion;
        private boolean pendiente;

        RegistroResiduo(String nombreUsuario, String tipoResiduo, String ubicacion, boolean pendiente) {
            this.nombreUsuario = nombreUsuario;
            this.tipoResiduo = tipoResiduo;
            this.ubicacion = ubicacion;
            this.pendiente = pendiente;
        }

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public boolean isPendiente() {
            return pendiente;
        }

        public void setPendiente(boolean pendiente) {
            this.pendiente = pendiente;
        }

        public String getTipoResiduo() {
            return tipoResiduo;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public void cambiarEstado() {
            this.pendiente = !this.pendiente;
        }
    }

    private static class EventoLimpieza {

        private String fecha;
        private String ubicacion;
        private String descripcion; // Descripción del evento

        EventoLimpieza(String fecha, String ubicacion, String descripcion) {
            this.fecha = fecha;
            this.ubicacion = ubicacion;
            this.descripcion = descripcion;
        }

        public String getFecha() {
            return fecha;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    private static class Usuario {

        private String nombre;
        private String contraseña;
        private String tipo; // Agregar tipo de usuario

        public Usuario(String nombre, String contraseña, String tipo) {
            this.nombre = nombre;
            this.contraseña = contraseña;
            this.tipo = tipo;
        }

        public String getNombre() {
            return nombre;
        }

        public String getContraseña() {
            return contraseña;
        }

        public String getTipo() {
            return tipo;
        }

        public void agregarEventoLimpieza(String fecha, String ubicacion, String descripcion) {
            eventosLimpieza.add(new EventoLimpieza(fecha, ubicacion, descripcion));
        }

        public void eliminarEventoLimpieza(int indice) {
            if (indice >= 0 && indice < eventosLimpieza.size()) {
                eventosLimpieza.remove(indice);
            }
        }
    }

    private static void ListaDeUsuarios() {
        JFrame frame = new JFrame("Usuarios Registrados");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(173, 216, 230)); // Color verde claro
        frame.getContentPane().add(panel);

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Mostrar la información de los usuarios registrados en el JTextArea
        textArea.setText("Usuarios Registrados:\n");
        for (Map.Entry<String, Usuario> entry : usuariosRegistrados.entrySet()) {
            Usuario usuario = entry.getValue();
            textArea.append("Nombre: " + usuario.getNombre() + ", Tipo: " + usuario.getTipo() + "\n");
        }

        frame.setVisible(true);
    }

    private static void verProblemasReportados() {
        JFrame frame = new JFrame("Problemas Reportados");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(173, 216, 230)); // Color verde claro
        frame.getContentPane().add(panel);

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Mostrar la lista de problemas reportados
        textArea.setText("Problemas Reportados:\n");
        for (String problema : problemasReportados) {
            textArea.append(problema + "\n");
        }

        frame.setVisible(true);
    }

}
