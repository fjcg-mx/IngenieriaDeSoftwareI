# Historia de Usuario

**Como** usuario del sistema, **quiero** poder actualizar la información de un usuario existente, **para que** pueda agregar o modificar datos relevantes de los usuarios cuando sea necesario.

## Conversación

### Vista de listado de usuarios
- Debe permitir que el usuario seleccione un usuario existente para editarlo. Puede hacerse con un botón "Editar" que se habilite cuando sea seleccionado un usuario.

### Vista de edición de usuario
- Una vez que el usuario haya seleccionado editar, se abrirá una nueva ventana separada (formulario) con los campos pre-rellenados para ese usuario. Allí podrá modificar la información.

### Validación de datos
- Si un campo es obligatorio (como nombre o apellidos), el sistema debe validar que los datos ingresados sean correctos o no estén vacíos.
- El sistema debe mostrar un mensaje de error si los datos no son válidos o están vacíos.

### Guardar los cambios
- Cuando un usuario haga clic en "Guardar" o "Actualizar", el sistema debe actualizar los datos del usuario en la base de datos.
- El sistema debe notificar al usuario que los cambios fueron guardados correctamente, por ejemplo mediante un mensaje en pantalla o notificación emergente.

### Error en caso de fallos
- Si ocurre un error al guardar los cambios (por ejemplo, problemas de conexión a la base de datos), el sistema debe mostrar un mensaje de error apropiado.
- La vista de listado debe mostrar la actualización realizada.

## Consideraciones

Podría considerarse esta historia de usuario como una **épica**, pero en nuestro ejemplo observamos que ya está implementado gran parte de la historia de mostrar una lista de usuarios. Solo es necesario añadir un botón para editar un usuario seleccionado, y pienso que desde aquí comienza nuestra HU. Sin embargo, la implementé como solo una historia porque al final el usuario también debe ver la actualización de sus modificaciones en la vista de lista de usuarios.

## Arquitectura

La arquitectura de referencia es **“Rich Client”**, por lo que debemos:

- Identificar cuáles son los módulos que debe llevar la aplicación.
- Identificar cuáles son las interfaces de los módulos.

### Diagrama de Módulos Involucrados

El diagrama que muestra los módulos involucrados para nuestra HU es el siguiente:

#### Capa de Presentación
- Tenemos las ventanas involucradas:
  - Vista principal.
  - Vista de la lista de usuarios.
  - Vista para editar un usuario.
  
  **Controladores**:
  - Aquí no agrego la vista de agregar usuario porque no se ocupa.

#### Capa de Negocio
- Actualmente tenemos dos entidades: **Usuario** y **Grupo**.
- Cada entidad tiene su respectivo **servicio**.

#### Capa de Datos
- Dado que tenemos dos entidades (Usuario y Grupo), se cuentan con sus respectivos **repositorios**.
- Recordemos que siempre hay un **servicio** y un **repositorio** por entidad.
