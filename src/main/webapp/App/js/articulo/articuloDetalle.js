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

const idTablaProveedores = "#tablaProveedores";
const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idBtnDetalla = "#btnDetalle";

const idTablaExistencias = "#tablaExistencias";
const idBtnModificarExistencia = "#btnModificarExistencia";
const idBtnCrearExistencia = "#btnCrearExistencia";
const idBtnEliminarExistencia = "#btnEliminarExistencia";
const idBtnDetallaExistencia = "#btnDetalleExistencia";

let articulo;
const User = {rol: ROLES.USER};

$(document).ready(function () {
    const inputIdArticulo = document.querySelector(idInputIdArticulo);
    const btnsArticulo = document.querySelectorAll(`[name="${nameBtnArticulo}"`);
    const btnsExistencia = document.querySelectorAll(`[name="${nameBtnExistencia}"`);
    const btnsProveedor = document.querySelectorAll(`[name="${nameBtnProveedor}"`);
    const btnsCancelar = document.querySelectorAll(`[name="${nameBtnCancelar}"]`); 
    const tablaProveedores = document.querySelector(idTablaProveedores);
    const btnCrear = document.querySelector(idBtnCrear);
    const btnModificar = document.querySelector(idBtnModificar);
    const btnEliminar = document.querySelector(idBtnEliminar);
    const btnDetalla = document.querySelector(idBtnDetalla);
    
    User.rol = document.querySelector(idInputUserRol).value;
    
    getArticulo(inputIdArticulo.value);
    
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
        btn.addEventListener('click', () => {
            if (document.referrer) {
                window.location.href = document.referrer;
            } else {
                // Fallback: vuelve a una página por defecto
                window.location.href = "almacen/existencias";
            }
        });
    });
  
});

function onDetectarFilaSeleccionadaProveedores(hayFilaSeleccionada) {
    if(User.rol === ROLES.ADMIN) {
        $(idBtnEliminar).prop('disabled', !hayFilaSeleccionada);
        $(idBtnModificar).prop('disabled', !hayFilaSeleccionada);
    }
    $(idBtnDetalla).prop('disabled', !hayFilaSeleccionada);
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
    
    //fillFielsEmplazamiento(emplazamiento);
    rellenarTablaProveedor(listaArticuloProveedores);
}

function fillFielsEmplazamiento(emplazamiento) {
    const div = document.querySelector(idContenedorEmplazamiento);

    const inputNombreAlmacen = div.querySelector(idInputNombreAlmacen);
    const inputDescripcionAlmacen = div.querySelector(idInputDescripcionAlmacen);
    const inputDireccionAlmacen = div.querySelector(idInputDireccionAlmacen);
    const inputNombreEmplazamiento = div.querySelector(idInputNombreEmplazamiento);
    const inputDescripcionEmplazamiento = div.querySelector(idInputDescripcionEmplazamiento);
    const inputTipoEmplazamiento = div.querySelector(idInputTipoEmplazamiento);
    
    inputNombreAlmacen.value = emplazamiento.almacen.nombre;
    inputDescripcionAlmacen.value = emplazamiento.almacen.descripcion;
    const direccion = emplazamiento.almacen.direccion;
    if(direccion && direccion.id > 0) {
        let cp = '';
        if(direccion.codigoPostal) {
            cp = `C.P. ${direccion.codigoPostal}, `;
        }
        inputDireccionAlmacen.value = `[Id: ${direccion.id}] ${direccion.calle}, ${cp}Nº ${direccion.numero}, ${direccion.localidad.nombre} (${direccion.localidad.provincia.nombre})`;
    } 
    
    inputNombreEmplazamiento.value = emplazamiento.nombre;
    inputDescripcionEmplazamiento.value = emplazamiento.descripcion;
    inputTipoEmplazamiento.value = emplazamiento.tipoEmplazamiento.nombre;

}

function fillTablaProveedor(proveedor) {
    const div = document.querySelector(idContenedorProveedor);
    
    const inputNombre = div.querySelector(idInputNombreProveedor);
    const inputDescripcion = div.querySelector(idInputDescripcionProveedor);
    const inputPaginaWeb = div.querySelector(idInputPaginaWeb);
    const inputHideImgProveedor = div.querySelector(idInputHideImgProveedor);
    const imgProveedor = div.querySelector(idImgProveedor);
    const labelImgProveedor = document.querySelector(idLabelImgProveedor);
    
    inputNombre.value = proveedor.nombre ?? "";
    inputDescripcion.value = proveedor.descripcion ?? "";
    inputPaginaWeb.value = proveedor.paginaWeb ?? "";
    
    // Imagen: usa la imagen del proveedor si existe, de lo contrario la imagen por defecto
    const imagenValida = proveedor.imagen && proveedor.imagen.trim() !== "";
    inputHideImgProveedor.value = imagenValida ? proveedor.imagen : "";
    imgProveedor.src = imagenValida ? proveedor.imagen : DEFAULT_IMG.PROVEEDOR;
    
    labelImgProveedor.textContent = "";
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
                <td>${articuloProveedor.fechaPrecio}</td>
                <td class="texto--centrado">${articuloProveedor.disponible === 'DISPONIBLE' ? ICONOS_TABLA.CHECK : ICONOS_TABLA.NO_CHECK}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaProveedores.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaProveedores);
}