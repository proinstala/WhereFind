import { solicitudPost, detectarCambiosFormulario, solicitudPut } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError } from '../alertasSweetAlert2.mjs?v=20241021_184300';

$(document).ready(function () {
    validarFormularioRecovery("#frmRecovery");
    detectarCambiosFormulario("#frmRecovery", onDetectarCambios);

    validarFormularioRecoveryFinal("#frmRecoveryFinal");
    detectarCambiosFormulario("#frmRecoveryFinal", onDetectarCambios);
});

function onDetectarCambios(hayCambios) {
    $("#btnEntrar").prop('disabled', !hayCambios);
}

function validarFormularioRecovery(nombreForm) {
    $(nombreForm).validate({
        rules: {
            nombreUsuario: {
                required: true,
                maxlength: 200
            }
        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 200 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            solicitudPost("api/identidad/recovery", nombreForm, true)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        mostrarMensaje("Recuperar password", response.result);
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                });
        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}

function validarFormularioRecoveryFinal(idForm) {
    $(idForm).validate({
        rules: {
            nuevoPassword: {
                required: true,
                maxlength: 60,
                minlength: 4
            },
            confirmPassword: {
                required: true,
                equalTo: "#nuevoPassword"
            }

        },//Fin de reglas ----------------
        messages: {
            nuevoPassword: {
                required: "Este campo es requerido.",
                maxlength: "Longitud máx 60 caracteres.",
                minlength: "Logitud minima de 4 caracteres."
            },
            confirmPassword: {
                required: "Este campo es requerido.",
                equalTo: "El password de confirmación es erroneo."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const usuarioIdInput = document.querySelector('#passwordUsuario_id');
            const usuarioId = usuarioIdInput ? usuarioIdInput.value : -1;

            solicitudPut(`api/identidad/updatePassword/${usuarioId}`, idForm, true)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede actualizar el password", response.result);
                        } else {
                            mostrarMensaje("Cambiar Password.", `Se ha modificado correctamente el password del usuario. Pruebe a loguearse de nuevo usando su nuevo password`, "success");
                        }
                    })
                    .catch(error => {
                        console.error(error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}
