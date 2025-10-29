import { solicitudGet, solicitudPut, mostrarContenedor, addRowSelected, observeRowSelectedChange, deleteRowSelectedTable, fillInputSelect } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import { DEFAULT_IMG, DISPLAY_TYPES, DISPONIBILIDAD } from '../constantes.mjs';

const idInputIdExistencia = "#existencia_id";

const idInputNombreArticulo = "#nombreArticulo";
const idInputDescripcionArticulo = "#descripcionArticulo";
const idInputMarca = "#marca";
const idInputModelo = "#modelo";
const idInputReferencia = "#referencia";

const idInputPrecio = "#precio";
const idInputSku = "#sku";
const idInputFechaCompra = "#fechaCompra";
const idInputComprador = "#comprador";
const idInputDisponibilidad = "#disponibilidad";
const idInputFechaNoDisponible = "#fechaNoDisponible";
const idInputAnotacion = "#anotacion";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";

const idInputNombreAlmacen = "#nombreAlmacen";
const idInputDescripcionAlmacen = "#descripcionAlmacen";
const idInputDireccionAlmacen = "#direccionAlmacen";
const idInputNombreEmplazamiento = "#nombreEmplazamiento";
const idInputDescripcionEmplazamiento = "#descripcionEmplazamiento";
const idInputTipoEmplazamiento = "#tipoEmplazamiento";

const idInputNombreProveedor = "#nombreProveedor";
const idInputDescripcionProveedor = "#descripcionProveedor";
const idInputPaginaWeb = "#paginaWeb";

const idContenedorImgProveedor = "#contenedorImgProveedor";
const idInputHideImgProveedor = "#imagenProveedorB64"; //Input oculto.
const idInputImgProveedor = "#inputImgProveedor";
const idImgProveedor = "#imgProveedor";
const idLabelImgProveedor = "#textoImagenProveedor";

const nameContenedorDatos = "contenedorDatos";
const idContenedorExistencia = "#contenedorExistencia";
const idContenedorEmplazamiento = "#contenedorEmplazamiento";
const idContenedorProveedor = "#contenedorProveedor";
const nameBtnExistencia = "btnExistencia";
const nameBtnEmplazamiento = "btnEmplazamiento";
const nameBtnProveedor = "btnProveedor";
const idBtnCancelar = "#btnCancelar";
const nameBtnCancelar = "btnCancelar";

let existencia;

$(document).ready(function () {
    const inputIdExistencia = document.querySelector(idInputIdExistencia);
    const btnsExistencia = document.querySelectorAll(`[name="${nameBtnExistencia}"`);
    const btnsEmplazamiento = document.querySelectorAll(`[name="${nameBtnEmplazamiento}"`);
    const btnsProveedor = document.querySelectorAll(`[name="${nameBtnProveedor}"`);
    const btnsCancelar = document.querySelectorAll(`[name="${nameBtnCancelar}"]`); 
    
    getExistencia(inputIdExistencia.value);
    
    btnsExistencia.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorExistencia);
        });
    });
    
    btnsEmplazamiento.forEach(btn => {
        btn.addEventListener('click', (event) => {
            mostrarContenedor(event, nameContenedorDatos, idContenedorEmplazamiento);
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

function getExistencia(idExistencia) {
    solicitudGet(`api/existencia/existencia?idExistencia=${idExistencia}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    existencia = response.data;
                    fillFielsExistencia(existencia);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}

function fillFielsExistencia(existencia) {
    const div = document.querySelector(idContenedorExistencia);
    
    const inputNombreArticulo = div.querySelector(idInputNombreArticulo);
    const inputDescripcionArticulo = div.querySelector(idInputDescripcionArticulo);
    const inputMarca = div.querySelector(idInputMarca);
    const inputModelo = div.querySelector(idInputModelo);
    const inputReferencia = div.querySelector(idInputReferencia);
    const inputPrecio = div.querySelector(idInputPrecio);
    const inputSku = div.querySelector(idInputSku);
    const inputFechaCompra = div.querySelector(idInputFechaCompra);
    const inputComprador = div.querySelector(idInputComprador);
    const inputFechaNoDisponible = div.querySelector(idInputFechaNoDisponible);
    const inputAnotacion = div.querySelector(idInputAnotacion); 
    const inputDisponibilidad = div.querySelector(idInputDisponibilidad); 
    
    const inputHideImgArticulo = div.querySelector(idInputHideImgArticulo);
    const imgArticulo = div.querySelector(idImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const articulo = existencia.articulo;
    const proveedor = existencia.proveedor;
    const emplazamiento = existencia.emplazamiento;
   
    inputNombreArticulo.value = articulo.nombre;
    inputDescripcionArticulo.value = articulo.descripcion ?? "";
    inputMarca.value = articulo.marca.nombre ?? "";
    inputModelo.value = articulo.modelo ?? "";
    inputReferencia.value = articulo.referencia ?? "";
    
    inputPrecio.value = existencia.precio ?? "";
    inputSku.value = existencia.sku ?? "";
    inputFechaCompra.value = existencia.fechaCompra;
    inputComprador.value = existencia.comprador ?? ""; 
    inputDisponibilidad.value = existencia.disponible;
    inputFechaNoDisponible.value = existencia.fechaNoDisponible ?? "";
    inputAnotacion.value = existencia.anotacion ?? "";
    
    // Imagen: usa la imagen del proveedor si existe, de lo contrario la imagen por defecto
    const imagenValida = articulo.imagen && articulo.imagen.trim() !== "";
    inputHideImgArticulo.value = imagenValida ? articulo.imagen : "";
    imgArticulo.src = imagenValida ?  articulo.imagen : DEFAULT_IMG.ARTICULO;
    
    labelImgArticulo.textContent = "";
    
    if(existencia.disponible === DISPONIBILIDAD.DISPONIBLE.name) {
        inputDisponibilidad.classList.add('disponible');
    } else {
        inputDisponibilidad.classList.add('no_disponible');
    }
    
    fillFielsEmplazamiento(emplazamiento);
    fillFielsProveedor(proveedor);
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

function fillFielsProveedor(proveedor) {
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