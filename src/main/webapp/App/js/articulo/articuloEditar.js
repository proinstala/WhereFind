
import { solicitudGet, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idSelectMarca = "#marca";
const idInputNombre = "#nombre";
const idInputIdArticulo = "#articulo_id";
const idInputDescripcion = "#descripcion";
const idInputReferencia = "#referencia";
const idInputModelo = "#modelo";
const idInputStockMinimo = "#stockMinimo";
const idFormArticulo = "#frmModificarArticulo";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosArticulo = "#btnDeshacerCambiosArticulo";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";

let oldArticulo;

$(document).ready(function () {
    const selectMarca = document.querySelector(idSelectMarca);
    const inputIdArticulo = document.querySelector(idInputIdArticulo);
    
    //Imagen articulo
    const contenedorImgArticulo = document.querySelector(idContenedorImgArticulo);
    const inputImgArticulo = document.querySelector(idInputImgArticulo);
    const imgArticulo = document.querySelector(idImgArticulo);
    const inputHideImgArticulo = document.querySelector(idInputHideImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const btnDeshacerCambiosArticulo = document.querySelector(idBtnDeshacerCambiosArticulo);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormArticulo);

    getArticulo(inputIdArticulo.value);

    btnDeshacerCambiosArticulo.addEventListener('click', () => {
        fillFielsArticulo(oldArticulo);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = "articulo/articulos";
    });
    
    inputImgArticulo.addEventListener('change', (e) => {
        const defaultImg = imgArticulo.src;
        const fileImg = e.target.files[0];

        //Establece la imagen seleccionada.
        setImageSelected(fileImg, imgArticulo, inputHideImgArticulo, defaultImg, 4)
            .then((result) => {
                if (result) {
                    //console.log("Imagen establecida correctamente.");

                    // Detecta el cambio de la imagen
                    onDetectarCambiosModificarArticulo(labelImgArticulo.textContent !== result);

                    labelImgArticulo.textContent = result;
                } else {
                    console.log("No se ha establecido la imagen.");
                }
            })
            .catch((error) => {
                // Maneja cualquier error que ocurra durante la validación o el proceso de establecer la imagen
                console.error('Error:', error);
                inputImgArticulo.value = '';
            });
    });

});

function onDetectarCambiosModificarArticulo(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosArticulo).prop('disabled', !hayCambios);
}


function getArticulo(idArticulo) {
    solicitudGet(`api/articulo/articulo?idArticulo=${idArticulo}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldArticulo = response.data;
                    fillFielsArticulo(oldArticulo);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsArticulo(articulo) {
    const form = document.querySelector(idFormArticulo);

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

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: true,
                maxlength: 100
            },
            descripcion: {
                required: true,
                maxlength: 200
            },
            referencia: {
                required: true,
                maxlength: 100
            },
            maraca: {
                required: true,
                number: true,
                min: -1,
                max: 2000000000
            },
            modelo: {
                maxlength: 100
            },
            stockMinimo: {
                min: 0,
                max: 2000000000
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre del artículo.",
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                required: "Debe introducir la descripcion del artículo.",
                maxlength: "Longitud máx 200 caracteres."
            },
            referencia: {
                required: "Debe introducir la referencia del artículo.",
                maxlength: "Longitud máx 100 caracteres."
            },
            marca: {
                number: "Valor seleccionado no válido.",
                min: "Valor seleccionado no válido.",
                max: "El valor máximo permitido es 2000000000."
            },
            modelo: {
                maxlength: "Longitud máx 100 caracteres."
            },
            stockMinimo: {
                min: "El valor mínimo permitido es 0.",
                max: "El valor máximo permitido es 2000000000."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Artículo", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const articuloIdInput = document.querySelector('#articulo_id');
                            const articuloId = articuloIdInput ? articuloIdInput.value : -1;

                            solicitudPut(`api/articulo/update/${articuloId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado artículo.", `Se han modificado correctamente los datos del artículo ${response.data.id}`, "success");
                                            oldArticulo = response.data;
                                            onDetectarCambiosModificarArticulo(false);
                                            detectarCambiosFormulario(idFormArticulo, onDetectarCambiosModificarArticulo);
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




