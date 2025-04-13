
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs?v=20241021_184300';

const idSelectProvincia = "#provincia";
const idInputNombre = "#nombre";
const idFormBusquedaLocalidad = "#frmBuscarLocalidad";
const idTablaLocalidades = "#tablaLocalidades"; 
const idBtnBuscar = "#btnBuscar";
const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idBtnCancelar = "#btnCancelar";
const idInputUserRol = "#userRol";

const ADMIN = 'Admin';

const User = {
    rol: ""
};

// Configuración de las urls
//const URL_MODIFICAR_PROVINCIA = "direccion/edit";

document.addEventListener("DOMContentLoaded", function () {
    const formBusquedaLocalidades = document.querySelector(idFormBusquedaLocalidad);
    const selectProvincia = document.querySelector(idSelectProvincia);
    const tablaLocalidades = document.querySelector(idTablaLocalidades);
    const btnBuscar = document.querySelector(idBtnBuscar);
    const btnCrear = document.querySelector(idBtnCrear);
    const btnModificar = document.querySelector(idBtnModificar);
    const btnEliminar = document.querySelector(idBtnEliminar);
    const btnCancelar = document.querySelector(idBtnCancelar);
    
    User.rol = document.querySelector(idInputUserRol).value;

    if(User.rol === ADMIN) {
        btnCrear.disabled = false;
    }
    
    cargarInputSelect(selectProvincia, "api/provincia/provincias", 'Todas', false, "");
    
    validarFormulario(idFormBusquedaLocalidad);
    
    observeRowSelectedChange(tablaLocalidades, onDetectarFilaSeleccionada);

    btnModificar.addEventListener('click', () => {
        const idLocalidad = tablaLocalidades.getAttribute('data-rowselected'); //data-rowSelected
        window.location.href = (`direccion/localidades/edit/${idLocalidad}`);
    });

    btnCrear.addEventListener('click', () => {
        window.location.href = (`direccion/localidades/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        const idLocalidad = tablaLocalidades.getAttribute('data-rowselected'); //data-rowSelected
        borrarLocalidad(idLocalidad);
    });
    
    btnCancelar.addEventListener('click', () => {
        window.location.href = "direccion";
    });
    
    //Dispara el evento de clic en el botón para que haga una busqueda inicial.
    btnBuscar.click();
});

function onDetectarFilaSeleccionada(hayFilaSeleccionada) {
    if(User.rol === ADMIN) {
        $("#btnEliminar").prop('disabled', !hayFilaSeleccionada);
        $("#btnModificar").prop('disabled', !hayFilaSeleccionada);
    }
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: false,
                maxlength: 100
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                maxlength: "Longitud máx 100 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            debugger;
            const formData = getDatosForm(idForm);
            const url = `api/localidad/find_localidades?${formData}`;

            solicitudGet(url, "", false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaLocalidades(response.data);
                    }
                })
                .catch(error => {
                    // Maneja el error aquí
                    console.error("Error:", error);
                    mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                });
        },
        //Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}


/**
 * Función que rellena una tabla HTML con las localidades proporcionadas.
 * @param {Array} localidades - Un array de objetos de localidades que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaLocalidades(localidades) {
    const tablaLocalidades = document.querySelector(idTablaLocalidades);
    const cuerpoTablaLocalidades = tablaLocalidades.querySelector('tbody');
    const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaLocalidades.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = localidades.map(localidad => {
        return `<tr id="${localidad.id}">
                <td>${localidad.id}</td>
                <td>${localidad.nombre}</td>
                <td>${localidad.provincia.nombre}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaLocalidades.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaLocalidades);
}

function borrarLocalidad(localidadId) {
    mostrarMensajeOpcion("Borrar Localidad", `¿Quieres realmente borrar los datos de la provincia con id ${localidadId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/localidad/delete/${localidadId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Localidad Borrada.", `Se han borrado correctamente los datos de la localidad.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaLocalidades);
                                        }
                                    })
                                    .catch(error => {
                                        // Maneja el error aquí
                                        console.error("Error:", error);
                                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                                    });
                        } else if (result.isDenied) {
                            //denegado
                        } else if (result.isDismissed) {
                            //cancelado
                        }
                    });
}


