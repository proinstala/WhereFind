import { solicitudPost, detectarCambiosFormulario } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError } from '../alertasSweetAlert2.mjs?v=20241021_184300';

$(document).ready(function () {
    validarFormulario("#frmLogin");
    detectarCambiosFormulario("#frmLogin", onDetectarCambios);
});

function onDetectarCambios(hayCambios) {
    $("#btnEntrar").prop('disabled', !hayCambios);
}

function validarFormulario(nombreForm) {
    $(nombreForm).validate({
        rules: {
            nombreUsuario: {
                required: true,
                maxlength: 100
            },
            passwordUsuario: {
                required: true,
                maxlength: 200
            }

        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 100 caracteres."
            },
            passwordUsuario: {
                required: "Debe introducir el password de usuario.",
                maxlength: "Longitud máx 200 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            solicitudPost("api/identidad/login", nombreForm, true)
                    .then(response => {
                        if(response.isError === 1) {
                            mostrarMensajeError("Acceso Denegado", response.result);
                        } else {
                            const acceso = () => window.location.replace(response.result);
                            mostrarMensaje(`Bienvenido ${response.data.nombre}`, "Acceso Permitido.", "success", (response.isUrl)? acceso : null);
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
