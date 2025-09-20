
import { solicitudGet, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idInputIdMarca = "#marca_id";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";

const idFormMarca = "#frmModificarMarca";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosMarca = "#btnDeshacerCambiosMarca";

const idContenedorImgMarca = "#contenedorImgMarca";
const idInputHideImgMarca = "#imagenMarcaB64"; //Input oculto.
const idInputImgMarca = "#inputImgMarca";
const idImgMarca = "#imgMarca";
const idLabelImgMarca = "#textoImagenMarca";

let oldMarca;

$(document).ready(function () {
    const inputIdMarca = document.querySelector(idInputIdMarca);
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    
    //Imagen marca
    const contenedorImgMarca = document.querySelector(idContenedorImgMarca);
    const inputImgMarca = document.querySelector(idInputImgMarca);
    const imgMarca = document.querySelector(idImgMarca);
    const inputHideImgMarca = document.querySelector(idInputHideImgMarca);
    const labelImgMarca = document.querySelector(idLabelImgMarca);
    
    const btnDeshacerCambiosMarca = document.querySelector(idBtnDeshacerCambiosMarca);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormMarca);

    getMarca(inputIdMarca.value);

    btnDeshacerCambiosMarca.addEventListener('click', () => {
        fillFielsMarca(oldMarca);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = "articulo/marcas";
    });
    
    inputImgMarca.addEventListener('change', (e) => {
        const defaultImg = imgMarca.src;
        const fileImg = e.target.files[0];

        //Establece la imagen seleccionada.
        setImageSelected(fileImg, imgMarca, inputHideImgMarca, defaultImg, 4)
            .then((result) => {
                if (result) {
                    //console.log("Imagen establecida correctamente.");

                    // Detecta el cambio de la imagen
                    onDetectarCambiosModificarMarca(labelImgMarca.textContent !== result);

                    labelImgMarca.textContent = result;
                } else {
                    console.log("No se ha establecido la imagen.");
                }
            })
            .catch((error) => {
                // Maneja cualquier error que ocurra durante la validación o el proceso de establecer la imagen
                console.error('Error:', error);
                inputImgMarca.value = '';
            });
    });

});

function onDetectarCambiosModificarMarca(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosMarca).prop('disabled', !hayCambios);
}


function getMarca(idMarca) {
    solicitudGet(`api/marca/marca?idMarca=${idMarca}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldMarca = response.data;
                    fillFielsMarca(oldMarca);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsMarca(marca) {
    const form = document.querySelector(idFormMarca);

    const inputNombre = form.querySelector(idInputNombre);
    const inputDescripcion = form.querySelector(idInputDescripcion);
    const inputHideImgMarca = form.querySelector(idInputHideImgMarca);
    const imgMarca = form.querySelector(idImgMarca);
    const labelImgMarca = document.querySelector(idLabelImgMarca);
   
    inputNombre.value = marca.nombre;
    inputDescripcion.value = marca.descripcion ?? "";
    
    // Imagen: usa la imagen de la marca si existe, de lo contrario la imagen por defecto
    const imagenValida = marca.imagen && marca.imagen.trim() !== "";
    inputHideImgMarca.value = imagenValida ? marca.imagen : "";
    imgMarca.src = imagenValida ? marca.imagen : DEFAULT_IMG.MARCA;
    
    labelImgMarca.textContent = "";
    
    onDetectarCambiosModificarMarca(false);
    detectarCambiosFormulario(idFormMarca, onDetectarCambiosModificarMarca);
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
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre de la marca.",
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                required: "Debe introducir la descripcion de la marca.",
                maxlength: "Longitud máx 200 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Marca", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const marcaIdInput = document.querySelector('#marca_id');
                            const marcaId = marcaIdInput ? marcaIdInput.value : -1;

                            solicitudPut(`api/marca/update/${marcaId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado marca.", `Se han modificado correctamente los datos de la marca ${response.data.id}`, "success");
                                            oldMarca = response.data;
                                            onDetectarCambiosModificarMarca(false);
                                            detectarCambiosFormulario(idFormMarca, onDetectarCambiosModificarMarca);
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


