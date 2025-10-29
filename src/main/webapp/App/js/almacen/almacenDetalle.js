
import { solicitudGet, solicitudPut, mostrarContenedor, addRowSelected, observeRowSelectedChange, deleteRowSelectedTable, fillInputSelect } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG, DISPLAY_TYPES, DISPONIBILIDAD, ROLES} from '../constantes.mjs';

const idInputUserRol = "#userRol";

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
const idBtnDetalla = "#btnDetalle";

let almacen;
const User = {rol: ROLES.USER};

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
    const btnDetalla = document.querySelector(idBtnDetalla);
    
    User.rol = document.querySelector(idInputUserRol).value;
  
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
            const referrer = document.referrer;

            if (referrer && referrer.includes("emplazamientos")) {
                // Si viene desde una página que contiene "emplazamientos"
                window.location.href = "almacen/almacenes";
            } else if (referrer) {
                // Si hay referrer pero no contiene "existencias"
                window.location.href = referrer;
            } else {
                // Si no hay referrer (por ejemplo, acceso directo)
                window.location.href = "almacen/almacenes";
            }
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
    
    btnDetalla.addEventListener('click', () => {
        const idEmplazamiento = tablaEmplazamientos.getAttribute('data-rowselected'); 
        window.location.href = (`almacen/emplazamientos/detalle/${idEmplazamiento}`);
    });
});

function onDetectarFilaSeleccionadaEmplazamientos(hayFilaSeleccionada) {
    if(User.rol === ROLES.ADMIN) {
        $(idBtnEliminar).prop('disabled', !hayFilaSeleccionada);
        $(idBtnModificar).prop('disabled', !hayFilaSeleccionada);
    }
    $(idBtnDetalla).prop('disabled', !hayFilaSeleccionada);
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
    const div = document.querySelector(idContenedorAlmacen);
    const inputNombreAlmacen = div.querySelector(idInputNombreAlmacen);
    const inputDescripcionAlmacen = div.querySelector(idInputDescripcionAlmacen);
    const inputNumeroEmplazamientos = div.querySelector(idInputNumeroEmplazamientos);
    const inputTotalExistencias = div.querySelector(idInputTotalExistencias);
   
    const listaEmplazamientos = almacen.listaEmplazamientos;
    const numEmplazamientos = listaEmplazamientos.length;
    
    const totalExistencias = listaEmplazamientos.reduce((total, emplazamiento) => {
        const disponibles = emplazamiento.listaExistencias.filter(
                existencia => existencia.disponible === DISPONIBILIDAD.DISPONIBLE.name
        ).length;
        return total + disponibles;
    }, 0);
    
    inputNombreAlmacen.value = almacen.nombre;
    inputDescripcionAlmacen.value = almacen.descripcion ?? "";
    inputNumeroEmplazamientos.value = numEmplazamientos;
    inputTotalExistencias.value = totalExistencias;
    
    fillFielsDireccion(almacen.direccion);
    rellenarTablaEmplazamientos(listaEmplazamientos);
}

function fillFielsDireccion(direccion) {
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

    tablaEmplazamientos.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = emplazamientos.map(emplazamiento => {
        const totalExistencias = emplazamiento.listaExistencias.reduce((total, existencia) => {
            if(existencia.disponible === DISPONIBILIDAD.DISPONIBLE.name) {
                total++;
            }
            return total;
        }, 0);
        
        return `<tr id="${emplazamiento.id}">
                <td>${emplazamiento.id}</td>
                <td>${emplazamiento.nombre}</td>
                <td>${emplazamiento.descripcion}</td>
                <td>${emplazamiento.tipoEmplazamiento.nombre}</td>
                <td>${totalExistencias}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaEmplazamientos.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaEmplazamientos);
}


function borrarEmplazamiento(emplazamientoId) {
    mostrarMensajeOpcion("Deshabilitar Emplazamiento", `¿Quieres realmente deshabilitar el emplazamiento con id ${emplazamientoId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/emplazamiento/delete/${emplazamientoId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede deshabilitar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Emplazamiento Deshabilitar.", `Se ha deshabilitar correctamente el emplazamiento.`, "success");

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

