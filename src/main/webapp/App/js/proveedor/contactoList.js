
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {ROLES} from '../constantes.mjs';

const idSelectProveedor = "#proveedor";
const idInputNombre = "#nombre";
const idFormBusquedaContacto = "#frmBuscarContacto";
const idTablaContactos = "#tablaContactos"; 
const idBtnBuscar = "#btnBuscar";
const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idBtnCancelar = "#btnCancelar";
const idInputUserRol = "#userRol";

const User = {
    rol: ""
};

document.addEventListener("DOMContentLoaded", function () {
    const formBusquedaContactos = document.querySelector(idFormBusquedaContacto);
    const selectProveedor = document.querySelector(idSelectProveedor);
    const tablaContactos = document.querySelector(idTablaContactos);
    const btnBuscar = document.querySelector(idBtnBuscar);
    const btnCrear = document.querySelector(idBtnCrear);
    const btnModificar = document.querySelector(idBtnModificar);
    const btnEliminar = document.querySelector(idBtnEliminar);
    const btnCancelar = document.querySelector(idBtnCancelar);
 
    User.rol = document.querySelector(idInputUserRol).value;

    if(User.rol === ROLES.ADMIN || User.rol === ROLES.USER) {
        btnCrear.disabled = false;
    }
    
    const promesaProveedor = cargarInputSelect(selectProveedor, "api/proveedor/proveedores", 'Todos', false, "");
    
    validarFormulario(idFormBusquedaContacto);
    
    observeRowSelectedChange(tablaContactos, onDetectarFilaSeleccionada);

    btnModificar.addEventListener('click', () => {
        const idContacto = tablaContactos.getAttribute('data-rowselected'); //data-rowSelected
        window.location.href = (`proveedor/contactos/edit/${idContacto}`);
    });

    btnCrear.addEventListener('click', () => {
        window.location.href = (`proveedor/contactos/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        const idContacto = tablaContactos.getAttribute('data-rowselected'); //data-rowSelected
        borrarContacto(idContacto);
    });
    
    btnCancelar.addEventListener('click', () => {
        window.location.href = "proveedor";
    });
    
    Promise.all([promesaProveedor])
        .then(() => {
            //Dispara el evento de clic en el botón para que haga una busqueda inicial.
            btnBuscar.click();
        })
        .catch(error => {
            console.error("Error al cargar selects: ", error);
        });
});

function onDetectarFilaSeleccionada(hayFilaSeleccionada) {
    $("#btnEliminar").prop('disabled', !hayFilaSeleccionada);
    $("#btnModificar").prop('disabled', !hayFilaSeleccionada);
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
            const formData = getDatosForm(idForm);
            const url = `api/contacto/find_contactos?${formData}`;

            solicitudGet(url, "", false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaContactos(response.data);
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
 * Función que rellena una tabla HTML con los contactos proporcionadas.
 * @param {Array} contactos - Un array de objetos de contactos que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaContactos(contactos) {
    const tablaContactos = document.querySelector(idTablaContactos);
    const cuerpoTablaContactos = tablaContactos.querySelector('tbody');
    const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaContactos.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = contactos.map(contacto => {
        return `<tr id="${contacto.id}">
                <td>${contacto.id}</td>
                <td>${contacto.nombre}</td>
                <td>${contacto.apellido}</td>
                <td>${contacto.puestoTrabajo.nombre}</td>
                <td>${contacto.telefono}</td>
                <td>${contacto.email}</td>
                <td>${contacto.proveedor ? contacto.proveedor.nombre : ''}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaContactos.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaContactos);
}


function borrarContacto(contactoId) {
    mostrarMensajeOpcion("Borrar Contacto", `¿Quieres realmente borrar los datos del contacto con id ${contactoId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/contacto/delete/${contactoId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Contacto Borrado.", `Se han borrado correctamente los datos del contacto.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaContactos);
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