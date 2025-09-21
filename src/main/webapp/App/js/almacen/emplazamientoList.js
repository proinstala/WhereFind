
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {ROLES} from '../constantes.mjs';

const idInputNombre = "#nombre";
const idSelectTipo = "#tipo";
const idSelectAlmacen = "#almacen";
const idFormBusquedaEmplazamiento = "#frmBuscarEmplazamiento";
const idTablaEmplazamiento = "#tablaEmplazamiento"; 
const idBtnBuscar = "#btnBuscar";
const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idBtnCancelar = "#btnCancelar";
const idBtnDetalle = "#btnDetalle";
const idInputUserRol = "#userRol";

const User = {
    rol: ROLES.USER
};

document.addEventListener("DOMContentLoaded", function () {
        const tablaEmplazamiento = document.querySelector(idTablaEmplazamiento);
        const selectTipo = document.querySelector(idSelectTipo);
        const selectAlmacen = document.querySelector(idSelectAlmacen);
        const btnBuscar = document.querySelector(idBtnBuscar);
        const btnCrear = document.querySelector(idBtnCrear);
        const btnModificar = document.querySelector(idBtnModificar);
        const btnEliminar = document.querySelector(idBtnEliminar);
        const btnDetalle = document.querySelector(idBtnDetalle);
        const btnCancelar = document.querySelector(idBtnCancelar);

        User.rol = document.querySelector(idInputUserRol).value;
   
        if(User.rol === ROLES.ADMIN || User.rol === ROLES.USER) {
            btnCrear.disabled = false;
        }

        const promesaTipo = cargarInputSelect(selectTipo, "api/tipo_emplazamiento/tipos_emplazamientos", 'Todos', false, "");
        const promesaAlmacen = cargarInputSelect(selectAlmacen, "api/almacen/almacenes", 'Todos', false, "");
        
        validarFormulario(idFormBusquedaEmplazamiento);
        
        observeRowSelectedChange(tablaEmplazamiento, onDetectarFilaSeleccionada);
    
        btnModificar.addEventListener('click', () => {
            const idEmplazamiento = tablaEmplazamiento.getAttribute('data-rowselected'); //data-rowSelected
            window.location.href = (`almacen/emplazamientos/edit/${idEmplazamiento}`);
        });
        
        btnDetalle.addEventListener('click', () => {
            const idEmplazamiento = tablaEmplazamiento.getAttribute('data-rowselected'); //data-rowSelected
            window.location.href = (`almacen/emplazamientos/detalle/${idEmplazamiento}`);
        });
    
        btnCrear.addEventListener('click', () => {
            window.location.href = (`almacen/emplazamientos/crear`);
        });
    
        btnEliminar.addEventListener('click', () => {
            const idEmplazamiento = tablaEmplazamiento.getAttribute('data-rowselected'); //data-rowSelected
            borrarEmplazamiento(idEmplazamiento);
        });
        
        btnCancelar.addEventListener('click', () => {
            window.location.href = "almacen";
        });
        
        Promise.all([promesaTipo, promesaAlmacen])
        .then(() => {
            //Dispara el evento de clic en el botón para que haga una busqueda inicial.
            btnBuscar.click();
        })
        .catch(error => {
            console.error("Error al cargar selects: ", error);
        });
});

function onDetectarFilaSeleccionada(hayFilaSeleccionada) {
    $(idBtnEliminar).prop('disabled', !hayFilaSeleccionada);
    $(idBtnModificar).prop('disabled', !hayFilaSeleccionada);
    $(idBtnDetalle).prop('disabled', !hayFilaSeleccionada);
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: false,
                maxlength: 100
            }, 
            descripcion: {
                required: false,
                maxlength: 100
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                maxlength: "Longitud máx 100 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const formData = getDatosForm(idForm);
            const url = `api/emplazamiento/find_emplazamientos?${formData}`;

            solicitudGet(url, "", false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaEmplazamiento(response.data);
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
 * Función que rellena una tabla HTML con los emplazamientos proporcionados.
 * @param {Array} emplazamientos - Un array de objetos de emplazamientos que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaEmplazamiento(emplazamientos) {
    const tablaEmplazamientos = document.querySelector(idTablaEmplazamiento);
    const cuerpoTablaEmplazamientos = tablaEmplazamientos.querySelector('tbody');
    const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaEmplazamientos.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = emplazamientos.map(emplazamiento => {
        return `<tr id="${emplazamiento.id}">
                <td>${emplazamiento.id}</td>
                <td>${emplazamiento.nombre}</td>
                <td>${emplazamiento.descripcion}</td>
                <td>${emplazamiento.tipoEmplazamiento.nombre ?? ''}</td>
                <td>${emplazamiento.almacen.nombre ?? ''}</td>
                </tr>`;
    }).join('');

    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaEmplazamientos.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaEmplazamientos);
}

function borrarEmplazamiento(emplazamientoId) {
    mostrarMensajeOpcion("Borrar Al,acen", `¿Quieres realmente borrar los datos del emplazamiento con id ${emplazamientoId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/emplazamiento/delete/${emplazamientoId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Alamacen Borrada.", `Se han borrado correctamente los datos del alamacen.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaEmplazamiento);
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