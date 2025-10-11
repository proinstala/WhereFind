import { solicitudGet, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idSelectDireccion = "#direccion";
const idInputIdAlmacen = "#almacen_id";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";
const idInputPaginaWeb = "#paginaWeb";

const idFormAlmacen = "#frmModificarAlmacen";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosAlmacen = "#btnDeshacerCambiosAlmacen";

let oldAlmacen;

$(document).ready(function () {
    const selectDireccion = document.querySelector(idSelectDireccion);
    const inputIdAlmacen = document.querySelector(idInputIdAlmacen);
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    
    const btnDeshacerCambiosAlmacen = document.querySelector(idBtnDeshacerCambiosAlmacen);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormAlmacen);

    getAlmacen(inputIdAlmacen.value);

    btnDeshacerCambiosAlmacen.addEventListener('click', () => {
        fillFielsAlmacen(oldAlmacen);
    });

    btnCancelar.addEventListener('click', () => {
        window.location.href = "almacen/almacenes";
    });

});

function onDetectarCambiosModificarAlmacen(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosAlmacen).prop('disabled', !hayCambios);
}


function getAlmacen(idAlmacen) {
    solicitudGet(`api/almacen/almacen?idAlmacen=${idAlmacen}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldAlmacen = response.data;
                    fillFielsAlmacen(oldAlmacen);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsAlmacen(almacen) {
    const form = document.querySelector(idFormAlmacen);

    const selectDireccion = form.querySelector(idSelectDireccion);
    const inputNombre = form.querySelector(idInputNombre);
    const inputDescripcion = form.querySelector(idInputDescripcion);
   
    inputNombre.value = almacen.nombre;
    inputDescripcion.value = almacen.descripcion ?? "";
    
    const direccionId = almacen.direccion.id;
    
    const promesaDireccion = cargarInputSelect(selectDireccion, `api/direccion/find_direcciones_libres?direccion=${direccionId}`, 'Sin Dirección', direccionId, () => {});
    
    Promise.all([promesaDireccion])
        .then(() => {
            onDetectarCambiosModificarAlmacen(false);
            detectarCambiosFormulario(idFormAlmacen, onDetectarCambiosModificarAlmacen);
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
            direccion: {
                maxlength: 100,
                min: -1,
                max: 999999
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre del almacén.",
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                required: "Debe introducir la descripcion del almacén.",
                maxlength: "Longitud máx 200 caracteres."
            },
            direccion: {
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Almacén", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const almacenIdInput = document.querySelector('#almacen_id');
                            const almacenId = almacenIdInput ? almacenIdInput.value : -1;

                            solicitudPut(`api/almacen/update/${almacenId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado almacén.", `Se han modificado correctamente los datos del almacén ${response.data.id}`, "success");
                                            oldAlmacen = response.data;
                                            onDetectarCambiosModificarAlmacen(false);
                                            detectarCambiosFormulario(idFormAlmacen, onDetectarCambiosModificarAlmacen);
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


