package mx.uam.ayd.proyecto.presentacion.editarUsuario;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import mx.uam.ayd.proyecto.negocio.modelo.Grupo;
import mx.uam.ayd.proyecto.negocio.modelo.Usuario;

import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Vista para editar un usuario
 */
@Component
public class VentanaEditarUsuario {

    private Stage stage;
    private TextField txtNombre;
    private TextField txtApellido;
    private TextField txtEdad;
    private ComboBox<String> comboBoxGrupo;

    private Button btnGuardar;
    private Button btnCancelar;
    
    private ControlEditarUsuario control;

    private Usuario usuario;

    private boolean initialized = false;

    /**
     * Constructor without UI initialization
     */
    public VentanaEditarUsuario() {
        // Constructor vacío
    }

    /**
     * Inicializa los componentes de la UI en el hilo de la aplicación JavaFX
     */
    private void initializeUI() {
        if (initialized) {
            return;
        }
        
        // Create UI only if we're on JavaFX thread
        if (!Platform.isFxApplicationThread()) {
          Platform.runLater(this::initializeUI);
          return;
        }

        // Crear la ventana
        stage = new Stage();
        stage.setTitle("Editar Usuario");
        
        // Crear un GridPane para una disposición más estructurada
        GridPane grid = new GridPane();
        grid.setVgap(10);  // Espacio vertical
        grid.setHgap(10);  // Espacio horizontal
        grid.setPadding(new Insets(20));  // Padding de 20 píxeles

        // Crear los componentes de la UI
        Label lblNombre = new Label("Nombre:");
        Label lblApellido = new Label("Apellido:");
        Label lblEdad = new Label("Edad:");
        Label lblGrupo = new Label("Grupo:");

        // Crear los campos de texto
        txtNombre = new TextField();
        txtApellido = new TextField();
        txtEdad = new TextField();
        comboBoxGrupo = new ComboBox<>();

        // Estilizar los campos de texto
        //txtNombre.setMinWidth(200);
        //txtApellido.setMinWidth(200);
        //txtEdad.setMinWidth(200);

        // Crear botones
        btnGuardar = new Button("Guardar");
        btnCancelar = new Button("Cancelar");

        // Estilizar los botones
        //btnGuardar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        //btnCancelar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px;");
        

        // Añadir acciones a los botones
        btnGuardar.setOnAction(e -> {
            if(txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()) {
              muestraDialogoConMensaje("El nombre y el apellido no deben estar vacios");
            } else {
              // Obtener los datos del formulario (nombre, etc.)
              String nuevo_nombre = txtNombre.getText();
              String nuevo_apellido = txtApellido.getText();
              int nueva_edad = Integer.parseInt(txtEdad.getText()); // Suponiendo que es un número
              // Actualizamos el usuario con los nuevos datos
              usuario.setNombre(nuevo_nombre);
              usuario.setApellido(nuevo_apellido);
              usuario.setEdad(nueva_edad);
              
              control.guardarCambios(usuario, comboBoxGrupo.getValue()); // Llamamos al controlador para guardar los cambios
            }
            
        });

        btnCancelar.setOnAction(e -> {
          control.termina();
        });

        // Colocar los componentes en el GridPane
        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(lblApellido, 0, 1);
        grid.add(txtApellido, 1, 1);
        grid.add(lblEdad, 0, 2);
        grid.add(txtEdad, 1, 2);
        grid.add(lblGrupo, 0, 3);
        grid.add(comboBoxGrupo, 1, 3);
        
        // Buttons in HBox
		    HBox buttonBox = new HBox(10);
		    buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.setCenterShape(true);
		    buttonBox.getChildren().addAll(btnGuardar, btnCancelar);

        // Diseño de la ventana
        // Main layout
		    VBox vbox = new VBox(10);
		    vbox.setPadding(new Insets(10));
		    vbox.getChildren().addAll(grid, buttonBox);
        
        // Crear la escena
        Scene scene = new Scene(vbox, 350, 300, Color.WHITE);
        stage.setScene(scene);

        initialized = true;
    }

    /**
     * Muestra la ventana de edición
     *
     * @param control El controlador asociado
     * @param usuario El usuario a editar
     */
    public void muestra(ControlEditarUsuario controlEditarUsuario, Usuario usuario, List<Grupo> grupos) {
        this.control = controlEditarUsuario;
        this.usuario = usuario;

        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.muestra(control, usuario, grupos));
            return;
        }

        initializeUI();

        // Pre-cargar los datos del usuario en los campos
        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtEdad.setText(String.valueOf(usuario.getEdad()));

        comboBoxGrupo.getItems().clear();
        for (Grupo g : grupos) {
            comboBoxGrupo.getItems().add(g.getNombre());
        }
        // buscar el grupo al que pertenece el usuario
        Grupo grupo = controlEditarUsuario.buscaGrupo(usuario);
        // Seleccionar el grupo actual
        comboBoxGrupo.setValue(grupo.getNombre());
        stage.show();
    }

    /**
	 * Muestra un diálogo con un mensaje
	 * 
	 * @param mensaje El mensaje a mostrar
	 */
    public void muestraDialogoConMensaje(String mensaje) {
      if (!Platform.isFxApplicationThread()) {
        Platform.runLater(() -> this.muestraDialogoConMensaje(mensaje));
        return;
      }
      
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Información");
      alert.setHeaderText(null);
      alert.setContentText(mensaje);
      alert.showAndWait();
    }

    /**
     * Oculta la ventana
     */
    public void setVisible(boolean visible) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> this.setVisible(visible));
            return;
        }
        
        if (!initialized) {
            if (visible) {
              initializeUI();
            } else {
              return;
            }
        }
        
        if (visible) {
            stage.show();
        } else {
            stage.hide();
        }
    }
}
