import { solicitudPost } from './comunes.mjs';

$(document).ready(function () {
    validarFormulario("#frmUser");
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
            },
            nombreRealUsuario: {
                required: true,
                maxlength: 100
            },
            apellidoRealUsuario: {
                required: true,
                maxlength: 100
            },
            emailUsuario: {
                required: true,
                email: true
            },


        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "Longitud máx 20 caracteres."
            },
            passwordUsuario: {
                required: "Debe introducir el password de usuario.",
                maxlength: "Longitud máx 60 caracteres."
            },
            nombreRealUsuario: {
                required: "Debe introducir su nombre.",
                maxlength: "Longitud máx 60 caracteres."
            },
            apellidoRealUsuario: {
                required: "Debe introducir sus apellidos.",
                maxlength: "Longitud máx 60 caracteres."
            },
            emailUsuario: {
                required: "Debe introducir su email.",
                email: "Debe introducir un email válido."
            },



        },//Fin de msg  ------------------

        submitHandler: function () {
            solicitudPost("api/identidad/create", "", nombreForm, true);
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
