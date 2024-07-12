
$(document).ready(function () {

    const form = document.getElementById("frmLogin");
    form.addEventListener("submit", loginSubmit);

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

        // submitHandler: function () {
        //     console.log("submitHandler");
        // },
       // Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error

        },
        complete: function () {
            console.log("complete");
        }
    });//Fin Validate
}

function getDatosForm() {
    const data = new FormData($("#frmLogin")[0]);
    return data;
}

function loginSubmit(event) {
    event.preventDefault();

    let formData = "nombreUsuario=" + $("#nombreUsuario").val() + "&passwordUsuario=" + $("#passwordUsuario").val();

    let request = new XMLHttpRequest();
    request.open("POST", "api/identidad/login");
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded')
    request.send(formData);

    request.onload = () => {
        if (request.status == 200) {
            let myArr = JSON.parse(request.responseText);
            if (myArr != null) {
                if (myArr.iserror) {
                    mostrarError(myArr.result);
                }
                else {
                    window.location = myArr.result;
                }
            }
        }
    }

    request.onerror = () => {
        mostrarError("No se ha podido realizar la acción por un error en el servidor.");
    }
}

function mostrarError(msg) {
    const msgError = document.getElementById("info-error");
    if (msgError) {
        msgError.innerHTML = msg;
        msgError.style.display = "block";
    }
}
