import { solicitudPost, detectarCambiosFormulario, solicitudPut } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError } from './alertasSweetAlert2.mjs';

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
                maxlength: 20
            }
        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 20 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {

            const loginCallBack = (response)  => {
                if (response.isError === 1) {
                    mostrarMensaje("Se ha producido un error", response.result, "error");
                } else {
                    mostrarMensaje("Recuperar password", response.result, "success", false);
                }
            };

            solicitudPost("api/identidad/recovery", loginCallBack, nombreForm, true);
        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error

        },
        complete: function () {
            console.log("complete");
            formDisable(false, nombreForm);
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
                passwordMatch: true
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
                passwordMatch: "El password de confirmación es erroneo."
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
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });

        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error

        },
        complete: function () {
            console.log("complete");
            formDisable(false, nombreForm);
        }
    });//Fin Validate
}
