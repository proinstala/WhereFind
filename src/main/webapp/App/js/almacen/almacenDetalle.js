
import { solicitudGet, solicitudPut, mostrarContenedor, addRowSelected, observeRowSelectedChange, deleteRowSelectedTable, fillInputSelect } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG, DISPLAY_TYPES} from '../constantes.mjs';

const idInputIdAlmacen = "#almacen_id";
const idInputNombreAlmacen = "#nombreAlmacen";
const idInputDescripcionAlmacen = "#descripcionAlmacen";
const idInputNumeroEmplazamientos = "#numeroEmplazamientos";
const idInputTotalExistencias = "#totalExistencias";

const idInputCalle = "#calle";
const idInputNumero = "#numero";
const idInputCodigoPostal = "#codigoPostal";
const idInputLocalidad = "#localidad";
const idInputProvincia = "#provincia";

const idTablaEmplazamientos = "#tablaEmplazamientos";

const nameContenedorDatos = "contenedorDatos";
const idContenedorAlmacen = "#contenedorAlmacen";
const idContenedorDireccion = "#contenedorDireccion";
const idContenedorEmplazamiento = "#contenedorEmplazamiento";
const nameBtnAlmacen = "btnAlmacen";
const nameBtnDireccion = "btnDireccion";
const nameBtnEmplazamientos = "btnEmplazamientos";
const idBtnCancelar = "#btnCancelar";
const nameBtnCancelar = "btnCancelar";

const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";

let almacen;

$(document).ready(function () {
    const inputIdAlmacen = document.querySelector(idInputIdAlmacen);
    const btnsAlmacen = document.querySelectorAll(`[name="${nameBtnAlmacen}"`);
    const btnsDireccion = document.querySelectorAll(`[name="${nameBtnDireccion}"`);
    const btnsEmplazamientos = document.querySelectorAll(`[name="${nameBtnEmplazamientos}"`);
    const btnsCancelar = document.querySelectorAll(`[name="${nameBtnCancelar}"]`); 
    const tablaEmplazamientos = document.querySelector(idTablaEmplazamientos);
    const btnCrear = document.querySelector(idBtnCrear);
    const btnModificar = document.querySelector(idBtnModificar);
    const btnEliminar = document.querySelector(idBtnEliminar);
    
  
    getAlmacen(inputIdAlmacen.value);
    
    observeRowSelectedChange(tablaEmplazamientos, onDetectarFilaSeleccionadaEmplazamientos);
    
    btnsAlmacen.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos,  idContenedorAlmacen);
        });
    });
    
    btnsDireccion.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorDireccion);
        });
    });
    
    btnsEmplazamientos.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorEmplazamiento);
        });
    });
    
    btnsCancelar.forEach(btn => {
        btn.addEventListener('click', () => {
            window.location.href = "almacen/almacenes";
        });
    });
    
    btnModificar.addEventListener('click', () => {
        const idEmplazamiento = tablaEmplazamientos.getAttribute('data-rowselected'); 
        window.location.href = (`almacen/emplazamientos/edit/${idEmplazamiento}`);
    });
    
    btnCrear.addEventListener('click', () => {
        window.location.href = (`almacen/emplazamientos/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        const idEmplazamiento = tablaEmplazamientos.getAttribute('data-rowselected'); 
        borrarEmplazamiento(idEmplazamiento);
    });
});

function onDetectarFilaSeleccionadaEmplazamientos(hayFilaSeleccionada) {
    $(idBtnEliminar).prop('disabled', !hayFilaSeleccionada);
    $(idBtnModificar).prop('disabled', !hayFilaSeleccionada);
}


function getAlmacen(idAlmacen) {
    solicitudGet(`api/almacen/almacen?idAlmacen=${idAlmacen}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    almacen = response.data;
                    fillFielsAlmacen(almacen);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsAlmacen(almacen) {
    //almacen
    debugger;
    const div = document.querySelector(idContenedorAlmacen);
    const inputNombreAlmacen = div.querySelector(idInputNombreAlmacen);
    const inputDescripcionAlmacen = div.querySelector(idInputDescripcionAlmacen);
   
    inputNombreAlmacen.value = almacen.nombre;
    inputDescripcionAlmacen.value = almacen.descripcion ?? "";
    
    
    
    fillFielsDireccion(almacen.direccion);
    //rellenarTablaEmplazamientos(almacen.listaEmplazamientos);
}

function fillFielsDireccion(direccion) {
    //direccion
    const direccionId = direccion.id;
    const divDireccion = document.querySelector(idContenedorDireccion);
    const inputCalle = divDireccion.querySelector(idInputCalle);
    const inputNumero = divDireccion.querySelector(idInputNumero);
    const inputCodigoPostal = divDireccion.querySelector(idInputCodigoPostal);
    const inputProvincia = divDireccion.querySelector(idInputProvincia);
    const inputLocalidad = divDireccion.querySelector(idInputLocalidad);

    inputCalle.value = direccion.calle ?? "";
    inputNumero.value = direccion.numero ?? "";
    inputLocalidad.value = direccion.localidad.nombre ?? "";
    inputProvincia.value = direccion.localidad.provincia.nombre ?? "";
    if(direccion.codigoPostal) {
        inputCodigoPostal.value = direccion.codigoPostal ?? "";
    }
    
}


/**
 * Función que rellena una tabla HTML con los emplazamientos proporcionadas.
 * @param {Array} emplazamientos - Un array de objetos de emplazamientos que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaEmplazamientos(emplazamientos) {
    const tablaEmplazamientos = document.querySelector(idTablaEmplazamientos);
    const cuerpoTablaEmplazamientos = tablaEmplazamientos.querySelector('tbody');
    //const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaEmplazamientos.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = emplazamientos.map(contacto => {
        return `<tr id="${contacto.id}">
                <td>${contacto.id}</td>
                <td>${contacto.nombre}</td>
                <td>${contacto.apellido}</td>
                <td>${contacto.puestoTrabajo.nombre}</td>
                <td>${contacto.telefono}</td>
                <td>${contacto.email}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaEmplazamientos.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaEmplazamientos);
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
                                            deleteRowSelectedTable(idTablaEmplazamientos);
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

