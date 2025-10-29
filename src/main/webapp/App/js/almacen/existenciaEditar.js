
import { solicitudGet, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG, DISPONIBILIDAD} from '../constantes.mjs';

const idSelectArticulo = "#articulo";
const idSelectArticuloProveedor = "#articuloProveedor";
const idSelectAlmacen = "#almacen";
const idSelectEmplazamiento = "#emplazamiento";
const idSelectDisponibilidad = "#disponibilidad";

const idInputIdExistencia = "#existencia_id";
const idInputPrecio = "#precio";
const idInputSku = "#sku";
const idInputFechaCompra = "#fechaCompra";
const idInputComprador = "#comprador";
const idInputFechaNoDisponible = "#fechaNoDisponible";
const idInputAnotacion = "#anotacion";
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
    const inputIdExistencia = document.querySelector(idInputIdExistencia);
    
    const btnDeshacerCambiosExistencia = document.querySelector(idBtnDeshacerCambiosExistencia);
    const btnCancelar = document.querySelector(idBtnCancelar);

    definicionReglaFechas();
    validarFormulario(idFormExistencia);

    getExistencia(inputIdExistencia.value);

    btnCancelar.addEventListener('click', () => {
        if (document.referrer) {
                window.location.href = document.referrer;
            } else {
                // Fallback: vuelve a una página por defecto
                window.location.href = "almacen/existencias";
            }
    });

    btnDeshacerCambiosExistencia.addEventListener('click', () => {
        fillFielsExistencia(oldExistencia);
    });

});

function onDetectarCambiosModificarExistencia(hayCambios) {
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

function fillFielsExistencia(existencia) {
    const form = document.querySelector(idFormExistencia);
    
    const selectArticulo = form.querySelector(idSelectArticulo);
    const selectArticuloProveedor = form.querySelector(idSelectArticuloProveedor);
    const selectAlmacen = form.querySelector(idSelectAlmacen);
    const selectEmplazamiento = form.querySelector(idSelectEmplazamiento);
    const selectDisponibilidad = form.querySelector(idSelectDisponibilidad);
    const inputPrecio = form.querySelector(idInputPrecio);
    const inputSku = form.querySelector(idInputSku);
    const inputFechaCompra = form.querySelector(idInputFechaCompra);
    const inputComprador = form.querySelector(idInputComprador);
    const inputFechaNoDisponible = form.querySelector(idInputFechaNoDisponible);
    const inputAnotacion = form.querySelector(idInputAnotacion);

    const inputHideImgArticulo = form.querySelector(idInputHideImgArticulo);
    const imgArticulo = form.querySelector(idImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    inputPrecio.value = existencia.precio;
    inputSku.value = existencia.sku ?? "";
    inputComprador.value = existencia.comprador ?? "";
    inputFechaCompra.value = existencia.fechaCompra ?? "";
    inputFechaNoDisponible.value = existencia.fechaNoDisponible ?? "";
    inputAnotacion.value = existencia.anotacion ?? "";
    
    const articuloId = existencia.articulo.id;
    const proveedorId = existencia.proveedor.id;
    const emplazamientoId = existencia.emplazamiento.id;
    const almacenId = existencia.emplazamiento.almacen.id;
    const disponible = existencia.disponible === DISPONIBILIDAD.DISPONIBLE.name ? DISPONIBILIDAD.DISPONIBLE.value : DISPONIBILIDAD.NO_DISPONIBLE.value;
    
    // Imagen: usa la imagen del articulo si existe, de lo contrario la imagen por defecto
    const imagenValida = existencia.articulo.imagen && existencia.articulo.imagen.trim() !== "";
    inputHideImgArticulo.value = imagenValida ? existencia.articulo.imagen : "";
    imgArticulo.src = imagenValida ? existencia.articulo.imagen : DEFAULT_IMG.ARTICULO;
    
    labelImgArticulo.textContent = "";
    
    inputFechaNoDisponible.min = inputFechaCompra.value;
    inputFechaCompra.addEventListener('change', () => {
        if (inputFechaCompra.value) {
            inputFechaNoDisponible.min = inputFechaCompra.value;

            // Si la fecha no disponible actual es anterior, la ajusta automáticamente
            if (inputFechaNoDisponible.value && inputFechaNoDisponible.value < inputFechaCompra.value) {
                //inputFechaNoDisponible.value = inputFechaCompra.value;
            }
        }
    });
   
    selectDisponibilidad.addEventListener('change', () => {
        const valor = selectDisponibilidad.value;
        
        // eliminar clases anteriores
        selectDisponibilidad.classList.remove('disponible', 'no_disponible');
        
        if(valor === '0') {
            selectDisponibilidad.classList.add('no_disponible');
        } else {
            selectDisponibilidad.classList.add('disponible');
            inputFechaNoDisponible.value = "";
        }
    });
     
    
    
    const promesaDisponible = seleccionarValorSelect(selectDisponibilidad, disponible);
    
    const cargaImputSelectArticuloProveedor = () => {
         solicitudGet(`api/articulo/articulo?idArticulo=${articuloId}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            let articulo = response.data;
                            fillInputSelect(selectArticuloProveedor, articulo.listaProveedores, 'Seleccione un proveedor');
                            if(proveedorId > 0) {
                                seleccionarValorSelect(selectArticuloProveedor, proveedorId);
                            }
                            console.log("cargaImputSelectArticuloProveedor");
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        
    };
    
    
    const promesaSelctArticulo = cargarInputSelect(selectArticulo, "api/articulo/articulos", '', articuloId, () => {
        cargaImputSelectArticuloProveedor();
        
        selectArticulo.addEventListener('change', (e) => {
            const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

            solicitudGet(`api/articulo/articulo?idArticulo=${optionSelected.value}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            const articulo = response.data;
                            fillInputSelect(selectArticuloProveedor, articulo.listaProveedores, 'Seleccione un proveedor');

                            // Imagen: usa la imagen del articulo si existe, de lo contrario la imagen por defecto
                            const imagenValida = articulo.imagen && articulo.imagen.trim() !== "";
                            inputHideImgArticulo.value = imagenValida ? articulo.imagen : "";
                            imgArticulo.src = imagenValida ? articulo.imagen : DEFAULT_IMG.ARTICULO;

                            labelImgArticulo.textContent = "";
                            console.log("promesaSelctArticulo - 1.1");
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
        
        console.log("promesaSelctArticulo - 1");
    });
    
    const cargaImputSelectEmpalzamiento = () => {
         solicitudGet(`api/almacen/almacen?idAlmacen=${almacenId}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            let almacen = response.data;
                            fillInputSelect(selectEmplazamiento, almacen.listaEmplazamientos, '',);
                            seleccionarValorSelect(selectEmplazamiento, emplazamientoId);
                            console.log("cargaImputSelectEmpalzamiento");
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        
    };
    

    //Carga el select articulo.
    const promesaSelctAlmacen = cargarInputSelect(selectAlmacen, "api/almacen/almacenes", '', almacenId, () => {
        cargaImputSelectEmpalzamiento();
        
        selectAlmacen.addEventListener('change', (e) => {
            const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

            solicitudGet(`api/almacen/almacen?idAlmacen=${optionSelected.value}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            let almacen = response.data;
                            fillInputSelect(selectEmplazamiento, almacen.listaEmplazamientos, 'Seleccione un emplazamiento');
                            console.log("promesaSelctAlmacen - 2.1");
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        });
        console.log("promesaSelctAlmacen - 2");
    });
    
    
    Promise.all([promesaSelctArticulo, promesaSelctAlmacen])
        .then(() => {
            console.log("promesas");
            onDetectarCambiosModificarExistencia(false);
            detectarCambiosFormulario(idFormExistencia, onDetectarCambiosModificarExistencia);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });
}

function definicionReglaFechas() {
    // --- Definir regla personalizada ---
    $.validator.addMethod("fechaPosteriorA", function(value, element, params) {
        const fechaCompra = $(params).val();
        if (!value || !fechaCompra) return true; // Si alguno está vacío, no bloquea
        return new Date(value) >= new Date(fechaCompra);
    }, "Fecha anterio a fecha compra.");
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            articulo: {
                required: true,
                min: 0,
                max: 2000000000
            },
            articuloProveedor: {
                required: false,
                min: -1,
                max: 2000000000
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
            precio: {
                required: true,
                number: true,
                min: 0,
                max: 2000000000
            },
            fechaCompra: {
                date: true
            },
            comprador: {
                maxlength: 100
            },
            sku: {
                maxlength: 50
            },
            fechaNoDisponible: {
                required: false,
                date: true,
                fechaPosteriorA: "#fechaCompra"
            },
            anotacion: {
                maxlength: 500
            },
            disponibilidad: {
                required: true,
                min: 0,
                max: 1
            }
        },//Fin de reglas ----------------
        messages: {
            articulo: {
                required: "Debe seleccionar un artículo.",
                min: "Valor no válido.",
                max: "Valor no válido."
            },
            articuloProveedor: {
                min: "Valor no válido.",
                max: "Valor no válido."
            },
            almacen: {
                required: "Debe seleccionar un almacén.",
                min: "Valor no válido.",
                max: "Valor no válido."
            },
            emplazamiento: {
                required: "Debe seleccionar un emplazamiento.",
                min: "Valor no válido.",
                max: "Valor no válido."
            },
            precio: {
                required: "Debe introducir un precio.",
                number: "Debe introducir un valor numérico.",
                min: "El precio no puede ser negativo.",
                max: "El valor máximo permitido es 2.000.000.000."
            },
            fechaCompra: {
                date: "Debe introducir una fecha válida."
            },
            comprador: {
                maxlength: "Longitud máxima: 100 caracteres."
            },
            sku: {
                maxlength: "Longitud máxima: 50 caracteres."
            },
            fechaNoDisponible: {
                date: "Debe introducir una fecha válida.",
                min: "Fecha anterio a fecha compra.",
                fechaPosteriorA: "Fecha anterio a fecha compra."
            },
            anotacion: {
                maxlength: "Longitud máxima: 500 caracteres."
            },
            disponibilidad: {
                required: "Debe indicar si la existencia está disponible.",
                min: "Valor no válido.",
                max: "Valor no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Existencia", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const existenciaIdInput = document.querySelector(idInputIdExistencia);
                            const existenciaId = existenciaIdInput ? existenciaIdInput.value : -1;

                            solicitudPut(`api/existencia/update/${existenciaId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado existencia.", `Se han modificado correctamente los datos de la existencia ${response.data.id}`, "success");
                                            oldExistencia = response.data;
                                            onDetectarCambiosModificarExistencia(false);
                                            detectarCambiosFormulario(idFormExistencia, onDetectarCambiosModificarExistencia);
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



