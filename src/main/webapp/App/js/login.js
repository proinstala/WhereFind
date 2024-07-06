
$(document).ready(function () {
    
    validarFormulario("#frmLogin");
    
});

function validarFormulario(nombreForm) {
    $(nombreForm).validate({
        rules: {
            nombreUsuario: {
                required: true, 
                maxlength: 5
            },
            passwordUsuario: {
                required: true, 
                maxlength: 60
            }
            
        },//Fin de reglas ----------------
        messages: {
            nombreUsuario: {
                required: "Debe introducir el nombre de usuario.",
                maxlength: "longitu máx 5 caracteres."
            },
            passwordUsuario: {
                required: "Debe introducir el password de usuario."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            console.log(getDatosForm());
            window.location = "App/web/inicio/inicio.jsp";
        },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
            
        },
        complete: function () {            
            //window.location.reload(true);
        }
    });//Fin Validate
}

function getDatosForm() {
    const data = new FormData($("#frmLogin")[0]);
    return data;
}
