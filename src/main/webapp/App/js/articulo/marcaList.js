
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {ROLES} from '../constantes.mjs';

const idInputNombre = "#nombre";
const idFormBusquedaMarca = "#frmBuscarMarca";
const idTablaMarca = "#tablaMarca"; 
const idBtnBuscar = "#btnBuscar";
const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idBtnCancelar = "#btnCancelar";
const idBtnDetalle = "#btnDetalle";
const idInputUserRol = "#userRol";

const User = {
    rol: ROLES.USER
};

document.addEventListener("DOMContentLoaded", function () {
        const tablaMarca = document.querySelector(idTablaMarca);
        const btnBuscar = document.querySelector(idBtnBuscar);
        const btnCrear = document.querySelector(idBtnCrear);
        const btnModificar = document.querySelector(idBtnModificar);
        const btnEliminar = document.querySelector(idBtnEliminar);
        const btnDetalle = document.querySelector(idBtnDetalle);
        const btnCancelar = document.querySelector(idBtnCancelar);

        User.rol = document.querySelector(idInputUserRol).value;
   
        if(User.rol === ROLES.ADMIN || User.rol === ROLES.USER) {
            btnCrear.disabled = false;
        }
        
        validarFormulario(idFormBusquedaMarca);
        
        observeRowSelectedChange(tablaMarca, onDetectarFilaSeleccionada);
    
        btnModificar.addEventListener('click', () => {
            const idMarca = tablaMarca.getAttribute('data-rowselected'); //data-rowSelected
            window.location.href = (`articulo/marcas/edit/${idMarca}`);
        });
    
        btnCrear.addEventListener('click', () => {
            window.location.href = (`articulo/marcas/crear`);
        });
    
        btnEliminar.addEventListener('click', () => {
            const idMarca = tablaMarca.getAttribute('data-rowselected'); //data-rowSelected
            borrarMarca(idMarca);
        });
        
        btnCancelar.addEventListener('click', () => {
            window.location.href = "articulo";
        });
        
        //Dispara el evento de clic en el botón para que haga una busqueda inicial.
        btnBuscar.click();
});

function onDetectarFilaSeleccionada(hayFilaSeleccionada) {
    $(idBtnEliminar).prop('disabled', !hayFilaSeleccionada);
    $(idBtnModificar).prop('disabled', !hayFilaSeleccionada);
    $(idBtnDetalle).prop('disabled', !hayFilaSeleccionada);
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            nombre: {
                required: false,
                maxlength: 100
            }, 
            descripcion: {
                required: false,
                maxlength: 100
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                maxlength: "Longitud máx 100 caracteres."
            },
            descripcion: {
                maxlength: "Longitud máx 100 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const formData = getDatosForm(idForm);
            const url = `api/marca/find_marcas?${formData}`;

            solicitudGet(url, "", false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaMarca(response.data);
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

/**
 * Función que rellena una tabla HTML con las marcas proporcionados.
 * @param {Array} marcas - Un array de objetos de marca que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaMarca(marcas) {
    const tablaMarca = document.querySelector(idTablaMarca);
    const cuerpoTablaMarca = tablaMarca.querySelector('tbody');
    const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaMarca.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = marcas.map(marca => {
        return `<tr id="${marca.id}">
                <td>${marca.id}</td>
                <td>${marca.nombre}</td>
                <td>${marca.descripcion}</td>
                </tr>`;
    }).join('');

    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaMarca.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaMarca);
}

function borrarMarca(marcaId) {
    mostrarMensajeOpcion("Borrar Marca", `¿Quieres realmente borrar los datos de la marca con id ${marcaId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/marca/delete/${marcaId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Marca Borrada.", `Se han borrado correctamente los datos de la marca.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaMarca);
                                        }
                                    })
                                    .catch(error => {
                                        // Maneja el error aquí
                                        console.error("Error:", error);
                                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                                    });
                        } else if (result.isDenied) {
                            //denegado
                        } else if (result.isDismissed) {
                            //cancelado
                        }
                    });
}

