
import {solicitudPost, setImageSelected, resetImg, fillInputSelect, cargarInputSelect, vaciarSelect, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG} from '../constantes.mjs';

const idSelectDireccion = "#direccion";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";
const idFormAlmacen = "#frmCrearAlmacen";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosAlmacen = "#btnDeshacerCambiosAlmacen";

$(document).ready(function () {
    const selectDireccion = document.querySelector(idSelectDireccion);
    const btnDeshacerCambiosAlmacen = document.querySelector(idBtnDeshacerCambiosAlmacen);
    const btnCancelar = document.querySelector(idBtnCancelar);

    //Carga el select direccion.
    const promesaCargaSelectDireccion = cargarInputSelect(selectDireccion, "api/direccion/find_direcciones_libres", 'Seleccione una dirección', false, () => {});
    
    Promise.all([promesaCargaSelectDireccion])
        .then(() => {
            onDetectarCambiosCrearAlmacen(false);
            detectarCambiosFormulario(idFormAlmacen, onDetectarCambiosCrearAlmacen);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });

    validarFormulario(idFormAlmacen);


    btnCancelar.addEventListener('click', () => {
        window.location.href = "almacen/almacenes";
    });

    btnDeshacerCambiosAlmacen.addEventListener('click', () => {
        resetCamposForm(idFormAlmacen);
        
        onDetectarCambiosCrearAlmacen(false);
        detectarCambiosFormulario(idFormAlmacen, onDetectarCambiosCrearAlmacen);
    });
    
});


function onDetectarCambiosCrearAlmacen(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosAlmacen).prop('disabled', !hayCambios);
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
            },
            direccion: {
                number: true,
                min: -1,
                max: 999999
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre del almacen.",
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                required: "Debe introducir la descripcion del almacen.",
                maxlength: "Longitud máx 200 caracteres."
            },
            direccion: {
                number: "Valor seleccionado no válido.",
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const almacenJSON = getAlmacenJson();
            solicitudPost(`api/almacen/create`, idForm, true, almacenJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = "almacen/almacenes";
                            mostrarMensaje("Almacen Creado.", `Se ha creado el almacen con id "${response.data.id}" correctamente`, "success", redireccionar);
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        },
        //Función error de respuesta
        errorPlacement: function (error, element) {
            error.insertAfter(element); // Esto colocará el mensaje de error después del elemento con error
        }
    });//Fin Validate
}

function getAlmacenJson() {
    // Guardar referencias de los elementos del DOM
    const selectDireccion = document.querySelector(idSelectDireccion);
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    
    // Obtener el valor seleccionado
    const direccionId = parseInt(selectDireccion.value, 10);

    const almacenJSON = {
        id: "0",
        nombre: inputNombre.value.trim(),
        descripcion: inputDescripcion.value.trim(),
        activo: true,
        direccion: direccionId === -1 ? null : {
            id: direccionId,
            calle: "",
            numero: "",
            codigoPostal: "0", 
            localidad: null
        }
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        almacenJSON: JSON.stringify(almacenJSON)
    }).toString();

    return data;
}





