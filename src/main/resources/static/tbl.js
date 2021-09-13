//---START---
$(document).ready(function () {
    $.ajax({
        url: '/api/user',
        contentType: 'application/json',
        type: "GET",
        success: function (currUser) {

            $('#currUsername').html(currUser.username);
            const currUserRoles = strRoles(currUser.roles);
            $('#currUserRoles').html('[' + currUserRoles + ']');
            $('#titleUserData').html(currUser.name + ': [' + currUserRoles + ']');

            renderCurrentUserData(currUser);
            if (currUserRoles.includes('ADMIN')) {
                $('#adminTab').click();
                getAllUsers();
                getAllRoles()
            } else {
                $('#adminTab').remove();
                $('#userTab').click();
            }
        }
    });
})

//Список ролей
function getAllRoles() {
    fetch("/api/roles").then(
        res => {
            res.json().then(
                data => {
                    const rls = renderRolesSelect(data);
                    document.getElementById("rolesInputAdd").innerHTML = rls;
                    document.getElementById("rolesEdit").innerHTML = rls;
                    document.getElementById("rolesDel").innerHTML = rls;
                }
            )
        }
    )
}

function renderRolesSelect(data) {
    let ret = '';

    if (data.length > 0) {
        data.forEach((r) => {
            let n = r.roleName;
            ret += "<option value=" + n;
            ret += " id=" + r.id;
            ret += n.includes('USER') ? " selected>" : ">";
            ret += n.substring(5) + "</option>";
        })
        return ret;
    }
}
// Таблица с пользователями
function getAllUsers() {
    fetch("/api/users").then(
        res => {
            res.json().then(
                data => {
                    document.getElementById("data").innerHTML = renderData(data);
                }
            )
        }
    )
}


function renderData(data) {
    let ret = '';
    if (data.length > 0) {   //список
        data.forEach((usr) => {
            ret += renderRow(usr);
        })
    } else if (!!data) { //один user проверка на undefined
        ret = renderRow(data);
    }
    return ret;
}

function renderRow(usr) {
    let ret = '';
    ret += "<tr id='user-" + usr.id + "'>";
    ret += renderCol(usr);
    ret += "</tr>";
    return ret;
}

function renderCol(usr) {
    let ret = '';
    ret += "<td>" + usr.id + "</td>";
    ret += "<td>" + usr.name + "</td>";
    ret += "<td>" + usr.username + "</td>";
    ret += "<td>" + usr.email + "</td>";
    let tmpRoles = strRoles(usr.roles);
    ret += "<td class='uRoles'>[" + tmpRoles + "]</td>";

    ret += "<td><a href=\"#\" onclick=\"openEditModal(\'" + usr.id + "\');\"" +
        " class=\"btn btn-primary btn-xs\">Редактир.</a> " +
        "<a href=\"#\" onclick=\"openDeleteModal(\'" + usr.id + "\');\"" +
        " class=\"btn btn-danger btn-xs\">Удалить</a></td>";
    return ret;
}

let renderCurrentUserData = (cu) => {
    let temp = "<tr>";
    temp += "<td>" + cu.id + "</td>";
    temp += "<td>" + cu.name + "</td>";
    temp += "<td>" + cu.username + "</td>";
    temp += "<td>" + cu.email + "</td>";
    temp += "<td class='uRoles'>[" + strRoles(cu.roles) + "]</td>";
    temp += "</tr>"
    document.getElementById("currUserData").innerHTML = temp;
}

//-----ROLES-----------
function strRoles(rls) {
    let ret = '';

    try {           //JSON
        JSON.parse(JSON.stringify(rls))
            .forEach((r) => {
                ret += r.roleName.substring(5) + ', ';
            })
    } catch (e) {                      //массив
        rls.forEach((r) => {
            ret += r.substring(5) + ', ';
        })
    }
    ret = ret.substr(0, ret.length - 2);
    return ret;
}

// function jsonRoles(sel){
//     let roles = [{}, {}];
//     let formS = document.getElementById(sel)
//     for (let n = 0; n < formS.length; n++) {
//         if (formS.options[n].selected) {
//             roles[n]['id'] = formS.options[n].id;
//             roles[n]['rolename'] = formS.options[n].value;
//         }
//     }
//     return roles;
// }

function arrRoles(sel) {
    let roles = [];
    let formS = document.getElementById(sel)
    for (let n = 0; n < formS.size; n++) {
        if (formS.options[n].selected) {
            roles.push(formS.options[n].value);
        }
    }
    return roles;
}

//-------------NEW USER-------------------
$(document).on('submit', '#userFormAdd', function (e) {
    e.preventDefault();
    let user = {};
    let formData = new FormData(e.target);
    formData.forEach((value, key) => user[key] = value);

    user.roles = arrRoles('rolesInputAdd');

    $.ajax({
        url: '/api/users',
        contentType: 'application/json',
        data: JSON.stringify(user),
        type: 'POST',
        success: function (newUsr) {
            let tableRef = document.getElementById("data");
            let newRow = tableRef.insertRow(tableRef.rows.length);
            newRow.id = "user-" + newUsr.id;
            newRow.innerHTML = renderCol(newUsr);

            $('#home-tab').click();
            $('#userFormAdd')[0].reset();
        }
    });
});

//-------------EDIT-UPDATE----------------

function openEditModal(id) {
    let row = document.getElementById('user-' + id);
    let tds = row.getElementsByTagName("td");

    $('#idInput').val(tds[0].innerHTML);
    $('#nameInput').val(tds[1].innerHTML);
    $('#usernameInput').val(tds[2].innerHTML);
    $('#emailInput').val(tds[3].innerHTML);
    $('#passInput').val('');

    let sel = document.getElementById("rolesEdit");
    let selRls = tds[4].innerHTML;
    for (let i = 0; i < sel.size; i++) {
        sel.options[i].selected = selRls.includes(sel.options[i].text);
    }
    $('#editUserModalRESTIndex').modal();
}

$(document).on('submit', '#userFormEdit', function (e) {
    e.preventDefault();
    let user = {};
    let formData = new FormData(e.target);
    formData.forEach((value, key) => user[key] = value);

    user.roles = arrRoles('rolesEdit');

    $.ajax({
        url: '/api/users',
        contentType: 'application/json',
        data: JSON.stringify(user),
        type: 'PUT',
        success: function () {
            let row = document.getElementById('user-' + user.id);
            let tds = row.getElementsByTagName("td");
            tds[1].innerHTML = user.name;
            tds[2].innerHTML = user.username;
            tds[3].innerHTML = user.email;
            tds[4].innerHTML = '[' + strRoles(user.roles) + ']';
            $('#userFormEdit')[0].reset();

        }
    });
    $('#editUserModalRESTIndex').modal('hide');
});

//-------------DELETE-----------------
let userIdToDel = null;

function openDeleteModal(id) {
    userIdToDel = id;
    let row = document.getElementById('user-' + id);
    let tds = row.getElementsByTagName("td");

    $('#idDel').val(tds[0].innerHTML);
    $('#nameDel').val(tds[1].innerHTML);
    $('#usernameDel').val(tds[2].innerHTML);
    $('#emailDel').val(tds[3].innerHTML);

    let sel = document.getElementById("rolesDel");
    let selRls = tds[4].innerHTML;
    for (let i = 0; i < sel.size; i++) {
        sel.options[i].selected = selRls.includes(sel.options[i].text);
    }
    $('#deleteUserModalRESTIndex').modal();
}

$(document).on('submit', '#userFormDelete', function (e) {
    e.preventDefault();
    $.ajax({
        url: '/api/users/' + userIdToDel,
        contentType: 'application/json',
        type: 'DELETE',
        success: function () {
            $('#userFormDelete')[0].reset();
            $(`#${"user-" + userIdToDel}`).remove();
            userIdToDel = null;
        }
    });
    $('#deleteUserModalRESTIndex').modal('hide');
});
