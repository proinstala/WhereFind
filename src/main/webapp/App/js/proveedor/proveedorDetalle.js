
import { solicitudGet, solicitudPut, mostrarContenedor, addRowSelected, observeRowSelectedChange, deleteRowSelectedTable, fillInputSelect } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG, DISPLAY_TYPES} from '../constantes.mjs';

const idInputIdProveedor = "#proveedor_id";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";
const idInputPaginaWeb = "#paginaWeb";

const idInputCalle = "#calle";
const idInputNumero = "#numero";
const idInputCodigoPostal = "#codigoPostal";
const idInputLocalidad = "#localidad";
const idInputProvincia = "#provincia";

const idTablaContactos = "#tablaContactos";

const nameContenedorDatos = "contenedorDatos";
const idContenedorProveedor = "#contenedorProveedor";
const idContenedorDireccion = "#contenedorDireccion";
const idContenedroContacto = "#contenedorContacto";
const nameBtnProveedor = "btnProveedor";
const nameBtnDireccion = "btnDireccion";
const nameBtnContactos = "btnContactos";
const idBtnCancelar = "#btnCancelar";
const nameBtnCancelar = "btnCancelar";

const idContenedorImgProveedor = "#contenedorImgProveedor";
const idInputHideImgProveedor = "#imagenProveedorB64"; //Input oculto.
const idInputImgProveedor = "#inputImgProveedor";
const idImgProveedor = "#imgProveedor";
const idLabelImgProveedor = "#textoImagenProveedor";

const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";

let proveedor;

$(document).ready(function () {
    const inputIdProveedor = document.querySelector(idInputIdProveedor);
    const btnsProveedor = document.querySelectorAll(`[name="${nameBtnProveedor}"`);
    const btnsDireccion = document.querySelectorAll(`[name="${nameBtnDireccion}"`);
    const btnsContactos = document.querySelectorAll(`[name="${nameBtnContactos}"`);
    const btnsCancelar = document.querySelectorAll(`[name="${nameBtnCancelar}"]`); 
    const tablaContactos = document.querySelector(idTablaContactos);
    const btnCrear = document.querySelector(idBtnCrear);
    const btnModificar = document.querySelector(idBtnModificar);
    const btnEliminar = document.querySelector(idBtnEliminar);
    
    
    //Imagen proveedor
    const contenedorImgProveedor = document.querySelector(idContenedorImgProveedor);
    const inputImgProveedor = document.querySelector(idInputImgProveedor);
    const imgProveedor = document.querySelector(idImgProveedor);
    const inputHideImgProveedor = document.querySelector(idInputHideImgProveedor);
    const labelImgProveedor = document.querySelector(idLabelImgProveedor);
  
    getProveedor(inputIdProveedor.value);
    
    observeRowSelectedChange(tablaContactos, onDetectarFilaSeleccionadaConactos);
    
    btnsProveedor.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos,  idContenedorProveedor);
        });
    });
    
    btnsDireccion.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorDireccion);
        });
    });
    
    btnsContactos.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedroContacto);
        });
    });
    
    btnsCancelar.forEach(btn => {
        btn.addEventListener('click', () => {
            window.location.href = "proveedor/proveedores";
        });
    });
    
    btnModificar.addEventListener('click', () => {
        const idContacto = tablaContactos.getAttribute('data-rowselected'); 
        window.location.href = (`proveedor/contactos/edit/${idContacto}`);
    });
    
    btnCrear.addEventListener('click', () => {
        window.location.href = (`proveedor/contactos/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        const idContacto = tablaContactos.getAttribute('data-rowselected'); 
        borrarContacto(idContacto);
    });
});

function onDetectarFilaSeleccionadaConactos(hayFilaSeleccionada) {
    $(idBtnEliminar).prop('disabled', !hayFilaSeleccionada);
    $(idBtnModificar).prop('disabled', !hayFilaSeleccionada);
}


function getProveedor(idProveedor) {
    solicitudGet(`api/proveedor/proveedor?idProveedor=${idProveedor}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    proveedor = response.data;
                    fillFielsProveedor(proveedor);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsProveedor(proveedor) {
    //proveedor
    const div = document.querySelector(idContenedorProveedor);
    const inputNombre = div.querySelector(idInputNombre);
    const inputDescripcion = div.querySelector(idInputDescripcion);
    const inputPaginaWeb = div.querySelector(idInputPaginaWeb);
    const inputHideImgProveedor = div.querySelector(idInputHideImgProveedor);
    const imgProveedor = div.querySelector(idImgProveedor);
    const labelImgProveedor = document.querySelector(idLabelImgProveedor);
   
    inputNombre.value = proveedor.nombre;
    inputDescripcion.value = proveedor.descripcion ?? "";
    inputPaginaWeb.value = proveedor.paginaWeb ?? "";
    
    // Imagen: usa la imagen del proveedor si existe, de lo contrario la imagen por defecto
    const imagenValida = proveedor.imagen && proveedor.imagen.trim() !== "";
    inputHideImgProveedor.value = imagenValida ? proveedor.imagen : "";
    imgProveedor.src = imagenValida ? proveedor.imagen : DEFAULT_IMG.PROVEEDOR;
    
    labelImgProveedor.textContent = "";
    
    //direccion
    const direccionId = proveedor.direccion.id;
    const divDireccion = document.querySelector(idContenedorDireccion);
    const inputCalle = divDireccion.querySelector(idInputCalle);
    const inputNumero = divDireccion.querySelector(idInputNumero);
    const inputCodigoPostal = divDireccion.querySelector(idInputCodigoPostal);
    const inputProvincia = divDireccion.querySelector(idInputProvincia);
    const inputLocalidad = divDireccion.querySelector(idInputLocalidad);

    inputCalle.value = proveedor.direccion.calle ?? "";
    inputNumero.value = proveedor.direccion.numero ?? "";
    inputLocalidad.value = proveedor.direccion.localidad.nombre ?? "";
    inputProvincia.value = proveedor.direccion.localidad.provincia.nombre ?? "";
    if(proveedor.direccion.codigoPostal) {
        inputCodigoPostal.value = proveedor.direccion.codigoPostal ?? "";
    }
   
    rellenarTablaContactos(proveedor.listaContactos);
}


/**
 * Función que rellena una tabla HTML con los contactos proporcionadas.
 * @param {Array} contactos - Un array de objetos de contactos que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaContactos(contactos) {
    const tablaContactos = document.querySelector(idTablaContactos);
    const cuerpoTablaContactos = tablaContactos.querySelector('tbody');
    //const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

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

