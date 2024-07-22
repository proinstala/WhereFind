import { solicitudPost, setImageSelected } from './comunes.mjs';
import { mostrarMensajeAcceso } from './alertasSweetAlert2.mjs';

$(document).ready(function () {
    validarFormulario("#frmUser");

    const btnFoto = document.querySelector('#btnFoto2');
    const contenedorImg = document.querySelector('#imgUsuario');
    const inputHide64 = document.querySelector('#imagenUsuarioB64');

    btnFoto.addEventListener('change', (e) => {
        const defaultUserImg = contenedorImg.src;
        const fileImg = e.target.files[0];

        //Establece la imagen seleccionada.
        setImageSelected(fileImg, contenedorImg, inputHide64, defaultUserImg)
            .then((result) => {
                if (result) {
                    //console.log("Imagen establecida correctamente.");
                } else {
                    console.log("No se ha establecido la imagen.");
                }
            })
            .catch((error) => {
                // Maneja cualquier error que ocurra durante la validación o el proceso de establecer la imagen
                console.error('Error:', error);
                btnFoto.value = '';
            });
    });
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
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const registrarCallBack = (response)  => {
                if (response.isError === 1) {
                    mostrarMensajeAcceso("No se puede registrar", response.result, "error");
                } else {
                    const acceso = () => window.location.replace(response.result);
                    mostrarMensajeAcceso(`Se ha creado correctamente el usuario de ${response.user.nombre}`, "Creado Usuario.", "success", (response.isUrl)? acceso : null);
                }
            };

            solicitudPost("api/identidad/create", registrarCallBack, nombreForm, true);
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
