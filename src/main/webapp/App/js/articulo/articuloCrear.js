
import {solicitudPost, setImageSelected, resetImg, fillInputSelect, cargarInputSelect, vaciarSelect, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idSelectMarca = "#marca";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";
const idInputReferencia = "#referencia";
const idInputModelo = "#modelo";
const idInputStockMinimo = "#stockMinimo";
const idFormArticulo = "#frmCrearArticulo";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosArticulo = "#btnDeshacerCambiosArticulo";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";


$(document).ready(function () {
    const selectMarca = document.querySelector(idSelectMarca);
    const btnDeshacerCambiosArticulo = document.querySelector(idBtnDeshacerCambiosArticulo);
    const btnCancelar = document.querySelector(idBtnCancelar);
    
    //Imagen articulo
    const contenedorImgArticulo = document.querySelector(idContenedorImgArticulo);
    const inputImgArticulo = document.querySelector(idInputImgArticulo);
    const imgArticulo = document.querySelector(idImgArticulo);
    const inputHideImgArticulo = document.querySelector(idInputHideImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);

    //Carga el select marca.
    const promesaCargaSelectMarca = cargarInputSelect(selectMarca, "api/marca/find_marcas", 'Seleccione una marca', false, () => {});
    
    Promise.all([promesaCargaSelectMarca])
        .then(() => {
            onDetectarCambiosCrearArticulo(false);
            detectarCambiosFormulario(idFormArticulo, onDetectarCambiosCrearArticulo);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });

    validarFormulario(idFormArticulo);


    btnCancelar.addEventListener('click', () => {
        window.location.href = "articulo/articulos";
    });

    btnDeshacerCambiosArticulo.addEventListener('click', () => {
        resetCamposForm(idFormArticulo);
        resetImg(contenedorImgArticulo, DEFAULT_IMG.ARTICULO);
        
        onDetectarCambiosCrearArticulo(false);
        detectarCambiosFormulario(idFormArticulo, onDetectarCambiosCrearArticulo);
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
                    onDetectarCambiosCrearArticulo(labelImgArticulo.textContent !== result);

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


function onDetectarCambiosCrearArticulo(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosArticulo).prop('disabled', !hayCambios);
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
            marca: {
                required: true,
                number: true,
                min: 1,
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
            const articuloJSON = getArticuloJson();
            solicitudPost(`api/articulo/create`, idForm, true, articuloJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = "articulo/articulos";
                            mostrarMensaje("Artículo Creado.", `Se ha creado artículo con id "${response.data.id}" correctamente`, "success", redireccionar);
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

function getArticuloJson() {
    // Guardar referencias de los elementos del DOM
    const selectMarca = document.querySelector(idSelectMarca);
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    const inputReferencia = document.querySelector(idInputReferencia);
    const inputModelo = document.querySelector(idInputModelo);
    const inputStockMinimo = document.querySelector(idInputStockMinimo);
    const inputHideImgArticulo = document.querySelector(idInputHideImgArticulo);
    
    // Obtener el valor seleccionado
    const marcaId = parseInt(selectMarca.value, 10);

    const articuloJSON = {
        id: "0",
        nombre: inputNombre.value.trim(),
        descripcion: inputDescripcion.value.trim(),
        referencia: inputReferencia.value.trim(),
        modelo: inputModelo.value.trim(),
        stockMinimo: Number(inputStockMinimo.value) || 0,
        imagen: inputHideImgArticulo.value,
        activo: true,
        marca: marcaId === -1 ? null : {
            id: marcaId,
            nombre: "",
            descripcion: "",
            imagen: ""
        }
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        articuloJSON: JSON.stringify(articuloJSON)
    }).toString();

    return data;
}





