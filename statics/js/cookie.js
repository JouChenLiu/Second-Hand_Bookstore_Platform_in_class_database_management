function eraseCookie(name) {
    document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
function deleteCookies() {
    Cookies.remove('type');
    Cookies.remove('id');

}
function setCookie(cname, cvalue, exdays) {
    const d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    let expires = "expires="+ d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}
$(document).ready(function(){
    var navbar = '';
    //if want to get user id, call getCookie("id")
    if(getCookie("type") == 2){ //admin
        navbar = `<li><a class="dropdown-item" href="personal.html">個人資訊</a></li>
        <li><hr class="dropdown-divider"></li>
        <li><a class="dropdown-item" href="adminList.html">管理員清單</a></li>
        <li><a class="dropdown-item" href="createAdmin.html">新增管理員</a></li>
        <li><a class="dropdown-item" href="editAdminPassword.html">修改密碼</a></li>
        <li><a class="dropdown-item" href="verifyUser.html">驗證使用者</a></li>
        <li><hr class="dropdown-divider"></li>
        <li><a id="logout" class="dropdown-item" href="#">登出</a></li>`;
    }
    else if(getCookie("type") == 1){ //user
        navbar = `<li><a class="dropdown-item" href="personal.html">個人資訊</a></li>
        <li><a class="dropdown-item" href="order.html">訂單資訊</a></li>
        <li><a class="dropdown-item" href="bargainlist.html">議價資訊</a></li>
        <li><a class="dropdown-item" href="createProduct.html">新增商品</a></li>
        <li><hr class="dropdown-divider"></li>
        <li><a id="logout" class="dropdown-item" href="#">登出</a></li>`;
    }
    else if(getCookie("type") == 3){ //not verified user
        navbar = `<li><a class="dropdown-item" href="personal.html">個人資訊</a></li>
        <li><a class="dropdown-item" href="verify.html">驗證信箱</a></li>
        <li><hr class="dropdown-divider"></li>
        <li><a id="logout" class="dropdown-item" href="#">登出</a></li>`;
    }
    else{ // not login
        navbar = `<li><a class="dropdown-item" href="register.html">註冊</a></li>
        <li><a class="dropdown-item" href="login.html">登入</a></li>`;
    }
    $('.dropdown-menu').html(navbar);
    $('header').on('click', '#logout', function(){
        deleteCookies();
        location.href = "product.html";
    })
});
