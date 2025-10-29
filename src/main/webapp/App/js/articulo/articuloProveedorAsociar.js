
import { solicitudGet, solicitudPost, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idInputIdArticulo = "#articulo_id";

const idInputArticulo = "#articulo";
const idInputDescripcionArticulo = "#descripcionArticulo";
const idSelectProveedor = "#proveedor";
const idInputPrecio = "#precio";
const idInputFechaPrecio = "#fechaPrecio";
const idFormArticuloProveedor = "#frmCrearArticuloProveedor";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosArticuloProveedor = "#btnDeshacerCambiosArticuloProveedor";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";

let articulo;
let listaAllProveedores;

$(document).ready(function () {
    const inputIdArticulo = document.querySelector(idInputIdArticulo);
    const selectProveedor = document.querySelector(idSelectProveedor);
    const inputPrecio = document.querySelector(idInputPrecio);
    const inputFechaPrecio = document.querySelector(idInputFechaPrecio);
    
    //Imagen articulo
    const contenedorImgArticulo = document.querySelector(idContenedorImgArticulo);
    const inputImgArticulo = document.querySelector(idInputImgArticulo);
    const imgArticulo = document.querySelector(idImgArticulo);
    const inputHideImgArticulo = document.querySelector(idInputHideImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const btnDeshacerCambiosArticuloProveedor = document.querySelector(idBtnDeshacerCambiosArticuloProveedor);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormArticuloProveedor);

    const promesaArticulo = getArticulo(inputIdArticulo.value);
    const promesaProveedores = getProveedores();

    btnDeshacerCambiosArticuloProveedor.addEventListener('click', () => {
        //fillFielsArticulo(oldArticulo);
        seleccionarValorSelect(selectProveedor, "-1");
        inputPrecio.value = "";
        inputFechaPrecio.value = "";
        onDetectarCambiosCrearArticuloProveedor(false);
        detectarCambiosFormulario(idFormArticuloProveedor, onDetectarCambiosCrearArticuloProveedor);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = obtenerReferenciaRedireccionar();
    });
    
    Promise.all([promesaArticulo, promesaProveedores])
        .then(() => {
            fillFielsProveedor(articulo.listaProveedores, listaAllProveedores);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });
});

function onDetectarCambiosCrearArticuloProveedor(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosArticuloProveedor).prop('disabled', !hayCambios);
}

function obtenerReferenciaRedireccionar() {
    if (document.referrer) {
        return window.location.href = document.referrer;
    } else {
        // Fallback: vuelve a una página por defecto
        return window.location.href = "articulo/articulos";
    }
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            precio: {
                min: 0,
                max: 2000000000
            },
            proveedor: {
                min: 1,
                max: 2000000000
            }
        },//Fin de reglas ----------------
        messages: {
            precio: {
                min: "No puede introducir numeros negativos.",
                max: "Valor introducido no válido."
            },
            proveedor: {
                min: "Seleccione un proveedor.",
                max: "Valor introducido no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const articuloProveedorJSON = getArticuloProveedorJson();
            console.log(articuloProveedorJSON);
            debugger;
            solicitudPost(`api/articulo/proveedor_create`, idForm, true, articuloProveedorJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = obtenerReferenciaRedireccionar();
                            mostrarMensaje("Proveedor Asociado.", `Se ha asociado el proveedor con id "${response.data.id}" correctamente`, "success", redireccionar);
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

function getArticulo(idArticulo) {
    return solicitudGet(`api/articulo/articulo?idArticulo=${idArticulo}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                    return Promise.reject(response.result);
                } else {
                    articulo = response.data;
                    fillFielsArticulo(articulo);
                    return articulo; // Devuelve el artículo como resultado de la promesa
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                return Promise.reject(error);
            });
}

function getProveedores() {
    return solicitudGet(`api/proveedor/proveedores`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                    return Promise.reject(response.result);
                } else {
                    listaAllProveedores = response.data;
                    return listaAllProveedores; // Devuelve la lista como resultado de la promesa
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                return Promise.reject(error);
            });
}

function fillFielsArticulo(articulo) {
    const form = document.querySelector(idFormArticuloProveedor);
    
    const inputArticulo = form.querySelector(idInputArticulo);
    const inputDescripcion = form.querySelector(idInputDescripcionArticulo);  
    
    const inputHideImgArticulo = form.querySelector(idInputHideImgArticulo);
    const imgArticulo = form.querySelector(idImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const referencia = articulo.referencia ? " - [Ref: " + articulo.referencia + "]": "";
    const marca = articulo.marca?.nombre ? " - [Marca: " + articulo.marca.nombre + "]": "";
    const modelo = articulo.modelo ? " - [Mod: " + articulo.modelo + "]": "";
    
    inputArticulo.value = `${articulo.nombre}${referencia}${marca}${modelo}`;
    inputDescripcion.value = articulo.descripcion ?? "";
    
    // Imagen: usa la imagen del articulo si existe, de lo contrario la imagen por defecto
    const imagenValida = articulo.imagen && articulo.imagen.trim() !== "";
    inputHideImgArticulo.value = imagenValida ? articulo.imagen : "";
    imgArticulo.src = imagenValida ? articulo.imagen : DEFAULT_IMG.ARTICULO;
    
    labelImgArticulo.textContent = "";
    
    
    console.log(articulo);
}

function fillFielsProveedor(listaArticuloProveedores, proveedores) {
    console.log(proveedores);
    
    const listaProveedoresFiltrada = proveedores.filter(p => {
        return !listaArticuloProveedores.some(artProveedor => artProveedor.proveedor.id === p.id);
    });
    
    console.log(listaProveedoresFiltrada);
    
    const selectProveedor = document.querySelector(idSelectProveedor);
    
    fillInputSelect(selectProveedor, listaProveedoresFiltrada, 'Seleccione un proveedor');

    onDetectarCambiosCrearArticuloProveedor(false);
    detectarCambiosFormulario(idFormArticuloProveedor, onDetectarCambiosCrearArticuloProveedor);
}


function getArticuloProveedorJson() {
    const inputIdArticulo = document.querySelector(idInputIdArticulo);
    const selectProveedor = document.querySelector(idSelectProveedor);
    const inputPrecio = document.querySelector(idInputPrecio);
    const inputFechaPrecio = document.querySelector(idInputFechaPrecio);
    
    // Obtener el valor seleccionado
    const proveedorId = parseInt(selectProveedor.value, 10);

    const articuloProveedorJSON = {
        id: "0",
        precio: inputPrecio.value.trim() || 0,
        fechaPrecio: inputFechaPrecio.value.trim(),
        disponible: true,
        articulo: {
            id: inputIdArticulo.value
        },
        proveedor: {
            id: selectProveedor.value 
        }
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        articuloProveedorJSON: JSON.stringify(articuloProveedorJSON)
    }).toString();

    return data;
}
