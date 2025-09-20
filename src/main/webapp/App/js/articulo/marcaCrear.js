
import {solicitudPost, setImageSelected, resetImg, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";
const idFormMarca = "#frmCrearMarca";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosMarca = "#btnDeshacerCambiosMarca";

const idContenedorImgMarca = "#contenedorImgMarca";
const idInputHideImgMarca = "#imagenMarcaB64"; //Input oculto.
const idInputImgMarca = "#inputImgMarca";
const idImgMarca = "#imgMarca";
const idLabelImgMarca = "#textoImagenMarca";

let defaultImgMarca = DEFAULT_IMG.MARCA;

$(document).ready(function () {
    const btnDeshacerCambiosMarca = document.querySelector(idBtnDeshacerCambiosMarca);
    const btnCancelar = document.querySelector(idBtnCancelar);
    
    //Imagen marca
    const contenedorImgMarca = document.querySelector(idContenedorImgMarca);
    const inputImgMarca = document.querySelector(idInputImgMarca);
    const imgMarca = document.querySelector(idImgMarca);
    const inputHideImgMarca = document.querySelector(idInputHideImgMarca);
    const labelImgMarca = document.querySelector(idLabelImgMarca);

    validarFormulario(idFormMarca);

    btnCancelar.addEventListener('click', () => {
        window.location.href = "articulo/marcas";
    });

    btnDeshacerCambiosMarca.addEventListener('click', () => {
        resetCamposForm(idFormMarca);
        resetImg(contenedorImgMarca, DEFAULT_IMG.MARCA);
        
        onDetectarCambiosCrearMarca(false);
        detectarCambiosFormulario(idFormMarca, onDetectarCambiosCrearMarca);
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
                    onDetectarCambiosCrearMarca(labelImgMarca.textContent !== result);

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
    
    onDetectarCambiosCrearMarca(false);
    detectarCambiosFormulario(idFormMarca, onDetectarCambiosCrearMarca);

});


function onDetectarCambiosCrearMarca(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosMarca).prop('disabled', !hayCambios);
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
            const marcaJSON = getMarcaJson();
            solicitudPost(`api/marca/create`, idForm, true, marcaJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = "articulo/marcas";
                            mostrarMensaje("Marca Creada.", `Se ha creado la marca con id "${response.data.id}" correctamente`, "success", redireccionar);
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

function getMarcaJson() {
    // Guardar referencias de los elementos del DOM
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    const inputHideImgMarca = document.querySelector(idInputHideImgMarca);

    const marcaJSON = {
        id: "0",
        nombre: inputNombre.value.trim(),
        descripcion: inputDescripcion.value.trim(),
        imagen: inputHideImgMarca.value,
        activo: true
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        marcaJSON: JSON.stringify(marcaJSON)
    }).toString();

    return data;
}



