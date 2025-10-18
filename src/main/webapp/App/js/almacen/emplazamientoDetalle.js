
import { solicitudGet, solicitudPut, mostrarContenedor, addRowSelected, observeRowSelectedChange, deleteRowSelectedTable, fillInputSelect } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG, DISPLAY_TYPES, DISPONIBILIDAD, ROLES, ICONOS_TABLA} from '../constantes.mjs';

const idInputUserRol = "#userRol";

const idInputIdEmplazamiento = "#emplazamiento_id";

const idInputNombreEmplazamiento = "#nombreEmplazamiento";
const idInputDescripcionEmplazamiento = "#descripcionEmplazamiento";
const idInputTipoEmplazamiento = "#tipoEmplazamiento";
const idInputTotalExistencias = "#totalExistencias";

const idInputNombreAlmacen = "#nombreAlmacen";
const idInputDescripcionAlmacen = "#descripcionAlmacen";
const idInputDireccionAlmacen = "#direccionAlmacen";

const idTablaExistencias = "#tablaExistencias";

const nameContenedorDatos = "contenedorDatos";
const idContenedorEmplazamiento = "#contenedorEmplazamiento";
const idContenedorAlmacen = "#contenedorAlmacen";
const idContenedorExistencias = "#contenedorExistencias";

const nameBtnEmplazamiento = "btnEmplazamiento";
const nameBtnAlmacen = "btnAlmacen";
const nameBtnExistencias = "btnExistencias";

const idBtnCancelar = "#btnCancelar";
const nameBtnCancelar = "btnCancelar";

const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idBtnDetalla = "#btnDetalle";

let emplazamiento;

const User = {
    rol: ROLES.USER
};

$(document).ready(function () {
    const inputIdExistencia = document.querySelector(idInputIdEmplazamiento);
    const btnsEmplazamiento = document.querySelectorAll(`[name="${nameBtnEmplazamiento}"`);
    const btnsAlmacen = document.querySelectorAll(`[name="${nameBtnAlmacen}"`);
    const btnsExistencias = document.querySelectorAll(`[name="${nameBtnExistencias}"`);
    const btnsCancelar = document.querySelectorAll(`[name="${nameBtnCancelar}"]`); 
    const tablaExistencias = document.querySelector(idTablaExistencias);
    const btnCrear = document.querySelector(idBtnCrear);
    const btnModificar = document.querySelector(idBtnModificar);
    const btnEliminar = document.querySelector(idBtnEliminar);
    const btnDetalla = document.querySelector(idBtnDetalla);
    
    User.rol = document.querySelector(idInputUserRol).value;
  
    getEmplazamiento(inputIdExistencia.value);
    
    observeRowSelectedChange(tablaExistencias, onDetectarFilaSeleccionadaExistencias);
    
    btnsEmplazamiento.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorEmplazamiento);
        });
    });
    
    btnsAlmacen.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos,  idContenedorAlmacen);
        });
    });
    
    btnsExistencias.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorExistencias);
        });
    });
    
    btnsCancelar.forEach(btn => {
        btn.addEventListener('click', () => {
            const referrer = document.referrer;

            if (referrer && referrer.includes("existencias")) {
                // Si viene desde una página que contiene "existencias"
                window.location.href = "almacen/emplazamientos";
            } else if (referrer) {
                // Si hay referrer pero no contiene "existencias"
                window.location.href = referrer;
            } else {
                // Si no hay referrer (por ejemplo, acceso directo)
                window.location.href = "almacen/emplazamientos";
            }
        });
    });
    
    btnModificar.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); 
        window.location.href = (`almacen/existencias/edit/${idExistencia}`);
    });
    
    btnCrear.addEventListener('click', () => {
        window.location.href = (`almacen/existencias/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); 
        borrarExistencia(idExistencia);
    });
    
    btnDetalla.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); 
        window.location.href = (`almacen/existencias/detalle/${idExistencia}`);
    });
});

function onDetectarFilaSeleccionadaExistencias(hayFilaSeleccionada) {
    if(User.rol === ROLES.ADMIN) {
        $(idBtnEliminar).prop('disabled', !hayFilaSeleccionada);
        $(idBtnModificar).prop('disabled', !hayFilaSeleccionada);
    }
    $(idBtnDetalla).prop('disabled', !hayFilaSeleccionada);
}


function getEmplazamiento(idEmplazamiento) {
    solicitudGet(`api/emplazamiento/emplazamiento?idEmplazamiento=${idEmplazamiento}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    emplazamiento = response.data;
                    fillFielsEmplazamiento(emplazamiento);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsEmplazamiento(emplazamiento) {
    const div = document.querySelector(idContenedorEmplazamiento);
    const inputNombreEmplazamiento = div.querySelector(idInputNombreEmplazamiento);
    const inputDescripcionEmplazamiento = div.querySelector(idInputDescripcionEmplazamiento);
    const inputTipoEmplazamiento = div.querySelector(idInputTipoEmplazamiento);
    const inputTotalExistencias = div.querySelector(idInputTotalExistencias);
   
    const listaExistencias = emplazamiento.listaExistencias;
    
    const totalExistencias = listaExistencias.reduce((total, existencia) => {
            if(existencia.disponible === DISPONIBILIDAD.DISPONIBLE.name) {
                total++;
            }
            return total;
        }, 0);
    
    inputNombreEmplazamiento.value = emplazamiento.nombre;
    inputDescripcionEmplazamiento.value = emplazamiento.descripcion ?? "";
    inputTipoEmplazamiento.value = emplazamiento.tipoEmplazamiento.nombre;
    inputTotalExistencias.value = totalExistencias;
    
    fillFielsAlmacen(emplazamiento.almacen);
    rellenarTablaExistencias(listaExistencias);
}

function fillFielsAlmacen(almacen) {
    const div = document.querySelector(idContenedorAlmacen);
    const inputNombreAlmacen = div.querySelector(idInputNombreAlmacen);
    const inputDescripcionAlmacen = div.querySelector(idInputDescripcionAlmacen);
    const inputDireccionAlmacen = div.querySelector(idInputDireccionAlmacen);
    
    inputNombreAlmacen.value = almacen.nombre;
    inputDescripcionAlmacen.value = almacen.descripcion ?? "";
    inputDireccionAlmacen.value = formatearDireccion(almacen.direccion);

}

function formatearDireccion(direccion) {
    
    if (!direccion) return "";

    const calle = direccion.calle ?? "";
    const numero = direccion.numero ? ` ${direccion.numero}` : "";
    const localidad = direccion.localidad?.nombre ?? "";
    const provincia = direccion.localidad?.provincia?.nombre ?? "";
    const codigoPostal = direccion.codigoPostal ? ` c.p. ${direccion.codigoPostal},` : "";

    // Creamos una cadena legible
    const direccionCompleta = `${calle}${numero},${codigoPostal} ${localidad}, ${provincia}`.trim();

    // Eliminamos comas sobrantes o espacios si hay datos vacíos
    return direccionCompleta.replace(/(^, |, ,| ,$)/g, "").replace(/\s+,/g, ",");
}


/**
 * Función que rellena una tabla HTML con las existencias proporcionadas.
 * @param {Array} existencias - Un array de objetos de existencias que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaExistencias(existencias) {
    const tablaExistencias = document.querySelector(idTablaExistencias);
    const cuerpoTablaExistencias = tablaExistencias.querySelector('tbody');

    tablaExistencias.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = existencias.map(existencia => {
        return `<tr id="${existencia.id}">
                <td>${existencia.id}</td>
                <td>${existencia.articulo.nombre}</td>
                <td>${existencia.articulo.referencia}</td>
                <td>${existencia.articulo.marca.nombre}</td>
                <td>${existencia.sku || ''}</td>
                <td class="texto--centrado">${existencia.disponible === 'DISPONIBLE' ? ICONOS_TABLA.CHECK : ICONOS_TABLA.NO_CHECK}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaExistencias.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaExistencias);
}


function borrarExistencia(emplazamientoId) {
    mostrarMensajeOpcion("Borra Existencia", `¿Quieres realmente borrar la existencia con id ${emplazamientoId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/existencia/delete/${emplazamientoId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Existencia Borrar.", `Se ha borrado correctamente la existencia.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaExistencias);
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

