import { solicitudGet, solicitudPut, mostrarContenedor, addRowSelected, observeRowSelectedChange, deleteRowSelectedTable, fillInputSelect } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import { DEFAULT_IMG, DISPLAY_TYPES, DISPONIBILIDAD, ROLES, ICONOS_TABLA } from '../constantes.mjs';

const idInputUserRol = "#userRol";

const idInputIdArticulo = "#articulo_id";

const idInputNombreArticulo = "#nombreArticulo";
const idInputDescripcionArticulo = "#descripcionArticulo";
const idInputMarca = "#marca";
const idInputModelo = "#modelo";
const idInputReferencia = "#referencia";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";

const nameContenedorDatos = "contenedorDatos";
const idContenedorArticulo = "#contenedorArticulo";
const idContenedorExistencia = "#contenedorExistencia";
const idContenedorProveedor = "#contenedorProveedor";

const nameBtnArticulo = "btnArticulo";
const nameBtnExistencia = "btnExistencia";
const nameBtnProveedor = "btnProveedor";
const idBtnCancelar = "#btnCancelar";
const nameBtnCancelar = "btnCancelar";

const idTablaExistencias = "#tablaExistencias";
const idBtnModificarExistencia = "#btnModificarExistencia";
const idBtnCrearExistencia = "#btnCrearExistencia";
const idBtnEliminarExistencia = "#btnEliminarExistencia";
const idBtnDetallaExistencia = "#btnDetalleExistencia";

const idTablaProveedores = "#tablaProveedores";
const idBtnModificarProveedor = "#btnModificarProveedor";
const idBtnCrearProveedor = "#btnCrearProveedor";
const idBtnEliminarProveedor = "#btnEliminarProveedor";


let articulo;
const User = {rol: ROLES.USER};

$(document).ready(function () {
    const inputIdArticulo = document.querySelector(idInputIdArticulo);
    const btnsArticulo = document.querySelectorAll(`[name="${nameBtnArticulo}"`);
    const btnsExistencia = document.querySelectorAll(`[name="${nameBtnExistencia}"`);
    const btnsProveedor = document.querySelectorAll(`[name="${nameBtnProveedor}"`);
    const btnsCancelar = document.querySelectorAll(`[name="${nameBtnCancelar}"]`); 
    
    const tablaExistencias = document.querySelector(idTablaExistencias);
    const btnCrearExistencia = document.querySelector(idBtnCrearExistencia);
    const btnModificarExistencia = document.querySelector(idBtnModificarExistencia);
    const btnEliminarExistencia = document.querySelector(idBtnEliminarExistencia);
    const btnDetallaExistencia = document.querySelector(idBtnDetallaExistencia);
    
    const tablaProveedores = document.querySelector(idTablaProveedores);
    const btnCrearProveedor = document.querySelector(idBtnCrearProveedor);
    const btnModificarProveedor = document.querySelector(idBtnModificarProveedor);
    const btnEliminarProveedor = document.querySelector(idBtnEliminarProveedor);
    
    User.rol = document.querySelector(idInputUserRol).value;
    
    getArticulo(inputIdArticulo.value);
    
    observeRowSelectedChange(tablaExistencias, onDetectarFilaSeleccionadaExistencias);
    
    observeRowSelectedChange(tablaProveedores, onDetectarFilaSeleccionadaProveedores);
    
    btnsArticulo.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorArticulo);
        });
    });
    
    btnsExistencia.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorExistencia);
        });
    });
    
    btnsProveedor.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos,  idContenedorProveedor);
        });
    });
    
    btnsCancelar.forEach(btn => {
        const referrer = document.referrer;
        btn.addEventListener('click', () => {
            if (referrer && referrer.includes("existencias")) {
                window.location.href = "articulo/articulos";
            } else if (referrer && referrer.includes("articuloProveedorAsociar")) {
                window.location.href = "articulo/articulos";
            } else if (referrer && referrer.includes("articuloProveedorEditar")) {
                window.location.href = "articulo/articulos";
            } else if (document.referrer) {
                window.location.href = document.referrer;
            } else {
                // Fallback: vuelve a una página por defecto
                window.location.href = "articulo/articulos";
            }
        });
    });
    
    btnModificarExistencia.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); 
        window.location.href = (`almacen/existencias/edit/${idExistencia}`);
    });
    
    btnCrearExistencia.addEventListener('click', () => {
        //window.location.href = (`almacen/existencias/crear`);
        window.location.href = (`almacen/existencias/crear/${inputIdArticulo.value}`);
    });

    btnEliminarExistencia.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); 
        borrarExistencia(idExistencia);
    });
    
    btnDetallaExistencia.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); 
        window.location.href = (`almacen/existencias/detalle/${idExistencia}`);
    });
    
    btnModificarProveedor.addEventListener('click', () => {
        const idArticuloProveedor = tablaProveedores.getAttribute('data-rowselected'); 
        window.location.href = (`articulo/articulos/articuloProveedorEditar/${idArticuloProveedor}`);
    });
    
    btnCrearProveedor.addEventListener('click', () => {
        //window.location.href = (`almacen/existencias/crear`);
        window.location.href = (`articulo/articulos/articuloProveedorAsociar/${inputIdArticulo.value}`);
    });

    btnEliminarProveedor.addEventListener('click', () => {
        const idArticuloProveedor = tablaProveedores.getAttribute('data-rowselected'); 
        borrarArticuloProveedor(idArticuloProveedor);
    });
  
});

function onDetectarFilaSeleccionadaProveedores(hayFilaSeleccionada) {
    if(User.rol === ROLES.ADMIN) {
        $(idBtnEliminarProveedor).prop('disabled', !hayFilaSeleccionada);
        $(idBtnModificarProveedor).prop('disabled', !hayFilaSeleccionada);
    }
}

function onDetectarFilaSeleccionadaExistencias(hayFilaSeleccionada) {
    $(idBtnEliminarExistencia).prop('disabled', !hayFilaSeleccionada);
    $(idBtnModificarExistencia).prop('disabled', !hayFilaSeleccionada);
    $(idBtnDetallaExistencia).prop('disabled', !hayFilaSeleccionada);
}

function getArticulo(idArticulo) {
    solicitudGet(`api/articulo/articulo?idArticulo=${idArticulo}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    articulo = response.data;
                    fillFielsArticulo(articulo);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}

function getExistencias(idArticulo) {
    return solicitudGet(`api/existencia/find_existencias_articulo?idArticulo=${idArticulo}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                    return [];
                } else {
                    const listaExistencias = response.data;
                    console.log(listaExistencias);
                    return listaExistencias;
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}

function fillFielsArticulo(articulo) {
    const div = document.querySelector(idContenedorArticulo);
    
    const inputNombreArticulo = div.querySelector(idInputNombreArticulo);
    const inputDescripcionArticulo = div.querySelector(idInputDescripcionArticulo);
    const inputMarca = div.querySelector(idInputMarca);
    const inputModelo = div.querySelector(idInputModelo);
    const inputReferencia = div.querySelector(idInputReferencia);
    
    const inputHideImgArticulo = div.querySelector(idInputHideImgArticulo);
    const imgArticulo = div.querySelector(idImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const listaArticuloProveedores = articulo.listaProveedores;
   
    inputNombreArticulo.value = articulo.nombre;
    inputDescripcionArticulo.value = articulo.descripcion ?? "";
    inputMarca.value = articulo.marca.nombre ?? "";
    inputModelo.value = articulo.modelo ?? "";
    inputReferencia.value = articulo.referencia ?? "";
    
    // Imagen: usa la imagen del proveedor si existe, de lo contrario la imagen por defecto
    const imagenValida = articulo.imagen && articulo.imagen.trim() !== "";
    inputHideImgArticulo.value = imagenValida ? articulo.imagen : "";
    imgArticulo.src = imagenValida ?  articulo.imagen : DEFAULT_IMG.ARTICULO;
    
    labelImgArticulo.textContent = "";
    
    rellenarTablaExistencia(articulo.id);
    rellenarTablaProveedor(listaArticuloProveedores);
}


/**
 * Función que rellena una tabla HTML con los proveedores proporcionadas.
 * @param {Array} articuloProveedores - Un array de objetos de articuloProveedores que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaProveedor(articuloProveedores) {
    const tablaProveedores = document.querySelector(idTablaProveedores);
    const cuerpoTablaProveedores = tablaProveedores.querySelector('tbody');
    console.log(articuloProveedores);
    tablaProveedores.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = articuloProveedores.map(articuloProveedor => {
        const proveedor = articuloProveedor.proveedor;
        return `<tr id="${articuloProveedor.id}">
                <td>${proveedor.nombre}</td>
                <td>${articuloProveedor.precio}</td>
                <td class="texto--centrado">${articuloProveedor.fechaPrecio}</td>
                <td class="texto--centrado ${articuloProveedor.disponible === 'DISPONIBLE' ? 'disponible' : 'no_disponible'}">${articuloProveedor.disponible === 'DISPONIBLE' ? ICONOS_TABLA.CHECK : ICONOS_TABLA.NO_CHECK}</td>
                </tr>`;
    }).join('');

    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaProveedores.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaProveedores);
}

async function rellenarTablaExistencia(articuloId) {
    const existencias = await getExistencias(articuloId);

    const tablaExistencias = document.querySelector(idTablaExistencias);
    const cuerpoTablaExistencias = tablaExistencias.querySelector('tbody');

    tablaExistencias.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = existencias.map(existencia => {
        
        return `<tr id="${existencia.id}">
                <td>${existencia.sku || ""}</td>
                <td>${existencia.proveedor.nombre || ""}</td>
                <td>${existencia.precio}</td>
                <td class="texto--centrado">${existencia.fechaCompra}</td>
                <td>${existencia.emplazamiento.almacen.nombre}</td>
                <td>${existencia.emplazamiento.nombre}</td>
                <td>${existencia.anotacion || ""}</td>
                <td class="texto--centrado ${existencia.disponible === 'DISPONIBLE' ? 'disponible' : 'no_disponible'}">${existencia.disponible === 'DISPONIBLE' ? ICONOS_TABLA.CHECK : ICONOS_TABLA.NO_CHECK}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaExistencias.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaExistencias);
}

function borrarExistencia(idExistencia) {
    mostrarMensajeOpcion("Borrar Existencia", `¿Quieres realmente borrar la existencia con id ${idExistencia}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/existencia/delete/${idExistencia}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Borrar Existencia.", `Se ha borrado correctamente la existencia.`, "success");

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

function borrarArticuloProveedor(idArticuloProveedor) {
    mostrarMensajeOpcion("Borrar Asociación", `¿Quieres realmente borrar la asociación con id ${idArticuloProveedor}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/articulo/proveedor_delete/${idArticuloProveedor}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Borrar Ascociación Proveedor.", `Se ha borrado correctamente la asociación.`, "success");

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