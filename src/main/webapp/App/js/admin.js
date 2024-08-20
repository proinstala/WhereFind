import { solicitudGet } from './comunes.mjs';

const adminLoadListUsers = (idElement) => {

    const adminLoadListUsersCallBack = (response, idElement)  => {

        let tableRef = document.getElementById(idElement).getElementsByTagName('tbody')[0];

        function ResetSelected() {
            for (const row of tableRef.rows){

                if (row) {
                    row.classList.remove("selected");
                }
            }
        }

        function UsuarioActivoOrEspecial(row, user) {

            if (user.activo) {

                if (user.rol == "Admin") {
                    row?.classList.add("user-admin");
                }

                return "checked"
            }
            else {
                row?.classList.add("user-deleted");
            }
        }



        for (const element of response.user){
            let row = tableRef.insertRow();
            row.setAttribute("data-id", element.id);
            row.innerHTML =
                "<td>" + element.id + "</td>" +
                "<td><div div class='checkbox-style'><input type='checkbox' "+UsuarioActivoOrEspecial(row, element)+" force-disabed=true disabled readonly id='switch-" + element.id + "' /> <label for='switch-" + element.id + "'></label></div></td> " +
                "<td>" + element.userName + "</td>"+
                "<td>" +element.nombre+ "</td>"+
                "<td>" +element.apellidos+ "</td>"+
                "<td>" + element.rol + "</td>";

                row.addEventListener('click', () => {
                    console.log("Id : " + row.getAttribute("data-id"));
                    ResetSelected();
                    row.classList.add("selected");
                });
        }


    }

    solicitudGet("api/identidad/users", adminLoadListUsersCallBack, idElement, true);
}


export { adminLoadListUsers };
