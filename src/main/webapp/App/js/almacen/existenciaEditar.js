
import { solicitudGet, setImageSelected, solicitudPut, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect, detectarCambiosFormulario } from '../comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from '../alertasSweetAlert2.mjs';
import {DEFAULT_IMG, DISPONIBILIDAD} from '../constantes.mjs';

const idSelectArticulo = "#articulo";
const idSelectArticuloProveedor = "#articuloProveedor";
const idSelectAlmacen = "#almacen";
const idSelectEmplazamiento = "#emplazamiento";
const idSelectDisponibilidad = "#disponibilidad";

const idInputIdExistencia = "#existencia_id";
const idInputPrecio = "#precio";
const idInputFechaCompra = "#fechaCompra";
const idInputComprador = "#comprador";
const idInputFechaNoDisponible = "#fechaNoDisponible";
const idFormExistencia = "#frmModificarExistencia";
const idBtnGuardar = "#btnGuardar";
const idBtnCancelar = "#btnCancelar";
const idBtnDeshacerCambiosExistencia = "#btnDeshacerCambiosExistencia";

const idContenedorImgArticulo = "#contenedorImgArticulo";
const idInputHideImgArticulo = "#imagenArticuloB64"; //Input oculto.
const idInputImgArticulo = "#inputImgArticulo";
const idImgArticulo = "#imgArticulo";
const idLabelImgArticulo = "#textoImagenArticulo";

const fechaHoy = new Date().toISOString().split("T")[0];
let oldExistencia;

$(document).ready(function () {
    const selectArticulo = document.querySelector(idSelectArticulo);
    const selectArticuloProveedor = document.querySelector(idSelectArticuloProveedor);
    const selectAlmacen = document.querySelector(idSelectAlmacen);
    const selectEmplazamiento = document.querySelector(idSelectEmplazamiento);
    const inputIdExistencia = document.querySelector(idInputIdExistencia);
    
    //Imagen proveedor
    const contenedorImgArticulo = document.querySelector(idContenedorImgArticulo);
    const inputImgArticulo = document.querySelector(idInputImgArticulo);
    const imgArticulo = document.querySelector(idImgArticulo);
    const inputHideImgArticulo = document.querySelector(idInputHideImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    const inputPrecio = document.querySelector(idInputPrecio);
    const inputFechaCompra = document.querySelector(idInputFechaCompra);
    
    const btnDeshacerCambiosExistencia = document.querySelector(idBtnDeshacerCambiosExistencia);
    const btnCancelar = document.querySelector(idBtnCancelar);


    //validarFormulario(idFormExistencia);

    getExistencia(inputIdExistencia.value);

    btnCancelar.addEventListener('click', () => {
        window.location.href = "almacen/existencias";
    });

    btnDeshacerCambiosExistencia.addEventListener('click', () => {
        fillFielsExistencia(oldArticulo);
    });

});

function onDetectarCambiosModificarExistencia(hayCambios) {
    $("#btnGuardar").prop('disabled', !hayCambios);
    $("#btnDeshacerCambiosExistencia").prop('disabled', !hayCambios);
}

function getExistencia(idExistencia) {
    solicitudGet(`api/existencia/existencia?idExistencia=${idExistencia}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    oldExistencia = response.data;
                    console.log(oldExistencia);
                    fillFielsExistencia(oldExistencia);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


//HAY QUE MODIFICAR ESTO PARA RELLENAR LOS DATOS DE EXISTENCIA. -------!!!!! EN CONSTRUCCIÓN.
function fillFielsExistencia(existencia) {
    const form = document.querySelector(idFormExistencia);
    debugger;
    
    const selectArticulo = form.querySelector(idSelectArticulo);
    const selectArticuloProveedor = form.querySelector(idSelectArticuloProveedor);
    const selectAlmacen = form.querySelector(idSelectAlmacen);
    const selectEmplazamiento = form.querySelector(idSelectEmplazamiento);
    const selectDisponibilidad = form.querySelector(idSelectDisponibilidad);
    const inputPrecio = form.querySelector(idInputPrecio);
    const inputFechaCompra = form.querySelector(idInputFechaCompra);
    const inputComprador = form.querySelector(idInputComprador);
    
    const inputFechaNoDisponible = form.querySelector(idInputFechaNoDisponible);

    const inputHideImgArticulo = form.querySelector(idInputHideImgArticulo);
    const imgArticulo = form.querySelector(idImgArticulo);
    const labelImgArticulo = document.querySelector(idLabelImgArticulo);
    
    inputPrecio.value = existencia.precio;
    inputComprador.value = existencia.comprador ?? "";
    inputFechaCompra.value = existencia.fechaCompra ?? "";
    inputFechaNoDisponible.value = existencia.fechaNoDisponible ?? "";
    
    const articuloId = existencia.articulo.id;
    const proveedorId = existencia.proveedor.id;
    const emplazamientoId = existencia.emplazamiento.id;
    const almacenId = existencia.emplazamiento.almacen.id;
    const disponible = existencia.disponible === DISPONIBILIDAD.DISPONIBLE.name ? DISPONIBILIDAD.DISPONIBLE.value : DISPONIBILIDAD.NO_DISPONIBLE.value;
    
    // Imagen: usa la imagen del articulo si existe, de lo contrario la imagen por defecto
    const imagenValida = existencia.articulo.imagen && existencia.articulo.imagen.trim() !== "";
    inputHideImgArticulo.value = imagenValida ? existencia.articulo.imagen : "";
    imgArticulo.src = imagenValida ? existencia.articulo.imagen : DEFAULT_IMG.ARTICULO;
    
    labelImgArticulo.textContent = "";
    
    seleccionarValorSelect(selectDisponibilidad, disponible);
    
    
    
    
    const cargaImputSelectArticuloProveedor = () => {
         solicitudGet(`api/articulo/articulo?idArticulo=${articuloId}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            let articulo = response.data;
                            console.log("articulo: ");
                            console.log(articulo);
                            debugger;
                            fillInputSelect(selectArticuloProveedor, articulo.listaProveedores, '',);
                            seleccionarValorSelect(selectArticuloProveedor, proveedorId);
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        
    };
    
    
    const promesaSelctArticulo = cargarInputSelect(selectArticulo, "api/articulo/articulos", '', articuloId, () => {
        cargaImputSelectArticuloProveedor();
        
        selectArticulo.addEventListener('change', (e) => {
            const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

            solicitudGet(`api/articulo/articulo?idArticulo=${optionSelected.value}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            const articulo = response.data;
                            fillInputSelect(selectArticuloProveedor, articulo.listaProveedores, 'Seleccione un proveedor');

                            // Imagen: usa la imagen del articulo si existe, de lo contrario la imagen por defecto
                            const imagenValida = articulo.imagen && articulo.imagen.trim() !== "";
                            inputHideImgArticulo.value = imagenValida ? articulo.imagen : "";
                            imgArticulo.src = imagenValida ? articulo.imagen : DEFAULT_IMG.ARTICULO;

                            labelImgArticulo.textContent = "";
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        });
        
        selectArticuloProveedor.addEventListener("change", (e) => {
            const optionSelected = e.target.selectedOptions[0];
            const precio = optionSelected.getAttribute("data-precio");
            inputPrecio.value = precio || "";
        });
    });
    
    
    
     
    const cargaImputSelectEmpalzamiento = () => {
         solicitudGet(`api/almacen/almacen?idAlmacen=${almacenId}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            let almacen = response.data;
                            fillInputSelect(selectEmplazamiento, almacen.listaEmplazamientos, '',);
                            seleccionarValorSelect(selectEmplazamiento, emplazamientoId);
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        
    };
    

    //Carga el select articulo.
    const promesaSelctAlmacen = cargarInputSelect(selectAlmacen, "api/almacen/almacenes", '', almacenId, () => {
        cargaImputSelectEmpalzamiento();
        
        selectAlmacen.addEventListener('change', (e) => {
            const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

            solicitudGet(`api/almacen/almacen?idAlmacen=${optionSelected.value}`, "", false)
                    .then(response => {
                        if (response.isError === 1) {
                            mostrarMensajeError("Se ha producido un error", response.result);
                        } else {
                            let almacen = response.data;
                            fillInputSelect(selectEmplazamiento, almacen.listaEmplazamientos, 'Seleccione un emplazamiento');
                        }
                    })
                    .catch(error => {
                        // Maneja el error aquí
                        console.error("Error:", error);
                        mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
                    });
        });
    });
    
    
    Promise.all([promesaSelctArticulo, promesaSelctAlmacen])
        .then(() => {
            onDetectarCambiosModificarExistencia(false);
            detectarCambiosFormulario(idFormExistencia, onDetectarCambiosModificarExistencia);
        })
        .catch(error => {
            console.error("Error al cargar selects:", error);
        });
}



