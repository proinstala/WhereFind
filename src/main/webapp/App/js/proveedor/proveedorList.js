
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {ROLES} from '../constantes.mjs';

const idInputNombre = "#nombre";
const idFormBusquedaProveedor = "#frmBuscarProveedor";
const idTablaProveedores = "#tablaProveedores"; 
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
    const formBusquedaProveedores = document.querySelector(idFormBusquedaProveedor);
    const tablaProveedores = document.querySelector(idTablaProveedores);
    const btnBuscar = document.querySelector(idBtnBuscar);
    const btnCrear = document.querySelector(idBtnCrear);
    const btnModificar = document.querySelector(idBtnModificar);
    const btnEliminar = document.querySelector(idBtnEliminar);
    const btnCancelar = document.querySelector(idBtnCancelar);

    User.rol = document.querySelector(idInputUserRol).value;

    if(User.rol === ROLES.ADMIN || User.rol === ROLES.USER) {
        btnCrear.disabled = false;
    }
    
    validarFormulario(idFormBusquedaProveedor);
    
    observeRowSelectedChange(tablaProveedores, onDetectarFilaSeleccionada);

    btnModificar.addEventListener('click', () => {
        const idProveedor = tablaProveedores.getAttribute('data-rowselected'); //data-rowSelected
        window.location.href = (`proveedor/proveedores/edit/${idProveedor}`);
    });

    btnCrear.addEventListener('click', () => {
        window.location.href = (`proveedor/proveedores/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        debugger;
        const idProveedor = tablaProveedores.getAttribute('data-rowselected'); //data-rowSelected
        borrarProveedor(idProveedor);
    });
    
    btnCancelar.addEventListener('click', () => {
        window.location.href = "proveedor";
    });
    
    //Dispara el evento de clic en el botón para que haga una busqueda inicial.
    btnBuscar.click();
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
            const url = `api/proveedor/find_proveedores?${formData}`;

            solicitudGet(url, "", false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaProveedores(response.data);
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
 * Función que rellena una tabla HTML con los proveedores proporcionadas.
 * @param {Array} proveedores - Un array de objetos de proveedores que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaProveedores(proveedores) {
    const tablaProveedores = document.querySelector(idTablaProveedores);
    const cuerpoTablaProveedores = tablaProveedores.querySelector('tbody');
    const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaProveedores.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = proveedores.map(proveedor => {
        return `<tr id="${proveedor.id}">
                <td>${proveedor.id}</td>
                <td>${proveedor.nombre}</td>
                <td>${proveedor.descripcion}</td>
                <td>${proveedor.paginaWeb}</td>
                <td>${proveedor.direccion.localidad.nombre ?? ''}</td>
                <td>${proveedor.direccion.localidad.provincia.nombre ?? ''}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaProveedores.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaProveedores);
}


function borrarProveedor(proveedorId) {
    debugger;
    mostrarMensajeOpcion("Borrar Proveedor", `¿Quieres realmente borrar los datos del proveedor con id ${proveedorId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/proveedor/delete/${proveedorId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Proveedor Borrada.", `Se han borrado correctamente los datos del proveedor.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaProveedores);
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

