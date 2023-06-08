<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div class="container">
    <div>
        <a href="/oauth2/authorization/facebook" type="submit">Đăng nhập với Facebook</a>
    </div>
    <br/>
    <form class="login-form" action="/login" method="POST">
        <h2>Đăng nhập</h2>
        <input type="text" name="username" placeholder="Tài khoản" required>
        <input type="password" name="password" placeholder="Mật khẩu" required>
        <button type="submit">Đăng nhập</button>
    </form>
</div>
</body>
</html>