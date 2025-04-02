package mx.uam.ayd.proyecto.presentacion.editarUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mx.uam.ayd.proyecto.negocio.modelo.Usuario;
import mx.uam.ayd.proyecto.negocio.ServicioGrupo;

import mx.uam.ayd.proyecto.negocio.modelo.Grupo;

import mx.uam.ayd.proyecto.negocio.ServicioUsuario;  // Suponiendo que tienes un servicio para manipular los usuarios

import java.util.List;


/**
 * 
 * Módulo de control para la historia de usuario EditarUsuario
 * 
 * @author franciscoJaviercruz
 *
 */

@Component
public class ControlEditarUsuario {

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private ServicioGrupo servicioGrupo;

    @Autowired
    private VentanaEditarUsuario ventana;

    private Runnable callback;  // Variable privada para almacenar el callback


    /**
     * Inicia la historia de usuario 
     * Muestra la ventana de edición para el usuario seleccionado
     * 
     */
    public void inicia(Usuario usuario, Runnable callback) {
      this.callback = callback;  // Guardar el callback para usarlo después

      // obtener la lista de grupos
      List <Grupo> grupos = servicioGrupo.recuperaGrupos();
      ventana.muestra(this, usuario, grupos);
      
      // El callback será ejecutado una vez el usuario 
      // haga click en 'Guardar', se procese los cambios y
      // notifique los cambios.
    }

    /**
     * Llamado cuando el usuario guarda los cambios
     */
    public void guardarCambios(Usuario usuario, String grupo) {
        // Aquí puedes agregar la lógica para guardar los cambios en la base de datos
        // Por ejemplo, utilizar el servicioUsuario para actualizar los datos del usuario.
        try {
          servicioUsuario.actualizarUsuario(usuario, grupo);
          ventana.muestraDialogoConMensaje("Actualizacion Exitosa");
          // Ejecutar el callback para notificar a la lista de usuarios
          if (callback != null) {
            callback.run();  // Ejecutamos el callback aquí
          }

        } catch(Exception ex) {
          ventana.muestraDialogoConMensaje("Error al actualizar usuario: "+ex.getMessage());
        }
        termina();
    }

    public Grupo buscaGrupo(Usuario usuario){
      Grupo grupo = servicioUsuario.recuperaGrupo(usuario);
      return grupo;
    }

    /**
     * Termina la historia de usuario
     * 
     */
    public void termina() {
      
      ventana.setVisible(false);		
    }
}
