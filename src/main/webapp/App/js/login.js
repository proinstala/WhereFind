import { solicitudPost } from './comunes.mjs';
import { mostrarMensajeAcceso } from './alertasSweetAlert2.mjs';

$(document).ready(function () {
    validarFormulario("#frmLogin");
});

function validarFormulario(nombreForm) {
    $(nombreForm).validate({
        rules: {
            nombreUsuario: {
                required: true,
                maxlength: 20
            },
            passwordUsuario: {
                required: true,
                maxlength: 60
            }

        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 20 caracteres."
            },
            passwordUsuario: {
                required: "Debe introducir el password de usuario.",
                maxlength: "Longitud máx 60 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {

            const loginCallBack = (response)  => {
                if (response.isError === 1) {
                    mostrarMensajeAcceso("Acceso Denegado", response.result, "error");
                } else {
                    const acceso = () => window.location.replace(response.result);
                    mostrarMensajeAcceso(`Bienvenido ${response.user.nombre}`, "Acceso Permitido.", "success", (response.isUrl)? acceso : null);
                }
            }

            solicitudPost("api/identidad/login", loginCallBack, nombreForm, true);
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
