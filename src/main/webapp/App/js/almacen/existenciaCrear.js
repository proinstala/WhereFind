import {solicitudPost, solicitudGet, resetImg, fillInputSelect, seleccionarValorSelect, cargarInputSelect, vaciarSelect, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import { DEFAULT_IMG, DISPONIBILIDAD } from '../constantes.mjs';

const idInputArticulo = "#articulo_id";

const idSelectArticulo = "#articulo";
const idSelectArticuloProveedor = "#articuloProveedor";
const idSelectAlmacen = "#almacen";
const idSelectEmplazamiento = "#emplazamiento";

const idInputPrecio = "#precio";
const idInputSku = "#sku";
const idInputFechaCompra = "#fechaCompra";
const idInputComprador = "#comprador";
const idInputAnotacion = "#anotacion";
const idFormExistencia = "#frmCrearExistencia";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosExistencia = "#btnDeshacerCambiosExistencia";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";

const fechaHoy = new Date().toISOString().split("T")[0];

$(document).ready(function () {
    const inputIdArticulo = document.querySelector(idInputArticulo);
    
    const selectArticulo = document.querySelector(idSelectArticulo);
    const selectArticuloProveedor = document.querySelector(idSelectArticuloProveedor);
    const selectAlmacen = document.querySelector(idSelectAlmacen);
    const selectEmplazamiento = document.querySelector(idSelectEmplazamiento);
    
    //Imagen articulo
    const contenedorImgArticulo = document.querySelector(idContenedorImgArticulo);
    const inputImgArticulo = document.querySelector(idInputImgArticulo);
    const imgArticulo = document.querySelector(idImgArticulo);
    const inputHideImgArticulo = document.querySelector(idInputHideImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const inputPrecio = document.querySelector(idInputPrecio);
    const inputFechaCompra = document.querySelector(idInputFechaCompra);
    
    const btnDeshacerCambiosExistencia = document.querySelector(idBtnDeshacerCambiosExistencia);
    const btnCancelar = document.querySelector(idBtnCancelar);

    //Añade un evento de cambio al select de articulo y actualiza el select de proveedor según el articulo seleccionado.
    const cargaInputSelectProveedor = () => {
        selectArticulo.addEventListener('change', (e) => {
            const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

            solicitudGet(`api/articulo/articulo?idArticulo=${optionSelected.value}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            const articulo = response.data;
                            const listaFiltrada = articulo.listaProveedores.filter(articuloProveedor => articuloProveedor.disponible === DISPONIBILIDAD.DISPONIBLE.name);
                            fillInputSelect(selectArticuloProveedor, listaFiltrada, 'Seleccione un proveedor');

                            // Imagen: usa la imagen del articulo si existe, de lo contrario la imagen por defecto
                            const imagenValida = articulo.imagen && articulo.imagen.trim() !== "";
                            inputHideImgArticulo.value = imagenValida ? articulo.imagen : "";
                            imgArticulo.src = imagenValida ? articulo.imagen : DEFAULT_IMG.ARTICULO;

                            labelImgArticulo.textContent = "";
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        });
        
        selectArticuloProveedor.addEventListener("change", (e) => {
            const optionSelected = e.target.selectedOptions[0];
            const precio = optionSelected.getAttribute("data-precio");
            inputPrecio.value = precio || "";
        });
    };

    //Carga el select articulo.
    const promesaSelctArticulo = cargarInputSelect(selectArticulo, "api/articulo/articulos", 'Seleccione un articulo', false, cargaInputSelectProveedor);
    
    //Añade un evento de cambio al select de almacén y actualiza el select de emplazamineto según el almacén seleccionado.
    const cargaInputSelectEmplazamiento = () => {
        selectAlmacen.addEventListener('change', (e) => {
            const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

            solicitudGet(`api/almacen/almacen?idAlmacen=${optionSelected.value}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            let almacen = response.data;
                            fillInputSelect(selectEmplazamiento, almacen.listaEmplazamientos, 'Seleccione un emplazamiento');
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        });
    };

    //Carga el select articulo.
    const promesaSelctAlmacen = cargarInputSelect(selectAlmacen, "api/almacen/almacenes", 'Seleccione un almacén', false, cargaInputSelectEmplazamiento);
    
    inputFechaCompra.value = fechaHoy;
    
    Promise.all([promesaSelctArticulo, promesaSelctAlmacen])
        .then(() => {
            onDetectarCambiosCrearExistencia(false);
            detectarCambiosFormulario(idFormExistencia, onDetectarCambiosCrearExistencia);
            
            const idArticulo = inputIdArticulo.value;
            if(idArticulo !== "-1") {
                seleccionarValorSelect(selectArticulo, idArticulo);
            }
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });
    
    
    validarFormulario(idFormExistencia);

    btnCancelar.addEventListener('click', () => {
        window.location.href = obtenerReferenciaRedireccionar();
    });

    btnDeshacerCambiosExistencia.addEventListener('click', () => {
        resetCamposForm(idFormExistencia);
        vaciarSelect(selectArticuloProveedor);
        vaciarSelect(selectEmplazamiento);
        resetImg(contenedorImgArticulo, DEFAULT_IMG.ARTICULO);
        inputFechaCompra.value = fechaHoy;
        onDetectarCambiosCrearExistencia(false);
        detectarCambiosFormulario(idFormExistencia, onDetectarCambiosCrearExistencia);
    });

});


function onDetectarCambiosCrearExistencia(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosExistencia").prop('disabled', !hayCambios);
}

function obtenerReferenciaRedireccionar() {
    if (document.referrer) {
        return window.location.href = document.referrer;
    } else {
        // Fallback: vuelve a una página por defecto
        return window.location.href = "almacen/existencias";
    }
}


function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            articulo: {
                required: true,
                min: 1,
                max: 2000000000
            },
            precio: {
                min: 0,
                max: 2000000000
            },
            sku: {
                maxlength: 50
            },
            almacen: {
                required: true,
                min: 1,
                max: 2000000000
            },
            emplazamiento: {
                required: true,
                min: 1,
                max: 2000000000
            },
            proveedor: {
                min: 1,
                max: 2000000000
            },
            comprador: {
                maxlength: 100
            },
            anotacion: {
                maxlength: 500
            }
        },//Fin de reglas ----------------
        messages: {
            articulo: {
                required: "Debe seleccionar un articulo.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            },
            precio: {
                min: "No puede introducir numeros negativos.",
                max: "Valor introducido no válido."
            },
            sku: {
                maxlength: "Longitud máx 50 caracteres."
            },
            almacen: {
                required: "Debe seleccionar un almacén.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            },
            emplazamiento: {
                required: "Debe seleccionar un emplazamiento.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            },
            proveedor: {
                min: "No puede introducir numeros negativos.",
                max: "Valor introducido no válido."
            },
            comprador: {
                maxlength: "Longitud máx 100 caracteres."
            },
            anotacion: {
                maxlength: "Longitud máx 500 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const existenciaJSON = getExistenciaJson();
            solicitudPost(`api/existencia/create`, idForm, true, existenciaJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = obtenerReferenciaRedireccionar();
                            mostrarMensaje("Existencia Creada.", `Se ha creado la existencia con id "${response.data.id}" correctamente`, "success", redireccionar);
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

function getExistenciaJson() {
    // Guardar referencias de los elementos del DOM
    const selectArticulo = document.querySelector(idSelectArticulo);
    const selectProveedor = document.querySelector(idSelectArticuloProveedor);
    const selectAlmacen = document.querySelector(idSelectAlmacen);
    const selectEmplazamiento = document.querySelector(idSelectEmplazamiento);
    const inputPrecio = document.querySelector(idInputPrecio);
    const inputSku = document.querySelector(idInputSku);
    const inputFechaCompra = document.querySelector(idInputFechaCompra);
    const inputComprador = document.querySelector(idInputComprador);
    const inputAnotacion = document.querySelector(idInputAnotacion);
    
    // Obtener el valor seleccionado
    const proveedorId = parseInt(selectProveedor.value, 10);

    // Construcción del objeto JSON con validación del código postal
    const existenciaJSON = {
        id: "0",
        precio: inputPrecio.value.trim() || 0,
        sku: inputSku.value.trim() || "",
        fechaCompra: inputFechaCompra.value.trim(),
        comprador: inputComprador.value.trim() || "",
        anotacion: inputAnotacion.value.trim() || "",
        disponible: true,
        articulo: {
            id: selectArticulo.value
        },
        proveedor: proveedorId === -1 ? null : {
            id: selectProveedor.value || -1
        },
        emplazamiento: {
            id: selectEmplazamiento.value
        }
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        existenciaJSON: JSON.stringify(existenciaJSON)
    }).toString();

    return data;
}



