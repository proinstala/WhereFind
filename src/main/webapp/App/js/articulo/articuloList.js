
import { solicitudGet, solicitudPut, getDatosForm, addRowSelected, fillInputSelect, cargarInputSelect, observeRowSelectedChange, deleteRowSelectedTable } from '../comunes.mjs?v=20241021_184300';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {ROLES} from '../constantes.mjs';

const idSelectMarca = "#marca";
const idInputNombre = "#nombre";
const idFormBusquedaArticulo = "#frmBuscarArticulo";
const idTablaArticulos = "#tablaArticulos"; 
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
    const tablaArticulos = document.querySelector(idTablaArticulos);
    const selectMarca = document.querySelector(idSelectMarca);
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
    
    const promesaMarca = cargarInputSelect(selectMarca, "api/marca/marcas", 'Todas', false, "");
    
    validarFormulario(idFormBusquedaArticulo);
    
    observeRowSelectedChange(tablaArticulos, onDetectarFilaSeleccionada);

    btnModificar.addEventListener('click', () => {
        const idArticulo = tablaArticulos.getAttribute('data-rowselected'); //data-rowSelected
        window.location.href = (`articulo/articulos/edit/${idArticulo}`);
    });
    
    btnDetalle.addEventListener('click', () => {
        const idArticulo = tablaArticulos.getAttribute('data-rowselected'); //data-rowSelected
        window.location.href = (`articulo/articulos/detalle/${idArticulo}`);
    });

    btnCrear.addEventListener('click', () => {
        window.location.href = (`articulo/articulos/crear`);
    });

    btnEliminar.addEventListener('click', () => {
        const idArticulo = tablaArticulos.getAttribute('data-rowselected'); //data-rowSelected
        borrarArticulo(idArticulo);
    });
    
    btnCancelar.addEventListener('click', () => {
        window.location.href = "articulo";
    });
    
   
    Promise.all([promesaMarca])
        .then(() => {
            //Dispara el evento de clic en el botón para que haga una busqueda inicial.
            btnBuscar.click();
        })
        .catch(error => {
            console.error("Error al cargar selects: ", error);
        });
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
            }
        },//Fin de reglas ----------------
        messages: {
            nombre: {
                maxlength: "Longitud máx 100 caracteres."
            }
        },//Fin de msg  ------------------

        submitHandler: function () {
            const formData = getDatosForm(idForm);
            const url = `api/articulo/find_articulos?${formData}`;

            solicitudGet(url, "", false)
                .then(response => {
                    if (response.isError === 1) {
                        mostrarMensajeError("Se ha producido un error", response.result);
                    } else {
                        rellenarTablaArticulos(response.data);
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
 * Función que rellena una tabla HTML con los articulos proporcionados.
 * @param {Array} articulos - Un array de objetos de articulos que contiene los datos para cada fila de la tabla.
 */
function rellenarTablaArticulos(articulos) {
    const tablaArticulos = document.querySelector(idTablaArticulos);
    const cuerpoTablaArticulos = tablaArticulos.querySelector('tbody');
    const inputUserRol = document.querySelector(idInputUserRol);  //Admin o User

    tablaArticulos.setAttribute('data-rowselected', -1); //Establece a -1 el rowselected para indicar que no se ha seleccionado ninguna fila.

    //Crear el contenido HTML de todas las filas a partir de los datos de provincias
    let filasHTML = articulos.map(articulo => {
        return `<tr id="${articulo.id}">
                <td>${articulo.id}</td>
                <td>${articulo.nombre}</td>
                <td>${articulo.descripcion}</td>
                <td>${articulo.referencia}</td>
                <td>${articulo.marca.nombre}</td>
                <td>${articulo.modelo}</td>
                <td>${articulo.stockMinimo}</td>
                <td>${articulo.stockActual}</td>
                </tr>`;
    }).join('');


    //Asignar el contenido HTML generado al cuerpo de la tabla, reemplazando cualquier contenido existente
    cuerpoTablaArticulos.innerHTML = filasHTML;

    //Añadir eventos de selección de filas a la tabla recién generada
    addRowSelected(cuerpoTablaArticulos);
}


function borrarArticulo(articuloId) {
    mostrarMensajeOpcion("Borrar Articulo", `¿Quieres realmente borrar los datos del articulo con id ${articuloId}?`)
                    .then((result) => {
                        if (result.isConfirmed) {
                            solicitudPut(`api/articulo/delete/${articuloId}`, "", true)
                                    .then(response => {
                                        if (response.isError === 1) {
                                            mostrarMensajeError("No se puede borrar los datos", response.result);
                                        } else {
                                            mostrarMensaje("Articulo Borrada.", `Se han borrado correctamente los datos del articulo.`, "success");

                                            //Elimina la fila seleccionada de la tabla.
                                            deleteRowSelectedTable(idTablaArticulos);
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




