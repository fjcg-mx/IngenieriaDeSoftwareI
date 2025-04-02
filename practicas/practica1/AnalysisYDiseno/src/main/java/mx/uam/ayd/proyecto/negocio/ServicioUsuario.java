package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.uam.ayd.proyecto.datos.GrupoRepository;
import mx.uam.ayd.proyecto.datos.UsuarioRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Grupo;
import mx.uam.ayd.proyecto.negocio.modelo.Usuario;

@Service
public class ServicioUsuario {
	
	// Define a static logger field
	private static final Logger log = LoggerFactory.getLogger(ServicioUsuario.class);
	
	@Autowired 
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private GrupoRepository grupoRepository;

  @Autowired
  private ServicioGrupo servicioGrupo;

	/**
	 * 
	 * Permite agregar un usuario
	 * 
	 * @param nombre nombre del usuario
	 * @param apellido apellido del usuario
	 * @param grupo nombre grupo al que debe pertencer
	 * @return el usuario que se agregó
	 * @throws IllegalArgumentException si el usuario ya 
	 * existe o no existe el grupo
	 * 
	 */
	public Usuario agregaUsuario(String nombre, String apellido, String nombreGrupo) {
		
		// Regla de negocio: No se permite agregar dos usuarios con el mismo nombre y apellido

		Usuario usuario = usuarioRepository.findByNombreAndApellido(nombre, apellido);
		
		if(usuario != null) {
			throw new IllegalArgumentException("Ese usuario ya existe");
		}
		
		Grupo grupo = grupoRepository.findByNombre(nombreGrupo);
		
		if(grupo == null) {
			throw new IllegalArgumentException("No se encontró el grupo");
		}
		
		// Se validaron correctamente las reglas de negocio
		
		log.info("Agregando usuario nombre: "+nombre+" apellido:"+apellido);
		
		// Crea el usuario
		
		usuario = new Usuario();
		usuario.setNombre(nombre);
		usuario.setApellido(apellido);
		
		//usuarioRepository.save(usuario); // Esto es el create
		// Persistir el usuario
    usuario = usuarioRepository.save(usuario);

		// Conecta al grupo con el usuario
		
		grupo.addUsuario(usuario);
		
		grupoRepository.save(grupo); // Esto es el update
		
		return usuario;
		

	}

  /**
	 * 
	 * Permite actualizar un usuario
	 * 
	 * @param usuario nombre del usuario
	 * 
	 */
  //@Transactional
  public void actualizarUsuario(Usuario usuario, String nombreGrupo) {
    log.info("Iniciando la actualización del usuario con ID: {}", usuario.getIdUsuario());    // Buscar el grupo por nombre
    // Encontrar el nuevo grupo
    Grupo grupoNuevo  = grupoRepository.findByNombre(nombreGrupo);

    // Verificar si el grupo existe
    if (grupoNuevo  == null) {
      log.error("Error: No se encontró el grupo con nombre: {}", nombreGrupo);
      throw new IllegalArgumentException("No se encontró el grupo");
    }

    // Validar si el usuario existe
    Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario()).orElse(null);

    // Si no existe, lanzamos una excepción
    if (usuarioExistente == null) {
      log.error("Error: El usuario con ID: {} no fue encontrado.", usuario.getIdUsuario());
      throw new IllegalArgumentException("Usuario no encontrado");
    }

    // Verificamos si es necesario actualizar el grupo del usuario
    Grupo grupoExistente = recuperaGrupo(usuarioExistente); // Encontramos el grupo actual
    log.info("Grupo actual del usuario: " + grupoExistente.getNombre());

    if (!grupoExistente.getNombre().equals(grupoNuevo.getNombre())) {  
        // Si el usuario pertenece a otro grupo, lo eliminamos de ese grupo
        log.info("El usuario con ID: " + usuarioExistente.getIdUsuario() + " pertenece al grupo: " + grupoExistente.getNombre() + ", removiéndolo...");
        grupoExistente.removeUsuario(usuarioExistente); // Eliminar del grupo anterior
        grupoRepository.save(grupoExistente); // Guardar el grupo actualizado (para eliminar la relación de la base de datos)

        // Añadir al usuario al nuevo grupo
        log.info("Añadiendo al usuario al grupo: " + grupoNuevo.getNombre());
        grupoNuevo.addUsuario(usuarioExistente);
    }
    // Actualizar los datos del usuario
    log.info("Actualizando datos del usuario: {} -> {} {}", usuarioExistente.getNombre(), usuario.getNombre(), usuario.getApellido());
    usuarioExistente.setNombre(usuario.getNombre());
    usuarioExistente.setApellido(usuario.getApellido());
    usuarioExistente.setEdad(usuario.getEdad());
    
    // Guardar el usuario actualizado en el repositorio
    usuarioRepository.save(usuarioExistente);
    log.info("Usuario con ID: {} actualizado correctamente.", usuarioExistente.getIdUsuario());

    // Guardar el grupo si ha sido modificado
    grupoRepository.save(grupoNuevo);
    log.info("Grupo con nombre: {} actualizado correctamente.", grupoNuevo.getNombre());

    log.info(" Fin de actualizacion");

  }

	public Grupo recuperaGrupo(Usuario usuarioExistente) {
    // Buscar en todos los grupos el que contiene al usuario
    List<Grupo> grupos = servicioGrupo.recuperaGrupos();
    
    for (Grupo grupo : grupos) {
        if (grupo.getUsuarios().contains(usuarioExistente)) {
            return grupo;
        }
    }
    
    return null; // Si no se encuentra el grupo
  }

  /**
	 * Recupera todos los usuarios existentes
	 * 
	 * @return Una lista con los usuarios (o lista vacía)
	 */
	public List <Usuario> recuperaUsuarios() {

		
		System.out.println("usuarioRepository = "+usuarioRepository);
		
		List <Usuario> usuarios = new ArrayList<>();
		
		for(Usuario usuario:usuarioRepository.findAll()) {
			usuarios.add(usuario);
		}
				
		return usuarios;
	}

}
