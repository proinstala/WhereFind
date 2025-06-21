
import {solicitudPost, setImageSelected, fillInputSelect, cargarInputSelect, vaciarSelect, detectarCambiosFormulario, resetCamposForm } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';

const idSelectDireccion = "#direccion";
const idInputNombre = "#nombre";
const idInputDescripcion = "#descripcion";
const idInputPaginaWeb = "#paginaWeb";
const idFormProveedor = "#frmCrearProveedor";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosProveedor = "#btnDeshacerCambiosProveedor";




$(document).ready(function () {
    const selectDireccion = document.querySelector(idSelectDireccion);
    const btnDeshacerCambiosProveedor = document.querySelector(idBtnDeshacerCambiosProveedor);
    const btnCancelar = document.querySelector(idBtnCancelar);
    
    

    //Carga el select direccion.
    const promesaCargaSelectDireccion = cargarInputSelect(selectDireccion, "api/direccion/direcciones", 'Seleccione una dirección', false, () => {});
    
    Promise.all([promesaCargaSelectDireccion])
        .then(() => {
            onDetectarCambiosCrearProveedor(false);
            detectarCambiosFormulario(idFormProveedor, onDetectarCambiosCrearProveedor);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });

    validarFormulario(idFormProveedor);


    btnCancelar.addEventListener('click', () => {
        window.location.href = "proveedor/proveedores";
    });

    btnDeshacerCambiosProveedor.addEventListener('click', () => {
        resetCamposForm(idFormProveedor);
        
        onDetectarCambiosCrearContacto(false);
        detectarCambiosFormulario(idFormProveedor, onDetectarCambiosCrearContacto);
    });
    
    const idInputHideImgProveedor = "#imagenProveedorB64"; //Input oculto.
    const idInputImgProveedor = "#inputImgProveedor";
    const idImgeProveedor = "#imgProveedor";
    const idLabelImgProveedor = "#textoImagenProveedor";
    
    const inputImgProveedor = document.querySelector(idInputImgProveedor);
    const ImgProveedor = document.querySelector(idImgeProveedor);
    const inputHideImgProveedor = document.querySelector(idInputHideImgProveedor);
    const labelImgProveedor = document.querySelector(idLabelImgProveedor);
    //debugger;
    inputImgProveedor.addEventListener('change', (e) => {
        const defaultImg = ImgProveedor.src;
        const fileImg = e.target.files[0];

        //Establece la imagen seleccionada.
        setImageSelected(fileImg, ImgProveedor, inputHideImgProveedor, defaultImg, 4)
            .then((result) => {
                if (result) {
                    console.log("Imagen establecida correctamente.");

                    // Detecta el cambio de la imagen
                    onDetectarCambios(labelImgProveedor.textContent !== result);

                    labelImgProveedor.textContent = result;
                } else {
                    console.log("No se ha establecido la imagen.");
                }
            })
            .catch((error) => {
                // Maneja cualquier error que ocurra durante la validación o el proceso de establecer la imagen
                console.error('Error:', error);
                inputImgProveedor.value = '';
            });
    });

});


function onDetectarCambiosCrearProveedor(hayCambios) {
    $(idBtnGuardar).prop('disabled', !hayCambios);
    $(idBtnDeshacerCambiosProveedor).prop('disabled', !hayCambios);
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
            paginaWeb: {
                maxlength: 100
            },
            direccion: {
                maxlength: 100,
                min: 1,
                max: 999999
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                required: "Debe introducir el nombre del proveedor.",
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                required: "Debe introducir la descripcion del proveedor.",
                maxlength: "Longitud máx 200 caracteres."
            },
            paginaWeb: {
                maxlength: "Longitud máx 100 caracteres."
            },
            direccion: {
                min: "Valor seleccionado no válido.",
                max: "Valor seleccionado no válido."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const proveedorJSON = getProveedorJson();
            solicitudPost(`api/proveedor/create`, idForm, true, proveedorJSON)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("No se puede crear los datos", response.result);
                        } else {
                            const redireccionar = () => window.location.href = "proveedor/proveedores";
                            mostrarMensaje("Proveedor Creado.", `Se ha creado el proveedor con id "${response.data.id}" correctamente`, "success", redireccionar);
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

function getProveedorJson() {
    // Guardar referencias de los elementos del DOM
    const selectDireccion = document.querySelector(idSelectDireccion);
    const inputNombre = document.querySelector(idInputNombre);
    const inputDescripcion = document.querySelector(idInputDescripcion);
    const inputPaginaWeb = document.querySelector(idInputPaginaWeb);

    // Construcción del objeto JSON con validación del código postal
    const proveedorJSON = {
        id: "0",
        nombre: inputNombre.value.trim(),
        descripcion: inputNombre.value.trim(),
        paginaWeb: inputPaginaWeb.value.trim(),
        activo: true,
        direccion: {
            id: selectDireccion.value,
            calle: "",
            numero: "",
            codigoPostal: "0", 
            localidad: null
        }
    };

    // Crear los datos en formato de URL usando URLSearchParams
    const data = new URLSearchParams({
        proveedorJSON: JSON.stringify(proveedorJSON)
    }).toString();

    return data;
}





