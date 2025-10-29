
import { solicitudGet, solicitudPost, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import { DEFAULT_IMG, DISPONIBILIDAD } from '../constantes.mjs';

const idInputIdArticuloProveedor = "#articuloProveedor_id";

const idInputArticulo = "#articulo";
const idInputDescripcionArticulo = "#descripcionArticulo";
const idInputProveedor = "#proveedor";
const idInputDescripcionProveedor = "#descripcionProveedor";
const idInputPrecio = "#precio";
const idInputFechaPrecio = "#fechaPrecio";
const idSelectDisponibilidad = "#disponibilidad";
const idInputFechaNoDisponible = "#fechaNoDisponible";
const idFormArticuloProveedor = "#frmEditarArticuloProveedor";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosArticuloProveedor = "#btnDeshacerCambiosArticuloProveedor";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";

const idContenedorImgProveedor = "#contenedorImgProveedor";
const idInputHideImgProveedor = "#imagenProveedorB64"; //Input oculto.
const idInputImgProveedor = "#inputImgProveedor";
const idImgProveedor = "#imgProveedor";
const idLabelImgProveedor = "#textoImagenProveedor";

const fechaHoy = new Date().toISOString().split("T")[0];
let oldArticuloProveedor;

$(document).ready(function () {
    const inputIdArticuloProveedor = document.querySelector(idInputIdArticuloProveedor);
    
    const btnDeshacerCambiosArticuloProveedor = document.querySelector(idBtnDeshacerCambiosArticuloProveedor);
    const btnCancelar = document.querySelector(idBtnCancelar);

    definicionReglaFechas();
    validarFormulario(idFormArticuloProveedor);

    getArticuloProveedor(inputIdArticuloProveedor.value);

    btnDeshacerCambiosArticuloProveedor.addEventListener('click', () => {
        fillFielsArticuloProveedor(oldArticuloProveedor);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = obtenerReferenciaRedireccionar();
    });
    
});

function onDetectarCambiosEditarArticuloProveedor(hayCambios) {
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

function definicionReglaFechas() {
    // --- Definir regla personalizada ---
    $.validator.addMethod("fechaPosteriorA", function(value, element, params) {
        const fechaPrecio = $(params).val();
        if (!value || !fechaPrecio) return true; // Si alguno está vacío, no bloquea
        return new Date(value) >= new Date(fechaPrecio);
    }, "Fecha anterio a fecha precio.");
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            precio: {
                min: 0,
                max: 2000000000
            },
            fechaPrecio: {
                date: true
            },
            fechaNoDisponible: {
                required: false,
                date: true,
                fechaPosteriorA: "#fechaPrecio"
            },
            disponibilidad: {
                required: true,
                min: 0,
                max: 1
            }
        },//Fin de reglas ----------------
        messages: {
            precio: {
                min: "No puede introducir numeros negativos.",
                max: "Valor introducido no válido."
            },
            fechaPrecio: {
                date: "Debe introducir una fecha válida."
            },
            fechaNoDisponible: {
                date: "Debe introducir una fecha válida.",
                min: "Fecha anterio a fecha precio.",
                fechaPosteriorA: "Fecha anterio a fecha precio."
            },
            disponibilidad: {
                required: "Debe indicar si la asociacion está disponible.",
                min: "Valor no válido.",
                max: "Valor no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Artículo Proveedor", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const articuloProveedorIdInput = document.querySelector(idInputIdArticuloProveedor);
                            const articuloProveedorId = articuloProveedorIdInput ? articuloProveedorIdInput.value : -1;

                            solicitudPut(`api/articulo/proveedor_update/${articuloProveedorId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado artículo proveedor.", `Se han modificado correctamente los datos del artículo proveedor ${response.data.id}`, "success");
                                            oldArticuloProveedor = response.data;
                                            onDetectarCambiosEditarArticuloProveedor(false);
                                            detectarCambiosFormulario(idFormArticuloProveedor, onDetectarCambiosEditarArticuloProveedor);
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
        },
        //Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}

function getArticuloProveedor(idArticuloProveedor) {
    solicitudGet(`api/articulo/proveedor?idArticuloProveedor=${idArticuloProveedor}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                    return Promise.reject(response.result);
                } else {
                    oldArticuloProveedor = response.data;
                    fillFielsArticuloProveedor(oldArticuloProveedor);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                return Promise.reject(error);
            });
}


function fillFielsArticuloProveedor(articuloProveedor) {
    const form = document.querySelector(idFormArticuloProveedor);
    
    const articulo = articuloProveedor.articulo;
    
    const inputArticulo = form.querySelector(idInputArticulo);
    const inputDescripcionArticulo = form.querySelector(idInputDescripcionArticulo);  
    const inputHideImgArticulo = form.querySelector(idInputHideImgArticulo);
    const imgArticulo = form.querySelector(idImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const referencia = articulo.referencia ? " - [Ref: " + articulo.referencia + "]": "";
    const marca = articulo.marca?.nombre ? " - [Marca: " + articulo.marca.nombre + "]": "";
    const modelo = articulo.modelo ? " - [Mod: " + articulo.modelo + "]": "";
    
    inputArticulo.value = `${articulo.nombre}${referencia}${marca}${modelo}`;
    inputDescripcionArticulo.value = articulo.descripcion ?? "";
    
    // Imagen: usa la imagen del articulo si existe, de lo contrario la imagen por defecto
    const imagenValidaArticulo = articulo.imagen && articulo.imagen.trim() !== "";
    inputHideImgArticulo.value = imagenValidaArticulo ? articulo.imagen : "";
    imgArticulo.src = imagenValidaArticulo ? articulo.imagen : DEFAULT_IMG.ARTICULO;
    labelImgArticulo.textContent = "Imagen Artículo";
    
    const proveedor = articuloProveedor.proveedor;
    
    const inputProveedor = form.querySelector(idInputProveedor);
    const inputDescripcionProveedor = form.querySelector(idInputDescripcionProveedor);
    const inputPrecio = form.querySelector(idInputPrecio);"#precio";
    const inputFechaPrecio = form.querySelector(idInputFechaPrecio);"#fechaPrecio";
    const selectDisponibilidad = form.querySelector(idSelectDisponibilidad);"#disponibilidad";
    const inputFechaNoDisponible = form.querySelector(idInputFechaNoDisponible);"#fechaNoDisponible";
    const inputHideImgProveedor = form.querySelector(idInputHideImgProveedor);
    const imgProveedor = form.querySelector(idImgProveedor);
    const labelImgProveedor = document.querySelector(idLabelImgProveedor);
    
    inputProveedor.value = proveedor.nombre ?? "";
    inputDescripcionProveedor.value = proveedor.descripcion ?? "";
   
    // Imagen: usa la imagen del proveedor si existe, de lo contrario la imagen por defecto
    const imagenValidaProveedor = proveedor.imagen && proveedor.imagen.trim() !== "";
    inputHideImgProveedor.value = imagenValidaProveedor ? proveedor.imagen : "";
    imgProveedor.src = imagenValidaProveedor ? proveedor.imagen : DEFAULT_IMG.PROVEEDOR;
    labelImgProveedor.textContent = "Imagen Proveedor";
    
    inputPrecio.value = articuloProveedor.precio ?? ""; 
    inputFechaPrecio.value = articuloProveedor.fechaPrecio ?? "";
    inputFechaNoDisponible.value = articuloProveedor.fechaNoDisponible ?? "";

    const disponible = articuloProveedor.disponible === DISPONIBILIDAD.DISPONIBLE.name ? DISPONIBILIDAD.DISPONIBLE.value : DISPONIBILIDAD.NO_DISPONIBLE.value;    
    
    selectDisponibilidad.addEventListener('change', () => {
        const valor = selectDisponibilidad.value;
        
        // eliminar clases anteriores
        selectDisponibilidad.classList.remove('disponible', 'no_disponible');

        // agregar la clase correcta
        if (valor === "0") {
            selectDisponibilidad.classList.add('no_disponible');
        } else {
            selectDisponibilidad.classList.add('disponible');
            inputFechaNoDisponible.value = "";
        }
    });
    
    seleccionarValorSelect(selectDisponibilidad, disponible);
    
    inputFechaPrecio.addEventListener('change', () => {
        if (inputFechaPrecio.value) {
            inputFechaNoDisponible.min = inputFechaPrecio.value;

            // Si la fecha no disponible actual es anterior, la ajusta automáticamente
            if (inputFechaNoDisponible.value && inputFechaNoDisponible.value < inputFechaPrecio.value) {
                //inputFechaNoDisponible.value = inputFechaPrecio.value;
            }
        }
    });
    
    onDetectarCambiosEditarArticuloProveedor(false);
    detectarCambiosFormulario(idFormArticuloProveedor, onDetectarCambiosEditarArticuloProveedor);
}



