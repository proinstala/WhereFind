
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {ROLES} from '../constantes.mjs';

const idInputNombre = "#nombre";
const idFormBusquedaAlmacen = "#frmBuscarAlmacen";
const idTablaAlmacen = "#tablaAlmacen"; 
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
        const tablaAlmacen = document.querySelector(idTablaAlmacen);
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
        
        validarFormulario(idFormBusquedaAlmacen);
        
        observeRowSelectedChange(tablaAlmacen, onDetectarFilaSeleccionada);
    
        btnModificar.addEventListener('click', () => {
            const idAlmacen = tablaAlmacen.getAttribute('data-rowselected'); //data-rowSelected
            window.location.href = (`almacen/almacenes/edit/${idAlmacen}`);
        });
        
        btnDetalle.addEventListener('click', () => {
            const idAlmacen = tablaAlmacen.getAttribute('data-rowselected'); //data-rowSelected
            window.location.href = (`almacen/almacenes/detalle/${idAlmacen}`);
        });
    
        btnCrear.addEventListener('click', () => {
            window.location.href = (`almacen/almacenes/crear`);
        });
    
        btnEliminar.addEventListener('click', () => {
            const idAlmacen = tablaAlmacen.getAttribute('data-rowselected'); //data-rowSelected
            borrarAlmacen(idAlmacen);
        });
        
        btnCancelar.addEventListener('click', () => {
            window.location.href = "almacen";
        });
        
        //Dispara el evento de clic en el botón para que haga una busqueda inicial.
        btnBuscar.click();
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
            const url = `api/almacen/find_almacenes?${formData}`;

            solicitudGet(url, "", false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaAlmacen(response.data);
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
 * Función que rellena una tabla HTML con los almacenes proporcionados.
 * @param {Array} almacenes - Un array de objetos de almacenes que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaAlmacen(almacenes) {
    const tablaAlmacenes = document.querySelector(idTablaAlmacen);
    const cuerpoTablaAlmacenes = tablaAlmacenes.querySelector('tbody');
    const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaAlmacenes.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = almacenes.map(almacen => {
        return `<tr id="${almacen.id}">
                <td>${almacen.id}</td>
                <td>${almacen.nombre}</td>
                <td>${almacen.descripcion}</td>
                <td>${almacen.direccion.localidad.nombre ?? ''}</td>
                <td>${almacen.direccion.localidad.provincia.nombre ?? ''}</td>
                </tr>`;
    }).join('');

    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaAlmacenes.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaAlmacenes);
}

function borrarAlmacen(almacenId) {
    mostrarMensajeOpcion("Borrar Al,acen", `¿Quieres realmente borrar los datos del almacén con id ${almacenId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/almacen/delete/${almacenId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Almacén Borrada.", `Se han borrado correctamente los datos del almacén.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaAlmacen);
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