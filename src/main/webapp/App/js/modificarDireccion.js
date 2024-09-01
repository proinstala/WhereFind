
import { solicitudGet, getDatosForm, fillInputSelect, cargarInputSelect, seleccionarValorSelect } from './comunes.mjs';
import { mostrarMensaje, mostrarMensajeError, mostrarMensajeOpcion } from './alertasSweetAlert2.mjs';

const idSelectProvincia = "#provincia";
const idSelectLocalidad = "#localidad";
const idInputIdDireccion = "#direccion_id";
const idInputCalle = "#calle";
const idInputNumero = "#numero";
const idInputCodigoPostal = "#codigoPostal";
const idFormDireccion = "#frmModificarDireccion";
const idBtnDeshacerCambiosDireccion = "#btnDeshacerCambiosDireccion";

let oldDireccion;

$(document).ready(function () {
    const selectProvincia = document.querySelector(idSelectProvincia);
    const selectLocalidad = document.querySelector(idSelectLocalidad);
    const inputIdDireccion = document.querySelector(idInputIdDireccion);
    const btnDeshacerCambiosDireccion = document.querySelector(idBtnDeshacerCambiosDireccion);
    
    getDireccion(inputIdDireccion.value);
    
    btnDeshacerCambiosDireccion.addEventListener('click', () => {
        fillFielsDireccion(oldDireccion);
    });
});


function getDireccion(idDireccion) {
    solicitudGet(`api/direccion/direccion?idDireccion=${idDireccion}`, "", true)
            .then(response => {
                if (response.isError === 1) {
                    mostrarMensajeError("Se ha producido un error", response.result);
                } else {
                    debugger;
                    oldDireccion = response.data;
                    fillFielsDireccion(oldDireccion);
                }
            })
            .catch(error => {
                // Maneja el error aquí
                console.error("Error:", error);
                mostrarMensajeError("Error", "No se ha podido realizar la acción por un error en el servidor.");
            });
}


function fillFielsDireccion(direccion) {
    const form = document.querySelector(idFormDireccion);
    
    const inputCalle = form.querySelector(idInputCalle);
    const inputNumero = form.querySelector(idInputNumero);
    const inputCodigoPostal = form.querySelector(idInputCodigoPostal);
    const selectProvincia = form.querySelector(idSelectProvincia);
    const selectLocalidad = form.querySelector(idSelectLocalidad);
    
    inputCalle.value = direccion.calle;
    inputNumero.value = direccion.numero;
    inputCodigoPostal.value = direccion.codigoPostal;
    
    cargarInputSelect(selectProvincia, "api/provincia/provincias", '', direccion.localidad.provincia.id, () => {
            //Crea un objeto con la información de la provincia seleccionada (id y nombre).
            const jsonProvincia = {
                id: selectProvincia.value,
                nombre: selectProvincia.textContent 
            };

            const jsonProvinciaString = JSON.stringify(jsonProvincia); //Convierte el objeto jsonProvincia a una cadena JSON.
            const encodedJsonProvincia = encodeURIComponent(jsonProvinciaString); //Codifica la cadena JSON para que sea segura al incluirla en la URL.

            //Llama a cargarInputSelect para llenar el select de localidades con los datos obtenidos del select provincia.
            cargarInputSelect(selectLocalidad, `api/localidad/localidades?jsonProvincia=${encodedJsonProvincia}`, '', direccion.localidad.id, () => {
                selectProvincia.addEventListener('change', (e) => {
                    const optionSelected = e.target.selectedOptions[0]; //Obtiene la opción seleccionada del select.

                    //Crea un objeto con la información de la provincia seleccionada (id y nombre).
                    const jsonProvincia = {
                        id: optionSelected.value,
                        nombre: optionSelected.textContent 
                    };

                    const jsonProvinciaString = JSON.stringify(jsonProvincia); //Convierte el objeto jsonProvincia a una cadena JSON.
                    const encodedJsonProvincia = encodeURIComponent(jsonProvinciaString); //Codifica la cadena JSON para que sea segura al incluirla en la URL.

                    //Llama a cargarInputSelect para llenar el select de localidades con los datos obtenidos del select provincia.
                    cargarInputSelect(selectLocalidad, `api/localidad/localidades?jsonProvincia=${encodedJsonProvincia}`, 'Seleccione');
                });
            });
    });
}