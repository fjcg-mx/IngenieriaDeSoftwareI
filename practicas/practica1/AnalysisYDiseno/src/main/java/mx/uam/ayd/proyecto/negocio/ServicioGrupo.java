package mx.uam.ayd.proyecto.negocio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.ayd.proyecto.datos.GrupoRepository;
import mx.uam.ayd.proyecto.datos.UsuarioRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Grupo;
import mx.uam.ayd.proyecto.negocio.modelo.Usuario;

@Service
/**
 * Servicio relacionado con los grupos
 * 
 * @author humbertocervantes
 *
 */
public class ServicioGrupo {
	
  @Autowired 
	private UsuarioRepository usuarioRepository;

	@Autowired 
	GrupoRepository grupoRepository;
	
	
	/**
	 * 
	 * Recupera todos los grupos
	 * 
	 * @return
	 */
	public List <Grupo> recuperaGrupos() {


		List <Grupo> grupos = new ArrayList<>();
		
		for(Grupo grupo:grupoRepository.findAll()) {
			grupos.add(grupo);
		}
				
		return grupos;
	}

  // Recuperar un grupo por nombre:
  public Grupo recuperaGrupoPorNombre(String nombre) {
    Grupo grupo = grupoRepository.findByNombre(nombre);
    if(grupo == null) {
        throw new IllegalArgumentException("Grupo no encontrado con el nombre: " + nombre);
    }
    return grupo;
  }

  // Crear un grupo nuevo:
  public Grupo crearGrupo(String nombre) {
    if(grupoRepository.findByNombre(nombre) != null) {
        throw new IllegalArgumentException("Ya existe un grupo con ese nombre");
    }

    Grupo nuevoGrupo = new Grupo();
    nuevoGrupo.setNombre(nombre);
    return grupoRepository.save(nuevoGrupo);
  }

  // Añadir un usuario a un grupo:
  public boolean agregarUsuarioAGrupo(long idUsuario, String nombreGrupo) {
    Grupo grupo = grupoRepository.findByNombre(nombreGrupo);
    Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

    if(grupo == null) {
        throw new IllegalArgumentException("El grupo no existe");
    }
    if(usuario == null) {
        throw new IllegalArgumentException("El usuario no existe");
    }

    return grupo.addUsuario(usuario);  // Utiliza el método de la entidad Grupo
  }

  // Eliminar un grupo:
  public void eliminarGrupo(long idGrupo) {
    Grupo grupo = grupoRepository.findById(idGrupo).orElse(null);
    if(grupo == null) {
        throw new IllegalArgumentException("Grupo no encontrado");
    }
    
    grupoRepository.delete(grupo);  // Elimina el grupo de la base de datos
  }

  // Eliminar un usuario de un grupo:
  public boolean eliminarUsuarioDeGrupo(long idUsuario, String nombreGrupo) {
    Grupo grupo = grupoRepository.findByNombre(nombreGrupo);
    Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

    if(grupo == null) {
        throw new IllegalArgumentException("Grupo no encontrado");
    }
    if(usuario == null) {
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    return grupo.removeUsuario(usuario);  // Utiliza el método de la entidad Grupo
  }

}
