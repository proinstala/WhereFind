import { solicitudGet, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idSelectDireccion = "#direccion";
const idInputIdProveedor = "#proveedor_id";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";
const idInputPaginaWeb = "#paginaWeb";

const idFormProveedor = "#frmModificarProveedor";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosProveedor = "#btnDeshacerCambiosProveedor";

const idContenedorImgProveedor = "#contenedorImgProveedor";
const idInputHideImgProveedor = "#imagenProveedorB64"; //Input oculto.
const idInputImgProveedor = "#inputImgProveedor";
const idImgProveedor = "#imgProveedor";
const idLabelImgProveedor = "#textoImagenProveedor";

let oldProveedor;

$(document).ready(function () {
    const selectDireccion = document.querySelector(idSelectDireccion);
    const inputIdProveedor = document.querySelector(idInputIdProveedor);
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    const inputPaginaWeb = document.querySelector(idInputPaginaWeb);
    
    //Imagen proveedor
    const contenedorImgProveedor = document.querySelector(idContenedorImgProveedor);
    const inputImgProveedor = document.querySelector(idInputImgProveedor);
    const imgProveedor = document.querySelector(idImgProveedor);
    const inputHideImgProveedor = document.querySelector(idInputHideImgProveedor);
    const labelImgProveedor = document.querySelector(idLabelImgProveedor);
    
    const btnDeshacerCambiosProveedor = document.querySelector(idBtnDeshacerCambiosProveedor);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormProveedor);

    getProveedor(inputIdProveedor.value);

    btnDeshacerCambiosProveedor.addEventListener('click', () => {
        fillFielsProveedor(oldProveedor);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = "proveedor/proveedores";
    });
    
    inputImgProveedor.addEventListener('change', (e) => {
        const defaultImg = imgProveedor.src;
        const fileImg = e.target.files[0];

        //Establece la imagen seleccionada.
        setImageSelected(fileImg, imgProveedor, inputHideImgProveedor, defaultImg, 4)
            .then((result) => {
                if (result) {
                    //console.log("Imagen establecida correctamente.");

                    // Detecta el cambio de la imagen
                    onDetectarCambiosModificarProveedor(labelImgProveedor.textContent !== result);

                    labelImgProveedor.textContent = result;
                } else {
                    console.log("No se ha establecido la imagen.");
                }
            })
            .catch((error) => {
                // Maneja cualquier error que ocurra durante la validación o el proceso de establecer la imagen
                console.error('Error:', error);
                inputImgProveedor.value = '';
            });
    });

});

function onDetectarCambiosModificarProveedor(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosProveedor).prop('disabled', !hayCambios);
}


function getProveedor(idProveedor) {
    solicitudGet(`api/proveedor/proveedor?idProveedor=${idProveedor}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldProveedor = response.data;
                    fillFielsProveedor(oldProveedor);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsProveedor(proveedor) {
    const form = document.querySelector(idFormProveedor);

    const selectDireccion = form.querySelector(idSelectDireccion);
    const inputNombre = form.querySelector(idInputNombre);
    const inputDescripcion = form.querySelector(idInputDescripcion);
    const inputPaginaWeb = form.querySelector(idInputPaginaWeb);
    const inputHideImgProveedor = form.querySelector(idInputHideImgProveedor);
    const imgProveedor = form.querySelector(idImgProveedor);
    const labelImgProveedor = document.querySelector(idLabelImgProveedor);
   
    inputNombre.value = proveedor.nombre;
    inputDescripcion.value = proveedor.descripcion ?? "";
    inputPaginaWeb.value = proveedor.paginaWeb ?? "";
    
    // Imagen: usa la imagen del proveedor si existe, de lo contrario la imagen por defecto
    const imagenValida = proveedor.imagen && proveedor.imagen.trim() !== "";
    inputHideImgProveedor.value = imagenValida ? proveedor.imagen : "";
    imgProveedor.src = imagenValida ? proveedor.imagen : DEFAULT_IMG.PROVEEDOR;
    
    labelImgProveedor.textContent = "";
    
    const direccionId = proveedor.direccion.id;
    
    const promesaDireccion = cargarInputSelect(selectDireccion, `api/direccion/find_direcciones_libres?direccion=${direccionId}`, 'Sin Dirección', direccionId, () => {});
    
    Promise.all([promesaDireccion])
        .then(() => {
            onDetectarCambiosModificarProveedor(false);
            detectarCambiosFormulario(idFormProveedor, onDetectarCambiosModificarProveedor);
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
            paginaWeb: {
                maxlength: 100
            },
            direccion: {
                maxlength: 100,
                min: -1,
                max: 999999
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre del proveedor.",
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                required: "Debe introducir la descripcion del proveedor.",
                maxlength: "Longitud máx 200 caracteres."
            },
            paginaWeb: {
                maxlength: "Longitud máx 100 caracteres."
            },
            direccion: {
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Proveedor", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const proveedorIdInput = document.querySelector('#proveedor_id');
                            const proveedorId = proveedorIdInput ? proveedorIdInput.value : -1;

                            solicitudPut(`api/proveedor/update/${proveedorId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado proveedor.", `Se han modificado correctamente los datos del proveedor ${response.data.id}`, "success");
                                            oldProveedor = response.data;
                                            onDetectarCambiosModificarProveedor(false);
                                            detectarCambiosFormulario(idFormProveedor, onDetectarCambiosModificarProveedor);
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


