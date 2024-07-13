
$(document).ready(function () {

    const form = document.getElementById("frmLogin");
    //form.addEventListener("submit", loginSubmit);

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
            solicitudLogin("api/identidad/login", getDatosForm());
        },
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
    //const data = new FormData($("#frmLogin")[0]);
    
    /*
    const formData = $("#frmLogin").serializeArray();
    let data = {};
    $(formData).each(function(index, obj){
        data[obj.name] = obj.value;
    });
    */
   
    const formData = $("#frmLogin").serialize();

    return formData;
}

function loginSubmit(event) {
    event.preventDefault();

    let formData = "nombreUsuario=" + $("#nombreUsuario").val() + "&passwordUsuario=" + $("#passwordUsuario").val();

    let request = new XMLHttpRequest();
    request.open("POST", "api/identidad/login");
    request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
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
    
    return false;
}

function mostrarError(msg) {
    const msgError = document.getElementById("info-error");
    if (msgError) {
        msgError.innerHTML = msg;
        msgError.style.display = "block";
    }
}


function solicitudLogin(url, data) {
    $.ajax({
        url: url,
        type: 'POST',
        data: data,
        //Antes del envio
        beforeSend: function () {
            
        },
        success: function (response) {
            //debugger;
            //console.log(response);

            if(response.isError === 1) {
                mostrarMensajeAcceso("Acceso Denegado ", response.result, "error");
            } else {
                const acceso = () => window.location.replace(response.result);
                mostrarMensajeAcceso("Bienvenido", "Acceso Permitido.", "success", acceso);
            }
        },
        //Funcion error de respuesta    
        error: function (jqXHR, textStatus, errorThrown) {
            //errorAjax(jqXHR, textStatus, errorThrown, urlServlet);
            console.log("error -----------!!!!!!");
            console.log(jqXHR);
            console.log(textStatus);
            console.log(errorThrown);
        },
        complete: function () {
           
        }
    });
}



function mostrarMensajeAcceso(titulo="titulo", contenido="contenido", icon="success", callback) {
    Swal.fire({
        title: titulo,
        html: `<span class="swal-contenido">${contenido}</span>`,
        icon: icon,
        confirmButtonText: 'Aceptar',
        width: '40%',
        timer: 2000,

        customClass: {
            title: 'swal-titulo',
            confirmButton: 'swal-boton',
            popup: 'custom-icon' 
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const selectedValue = result.value;
            console.log('value:', selectedValue);
        }
     
        if (typeof callback === "function") {
            callback();
        }
    });;
}
