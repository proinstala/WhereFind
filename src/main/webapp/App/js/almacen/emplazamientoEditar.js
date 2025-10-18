
import { solicitudGet, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idSelectTipo = "#tipo";
const idSelectAlmacen = "#almacen";
const idInputIdEmplazamiento = "#emplazamiento_id";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";

const idFormEmplazamiento = "#frmModificarEmplazamiento";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosEmplazamiento = "#btnDeshacerCambiosEmplazamiento";

let oldEmplazamiento;

$(document).ready(function () {
    const selectTipo = document.querySelector(idSelectTipo);
    const selectAlmacen = document.querySelector(idSelectAlmacen);
    const inputIdEmplazamiento = document.querySelector(idInputIdEmplazamiento);
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    
    const btnDeshacerCambiosEmplazamiento = document.querySelector(idBtnDeshacerCambiosEmplazamiento);
    const btnCancelar = document.querySelector(idBtnCancelar);

    validarFormulario(idFormEmplazamiento);

    getEmplazamiento(inputIdEmplazamiento.value);

    btnDeshacerCambiosEmplazamiento.addEventListener('click', () => {
        fillFielsEmplazamiento(oldEmplazamiento);
    });

    btnCancelar.addEventListener('click', () => {
        if (document.referrer) {
            window.location.href = document.referrer;
        } else {
            // Fallback: vuelve a una página por defecto
            window.location.href = "almacen/emplazamientos";
        }
    });

});

function onDetectarCambiosModificarEmplazamiento(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosEmplazamiento).prop('disabled', !hayCambios);
}


function getEmplazamiento(idEmplazamiento) {
    solicitudGet(`api/emplazamiento/emplazamiento?idEmplazamiento=${idEmplazamiento}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldEmplazamiento = response.data;
                    fillFielsEmplazamiento(oldEmplazamiento);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsEmplazamiento(emplazamiento) {
    const form = document.querySelector(idFormEmplazamiento);

    const selectTipo = form.querySelector(idSelectTipo);
    const selectAlmacen = form.querySelector(idSelectAlmacen);
    const inputNombre = form.querySelector(idInputNombre);
    const inputDescripcion = form.querySelector(idInputDescripcion);
   
    inputNombre.value = emplazamiento.nombre;
    inputDescripcion.value = emplazamiento.descripcion ?? "";
    
    const tipoId = emplazamiento.tipoEmplazamiento.id;
    const almacenId = emplazamiento.almacen.id;
    
    const promesaTipo = cargarInputSelect(selectTipo, `api/tipo_emplazamiento/tipos_emplazamientos`, '', tipoId, () => {});
    const promesaAlmacen = cargarInputSelect(selectAlmacen, `api/almacen/almacenes`, '', almacenId, () => {});
    
    Promise.all([promesaTipo, promesaAlmacen])
        .then(() => {
            onDetectarCambiosModificarEmplazamiento(false);
            detectarCambiosFormulario(idFormEmplazamiento, onDetectarCambiosModificarEmplazamiento);
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
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre del emplazamiento.",
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                required: "Debe introducir la descripcion del emplazamiento.",
                maxlength: "Longitud máx 200 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            mostrarMensajeOpcion("Modificar Emplazamiento", '¿Quieres realmente modificar los datos?')
                    .then((result) => {
                        if (result.isConfirmed) {
                            const emplazamientoIdInput = document.querySelector('#emplazamiento_id');
                            const emplazamientoId = emplazamientoIdInput ? emplazamientoIdInput.value : -1;

                            solicitudPut(`api/emplazamiento/update/${emplazamientoId}`, idForm, true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede actualizar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Modificado emplazamiento.", `Se han modificado correctamente los datos del emplazamiento ${response.data.id}`, "success");
                                            oldEmplazamiento = response.data;
                                            onDetectarCambiosModificarEmplazamiento(false);
                                            detectarCambiosFormulario(idFormEmplazamiento, onDetectarCambiosModificarEmplazamiento);
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



