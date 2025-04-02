package mx.uam.ayd.proyecto.presentacion.listarUsuarios;

import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.ServicioUsuario;
import mx.uam.ayd.proyecto.negocio.modelo.Usuario;


/**
 * Ventana para listar usuarios usando JavaFX
 */
@Component
public class VentanaListarUsuarios {

	private Stage stage;
	private TableView<Usuario> tableUsuarios;
	private ControlListarUsuarios control;  // Controlador para listar usuarios
	
  private boolean initialized = false;

  private Button btnEditar; // Botón de editar
 	private static final Logger log = LoggerFactory.getLogger(ServicioUsuario.class);

  // Lista Observable que mantiene el estado de los usuarios
  private ObservableList<Usuario> usuariosData = FXCollections.observableArrayList();

	/**
	 * Constructor without UI initialization
	 */
  @Autowired
  public VentanaListarUsuarios() {
    // No inicializar componentes de JavaFX en el constructor
  }
	
	/**
	 * Initialize UI components on the JavaFX application thread
	 */
	private void initializeUI() {
     
		if (initialized) {
			return; // Si ya está inicializado, no hacemos nada
		}
		
    // Crea una UI solo si estamos en el hilo de JavaFX
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::initializeUI);
			return;
		}
		

		stage = new Stage();
		stage.setTitle("Lista de Usuarios");
		
		// Create UI components
		Label lblTitulo = new Label("Usuarios Registrados");
		
		// Create table
		tableUsuarios = new TableView<>();
		
		// Configure columns
		TableColumn<Usuario, Long> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
		
		TableColumn<Usuario, String> nombreColumn = new TableColumn<>("Nombre");
		nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		
		TableColumn<Usuario, String> apellidoColumn = new TableColumn<>("Apellido");
		apellidoColumn.setCellValueFactory(new PropertyValueFactory<>("apellido"));
		
		TableColumn<Usuario, Integer> edadColumn = new TableColumn<>("Edad");
		edadColumn.setCellValueFactory(new PropertyValueFactory<>("edad"));
		
		tableUsuarios.getColumns().addAll(idColumn, nombreColumn, apellidoColumn, edadColumn);
		
    // Botón de "Editar"
    btnEditar = new Button("Editar");
    btnEditar.setDisable(true); // Inicialmente deshabilitado
    
    // Habilitar el botón "Editar" cuando un usuario es seleccionado
    tableUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
      btnEditar.setDisable(newSelection == null); // Habilitar/deshabilitar el botón según la selección
    });

    btnEditar.setOnAction(e -> {
        Usuario usuarioSeleccionado = tableUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
          log.info("Se selecciono un usuario y se dio Clic en boton Editar...");
          control.editarUsuario(usuarioSeleccionado); // Llamar al controlador para editar el usuario
        }
    });
    
    // Boton Cerrar
		Button btnCerrar = new Button("Cerrar");
		btnCerrar.setOnAction(e -> stage.close());
		
		// Layout
		VBox vboxTop = new VBox(10);
		vboxTop.setPadding(new Insets(10));
		vboxTop.getChildren().add(lblTitulo);

    // boton cerrar
		VBox vboxBottom = new VBox(10);
		vboxBottom.setPadding(new Insets(10));
		vboxBottom.getChildren().addAll(btnEditar, btnCerrar);

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(vboxTop);
		borderPane.setCenter(tableUsuarios);
		borderPane.setBottom(vboxBottom);
		
    //Crear escena
		Scene scene = new Scene(borderPane, 450, 400);
		stage.setScene(scene);
    
		initialized = true;
	}
	
	/**
	 * Muestra la ventana y carga los usuarios
	 * 
	 * @param control El controlador asociado
	 * @param usuarios La lista de usuarios a mostrar
	 */
	public void muestra(ControlListarUsuarios control, List<Usuario> usuarios) {
		this.control = control;
		
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra(control, usuarios));
			return;
		}
		
		initializeUI();

    // Reiniciar el TableView para forzar la actualización
    ObservableList<Usuario> data = FXCollections.observableArrayList(usuarios);
    tableUsuarios.getItems().clear();  // Limpiar la tabla antes de volver a cargar los usuarios
    tableUsuarios.setItems(data);  // Asignar la nueva lista de usuarios

    // Forzamos la actualización de la tabla
    //tableUsuarios.refresh();  // Refrescar la vista de la tabla

    stage.show();
	}

  /**
   * Método para actualizar la vista de la lista de usuarios
   */
  public void actualizarVistaConUsuarios(List<Usuario> usuariosActualizados) {
    // Asegurarse de que la actualización de la vista ocurra en el hilo de la aplicación
    log.info("- En funcion actualizarVistaConUsuarios(List<Usuario> usuariosActualizados) ");

    Platform.runLater(() -> {
      // Actualizamos los datos de la lista con los usuarios editados
      usuariosData.setAll(usuariosActualizados);
      tableUsuarios.refresh(); // Forzamos la actualización de la tabla
      log.info("Vista de usuarios actualizada despues de la edicion");
    }); 
    log.info(" fin de la funcion actualizarVistaConUsuarios(List<Usuario> usuariosActualizados) ");

  }

  public void imprimirUsuariosEnTabla() {
    // Obtener los elementos actuales que están siendo mostrados en la tabla
    ObservableList<Usuario> usuarios = tableUsuarios.getItems();
    log.info(" En la funcion muestra, usuarios de la tableUsuarios.getItems(): ");
    // Imprimir cada usuario
    for (Usuario usuario : usuarios) {
        // Aquí puedes acceder a los atributos y mostrarlos
        log.info("Usuario en la tabla: ID=" + usuario.getIdUsuario() + 
                 ", Nombre=" + usuario.getNombre() +
                 ", Apellido=" + usuario.getApellido() +
                 ", Edad=" + usuario.getEdad());
    }
  }

}
