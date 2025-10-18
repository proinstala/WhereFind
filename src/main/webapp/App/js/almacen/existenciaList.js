
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeAdvertencia, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {ROLES, ICONOS_TABLA} from '../constantes.mjs';

const idSelectMarca = "#marca";
const idSelectAlmacen = "#almacen";
const idInputArticulo = "#articulo";
const idInputReferencia = "#referencia";
const idInputSku = "#sku";
const idInputNombreEmplazamiento = "#nombreEmplazamiento";
const idFormBusquedaExistencia = "#frmBuscarExistencia";
const idTablaExistencias = "#tablaExistencias"; 
const idBtnBuscar = "#btnBuscar";
const idBtnModificar = "#btnModificar";
const idBtnCrear = "#btnCrear";
const idBtnEliminar = "#btnEliminar";
const idBtnCancelar = "#btnCancelar";
const idBtnDetalle = "#btnDetalle";
const idInputUserRol = "#userRol";

const User = {
    rol: ""
};

document.addEventListener("DOMContentLoaded", function () {
    const tablaExistencias = document.querySelector(idTablaExistencias);
    const selectMarca = document.querySelector(idSelectMarca);
    const selectAlmacen = document.querySelector(idSelectAlmacen);
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
    
    if(User.rol === ROLES.USER) {
        btnEliminar.disabled = true;
    }
    
    const promesaMarca = cargarInputSelect(selectMarca, "api/marca/marcas", 'Todas', false, "");
    const promesaAlmacen = cargarInputSelect(selectAlmacen, "api/almacen/almacenes", 'Todos', false, "");
    
    validarFormulario(idFormBusquedaExistencia);
    
    observeRowSelectedChange(tablaExistencias, onDetectarFilaSeleccionada);

    btnModificar.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); //data-rowSelected
        window.location.href = (`almacen/existencias/edit/${idExistencia}`);
    });
    
    btnDetalle.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); //data-rowSelected
        window.location.href = (`almacen/existencias/detalle/${idExistencia}`);
    });

    btnCrear.addEventListener('click', () => {
        window.location.href = (`almacen/existencias/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        const idExistencia = tablaExistencias.getAttribute('data-rowselected'); //data-rowSelected
        borrarExistencia(idExistencia);
    });
    
    btnCancelar.addEventListener('click', () => {
        window.location.href = "almacen";
    });
    
   
    Promise.all([promesaMarca, promesaAlmacen])
        .then(() => {
            //Dispara el evento de clic en el botón para que haga una busqueda inicial.
            btnBuscar.click();
        })
        .catch(error => {
            console.error("Error al cargar selects: ", error);
        });
});

function onDetectarFilaSeleccionada(hayFilaSeleccionada) {
    if(User.rol === ROLES.ADMIN) {
        $(idBtnEliminar).prop('disabled', !hayFilaSeleccionada);
    }
    
    $(idBtnModificar).prop('disabled', !hayFilaSeleccionada);
    $(idBtnDetalle).prop('disabled', !hayFilaSeleccionada);
}

function validarFormulario(idForm) {
    $(idForm).validate({
        rules: {
            articulo: {
                required: false,
                maxlength: 100
            },
            referencia: {
                required: false,
                maxlength: 100
            },
            sku: {
                required: false,
                maxlength: 50
            },
            marca: {
                number: true,
                min: -1,
                max: 2000000000
            },
            emplazamiento: {
                required: false,
                maxlength: 100
            }
            
        },//Fin de reglas ----------------
        messages: {
            articulo: {
                maxlength: "Longitud máx 100 caracteres."
            },
            referencia: {
                maxlength: "Longitud máxima: 100 caracteres."
            },
            sku: {
                maxlength: "Longitud máxima: 50 caracteres."
            },
            marca: {
                number: "Debe ser un número válido.",
                min: "El valor mínimo permitido es -1.",
                max: "El valor máximo permitido es 2.000.000.000."
            },
            emplazamiento: {
                maxlength: "Longitud máxima: 100 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const formData = getDatosForm(idForm);
            const url = `api/existencia/find_existencias?${formData}`;

            solicitudGet(url, "", false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaExistencias(response.data);
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
 * Función que rellena una tabla HTML con los existencias proporcionados.
 * @param {Array} existencias - Un array de objetos de existencias que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaExistencias(existencias) {
    const tablaExistencias = document.querySelector(idTablaExistencias);
    const cuerpoTablaExistencias = tablaExistencias.querySelector('tbody');
    const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaExistencias.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = existencias.map(existencia => {
        let fila = `<tr id="${existencia.id}">
                <td>${existencia.id}</td>
                <td>${existencia.articulo.nombre}</td>
                <td>${existencia.articulo.descripcion}</td>
                <td>${existencia.articulo.referencia}</td>
                <td>${existencia.articulo.marca.nombre}</td>
                <td>${existencia.sku || ''}</td>
                <td>${existencia.emplazamiento.almacen.nombre}</td>
                <td>${existencia.emplazamiento.nombre}</td>
                <td class="texto--centrado">${existencia.disponible === 'DISPONIBLE' ? ICONOS_TABLA.CHECK : ICONOS_TABLA.NO_CHECK}</td>
                </tr>`;
        return fila;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaExistencias.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaExistencias);
}


function borrarExistencia(existenciaId) {
    
    if(User.rol === ROLES.USER) {
        mostrarMensajeAdvertencia("No Tienes Permisos.", "No puedes borrar los datos de una existencia.");
        return;
    }
    
    mostrarMensajeOpcion("Borrar Existencia", `¿Quieres realmente borrar los datos del existencia con id ${existenciaId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/existencia/delete/${existenciaId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Existencia Borrada.", `Se han borrado correctamente los datos del existencia.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaExistencias);
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





