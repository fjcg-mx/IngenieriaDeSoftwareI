package mx.uam.ayd.proyecto.negocio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.uam.ayd.proyecto.datos.GrupoRepository;
import mx.uam.ayd.proyecto.datos.UsuarioRepository;
import mx.uam.ayd.proyecto.negocio.modelo.Grupo;
import mx.uam.ayd.proyecto.negocio.modelo.Usuario;

@ExtendWith(MockitoExtension.class)
class ServicioUsuarioTest {
	
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private GrupoRepository grupoRepository;

  @Mock
  private ServicioGrupo servicioGrupo;
	
	@InjectMocks
	private ServicioUsuario servicioUsuario;

  private Grupo grupoPrueba;

  @BeforeEach
  void setUp() {
      grupoPrueba = new Grupo();
      grupoPrueba.setNombre("CK01"); // Establecemos el nombre del grupo en la prueba
  }

  @Test
  void testActualizarUsuario() {
      // Crear un usuario de prueba
      Usuario usuarioPrueba = new Usuario();
      usuarioPrueba.setNombre("Juan");
      usuarioPrueba.setApellido("Perez");
      usuarioPrueba.setEdad(20);
      usuarioPrueba.setIdUsuario(1L);  // Asegúrate de asignar un ID al usuario

      // Mockear que el grupoRepository encuentre el grupo correcto
      when(grupoRepository.findByNombre("CK01")).thenReturn(grupoPrueba);

      // Crear un grupo y asignar el usuario a este grupo
      grupoPrueba.addUsuario(usuarioPrueba); // Añadimos el usuario al grupo

      // Mockear el repositorio de usuario para que encuentre al usuario cuando se le pida
      when(usuarioRepository.findById(usuarioPrueba.getIdUsuario())).thenReturn(java.util.Optional.of(usuarioPrueba));
      
      // Simular que el usuario se guarda correctamente
      when(usuarioRepository.save(usuarioPrueba)).thenReturn(usuarioPrueba);

      // Simular la recuperación del grupo asociado al usuario (debemos devolver una lista de grupos)
      List<Grupo> listaGrupos = new ArrayList<>();
      listaGrupos.add(grupoPrueba); // Agregar el grupo a la lista
      when(servicioGrupo.recuperaGrupos()).thenReturn(listaGrupos); // Simulamos que el método devuelve una lista de grupos

      // Actualizar los datos del usuario
      usuarioPrueba.setNombre("Carlos");
      usuarioPrueba.setApellido("Gomez");
      usuarioPrueba.setEdad(35);

      // Llamamos al servicio para actualizar el usuario
      servicioUsuario.actualizarUsuario(usuarioPrueba, "CK01");

      // Verificar que los cambios se reflejan en el usuario
      assertEquals("Carlos", usuarioPrueba.getNombre());
      assertEquals("Gomez", usuarioPrueba.getApellido());
      assertEquals(35, usuarioPrueba.getEdad());

      // Verificar que la operación de guardar el usuario se realizó
      verify(usuarioRepository).save(usuarioPrueba);

      // Verificar que el grupo fue correctamente modificado
      verify(grupoRepository).findByNombre("CK01"); // Verificamos que se busque el grupo correcto
  }

	@Test
	void testRecuperaUsuarios() {

		// Caso de prueba 1: No hay usuarios guardados, me regresa lista vacía
		
		List <Usuario> usuarios = servicioUsuario.recuperaUsuarios();
		
		assertEquals(0,usuarios.size());
		
		
		// Caso de prueba 2: Si hay usuarios guardados, me regresa lista con usuarios

		ArrayList <Usuario> lista = new ArrayList <> ();

		// Tengo que crear un Iterable <Usuario> para que el método 
		// usuarioRepository.findAll() no me regrese una lista vacía
		// cuando lo invoco
		Usuario usuario1 = new Usuario();
		usuario1.setNombre("Juan");
		usuario1.setApellido("Perez");

		Usuario usuario2 = new Usuario();
		usuario2.setNombre("María");
		usuario2.setApellido("Ramírez");
		
		lista.add(usuario1);
		lista.add(usuario2);

		Iterable <Usuario> listaIterable = lista;
		
		// Al usar when, lo que hacemos es que definimos un comportamiento
		// para la invoación del método.
		// A partir de este punto, la invocación a usuarioRepository.findAll() ya
		// no me regresa una lista vacía, si no que me regresa una listaLigada
		// vista como Iterable que tiene dos elementos
		when(usuarioRepository.findAll()).thenReturn(listaIterable);
		
		usuarios = servicioUsuario.recuperaUsuarios();
		
		assertEquals(2,usuarios.size());
		
		
	}


}
