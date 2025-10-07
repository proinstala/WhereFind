
import { solicitudGet, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idSelectArticulo = "#articulo";
const idSelectArticuloProveedor = "#articuloProveedor";
const idSelectAlmacen = "#almacen";
const idSelectEmplazamiento = "#emplazamiento";

const idInputIdExistencia = "#existencia_id";
const idInputPrecio = "#precio";
const idInputFechaCompra = "#fechaCompra";
const idInputComprador = "#comprador";
const idFormExistencia = "#frmModificarExistencia";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosExistencia = "#btnDeshacerCambiosExistencia";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";

const fechaHoy = new Date().toISOString().split("T")[0];
let oldExistencia;

$(document).ready(function () {
    const selectArticulo = document.querySelector(idSelectArticulo);
    const selectArticuloProveedor = document.querySelector(idSelectArticuloProveedor);
    const selectAlmacen = document.querySelector(idSelectAlmacen);
    const selectEmplazamiento = document.querySelector(idSelectEmplazamiento);
    const inputIdExistencia = document.querySelector(idInputIdExistencia);
    
    //Imagen proveedor
    const contenedorImgArticulo = document.querySelector(idContenedorImgArticulo);
    const inputImgArticulo = document.querySelector(idInputImgArticulo);
    const imgArticulo = document.querySelector(idImgArticulo);
    const inputHideImgArticulo = document.querySelector(idInputHideImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const inputPrecio = document.querySelector(idInputPrecio);
    const inputFechaCompra = document.querySelector(idInputFechaCompra);
    
    const btnDeshacerCambiosExistencia = document.querySelector(idBtnDeshacerCambiosExistencia);
    const btnCancelar = document.querySelector(idBtnCancelar);


    //validarFormulario(idFormExistencia);

    getExistencia(inputIdExistencia.value);

    btnCancelar.addEventListener('click', () => {
        window.location.href = "almacen/existencias";
    });

    btnDeshacerCambiosExistencia.addEventListener('click', () => {
        fillFielsExistencia(oldArticulo);
    });

});

function onDetectarCambiosCrearExistencia(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosExistencia").prop('disabled', !hayCambios);
}

function getExistencia(idExistencia) {
    solicitudGet(`api/existencia/existencia?idExistencia=${idExistencia}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldExistencia = response.data;
                    console.log(oldExistencia);
                    fillFielsExistencia(oldExistencia);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


//HAY QUE MODIFICAR ESTO PARA RELLENAR LOS DATOS DE EXISTENCIA. -------!!!!! EN CONSTRUCCIÓN.
function fillFielsExistencia(existencia) {
    const form = document.querySelector(idFormArticulo);
    debugger;

    const selectMarca = form.querySelector(idSelectMarca);
    const inputNombre = form.querySelector(idInputNombre);
    const inputDescripcion = form.querySelector(idInputDescripcion);
    const inputReferencia = form.querySelector(idInputReferencia);
    const inputModelo = form.querySelector(idInputModelo);
    const inputStockMinimo = form.querySelector(idInputStockMinimo);
    
    const inputHideImgArticulo = form.querySelector(idInputHideImgArticulo);
    const imgArticulo = form.querySelector(idImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
   
    inputNombre.value = articulo.nombre;
    inputDescripcion.value = articulo.descripcion ?? "";
    inputReferencia.value = articulo.referencia ?? "";
    inputModelo.value = articulo.modelo ?? "";
    inputStockMinimo.value = articulo.stockMinimo ?? "";
    
    // Imagen: usa la imagen del articulo si existe, de lo contrario la imagen por defecto
    const imagenValida = articulo.imagen && articulo.imagen.trim() !== "";
    inputHideImgArticulo.value = imagenValida ? articulo.imagen : "";
    imgArticulo.src = imagenValida ? articulo.imagen : DEFAULT_IMG.ARTICULO;
    
    labelImgArticulo.textContent = "";
    
    const marcaId = articulo.marca.id;
    
    const promesaMarca = cargarInputSelect(selectMarca, `api/marca/marcas?direccion=${marcaId}`, 'Sin Marca', marcaId, () => {});
    
    Promise.all([promesaMarca])
        .then(() => {
            onDetectarCambiosModificarArticulo(false);
            detectarCambiosFormulario(idFormArticulo, onDetectarCambiosModificarArticulo);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });
    
}

