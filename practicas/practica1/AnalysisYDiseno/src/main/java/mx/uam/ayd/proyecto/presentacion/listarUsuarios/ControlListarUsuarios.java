package mx.uam.ayd.proyecto.presentacion.listarUsuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.negocio.modelo.Usuario;
import mx.uam.ayd.proyecto.negocio.ServicioUsuario;

import java.util.List;

import mx.uam.ayd.proyecto.presentacion.editarUsuario.ControlEditarUsuario;

/**
 * Esta clase lleva el flujo de control de la ventana principal
 * 
 * @author franciscojaviercruz
 *
 */

@Component
public class ControlListarUsuarios {
	
	private static final Logger log = LoggerFactory.getLogger(ControlListarUsuarios.class);
	
	@Autowired
	private ServicioUsuario servicioUsuario;

	@Autowired
	private ControlEditarUsuario controlEditarUsuario;

	@Autowired
	private VentanaListarUsuarios ventana;

	/**
	 * Inicia el caso de uso
	 */
	public void inicia() {
		List<Usuario> usuarios = servicioUsuario.recuperaUsuarios();
		log.info("Inicio de la ventana lista de usuarios: ");
		for(Usuario usuario : usuarios) {
			log.info(" <>: " + usuario);
		}
    ventana.muestra(this, usuarios);  // Llamar a muestra con la lista actualizada
	}

  /**
   * Método que arranca la historia de usuario "editar usuario"
   * 
   */
  
	public void editarUsuario(Usuario usuario) {

    // Iniciar la edición de un usuario
    controlEditarUsuario.inicia(usuario, new Runnable() {
      @Override
      public void run() {
          // Este código se ejecutará cuando la edición termine
          actualizarVistaConUsuarios();
      }
    });
/* 
    log.info(" - En funcion editarUsuario(Usuario usuario)");
    log.info(" recibio: usuario = " + usuario);
    log.info("se  llamara controlEditarUsuario.inicia(usuario);");
		controlEditarUsuario.inicia(usuario); // Inicia el flujo de edición
    log.info("  despues de la llamada, lista recuperada: ");
    // Después de la edición, recuperamos los usuarios actualizados de la base de datos
    List<Usuario> usuariosActualizados = servicioUsuario.recuperaUsuarios();
    
    for(Usuario user : usuariosActualizados) {
        log.info("Usuario actualizado: " + user);
    }

    log.info(" Se llamara a ventana.actualizarVistaConUsuarios(usuariosActualizados); ");
    // Actualizamos la vista de la lista de usuarios sin necesidad de cerrar y abrir la ventana
    ventana.actualizarVistaConUsuarios(usuariosActualizados);  
    log.info(" fin de la funcion de editarUsuario(Usuario usuario)");
    */
	}

  private void actualizarVistaConUsuarios() {
    // Recuperar los usuarios después de la edición y actualizar la vista
    List<Usuario> usuariosActualizados = servicioUsuario.recuperaUsuarios();
    ventana.muestra(this, usuariosActualizados);
}
}
